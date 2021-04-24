<template>
  <a-layout>
    <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
      <a-row>
        <a-col :span="6">
          <a-tree
              v-if="level1.length > 0"
              :tree-data="level1"
              @select="onSelect"
              :replaceFields="{title: 'name', key: 'id', value: 'id'}"
              :defaultExpandAll="true"
          >
          </a-tree>
        </a-col>
        <a-col :span="18">
          <div :innerHTML="htmlContent"></div>
        </a-col>
      </a-row>
    </a-layout-content>
  </a-layout>
</template>


<script lang="ts">
import { defineComponent, onMounted, ref} from 'vue';
import axios from 'axios';
import {message} from 'ant-design-vue';
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";

export default defineComponent({
  name: 'AdminDoc',
  setup() {
    const route = useRoute();
    const docs = ref();
    const htmlContent = ref<string>();
    htmlContent.value = "";

    /**
     * 一级文档树，children属性就是二级文档
     * [{
     *   id: "",
     *   name: "",
     *   children: [{
     *     id: "",
     *     name: "",
     *   }]
     * }]
     */
    const level1 = ref(); // 一级文档树，children属性就是二级文档
    level1.value = [];

    /**
     * 数据查询
     **/
    const handleQueryDoc = () => {
      axios.get("/doc/query/" + route.query.ebookId).then((response) => {
        const respData = response.data;
        if (respData.code === 0) {
          docs.value = respData.data;

          level1.value = [];
          level1.value = Tool.array2Tree(docs.value, 0);
        } else {
          message.error(respData.message);
        }
      });
    };

    /**
     * 内容查询
     **/
    const handleQueryDocContent = (docId: string) => {
      axios.get("/doc/read-content/" + docId).then((response) => {
        const respData = response.data;
        if (respData.code === 0) {
          htmlContent.value = respData.data;
          console.log(htmlContent);
        } else {
          message.error(respData.msg);
        }
      });
    };

    const onSelect = (selectedKeys: any, info: any) => {
      console.log('selected', selectedKeys, info);
      if (Tool.isNotEmpty(selectedKeys)) {
        // 加载内容
        handleQueryDocContent(selectedKeys[0]);
      }
    };

    onMounted(() => {
      handleQueryDoc();
    });

    return {
      level1,

      htmlContent,
      onSelect,
    }
  }
});
</script>