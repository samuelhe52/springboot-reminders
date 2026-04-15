import axios from "axios";
import { ElNotification } from "element-plus";

const ERROR_MAP = {
  "Username already exists": "该用户名已被注册",
  "Invalid username or password": "用户名或密码错误",
  "Username and password are required": "请填写用户名和密码",
  "Server error": "服务器错误，请稍后重试",
};

const http = axios.create({
  baseURL: "/api",
  withCredentials: true,
});

http.interceptors.response.use(
  (response) => response,
  async (error) => {
    const status = error.response?.status;
    const raw = error.response?.data?.error || "Request failed";
    const message = ERROR_MAP[raw] ?? raw;

    ElNotification({ message, type: "error", position: "bottom-right", duration: 3000 });

    if (status === 401 && window.location.pathname !== "/auth") {
      window.location.href = "/auth";
    }

    return Promise.reject(error);
  },
);

export default http;
