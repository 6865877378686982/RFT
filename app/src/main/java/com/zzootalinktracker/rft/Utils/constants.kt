package com.zzootalinktracker.rft.Utils

import android.content.Context
import android.graphics.Color
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.database.getStringOrNull
import com.google.android.material.snackbar.Snackbar
import com.zzootalinktracker.rft.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/*Test URL*/
var BASE_URL = "http://20.211.90.127/api/"
var BASE_URL_RFT = "https://admin.zzoota.com/api/"

const val LOGIN_TIMESTAMP = "logintimestamp"
const val API_HASH = "api_hash"
const val DEVICE_ID = "device_id"
const val USER_ID = "user_id"
const val RFT_DRIVER_ID = "rft_driver_id"
const val IMEI_ID = "IMEI_ID"

/*For Server States*/
const val NO_INTERNET = 1
const val NO_SERVER = 2
const val DEACTIVE_DEVICE = 3



const val SUCCESS_STATUS = 1
const val SUCCESS_STATUS_EDGE = "success"



fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


    val capabilities =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            return true
        }
    }
    return false
}
fun getDataFormatForEdge(): SimpleDateFormat {
    return SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
}



fun getCurrentDateForEdge(): String {
    val currentDate: String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    return "$currentDate"
}

fun getCalculatedDate(days: Int): String {
    val cal = Calendar.getInstance()
    val s = getDataFormatForEdge()
    cal.add(Calendar.DAY_OF_YEAR, days)
    return s.format(Date(cal.timeInMillis))
}
public fun checkLocationEnabled(context: Context): Boolean {
    val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

fun getCurrentDateOnly(): String {
    return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
}




fun convertTime(time: String): String {
    val serverFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val newFormat = SimpleDateFormat("hh:mm:ss aa")
    val dt: Date
    var convertedTime = ""
    try {
        dt = serverFormat.parse(time)
        println("Time Display: " + newFormat.format(dt)) // <-- I got result here
        convertedTime = newFormat.format(dt)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return convertedTime
}

fun getSygicId(uri: Uri, context: Context): String? {
    context.contentResolver.query(uri, arrayOf("id"), null, null, null)?.use {
        return if (it.moveToFirst()) it.getStringOrNull(0) else null
    }
    return null
}

fun convertTimein24Hour(time: String): String {
    val serverFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val newFormat = SimpleDateFormat("HH:mm:ss")
    val dt: Date
    var convertedTime = ""
    try {
        dt = serverFormat.parse(time)
        println("Time Display: " + newFormat.format(dt)) // <-- I got result here
        convertedTime = newFormat.format(dt)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return convertedTime
}

fun getTime(time: String): String {
    val serverFormat = SimpleDateFormat("mm:s")
    val newFormat = SimpleDateFormat("mm:ss")
    val dt: Date
    var convertedTime = ""
    try {
        dt = serverFormat.parse(time)
        println("Time Display: " + newFormat.format(dt)) // <-- I got result here
        convertedTime = newFormat.format(dt)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return convertedTime
}

fun getCurrentTime(): String {
    val currentTime: String =
        SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
    return currentTime
}

fun getDateFormationOnly():SimpleDateFormat{
  return   SimpleDateFormat("yyyy-MM-dd")
}
fun getCurrentDateTime(): String {
    val currentDate: String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val currentTime: String =
        SimpleDateFormat("hh:mm:ss", Locale.getDefault()).format(Date())

    var datetime = "$currentDate $currentTime"
    return datetime
}

fun getCurrentDateTimeInJson(): String {
    val currentDate: String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val currentTime: String =
        SimpleDateFormat("hh:mm:ss", Locale.getDefault()).format(Date())

    val datetime = currentDate + "T" + currentTime
    return datetime
}


fun getCurrentDateTime24Hour(): String {
    val currentDate: String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val currentTime: String =
        SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

    val datetime = "$currentDate $currentTime"
    return datetime
}


public fun convertMeterToKilometer(meter: Float): Float {
    return (meter * 0.001).toFloat()
}

fun GetUtcDateTimeAsString(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return sdf.format(Date())
}


