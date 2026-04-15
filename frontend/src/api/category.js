import http from "./http";

export const addCategoryApi = (payload) => http.post("/category/add", payload);
export const listCategoryApi = () => http.get("/category/list");
export const updateCategoryApi = (payload) =>
  http.post("/category/update", payload);
export const deleteCategoryApi = (payload) =>
  http.post("/category/delete", payload);
