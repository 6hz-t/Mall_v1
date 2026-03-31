<template>
 <view class="container">
    <!--input列表 -->
    <view class="input-box">
      <view class="section">
        <text>收货人</text>
        <input
          placeholder="姓名"
          type="text"
          maxlength="15"
          :value="detailAddressEntity.receiverName"
          @input="onReceiverInput"
        >
      </view>
      <view class="section">
        <text>手机号码</text>
        <input
          placeholder="11位手机号码"
          type="number"
          maxlength="11"
          :value="detailAddressEntity.receiverPhone"
          @input="onPhoneInput"
        >
      </view>
      <view
        class="section"
        @tap="selectArea"
      >
        <text>所在地区</text>
        <view class="pca">
          {{ detailAddressEntity.province }} {{ detailAddressEntity.city }} {{ detailAddressEntity.district }}
        </view>
        <view
          class="animation-element-wrapper"
          :animation="animation"
		  v-if="showPop"
        >
          <view
            class="animation-element"
          >
            <text
              class="right-bt"
              @tap.stop="hiddenFloatView"
            >
              确定
            </text>
            <view class="line" />
            <picker-view
              indicator-style="height: 50rpx;"
              :value="valueList"
              @change="bindChange"
            >
              <!--省-->
              <picker-view-column>
                <view
                  v-for="(item, indexs) in provinceList"
                  :key="indexs"
				  :id="item.id"
                >
                  {{ item.name }}
                </view>
              </picker-view-column>
              <!--地级市-->
              <picker-view-column>
                <view
                  v-for="(item, indexss) in cityList"
                  :key="indexss"
				  :id="item.id"
                >
                  {{ item.name }}
                </view>
              </picker-view-column>
              <!--区县-->
              <picker-view-column>
                <view
                  v-for="(item, indexsss) in districtList"
                  :key="indexsss"
				  :id="item.id"
                >
                  {{ item.name }}
                </view>
              </picker-view-column>
            </picker-view>
          </view>
        </view>

        <view class="arrow">
          <image src="@/static/images/icon/more.png" />
        </view>
      </view>
      <view class="section">
        <text>详细地址</text>
        <input
          placeholder="如楼号/单元/门牌号"
          type="text"
		  v-if="detailAddressEntity"
          :value="detailAddressEntity.detailAddress"
          @input="onDetailAddressInput"
        >
      </view>
	  <view class="section">
	    <text>邮编</text>
	    <input
	      placeholder="邮编"
	      type="text"
	      maxlength="15"
	      :value="detailAddressEntity.postCode"
	      @input="onPostCodeInput"
	    >
	  </view>
    </view>
    <!-- end input列表 -->
    <!-- 功能按钮 -->
    <view class="btn-box">
      <view
        class="keep btn"
        @tap="onSaveAddress"
      >
        <text>保存收货地址</text>
      </view>

      <view
        v-if="detailAddressEntity && detailAddressEntity.id"
        class="clear btn"
        @tap="onDeleteAddress"
      >
        <text>删除收货地址</text>
      </view>
    </view>
    <!-- end 功能按钮 -->
  </view>
</template>

<script>
	import { request } from '@/utils/http'
	export default {
		onLoad(param){
	      if(param.id) {
			  this.getDeliveryAddressInfo(param.id)
		  } else {
			  this.intAreaData()
		  }		 
		},
		data() {
			return {
				showPop:false,
				animation: '',
				detailAddressEntity: {},
				indexList: [],
				valueList: [0,0,0],
				provinceList: [],
				cityList: [],
				districtList: [],
			}
		},
		methods: {
			getDeliveryAddressInfo(id) {
				uni.showLoading()
				request({
					  url: this.$host + '/v1/web/deliveryAddress/getDetail?id='+id,
					  method: 'GET',
					  data: {}
				})
				.then(({ data }) => {
					 this.detailAddressEntity = data
				 })	
				uni.hideLoading()
			},
			intAreaData() {
				request({
					  url: this.$host + '/v1/web/area/getAreaByParentId?parentId='+0,
					  method: 'GET',
					  data: {}
				})
				.then(({ data }) => {
					 if(data) {
						this.provinceList = data
						if (this.detailAddressEntity.provinceId) {
						  for (const index in data) {
						    if (data[index].id === this.detailAddressEntity.provinceId) {
						      this.valueList = [parseInt(index), this.valueList[1], this.valueList[2]]
						    }
						  }
						}
						this.getAreaList() 
					 }
				 })	
			},
			getAreaList() {
				if(this.detailAddressEntity.provinceId) {
					this.getCityAreaList(this.detailAddressEntity.provinceId)
				} else {
					this.indexList = this.valueList
					if(this.provinceList && this.provinceList.length > 0) {
						this.getCityAreaList(this.provinceList[0].id)
					}
				}
			},
			getCityAreaList(parentId) {
				request({
					  url: this.$host + '/v1/web/area/getAreaByParentId?parentId='+parentId,
					  method: 'GET',
					  data: {}
				})
				.then(({ data }) => {
					this.cityList = data
					if (this.detailAddressEntity.cityId) {
					  for (const index in data) {
					    if (data[index].id === this.detailAddressEntity.cityId) {
					      this.valueList = [this.valueList[0], parseInt(index), this.valueList[2]]
					    }
					  }
					} else {
						this.detailAddressEntity.cityId = this.cityList[0].id
						this.valueList = [this.valueList[0], 0, this.valueList[2]]
					}
					this.getDistrictAreaList(this.detailAddressEntity.cityId)
				 })	
			},
			getDistrictAreaList(parentId) {
				request({
					  url: this.$host + '/v1/web/area/getAreaByParentId?parentId='+parentId,
					  method: 'GET',
					  data: {}
				})
				.then(({ data }) => {
					this.districtList = data
					if (this.detailAddressEntity.districtId) {
					  for (const index in data) {
					    if (data[index].id === this.detailAddressEntity.districtId) {
					      this.valueList = [this.valueList[0], this.valueList[1], parseInt(index)]
					    }
					  }
					} else {
						this.detailAddressEntity.districtId = this.districtList[0].id
						this.valueList = [this.valueList[0], this.valueList[1], 0]
					}
				 })	
			},
			onReceiverInput(e) {
				this.detailAddressEntity.receiverName = e.detail.value
			},
			onPostCodeInput(e) {
				this.detailAddressEntity.postCode = e.detail.value
			},
			selectArea(e) {
			  this.showPop = true
			  if(this.showPop) {
				 this.intAreaData()
				 this.indexList = this.valueList  
			  }
			},
			bindChange(e) {
				console.info("222")
				const val = e.detail.value
				//如果选择了省份
				if(this.indexList[0] != val[0]) {
					this.detailAddressEntity.provinceId = this.provinceList[val[0]].id
					this.valueList[1] = 0
					this.valueList[2] = 0
					this.detailAddressEntity.cityId = null
					this.detailAddressEntity.districtId = null
					this.getCityAreaList(this.detailAddressEntity.provinceId)
					this.valueList = [val[0], 0, 0]
				}
				//如果选择了城市
				else if(this.indexList[1] != val[1]) { 
					this.detailAddressEntity.cityId = this.cityList[val[1]].id
					this.valueList[2] = 0
					this.detailAddressEntity.districtId = null
					this.getDistrictAreaList(this.detailAddressEntity.cityId)	
					this.valueList = [this.valueList[0], val[1], 0]
				}
				//如果选择了区县
				else if (this.indexList[2] != val[2]) { 
					this.detailAddressEntity.districtId = this.districtList[val[2]].id
					this.valueList = [this.valueList[0], this.valueList[1], val[2]]
				}
			},
			onPhoneInput(e) {
				this.detailAddressEntity.receiverPhone = e.detail.value
			},
			hiddenFloatView(e) {
				this.showPop = false
			},
			onDetailAddressInput(e) {
				this.detailAddressEntity.detailAddress = e.detail.value
			},
			onSaveAddress() {
				if (!this.detailAddressEntity.receiverName) {
				  uni.showToast({
				    title: '请输入姓名',
				    icon: 'none'
				  })
				  return
				}
				if (!this.detailAddressEntity.receiverPhone) {
				  uni.showToast({
				    title: '请输入手机号码',
				    icon: 'none'
				  })
				  return
				}
				if (!this.detailAddressEntity.detailAddress) {
				  uni.showToast({
				    title: '请输入详细地址',
				    icon: 'none'
				  })
				  return
				}
				if (!this.detailAddressEntity.postCode) {
				  uni.showToast({
				    title: '请输入邮编',
				    icon: 'none'
				  })
				  return
				}
				let provinceEntity = this.provinceList[this.valueList[0]]
				let cityEntity = this.cityList[this.valueList[1]]
				let districtEntity = this.districtList[this.valueList[2]]
				if (!provinceEntity||!cityEntity||!districtEntity) {
				  uni.showToast({
				    title: '请选择省市区',
				    icon: 'none'
				  })
				  return
				}
				
				uni.showLoading()
				request({
				  url: this.$host + '/v1/web/deliveryAddress/save',
				  method: 'POST',
				  data: {
					  id: this.detailAddressEntity.id,
					  receiverName: this.detailAddressEntity.receiverName,
					  receiverPhone: this.detailAddressEntity.receiverPhone,
					  provinceId: provinceEntity.id,
					  province: provinceEntity.name,
					  cityId: cityEntity.id,
					  city: cityEntity.name,
					  districtId: districtEntity.id,
					  district: districtEntity.name,
					  detailAddress: this.detailAddressEntity.detailAddress,
					  postCode: this.detailAddressEntity.postCode
				  }
				})
				  .then(() => {
				    uni.hideLoading()
				    uni.navigateTo({
				      url: '/pages/delivery-address/delivery-address'
				    })
				  })
			},
			onDeleteAddress() {
				const that = this
				uni.showModal({
				  title: '',
				  content: '确定要删除此收货地址吗？',
				  confirmColor: '#eb2444',
				
				  success (res) {
				    if (res.confirm && that.detailAddressEntity) {
				      const id = that.detailAddressEntity.id
				      uni.showLoading()
				      request({
				        url: that.$host +'/v1/web/deliveryAddress/deleteByIds',
				        method: 'POST',
						data: [id]
				      })
				        .then(() => {
				          uni.hideLoading()
				          uni.navigateTo({
				            url: '/pages/delivery-address/delivery-address'
				          })
				        })
				    }
				  }
				})
			}
		}
	}
</script>

<style scoped lang="scss">
@import './edit-address.scss';
</style>
