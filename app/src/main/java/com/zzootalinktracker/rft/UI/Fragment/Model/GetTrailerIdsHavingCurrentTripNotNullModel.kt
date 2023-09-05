package com.zzootalinktracker.rft.UI.Fragment.Model

class GetTrailerIdsHavingCurrentTripNotNullModel(
    var status: String,
    var message: String,
    var data: ArrayList<Data>
) {
    class Data(var deviceId: String, var trailerId: String, var driverId: String) {

    }
}