<template>
 <view class="container">
    <view class="news-detail">
      <view class="news-detail-title">
        {{ noticeDetail.title }}
      </view>
      <rich-text
        class="content"
        :nodes="noticeDetail.content"
      />
    </view>
  </view>
</template>

<script>
	export default {
		onLoad(param) {
		  this.getNoticeDetail(param.id	)		
		},
		data() {
			return {
				noticeDetail: {}
			}
		},
		methods: {
			getNoticeDetail(id) {
				uni.showLoading()
				uni.request({
				  url: this.$host + '/v1/web/index/getIndexNoticeDetail?id=' + id,
				  method: 'GET',
				  data: {
				  }
				})
				.then(({ data }) => {
					 this.noticeDetail = data.data
					 uni.hideLoading()
				 })	
			}
		}
	}
</script>

<style scoped lang="scss">
@import './notice-detail.scss';
</style>
