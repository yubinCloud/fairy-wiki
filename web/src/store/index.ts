import { createStore } from 'vuex'

declare let SessionStorage: any;
const USER = 'USER';

const store = createStore({
  state: {
    localUser: SessionStorage.get(USER) || {}  // 表示当前登录的用户
  },
  mutations: {
    setLocalUser(state, user) {
      state.localUser = user;
      SessionStorage.set(USER, user);  // 将该用户的信息存放于 SessionStorage 中
    }
  },
  actions: {
  },
  modules: {
  }
})


export default store;