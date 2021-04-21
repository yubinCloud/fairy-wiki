<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <p>
        <a-form
            layout="inline"
            :model="categoryQueryForm"
            @finish="handleQueryFormSubmit(categoryQueryForm)"
        >
          <a-form-item>
            <a-input v-model:value="categoryQueryForm.name" placeholder="category name">
              <template #prefix><EyeOutlined style="color: rgba(0, 0, 0, 0.25)" /></template>
            </a-input>
          </a-form-item>
          <a-form-item>
            <a-button
                type="primary"
                html-type="submit"
                size="large"
                :disabled="categoryQueryForm.name === ''"
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
          :data-source="categorys"
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
                @confirm="handleDeleteCategory(record.id)"
            >
              <a-button type="danger">删除</a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>

  <a-modal
      title="分类表单"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <a-form :model="category" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="名称">
        <a-input v-model:value="category.name" />
      </a-form-item>
      <a-form-item label="父分类">
        <a-input v-model:value="category.parent" />
      </a-form-item>
      <a-form-item label="顺序">
        <a-input v-model:value="category.sort" type="textarea" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, UnwrapRef, reactive } from 'vue';
import axios from 'axios';
import { message } from 'ant-design-vue'
import { Tool } from "@/util/tool";

interface CategoryQueryForm {
  name: string;
}

export default defineComponent({
  name: 'AdminCategory',
  setup() {
    const categoryQueryForm: UnwrapRef<CategoryQueryForm> = reactive({
      name: ''
    });

    const categorys = ref();
    const pagination = ref({
      current: 1,
      pageSize: 10,
      total: 0
    });
    const loading = ref(false);

    const columns = [
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '父分类',
        key: 'parent',
        dataIndex: 'parent'
      },
      {
        title: '顺序',
        dataIndex: 'sort'
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
    const handleQuery = (queryParams: any) => {
      loading.value = true;
      axios.get("/category/query", {
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
          categorys.value = pageData.list;

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
    const handleQueryFormSubmit = (categoryForm: CategoryQueryForm) => {
      handleQuery({
        pageNum: 1,
        pageSize: 4,
        name: categoryForm.name,
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
    const category = ref({});
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalLoading.value = true;
      axios.post("/category/save", category.value).then((response) => {
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
      category.value = Tool.copy(record);
    };

    /**
     * 新增
     */
    const add = () => {
      modalVisible.value = true;
      category.value = {};
    }

    /**
     * 删除
     */
    const handleDeleteCategory = (categoryId: string) => {
      console.log(categoryId);
      axios.delete("/category/delete/" + categoryId).then((response) => {
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
      categoryQueryForm,
      labelCol: { span: 4 },
      wrapperCol: { span: 14 },

      categorys,
      pagination,
      columns,
      loading,
      handleTableChange,

      edit,
      add,
      handleDeleteCategory,
      handleQueryFormSubmit,

      category,
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
