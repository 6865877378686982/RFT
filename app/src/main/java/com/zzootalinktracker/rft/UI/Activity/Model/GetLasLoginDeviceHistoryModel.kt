package com.zzootalinktracker.rft.UI.Activity.Model

import android.os.Parcel
import android.os.Parcelable

class GetLasLoginDeviceHistoryModel(var status: Int, var message: String, var data: Data) {

    class Data(
        var id: String?,
        var device_id: String?,
        var device_sdk_version: String?,
        var last_login_time: String?,
        var version_name: String?,
        var created_at: String?,
        var updated_at: String?,
        var sygic_app_version: String?,
        var user_id: String?,
    ) {


    }
}