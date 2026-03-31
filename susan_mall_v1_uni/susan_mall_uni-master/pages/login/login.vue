<template>
  <view class="con">
    <image src="@/static/logo.png" />
    <!-- 登录 -->
    <view class="login-form">
      <view :class="['item',errorTips==1? 'error':'']">
        <view class="account">
          <text class="input-item">
            账号
          </text>
          <input
            type="text"
            data-type="account"
            placeholder-class="inp-palcehoder"
            placeholder="请输入用户名"
            @input="getInputVal"
          >
        </view>
        <view
          v-if="errorTips==1"
          class="error-text"
        >
          <text class="warning-icon">
            !
          </text>
          请输入账号！
        </view>
      </view>
      <view :class="['item',errorTips==2? 'error':'']">
        <view class="account">
          <text class="input-item">
            密码
          </text>
          <input
            type="password"
            data-type="password"
            placeholder-class="inp-palcehoder"
            placeholder="请输入密码"
            @input="getInputVal"
          >
        </view>
        <view
          v-if="errorTips==2"
          class="error-text"
        >
          <text class="warning-icon">
            !
          </text>
          请输入密码！
        </view>
      </view>
	  <view :class="['item',errorTips==3? 'error':'']">
	    <view class="account">
	      <text class="input-item" style="width: 150rpx;">
	        验证码
	      </text>
	      <input
	        type="code"
	        data-type="code"
	        placeholder-class="inp-palcehoder"
	        placeholder="请输入验证码"
	        @input="getInputVal"
	      >
		  <img :src="codeUrl" @click="getCode">
	    </view>
	    <view
	      v-if="errorTips==3"
	      class="error-text"
	    >
	      <text class="warning-icon">
	        !
	      </text>
	      请输入验证码！
	    </view>
	  </view>
      <view class="operate">
        <view
          class="to-register"
          @tap="toRegitser"
        >
          没有账号？
          <text>立即注册</text>
        </view>
      </view>
    </view>

    <view>
      <button
        class="authorized-btn"
        @tap="login"
      >
        登录
      </button>
      <button
        class="to-idx-btn"
        @tap="toIndex"
      >
        回到首页
      </button>
    </view>
  </view>
</template>

<script>
	import { encrypt } from '@/utils/rsaEncrypt'
	
	export default {
		data() {
			return {
				codeUrl: '',
				uuid: '',
				username: '',
				password: '',
				code: '',
				errorTips: 0,
			}
		},
		created() {
		   this.getCode()	
		},
		methods: {
			/**
			 * 回到首页
			 */
			toIndex() {
			  wx.switchTab({
			    url: '/pages/index/index'
			  })
			},
			getCode() {
				uni.request({
				  url: this.$host + '/v1/web/user/code',
				  method: 'get',
				  data: {
				  },
				  success: res => {
					 if(res && res.data) {
						 const data = res.data
						 if(data.code === 200) {
							const codeObj = data.data 
							this.codeUrl = codeObj.img
							this.uuid = codeObj.uuid
						 }
					 } 
				  },
				  fail: () => {},
				  complete: () => {}
				})
			},
			/**
			 * 输入框的值
			 */
			getInputVal(e){
			  const type = e.currentTarget.dataset.type
			  if (type == 'account') {
			    this.username = e.detail.value
			  } else if (type == 'password') {
			    this.password = e.detail.value
			  } else if (type == 'code') {
			    this.code = e.detail.value
			  }
			},
			/**
			 * 登录
			 */
			login() {
			  if (this.username.length == 0) {
			    this.errorTips = 1
			  } else if (this.password.length == 0) {
			    this.errorTips = 2
			  } else {
			    this.errorTips = 0
			    uni.request({
			      url: this.$host + '/v1/web/user/login',
			      method: 'post',
			      data: {
					uuid: this.uuid,  
					code: this.code,
			        username: this.username,
					password: encrypt(this.password)
			      },
				  success: res => {
					 if(res && res.data) {
						 const data = res.data
						 if(data.code === 200) {
							this.loginSuccess(data)
						 } else {
							 uni.showToast({
							   title: '登录失败',
							   content: data.message
							 })
						 }
					 } else {
						 uni.showToast({
						   title: '登录失败',
						 })
					 }
				  },
				  fail: () => {},
				  complete: () => {}
			    })
			  }
			},
			loginSuccess(data) {
				const result = data.data
		        // 保存登陆信息
		        wx.setStorageSync('loginResult', result)
		        // 保存成功登录标识,token过期判断
		        wx.setStorageSync('hadLogin', true)
		        const expiresTimeStamp = result.expiresIn * 1000 / 2 + new Date().getTime()
		        // 缓存token的过期时间
		        uni.setStorageSync('expiresTimeStamp', expiresTimeStamp)
		      
		        wx.setStorageSync('Token', result.token) // 把token存入缓存，请求接口数据时要用
			    uni.showToast({
			      title: '登录成功',
			      icon: 'none',
			      complete: () => {
			        setTimeout(() => {
			          wx.switchTab({
			            url: '/pages/index/index'
			          })
			        }, 1000)
			      }
			    })
			},
			/**
			 * 去注册
			 */
			toRegitser() {
			  uni.switchTab({
			    url: '/pages/register/register'
			  })
			},
		},
		
	}
</script>

<style scoped lang="scss">
@import "./login.scss";
</style>
