package com.zzootalinktracker.rft.UI.Fragment.Model

class GetTrailerTagsStatusModel(
    var status: String,
    var message: String,
    var data: ArrayList<Data>
) {
    class Data(
        var trailerId: String,
        var trailerName: String,
        var tag1: Boolean,
        var tag1Name: String,
        var tag2: Boolean,
        var tag2Name: String,
        var imei: String,
        var tag1Imei: String,
        var tag1IsMissingOrStored: String,
        var tag2Imei: String,
        var tag2IsMissingOrStored: String,

        ) {

    }
}