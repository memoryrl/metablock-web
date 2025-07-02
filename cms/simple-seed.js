import payload from 'payload'
import path from 'path'
import { fileURLToPath } from 'url'
import dotenv from 'dotenv'

// Load environment variables
dotenv.config({ path: './.env' })
process.env.PAYLOAD_SECRET = '0123456789abcdefghijklmnopqrstuvwxyzABCD'
process.env.DATABASE_URI = 'postgres://gimnamcheol:@localhost:5432/payload-cms-clean'
process.env.NEXT_PUBLIC_SERVER_URL = 'http://localhost:3001'
console.log('ENV PAYLOAD_SECRET:', process.env.PAYLOAD_SECRET ? 'loaded' : 'undefined')

const filename = fileURLToPath(import.meta.url)
const dirname = path.dirname(filename)

const start = async () => {
  try {
    console.log('🌱 시드 데이터 생성 시작...')

    // Initialize Payload
    await payload.init({
      config: path.resolve(dirname, './src/payload.config.ts'),
      secret: '0123456789abcdefghijklmnopqrstuvwxyzABCD',
      local: true,
    })

    // 기존 사용자 확인
    const existingUsers = await payload.find({
      collection: 'users',
      depth: 0,
    })

    let adminUser
    if (existingUsers.docs.length === 0) {
      // 관리자 계정 생성
      adminUser = await payload.create({
        collection: 'users',
        data: {
          email: 'admin@metablock.com',
          password: 'admin123',
          roles: ['admin'],
        },
      })
      console.log('✅ 관리자 계정 생성:', adminUser.email)
    } else {
      adminUser = existingUsers.docs[0]
      console.log('⚠️ 관리자 계정이 이미 존재합니다.')
    }

    // 추가 사용자 생성
    const additionalUsers = [
      {
        email: 'editor@metablock.com',
        password: 'editor123',
        roles: ['editor'],
      },
      {
        email: 'author@metablock.com',
        password: 'author123',
        roles: ['author'],
      },
    ]

    for (const userData of additionalUsers) {
      try {
        await payload.create({
          collection: 'users',
          data: userData,
        })
        console.log(`✅ 사용자 생성: ${userData.email}`)
      } catch (error) {
        console.log(`⚠️ 사용자 이미 존재하거나 오류: ${userData.email}`)
      }
    }

    // 카테고리 생성
    const categories = [
      { title: 'Technology', slug: 'technology' },
      { title: 'Development', slug: 'development' },
      { title: 'CMS', slug: 'cms' },
      { title: 'Web Development', slug: 'web-development' },
      { title: 'Mobile Development', slug: 'mobile-development' },
      { title: 'DevOps', slug: 'devops' },
      { title: 'Database', slug: 'database' },
      { title: 'Frontend', slug: 'frontend' },
      { title: 'Backend', slug: 'backend' },
      { title: 'AI & Machine Learning', slug: 'ai-machine-learning' },
    ]

    const createdCategories = []
    for (const categoryData of categories) {
      try {
        const category = await payload.create({
          collection: 'categories',
          data: categoryData,
        })
        createdCategories.push(category)
        console.log(`✅ 카테고리 생성: ${categoryData.title}`)
      } catch (error) {
        console.log(`⚠️ 카테고리 이미 존재하거나 오류: ${categoryData.title}`)
      }
    }

    // 포스트 생성
    const posts = [
      {
        title: '🚀 Payload CMS 3.44.0 완전 가이드',
        slug: 'payload-cms-complete-guide',
        _status: 'published',
        content: [
          {
            children: [
              {
                text: 'Payload CMS는 현대적인 헤드리스 CMS로, TypeScript로 작성되어 개발자 친화적입니다. 이 가이드에서는 Payload CMS의 모든 기능을 자세히 살펴보겠습니다.',
              },
            ],
          },
          {
            children: [
              {
                text: '주요 특징',
              },
            ],
            type: 'h2',
          },
          {
            children: [
              {
                text: '• TypeScript 지원\n• 자동 API 생성\n• 관리자 패널\n• 파일 업로드\n• 관계형 데이터\n• 버전 관리',
              },
            ],
          },
        ],
        categories: createdCategories.slice(0, 3).map(cat => cat.id),
        authors: [adminUser.id],
        meta: {
          title: 'Payload CMS 3.44.0 완전 가이드 - 모든 기능 살펴보기',
          description: 'Payload CMS의 모든 기능을 자세히 알아보는 완전한 가이드입니다.',
        },
      },
      {
        title: '💻 Spring Boot와 Payload CMS 연동 방법',
        slug: 'spring-boot-payload-integration',
        _status: 'published',
        content: [
          {
            children: [
              {
                text: 'Spring Boot 애플리케이션에서 Payload CMS를 연동하는 방법을 알아보겠습니다. REST API를 통해 데이터를 주고받는 방법을 단계별로 설명합니다.',
              },
            ],
          },
          {
            children: [
              {
                text: '연동 단계',
              },
            ],
            type: 'h2',
          },
          {
            children: [
              {
                text: '1. Payload CMS API 설정\n2. Spring Boot REST Client 구성\n3. 데이터 모델 매핑\n4. 인증 처리\n5. 에러 핸들링',
              },
            ],
          },
        ],
        categories: [createdCategories[1].id, createdCategories[8].id],
        authors: [adminUser.id],
        meta: {
          title: 'Spring Boot와 Payload CMS 연동 가이드',
          description: 'Spring Boot에서 Payload CMS API를 연동하는 방법을 단계별로 설명합니다.',
        },
      },
      {
        title: '🎯 PostgreSQL 데이터베이스 최적화 전략',
        slug: 'postgresql-optimization-strategies',
        _status: 'published',
        content: [
          {
            children: [
              {
                text: 'PostgreSQL 데이터베이스의 성능을 최적화하는 다양한 전략들을 알아보겠습니다. 인덱싱, 쿼리 최적화, 설정 튜닝 등을 다룹니다.',
              },
            ],
          },
          {
            children: [
              {
                text: '최적화 방법',
              },
            ],
            type: 'h2',
          },
          {
            children: [
              {
                text: '• 인덱스 최적화\n• 쿼리 튜닝\n• 설정 파라미터 조정\n• 파티셔닝\n• 연결 풀 관리',
              },
            ],
          },
        ],
        categories: [createdCategories[6].id, createdCategories[5].id],
        authors: [adminUser.id],
        meta: {
          title: 'PostgreSQL 데이터베이스 최적화 전략',
          description: 'PostgreSQL 성능을 향상시키는 다양한 최적화 전략을 알아봅니다.',
        },
      },
      {
        title: '⚡ Next.js 15와 Payload CMS로 현대적인 웹사이트 구축',
        slug: 'nextjs-15-payload-cms-website',
        _status: 'published',
        content: [
          {
            children: [
              {
                text: 'Next.js 15의 최신 기능들과 Payload CMS를 활용하여 현대적이고 빠른 웹사이트를 구축하는 방법을 알아보겠습니다.',
              },
            ],
          },
          {
            children: [
              {
                text: '주요 기능',
              },
            ],
            type: 'h2',
          },
          {
            children: [
              {
                text: '• App Router 활용\n• Server Components\n• ISR (Incremental Static Regeneration)\n• API Routes\n• TypeScript 통합',
              },
            ],
          },
        ],
        categories: [createdCategories[3].id, createdCategories[7].id],
        authors: [adminUser.id],
        meta: {
          title: 'Next.js 15와 Payload CMS로 현대적인 웹사이트 구축',
          description: 'Next.js 15와 Payload CMS를 활용한 현대적인 웹사이트 개발 가이드입니다.',
        },
      },
      {
        title: '🤖 AI와 머신러닝을 활용한 콘텐츠 관리',
        slug: 'ai-machine-learning-content-management',
        _status: 'published',
        content: [
          {
            children: [
              {
                text: 'AI와 머신러닝 기술을 활용하여 콘텐츠 관리 시스템을 향상시키는 방법들을 알아보겠습니다.',
              },
            ],
          },
          {
            children: [
              {
                text: 'AI 활용 분야',
              },
            ],
            type: 'h2',
          },
          {
            children: [
              {
                text: '• 자동 태그 생성\n• 콘텐츠 추천 시스템\n• 이미지 자동 분류\n• 텍스트 요약\n• 감정 분석',
              },
            ],
          },
        ],
        categories: [createdCategories[9].id, createdCategories[2].id],
        authors: [adminUser.id],
        meta: {
          title: 'AI와 머신러닝을 활용한 콘텐츠 관리',
          description: 'AI 기술을 활용하여 콘텐츠 관리 시스템을 향상시키는 방법을 알아봅니다.',
        },
      },
    ]

    for (const postData of posts) {
      try {
        await payload.create({
          collection: 'posts',
          data: postData,
        })
        console.log(`✅ 포스트 생성: ${postData.title}`)
      } catch (error) {
        console.log(`⚠️ 포스트 이미 존재하거나 오류: ${postData.title}`)
      }
    }

    // 페이지 생성
    const pages = [
      {
        title: '홈페이지',
        slug: 'home',
        _status: 'published',
        hero: {
          type: 'contentMedia',
          content: [
            {
              children: [
                {
                  text: 'MetaBlock CMS',
                },
              ],
              type: 'h1',
            },
            {
              children: [
                {
                  text: '현대적이고 강력한 콘텐츠 관리 시스템',
                },
              ],
            },
          ],
          media: null,
        },
        layout: [
          {
            blockType: 'content',
            content: [
              {
                children: [
                  {
                    text: 'MetaBlock CMS에 오신 것을 환영합니다',
                  },
                ],
                type: 'h2',
              },
              {
                children: [
                  {
                    text: 'MetaBlock CMS는 개발자 친화적이고 확장 가능한 헤드리스 CMS입니다. TypeScript로 작성되어 타입 안전성을 보장하며, 자동 API 생성, 관리자 패널, 파일 업로드 등 다양한 기능을 제공합니다.',
                  },
                ],
              },
            ],
          },
        ],
        meta: {
          title: 'MetaBlock CMS - 현대적인 콘텐츠 관리 시스템',
          description: '개발자 친화적이고 확장 가능한 헤드리스 CMS',
        },
      },
      {
        title: '서비스 소개',
        slug: 'services',
        _status: 'published',
        hero: {
          type: 'contentMedia',
          content: [
            {
              children: [
                {
                  text: '서비스 소개',
                },
              ],
              type: 'h1',
            },
            {
              children: [
                {
                  text: 'MetaBlock CMS가 제공하는 다양한 서비스들을 알아보세요',
                },
              ],
            },
          ],
          media: null,
        },
        layout: [
          {
            blockType: 'content',
            content: [
              {
                children: [
                  {
                    text: '주요 서비스',
                  },
                ],
                type: 'h2',
              },
              {
                children: [
                  {
                    text: '• 웹사이트 구축\n• CMS 개발\n• API 개발\n• 데이터베이스 설계\n• 성능 최적화\n• 유지보수',
                  },
                ],
              },
            ],
          },
        ],
        meta: {
          title: '서비스 소개 - MetaBlock CMS',
          description: 'MetaBlock CMS가 제공하는 다양한 서비스들을 소개합니다.',
        },
      },
      {
        title: '연락처',
        slug: 'contact',
        _status: 'published',
        hero: {
          type: 'contentMedia',
          content: [
            {
              children: [
                {
                  text: '연락처',
                },
              ],
              type: 'h1',
            },
            {
              children: [
                {
                  text: '문의사항이 있으시면 언제든 연락주세요',
                },
              ],
            },
          ],
          media: null,
        },
        layout: [
          {
            blockType: 'content',
            content: [
              {
                children: [
                  {
                    text: '연락처 정보',
                  },
                ],
                type: 'h2',
              },
              {
                children: [
                  {
                    text: '이메일: contact@metablock.com\n전화: 02-1234-5678\n주소: 서울특별시 강남구 테헤란로 123',
                  },
                ],
              },
            ],
          },
        ],
        meta: {
          title: '연락처 - MetaBlock CMS',
          description: 'MetaBlock CMS에 문의하실 수 있는 연락처 정보입니다.',
        },
      },
    ]

    for (const pageData of pages) {
      try {
        await payload.create({
          collection: 'pages',
          data: pageData,
        })
        console.log(`✅ 페이지 생성: ${pageData.title}`)
      } catch (error) {
        console.log(`⚠️ 페이지 이미 존재하거나 오류: ${pageData.title}`)
      }
    }

    console.log('🎉 시드 데이터 생성 완료!')
    console.log('🔑 관리자 로그인: admin@metablock.com / admin123')
    console.log('🔑 에디터 로그인: editor@metablock.com / editor123')
    console.log('🔑 작성자 로그인: author@metablock.com / author123')
    console.log('📊 생성된 데이터:')
    console.log('   - 사용자: 3명')
    console.log('   - 카테고리: 10개')
    console.log('   - 포스트: 5개')
    console.log('   - 페이지: 3개')
    
  } catch (error) {
    console.error('❌ 시드 에러:', error)
  } finally {
    process.exit(0)
  }
}

start() 