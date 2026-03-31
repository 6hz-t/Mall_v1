<template>
  <view class="container">

    <!-- 页面头部 -->
    <view class="header">
      <view class="title">
        <text>手机号登录</text>
      </view>
    </view>
    <!-- 表单 -->
    <view class="login-form">
      <!-- 手机号 -->
      <view class="form-item">
        <input class="form-item--input" type="number" v-model="phone" maxlength="11" placeholder="请输入手机号码" />
      </view>
      <!-- 图形验证码 -->
      <view class="form-item">
        <input class="form-item--input" type="text" v-model="captchaCode" maxlength="5" placeholder="请输入图形验证码" />
        <view class="form-item--parts">
          <view class="captcha" @click="getCaptcha()">
            <image class="image" :src="captcha"></image>
          </view>
        </view>
      </view>
      <!-- 短信验证码 -->
      <view class="form-item">
        <input class="form-item--input" type="number" v-model="smsCode" maxlength="6" placeholder="请输入短信验证码" />
        <view class="form-item--parts">
          <view class="captcha-sms" @click="handelSmsCaptcha()">
            <text v-if="!smsState" class="activate">获取验证码</text>
            <text v-else class="un-activate">重新发送({{ expireSecond }})秒</text>
          </view>
        </view>
      </view>
      <!-- 登录按钮 -->
      <view class="login-button"  @click="handleLogin()">
        <text>登录</text>
      </view>
    </view>
  </view>
</template>

<script>
  import * as Verify from '@/utils/verify'

  // 表单验证场景
  const GET_CAPTCHA = 10
  const SUBMIT_LOGIN = 20

  export default {
    components: {
    },

    props: {
      // 是否存在第三方用户信息
      isParty: {
        type: Boolean,
        default: () => false
      },
      // 第三方用户信息数据
      partyData: {
        type: Object
      },
    },

    data() {
      return {
		expireSecond: 60,
        // 正在加载
        isLoading: false,
        // 按钮禁用
        disabled: false,
        // 图形验证码信息
        captcha: '',
		captchaUuid: '',
        // 短信验证码发送状态
        smsState: false,
        // 手机号
        phone: '',
        // 图形验证码
        captchaCode: '',
        // 短信验证码
        smsCode: ''
      }
    },

    /**
     * 生命周期函数--监听页面加载
     */
    created() {
      // 获取图形验证码
      this.getCaptcha()
    },

    methods: {
      // 获取图形验证码
      getCaptcha() {
        const app = this
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
					if(codeObj) {
						this.captcha  = codeObj.img
						this.captchaUuid = codeObj.uuid
					}
		  		 }
		  	 } 
		    },
		    fail: () => {},
		    complete: () => {}
		  })
      },

      // 点击发送短信验证码
      handelSmsCaptcha() {
        const app = this
        if (!app.isLoading && !app.smsState && app.formValidation(GET_CAPTCHA)) {
          app.sendSmsCaptcha()
        }
      },

      // 表单验证
      formValidation(scene = GET_CAPTCHA) {
        const app = this
        // 验证获取短信验证码
        if (scene === GET_CAPTCHA) {
          if (!app.validteMobile(app.phone) || !app.validteCaptchaCode(app.captchaCode)) {
            return false
          }
        }
        // 验证提交登录
        if (scene === SUBMIT_LOGIN) {
          if (!app.validteMobile(app.phone) || !app.validteSmsCode(app.smsCode)) {
            return false
          }
        }
        return true
      },

      // 验证手机号
      validteMobile(str) {
        if (Verify.isEmpty(str)) {
		  uni.showToast({
		    title: '请先输入手机号',
		  })
          return false
        }
        if (!Verify.isMobile(str)) {
		  uni.showToast({
		    title: '请输入正确格式的手机号'
		  })
          return false
        }
        return true
      },

      // 验证图形验证码
      validteCaptchaCode(str) {
        if (Verify.isEmpty(str)) {
		  uni.showToast({
		    title: '请先输入图形验证码'
		  })
          return false
        }
        return true
      },

      // 验证短信验证码
      validteSmsCode(str) {
        if (Verify.isEmpty(str)) {
		  uni.showToast({
		    title: '请先输入短信验证码'
		  })
          return false
        }
        return true
      },

      // 请求发送短信验证码接口
      sendSmsCaptcha() {
        const app = this
        app.isLoading = true
		uni.request({
		  url: this.$host + '/v1/web/sms/sendSmsCode',
		  method: 'post',
		  data: {
			phone: app.phone,
			captchaUuid: app.captchaUuid,
			captchaCode: app.captchaCode,
			type: 2
		  },
		  success: res => {
			 if(res && res.data) {
				 const data = res.data
				 if(data.code === 200) {
					const codeObj = data.data 
					if(codeObj) {
						app.isLoading = false
						app.expireSecond = codeObj.expireSecond
						uni.showToast({
						  title: '短信发送成功'
						})
						app.timer()
					}
				 } else {
					app.isLoading = false
					if(data.message) {
						uni.showToast({
						  title: data.message
						}) 
					}  else {
						uni.showToast({
						  title: '短信发送失败'
						}) 
					}
					app.getCaptcha()
				 }
			 } 
		  },
		  fail: () => {},
		  complete: () => {}
		})
      },

      // 执行定时器
      timer() {
        const app = this
        app.smsState = true
        const inter = setInterval(() => {
		  const initExpireSecond = app.expireSecond
          app.expireSecond = app.expireSecond - 1
          if (app.expireSecond <= 0) {
            app.smsState = false
            app.expireSecond = initExpireSecond
            clearInterval(inter)
          }
        }, 1000)
      },

      // 点击登录
      handleLogin() {
        const app = this
        if (!app.isLoading && !app.disabled && app.formValidation(SUBMIT_LOGIN)) {
          app.submitLogin()
        }
      },

      // 确认登录
      submitLogin() {
		const app = this
		uni.request({
		  url: app.$host + '/v1/web/user/loginByPhone',
		  method: 'post',
		  data: {
			phone: app.phone,  
			smsCode: app.smsCode
		  },
		  success: res => {
			 if(res && res.data) {
				 const data = res.data
				 if(data.code === 200) {
					app.loginSuccess(data)
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
       * 登录成功-跳转回原页面
       */
      onNavigateBack(delta = 1) {
        const pages = getCurrentPages()
        if (pages.length > 1) {
          uni.navigateBack({
            delta: Number(delta || 1)
          })
        } else {
          this.$navTo('pages/index/index')
        }
      }

    }
  }
</script>

<style scoped lang="scss">
@import './main.scss';
</style>