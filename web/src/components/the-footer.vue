<template>
  <a-layout-footer style="text-align: center">
    <p v-if="currUser.name">Welcome, {{currUser.name}}</p>
    --- Fairy Wiki ---
  </a-layout-footer>
</template>

<script lang="ts">
import {computed, defineComponent} from "vue";
import store from "@/store";

export default defineComponent({
  name: 'the-footer',
  setup() {
    const currUser = computed(() => store.state.localUser);

    return {
      currUser
    }
  }
})

</script>

<style scoped>

</style>