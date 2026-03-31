<template>
	<view>
	  <view class="container">
	    <view class="submit-order">
	      <!-- 收货地址 -->
	      <view
	        class="delivery-addr "
	        @tap="toAddressListPage"
	      >
	        <view
	          v-if="!deliveryAddressWebEntity"
	          class="addr-bg "
	        >
	          <view class="add-addr">
	            <view class="plus-sign-img">
	              <image src="@/static/images/icon/plus-sign.png" />
	            </view>
	            <text>新增收货地址</text>
	          </view>
	          <view class="arrow empty" />
	        </view>
	        <view
	          v-if="deliveryAddressWebEntity"
	          class="addr-bg whole"
	        >
	          <view class="addr-icon">
	            <image src="@/static/images/icon/addr.png" />
	          </view>
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
	          <view class="arrow" />
	        </view>
	      </view>
	
	      <!-- 商品详情 -->
	      <view class="prod-item">
	        <block
	          v-for="(item, index) in orderTradeItemList"
	          :key="index"
	        >
	          <view
	            class="item-cont"
	            :data-orderCode="item.code"
	            @tap="toOrderDetailPage"
	          >
	            <view class="prod-pic">
	              <image :src="item.coverUrl" />
	            </view>
	            <view class="prod-info">
	              <view class="prodname">
	                {{ item.productName }}
	              </view>
	              <view class="prod-info-cont">
	                {{ item.model }}
	              </view>
	              <view class="price-nums">
	                <text class="prodprice">
	                  <text class="symbol">
	                    ￥
	                  </text>
	                  <text class="big-num">
	                    {{ item.price}}
	                  </text>
					  <text class="pay-price-text" v-if="item.payPrice">
					  	到手价：{{item.payPrice}}
					  </text>
	                </text>
	                <text class="prodcount">
					  数量：	
	                  {{ item.quantity }}
	                </text>
	              </view>
	            </view>
	          </view>
	        </block>
	
	        <view class="total-num">
	          <text class="prodcount">
	            共{{ totalCount }}件商品
	          </text>
	          <view class="prodprice">
	            合计：
	            <text class="symbol">
	              ￥
	            </text>
	            <text class="big-num">
	              {{ totalMoney }}
	            </text>
	          </view>
	        </view>
	      </view>
	
	      <!-- 订单详情 -->
	      <view class="order-msg">
	        <view class="msg-item">
	          <view
	            class="item coupon"
	            @tap="showCouponPopup"
	          >
	            <text class="item-tit">
	              优惠券：
	            </text>
	            <text
	              v-if="!tradeCouponWebEntity.canUseCouponList"
	              class="item-txt"
	            >
	              暂无可用
	            </text>
	            <text class="coupon-btn">
	              {{ tradeCouponWebEntity.totalCount ? tradeCouponWebEntity.totalCount : 0 }}张
	            </text>
	            <text class="arrow" />
	          </view>
	          <view class="item">
	            <text>买家留言：</text>
	            <input
	              v-model="remark"
	              placeholder="点击这里留言"
	            >
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
	              优惠金额：
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
	              小计：
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
	    </view>
	
	    <!-- 底部栏 -->
	    <view class="submit-order-footer">
	      <view class="sub-order">
	        <view class="item-txt">
	          合计：
	          <view class="price">
	            <text class="symbol">
	              ￥
	            </text>
	            <text class="big-num">
	              {{ finalMoney }}
	            </text>
	          </view>
	        </view>
	      </view>
	      <view
	        class="footer-box"
	        @tap="toSubmit"
	      >
	        提交订单
	      </view>
	    </view>
	  </view>
	
	  <!-- 选择优惠券弹窗 -->
	  <view
	    v-if="popupShow"
	    class="popup-hide"
	  >
	    <view class="popup-box">
	      <view class="popup-tit">
	        <text>优惠券</text>
	        <text
	          class="close"
	          @tap="closePopup"
	        />
	      </view>
	      <view class="coupon-tabs">
	        <view
	          :class="'coupon-tab ' + (couponUseType==1?'on':'')"
	          data-userType="1"
	          @tap="changeCouponUseType"
	        >
	          可用优惠券({{ tradeCouponWebEntity.canUseCouponList.length ? tradeCouponWebEntity.canUseCouponList.length : 0 }})
	        </view>
	        <view
	          :class="'coupon-tab ' + (couponUseType==2?'on':'')"
	          data-userType="2"
	          @tap="changeCouponUseType"
	        >
	          不可用优惠券({{ tradeCouponWebEntity.unCanUseCouponList.length ? tradeCouponWebEntity.unCanUseCouponList.length : 0 }})
	        </view>
	      </view>
	      <view class="popup-cnt">
	        <block v-if="couponUseType == 1">
	          <view
	            v-for="(item, index) in tradeCouponWebEntity.canUseCouponList"
	            :key="index"
	          >
	            <coupon
	              :item="item"
				  :index="index"
				  :disable="false"
				  :type="2"
	              @receiveCoupon="chooseCoupon"
	            />
	          </view>
	        </block>
	        <block v-if="couponUseType == 2">
	          <view
	            v-for="(item, index) in tradeCouponWebEntity.unCanUseCouponList"
	            :key="index"
	          >
	            <coupon
	              :item="item"
	              :index="index"
				  :disable="true"
				  :type="2"
	            />
	          </view>
	        </block>
	      </view>
	    </view>
	  </view>
	</view>
</template>

<script>
	import { request } from '@/utils/http'
	import coupon from '../../pages/coupon/coupon.vue'
	
	export default {
		onLoad(param){
		  this.confirmOrder()
		},
		components: {
		  coupon	 	
		},
		data() {
			return {
				orderTradeItemList: [],
				deliveryAddressWebEntity: {},
				couponIds: [],
				tradeCouponWebEntity: {},
				orderItems: [],
				totalCount: 0,
				totalAmount: 0,
				totalMoney: 0,
				subtractMoney: 0,
				finalMoney: 0,
				shopReduce: '',
				couponUseType: 1,
				popupShow: false,
				remark: '',
				tradeCode: ''
			}
		},
		methods: {
			confirmOrder() {
				request({
					  url: this.$host + '/v1/web/trade/confirm',
					  method: 'POST',
					  data: {
						  tradeConfirmItemReqWebEntityList: JSON.parse(uni.getStorageSync('shoppingCartList')),
					  }
				})
				.then(({ data }) => {
					 this.orderTradeItemList = data.orderTradeItemList
					 this.deliveryAddressWebEntity = data.deliveryAddressWebEntity
					 this.tradeCouponWebEntity = data.tradeCouponWebEntity
					 this.totalMoney = data.totalMoney
					 this.finalMoney = data.finalMoney
					 this.subtractMoney = data.subtractMoney
					 this.totalCount = data.totalCount
					 this.tradeCode = data.tradeCode
				 })	
				uni.hideLoading()
			},
			toAddressListPage(e) {
				uni.navigateTo({
				  url: '/pages/delivery-address/delivery-address'
				})
			},
			toOrderDetailPage(e) {
				
			},
			showCouponPopup() {
				//this.popupShow = true
			},
			closePopup() {
				this.popupShow = false
			},
			useCoupon() {
				
			},
			chooseCoupon(item,index) {
				item.hasUsed = true
				item.currentUserReceived = true
			},
			toSubmit() {
				if(!this.deliveryAddressWebEntity || !this.deliveryAddressWebEntity.id) {
					uni.showToast({
					  title: '下单失败',
					  content: '收货地址不能为空'
					})
					return
				}
				uni.showLoading()
				request({
					  url: this.$host + '/v1/web/trade/submit',
					  method: 'POST',
					  data: {
						  tradeCode: this.tradeCode,
						  deliveryAddressId: this.deliveryAddressWebEntity.id,
						  remark: this.remark
					  }
				})
				.then(({ data }) => {
					uni.hideLoading()
					this.mockPay(data.code)
				})	
			},
			changeCouponUseType(e) {
				this.couponUseType = e.currentTarget.dataset.userType
			},
			mockPay(tradeCode) {
				uni.showLoading()
				request({
					  url: this.$host + '/v1/web/pay/mockPay',
					  method: 'POST',
					  data: {
						  tradeCode: tradeCode
					  }
				})
				.then(({ data }) => {
					uni.hideLoading()
					if(data) {
						uni.showToast({
						  title: '模拟支付成功',
						  icon: 'none'
						})
						setTimeout(() => {
						  uni.navigateTo({
						    url: '/pages/pay-result/pay-result?payStatus=2&tradeCode=' + tradeCode
						  })
						}, 1200)
					} else {
						uni.showToast({
						  title: '支付失败！',
						  icon: 'none'
						})
					}
				
				})	
			}
		}
	}
</script>

<style scoped lang="scss">
@import './create-order.scss';
</style>
