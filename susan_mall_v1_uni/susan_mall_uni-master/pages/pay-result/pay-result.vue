<template>
	<view class="container">
	  <view v-if="payStatus == 4">
	    <view class="pay-sts fail">
	      支付失败
	    </view>
	    <view class="tips">
	      请在
	      <text class="warn">
	        30分钟
	      </text>内完成付款
	    </view>
	    <view class="tips">
	      否则订单会被系统取消
	    </view>
	    <view class="btns">
	      <text
	        class="button checkorder"
	        @tap="toOrderListPage"
	      >
	        查看订单
	      </text>
	      <text
	        class="button payagain"
	        @tap="payAgain"
	      >
	        重新支付
	      </text>
	    </view>
	  </view>
	
	  <view v-if="payStatus == 2">
	    <view class="pay-sts succ">
	      支付成功
	    </view>
	    <view class="tips">
	      感谢您的购买
	    </view>
	    <view class="btns">
	      <text
	        class="button checkorder"
	        @tap="toOrderListPage"
	      >
	        查看订单
	      </text>
	      <text
	        class="button shopcontinue"
	        @tap="toIndexPage"
	      >
	        继续购物
	      </text>
	    </view>
	  </view>
	</view>
</template>

<script>
	export default {
		onLoad(param){
			this.initPayStatus(param)
		},
		data() {
			return {
				payStatus: 0,
				tradeCode: ''
			}
		},
		methods: {
			initPayStatus(param) {
			   this.payStatus = param.payStatus
			   this.tradeCode = param.tradeCode	
			},
			toOrderListPage() {
				uni.navigateTo({
					url: '/pages/order-list/order-list?orderStatus=1'
				})  
			},
			payAgain() {
				
			},
			toIndexPage() {
				uni.switchTab({
					url: '/pages/index/index'
				})  
			}
			
		}
	}
</script>

<style scoped lang="scss">
@import './pay-result.scss';
</style>

