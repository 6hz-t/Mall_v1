import request from '@/utils/request'

export function getPage(params) {
  const data = params
  return request({
    url: '/v1/productComment/searchByPage',
    method: 'post',
    data
  })
}

export function add(data) {
  return request({
    url: 'v1/productComment/insert',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'v1/productComment/deleteByIds',
    method: 'post',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'v1/productComment/update',
    method: 'post',
    data
  })
}

export default { add, edit, del, getPage }
