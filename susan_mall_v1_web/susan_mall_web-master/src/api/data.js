import request from '@/utils/request'

export function initData(url, params) {
  const data = params
  return request({
    url: url,
    method: 'post',
    data
  })
}

export function download(url, params) {
  const data = params
  return request({
    url: url,
    method: 'post',
    responseType: 'blob',
    data
  })
}
