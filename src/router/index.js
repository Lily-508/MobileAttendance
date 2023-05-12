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
    component: () => import("../components/LoginMenu.vue"),
    meta: { isAuth: false },
  },
  {
    path: "/user",
    name: "user",
    component: () => import("../components/UserMenu.vue"),
    meta: { isAuth: true },
    children: [
      {
        path: "status",
        component: () => import("../components/pages/UserStatus.vue"),
      },
      {
        path: "staff",
        component: () => import("../components/pages/StaffManage.vue"),
      },
      {
        path: "affair/work-outside",
        component: () => import("../components/pages/WorkOutsideManage.vue"),
      },
    ],
  },
]

const router = new VueRouter({
  mode: "history",
  routes,
})
router.beforeEach((to, from, next) => {
  if (to.meta.isAuth) {
    let token = window.sessionStorage.getItem("token")
    if (token) {
      Vue.prototype.$axiosJwt({
        url: "/staffs/check-token",
        method: "get",
        params: {},
        success(response) {
          if (response.data.code === 200) {
            next()
          }
        },
        error(err) {
          alert(err.msg)
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
