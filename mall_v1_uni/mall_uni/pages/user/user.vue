<template>
	<view class="container">
	  <!-- 用户信息 -->
	  <view
	    v-if="isAuthInfo"
	    class="userinfo"
	  >
	    <view class="userinfo-con" v-if="userWebEntity">
	      <view class="userinfo-avatar">
	        <image
			  v-if="userWebEntity.avatarUrl"
	          :src="userWebEntity.avatarUrl"
	        />
	      </view>
	      <view class="userinfo-name">
	        <view>{{ userWebEntity.nickName ? userWebEntity.nickName : "用户昵称" }}</view>
	      </view>
	    </view>
	  </view>
	
	  <view
	    v-if="!isAuthInfo"
	    class="userinfo-none"
	  >
	    <view
	      class="default-pic"
	      @tap="toLoginPage"
	    >
	      <image src="@/static/images/icon/head04.png" />
	    </view>
	    <view
	      class="none-login"
	      @tap="toLoginPage"
	    >
	      <button class="unlogin">
	        未登录
	      </button>
	      <button class="click-login">
	        点击登录账号
	      </button>
	    </view>
	  </view>
	  <!-- end 用户信息 -->
	
	  <view class="list-cont">
	    <!-- 订单状态 -->
	    <view class="total-order">
	      <view class="order-tit">
	        <text style="font-weight:bold">
	          我的订单
	        </text>
	        <view
	          class="checkmore"
	          data-sts="0"
	          @tap="toOrderListPage"
	        >
	          <text>查看全部</text>
	          <text class="arrowhead" />
	        </view>
	      </view>
	      <view class="procedure">
	        <view
	          class="items"
	          data-sts="1"
	          @tap="toOrderListPage"
	        >
	          <image src="@/static/images/icon/toPay.png" />
	          <text>待支付</text>
	          <text
	            v-if="orderTradeEntity.waitPayCount>0"
	            class="num-badge"
	          >
	            {{ orderTradeEntity.waitPayCount }}
	          </text>
	        </view>
	        <view
	          class="items"
	          data-sts="2"
			  v-if="orderTradeEntity"
	          @tap="toOrderListPage"
	        >
	          <image src="@/static/images/icon/toDelivery.png" />
	          <text>待发货</text>
	          <text
	            v-if="orderTradeEntity.payCount>0"
	            class="num-badge"
	          >
	            {{ orderTradeEntity.payCount }}
	          </text>
	        </view>
	        <view
	          class="items"
	          data-sts="3"
	          @tap="toOrderListPage"
	        >
	          <image src="@/static/images/icon/toTake.png" />
	          <text>待收货</text>
	          <text
	            v-if="orderTradeEntity.shippedCount>0"
	            class="num-badge"
	          >
	            {{ orderTradeEntity.shippedCount }}
	          </text>
	        </view>
	        <view
	          class="items"
	          data-sts="4"
	          @tap="toOrderListPage"
	        >
	          <image src="@/static/images/icon/toComment.png" />
	          <text>已完成</text>
			  <text
			    v-if="orderTradeEntity.finishCount>0"
			    class="num-badge"
			  >
			    {{ orderTradeEntity.finishCount }}
			  </text>
	        </view>
	      </view>
	    </view>
	    <!--end 订单状态 -->
	
	    <view class="prod-col">
	      <view
	        class="col-item"
	        @tap="handleCollection"
	      >
	        <view
	          v-if="loginResult"
	          class="num"
	        >
	          {{myCountStat.favoritesCount}}
	        </view>
	        <view
	          v-else
	          class="num"
	        >
	          --
	        </view>
	        <view class="tit">
	          我的收藏
	        </view>
	      </view>
	      <view
	        class="col-item"
	        @tap="handleMessage"
	      >
	        <view
	          v-if="loginResult"
	          class="num"
	        >
	          {{myCountStat.messageCount}}
	        </view>
	        <view
	          v-else
	          class="num"
	        >
	          --
	        </view>
	        <view class="tit">
	          我的消息
	        </view>
	      </view>
	      <view
	        class="col-item"
	        @tap="handleMessage"
	      >
	        <view
	          v-if="loginResult"
	          class="num"
	        >
	          {{myCountStat.viewRecordCount}}
	        </view>
	        <view
	          v-else
	          class="num"
	        >
	          --
	        </view>
	        <view class="tit">
	          我的足迹
	        </view>
	      </view>
	    </view>
	
	    <view class="my-menu">
	      <view
	        class="memu-item"
	        @tap="toCouponCenterPage"
	      >
	        <view class="i-name">
	          <image src="@/static/images/icon/getCoupon.png" />
	          <text>领券中心</text>
	        </view>
	        <view class="arrowhead" />
	      </view>
	      <view
	        class="memu-item"
	        @tap="toMyCouponPage"
	      >
	        <view class="i-name">
	          <image src="@/static/images/icon/myCoupon.png" />
	          <text>我的优惠券</text>
	        </view>
	        <view class="arrowhead" />
	      </view>
	      <view
	        class="memu-item"
	        @tap="toDeriveryAddressPage"
	      >
	        <view class="i-name">
	          <image src="@/static/images/icon/myAddr.png" />
	          <text>收货地址</text>
	        </view>
	        <view class="arrowhead" />
	      </view>
	    </view>
	    <!--end 列表项 -->
	
	    <view
	      v-if="isAuthInfo"
	      class="log-out"
	      @tap="doLogout"
	    >
	      <view class="log-out-n">
	        <text>退出登录</text>
	      </view>
	    </view>
	  </view>
	</view>
</template>

<script>
	import { request } from '@/utils/http'
	export default {
		onShow(param) {
			const token = uni.getStorageSync('Token')
			if(!token) {
				this.toLoginPage()
				return
			}
			this.initUserDetail()
			this.initUserOrderTradeCount()
			this.initMyCountStat()
		},
		data() {
			return {
				isAuthInfo: true,
				loginResult: true,
				userWebEntity: {},
				orderTradeEntity: {},
				myCountStat: {}
			}
		},
		methods: {
			initUserDetail() {
				uni.showLoading()
				request({
				  url: this.$host + '/v1/web/user/getUserDetail',
				  method: 'GET',
				  data: {}
				})
				.then(({ data }) => {
					 this.userWebEntity = data
					 uni.hideLoading()
				 })	
			},
			initUserOrderTradeCount() {
				uni.showLoading()
				request({
				  url: this.$host + '/v1/web/trade/getUserOrderTradeCount',
				  method: 'GET',
				  data: {}
				})
				.then(({ data }) => {
					 this.orderTradeEntity = data
					 uni.hideLoading()
				 })	
			},
			initMyCountStat() {
				uni.showLoading()
				request({
				  url: this.$host + '/v1/web/user/myCountStat',
				  method: 'GET',
				  data: {}
				})
				.then(({ data }) => {
					 this.myCountStat = data
					 uni.hideLoading()
				 })	
			},
			toLoginPage() {
				uni.redirectTo({
					url: '/pages/login/index'
				}) 
			},
			handleCollection() {
				
			},
			handleMessage() {
				
			},
			toCouponCenterPage() {
				uni.navigateTo({
					url: '/pages/coupon/coupon-center'
				}) 
			},
			toMyCouponPage() {
				uni.navigateTo({
					url: '/pages/coupon/coupon-center?type=user'
				}) 
			},
			toOrderListPage() {
				uni.navigateTo({
					url: '/pages/order-list/order-list'
				}) 
			},
			toDeriveryAddressPage() {
				uni.navigateTo({
					url: '/pages/delivery-address/delivery-address'
				}) 
			},
			doLogout() {
				request({
				  url: this.$host + '/v1/web/user/logout',
				  method: 'POST',
				  data: {}
				})
				.then(() => {
					uni.removeStorageSync('expiresTimeStamp')
					uni.removeStorageSync('loginResult')
					uni.removeStorageSync('Token')
					uni.showToast({
					  title: '退出成功',
					  icon: 'none'
					})
					setTimeout(() => {
					  uni.switchTab({
					    url: '/pages/index/index'
					  })
					}, 1200)
				 })	
			}
		}
	}
</script>

<style scoped lang="scss">
@import './user.scss';
</style>
