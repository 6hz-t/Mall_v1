import request from '@/utils/request'

export function getPage(params) {
  const data = params
  return request({
    url: '/v1/productViewRecord/searchByPage',
    method: 'post',
    data
  })
}

export function add(data) {
  return request({
    url: 'v1/productViewRecord/insert',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'v1/productViewRecord/deleteByIds',
    method: 'post',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'v1/productViewRecord/update',
    method: 'post',
    data
  })
}

export default { add, edit, del, getPage }
