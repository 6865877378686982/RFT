package com.zzootalinktracker.rft.UI.Activity.Model


class TrailerModel(var status: String, var data: ArrayList<Data>) {
    class Data(
        var status: String,
        var message: String,
        var dateTimeInUtcTime: String,
        var dateTimeInUserTimeZone: String
    )
}

