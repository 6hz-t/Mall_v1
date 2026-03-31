<template>
<view class="container">
	  <!-- 商品列表 -->
	  <view class="prod-list">
	    <!-- 纵向列表 -->
	    <view
	      class="cont-item"
	    >
	      <block
	        v-for="(product, index) in recommendProductList"
	        :key="index"
	      >
	        <view
	          class="show-item"
	          :data-productId="product.id"
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
	      v-if="!recommendProductList"
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
		  this.getRecommendProductList()	
		},	
		data() {
			return {
				recommendProductList: [],
			}
		},
		methods: {
			getRecommendProductList() {
				uni.showLoading()
				request({
				  url: this.$host + '/v1/web/recommend/recommendProduct',
				  method: 'GET',
				  data: {
				  }
				})
				.then(({ data }) => {
					 if(data) {
						this.recommendProductList = data
					 }
					 uni.hideLoading()
				 })	
			},
			toProductDetailPage(e) {
				const productId = e.currentTarget.dataset.productId
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
@import './recommend-product.scss';
</style>
