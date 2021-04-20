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
        <template v-slot:action="{ text, record }">
          <a-space size="small">
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
      <a-form-item label="分类一">
        <a-input v-model:value="ebook.category1Id" />
      </a-form-item>
      <a-form-item label="分类二">
        <a-input v-model:value="ebook.category2Id" />
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

interface EbookQueryForm {
  name: string;
}

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
        title: '分类一',
        key: 'category1Id',
        dataIndex: 'category1Id'
      },
      {
        title: '分类二',
        dataIndex: 'category2Id'
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
     * 根据表单提交的数据进行查询
     **/
    const handleQueryFormSubmit = (ebookForm: EbookQueryForm) => {
      loading.value = true;
      axios.get("/ebook/query", {
        params: {
          name: ebookForm.name,
          pageNum: 1,
          pageSize: 4
        }
      }).then((response) => {
        loading.value = false;
        const respData = response.data;

        if (respData.code == 0) {
          const pageData = respData.data;
          ebooks.value = pageData.list;

          // 重置分页按钮
          pagination.value.current = 1;
          pagination.value.total = pageData.total;
        } else {
          message.error(respData.msg);
        }
      });
    };


    /**
     * 数据查询
     **/
    const handleQuery = (queryParams: any) => {
      loading.value = true;
      axios.get("/ebook/list", {
        params: {
          pageNum: queryParams.pageNum,
          pageSize: queryParams.pageSize
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
     * 表格点击页码时触发
     */
    const handleTableChange = (pagination: any) => {
      console.log("看看自带的分页参数都有啥：", pagination);
      handleQuery({
        pageNum: pagination.current,
        pageSize: pagination.pageSize
      });
    };

    // -------- 表单 ---------
    const ebook = ref({});
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalLoading.value = true;
      axios.post("/ebook/save", ebook.value).then((response) => {
        const respData = response.data;
        modalLoading.value = false;
        if (respData.code == 0) {
          modalVisible.value = false;
        } else {
          message.error(respData.msg);
        }
        handleQuery({
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
      ebook.value = record
    };

    /**
     * 新增
     */
    const add = () => {
      modalVisible.value = true;
      ebook.value = {};
    }

    /**
     * 删除
     */
    const handleDeleteEbook = (ebookId: string) => {
      console.log(ebookId);
      axios.delete("/ebook/delete/" + ebookId).then((response) => {
        const respData = response.data;
        if (respData.code == 0) {
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        }
      });
    }


    onMounted(() => {
      handleQuery({
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
      handleTableChange,

      edit,
      add,
      handleDeleteEbook,
      handleQueryFormSubmit,

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
