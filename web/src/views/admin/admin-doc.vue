<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-row :gutter="24">
        <a-col :span="8">
          <p>
            <a-form layout="inline" :model="docQueryForm">
              <a-form-item>
                <a-button type="primary" @click="handleQueryFormSubmit">
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
              v-if="level1.length > 0"
              :columns="columns"
              :row-key="record => record.id"
              :data-source="level1"
              :loading="loading"
              :pagination="false"
              size="small"
              :defaultExpandAllRows="true"
          >
            <template #name="{ text, record }">
              {{ record.sort }} {{ text }}
            </template>
            <template v-slot:action="{ text, record }">
              <a-space size="small">
                <a-button type="primary" @click="edit(record)" size="small">
                  编辑
                </a-button>
                <a-popconfirm
                    title="删除后不可恢复，确认删除?"
                    ok-text="是"
                    cancel-text="否"
                    @confirm="handleDeleteDoc(record.id)"
                >
                  <a-button type="danger" size="small">
                    删除
                  </a-button>
                </a-popconfirm>
              </a-space>
            </template>
          </a-table>
        </a-col>
        <a-col :span="16">
          <p>
            <a-form layout="inline" :model="param">
              <a-form-item>
                <a-button type="primary" @click="handleSaveDoc()">
                  保存
                </a-button>
              </a-form-item>
            </a-form>
          </p>
          <a-form :model="doc" layout="vertical">
            <a-form-item>
              <a-input v-model:value="doc.name" placeholder="名称"/>
            </a-form-item>
            <a-form-item>
              <a-tree-select
                  v-model:value="doc.parent"
                  style="width: 100%"
                  :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                  :tree-data="treeSelectData"
                  placeholder="请选择父文档"
                  tree-default-expand-all
                  :replaceFields="{title: 'name', key: 'id', value: 'id'}"
              >
              </a-tree-select>
            </a-form-item>
            <a-form-item>
              <a-input v-model:value="doc.sort" placeholder="顺序"/>
            </a-form-item>
            <a-form-item>
              <div id="content"></div>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>

    </a-layout-content>
  </a-layout>

  <!--<a-modal-->
  <!--  title="文档表单"-->
  <!--  v-model:visible="modalVisible"-->
  <!--  :confirm-loading="modalLoading"-->
  <!--  @ok="handleModalOk"-->
  <!--&gt;-->
  <!--  -->
  <!--</a-modal>-->
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, UnwrapRef, reactive, createVNode } from 'vue';
import axios from 'axios';
import { message, Modal } from 'ant-design-vue';
import ExclamationCircleOutlined from "@ant-design/icons-vue/ExclamationCircleOutlined";
import { Tool } from "@/util/tool";
import { Doc, DocQueryForm } from "@/models";
import {useRoute} from "vue-router";
import E from 'wangeditor';



export default defineComponent({
  name: 'AdminDoc',
  setup() {
    const route = useRoute();

    const docQueryForm: UnwrapRef<DocQueryForm> = reactive({
      name: ''
    });

    const docs = ref<Doc[]>([]);
    const loading = ref(false);

    const columns = [
      {
        title: '名称',
        dataIndex: 'name',
        slots: { customRender: 'name' }
      },
      {
        title: 'Action',
        key: 'action',
        slots: { customRender: 'action' }
      }
    ];

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
    const handleQuery = () => {
      loading.value = true;
      level1.value = [];
      axios.get("/doc/all").then((response) => {
        loading.value = false;
        const respData = response.data;

        if (respData.code == 0) {
          docs.value = respData.data;
          console.log("原始数组：", docs.value);

          level1.value = [];
          level1.value = Tool.array2Tree(docs.value, 0);
          console.log("树形结构：", level1);

        } else {
          message.error(respData.msg);
        }
      });
    };

    /**
     * 根据表单提交的数据进行查询
     **/
    const handleQueryFormSubmit = () => {
      handleQuery();
    };


    // -------- 表单 ---------
    const treeSelectData = ref();  // 因为树选择组件的属性状态，会随当前编辑的节点而变化，所以单独声明一个响应式变量
    treeSelectData.value = [];
    const doc = ref();
    doc.value = {};
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    let textEditor: E;

    const handleSaveDoc = () => {
      modalLoading.value = true;
      doc.value.content = textEditor.txt.html();

      axios.post("/doc/save", doc.value).then((response) => {
        const respData = response.data;
        modalLoading.value = false;
        if (respData.code == 0) {
          modalVisible.value = false;
        } else {
          message.error(respData.msg);
        }
        handleQuery();
      })
    };

    /**
     * 将某节点及其子孙节点全部置为disabled
     */
    const setDisable = (treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      // 遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          // 如果当前节点就是目标节点
          console.log("disabled", node);
          // 将目标节点设置为disabled
          node.disabled = true;

          // 遍历所有子节点，将所有子节点全部都加上disabled
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              setDisable(children, children[j].id)
            }
          }
        } else {
          // 如果当前节点不是目标节点，则到其子节点再找找看。
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            setDisable(children, id);
          }
        }
      }
    };


    /**
     * 编辑
     */
    const edit = (record: any) => {
      modalVisible.value = true;
      doc.value = Tool.copy(record);

      // 不能选择当前节点及其所有子孙节点，作为父节点，会使树断开
      treeSelectData.value = Tool.copy(level1.value);
      setDisable(treeSelectData.value, record.id);

      // 为选择树添加一个"无"
      treeSelectData.value.unshift({id: 0, name: '无'});
    };

    /**
     * 新增
     */
    const add = () => {
      modalVisible.value = true;
      let ebookId: string;
      doc.value = {
        ebookId: route.query.ebookId
      };
      treeSelectData.value = Tool.copy(level1.value);

      // 为选择树添加一个"无"
      treeSelectData.value.unshift({id: 0, name: '无'});
    }

    let docIdListToDelete: Array<string> = [];
    let docNameListToDelete: Array<string> = [];
    /**
     * 查找整根树枝
     */
    const getDeleteIds = (treeSelectData: any, id: any) => {
      // 遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          // 如果当前节点就是目标节点
          console.log("delete", node);
          // 将目标ID放入结果集ids
          // node.disabled = true;
          docIdListToDelete.push(id);
          docNameListToDelete.push(node.name);
          // 遍历所有子节点
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              getDeleteIds(children, children[j].id)
            }
          }
        } else {
          // 如果当前节点不是目标节点，则到其子节点再找找看。
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            getDeleteIds(children, id);
          }
        }
      }
    };

    /**
     * 删除用户选中的一个文档以及其所有子文档
     * @param docId 用户选中所要删除的文档的 id
     */
    const handleDeleteDoc = (docId: string) => {
      // 清空数组，否则多次删除时，数组会一直增加
      docIdListToDelete = [];
      docNameListToDelete = [];
      getDeleteIds(level1.value, docId);
      Modal.confirm({
        title: '重要提醒',
        icon: createVNode(ExclamationCircleOutlined),
        content: '将删除：【' + docNameListToDelete.join("，") + "】删除后不可恢复，确认删除？",
        onOk() {
          console.log('deleteIds: ', docIdListToDelete);
          axios.delete("/doc/delete", {
            data: {
              ids: docIdListToDelete
            }
          }).then((response) => {
            const respData = response.data;
            if (respData.code == 0) {
              handleQuery();
            }
          })
        },
      });
    }


    onMounted(() => {
      handleQuery();
      textEditor = new E('#content');
      textEditor.config.zIndex = 0;
      textEditor.create();
    });

    return {

      docQueryForm,
      level1,
      labelCol: { span: 4 },
      wrapperCol: { span: 14 },

      columns,
      loading,

      edit,
      add,
      handleDeleteDoc,
      handleQueryFormSubmit,
      handleSaveDoc,


      doc,
      modalVisible,
      modalLoading,
      treeSelectData,
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
