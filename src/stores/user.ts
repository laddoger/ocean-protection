import { defineStore } from 'pinia'
import { userApi } from '@/api/user'
import type { User } from '@/types/user'

export const useUserStore = defineStore('user', {
  state: () => ({
    user: null as User | null,
    token: localStorage.getItem('token') || ''
  }),

  actions: {
    async register(data: { username: string; password: string }) {
      try {
        const user = await userApi.register(data)
        return user
      } catch (error) {
        console.error('Store register error:', error)
        throw error
      }
    },

    async login(data: { username: string; password: string }) {
      const response = await userApi.login(data)
      this.user = response.user
      this.token = response.token
      localStorage.setItem('token', response.token)
      return response
    }
  }
}) 