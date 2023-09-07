package com.zzootalinktracker.rft.UI.Activity.Model

class AddSpace10XBTDataModel(var list: ArrayList<TrailersData>) {

    class TrailersData(var mac: String, var devices: ArrayList<Data>) {

    }

    class Data(
        var time_of_data: String, var mac_address: String, var isStoredOrMissing: String

    ) {

    }
}