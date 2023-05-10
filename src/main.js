import Vue from "vue"
import App from "./App.vue"
import router from "./router"
import Element from "element-ui"
import "element-ui/lib/theme-chalk/index.css"
import { Message } from "element-ui"
import astrict from "./utils/astrict"
Vue.config.productionTip = false
Vue.use(Element)
Vue.use(astrict)
// 封装axios方法
import axios from "axios"
axios.defaults.baseURL = "https://localhost:8433"
const defaultHeaders = {
  "Content-Type": "application/json;charset=UTF-8",
  token: "",
}
Vue.prototype.$axiosNoToken = (options) => {
  axios({
    url: options.url,
    method: options.method || "post",
    data: options.data || {},
    timeout: options.timeout || 1000,
    params: options.params || {},
    headers: options.headers || defaultHeaders,
    responseType: options.responseType || "json",
  })
    .then((response) => {
      if (options.success) options.success(response)
    })
    .catch((err) => {
      if (err.response) {
        //状态码超过2xx
        if (options.error) {
          options.error(err.response.data)
        } else {
          Message.error({ message: err.response.data.msg, offset: 150 })
        }
      } else if (err.request) {
        // 没有响应
        if (options.error) {
          options.error(err.request)
        } else {
          Message.error({ message: "请求错误", offset: 150 })
        }
      } else {
        // 发送出现问题
        if (options.error) {
          options.error(err)
        } else {
          Message.error({ message: "发送错误", offset: 150 })
        }
      }
    })
}
Vue.prototype.$axiosJwt = (options) => {
  if (window.sessionStorage.getItem("token")) {
    if (options.headers != null) {
      options.headers.token = window.sessionStorage.getItem("token")
    } else {
      defaultHeaders.token = window.sessionStorage.getItem("token")
    }
    Vue.prototype.$axiosNoToken(options)
  } else {
    if (options.error) options.error({ message: "没有token" })
    Message.error({ message: "请先登录", offset: 150 })
  }
}
new Vue({
  router,
  render: (h) => h(App),
}).$mount("#app")
