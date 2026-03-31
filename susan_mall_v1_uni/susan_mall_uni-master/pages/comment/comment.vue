<template>
	<view v-if="!isLoading" class="container">
	  <view class="goods-list">
	    <view class="goods-item" v-for="(item, index) in orderTradeItemList" :key="index">
	      <!-- 商品详情 -->
	      <view class="goods-main">
	        <!-- 商品图片 -->
	        <view class="goods-image">
	          <image class="image" :src="item.coverUrl" mode="scaleToFill"></image>
	        </view>
	        <!-- 商品信息 -->
	        <view class="goods-content">
	          <view class="goods-title">
				  <text class="twoline-hide">{{ item.productName }}</text></view>
	            <view class="goods-props-item">
	              <text>{{ item.model }}</text>
	            </view>
				<text class="number">
				  数量：{{ item.quantity }}
				</text>
	          </view>
	        </view>
	        <!-- 交易信息 -->
	        <view class="goods-trade">
	          <view class="goods-price">
	            <text class="unit">总金额：￥</text>
	            <text class="unit">{{ item.amount }}</text>
	          </view>
	        </view>
	    
	      <!-- 选择评价 -->
	      <view class="score-row">
			<uni-section title="商品打分" type="line" padding>
				<uni-rate v-model="formData[index].rating" @change="onChange" />
			</uni-section>
	      </view>
	
	      <!-- 评价内容 -->
	      <view class="form-content">
	        <textarea class="textarea" v-model="formData[index].content" maxlength="500" placeholder="请输入评价内容"></textarea>
	      </view>
	
	      <!-- 图片列表 -->
<!-- 	      <view class="image-list">
	        <view class="image-preview" v-for="(image, imageIndex) in formData[index].imageList" :key="imageIndex">
	          <text class="image-delete iconfont icon-shanchu" @click="deleteImage(index, imageIndex)"></text>
	          <image class="image" mode="aspectFill" :src="image.path"></image>
	        </view>
	        <view v-if="!formData[index].imageList || formData[index].imageList.length < maxImageLength" class="image-picker"
	          @click="chooseImage(index)">
	          <text class="choose-icon iconfont icon-camera"></text>
	          <text class="choose-text">+上传图片</text>
	        </view>
	      </view> -->
	    </view>
	  </view>
	  <!-- 底部操作按钮 -->
	  <view class="footer-fixed">
	    <view class="btn-wrapper">
	      <view class="btn-item btn-item-main" @click="handleSubmit()">确认提交</view>
	    </view>
	  </view>
  </view>
</template>

<script>
	import { request } from '@/utils/http'
	export default {
		onLoad(param) {
		  this.tradeCode = param.tradeCode	
		  this.getProductList(this.tradeCode)	
		},
		components: {},
		data() {
			return {
				// 正在加载
				isLoading: true,
				// 当前订单code
				tradeCode: '',
				// 待评价商品列表
				orderTradeItemList: [],
				// 表单数据
				formData: [],
				// 最大图片数量
				maxImageLength: 5,
				// 按钮禁用
				disabled: false
			}
		},
		methods: {
			onChange(e) {
				
			},
			getProductList(tradeCode) {
				if(tradeCode) {
					request({
						  url: this.$host + '/v1/web/trade/getDetail/'+tradeCode,
						  method: 'GET',
						  data: {}
					})
					.then(({ data }) => {
						 this.orderTradeItemList = data.orderTradeItemList
						 this.deliveryAddressWebEntity = data.deliveryAddressWebEntity
						 this.tradeCouponWebEntity = data.tradeCouponWebEntity
						 this.tradeCode = data.code
						 this.initFormData()
						 this.isLoading = false
					 })	
					uni.hideLoading()
				}
			},
			// 初始化form数据
			initFormData() {
			  const { orderTradeItemList } = this
			  const formData = orderTradeItemList.map(item => {
			    return {
			      productId: item.productId,
			      code: item.code,
			      rating: 0,
			      content: '',
			      imageList: [],
			      uploaded: []
			    }
			  })
			  this.formData = formData
			},
			setScore(index, rating) {
				this.formData[index].rating = rating
			},
			deleteImage(index, imageIndex) {
			  this.formData[index].imageList.splice(imageIndex, 1)
			},
			chooseImage(index) {
				const app = this
				const oldImageList = app.formData[index].imageList
				// 选择图片
				uni.chooseImage({
				  count: this.maxImageLength - oldImageList.length,
				  sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
				  sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
				  success({ tempFiles }) {
				    // tempFiles = [{path:'xxx', size:100}]
				    app.formData[index].imageList = oldImageList.concat(tempFiles)
				  }
				});
			},
			handleSubmit() {
				const app = this
				// 判断是否重复提交
				if (app.disabled === true) return false
				// 按钮禁用
				app.disabled = true
				// 判断是否需要上传图片
				const imagesLength = app.getImagesLength()
				if (imagesLength > 0) {
				  app.uploadFile()
				    .then(() => {
				      console.log('then')
				      app.onSubmit()
				    })
				    .catch(err => {
				      console.log('catch')
				      app.disabled = false
				      if (err.statusCode !== 0) {
				        app.$toast(err.errMsg)
				      }
				      console.log('err', err)
				    })
				} else {
				  app.onSubmit()
				}
			},
			onSubmit() {
			  const app = this
			  request({
			  	  url: this.$host + '/v1/web/product/saveProductComment',
			  	  method: 'POST',
			  	  data: {
					  tradeCode: this.tradeCode,
					  productCommentWebEntityList: this.formData
				  }
			  })
			  .then(({ data }) => {
				uni.showToast({
				  title: '评价成功',
				  icon: 'none'
				})
				setTimeout(() => {
				  uni.navigateTo({
				   url: '/pages/order-list/order-list'
				 })
				}, 1500)
			   })	
			},
			getImagesLength() {
			  const { formData } = this
			  let imagesLength = 0
			  formData.forEach(item => {
			    if (item.content.trim()) {
			      imagesLength += item.imageList.length
			    }
			  })
			  return imagesLength
			},
			// 上传图片
			uploadFile() {
			  const app = this
			  const { formData } = app
			  // 整理上传文件路径
			  const files = []
			  formData.forEach((item, index) => {
			    if (item.content.trim() && item.imageList.length) {
			      const images = item.imageList.map(image => image)
			      files.push({ formDataIndex: index, images })
			    }
			  })
			  // 批量上传
			  return new Promise((resolve, reject) => {
			    Promise.all(files.map((file, index) => {
			        return new Promise((resolve, reject) => {
			          UploadApi.image(file.images)
			            .then(fileIds => {
			              app.formData[index].uploaded = fileIds
			              resolve(fileIds)
			            })
			            .catch(reject)
			        })
			      }))
			      .then(resolve, reject)
			  })
			}
		}
	}
</script>
<style lang='scss'>
	@import "./comment.scss";
</style>