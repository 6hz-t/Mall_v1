import request from '@/utils/request'

export function getPage(params) {
  const data = params
  return request({
    url: '/v1/couponUserReceive/searchByPage',
    method: 'post',
    data
  })
}

export function add(data) {
  return request({
    url: 'v1/couponUserReceive/insert',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'v1/couponUserReceive/deleteByIds',
    method: 'post',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'v1/couponUserReceive/update',
    method: 'post',
    data
  })
}

export default { add, edit, del, getPage }
