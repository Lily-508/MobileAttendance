package com.ma.mobileattendance.logic.model

class EnumNoun{
    companion object{
        const val NORMAL_PUNCH:String="正常考勤"
        const val NORMAL_PUNCH_RULE:Int=1
        const val OUTSIDE_PUNCH:String="外派考勤"
        const val VISIT_PUNCH:String="拜访考勤"
        const val PUNCH_ON_RESULT:String="考勤中"
        const val PUNCH_SUCCESS_RESULT:String="考勤成功"
        const val PUNCH_FAILURE_RESULT:String="考勤异常"
        const val AUDIT_FAILURE_RESULT:String="未通过"
        const val AUDIT_AGREE_RESULT:String="通过"
        const val AUDIT_ON_RESULT:String="审核中"
        const val MEN:String="男"
        const val WOMAN:String="女"
        const val STATUS_ON:String="在职"
        const val STATUS_OFF:String="离职"
        const val PERSONAL:String="事假"
        const val MARRIAGE:String="婚假"
        const val COMPENSATORY:String="调休"
        const val ANNUAL:String="年假"
        const val SICK:String="病假"
        const val PREGNANCY:String="产假"
        const val NORMAL_RIGHT:Int=0
        const val LEADER_RIGHT:Int=1
        const val ADMIN_RIGHT:Int=2
        const val USERNAME_CODE_LOGIN:Int=0
        const val PHONE_CODE_LOGIN:Int=1
    }
}