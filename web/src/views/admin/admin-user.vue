<template>
  <a-layout>
    <a-layout-content
      :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <p>
        <a-form layout="inline" :model="param">
          <a-form-item>
            <a-input v-model:value="param.loginName" placeholder="登陆名">
            </a-input>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="handleQueryUser({pageNum: 1, pageSize: pagination.pageSize})">
              查询
            </a-button>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="add()">
              新增
            </a-button>
          </a-form-item>
        </a-form>
      </p>
      <a-table
        :columns="columns"
        :row-key="record => record.id"
        :data-source="users"
        :pagination="pagination"
        :loading="loading"
        @change="handleTableChange"
      >
        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <a-button type="primary" @click="resetPwd(record)">
              重置密码
            </a-button>
            <a-button type="primary" @click="edit(record)">
              编辑
            </a-button>
            <a-popconfirm
              title="删除后不可恢复，确认删除?"
              ok-text="是"
              cancel-text="否"
              @confirm="handleDeleteUser(record.id)"
            >
              <a-button type="danger">
                删除
              </a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>

  <a-modal
    title="用户表单"
    v-model:visible="modalVisible"
    :confirm-loading="modalLoading"
    @ok="handleModalOk"
  >
    <a-form :model="user" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="登陆名">
        <a-input v-model:value="user.loginName" :disabled="!!user.id"/>
      </a-form-item>
      <a-form-item label="昵称">
        <a-input v-model:value="user.name" />
      </a-form-item>
      <a-form-item label="密码" v-show="!user.id">
        <a-input v-model:value="user.password" />
      </a-form-item>
    </a-form>
  </a-modal>

  <a-modal
      title="重置密码"
      v-model:visible="resetModalVisible"
      :confirm-loading="resetModalLoading"
      @ok="handleResetModalOk"
  >
    <a-form :model="user" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="新密码">
        <a-input v-model:value="user.password"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
  import { defineComponent, onMounted, ref } from 'vue';
  import axios from 'axios';
  import { message } from 'ant-design-vue';
  import {Tool} from "@/util/tool";

  declare let hexMd5: any;
  declare let KEY: any;

  export default defineComponent({
    name: 'AdminUser',
    setup() {
      const param = ref();
      param.value = {};
      const users = ref();
      const pagination = ref({
        current: 1,
        pageSize: 10,
        total: 0
      });
      const loading = ref(false);

      const columns = [
        {
          title: '登陆名',
          dataIndex: 'loginName'
        },
        {
          title: '名称',
          dataIndex: 'name'
        },
        {
          title: '密码',
          dataIndex: 'password'
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
      const handleQueryUser = (queryParams: any) => {
        loading.value = true;
        // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
        users.value = [];
        axios.get("/user/list", {
          params: {
            pageNum: queryParams.pageNum,
            pageSize: queryParams.pageSize,
            loginName: param.value.loginName
          }
        }).then((response) => {
          loading.value = false;
          const respData = response.data;
          if (respData.code === 0) {
            users.value = respData.data.list;

            // 重置分页按钮
            pagination.value.current = queryParams.pageNum;
            pagination.value.total = respData.data.total;
          } else {
            message.error(respData.msg);
          }
        });
      };

      /**
       * 表格点击页码时触发
       */
      const handleTableChange = (pagination: any) => {
        console.log("看看自带的分页参数都有啥：" + pagination);
        handleQueryUser({
          pageNum: pagination.current,
          pageSize: pagination.pageSize
        });
      };

      // -------- 表单 ---------
      const user = ref();
      const modalVisible = ref(false);
      const modalLoading = ref(false);
      const handleModalOk = () => {
        user.value.password = hexMd5(user.value.password + KEY);  // 对密码进行 md5 哈希
        modalLoading.value = true;
        axios.post("/user/save", user.value).then((response) => {
          modalLoading.value = false;
          const respData = response.data; // data = commonResp
          if (respData.code === 0) {
            modalVisible.value = false;

            // 重新加载列表
            handleQueryUser({
              pageNum: pagination.value.current,
              pageSize: pagination.value.pageSize,
            });
          } else {
            message.error(respData.msg);
          }
        });
      };

      /**
       * 编辑
       */
      const edit = (record: any) => {
        modalVisible.value = true;
        user.value = Tool.copy(record);
      };

      /**
       * 新增
       */
      const add = () => {
        modalVisible.value = true;
        user.value = {};
      };

      const handleDeleteUser = (id: number) => {
        axios.delete("/user/delete/" + id).then((response) => {
          const respData = response.data; // data = commonResp
          if (respData.code === 0) {
            // 重新加载列表
            handleQueryUser({
              pageNum: pagination.value.current,
              pageSize: pagination.value.pageSize,
            });
          }
        });
      };

      // -------- 重置密码 ---------
      const resetModalVisible = ref(false);
      const resetModalLoading = ref(false);
      const handleResetModalOk = () => {
        resetModalLoading.value = true;

        user.value.password = hexMd5(user.value.password + KEY);  // 在前端进行哈希

        axios.post("/user/reset-pwd", user.value).then((response) => {
          resetModalLoading.value = false;
          const respData = response.data; // data = commonResp
          if (respData.code === 0) {
            resetModalVisible.value = false;

            // 重新加载列表
            handleQueryUser({
              pageNum: pagination.value.current,
              pageSize: pagination.value.pageSize,
            });
          } else {
            message.error(respData.msg);
          }
        });
      };

      /**
       * 重置密码
       */
      const resetPwd = (record: any) => {
        resetModalVisible.value = true;
        user.value = Tool.copy(record);
        user.value.password = null;
      };

      onMounted(() => {
        handleQueryUser({
          pageNum: 1,
          pageSize: pagination.value.pageSize,
        });
      });

      return {
        param,
        users,
        pagination,
        columns,
        loading,
        handleTableChange,
        handleQueryUser,

        edit,
        add,

        user,
        modalVisible,
        modalLoading,
        handleModalOk,

        handleDeleteUser,

        resetModalVisible,
        resetModalLoading,
        handleResetModalOk,
        resetPwd,
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
