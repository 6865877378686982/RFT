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


/*LIVE URL*/
/*var BASE_URL = "https://139.99.208.82/api/"
var SOCKET_URL = "http://139.99.208.82:9001"
var BASE_URL_RFT = "https://admin.zzoota.com/api/"
var CHECKLIST_QR_PATH = "https://track.zzoota.com/"*/

/*http://139.99.208.82/api/login?email=Ad@zzoota.com&password=AD@zzoota.com*/


/*Dev URL*/
/*var BASE_URL = "http://20.213.57.167/api/"
var SOCKET_URL = "http://20.213.57.167:9001"
var CHECKLIST_QR_PATH = "http://20.213.57.167/"
var BASE_URL_RFT = "https://admin.zzoota.com/api/"*/

/*Test URL*/
var BASE_URL = "http://20.211.90.127/api/"
var SOCKET_URL = "http://20.211.90.127:9001"
var CHECKLIST_QR_PATH = "http://20.211.90.127/"
var BASE_URL_RFT = "https://admin.zzoota.com/api/"

const val USER_DATA = "user_data"
const val E_MAIL = "email"
const val PASSWORD = "PASSWORD"
const val BreakList = "BreakList"
const val LOGIN_TIMESTAMP = "logintimestamp"
const val API_HASH = "api_hash"
const val USER_TYPE = "USER_TYPE"
const val NOTIFICATION_REMAINING_DISTANCE = "notification_remaining_distance"
const val NOTIFICATION_REMAINING_TIME = "notification_remaining_time"
const val DEVICE_NAME = "device_name"
const val DEVICE_ID = "device_id"
const val USER_ID = "user_id"
const val RFT_DRIVER_ID = "rft_driver_id"
const val POSITION_PERMISSION = "POSITION_PERMISSION"
const val CHAT_COUNT = "chat_count"
const val JOB_COUNT = "job_count"
const val SMS_DOCKET_STATUS = "SMS_DOCKET_STATUS"
const val IMEI_ID = "IMEI_ID"
const val QR_CODE_LOGIN = "QR_CODE_LOGIN"
const val CHECK_LIST_ID = "check_list_id"
const val CHECK_LIST_START_TIME = "CHECK_LIST_START_TIME"
const val MIN_BREAK_DURATION_MINUTE = "MIN_BREAK_DURATION_MINUTE"
const val MAX_BREAK_DURATION_MINUTES = "MAX_BREAK_DURATION_MINUTES"
const val DRIVE_DURATION_MINUTE = "DRIVE_DURATION_MINUTES"
const val LAST_BREAK_NOTIFICATION_TIME = "LAST_BREAK_NOTIFICATION_TIME"

const val VEHICLE_DATA = "VEHICLE_DATA"
const val ALERT_DATA = "ALERT_DATA"
const val VEHICLE_DATA_EXIST = "VEHICLE_DATA_EXIST"
const val TOTAL_BREAK_HOURS = "TOTAL_BREAK_HOURS"
const val START_BREAK_TIME = "START_BREAK_TIME"
const val END_BREAK_TIME = "END_BREAK_TIME"

const val SUCCESS_STATUS = 1
const val SUCCESS_STATUS_EDGE = "success"
var IS_BREAK_START = false
var IS_CHECKLIST_FILL = false
var IS_CHECKLIST_FILL_POSITION = -1

const val AUTO_LOGOUT = 0

const val CPU_CREDITOR_ID = 623

const val NOTIFICATION330 = "NOTIFICATION330"
const val NOTIFICATION480 = "NOTIFICATION480"
const val NOTIFICATION660 = "NOTIFICATION660"
const val NOTIFICATION1440 = "NOTIFICATION1440"


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

//fun showToast(msg: String, context: Context) {
//    try {
//        /* Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()*/
//        ToastCompat.makeText(context, msg, Toast.LENGTH_SHORT)
//            .setBadTokenListener { toast -> addFlurryDialogErrorEvents("showToast c", msg) }.show()
//    } catch (e: Exception) {
//        addFlurryDialogErrorEvents("showToast a", e.toString())
//    } catch (e: WindowManager.BadTokenException) {
//        addFlurryDialogErrorEvents("showToast b", e.toString())
//    }
//}

//fun addFlurryDialogErrorEvents(
//    dialog: String,
//    error: String
//) {
//    try {
//        val flurry_parms: MutableMap<String, String> = HashMap()
//        flurry_parms["SCREEN"] = dialog
//        flurry_parms["ERROR"] = error
//        FlurryAgent.logEvent("Dialog ERROR", flurry_parms)
//    } catch (e: Exception) {
//
//    }
//
//}

fun showSnackbar(view: View, msg: String, context: Context) {
    val snackbar = Snackbar.make(
        view, msg,
        Snackbar.LENGTH_SHORT
    )
    snackbar.setActionTextColor(Color.BLUE)
    val snackbarView = snackbar.view
    snackbarView.setBackgroundColor(ContextCompat.getColor(context, R.color.green))
    val textView =
        snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
    textView.setTextColor(Color.WHITE)
    textView.typeface = ResourcesCompat.getFont(context, R.font.gotham_rounded_bold)
    textView.textSize = 14f
    snackbar.show()
}

fun showSnackbarForAlerts(view: View, msg: String, context: Context) {
    val snackbar = Snackbar.make(
        view, msg,
        Snackbar.LENGTH_SHORT
    )
    snackbar.setActionTextColor(Color.WHITE)
    val snackbarView = snackbar.view
    snackbarView.setBackgroundColor(ContextCompat.getColor(context, R.color.red))
    val textView =
        snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
    textView.setTextColor(Color.WHITE)
    textView.typeface = ResourcesCompat.getFont(context, R.font.gotham_rounded_bold)
    textView.textSize = 14f
    snackbar.show()
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

fun GetUTCdatetimeAsString(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return sdf.format(Date())
}

//fun addFlurrySuccessEvents(screen: String, api: String, device_id: String) {
//    var flurry_parms: MutableMap<String, String> = HashMap()
//    flurry_parms["SCREEN"] = screen
//    flurry_parms["Api"] = api
//    flurry_parms["DEVICE_ID"] = device_id
//    FlurryAgent.logEvent(api + " " + "SUCCESSS", flurry_parms)
//}

//fun addFlurrySuccessEventsIMEI(screen: String, api: String, imei: String) {
//    val flurry_parms = HashMap<String, String>()
//    flurry_parms["SCREEN"] = screen
//    flurry_parms["Api"] = api
//    flurry_parms["imei"] = imei
//    FlurryAgent.logEvent(api + " " + "SUCCESS", flurry_parms)
//}

//fun addFlurryErrorEvents(
//    screen: String,
//    api: String,
//    device_id: String,
//    error: String,
//    error_type: String
//) {
//    val flurry_parms: MutableMap<String, String> = HashMap()
//    flurry_parms["SCREEN"] = screen
//    flurry_parms["Api"] = api
//    flurry_parms["DEVICE_ID"] = device_id
//    flurry_parms["ERROR"] = error
//    flurry_parms["ERROR_TYPE"] = error_type
//    FlurryAgent.logEvent(api + " " + "ERROR", flurry_parms)
//}

//fun addFlurryErrorEventsWithIMEI(
//    screen: String,
//    api: String,
//    imei: String,
//    error: String,
//    error_type: String
//) {
//    val flurry_parms: MutableMap<String, String> = HashMap()
//    flurry_parms["SCREEN"] = screen
//    flurry_parms["Api"] = api
//    flurry_parms["IMEI"] = imei
//    flurry_parms["ERROR"] = error
//    flurry_parms["ERROR_TYPE"] = error_type
//    FlurryAgent.logEvent(api + " " + "ERROR", flurry_parms)
//}
