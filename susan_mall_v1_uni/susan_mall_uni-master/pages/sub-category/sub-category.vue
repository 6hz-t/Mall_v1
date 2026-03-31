<template>
 <view class="container">
    <!-- 顶部子分类tab -->
    <scroll-view
      scroll-x="true"
      class="category-tit"
      :scroll-into-view="intoView"
      :scroll-with-animation="true"
    >
      <block
        v-for="(item, index) in subCategoryList"
        :key="index"
      >
        <view
          :id="'sw' + item.id"
          :class="'category-item ' + (item.id==categoryId? 'on':'')"
          :data-id="item.id"
          @tap="onSubCategoryTap"
        >
          {{ item.name }}
        </view>
      </block>
    </scroll-view>
    <!-- 商品列表 -->
    <view class="prod-item">
      <block v-if="productList.length">
        <block
          v-for="(product, key) in productList"
          :key="key"
        >
          <view
            class="prod-items"
            :data-productid="product.id"
            @tap="toProductPage"
          >
            <view class="hot-imagecont">
              <image
                :src="product.cover"
                class="hotsaleimg"
              />
            </view>
            <view class="hot-text">
              <view class="hotprod-text">
                {{ product.name }}
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
              </view>
            </view>
          </view>
        </block>
      </block>
      <view
        v-else
        class="empty-wrap"
      >
        暂无商品数据~
      </view>
    </view>
  </view>
</template>

<script>
	import { request } from '@/utils/http'
	export default {
		onLoad(param) {
		  this.getSubCategory(param.categoryId)	
		},
		data() {
			return {
				categoryId: 0,
				currentPageNo: 1,
				totalPage: 0,
				subCategoryList: [],
				productList: [],
				intoView: ''
			}
		},
		methods: {
			getSubCategory(parentId) {
				request({
					  url: this.$host + '/v1/web/category/getCategoryByParentId',
					  method: 'GET',
					  data: {
						 parentId: parentId
					  }
				})
				.then(({ data }) => {
					 this.subCategoryList = data
					 if(data && data.length > 0) {
						 this.currentPageNo = 1
						 this.totalPage = 0
						 this.categoryId = data[0].id
						 this.intoView = 'sw' + this.categoryId
						 this.getProductList()
					 }
				 })
			},
			onSubCategoryTap(e) {
				this.categoryId = e.currentTarget.dataset.id
				this.currentPageNo = 1
				this.totalPage = 0
				this.intoView = 'sw' + e.currentTarget.dataset.id
				this.getProductList()
			},
			toProductPage(e) {
				const productId = e.currentTarget.dataset.productid
				if (productId) {
				  uni.navigateTo({
					url: '/pages/product-detail/product-detail?productId=' + productId
				  })
				}
			},
			getProductList() {
				uni.showLoading()
				request({
				  url: this.$host + '/v1/web/product/search',
				  method: 'POST',
				  data: {
					categoryId: this.categoryId,  
					pageSize: 10,
					pageNo: this.currentPageNo,
				  }
				})
				.then(({ data }) => {
					 if(data) {
						this.productList = data.data
						this.totalPage = data.totalPage
						this.currentPageNo = data.pageNo 
					 }
					 uni.hideLoading()
				 })	
			}
		}
	}
</script>

<style scoped lang="scss">
@import './sub-category.scss';
</style>
