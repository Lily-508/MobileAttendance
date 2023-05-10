import Vue from "vue"
import VueRouter from "vue-router"
Vue.use(VueRouter)

const routes = [
  {
    path: "*",
    redirect: "/login",
  },
  {
    path: "/login",
    name: "login",
    component: () => import("../components/pages/LoginMenu.vue"),
    meta: { isAuth: false },
  },
]

const router = new VueRouter({
  mode: "history",
  routes,
})
router.beforeEach((to, from, next) => {
  // console.log('beforeEach', to, from);
  if (to.meta.isAuth) {
    let token = window.sessionStorage.getItem("token")
    if (token) {
      Vue.prototype.$axiosJwt({
        url: "/staff/check-token",
        method: "get",
        params: {},
        success(response) {
          if (!response.data.code === 200) {
            next()
          }
        },
        error(err) {
          alert(err.message)
          window.location.href = "/login"
        },
      })
    } else {
      alert("无权查看,请登录")
      window.location.href = "/login"
    }
  } else {
    next()
  }
})
export default router
