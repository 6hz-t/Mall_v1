// 全局请求封装
export function request(params) {
    // 发起请求
    return new Promise((resolve, reject) => {
      uni.request({
        header: {
          Authorization: 'Basic@' + uni.getStorageSync('Token')
        },
        url:  params.url,
        data: params.data,
        method: params.method === undefined ? 'POST' : params.method,
        success: (res) => {
          const responseData = res.data

          // 请求成功
          if (responseData.code === 200) {
            resolve(responseData)
          }
		  
          if (responseData.code === 403) {
            uni.removeStorageSync('expiresTimeStamp')
            uni.removeStorageSync('loginResult')
            uni.removeStorageSync('Token')
			const pages = getCurrentPages()
			// 登录后的回跳地址
			if (pages[pages.length - 1].route.indexOf('user-login') === -1) {
			  uni.setStorageSync('routeUrlAfterLogin', pages[pages.length - 1].$page.fullPath)
			}
          
            uni.showModal({
                title: '提示',
                content: uni.getStorageSync('hadLogin') ? '登录已过期，请重新登陆！' : '请先进行登录！',
                cancelText: '取消',
                confirmText: '确定',
                success: res => {
                  if (res.confirm) {
                    uni.redirectTo({
                      url: '/pages/login/index'
                    })
                  } else {
                      uni.navigateTo({
                        url: '/pages/index/index'
                      })
                  }
                }
              })
            
            resolve(responseData)
          }


          // 其他异常码
          if (responseData.code === 500 || responseData.code === 1) {
              uni.showToast({
                title: responseData.message || responseData.data || 'Error',
                icon: 'none'
              })
          }
        },
        fail: (res) => {
          uni.showToast({
            title: '请求失败'
          })
          reject(res)
        }
      })
    })
}

