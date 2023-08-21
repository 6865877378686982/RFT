package com.zzootalinktracker.rft.UI.Activity.Model

class RftLoginModel(var status: Int, var message: String, var data: Data) {
    class Data(
        var id: String,
        var user_id: String,
        var current_driver_id: String,
        var timezone_id: String,
        var traccar_device_id: String,
        var icon_id: String,
        var active: Int,
        var name: String,
        var imei: String,
        var rft_driver_id: String

    )
}