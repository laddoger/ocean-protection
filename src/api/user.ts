import request from '@/utils/request'
import type { User } from '@/types/user'

interface RegisterResponse {
  code: number
  message: string
  data: User
}

export const userApi = {
  register: async (data: { username: string; password: string }) => {
    try {
      const response = await request.post<RegisterResponse>('/api/auth/register', data)
      if (response.data.code === 200) {
        return response.data.data
      }
      throw new Error(response.data.message || '注册失败')
    } catch (error) {
      console.error('Registration failed:', error)
      throw error
    }
  },
    
  login: async (data: { username: string; password: string }) => {
    const response = await request.post<RegisterResponse>('/api/auth/login', data)
    if (response.data.code === 200) {
      return response.data.data
    }
    throw new Error(response.data.message || '登录失败')
  }
} 