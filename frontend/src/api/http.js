import axios from 'axios'
import {ElMessage} from 'element-plus'

const http = axios.create({
  baseURL: '/api',
  withCredentials: true
})

http.interceptors.response.use(
  (response) => response,
  async (error) => {
    const status = error.response?.status
    const message = error.response?.data?.error || 'Request failed'

    ElMessage.error(message)

    if (status === 401 && window.location.pathname !== '/auth') {
      window.location.href = '/auth'
    }

    return Promise.reject(error)
  }
)

export default http
