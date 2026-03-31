<template>
	<view class="container">
	  <!-- 头部菜单 -->
	  <view class="order-tit">
	    <text
	      data-orderStatus="0"
	      :class="orderStatus==0?'on':''"
	      @tap="onOrderStatusTap"
	    >
	      全部
	    </text>
	    <text
	      data-orderStatus="1"
	      :class="orderStatus==1?'on':''"
	      @tap="onOrderStatusTap"
	    >
	      待支付
	    </text>
	    <text
	      data-orderStatus="2"
	      :class="orderStatus==2?'on':''"
	      @tap="onOrderStatusTap"
	    >
	      待发货
	    </text>
	    <text
	      data-orderStatus="3"
	      :class="orderStatus==3?'on':''"
	      @tap="onOrderStatusTap"
	    >
	      待收货
	    </text>
		<text
		    data-orderStatus="4"
		    :class="orderStatus==4?'on':''"
		    @tap="onOrderStatusTap"
		  >
		    评价
		  </text>
		  <text
		    data-orderStatus="6"
		    :class="orderStatus==6?'on':''"
		    @tap="onOrderStatusTap"
		  >
		    售后
		  </text>
	  </view>
	  <!-- end 头部菜单 -->
	  <view class="main">
	    <view
	      v-if="!orderTradeList || orderTradeList.length==0"
	      class="empty"
	    >
	      还没有任何相关订单
	    </view>
	    <!-- 订单列表 -->
	    <block
	      v-for="(item, index) in orderTradeList"
	      :key="index"
	    >
	      <view class="prod-item">
	        <view class="order-num">
	          <text>订单编号：{{ item.code }}</text>
	          <view class="order-state">
	            <text
	              :class="'order-sts  ' + (item.orderStatus==1?'red':'') + '  ' + ((item.orderStatus==5||item.orderStatus==6)?'gray':'')"
	            >
	              {{
	                item.orderStatus == 1 ? '待支付' : (item.orderStatus == 2 ? '已支付' : (item.orderStatus == 3 ? '待收货' : (item.orderStatus == 4 || item.orderStatus == 7 ? '已完成' : '已取消')))
	              }}
	            </text>
	
	            <view
	              v-if="item.orderStatus==5 || item.orderStatus==6"
	              class="clear-btn"
	            >
	              <image
	                src="@/static/images/icon/clear-his.png"
	                class="clear-list-btn"
	                :data-code="item.code"
	                @tap="deleteOrderTrade"
	              />
	            </view>
	          </view>
	        </view>
	
	        <!-- 商品列表 -->
	        <!-- 一个订单单个商品的显示 -->
	        <block v-if="item.tradeItemEntityList.length==1">
	          <block
	            v-for="(product, index2) in item.tradeItemEntityList"
	            :key="index2"
	          >
	            <view>
	              <view
	                class="item-cont"
	                :data-code="item.code"
	                @tap="toOrderTradeDetailPage"
	              >
	                <view class="prod-pic">
	                  <image :src="product.coverUrl" />
	                </view>
	                <view class="prod-info">
	                  <view class="prodname">
	                    {{ product.productName }}
	                  </view>
	                  <view class="prod-info-cont">
	                    {{ product.model }}
	                  </view>
	                  <view class="price-nums">
	                    <text class="prodprice">
	                      <text class="symbol">
	                        ￥
	                      </text>
	                      <text class="big-num">
	                        {{ product.price }}
	                      </text>
	                    </text>
	                    <text class="prodcount">
	                      {{ product.quantity }}
	                    </text>
	                  </view>
	                </view>
	              </view>
	            </view>
	          </block>
	        </block>
	        <!-- 一个订单多个商品时的显示 -->
	        <block v-else>
	          <view
	            class="item-cont"
	            :data-code="item.code"
	            @tap="toOrderTradeDetailPage"
	          >
	            <scroll-view
	              scroll-x="true"
	              scroll-left="0"
	              scroll-with-animation="false"
	              class="categories"
	            >
	              <block
	                v-for="(product, index2) in item.tradeItemEntityList"
	                :key="index2"
	              >
	                <view class="prod-pic">
	                  <image :src="product.coverUrl" />
	                </view>
	              </block>
	            </scroll-view>
	          </view>
	        </block>
	
	        <view class="total-num">
	          <text class="prodcount">
	            共1件商品
	          </text>
	          <view class="prodprice">
	            合计：
	            <text class="symbol">
	              ￥
	            </text>
	            <text class="big-num">
	              {{item.paymentAmount}}
	            </text>
	          </view>
	        </view>
	        <!-- end 商品列表 -->
	        <view class="prod-foot">
	          <view class="btn">
	            <text
	              v-if="item.orderStatus==1"
	              class="button"
	              :data-code="item.code"
	              hover-class="none"
	              @tap="onCancelOrderTrade"
	            >
	              取消订单
	            </text>
	            <text
	              v-if="item.orderStatus==1"
	              class="button warn"
	              :data-code="item.code"
	              hover-class="none"
	              @tap="normalPay"
	            >
	              付款
	            </text>
	            <text
	              v-if="item.orderStatus==3 || item.orderStatus==5"
	              class="button"
	              :data-code="item.code"
	              hover-class="none"
	              @tap="toDeliveryPage"
	            >
	              查看物流
	            </text>
				<text
				  v-if="item.orderStatus==3 || item.orderStatus==4"
				  class="button warn"
				  :data-code="item.code"
				  hover-class="none"
				  @tap="onRefund"
				>
				  申请售后
				</text>
	            <text
	              v-if="item.orderStatus==3"
	              class="button warn"
	              :data-code="item.code"
	              hover-class="none"
	              @tap="onConfirmReceive"
	            >
	              确认收货
	            </text>
				<text
				  v-if="item.orderStatus==4 && !item.isComment"
				  class="button warn"
				  :data-code="item.code"
				  hover-class="none"
				  @tap="onComment"
				>
				  去评价
				</text>
	          </view>
	        </view>
	      </view>
	    </block>
	  </view>
	</view>
	<!-- end 订单列表 -->
</template>

<script>
	import { request } from '@/utils/http'
	export default {
		onLoad(param) {
	      this.orderStatus = param.orderStatus		
		  this.getOrderTradeList()		
		},	
		data() {
			return {
				currentPageNo: 1,
				totalPage: 0,
				orderStatus: 1,
				orderTradeList: []
			}
		},
		methods: {
			getOrderTradeList() {
				uni.showLoading()
				request({
				  url: this.$host + '/v1/web/trade/searchUserTradeByPage',
				  method: 'POST',
				  data: {
					orderStatus: this.orderStatus,
					pageSize: 10,
					pageNo: this.currentPageNo + 1,
				  }
				})
				.then(({ data }) => {
					 this.orderTradeList = data.data
					 this.totalPage = data.totalPage
					 this.currentPageNo = data.pageNo
					 uni.hideLoading()
				 })	
			},
			onOrderStatusTap(e) {
				this.currentPageNo = 0
				this.orderStatus = e.currentTarget.dataset.orderStatus
				this.getOrderTradeList()
			},
			deleteOrderTrade(e) {
				let tradeCode = e.currentTarget.dataset.code
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
						   	tradeCode: tradeCode,
						     }
						   })
						   .then(({ data }) => {
								that.currentPageNo = 0
								that.getOrderTradeList()
								uni.showToast({
								  title: '删除成功',
								  icon: 'none'
								})
						    })	
						}
					}
				})		
			},
			toOrderTradeDetailPage(e) {
				const tradeCode = e.currentTarget.dataset.code
					
				if (tradeCode) {
				  uni.navigateTo({
					url: '/pages/order-detail/order-detail?tradeCode=' + tradeCode
				  })
				}
			},
			onCancelOrderTrade(e) {
				let tradeCode = e.currentTarget.dataset.code
				let that = this
				uni.showModal({
				  title: '',
				  content: '要取消此订单？',
				  confirmColor: '#3e62ad',
				  cancelColor: '#3e62ad',
				  cancelText: '否',
				  confirmText: '是',
				  success (res) {
				    if (res.confirm) {
						   request({
						     url: that.$host + '/v1/web/trade/cancel',
						     method: 'POST',
						     data: {
						      	tradeCode: tradeCode
						     }
						   })
						   .then(({ data }) => {
								that.currentPageNo = 0
								that.getOrderTradeList()
						    })	
						}
					}
				})		
				
			},
			onRefund(e) {
				const tradeCode = e.currentTarget.dataset.code
					
				if (tradeCode) {
				  uni.navigateTo({
					url: '/pages/refund/refund?tradeCode=' + tradeCode 
				  })
				}
			},
			toDeliveryPage() {
				
			},
			onConfirmReceive(e) {
				let that = this
				uni.showModal({
				  title: '',
				  content: '我确认已收到货？',
				  confirmColor: '#eb2444',
				  success (res) {
				    if (res.confirm) {
				      let tradeCode = e.currentTarget.dataset.code
				      request({
				        url: that.$host + '/v1/web/trade/confirmReceive',
				        method: 'POST',
				        data: {
				      	  tradeCode: tradeCode,
				        }
				      })
				      .then(({ data }) => {
				      	  that.currentPageNo = 0
				      	  that.getOrderTradeList()
				       })			  
					}
				  }
		        })				   
			
			},
			onComment(e) {
				const tradeCode = e.currentTarget.dataset.code
					
				if (tradeCode) {
				  uni.navigateTo({
					url: '/pages/comment/comment?tradeCode=' + tradeCode
				  })
				}
			},
			normalPay(e) {
				let tradeCode = e.currentTarget.dataset.code
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
@import './order-list.scss';
</style>
