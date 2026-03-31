<template>
	<view class="container">
	  <view class="order-detail">
	    <view
	      v-if="deliveryAddressWebEntity"
	      class="delivery-addr"
	    >
	      <view class="user-info">
	        <text class="item">
	          {{ deliveryAddressWebEntity.receiverName }}
	        </text>
	        <text class="item">
	          {{ deliveryAddressWebEntity.receiverPhone }}
	        </text>
	      </view>
	      <view class="addr">
	        {{ deliveryAddressWebEntity.province }}{{ deliveryAddressWebEntity.city }}{{ deliveryAddressWebEntity.district }}{{ deliveryAddressWebEntity.detailAddress }}
	      </view>
	    </view>
	
	    <!-- 商品信息 -->
	    <view
	      v-if="orderTradeItemList"
	      class="prod-item"
	    >
	      <block
	        v-for="(item, index) in orderTradeItemList"
	        :key="index"
	      >
	        <view
	          class="item-cont"
	          :data-productId="item.productId"
	          @tap="toProductPage"
	        >
	          <view class="prod-pic">
	            <image :src="item.coverUrl" />
	          </view>
	          <view class="prod-info">
	            <view class="prodname">
	              {{ item.productName }}
	            </view>
	            <view class="prod-info-cont">
	              <text class="number">
	                数量：{{ item.quantity }}
	              </text>
	              <text class="info-item">
	                {{ item.model }}
	              </text>
	            </view>
	            <view class="price-nums clearfix">
	              <text class="prodprice">
	                <text class="symbol">
	                  ￥
	                </text>
	                <text class="big-num">
	                  {{ item.price}}
	                </text>
	              </text>
	              <view class="btn-box" />
	            </view>
	          </view>
	        </view>
	      </block>
	    </view>
	
	    <!-- 订单信息 -->
	    <view class="order-msg">
	      <view class="msg-item">
	        <view class="item">
	          <text class="item-tit">
	            订单编号：
	          </text>
	          <text class="item-txt">
	            {{ tradeCode }}
	          </text>
	        </view>
	        <view class="item">
	          <text class="item-tit">
	            下单时间：
	          </text>
	          <text class="item-txt">
	            {{ orderTime }}
	          </text>
	        </view>
	      </view>
	      <view class="msg-item">
	        <view class="item">
	          <text class="item-tit">
	            支付方式：
	          </text>
	          <text class="item-txt">
	            微信支付
	          </text>
	        </view>
	        <view class="item">
	          <text class="item-tit">
	            配送方式：
	          </text>
	          <text class="item-txt">
	            普通配送
	          </text>
	        </view>
	        <view class="item">
	          <text
	            v-if="!!remark"
	            class="item-tit"
	          >
	            订单备注：
	          </text>
	          <text class="item-txt remarks">
	            {{ remark }}
	          </text>
	        </view>
	      </view>
	    </view>
	
	    <view class="order-msg">
	      <view class="msg-item">
	        <view class="item">
	          <view class="item-tit">
	            订单总额：
	          </view>
	          <view class="item-txt price">
	            <text class="symbol">
	              ￥
	            </text>
	            <text class="big-num">
	              {{ totalMoney }}
	            </text>
	          </view>
	        </view>
	        <view class="item">
	          <view class="item-tit">
	            优惠券：
	          </view>
	          <view class="item-txt price">
	            <text class="symbol">
	              -￥
	            </text>
	            <text class="big-num">
	              {{ subtractMoney }}
	            </text>
	          </view>
	        </view>
	        <view class="item payment">
	          <view class="item-txt price">
	            实付款：
	            <text class="symbol">
	              ￥
	            </text>
	            <text class="big-num">
	              {{ finalMoney }}
	            </text>
	          </view>
	        </view>
	      </view>
	    </view>
	
	    <!-- 底部栏 -->
	    <view
	      v-if="orderStatus==5||orderStatus==6"
	      class="order-detail-footer"
	    >
	      <text
	        v-if="orderStatus==5||orderStatus==6"
	        class="dele-order"
	        @tap="deleteOrderTrade"
	      >
	        删除订单
	      </text>
	    </view>
	  </view>
	</view>
</template>

<script>
	import { request } from '@/utils/http'
	export default {
		onLoad(param) {
		  this.tradeCode = param.tradeCode		
		  this.getOrderDetail(this.tradeCode)		
		},	
		data() {
			return {
				tradeCode: '',
				remark: '',
				orderTime: '',
				orderStatus: 0,
				deliveryAddressWebEntity: {},
				orderTradeItemList: [],
				tradeCouponWebEntity: {},
				totalMoney: 0,
				finalMoney: 0,
				subtractMoney: 0,
				totalCount: 0
			}
		},
		methods: {
			getOrderDetail(tradeCode) {
				if(tradeCode) {
					request({
						  url: this.$host + '/v1/web/trade/getDetail/'+tradeCode,
						  method: 'GET',
						  data: {}
					})
					.then(({ data }) => {
						 this.orderTradeItemList = data.orderTradeItemList
						 this.deliveryAddressWebEntity = data.deliveryAddressWebEntity
						 this.tradeCouponWebEntity = data.tradeCouponWebEntity
						 this.tradeCode = data.code
						 this.orderStatus = data.orderStatus
						 this.orderTime = data.orderTime
						 this.remark = data.remark
						 this.totalMoney = data.totalMoney
						 this.finalMoney = data.finalMoney
						 this.subtractMoney = data.subtractMoney
						 this.totalCount = data.totalCount
					 })	
					uni.hideLoading()
				}
			},
			toProductPage(e) {
				const productId = e.currentTarget.dataset.productId
				if (productId) {
				  uni.navigateTo({
					url: '/pages/product-detail/product-detail?productId=' + productId
				  })
				}
			},
			toOrderListPage() {
				uni.navigateTo({
					url: '/pages/order-list/order-list?orderStatus=1'
				})  
			},
			deleteOrderTrade() {
				let that = this
				uni.showModal({
				  title: '',
				  content: '你确定要删除此订单？',
				  confirmColor: '#3e62ad',
				  cancelColor: '#3e62ad',
				  cancelText: '否',
				  confirmText: '是',
				  success (res) {
				    if (res.confirm) {
						   request({
						     url: that.$host + '/v1/web/trade/delete',
						     method: 'POST',
						     data: {
						   	   tradeCode: that.tradeCode,
						     }
						   })
						   .then(({ data }) => {
								that.currentPageNo = 0
								that.toOrderListPage()
						    })	
						}
					}
				})		
			}
		}
	}
</script>

<style scoped lang="scss">
@import './order-detail.scss';
</style>
