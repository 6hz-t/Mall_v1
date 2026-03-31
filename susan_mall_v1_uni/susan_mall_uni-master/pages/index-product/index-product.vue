<template>
	<view class="container">
	  <view>
	    <block
	      v-for="(item, index) in productList"
	      :key="index"
	    >
	     <view
	       class="prod-items"
	       :data-productid="item.productId"
	       @tap="toProductPage"
	     >
	       <view class="hot-imagecont">
	         <image
	           :src="item.cover"
	           class="hotsaleimg"
	         />
	       </view>
	       <view class="hot-text">
	         <view class="hotprod-text">
	           {{ item.productName }}
	         </view>
	         <view class="prod-text-info">
	           <view class="price">
	             <text
	               v-if="sts===3"
	               class="deadline-price"
	             >
	               秒杀价
	             </text>
	             <text class="symbol">
	               ￥
	             </text>
	             <text class="big-num">
	               {{ item.price }}
	             </text>
	           </view>
	         </view>
	       </view>
	     </view>
	    </block>
	    <view
	      v-if="!productList.length"
	      class="empty"
	    >
	      暂无数据
	    </view>
	  </view>
	</view>
</template>

<script>
	export default {
		onLoad(param) {
		  const type = param.type
		  const title = ''
		  if(type == 1) {
			uni.setNavigationBarTitle({
			  title: '热门商品'
			})
		  } else if(type == 2) {
			uni.setNavigationBarTitle({
			  title: '最新商品'
			})
		  }  else if(type == 3) {
			uni.setNavigationBarTitle({
			  title: '秒杀商品'
			})
		  }  
		  
		  this.loadProductData(type)	
		}, 
		data() {
			return {
				sts: 1,
				title: '',
				current: 1,
				size: 12,
				pages: 0,
				productList: []
			}
		},
		methods: {
		  toProductPage(e){
			const productId = e.currentTarget.dataset.productid
	
			if (productId) {
			  uni.navigateTo({
				url: '/pages/product-detail/product-detail?productId=' + productId
			  })
			}
		  },
		  loadProductData(type) {
			 uni.request({
			   url: this.$host + '/v1/web/index/getIndexProductList',
			   method: 'GET',
			   data: {
			 	type: type   
			   },
			   success: res => {
			 	 if(res && res.data) {
			 		 const data = res.data
			 		 if(data.code === 200) {
			 			this.productList = data.data
					    uni.hideLoading()
			 		 } else {
			 			 uni.showToast({
			 			   title: '页面异常',
			 			   content: data.message
			 			 })
			 		 }
			 	 }
			   },
			   fail: () => {},
			   complete: () => {}
			 })  
		  }	
		}
	}
</script>

<style scoped lang="scss">
@import './index-product.scss';
</style>
