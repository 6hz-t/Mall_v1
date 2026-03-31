import request from '@/utils/request'

export function getPage(params) {
  const data = params
  return request({
    url: '/v1/brand/searchByPage',
    method: 'post',
    data
  })
}

export function add(data) {
  return request({
    url: 'v1/brand/insert',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'v1/brand/deleteByIds',
    method: 'post',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'v1/brand/update',
    method: 'post',
    data
  })
}

export default { getPage, add, edit, del }
