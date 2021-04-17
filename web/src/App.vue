<template>
  <a-layout>
    <the-header></the-header>  <!-- 公共不变的头部-->
    <router-view/>  <!-- 中间的内容部分，是变化的，用一个该标签作为占位符-->
    <the-footer></the-footer>  <!-- 公共不变的尾部-->
  </a-layout>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import TheHeader from '@/components/the-header.vue'; // @ is an alias to /src
import TheFooter from '@/components/the-footer.vue';

export default defineComponent({
  name: 'app',
  components: {
    TheHeader,
    TheFooter
  },
});
</script>

