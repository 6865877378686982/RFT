package com.zzootalinktracker.rft.Database

import android.content.Context
import android.content.SharedPreferences

class SessionManagerEmailSave {
    var context: Context

    val PREFERENCE_NAMGE = "h4h_email"

    var manager: SharedPreferences

    constructor(context: Context) {
        this.context = context
        manager = context.getSharedPreferences(PREFERENCE_NAMGE, Context.MODE_PRIVATE)

    }

    public fun saveEmail(key_value: String) {
        val editor = manager.edit()
        editor.putString("email", key_value)
        editor.apply()
    }


    public fun getEmail(): String {
        return manager.getString("email", "")!!
    }

    public fun saveLogoutTime(key_value: String) {
        val editor = manager.edit()
        editor.putString("logout_time", key_value)
        editor.apply()
    }

    public fun saveIntroSliderStatus(key_value: String) {
        val editor = manager.edit()
        editor.putString("intoslider_status", key_value)
        editor.apply()
    }

    public fun getIntroSliderStatus(): String {
        return manager.getString("intoslider_status", "")!!
    }

    public fun saveCheckListId(key_value: String) {
        val editor = manager.edit()
        editor.putString("checklist_id", key_value)
        editor.apply()
    }

    public fun saveStartShiftTime(key_value: String) {
        val editor = manager.edit()
        editor.putString("startShift_time", key_value)
        editor.apply()
    }


    public fun getLastLogoutTime(): String {
        return manager.getString("logout_time", "")!!
    }

    public fun getCheckListId(): String {
        return manager.getString("checklist_id", "")!!
    }

    public fun getStartShiftTime(): String {
        return manager.getString("startShift_time", "")!!
    }


    public fun logOut() {
        val editor = manager.edit()
        editor.clear()
        editor.apply()
    }


}
