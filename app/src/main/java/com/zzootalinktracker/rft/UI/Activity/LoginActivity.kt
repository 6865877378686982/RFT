package com.zzootalinktracker.rft.UI.Activity

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.gson.Gson
import com.zzootalinktracker.rft.AlarmReceiver
import com.zzootalinktracker.rft.BuildConfig
import com.zzootalinktracker.rft.Database.ApiInterface
import com.zzootalinktracker.rft.Database.SessionManager
import com.zzootalinktracker.rft.Database.SessionManagerEmailSave
import com.zzootalinktracker.rft.MainActivity
import com.zzootalinktracker.rft.R
import com.zzootalinktracker.rft.UI.Activity.Model.LoginModel
import com.zzootalinktracker.rft.UI.Activity.Model.UpdateCheck_Response
import com.zzootalinktracker.rft.UI.Activity.Model.UpdateVehicleAttributeResponse
import com.zzootalinktracker.rft.Utils.*
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var sessionManager: SessionManager
    lateinit var sessionManagerEmailSave: SessionManagerEmailSave
    private lateinit var btn_login: Button
    private lateinit var email_et: EditText
    private lateinit var password_et: EditText
    private lateinit var coordinate_layout_login: ConstraintLayout
    private lateinit var progressbar_login: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        sessionManager = SessionManager(applicationContext)
        sessionManagerEmailSave = SessionManagerEmailSave(applicationContext)
        initalizeViews()
        setEditTextDone()
    }

    private fun setEditTextDone() {
        password_et.setOnEditorActionListener { _, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                checkInternetConnection()
            }
            false
        }
    }

    private fun checkInternetConnection() {
        if (isOnline(applicationContext)) {
            checkValidations()
        } else {
            /* showSnackbar(
                 coordinate_layout_login,
                 "Please Check Your internet connections",
                 applicationContext
             )*/
            val intent = Intent(applicationContext, ServerDownActivity::class.java)
            intent.putExtra("is_internet_layout", true)
            intent.putExtra("is_splash_activity", false)
            startActivity(intent)

        }
    }

    private fun checkValidations() {

        val email = email_et.text.toString().trim()
        val password = password_et.text.toString().trim()
        if (email == "") {
            showSnackbar(coordinate_layout_login, "Please enter Email", applicationContext)
            return
        } else if (password == "") {
            showSnackbar(coordinate_layout_login, "Please enter Password", applicationContext)
            return
        } else if (!isValidEmailId(email)) {
            showSnackbar(coordinate_layout_login, "Please Enter a vaild Email", applicationContext)
            return
        } else {


            loginApi(email, password)

            return
        }
    }

    private fun showHideProgressBar(show: Boolean) {

        if (show) {
            progressbar_login.visibility = View.VISIBLE
            btn_login.isEnabled = false
        } else {
            progressbar_login.visibility = View.GONE
            btn_login.isEnabled = true

        }
    }

    private fun loginApi(email: String, password: String) {
        showHideProgressBar(true)
        val version = Build.VERSION.SDK_INT
        var isVersionAbove28 = 0
        if (version < Build.VERSION_CODES.Q) isVersionAbove28 = 0 else isVersionAbove28 = 1
        try {

            ApiInterface.create().loginWithImei(
                email,
                password,
                sessionManager.getIMEI(),
                isVersionAbove28.toString()
            ).enqueue(object : Callback<LoginModel> {
                override fun onResponse(
                    call: retrofit2.Call<LoginModel>,
                    response: Response<LoginModel>
                ) {

                    if (response.isSuccessful) {
                        if (response.body()!!.status == SUCCESS_STATUS) {
                            if (response.body()!!.device != null) {
                                val gson = Gson()
                                val jsonObject = gson.toJson(response.body())
                                sessionManager.saveStringData(USER_DATA, jsonObject)
                                sessionManager.saveStringData(E_MAIL, email)
                                sessionManager.saveStringData(PASSWORD, password)
                                sessionManagerEmailSave.saveEmail(email)
                                sessionManager.saveStringData(
                                    LOGIN_TIMESTAMP,
                                    getCurrentDateTime24Hour()
                                )
                                sessionManager.saveStringData(
                                    API_HASH,
                                    response.body()!!.user_api_hash
                                )
                                try {
                                    sessionManager.saveStringData(
                                        USER_TYPE,
                                        response.body()!!.type
                                    )
                                } catch (e: Exception) {
e.printStackTrace()
                                }
                                try {
                                    sessionManager.saveStringData(
                                        NOTIFICATION_REMAINING_DISTANCE,
                                        response.body()!!.notification_remaining_distance.toString()
                                    )
                                    sessionManager.saveStringData(
                                        NOTIFICATION_REMAINING_TIME,
                                        response.body()!!.notification_remaining_time
                                    )
                                } catch (e: Exception) {
e.printStackTrace()
                                }
                                sessionManager.saveIntData(USER_ID, response.body()!!.user_id)
                                sessionManager.saveIntData(
                                    POSITION_PERMISSION,
                                    response.body()!!.can_change_positions
                                )

                                val item = response.body()!!.device
                                sessionManager.saveIntData(DEVICE_ID, item.id.toInt())
                                sessionManager.saveStringData(DEVICE_NAME, item.name)
                                autoLogoutStartAlarm(getCurrentDateTime24Hour())
                                insertAilloyHistroy()
                            } else {
                                try {
                                    val builder1 = AlertDialog.Builder(this@LoginActivity)
                                    builder1.setMessage("Device IMEI not Found")
                                    builder1.setCancelable(true)
                                    builder1.setPositiveButton(
                                        "ok"
                                    ) { dialog, _ -> dialog.cancel() }

                                    val alert11 = builder1.create()
                                    alert11.show()

                                } catch (e: Exception) {

                                }
                                showHideProgressBar(false)
                            }
                        } else {
                            showHideProgressBar(false)
                            showSnackbar(
                                coordinate_layout_login,
                                "Wrong Credentials",
                                applicationContext
                            )
                        }
                    } else {
                        try {
//                            addFlurryErrorEventsWithIMEI(
//                                this@LoginScreen.localClassName,
//                                "login",
//                                sessionManager.getIMEI(),
//                                response.message(),
//                                "api_unsuccess"
//                            )
                        } catch (e: Exception) {

                        }
                        showHideProgressBar(false)
                        showSnackbar(
                            coordinate_layout_login,
                            "Wrong Credentials",
                            applicationContext
                        )
                    }
                }

                override fun onFailure(call: retrofit2.Call<LoginModel>, t: Throwable) {
               Toast.makeText(applicationContext,t.message,Toast.LENGTH_LONG).show()
                }

            })
        } catch (e: Exception) {

        }

    }
    private fun updateCurrentTimezone() {
        try {

            val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault())
            val timeZone = SimpleDateFormat("Z").format(calendar.time)
            val localTime = timeZone.substring(0, 3) + ":" + timeZone.substring(3, 5)
            //  val timezone = localTime
            var encodeTimeZone = ""
            if (localTime.contains("+")) {
                //  encodeTimeZone = timezone.replace("+", "%2B")
                if (localTime[1].toString() == "0") {
                    encodeTimeZone = "UTC " + localTime.removeRange(1, 2)
                    encodeTimeZone = encodeTimeZone.replace("+", "+")
                    // encodeTimeZone = encodeTimeZone.replace("+", "%2B")
                } else {
                    encodeTimeZone = "UTC $localTime"
                    encodeTimeZone = encodeTimeZone.replace("+", "+")
                    //encodeTimeZone = encodeTimeZone.replace("+", "%2B")
                }
            } else {
                if (localTime[1].toString() == "0") {
                    encodeTimeZone = "UTC " + localTime.removeRange(1, 2)
                    encodeTimeZone = encodeTimeZone.replace("-", "-")
                } else {
                    encodeTimeZone = "UTC $localTime"
                    encodeTimeZone = encodeTimeZone.replace("-", "-")
                }

            }
            //  val encodeTimeZone= URLEncoder.encode(timezone, "utf-8")
            // showToast(timezone,applicationContext)
       ApiInterface.create().updateUserTimeZone(sessionManager.getStringData(API_HASH), encodeTimeZone, sessionManager.getIntData(
           USER_ID
       ).toString()).enqueue(object :Callback<UpdateVehicleAttributeResponse>{
           override fun onResponse(
               call: retrofit2.Call<UpdateVehicleAttributeResponse>,
               response: Response<UpdateVehicleAttributeResponse>
           ) {
               if (response.isSuccessful) {
                   if (response.body()!!.status == SUCCESS_STATUS) {
                       val intent =
                           Intent(applicationContext, MainActivity::class.java)
                       startActivity(intent)
                       finish()
                   }

               }
           }

           override fun onFailure(
               call: retrofit2.Call<UpdateVehicleAttributeResponse>,
               t: Throwable
           ) {

           }


       })



    }catch (e:Exception){
    }
    }

    private fun insertAilloyHistroy() {
        if (isOnline(applicationContext)) {
            var sygicVersionName = ""
            try {
                sygicVersionName = "21.1.2"
            } catch (e: java.lang.Exception) {
                ApiInterface.create().insertDeviceLoginHistroy(
                    sessionManager.getStringData(API_HASH),
                    Build.VERSION.SDK_INT.toString(),
                    getCurrentDateTime(),
                    BuildConfig.VERSION_NAME,
                    sygicVersionName,
                    sessionManager.getIntData(DEVICE_ID).toString()
                ).enqueue(object : Callback<UpdateCheck_Response> {
                    override fun onResponse(
                        call: retrofit2.Call<UpdateCheck_Response>,
                        response: Response<UpdateCheck_Response>
                    ) {
                        try {
                            if (response.body()!!.status == SUCCESS_STATUS) {
                                updateCurrentTimezone()
                                /*       val intent =
                                           Intent(applicationContext, MainActivity::class.java)
                                       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                       startActivity(intent)
                                       finish()*/
                            }
                        } catch (e: java.lang.Exception) {

                        }

                    }

                    override fun onFailure(
                        call: retrofit2.Call<UpdateCheck_Response>,
                        t: Throwable
                    ) {

                    }


                })
            }
        }
    }

    private fun getTimeDifference(startDate: Date, endDate: Date): Int {
        val different: Long = startDate.time - endDate.time
        val minute = different / (1000 * 60)
        return minute.toInt()
    }

    private fun autoLogoutStartAlarm(time: String) {
        /*Auto logout after 12 hour*/
        try {


            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val ispreviouseTime = getTimeDifference(
                formatter.parse(getCurrentDateOnly() + " 23:59:59")!!, formatter.parse(
                    time
                )!!
            )
            val intentForLogout = Intent(
                this,
                AlarmReceiver::class.java
            )
            intentForLogout.putExtra("data", "auto_logout")
            val pendingIntent = PendingIntent.getBroadcast(
                this, AUTO_LOGOUT, intentForLogout, PendingIntent.FLAG_ONE_SHOT
            )
            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            try {
                val calendar = addTime(0, ispreviouseTime, time).time
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    calendar.time,
                    pendingIntent
                )

            } catch (e: Exception) {
                e.printStackTrace()
            }

        } catch (e: Exception) {

        }
    }

    private fun addTime(hour: Int, min: Int, time: String): Calendar {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date: Date = formatter.parse(time)
        val calendar = Calendar.getInstance()
        calendar.time = date
        if (hour != 0) {
            calendar.add(Calendar.HOUR, hour)
        }
        calendar.add(Calendar.MINUTE, min)
        return calendar
    }

    private fun isValidEmailId(email: String): Boolean {
        return Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
    }

    private fun initalizeViews() {
        btn_login = findViewById(R.id.btn_login)
        email_et = findViewById(R.id.email_et)
        progressbar_login = findViewById(R.id.progressbar_login)
        password_et = findViewById(R.id.password_et)
        coordinate_layout_login = findViewById(R.id.coordinate_layout_login)

        btn_login.setOnClickListener(this)

        email_et.setText(sessionManagerEmailSave.getEmail())
//        email_et.setText(sessionManager.getIMEI())
//        email_et.setText(sessionManager.getIMEI())
        /* var androidId = Settings.Secure.getString(
             this.contentResolver,
             Settings.Secure.ANDROID_ID
         )
         email_et.setText(androidId)*/
    }

    override fun onClick(v: View?) {
        when (v) {
            btn_login -> {
                checkInternetConnection()
            }

        }
    }
}