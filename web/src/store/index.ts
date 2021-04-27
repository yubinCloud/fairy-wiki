import { createStore } from 'vuex'

const store = createStore({
  state: {
    localUser: {
      name: ''
    }  // 表示当前登录的用户及其信息
  },
  mutations: {
    setLocalUser(state, user) {
      state.localUser = user;
    }
  },
  actions: {
  },
  modules: {
  }
})


export default store;