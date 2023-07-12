package com.zzootalinktracker.rft.UI.Activity

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.textfield.TextInputEditText
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var etUsername: TextInputEditText
    lateinit var etPassword: TextInputEditText
    lateinit var tvIMEI: TextView
    lateinit var ivCopy: ImageView
    lateinit var btnLogin: CardView
    lateinit var coordinate_layout_login: RelativeLayout
    lateinit var sessionManager: SessionManager
    lateinit var sessionManagerEmailSave: SessionManagerEmailSave
    lateinit var progressAnimationView: LottieAnimationView
    lateinit var copyIMEILayout: RelativeLayout

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        sessionManager = SessionManager(applicationContext)
        sessionManagerEmailSave = SessionManagerEmailSave(applicationContext)
        etUsername = findViewById(R.id.etUsername)
        ivCopy = findViewById(R.id.ivCopy)
        tvIMEI = findViewById(R.id.tvIMEI)
        etPassword = findViewById(R.id.etPassword)
        progressAnimationView = findViewById(R.id.progressAnimationView)
        copyIMEILayout = findViewById(R.id.CopyIMEILayout)
        btnLogin = findViewById(R.id.btnLogin)
        coordinate_layout_login = findViewById(R.id.coordinate_layout_login)
        tvIMEI.setText("Android ID:" + sessionManager.getIMEI())
        btnLogin.setOnClickListener(this)
        ivCopy.setOnClickListener(this)
        copyIMEILayout.setOnClickListener(this)

        etUsername.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {

                etPassword.requestFocus()
                true
            } else {
                false
            }
        }
        etPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(etPassword.windowToken, 0)
                checkInternetConnection()
                true
            } else {
                false
            }
        }

    }

    override fun onClick(v: View?) {
        when (v) {
            btnLogin -> {


                checkInternetConnection()
            }
            copyIMEILayout -> {
                val clipboardManager =
                    getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val textToCopy = tvIMEI.text.toString().replace("Android ID:", "")
                val clipData = ClipData.newPlainText("text", textToCopy)
                clipboardManager.setPrimaryClip(clipData)
                Toast.makeText(this, "Data copied to clipboard", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun checkInternetConnection() {
        if (isOnline(applicationContext)) {
            checkValidations()
        } else {

            val intent = Intent(applicationContext, ServerDownActivity::class.java)
            intent.putExtra("is_internet_layout", true)
            intent.putExtra("is_splash_activity", false)
            startActivity(intent)

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun checkValidations() {

        val email = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()
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

  /*          val handler = Handler()
            val runnable = Runnable {

            }
            handler.postDelayed(runnable, 3000)*/
            loginApi(email, password)
            return
        }
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

    private fun loginApi(email: String, password: String) {
        /*     showHideProgressBar(true)*/
        progressAnimationView.visibility = View.VISIBLE
        if (progressAnimationView.isAnimating) {
            progressAnimationView.cancelAnimation()
        } else {
            progressAnimationView.playAnimation()
        }
        val version = Build.VERSION.SDK_INT
        var isVersionAbove28 = 0
        if (version < Build.VERSION_CODES.Q) isVersionAbove28 = 0 else isVersionAbove28 = 1
        try {
            ApiInterface.create()
                .loginWithImei(
                    email,
                    password,
                    sessionManager.getIMEI(),
                    isVersionAbove28.toString()
                )
                .enqueue(object : Callback<LoginModel> {
                    override fun onResponse(
                        call: Call<LoginModel>,
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
                                    //        showHideProgressBar(false)
                                    progressAnimationView.visibility = View.GONE
                                }
                            } else {
                                //    showHideProgressBar(false)


                                showSnackbar(
                                    coordinate_layout_login,
                                    "Wrong Credentials",
                                    applicationContext
                                )
                                progressAnimationView.visibility = View.GONE
                            }
                        } else {
                            /*   try {
                                   addFlurryErrorEventsWithIMEI(
                                       this@LoginScreen.localClassName,
                                       "login",
                                       sessionManager.getIMEI(),
                                       response.message(),
                                       "api_unsuccess"
                                   )
                               } catch (e: Exception) {

                               }*/
                            //      showHideProgressBar(false)
                            showSnackbar(
                                coordinate_layout_login,
                                "Wrong Credentials",
                                applicationContext

                            )
                            progressAnimationView.visibility = View.GONE
                        }
                    }

                    override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                        /*    try {

                                addFlurryErrorEventsWithIMEI(
                                    this@LoginScreen.localClassName,
                                    "login",
                                    sessionManager.getIMEI(),
                                    t.message.toString(),
                                    "api_failure"
                                )
                            } catch (e: Exception) {

                            }*/
                        /*   showHideProgressBar(false)*/

                        progressAnimationView.visibility = View.GONE
                        val intent = Intent(applicationContext, ServerDownActivity::class.java)
                        intent.putExtra("is_internet_layout", false)
                        intent.putExtra("is_splash_activity", false)
                        startActivity(intent)
                    }

                })
        } catch (e: Exception) {
            /*showToast(e.message.toString(), applicationContext)*/
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

    private fun insertAilloyHistroy() {

        if (isOnline(applicationContext)) {
            var sygicVersionName = ""
            try {
                sygicVersionName = "21.1.2"
            } catch (e: java.lang.Exception) {

            }
            ApiInterface.create().insertDeviceLoginHistroy(
                sessionManager.getStringData(API_HASH),
                Build.VERSION.SDK_INT.toString(),
                getCurrentDateTime(),
                BuildConfig.VERSION_NAME,
                sygicVersionName,
                sessionManager.getIntData(
                    DEVICE_ID
                ).toString(), sessionManager.getIntData(USER_ID).toString()
            ).enqueue(object : Callback<UpdateCheck_Response> {
                override fun onResponse(
                    call: Call<UpdateCheck_Response>,
                    response: Response<UpdateCheck_Response>
                ) {
                    try {
                        if (response.body()!!.status == SUCCESS_STATUS) {
                            updateCurrentTimezone()
                            val intent =
                                Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    } catch (e: java.lang.Exception) {
                    }
                }

                override fun onFailure(call: Call<UpdateCheck_Response>, t: Throwable) {

                }

            })
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
            ApiInterface.create().updateUserTimeZone(
                sessionManager.getStringData(API_HASH), encodeTimeZone, sessionManager.getIntData(
                    USER_ID
                ).toString()
            ).enqueue(object : Callback<UpdateVehicleAttributeResponse> {
                override fun onResponse(
                    call: Call<UpdateVehicleAttributeResponse>,
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

                override fun onFailure(call: Call<UpdateVehicleAttributeResponse>, t: Throwable) {

                }

            })
        } catch (e: java.lang.Exception) {

        }
    }
}