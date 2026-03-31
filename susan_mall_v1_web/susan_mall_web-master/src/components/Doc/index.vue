<template>
  <div>
    <svg-icon icon-class="doc" @click="click" />
  </div>
</template>

<script>
export default {
  name: 'Doc',
  methods: {
    click() {
      window.open('http://mall.susan.net.cn/pages/010101/', '_blank')
    }
  }
}
</script>
