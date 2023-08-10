package com.zzootalinktracker.rft.UI.Activity.Model

class TrailerTagModel(
    var trailerId: String,
    var trailerName: String,
    var imei: String,
    var taglist: ArrayList<Tags>
) {
    class Tags(var id: String, var status: String?, var tagName: String, var tagImei: String)
}