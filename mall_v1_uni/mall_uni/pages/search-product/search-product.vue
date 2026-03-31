<template>
	<view class="container">
	  <!-- 搜索框 -->
	  <view class="fixed-box">
	    <view class="search-bar">
	      <view class="search-box">
	        <input
	          placeholder="输入关键字搜索"
	          class="sear-input"
	          :value="keyword"
	          confirm-type="search"
	          @confirm="doSearchProduct"
	        >
	        <image
	          src="@/static/images/icon/search.png"
	          class="search-img"
	        />
	      </view>
	    </view>
	    <view class="tabs">
	      <text
	        :class="'tab-item complete ' + (type==1?'on':'')"
	        data-type="1"
	        @tap="changeType"
	      >
	        综合
	      </text>
	      <text
	        :class="'tab-item ' + (type==2?'on':'')"
	        data-type="2"
	        @tap="changeType"
	      >
	        销量
	      </text>
	      <text
	        :class="'tab-item ' + (type==3?'on':'')"
	        data-type="3"
	        @tap="changeType"
	      >
	        价格
	      </text>
	    </view>
	  </view>
	
	  <!-- 商品列表 -->
	  <view class="prod-list">
	    <!-- 纵向列表 -->
	    <view
	      class="cont-item"
	    >
	      <block
	        v-for="(product, index) in searchProductList"
	        :key="index"
	      >
	        <view
	          class="show-item"
	          :data-productid="product.id"
	          @tap="toProductDetailPage"
	        >
	          <view class="more-prod-pic">
	            <image
	              :src="product.cover"
	              class="more-pic"
	            />
	          </view>
	          <view class="prod-text-right">
	            <view class="prod-text more">
	              {{ product.name }}
	            </view>
				<view class="prod-text">
				  {{ product.model }}
				</view>
	            <view class="cate-prod-info">
	              销量{{ product.saleQuantity }} 好评率{{ product.positiveRating }}%
	            </view>
	            <view class="prod-price more">
	              <text class="symbol">
	                ￥
	              </text>
	              <text class="big-num">
	                {{ product.price }}
	              </text>
	            </view>
	          </view>
	        </view>
	      </block>
	    </view>
	
	    <!-- 空占位 -->
	    <view
	      v-if="!searchProductList"
	      :class="['empty']"
	    >
	      暂无结果
	    </view>
	  </view>
	</view>
</template>

<script>
	import { request } from '@/utils/http'
	
	export default {
		onLoad(param) {
		  this.getSearchProductList()	
		},	
		data() {
			return {
				currentPageNo: 0,
				type: 1,
				keyword: '',
				searchProductList: []
			}
		},
		methods: {
			getSearchProductList() {
				uni.showLoading()
				request({
				  url: this.$host + '/v1/web/product/search',
				  method: 'POST',
				  data: {
					keyword: this.keyword,  
					type: this.type,  
					pageSize: 10,
					pageNo: this.currentPageNo + 1,
				  }
				})
				.then(({ data }) => {
					 if(data) {
						this.searchProductList = data.data
						this.totalPage = data.totalPage
						this.currentPageNo = data.pageNo 
					 }
					 uni.hideLoading()
				 })	
			},
			changeShowType() {
			  if(this.showType === 1) {
				  this.showType = 2
			  } else {
				  this.showType = 1
			  }		 
			},
			changeType(e) {
				this.currentPageNo = 0
				this.type = e.currentTarget.dataset.type
				this.getSearchProductList()
			},
			doSearchProduct(e) {
				this.currentPageNo = 0
				this.keyword = e.detail.value
				this.getSearchProductList()
			},
			toProductDetailPage(e) {
				const productId = e.currentTarget.dataset.productid
				if (productId) {
				  uni.navigateTo({
					url: '/pages/product-detail/product-detail?productId=' + productId
				  })
				}
			}
		}
	}
</script>

<style scoped lang="scss">
@import './search-product.scss';
</style>
