<template>
	<view class="container">
	  <view class="recent-news">
	    <block
	      v-for="(item, index) in noticeList"
	      :key="index"
	    >
	      <view
	        class="news-item"
	        :data-id="item.id"
	        @tap="toNoticeDetailPage"
	      >
	        <view class="news-item-title">
	          {{ item.title }}
	        </view>
	        <view class="news-item-date">
	          {{ item.createTime }}
	        </view>
	      </view>
	    </block>
	    <view
	      v-if="!noticeList"
	      class="empty"
	    >
	      暂无数据
	    </view>
	  </view>
	</view>
</template>

<script>
	import { request } from '@/utils/http'
	export default {
		onLoad(param) {
		  this.getNoticeList()		
		},
		data() {
			return {
				currentPageNo: 0,
				noticeList: []
			}
		},
		methods: {
			getNoticeList() {
				uni.showLoading()
				request({
				  url: this.$host + '/v1/web/index/searchIndexNoticeByPage',
				  method: 'POST',
				  data: {
					pageSize: 10,
					pageNo: this.currentPageNo + 1,
				  }
				})
				.then(({ data }) => {
					 this.noticeList = data.data
					 this.totalPage = data.totalPage
					 this.currentPageNo = data.pageNo
					 uni.hideLoading()
				 })	
			},
			toNoticeDetailPage(e) {
				const id = e.currentTarget.dataset.id
				if (id) {
				  uni.navigateTo({
					url: '/pages/notice-detail/notice-detail?id=' + id
				  })
				}
			}
		}
	}
</script>

<style scoped lang="scss">
@import './notice-list.scss';
</style>