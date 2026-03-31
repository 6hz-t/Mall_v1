<template>
	<!-- 商品详情 -->
	<view class="container">
	  <!-- 轮播图 -->
	  <swiper
	    :indicator-dots="indicatorDots"
	    :autoplay="autoplay"
	    :indicator-color="indicatorColor"
	    :interval="interval"
	    :duration="duration"
	    :indicator-active-color="indicatorActiveColor"
	  >
	    <block
	      v-for="(item, index) in productDetail.swiper"
	      :key="index"
	    >
	      <swiper-item>
			<image
			  :src="item"
			  class="banner"
			  @tap="toProductPage"
			/>
	      </swiper-item>
	    </block>
	  </swiper>
	  <!-- end  轮播图 -->
	  <!-- 商品信息 -->
	  <view class="prod-info">
	    <view class="tit-wrap">
	      <view class="prod-tit">
	        {{ defaultSku.name }}
	      </view>
	      <view
	        class="col"
	        @tap="addOrCancelFavorites"
	      >
	        <image
			  v-if="!isFavorites"
	          src="@/static/images/icon/prod-col.png"
	        />
			<image
			  v-if="isFavorites"
			  src="@/static/images/icon/prod-col-red.png"
			/>
	        收藏
	      </view>
	    </view>
	    <view class="sales-p">
	      {{ defaultSku.model }}
	    </view>
	    <view class="prod-price">
	      <text
	        class="price"
	      >
	        ￥
	        <text class="price-num">
	          {{ defaultSku.price}}
	        </text>
	      </text>
		  <text>
			  库存：
			  <text >
				  {{defaultSku.remainQuantity}}
			  </text>
		  </text>
	    </view>
	  </view>
	  <!-- 已选规格 -->
	  <view
	    class="sku"
	    @tap="onShowSku"
	  >
	    <view class="sku-tit">
	      已选
	    </view>
	    <view class="sku-con">
	      {{ selectedProp.length > 0 ? selectedProp + '，' : '' }}{{ productCount }}件
	    </view>
	    <view class="more">
	      ...
	    </view>
	  </view>
	  <!-- 商品详情 -->
	  <view class="prod-detail">
	    <view>
	      <rich-text :nodes="defaultSku.detail" />
	    </view>
	  </view>
	  <!-- end 商品详情 -->
	  <!-- 评价-->
	  <view class="cmt-wrap">
	    <view
	      class="cmt-tit"
	      @tap="onShowComment"
	    >
	      <view class="cmt-t">
	        评价
	        <text class="cmt-good" v-if="productCommentStatistic.positiveRating">
	          好评{{ productCommentStatistic.positiveRating }}%
	        </text>
	      </view>
	      <view class="cmt-count">
	        共{{ productCommentStatistic.all }}条
	        <text class="cmt-more" />
	      </view>
	    </view>
	    <view class="cmt-cont">
	      <view
	        class="cmt-tag"
	        @tap="onShowComment"
	      >
	        <text>全部({{ productCommentStatistic.all }})</text>
	        <text>好评({{ productCommentStatistic.positive }})</text>
	        <text>中评({{ productCommentStatistic.moderate }})</text>
	        <text>差评({{ productCommentStatistic.negative }})</text>
	      </view>
	      <view class="cmt-items">
	        <view
	          v-for="(item, index) in productCommentList"
	          :key="index"
	          class="cmt-item"
	        >
	          <view class="cmt-user">
	            <text class="date">
	              {{ item.createTimeStr }}
	            </text>
	            <view class="cmt-user-info">
	              <image
	                class="user-img"
	                :src="item.avatar"
	              />
	              <view class="nickname">
	                {{ item.nickName }}
	              </view>
	            </view>
	          </view>
	          <view class="cmt-cnt">
	            {{ item.content }}
	          </view>
	        </view>
	      </view>
	      <view
	        v-if="productCommentList.length > 2"
	        class="cmt-more-v"
	      >
	        <text @tap="onShowComment">
	          查看全部评价
	        </text>
	      </view>
	    </view>
	  </view>
	
	<!-- 规格弹窗 -->
	<view
	  v-if="showSku"
	  class="pup-sku"
	>
	  <view class="pup-sku-main">
	    <view class="pup-sku-header">
	      <image
	        class="pup-sku-img"
			v-if="defaultSku.cover && defaultSku.cover.length>0"
	        :src="defaultSku.cover[0]"
	      />
	      <view class="pup-sku-price">
	        ￥
	        <text
	          v-if="defaultSku && defaultSku.price"
	          class="pup-sku-price-int"
	        >
	          {{ defaultSku.price }}
	        </text>
	      </view>
	      <view class="pup-sku-prop">
	        <text>已选</text>
	        {{ selectedProp.length > 0 ? selectedProp + '，' : '' }}{{ productCount }}件
	      </view>
	      <view
	        class="close"
	        @tap="closeShowSku"
	      />
	    </view>
	    <view class="pup-sku-body">
	      <view class="pup-sku-area">
	        <view
	          v-if="groupAttributeList.length"
	          class="sku-box"
	        >
	            <view
	              v-for="(groupAttribute,index) in groupAttributeList"
	              :key="groupAttribute.id"
	              class="items sku-text"
	            >
	              <text class="sku-kind">
	                {{ groupAttribute.name }}
	              </text>
	              <view class="con">
	                <text
	                  v-for="valueItem in groupAttribute.valueList"
	                  :key="valueItem.id"
	                  class="sku-choose-item"
	                  :class="[selectedPropList.indexOf(groupAttribute.name + ':' + valueItem.value) !== -1?'active':'',
                               isDisable(valueItem, groupAttribute, index) ? 'gray':'']"
	                  @click="toChooseItem(valueItem, groupAttribute)"
	                >
	                  {{ valueItem.value }}
	                </text>
	              </view>
	            </view>
	        </view>
	      </view>
	      <view class="pup-sku-count">
	        <view class="num-wrap">
	          <view
	            class="minus"
	            @tap="onCountMinus"
	          >
	            <text class="row" />
	          </view>
	          <view class="text-wrap">
	            <input
	              type="number"
	              :value="productCount"
	              disabled
	            >
	          </view>
	          <view
	            class="plus"
	            @tap="onCountPlus"
	          >
	            <text class="row" />
	            <text class="col" />
	          </view>
	        </view>
	        <view class="count-name">
	          数量
	        </view>
	      </view>
	    </view>
	   </view>	
	  </view> 
	  <!-- 底部按钮 -->
	  <view class="cart-footer">
	    <view
	      class="btn icon"
	      @tap="toIndexPage"
	    >
	      <image src="@/static/images/tabbar/homepage.png" />
	      首页
	    </view>
	    <view
	      class="btn icon"
	      @tap="toShoppingCartPage"
	    >
	      <image src="@/static/images/tabbar/basket.png" />
	      购物车
	      <view
	        v-if="totalCartNum>0"
	        class="badge badge-1"
	      >
	        {{ totalCartNum }}
	      </view>
	    </view>
	    <view
	      class="btn cart"
	      @tap="addShoppingCart"
	    >
	      <text>加入购物车</text>
	    </view>
	    <view
	      class="btn buy"
	      @tap="toBuyPage"
	    >
	      <text>立即购买</text>
	    </view>
	  </view>
	  <!-- end 底部按钮 -->
    <view
      class="cmt-popup"
	  v-if="showComment"
     >
      <view class="cmt-tit">
        <view class="cmt-t">
          商品评价
          <text class="cmt-good" v-if="productCommentStatistic.positiveRating">
            好评度{{ productCommentStatistic.positiveRating }}%
          </text>
        </view>
        <text
          class="close"
          @tap="closeComment"
        />
      </view>
      <view class="cmt-cont">
        <view class="cmt-tag">
          <text
            data-type="0"
            :class="type==0?'selected':''"
            @tap="searchProductComment"
          >
            全部({{ productCommentStatistic.all }})
          </text>
          <text
            data-type="1"
            :class="type==1?'selected':''"
            @tap="searchProductComment"
          >
            好评({{ productCommentStatistic.positive }})
          </text>
          <text
            data-type="2"
            :class="type==2?'selected':''"
            @tap="searchProductComment"
          >
            中评({{ productCommentStatistic.moderate }})
          </text>
          <text
            data-type="3"
            :class="type==3?'selected':''"
            @tap="searchProductComment"
          >
            差评({{ productCommentStatistic.negative }})
          </text>
        </view>
        <view class="cmt-items">
          <block v-if="productCommentList.length">
            <view
              v-for="(item, index) in productCommentList"
              :key="index"
              class="cmt-item"
            >
              <view class="cmt-user">
                <text class="date">
                  {{ item.createTimeStr }}
                </text>
                <view class="cmt-user-info">
                  <image
                    class="user-img"
                    :src="item.avatar"
                  />
                  <view class="nickname">
                    {{ item.nickName }}
                  </view>
                </view>
              </view>
              <view class="cmt-cnt">
                {{ item.content }}
              </view>
            </view>
          </block>
          <view
            v-if="!productCommentList.length"
            class="empty"
          >
            暂无评价
          </view>
        </view>
        <view
          class="load-more"
		  v-if="totalPage > currentPageNo"
        >
          <text @tap="getMoreProductCommentPage">
            点击加载更多
          </text>
        </view>
      </view>
    </view>
	</view>
</template>

<script>
	import { request } from '@/utils/http'
	
	export default {
		onLoad(param) {
		  this.productId = param.productId
		  this.getProductInfo(this.productId)
		  this.getProductComment(0)		
		},	
		data() {
			return {
			  indicatorDots: true,
			  indicatorColor: '#f2f2f2',
			  indicatorActiveColor: '#eb2444',
			  autoplay: true,
			  interval: 3000,
			  duration: 1000,
			  productId: 0,
			  productDetail: {},
			  productCommentStatistic: {},
			  productCommentList: [],
			  currentPageNo: 0,
			  totalPage: 0,
			  type: 0,
			  defaultSku: {},
			  productCount: 1,
			  skuAttributeEntityList: [],
			  selectedProp: [],
			  selectedPropList: [],
			  groupAttributeList: [],
			  groupProductList: [],
			  isFavorites: false,
			  showComment: false,
			  showSku: false,
			  totalCartNum: 0
			}
		},
		methods: {
			isDisable(valueItem, groupAttribute, index) {
				let disabled = true
				if(index === 0) {
					disabled = false
					return disabled
				} 
				const currentProp = groupAttribute.name + ":" + valueItem.value
				const sortList = this.sortAttribute(this.selectedPropList)
				this.groupProductList.forEach(item => {
					let attributeArray = item.model.split(" ");
					let matchCount = 0
					attributeArray.forEach(attribute => {
						  for(let i=0; i<index; i++) {
							  let propValue = sortList[i]
							  if(propValue === attribute) {
							  	matchCount++
							  }
						  }
					    }
					)
			       if(matchCount === index) {
					   attributeArray.forEach(attribute => {
						      if(currentProp === attribute) {
								  disabled = false
								  return disabled
							  }
						   }
						)  
					 }
				   }
				)	
				return disabled
			},
			sortAttribute(list) {
				let sortList = []
				this.groupAttributeList.forEach(item => {
					list.forEach(value => {
						let array = value.split(":")
						if(item.name === array[0]) {
							sortList.push(value)
						}
					})
				})
				return sortList
			},
			toChooseItem(valueItem, groupAttribute) {
				const prop = groupAttribute.name + ":" + valueItem.value
				if(this.selectedPropList.indexOf(prop) === -1) {
					var findProp = false
					this.selectedPropList.forEach((item,index) => {
						if(item.includes(groupAttribute.name + ":")) {
							this.selectedPropList[index] = prop
							findProp = true
							return
						}
					})
					if(!findProp) {
					  this.selectedPropList.push(prop)
					}
					this.findDefaultSku()
				}
			},
			findDefaultSku() {
				this.groupProductList.forEach(item => {
					let attributeArray = item.model.split(" ");
					let matchCount = 0;
					attributeArray.forEach(attribute => {
						this.selectedPropList.forEach(prop => {
							if(prop === attribute) {
								matchCount++
							}
						})
					})
					if(matchCount === attributeArray.length) {
						this.defaultSku = item
						uni.navigateTo({
							url: '/pages/product-detail/product-detail?productId=' + item.id
						})
						return
					}
				})
			},
			initDefaultSku() {
			  this.defaultSku = {
				 id : this.productDetail.id,
				 name: this.productDetail.name, 
				 model: this.productDetail.model,
				 remainQuantity: this.productDetail.remainQuantity,
				 price: this.productDetail.price,
				 detail: this.productDetail.detail, 
				 cover: this.productDetail.cover
			  }	
			},
			initSelectedPropList() {
			  if(this.skuAttributeEntityList) {
				 this.skuAttributeEntityList.forEach(item => {
				 	this.selectedPropList.push(item.attributeName + ":" + item.value)
				 })	 
			  }	 
			},
			onCountMinus() {
				if(this.productCount - 1 >= 0) {
					this.productCount = this.productCount - 1
				}
			},
			onCountPlus() {
				if(this.productCount + 1 <= this.productDetail.remainQuantity) {
					this.productCount = this.productCount + 1
				}
			},
			onShowSku() {
			  this.showSku = true	
			},
			onShowComment() {
			   this.showComment = true	
			},
			closeShowSku() {
			  this.showSku = false	
			},
			closeComment() {
			   this.currentPageNo = 0	
			   this.showComment = false
			   this.getProductComment(0)			   
			},
			getProductComment(type) {
				request({
				  url: this.$host + '/v1/web/product/searchProductComment',
				  method: 'POST',
				  data: {
					productId: this.productId,
					type: type,
					pageSize: 2,
					pageNo: this.currentPageNo + 1,
				  }
			})
			.then(({ data }) => {
				 this.productCommentList = data.data
				 this.totalPage = data.totalPage
				 this.currentPageNo = data.pageNo
				 uni.hideLoading()
			 })	
			},
			searchProductComment(e) {
				this.currentPageNo = 0
				this.type = e.currentTarget.dataset.type
				this.getProductComment(this.type)
			},
			getMoreProductCommentPage(e) {
				this.type = e.currentTarget.dataset.type
				this.getProductComment(this.type)
			},
			addOrCancelFavorites() {
				request({
				  url: this.$host + '/v1/web/product/addOrCancelFavorites',
				  method: 'POST',
				  data: {
				    productId: this.productId   
				  }
				})
				.then(({ data }) => {
					 this.isFavorites = data
					 uni.hideLoading()
				 })
			},
			toIndexPage() {
				wx.redirectTo({
				  url: '/pages/index/index'
				})
			},
			addShoppingCart() {
				if(this.productCount > this.defaultSku.remainQuantity) {
					uni.showToast({
					  title: '库存不足',
					  icon: 'none'
					})
					return
				}
				request({
				  url: this.$host + '/v1/web/product/addShoppingCart',
				  method: 'POST',
				  data: {
					quantity: this.productCount,
					productId: this.productId
				  }
				})
				  .then((res) => {
					if(res.data) {
						this.totalCartNum = this.totalCartNum + this.productCount	
					}  
					uni.hideLoading()
					uni.showToast({
					  title: '加入购物车成功',
					  icon: 'none'
					})
				  })	
			},
			toBuyPage() {
				
			},
			toShoppingCartPage() {
				wx.switchTab({
				  url: '/pages/shopping-cart/shopping-cart'
				})
			},
			toProductPage() {
				wx.redirectTo({
				  url: '/pages/index-product/index-product?type=1'
				})
			},
			getProductInfo(productId) {
				request({
				  url: this.$host + '/v1/web/product/getDetail',
				  method: 'GET',
				  data: {
					productId: productId   
				  }			
			    }).then(({ data }) => {
				  if(data) {
					 this.productDetail = data
					 this.isFavorites = data.isFavorites
					 this.selectedProp.push(data.model)
					 this.productCommentStatistic = data.productCommentStatistic
					 this.groupAttributeList = data.groupAttributeList
					 this.groupProductList = data.groupProductList
					 this.skuAttributeEntityList = data.skuAttributeEntityList
					 this.totalCartNum = data.totalCartNum
					 this.initDefaultSku()
					 this.initSelectedPropList() 
				  }	
				  uni.hideLoading()
				})  
		}
	},
}	
</script>
<style scoped lang="scss">
@import './product-detail.scss';
</style>
