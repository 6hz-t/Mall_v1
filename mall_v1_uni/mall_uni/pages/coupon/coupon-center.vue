<template>
  <view class="coupon_box">
    <view v-if="type === 'user'" class="other_type">
      <view class="text">
        <view>我的优惠券</view>
      </view>
    </view>
    <coupon
      v-for="(item, index) in userCouponList"
      :key="index"
      :item="item"
      :index="index"
      theme="#ff0000"
	  :disable="false"
	  :type="1"
      @receiveCoupon="receiveCoupon"
    />
    <view v-if="type === 'list' || obatinableCouponList.length > 0" class="other_type">
      <view class="text">
        <view>可领取优惠券</view>
      </view>
    </view>
    <coupon
      v-for="(item, index) in obatinableCouponList"
      :key="index"
      :item="item"
      :index="index"
      theme="#ff0000"
	  :disable="false"
	  :type="1"
      @receiveCoupon="receiveCoupon"
    />
  </view>
</template>

<script>
import { request } from '@/utils/http'
import coupon from './coupon.vue'
export default {
  components: {
	coupon  
  },	
  data() {
    return {
      title: '可领取优惠券',
      userCouponList: [],
      obatinableCouponList: [],
      type: 'list',
      couponId: undefined
    }
  },
  onLoad(options) {
    if (options.type) {
      this.type = options.type
    } else {
      this.type = 'list'
    }
    this.loadData()
  },
  methods: {
    async loadData() {
      const that = this
      if (that.type === 'list') {
		request({
		  url: that.$host + '/v1/web/coupon/getObtainableCouponList',
		  method: 'GET',
		  data: {}			
		}).then(({ data }) => {
		  that.obatinableCouponList = data
		  uni.hideLoading()
		})  
      } else if (that.type === 'user'){
		  request({
		    url: that.$host + '/v1/web/coupon/getUserCouponList',
		    method: 'GET',
		    data: {}			
		  }).then(({ data }) => {
		    that.userCouponList = data
		    uni.hideLoading()
		  })  
	  }  
    },
    receiveCoupon(item, index) {
      const that = this
	  request({
	    url: that.$host + '/v1/web/coupon/receiveCoupon',
	    method: 'POST',
	    data: {
			id: item.id
		}			
	  }).then(() => {
		uni.showToast({
		  title: '领取成功',
		})
		that.loadData()
	  }) 
    }
  }
}
</script>

<style lang='scss'>
  	@import "./coupon-center.scss";
</style>