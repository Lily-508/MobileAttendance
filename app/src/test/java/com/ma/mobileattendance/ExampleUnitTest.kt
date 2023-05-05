package com.ma.mobileattendance

import com.google.gson.Gson
import com.ma.mobileattendance.logic.model.Company
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val json="{\"cContent\"=\"null\",\"cId\"=111}"
        val company= Gson().fromJson(json, Company::class.java)
        print(company)
    }
}