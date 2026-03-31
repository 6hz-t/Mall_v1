<template>
  <view class="coupon-item">
    <view class="coupon-money">
      <view class="nick">{{ item.name }}</view>
      <view :style="{color:theme}" class="layof">
		  <view v-if="item.type<=3">
			  ￥{{ item.offMoney  }}
		  </view>
		  <view v-if="item.type>3">
		  		{{ item.discount/10  }}折
		  </view>
	  </view>
      <view v-if="item.currentUserReceived===false" class="coupon-rule">在{{ item.useEndTimeStr }}前有效， 总共有{{ item.quantity }}张，已领{{ item.receiveCount }}张。</view>
      <view v-if="item.currentUserReceived===true" class="coupon-rule">在领取后{{ item.validDays }}天内有效。总共有{{ item.quantity }}张，已领{{ item.receiveCount }}张。</view>
      <view class="coupon-rule"><view v-if="item.minMoney">满{{ item.minMoney }}元可用，</view>{{ item.productName?'限' + item.productName + '可用': '全品类可用' }}</view>
    </view>
    <view v-if="item.currentUserReceived===true" class="disable-btn" @click="chooseCoupon">{{ item.receiveCount >= item.quantity ? '已抢完' : item.hasUsed === true? '已使用': '已领取'}}</view>
    <view v-if="item.currentUserReceived===false" :style="{color:color, borderColor:color, background:solid}" class="get-btn" @click="receiveCoupon">{{ item.receiveCount >= item.quantity ? '已抢完' :  (type === 2 ? '立即使用': '立即领取') }}</view>
  </view>
</template><!--  -->

<script>
export default {
  components: {

  },
  props: {
    item: {
      type: Object,
      required: true
    },
    disable: {
      Boolean: false,
    },
    type: {
      type: Number,
      default: 1
    },
    theme: {
      type: String,
      default: '#ff9000'
    },
    solid: {
      type: String,
      default: '#ffffff'
    },
    color: {
      type: String,
      default: '#ff9000'
    }
  },
  data() {
    return {

    }
  },
  methods: {
    receiveCoupon() {
	  if(this.item.currentUserReceived === true) {
		  return
	  }	
      this.$emit('receiveCoupon', this.item, this.index)
    },
  }
}
</script>

<style lang='scss'>
	@import "./coupon.scss";
</style>