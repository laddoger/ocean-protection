import axios from 'axios';

const request = axios.create({
  baseURL: '/api',
  timeout: 5000
});

// 请求拦截器
request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  (response) => {
    console.log('Response:', response.data)
    return response
  },
  (error) => {
    console.error('Response error:', error.response || error)
    return Promise.reject(error)
  }
)

export default request; 