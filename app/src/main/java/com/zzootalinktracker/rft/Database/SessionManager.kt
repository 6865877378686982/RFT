package com.zzootalinktracker.rft.Database

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.zzootalinktracker.rft.UI.Activity.Model.LoginModel
import com.zzootalinktracker.rft.Utils.*

class SessionManager {

    var context: Context

    val PREFERENCE_NAMGE = "h4h"

    var manager: SharedPreferences

    constructor(context: Context) {
        this.context = context
        manager = context.getSharedPreferences(PREFERENCE_NAMGE, Context.MODE_PRIVATE)

    }

 /*   public fun getBreakList(): BreakTimingsModel? {
        val data = getStringData(BreakList)
        if (data != "") {
            var gson = Gson()
            var loginModel = gson.fromJson(data, BreakTimingsModel::class.java)
            return loginModel
        }
        return null
    }*/

    public fun saveStringData(key_name: String, key_value: String) {
        val editor = manager.edit()
        editor.putString(key_name, key_value)
        editor.apply()
    }

    fun saveCustomerApproachAlertData(key_name: String, key_value: String) {
        val editor = manager.edit()
        editor.putString(key_name, key_value)
        editor.apply()

    }
/*
    public fun getCustomerApproachAlertData(key_name: String): GetCustomerApproachAlertsModel? {
        val data = getStringData(key_name)
        if (data != "") {
            val gson = Gson()
            val deviceModel = gson.fromJson(data, GetCustomerApproachAlertsModel::class.java)
            return deviceModel
        }
        return null
    }*/

    public fun saveIntData(key_name: String, key_value: Int) {
        val editor = manager.edit()
        editor.putInt(key_name, key_value)
        editor.apply()
    }

    public fun saveBooleanData(key_name: String, key_value: Boolean) {
        val editor = manager.edit()
        editor.putBoolean(key_name, key_value)
        editor.apply()
    }

    public fun getStringData(key_name: String): String {
        return manager.getString(key_name, "")!!
    }

    public fun getIntData(key_name: String): Int {
        return manager.getInt(key_name, 0)
    }

    public fun getBooleanData(key_name: String): Boolean {
        return manager.getBoolean(key_name, false)
    }

    public fun logOut() {
        val editor = manager.edit()
        editor.clear()
        editor.apply()
    }


    public fun getLoginData(): LoginModel? {
        val data = getStringData(USER_DATA)
        if (data != "") {
            var gson = Gson()
            var loginModel = gson.fromJson(data, LoginModel::class.java)
            return loginModel
        }
        return null
    }

    public fun getChatCount(): String {
        val data = getStringData(CHAT_COUNT)
        return data

    }

    public fun saveChatCount(count: String) {
        saveStringData(CHAT_COUNT, count)
    }

    public fun saveJobCount(count: String) {
        saveStringData(JOB_COUNT, count)
    }

    public fun getJobCount(): String {
        val data = getStringData(JOB_COUNT)
        return data

    }

    public fun saveSendSmsDocketInfo(value: String) {
        saveStringData(SMS_DOCKET_STATUS, value)
    }

    public fun getSendSmsDocketInfo(): String {
        val data = getStringData(SMS_DOCKET_STATUS)
        return data

    }


    public fun saveIMEI(count: String) {
        saveStringData(IMEI_ID, count)
    }

    public fun getIMEI(): String {
        val data = getStringData(IMEI_ID)
        return data
    }

/*    public fun saveNotificationWarnings(key_name: String, data: BreakFatugueNotification) {
        val gson = Gson()
        val jsonObject = gson.toJson(data)
        saveStringData(key_name, jsonObject)
    }*/
/*
    public fun getNotificatonData(key_name: String): BreakFatugueNotification? {
        val data = getStringData(key_name)
        if (data != "") {
            val gson = Gson()
            val deviceModel = gson.fromJson(data, BreakFatugueNotification::class.java)
            return deviceModel
        }
        return null
    }*/

}
