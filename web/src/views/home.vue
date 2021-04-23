<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
          mode="inline"
          :style="{ height: '100%', borderRight: 0 }"
          @click="handleClick"
      >
        <a-menu-item key="welcome">
          <MailOutlined />
          <span>欢迎</span>
        </a-menu-item>
        <a-sub-menu v-for="item in level1" :key="item.id">
          <template v-slot:title>
            <span><user-outlined />{{ item.name }}</span>
          </template>
          <a-menu-item v-for="child in item.children" :key="child.id">
            <MailOutlined /><span>{{ child.name }}</span>
          </a-menu-item>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <div class="welcome" v-show="isShowWelcome">
        <h1>欢迎进入 Fairy Wiki</h1>
      </div>
      <a-list v-show="!isShowWelcome" item-layout="vertical" size="large"
              :grid="{ gutter: 20, column: 3 }" :data-source="ebooks">
        <template #renderItem="{ item }">
          <a-list-item key="item.name">
            <template #actions>
              <span v-for="{ type, text } in actions" :key="type">
                <component v-bind:is="type" style="margin-right: 8px" />
                {{ text }}
              </span>
            </template>
            <a-list-item-meta :description="item.description">
              <template #title>
                <router-link :to="'/doc?ebookId=' + item.id">
                  {{ item.name }}
                </router-link>
              </template>
              <template #avatar><a-avatar :src="item.cover"/></template>
            </a-list-item-meta>
          </a-list-item>
        </template>
      </a-list>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from 'axios';
import { StarOutlined, LikeOutlined, MessageOutlined } from '@ant-design/icons-vue';
import {Tool} from "@/util/tool";
import {message} from "_ant-design-vue@2.0.0-rc.3@ant-design-vue";
import {Category} from "@/models";


export default defineComponent({
  name: 'Home',
  components: {
    StarOutlined,
    LikeOutlined,
    MessageOutlined,
  },
  setup() {
    console.log("setup");
    let ebooks = ref();
    let categories: Category[];
    let level1 = ref();
    let isShowWelcome = ref(true);
    let categoryId2: string|null = null;

    const pagination = {
      onChange: (page: number) => {
        console.log(page);
      },
      pageSize: 3,
    };

    const actions: Record<string, string>[] = [
      { type: 'StarOutlined', text: '156' },
      { type: 'LikeOutlined', text: '156' },
      { type: 'MessageOutlined', text: '2' },
    ];

    /**
     * 查询所有分类
     **/
    const handleQueryCategory = () => {
      axios.get("/category/all").then((response) => {
        const respData = response.data;

        if (respData.code == 0) {
          categories = respData.data;
          console.log("原始数组：", categories);

          level1.value = [];
          level1.value = Tool.array2Tree(categories, 0);
          console.log("树形结构：", level1);

        } else {
          message.error(respData.msg);
        }
      });
    };

    const handleQueryEbook = () => {
      let queryParams = {
        pageNum: 1,
        pageSize: 1000,
        categoryId2: categoryId2
      }
      axios.get("/ebook/query", {params: queryParams}).then((response) => {
        console.log(response);
        const respData = response.data;
        const pageData = respData.data;
        ebooks.value = pageData.list;
      })
    }

    const handleClick = (value: any) => {
      if (value.key === 'welcome') {
        isShowWelcome.value = true;
      } else {
        categoryId2 = value.key;
        isShowWelcome.value = false;
        handleQueryEbook();
      }
    };

    onMounted(() => {
      handleQueryCategory();
    });


    return {
      ebooks,
      pagination,
      actions,
      level1,
      isShowWelcome,

      handleQueryCategory,
      handleClick,
    }
  }
});
</script>

<style scoped>
.ant-avatar {
  width: 50px;
  height: 50px;
  line-height: 50px;
  border-radius: 8%;
  margin: 5px 0;
}
</style>