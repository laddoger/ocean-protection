import { defineStore } from 'pinia'
import { login as loginApi, getUserInfo } from '@/api/user'

export const useUserStore = defineStore('user', {
    state: () => ({
        token: localStorage.getItem('token') || '',
        userInfo: null
    }),
    actions: {
        async login(loginForm: { username: string; password: string }) {
            try {
                const response = await loginApi(loginForm)
                const { token } = response.data.data
                localStorage.setItem('token', token)
                this.token = token
                await this.loadUserInfo()
                return response
            } catch (error) {
                console.error('Login error:', error)
                this.logout()
                throw error
            }
        },
        async loadUserInfo() {
            try {
                console.log('Loading user info...')
                const response = await getUserInfo()
                this.userInfo = response.data.data
            } catch (error) {
                console.error('Get user info error:', error)
                throw error
            }
        },
        logout() {
            console.log('Logging out...')
            localStorage.removeItem('token')
            this.token = ''
            this.userInfo = null
        }
    }
}) 