package com.zzootalinktracker.rft.UI.Fragment.Model

class GetDeviceDriverInfoModel(var status: String, var message: String, var data: Data) {

    class Data(
        var id: String,
        var driverId: String,
        var firstName: String,
        var lastName: String,
        var birthDate: String,
        var startDate: String,
        var tabletId: String,
        var tabletMobileno: String,
        var androidId: String,
        var imei: String,
        var token: String

    )
}