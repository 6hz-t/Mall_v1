<template>
	<view class="container">
	
	  <!-- 商品详情 -->
	  <view class="goods-detail b-f dis-flex flex-dir-row" v-if="product">
	    <view class="left">
	      <image class="goods-image" :src="product.coverUrl"></image>
	    </view>
	    <view class="right dis-flex flex-box flex-dir-column flex-x-around">
	      <view class="goods-name">
	        <text class="twoline-hide">{{ product.productName }}</text>
	      </view>
	      <view class="dis-flex">
			<view class="t-r">{{ product.model }}</view>
			<view class="t-r">数量：{{ product.quantity }}</view>
	      </view>
	    </view>
	  </view>
	
	  <!-- 服务类型 -->
	  <view class="row-service b-f m-top20">
	    <view class="row-title">服务类型</view>
	    <view class="service-switch dis-flex">
	      <view class="switch-item" v-for="(item, index) in RefundTypeEnum.data" :key="index" :class="{ active: formData.type == item.value }"
	        @click="onSwitchService(item.value)">{{ item.name }}</view>
	    </view>
	  </view>
	
	  <!-- 申请原因 -->
	  <view class="row-textarea b-f m-top20">
	    <view class="row-title">申请原因</view>
	    <view class="content">
	      <textarea class="textarea" v-model="formData.content" maxlength="500" placeholder="请详细填写申请原因"
	        placeholderStyle="color:#ccc"></textarea>
	    </view>
	  </view>
	
	  <!-- 退款金额 -->
	  <view v-if="product && formData.refundType == RefundTypeEnum.RETURN.value" class="row-money b-f m-top20 dis-flex">
	    <view class="row-title">退款金额</view>
	    <view class="money">￥{{ product.payAmount }}</view>
	  </view>
	
	  <!-- 上传凭证 -->
	  <view class="row-voucher b-f m-top20">
	    <view class="row-title">上传凭证 (最多5张)</view>
	    <view class="image-list">
	      <!-- 图片列表 -->
	      <view class="image-preview" v-for="(image, imageIndex) in imageList" :key="imageIndex">
	        <text class="image-delete iconfont icon-shanchu" @click="deleteImage(imageIndex)"></text>
	        <image class="image" mode="aspectFill" :src="image.path"></image>
	      </view>
	      <!-- 上传图片 -->
	      <view v-if="imageList.length < maxImageLength" class="image-picker" @click="chooseImage()">
	        <text class="choose-icon iconfont icon-camera"></text>
	        <text class="choose-text">+上传图片</text>
	      </view>
	    </view>
	  </view>
	
	  <!-- 底部操作按钮 -->
	  <view class="footer-fixed">
	    <view class="btn-wrapper">
	      <view class="btn-item btn-item-main" :class="{ disabled }" @click="handleSubmit()">确认提交</view>
	    </view>
	  </view>
	
	</view>
</template>

<script>
	import { RefundTypeEnum } from '@/common/enum/refund'
	import { request } from '@/utils/http'
	
	const maxImageLength = 5
	
	export default {
		/**
		 * 生命周期函数--监听页面加载
		 */
		onLoad(param) {
		  this.tradeCode = param.tradeCode
		  this.tradeItemId = param.tradeItemId
		  // 获取订单商品详情
		  this.getTradeItemDetail()
		},
		data() {
			return {
				// 枚举类
				RefundTypeEnum,
				// 正在加载
				isLoading: true,
				// 订单明细id
				tradeCode: '',
				tradeItemId: null,
				// 订单商品详情
				product: {},
				// 表单数据
				formData: {
				  // 图片上传成功的文件ID集
				  images: [],
				  // 服务类型
				  refundType: 10,
				  // 申请原因
				  content: ''
				},
				// 用户选择的图片列表
				imageList: [],
				// 最大图片数量
				maxImageLength,
				// 按钮禁用
				disabled: false
			}
		},
		methods: {
			// 获取订单商品详情
			getTradeItemDetail() {
			  uni.showLoading()
			  request({
			    url: this.$host + '/v1/web/trade/getTradeItem',
			    method: 'POST',
			    data: {
			  	  tradeCode: this.tradeCode
			    }
			  })
			  .then(({ data }) => {
			  	 this.product = data
				 this.tradeItemId = data.id
			  	 uni.hideLoading()
			   })	
			},
			// 切换类型
			onSwitchService(value) {
			  this.formData.refundType = value
			},
			
			// 选择图片
			chooseImage() {
			  const app = this
			  const oldImageList = app.imageList
			  // 选择图片
			  uni.chooseImage({
			    count: maxImageLength - oldImageList.length,
			    sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
			    sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
			    success({ tempFiles }) {
			      // tempFiles = [{path:'xxx', size:100}]
			      app.imageList = oldImageList.concat(tempFiles)
			    }
			  });
			},
			
			// 删除图片
			deleteImage(imageIndex) {
			  this.imageList.splice(imageIndex, 1)
			},
			
			// 表单提交
			handleSubmit() {
			  const app = this
			  const { imageList } = app
			  // 判断是否重复提交
			  if (app.disabled === true) return false
			  // 表单验证
			  if (!app.formData.content.trim().length) {
				uni.showToast({
				  title: '请填写申请原因',
				  icon: 'none'
				})
			    return false
			  }
			  // 按钮禁用
			  app.disabled = true
			  // 判断是否需要上传图片
			  if (imageList.length > 0) {
			    app.uploadFile()
			      .then(() => app.onSubmit())
			      .catch(err => {
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
			
			// 提交到后端
			onSubmit() {
			  const app = this
			  request({
			    url: this.$host + '/v1/web/refund/doRefund',
			    method: 'POST',
			    data: {
			  	 tradeCode: this.tradeCode,
				 tradeItemId: this.tradeItemId,
				 refundType: this.formData.refundType,
				 images: this.formData.images,
				 content: this.formData.content
			    }
			  })
			  .then(({ data }) => {
			  	 uni.navigateBack()
			   })
			},
			
			// 上传图片
			uploadFile() {
			  const app = this
			  // 批量上传
			  return new Promise((resolve, reject) => {
			    if (this.imageList.length > 0) {
				  uni.uploadFile({
				    url: this.$host + '/v1/image/batchUpload',
					header: {
					  Authorization: 'Basic@' + uni.getStorageSync('Token')
					},
				    data: {
				  	  file: this.imageList,
				    }
				  })
				  .then(({data}) => {
					 let json = JSON.parse(data)
				  	 const downloadUrl = json.data.downloadUrl
					 if(downloadUrl) {
						 this.formData.images = downloadUrl.split(",")
					 }
					  resolve()
				   })	
			    } else {
			      resolve()
			    }
			  })
			}
			
		}
	}
</script>

<style scoped lang="scss">
@import './refund.scss';
</style>
