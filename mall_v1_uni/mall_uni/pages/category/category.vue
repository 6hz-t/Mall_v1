<template>
<view class="container">
    <!-- 头部搜索区 -->
    <view class="search-bar">
      <view
        class="search-box"
        @tap="toSearchPage"
      >
        <image
          src="@/static/images/icon/search.png"
          class="search-img"
        />
        <text class="sear-input">
          搜索您想要的商品
        </text>
      </view>
    </view>
    <!-- 滚动内容区 -->
    <view class="main">
      <!-- 左侧菜单start -->
      <scroll-view
        scroll-y="true"
        class="leftmenu"
      >
        <block
          v-for="(item, index) in categoryList"
          :key="index"
        >
          <view
            :class="'menu-item ' + (selIndex==index?'active':'') + ' '"
            :data-index="index"
            :data-id="item.id"
            @tap="onMenuTab"
          >
            {{ item.name }}
          </view>
        </block>
        <view
          v-if="!categoryList || !categoryList.length"
          class="ca-empty"
        >
          {{ categoryList && categoryList.length ? '该分类下暂无商品' : '暂无商品' }}
        </view>
      </scroll-view>
      <!-- 左侧菜单end -->

      <!-- 右侧内容start -->
      <scroll-view
        scroll-y="true"
        class="rightcontent"
      >
<!--        <view class="adver-map">
          <view class="item-a">
            <image
              :src="util.checkFileUrl(categoryImg)"
              mode="widthFix"
            />
          </view>
        </view> -->
        <!-- 子分类 -->
        <view
          v-if="subCategoryList"
          class="th-cate-con"
        >
          <block
            v-for="(thCateItem, index) in subCategoryList"
            :key="index"
          >
            <view class="sub-category">
              <view
                class="sub-category-item"
                :data-categoryid="thCateItem.id"
                :data-parentid="thCateItem.parentId"
                @tap="toSubCategoryPage"
              >
 <!--               <image
                  :src="util.checkFileUrl(thCateItem.pic)"
                  class="more-pic"
                  mode="widthFix"
                /> -->
                <text>{{ thCateItem.name }}</text>
              </view>
            </view>
          </block>
        </view>
        <view
          v-else
          class="cont-item empty"
        >
          该分类下暂无子分类~
        </view>
      </scroll-view>
      <!-- 右侧内容end -->
    </view>
  </view>
</template>

<script>
	import { request } from '@/utils/http'
	export default {
		onLoad(param) {
		  this.getCategory(param.parentId)	
		},	
		data() {
			return {
				categoryList:[],
				subCategoryList:[],
				selIndex: 0,
				parentId: 0
			}
		},
		methods: {
			getCategory() {
			  request({
			  	  url: this.$host + '/v1/web/category/getCategoryByParentId',
			  	  method: 'GET',
			  	  data: {
			  		 parentId: this.parentId
			  	  }
			  })
			  .then(({ data }) => {
			  	 this.categoryList = data
				 if(data) {
					this.getSubCategory(data[0].id) 
				 }
			   })		
			},
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
				 })
			},
			toSearchPage() {
				uni.navigateTo({
				  url: '/pages/search-product/search-product'
				})
			},
			onMenuTab(e) {
				const index = e.currentTarget.dataset.index
				this.getSubCategory(this.categoryList[index].id)
				this.prarentId = this.categoryList[index].id
				this.selIndex = index
			},
			toSubCategoryPage(e) {
				const  categoryId  = e.currentTarget.dataset.categoryid
				if(categoryId) {
					uni.navigateTo({
					  url: '/pages/sub-category/sub-category?categoryId='+categoryId
					})	
				}
			}
		}
	}
</script>

<style lang='scss'>
	@import "./category.scss";
</style>