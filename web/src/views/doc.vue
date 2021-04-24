<template>
  <a-layout>
    <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
      <h3 v-if="level1.length === 0">对不起，找不到相关文档！</h3>
      <a-row>
        <a-col :span="6">
          <a-tree
              v-if="level1.length > 0"
              :tree-data="level1"
              @select="onSelect"
              :replaceFields="{title: 'name', key: 'id', value: 'id'}"
              :defaultSelectedKeys="defaultSelectedKeys"
          >
          </a-tree>
        </a-col>
        <a-col :span="18">
          <div class="wangeditor" :innerHTML="htmlContent"></div>
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
    const htmlContent = ref<string>();  // 所要展示的文档的内容
    htmlContent.value = "";
    const defaultSelectedKeys = ref();  // 初始时默认选中的文档 keys
    defaultSelectedKeys.value = [];

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

          if (Tool.isNotEmpty(level1)) {
            defaultSelectedKeys.value = [level1.value[0].id];  // 默认选中 level1 的第一个文档
            handleQueryDocContent(level1.value[0].id);  // 并对该默认选中的文档进行一次内容查询并展示出来
          }
        } else {
          message.error(respData.message);
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
      defaultSelectedKeys
    }
  }
});
</script>

<style>
/* wangeditor默认样式, 参照: http://www.wangeditor.com/doc/pages/02-%E5%86%85%E5%AE%B9%E5%A4%84%E7%90%86/03-%E8%8E%B7%E5%8F%96html.html */
/* table 样式 */
.wangeditor table {
  border-top: 1px solid #ccc;
  border-left: 1px solid #ccc;
}
.wangeditor table td,
.wangeditor table th {
  border-bottom: 1px solid #ccc;
  border-right: 1px solid #ccc;
  padding: 3px 5px;
}
.wangeditor table th {
  border-bottom: 2px solid #ccc;
  text-align: center;
}

/* blockquote 样式 */
.wangeditor blockquote {
  display: block;
  border-left: 8px solid #d0e5f2;
  padding: 5px 10px;
  margin: 10px 0;
  line-height: 1.4;
  font-size: 100%;
  background-color: #f1f1f1;
}

/* code 样式 */
.wangeditor code {
  display: inline-block;
  *display: inline;
  *zoom: 1;
  background-color: #f1f1f1;
  border-radius: 3px;
  padding: 3px 5px;
  margin: 0 3px;
}
.wangeditor pre code {
  display: block;
}

/* ul ol 样式 */
.wangeditor ul, ol {
  margin: 10px 0 10px 20px;
}

/* 和antdv p冲突，覆盖掉 */
.wangeditor blockquote p {
  font-family:"YouYuan";
  margin: 20px 10px !important;  /* 加上 !important 以提高优先级从而覆盖掉原 Ant Design 的p标签样式*/
  font-size: 16px !important;
  font-weight:600;
}
</style>