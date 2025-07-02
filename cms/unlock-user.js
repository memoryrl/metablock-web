import { fileURLToPath } from 'node:url'
import path from 'path'
import payload from 'payload'

// 환경 변수 설정
process.env.PAYLOAD_SECRET = '0123456789abcdefghijklmnopqrstuvwxyzABCD'
process.env.MONGODB_URI = 'mongodb://localhost:27017/metablock-cms'
process.env.PAYLOAD_PUBLIC_SERVER_URL = 'http://localhost:3001'

const filename = fileURLToPath(import.meta.url)
const dirname = path.dirname(filename)

const start = async () => {
  try {
    console.log('🔓 사용자 계정 잠금 해제 시작...')

    // Initialize Payload
    await payload.init({
      config: path.resolve(dirname, './src/payload.config.ts'),
      secret: process.env.PAYLOAD_SECRET,
      local: true,
    })

    // 사용자 찾기
    const users = await payload.find({
      collection: 'users',
      depth: 0,
      where: {
        email: {
          equals: 'admin@metablock.com'
        }
      }
    })

    if (users.docs.length === 0) {
      console.log('❌ admin@metablock.com 계정을 찾을 수 없습니다.')
      return
    }

    const user = users.docs[0]
    console.log('✅ 사용자 찾음:', user.email)

    // 잠금 해제를 위해 로그인 시도 횟수 초기화
    await payload.update({
      collection: 'users',
      id: user.id,
      data: {
        lockUntil: null,
        loginAttempts: 0
      },
      overrideAccess: true
    })

    console.log('✅ 사용자 계정 잠금이 해제되었습니다.')
    console.log('🔑 이제 admin@metablock.com / admin123으로 로그인할 수 있습니다.')

    process.exit(0)
  } catch (error) {
    console.error('❌ 잠금 해제 에러:', error)
    process.exit(1)
  }
}

start() 