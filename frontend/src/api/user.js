import http from './http'

export const registerApi = (payload) => http.post('/user/register', payload)
export const loginApi = (payload) => http.post('/user/login', payload)
export const logoutApi = () => http.get('/user/logout')
