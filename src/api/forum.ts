// import request from '@/utils/request';
// import type { User } from '@/types/user';

// export interface ApiResponse<T> {
//   code: number;
//   message: string;
//   data: T;
// }

// export interface ForumComment {
//   id: number;
//   postId: number;
//   userId: number;
//   content: string;
//   createdTime: string;
//   updatedTime: string;
//   user?: User;
// }

// // API 方法
// export const getComments = (postId: number) => {
//   return request.get<ApiResponse<ForumComment[]>>(`/api/forum/posts/${postId}/comments/list`);
// };

// export const addComment = (postId: number, content: string) => {
//   return request.post<ApiResponse<ForumComment[]>>(`/api/forum/posts/${postId}/comments/add`, { content });
// }; 

export const forumApi = {
  // ... 其他方法

  // 获取评论列表
  getComments: (postId: number) => 
    request.get<ApiResponse<Comment[]>>(`/api/forum/posts/${postId}/comments`),

  // 添加评论
  addComment: (postId: number, content: string) =>
    request.post<ApiResponse<Comment[]>>(`/api/forum/posts/${postId}/comments/add`, { content }),

  // 删除评论
  deleteComment: (postId: number, commentId: number) =>
    request.delete<ApiResponse<void>>(`/api/forum/posts/${postId}/comments/${commentId}`)
} 