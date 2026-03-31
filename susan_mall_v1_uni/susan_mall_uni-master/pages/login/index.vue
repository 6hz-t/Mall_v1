<template>
  <view v-if="isLoad" class="login">
    <Main :isParty="isParty" :partyData="partyData"/>
  </view>
</template>

<script>
  import Main from './components/main'

  export default {
    components: {
      Main
    },

    data() {
      return {
        isLoad: false,
        // 是否存在第三方用户信息
        isParty: false,
        // 第三方用户信息数据
        partyData: {}
      }
    },

    /**
     * 生命周期函数--监听页面加载
     */
    async onLoad(options) {
      // 数据加载完成
      this.isLoad = true
    },

    methods: {
    }
  }
</script>

<style lang="scss" scoped>

</style>