import http from "./http";

export const addTodoApi = (payload) => http.post("/todo/add", payload);
export const listTodoApi = (params) => http.get("/todo/list", { params });
export const updateTodoApi = (payload) => http.post("/todo/update", payload);
export const deleteTodoApi = (payload) => http.post("/todo/delete", payload);
