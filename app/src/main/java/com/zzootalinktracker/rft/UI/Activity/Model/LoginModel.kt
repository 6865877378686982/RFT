package com.zzootalinktracker.rft.UI.Activity.Model

class LoginModel(
    var status: Int,
    var user_api_hash: String,
    var permissions: PermissonsModel,
    var can_change_positions: Int,
    var user_id: Int,
    var message: String,
    var notification_remaining_time: String,
    var type: String,
    var notification_remaining_distance: String,
    var device: Device
) {

    class PermissonsModel(
        var devices: PermissionsData,
        var alerts: PermissionsData,
        var geofences: PermissionsData,
        var routes: PermissionsData,
        var tasks: PermissionsData,
        var maintenance: PermissionsData
    )

    class PermissionsData(var view: Boolean, var edit: Boolean, var remove: Boolean)
    class Device(var id: String, var name: String, var imei: String)
}