// import request from '@/utils/request'
// import type { Organization, Activity, CreateOrgForm } from '@/types/volunteer'

// export const volunteerApi = {
//   // 组织相关
//   getOrganizations: () => 
//     request.get<Organization[]>('/api/volunteer/organizations'),
  
//   searchOrganizations: (keyword: string) => 
//     request.get<Organization[]>('/api/volunteer/organizations/search', {
//       params: { keyword }
//     }),
  
//   createOrganization: (data: CreateOrgForm) => 
//     request.post<Organization>('/api/volunteer/organizations', data),
  
//   deleteOrganization: (id: number) => 
//     request.delete<void>(`/api/volunteer/organizations/${id}`),
  
//   joinOrganization: (id: number) => 
//     request.post<void>(`/api/volunteer/organizations/${id}/join`),
  
//   leaveOrganization: (id: number) => 
//     request.post<void>(`/api/volunteer/organizations/${id}/leave`),

//   // 活动相关
//   getOngoingActivities: () => 
//     request.get<Activity[]>('/api/volunteer/activities/ongoing'),
  
//   getFinishedActivities: () => 
//     request.get<Activity[]>('/api/volunteer/activities/finished'),
  
//   searchActivities: (keyword: string) => 
//     request.get<Activity[]>('/api/volunteer/activities/search', {
//       params: { keyword }
//     }),
  
//   getActivityDetail: (id: number) => 
//     request.get<Activity>(`/api/volunteer/activities/${id}`),
  
//   joinActivity: (id: number) => 
//     request.post<void>(`/api/volunteer/activities/${id}/join`),
  
//   leaveActivity: (id: number) => 
//     request.post<void>(`/api/volunteer/activities/${id}/leave`)
// } 