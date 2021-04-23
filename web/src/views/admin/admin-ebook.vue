<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <p>
        <a-form
            layout="inline"
            :model="ebookQueryForm"
            @finish="handleQueryFormSubmit(ebookQueryForm)"
        >
          <a-form-item>
            <a-input v-model:value="ebookQueryForm.name" placeholder="ebook name">
              <template #prefix><EyeOutlined style="color: rgba(0, 0, 0, 0.25)" /></template>
            </a-input>
          </a-form-item>
          <a-form-item>
            <a-button
                type="primary"
                html-type="submit"
                size="large"
                :disabled="ebookQueryForm.name === ''"
            >
              查询
            </a-button>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="add()" size="large">新增</a-button>
          </a-form-item>
        </a-form>
      </p>
      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="ebooks"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >
        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar" />
        </template>
        <template v-slot:category="{ text, record }">
          <span>{{ getCategoryName(record.category1Id) }} / {{ getCategoryName(record.category2Id) }}</span>
        </template>
        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <router-link :to="'/admin/doc?ebookId=' + record.id">
              <a-button type="primary">
                文档管理
              </a-button>
            </router-link>
            <a-button type="primary" @click="edit(record)">
              编辑
            </a-button>
            <a-popconfirm
                title="确认删除？"
                ok-text="Yes"
                cancel-text="No"
                @confirm="handleDeleteEbook(record.id)"
            >
              <a-button type="danger">删除</a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>

  <a-modal
      title="电子书表单"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <a-form :model="ebook" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="封面">
        <a-input v-model:value="ebook.cover" />
      </a-form-item>
      <a-form-item label="名称">
        <a-input v-model:value="ebook.name" />
      </a-form-item>
      <a-form-item label="分类">
        <a-cascader v-model:value="categoryIds"
                    :field-names="{ label: 'name', value: 'id', children: 'children' }"
                    :options="level1"
                    placeholder="Please select" />
      </a-form-item>
      <a-form-item label="描述">
        <a-input v-model:value="ebook.description" type="textarea" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, UnwrapRef, reactive } from 'vue';
import axios from 'axios';
import { message } from 'ant-design-vue'
import { Tool } from "@/util/tool";
import {Category, Ebook, EbookQueryForm} from "@/models"

export default defineComponent({
  name: 'AdminEbook',
  setup() {
    const ebookQueryForm: UnwrapRef<EbookQueryForm> = reactive({
      name: ''
    });

    const ebooks = ref();
    const pagination = ref({
      current: 1,
      pageSize: 4,
      total: 0
    });
    const loading = ref(false);

    const columns = [
      {
        title: '封面',
        dataIndex: 'cover',
        slots: { customRender: 'cover' }
      },
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '分类',
        slots: { customRender: 'category' }
      },
      {
        title: '文档数',
        dataIndex: 'docCount'
      },
      {
        title: '阅读数',
        dataIndex: 'viewCount'
      },
      {
        title: '点赞数',
        dataIndex: 'voteCount'
      },
      {
        title: 'Action',
        key: 'action',
        slots: { customRender: 'action' }
      }
    ];


    /**
     * 数据查询
     **/
    const handleQueryEbooks = (queryParams: any) => {
      loading.value = true;
      axios.get("/ebook/query", {
        params: {
          pageNum: queryParams.pageNum,
          pageSize: queryParams.pageSize,
          name: queryParams.name,
        }
      }).then((response) => {
        loading.value = false;
        const respData = response.data;

        if (respData.code == 0) {
          const pageData = respData.data;
          ebooks.value = pageData.list;

          // 重置分页按钮
          pagination.value.current = queryParams.pageNum;
          pagination.value.total = pageData.total;
        } else {
          message.error(respData.msg);
        }
      });
    };

    /**
     * 根据表单提交的数据进行查询
     **/
    const handleQueryFormSubmit = (ebookForm: EbookQueryForm) => {
      handleQueryEbooks({
        pageNum: 1,
        pageSize: 4,
        name: ebookForm.name,
      });
    };

    /**
     * 表格点击页码时触发
     */
    const handleTableChange = (pagination: any) => {
      console.log("看看自带的分页参数都有啥：", pagination);
      handleQueryEbooks({
        pageNum: pagination.current,
        pageSize: pagination.pageSize
      });
    };

    // -------- 表单 ---------
    const categoryIds = ref<string[]>([]);

    const emptyEbook = {
      id: '',
      name: '',
      category1Id: '',
      category2Id: '',
      description: '',
      cover: '',
      docCount: 0,
      viewCount: 0,
      voteCount: 0
    };
    const ebook = ref<Ebook>(Tool.copy(emptyEbook));
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalLoading.value = true;
      ebook.value.category1Id = categoryIds.value[0];
      ebook.value.category2Id = categoryIds.value[1];
      axios.post("/ebook/save", ebook.value).then((response) => {
        const respData = response.data;
        modalLoading.value = false;
        if (respData.code == 0) {
          modalVisible.value = false;
        } else {
          message.error(respData.msg);
        }
        handleQueryEbooks({
          page: pagination.value.current,
          size: pagination.value.pageSize,
        });
      })
    };

    /**
     * 编辑
     */
    const edit = (record: any) => {
      modalVisible.value = true;
      ebook.value = Tool.copy(record);
      categoryIds.value = [ebook.value.category1Id, ebook.value.category2Id];
    };

    /**
     * 新增
     */
    const add = () => {
      modalVisible.value = true;
      ebook.value = Tool.copy(emptyEbook);
    }

    /**
     * 删除
     */
    const handleDeleteEbook = (ebookId: string) => {
      console.log(ebookId);
      axios.delete("/ebook/delete/" + ebookId).then((response) => {
        const respData = response.data;
        if (respData.code == 0) {
          handleQueryEbooks({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        }
      });
    }

    const level1 =  ref();
    let categories: Category[];
    /**
     * 查询所有分类
     **/
    const handleQueryCategory = () => {
      loading.value = true;
      axios.get("/category/all").then((response) => {
        loading.value = false;
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

    const getCategoryName = (cid: string) => {
      let result = "";
      for (let item of categories) {
        if (item.id === cid) {
          result = item.name;
        }
      }
      return result;
    };

    onMounted(() => {
      handleQueryCategory();
      handleQueryEbooks({
        pageNum: 1,
        pageSize: pagination.value.pageSize,
      });
    });

    return {
      ebookQueryForm,
      labelCol: { span: 4 },
      wrapperCol: { span: 14 },

      ebooks,
      pagination,
      columns,
      loading,
      categoryIds,
      level1,
      handleTableChange,

      edit,
      add,
      handleDeleteEbook,
      handleQueryFormSubmit,
      getCategoryName,

      ebook,
      modalVisible,
      modalLoading,
      handleModalOk
    }
  }
});
</script>

<style scoped>
img {
  width: 50px;
  height: 50px;
}
</style>
