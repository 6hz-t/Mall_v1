<template>
	<view class="container">
	  <view class="main">
	    <view
	      v-if="!addressList || addressList.length === 0"
	      class="empty"
	    >
	      <view class="txt">
	        您还没有收货地址
	      </view>
	    </view>
	    <radio-group class="radio-group">
	      <block
	        v-for="(item, index) in addressList"
	        :key="index"
	      >
	        <view class="address">
	          <view
	            class="personal"
				@tap="goToCreateOrderPage(item)"
	          >
	            <view class="info-tit">
	              <text class="name">
	                {{ item.receiverName }}
	              </text>
	              <text class="tel">
	                {{ item.receiverPhone }}
	              </text>
	              <image
	                src="@/static/images/icon/revise.png"
	                :data-id="item.id"
	                @tap.stop="toEditAddress"
	              />
	            </view>
	            <view class="addr">
	              <text class="addr-get">
	                {{ item.province }}{{ item.city }}{{ item.district }}{{ item.detailAddress }}
	              </text>
	            </view>
	          </view>
	          <view
	            class="select-btn"
	            :data-id="item.id"
	            @tap="onDefaultAddress"
	          >
	            <view class="box">
	              <radio
	                :value="item.id"
	                :checked="item.isDefault===true"
	                color="#eb2444"
	                :data-id="item.id"
	                @tap="onDefaultAddress"
	              />
	              设为默认地址
	            </view>
	          </view>
	        </view>
	      </block>
	    </radio-group>
	  </view>
	  <view
	    class="footer"
	    @tap="toAddAddress"
	  >
	    <text>新增收货地址</text>
	  </view>
	</view>
</template>

<script>
	import { request } from '@/utils/http'
	export default {
		onLoad(param){
		  this.initAddressList()
		},
		data() {
			return {
				addressList:[]
			}
		},
		methods: {
			initAddressList() {
				request({
					  url: this.$host + '/v1/web/deliveryAddress/getUserDeliveryAddressList',
					  method: 'GET',
					  data: {}
				})
				.then(({ data }) => {
					 this.addressList = data
				 })	
				uni.hideLoading()
			},
			toEditAddress(e) {
				const id = e.currentTarget.dataset.id
				uni.navigateTo({
				  url: '/pages/edit-address/edit-address?id=' + id
				})
			},
			onDefaultAddress(e) {
				const id = e.currentTarget.dataset.id
				uni.showLoading()
				request({
					  url: this.$host + '/v1/web/deliveryAddress/setDefaultDeliveryAddress',
					  method: 'POST',
					  data: {
						  id: id
					  }
				})
				.then(({ data }) => {
					uni.showToast({
					  title: '设置默认收货地址成功',
					  icon: 'none'
					})
					this.initAddressList()
				 })	
				uni.hideLoading()
			},
			toAddAddress() {
				uni.navigateTo({
				  url: '/pages/edit-address/edit-address'
				})
			},
			goToCreateOrderPage(item) {
			  const pages = getCurrentPages() // 当前页面
			  const prevPage = pages[pages.length - 2] // 上一页面
			  prevPage.deliveryAddressWebEntity = item // 直接给上一页面赋值
			  // 返回
			  uni.navigateBack({
				delta: 1
			  })
			}
		}
	}
</script>

<style scoped lang="scss">
@import './delivery-address.scss';
</style>
