<template>
<view class="container">
    <view class="prod-list">
		<block
		  v-for="(item, scIndex) in couponGroupProductEntityList"
		  :key="scIndex"
		>
		  <view class="prod-block">
		    <view
		      v-if="item.couponWebEntity.name"
		      class="discount-tips"
		    >
		      <text class="text-block">
		        {{item.couponWebEntity.name }}
		      </text>
		    </view>
		  <block
			v-for="(product, scIndex) in item.shoppingCartList"
			:key="scIndex"
		  >
			<view class="item">
			  <view class="btn">
				<label>
				  <checkbox
					:data-scindex="scIndex"
					:value="product.id"
					:checked="product.checked"
					color="#105c3e"
					@tap="onSelectedItem(product)"
				  />
				</label>
			  </view>
			  <view class="prodinfo">
				<view class="pic">
				  <image :src="product.cover" />
				</view>
				<view class="opt">
				  <view class="prod-name">
					{{ product.productName }}
				  </view>
				  <text :class="'prod-info-text ' + (product.model?'':'empty-n')">
					{{ product.model }}
				  </text>
				  <view class="price-count">
					<view class="price">
					  <text class="price-symbol">
						￥
					  </text>
					  <text class="big-num">
						{{ product.price}}
					  </text>
					</view>
					<view v-if="product.price > product.payPrice" class="pay-price-text">
					  <text class="big-num">
						到手价:
						￥{{ product.payPrice}}
					  </text>
					</view>
					<view class="m-numSelector">
					  <view
						class="minus"
						:data-scindex="scIndex"
						@tap="onMinus(product)"
					  />
					  <input
						type="number"
						:value="product.quantity"
						disabled
					  >
					  <view
						class="plus"
						:data-scindex="scIndex"
						@tap="onPlus(product)"
					  />
					</view>
				  </view>
				</view>
			  </view>
			</view>
		  </block>
		 </view>
		</block>  
    </view>

    <view
      v-if="!shoppingCartList.length"
      class="empty"
    >
      <view class="img">
        <image src="@/static/images/tabbar/basket.png" />
      </view>
      <view class="txt">
        您还没有添加任何商品哦~
      </view>
    </view>

    <!-- 底部按钮 -->
    <view
      v-if="shoppingCartList.length>0"
      class="cart-footer"
    >
      <view class="btn all">
        <checkbox
          :checked="allChecked"
          color="#f7d731;"
          @tap="onSelectAll"
        />
        全选
      </view>
      <view
        class="btn del"
        @tap="onDelete"
      >
        <text>删除</text>
      </view>
      <view class="btn total">
        <view class="finally">
          <view class="price">
			<text>合计:</text>
            <text class="symbol">
              ￥
            </text>
            <text class="big-num">
              {{ finalMoney}}
            </text>
          </view>
        </view>
        <view
          v-if="subtractMoney>0"
          class="total-msg"
        >
          总额:￥{{ totalMoney }} 立减:￥{{ subtractMoney }}
        </view>
      </view>
      <view
        class="btn settle"
        @tap="toSettle"
      >
        <text>结算</text>
      </view>
    </view>
    <!-- end 底部按钮 -->
  </view>
</template>

<script>
	import { request } from '@/utils/http'
	
	export default {
		onShow(param){
			const token = uni.getStorageSync('Token')
			if(!token) {
				this.toLoginPage()
				return
			}
		   this.initShoppingCart()
		},
		data() {
			return {
				couponGroupProductEntityList:[],
				shoppingCartList:[],
				allChecked: false,
				totalMoney: 0,
				finalMoney: 0,
				subtractMoney: 0
			}
		},
		methods: {
			initShoppingCart() {
				uni.showLoading() // 加载购物车
				request({
					  url: this.$host + '/v1/web/product/getShoppingCartProduct',
					  method: 'POST',
					  data: {
					  }
				})
				.then(({ data }) => {
					 if(data) {
						this.couponGroupProductEntityList = data.couponGroupProductWebEntityList
						if(this.couponGroupProductEntityList) {
							this.couponGroupProductEntityList.forEach(entity =>{
								this.shoppingCartList.push(entity.shoppingCartList);
							}) 
						}
						this.totalMoney = data.totalMoney
						this.finalMoney = data.finalMoney
						this.subtractMoney = data.subtractMoney 
					 }
				
				 })	
				uni.hideLoading()
			},
			onSelectAll() {
				this.allChecked = !this.allChecked
				this.couponGroupProductEntityList.forEach(couponGroup => {
					couponGroup.shoppingCartList.forEach(product =>{
						product.checked = this.allChecked
					})
				})
			
				
			},
			onUpdate(product, count) {
				request({
				  url: this.$host + '/v1/web/product/updateShoppingCart',
				  method: 'POST',
				  data: {
					quantity: product.quantity + count,
					id: product.id
				  }
				})
				  .then(() => {
					this.initShoppingCart()
				  })	
			},
			onDelete() {
				let ids = []
				this.couponGroupProductEntityList.forEach(couponGroup => {
					couponGroup.shoppingCartList.forEach(product =>{
						if(product.checked) {
							ids.push(product.id)
						}
					})
				})
				if(ids.length === 0) {
					uni.showToast({
					  title: '请选择要删除的商品',
					  icon: 'none'
					})
				} else {
					let host = this.$host
					let that = this
					uni.showModal({
					  title: '',
					  content: '确认要删除选中的商品吗？',
					  confirmColor: '#eb2444',
					  success (res) {
					    if (res.confirm) {
					      uni.showLoading({
					        mask: true
					      })
					      request({
					        url: host + '/v1/web/product/deleteShoppingCart',
					        method: 'POST',
					        data: ids
					      })
					        .then(() => {
					          uni.hideLoading()
					          that.initShoppingCart()
					        })
					    }
					  }
					
					})
				}
			},
			toSettle() {
				let shoppingCartList = []
				this.couponGroupProductEntityList.forEach(couponGroup => {
					couponGroup.shoppingCartList.forEach(cartItem =>{
						if(cartItem.checked) {
							let item = {
								shoppingCartId: cartItem.id,
								couponId: (couponGroup.couponWebEntity) ? couponGroup.couponWebEntity.id: null
							}
							shoppingCartList.push(item)
						}
					})
				})
				if(!shoppingCartList||shoppingCartList.length===0) {
					uni.showToast({
					  title: '请选择要结算的商品',
					  icon: 'none'
					})
					return
				}
				
				uni.setStorageSync('shoppingCartList', JSON.stringify(shoppingCartList))
				uni.navigateTo({
				  url: '/pages/create-order/create-order'
				})
			},
			onSelectedItem(item) {
				item.checked = !item.checked
				let selectCount = 0
				this.couponGroupProductEntityList.forEach(couponGroup => {
					couponGroup.shoppingCartList.forEach(product =>{
						if(product.checked) {
							selectCount++
						}
					})
				})
		
				if(this.shoppingCartList.length === selectCount) {
					this.allChecked = true
				} else {
					if(this.allChecked) {
						this.allChecked = false
					}
				}
			},
			onMinus(product) {
				if(product.quantity >= 1) {
					this.onUpdate(product, -1)
				}
				
			},
			onPlus(product) {
				if(product.quantity + 1 <= product.stock) {
					this.onUpdate(product, 1)
				}
				
			},
			toLoginPage() {
				uni.redirectTo({
					url: '/pages/login/index'
				}) 
			},
		}
	}
</script>

<style scoped lang="scss">
@import './shopping-cart.scss';
</style>
