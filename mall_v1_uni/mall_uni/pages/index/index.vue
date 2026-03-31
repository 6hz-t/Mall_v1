<template>
  <view class="container">
    <view class="bg-sear">
      <view class="scrolltop">
        <view
          class="section"
          @tap="toSearchProductPage"
        >
          <image
            src="@/static/images/icon/search.png"
            class="search-img"
          />
          <text class="placeholder">
            搜索
          </text>
        </view>
      </view>
    </view>

    <view class="content">
      <!-- swiper -->
      <swiper
        :autoplay="true"
        :indicator-color="indicatorColor"
        :interval="interval"
        :duration="duration"
        :indicator-active-color="indicatorActiveColor + ' '"
        :circular="true"
        class="pic-swiper"
        indicator-dots
        previous-margin="10rpx"
        next-margin="10rpx"
      >
        <block
          v-for="(item, index) in indexImgs"
          :key="index"
        >
          <swiper-item class="banner-item">
            <view class="img-box">
              <image
                :src="item.url"
                :data-id="item.id"
                class="banner"
                @tap="toProductDetailPage"
              />
            </view>
          </swiper-item>
        </block>
      </swiper>
      <!-- end swiper -->

      <view class="cat-item">
        <view
          class="item"
          data-type="1"
          @tap="toIndexProductPage"
        >
          <image src="@/static/images/icon/newProd.png" />
          <text>热销商品</text>
        </view>
        <view
          class="item"
          data-type="2"
          @tap="toIndexProductPage"
        >
          <image src="@/static/images/icon/timePrice.png" />
          <text>最新商品</text>
        </view>
        <view
          class="item"
          data-type="3"
          @tap="toIndexProductPage"
        >
          <image src="@/static/images/icon/neweveryday.png" />
          <text>秒杀商品</text>
        </view>
		<view
		  class="item"
		  data-type="3"
		  @tap="toRecommendProductPage"
		>
		  <image src="@/static/images/icon/promotion.png" />
		  <text>猜你喜欢</text>
		</view>
        <view
          class="item"
          @tap="toCouponCenterPage"
        >
          <image src="@/static/images/icon/newprods.png" />
          <text>领优惠券</text>
        </view>
      </view>

      <!-- 消息播放 -->
      <view
        v-if="noticeList"
        class="message-play"
        @tap="toIndexNoticePage"
      >
        <image
          src="@/static/images/icon/horn.png"
          class="hornpng"
        />
        <swiper
          :vertical="true"
          :autoplay="true"
          :circular="true"
          duration="1000"
          class="swiper-cont"
        >
          <block
            v-for="(item, index) in noticeList"
            :key="index"
          >
            <swiper-item class="items">
              {{ item.title }}
            </swiper-item>
          </block>
        </swiper>
        <text class="arrow" />
      </view>
    </view>
	
	<view
	  v-if="hotProductList && hotProductList.length"
	  class="hot-sale"
	>
	  <view class="hotsale-item-cont">
	    <block
	      v-for="(product, index2) in hotProductList"
	      :key="index2"
	    >
	      <view
	        class="prod-items"
	        :data-productid="product.productId"
	        @tap="toProductDetailPage"
	      >
	        <view class="hot-imagecont">
	          <image
	            :src="product.cover"
				style="width: 45px;height: 45px;"
	            :class-list="['hotsaleimg']"
	          />
	        </view>
	        <view class="hot-text">
	          <view class="hotprod-text">
	            {{ product.productName }}
	          </view>
	          <view class="prod-info">
	            {{ product.model }}
	          </view>
	          <view class="prod-text-info">
	            <view class="price">
	              <text class="symbol">
	                ￥
	              </text>
	              <text class="big-num">
	                {{ product.price}}
	              </text>
	            </view>
	            <image
	              src="@/static/images/tabbar/basket-sel.png"
	              class="basket-img"
	            />
	          </view>
	        </view>
	      </view>
	    </block>
	  </view>
	</view>
  </view>
</template>
<script>

export default {
	data() {
	  return {
		  noticeList: [],
		  duration: 1000,
		  interval: 2000,	
		  indexImgs: [],
		  hotProductList: [],	
		  newProductList: [],
		  indicatorActiveColor:'#1b7dec',
		  indicatorColor:'#d1e5fb'
	  }
	},
	created() {
	  this.getIndexCarouselImageList()	
	  this.getNoticeList()
	  this.searchProduct(1)	
	},
	methods: {
	  toIndexNoticePage() {
		uni.navigateTo({
		  url: '/pages/notice-list/notice-list'
		})
	  },
	  toSearchProductPage() {
		  uni.navigateTo({
		    url: '/pages/search-product/search-product'
		  })
	  },
	  toRecommendProductPage() {
		 uni.navigateTo({
		   url: '/pages/recommend-product/recommend-product'
		 }) 
	  },
	  toCouponCenterPage() {
		uni.navigateTo({
			url: '/pages/coupon/coupon-center?type=list'
		})  
	  },
	  toProductDetailPage(e){
		const id = e.currentTarget.dataset.id;  
		const productId = id ? id : e.currentTarget.dataset.productid
		if (productId) {
			console.info('productId:'+productId)
		  uni.navigateTo({
			url: '/pages/product-detail/product-detail?productId=' + productId
		  })
		}
	  },
	  toIndexProductPage(e) {
		const type = e.currentTarget.dataset.type  
		let url = '/pages/index-product/index-product?type=' + type
		uni.navigateTo({
		  url
		})
	  },
	 getNoticeList() {
		uni.request({
		  url: this.$host + '/v1/web/index/getIndexNoticeList',
		  method: 'GET',
		  data: {
		  }
		})
		.then(({ data }) => {
			 if(data) {
				this.noticeList = data.data
			 }
		 })	
	 },
      searchProduct(type){
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
					this.hotProductList = data.data
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
	  },
	  getIndexCarouselImageList(){
	  		uni.request({
	  		  url: this.$host + '/v1/web/index/getIndexCarouselImageList',
	  		  method: 'GET',
			  data: {
			  },
	  		  success: res => {
	  			 if(res && res.data) {
	  				 const data = res.data
	  				 if(data.code === 200) {
	  					this.indexImgs = data.data
						console.info(this.indexImgs)
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
@import "./index.scss";
</style>
