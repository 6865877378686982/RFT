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
    fun saveTimestamp(timestamp: String) {
        val editor = manager.edit()
        editor.putString(TIMESTAMP_KEY, timestamp)
        editor.apply()
    }

    fun getTimestamp(): String {
        return manager.getString(TIMESTAMP_KEY, 0L.toString()).toString()
    }


    public fun logOut() {
        val editor = manager.edit()
        editor.clear()
        editor.apply()
    }







    public fun saveIMEI(count: String) {
        saveStringData(IMEI_ID, count)
    }

    public fun getIMEI(): String {
        val data = getStringData(IMEI_ID)
        return data
    }

    fun saveAndroidID(count: String){
        saveStringData(ANDROID_ID, count)

    }

    public fun getAndroidId(): String {
        val data = getStringData(ANDROID_ID)
        return data
    }

    public fun saveMacAddress(count: String?) {
        if (count != null) {
            saveStringData(MAC_ADDRESS, count)
        }
    }

    public fun getMacAddress(): String {
        val data = getStringData(MAC_ADDRESS)
        return data
    }


    public fun saveDeviceId(count: String) {
        saveStringData(DEVICE_ID, count)
    }

    public fun getDeviceId(): String {
        val data = getStringData(DEVICE_ID)
        return data
    }

    fun saveRftDriverId(count: String) {
        saveStringData(RFT_DRIVER_ID, count)
    }

    public fun getRftDriverId(): String {
        val data = getStringData(RFT_DRIVER_ID)
        return data
    }

    public fun saveLoginTimeStamp(count: String) {
        saveStringData(LOGIN_TIMESTAMP, count)
    }

    public fun getLoginTimeStamp(): String {
        val data = getStringData(LOGIN_TIMESTAMP)
        return data
    }


}
