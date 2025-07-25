import type { Metadata } from 'next'
import type { EditConfig, SanitizedCollectionConfig, SanitizedGlobalConfig } from 'payload'

import type { GenerateViewMetadata } from '../Root/index.js'

import { getNextRequestI18n } from '../../utilities/getNextRequestI18n.js'
import { generateAPIViewMetadata } from '../API/metadata.js'
import { generateEditViewMetadata } from '../Edit/metadata.js'
import { generateNotFoundViewMetadata } from '../NotFound/metadata.js'
import { generateVersionViewMetadata } from '../Version/metadata.js'
import { generateVersionsViewMetadata } from '../Versions/metadata.js'
import { getDocumentView } from './getDocumentView.js'

export type GenerateEditViewMetadata = (
  args: {
    collectionConfig?: null | SanitizedCollectionConfig
    globalConfig?: null | SanitizedGlobalConfig
    view?: keyof EditConfig
  } & Parameters<GenerateViewMetadata>[0],
) => Promise<Metadata>

export const getMetaBySegment: GenerateEditViewMetadata = async ({
  collectionConfig,
  config,
  globalConfig,
  params,
}) => {
  const { segments } = params

  let fn: GenerateEditViewMetadata | null = null

  const [segmentOne] = segments
  const isCollection = segmentOne === 'collections'
  const isGlobal = segmentOne === 'globals'

  const isEditing =
    isGlobal || Boolean(isCollection && segments?.length > 2 && segments[2] !== 'create')

  if (isCollection) {
    // `/:collection/:id`
    if (params.segments.length === 3) {
      fn = generateEditViewMetadata
    }

    // `/:collection/:id/:view`
    if (params.segments.length === 4) {
      switch (params.segments[3]) {
        case 'api':
          // `/:collection/:id/api`
          fn = generateAPIViewMetadata
          break
        case 'versions':
          // `/:collection/:id/versions`
          fn = generateVersionsViewMetadata
          break
        default:
          break
      }
    }

    // `/:collection/:id/:slug-1/:slug-2`
    if (params.segments.length === 5) {
      switch (params.segments[3]) {
        case 'versions':
          // `/:collection/:id/versions/:version`
          fn = generateVersionViewMetadata
          break
        default:
          break
      }
    }
  }

  if (isGlobal) {
    // `/:global`
    if (params.segments?.length === 2) {
      fn = generateEditViewMetadata
    }

    // `/:global/:view`
    if (params.segments?.length === 3) {
      switch (params.segments[2]) {
        case 'api':
          // `/:global/api`
          fn = generateAPIViewMetadata
          break
        case 'versions':
          // `/:global/versions`
          fn = generateVersionsViewMetadata
          break
        default:
          break
      }
    }

    // `/:global/versions/:version`
    if (params.segments?.length === 4 && params.segments[2] === 'versions') {
      fn = generateVersionViewMetadata
    }
  }

  const i18n = await getNextRequestI18n({
    config,
  })

  if (typeof fn === 'function') {
    return fn({
      collectionConfig,
      config,
      globalConfig,
      i18n,
      isEditing,
    })
  } else {
    const { viewKey } = getDocumentView({
      collectionConfig,
      config,
      docPermissions: {
        create: true,
        delete: true,
        fields: true,
        read: true,
        readVersions: true,
        update: true,
      },
      globalConfig,
      routeSegments: typeof segments === 'string' ? [segments] : segments,
    })

    if (viewKey) {
      const customViewConfig =
        collectionConfig?.admin?.components?.views?.edit?.[viewKey] ||
        globalConfig?.admin?.components?.views?.edit?.[viewKey]

      if (customViewConfig) {
        return generateEditViewMetadata({
          collectionConfig,
          config,
          globalConfig,
          i18n,
          isEditing,
          view: viewKey as keyof EditConfig,
        })
      }
    }
  }

  return generateNotFoundViewMetadata({ config, i18n })
}
