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


    public fun saveStringData(key_name: String, key_value: String) {
        val editor = manager.edit()
        editor.putString(key_name, key_value)
        editor.apply()
    }

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


    fun saveRftDriverId(count: String) {
        saveStringData(RFT_DRIVER_ID, count)
    }

    public fun getRftDriverId(): String {
        val data = getStringData(RFT_DRIVER_ID)
        return data
    }

    public fun saveDriverName(count: String) {
        saveStringData("driver_name", count)
    }

    public fun getDriverName(): String {
        val data = getStringData("driver_name")
        return data
    }

    public fun saveStoredToken(count: String) {
        saveStringData("stored_token", count)
    }

    public fun getStoredToken(): String {
        val data = getStringData("stored_token")
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
