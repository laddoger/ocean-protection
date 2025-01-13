import { defineStore } from 'pinia'
import { userApi } from '@/api/user'

export const useUserStore = defineStore('user', {
    state: () => ({
        userInfo: null,
        token: localStorage.getItem('token') || ''
    }),
    actions: {
        async updateProfile(profileData: any) {
            try {
                const response = await userApi.updateProfile(profileData)
                if (response.data.code === 200) {
                    await this.loadUserInfo()  // 更新后重新加载用户信息
                    return response.data
                }
                throw new Error(response.data.message || '更新失败')
            } catch (error) {
                console.error('Update profile error:', error)
                throw error
            }
        },

        async updateUserInfo(userInfo: any) {
            try {
                const response = await userApi.updateUserInfo(userInfo)
                if (response.data.code === 200) {
                    await this.loadUserInfo()  // 更新后重新加载用户信息
                    return response.data
                }
                throw new Error(response.data.message || '更新失败')
            } catch (error) {
                console.error('Update user info error:', error)
                throw error
            }
        }
    }
}) 