package com.zzootalinktracker.rft.Service

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.flurry.android.FlurryAgent
import com.google.gson.Gson
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.TransportEnum
import com.zzootalinktracker.rft.Database.ApiInterface
import com.zzootalinktracker.rft.Database.SessionManager
import com.zzootalinktracker.rft.UI.Fragment.Model.GetTrailerTagsStatusModel
import com.zzootalinktracker.rft.Utils.SUCCESS_STATUS_EDGE
import com.zzootalinktracker.rft.Utils.addFlurryErrorEvents
import com.zzootalinktracker.rft.Utils.isOnline
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GetTrailerTagStatusService : Service() {

    lateinit var sessionManager: SessionManager
    var handler: Handler? = null
    var runnable: Runnable? = null
    val REQUEST_TIME = 30000.toLong()
    val version = Build.VERSION.SDK_INT

    val serverUrlSignalR = "wss://admin.zzoota.com/GetTagsNotifications"
    private lateinit var hubConnection: HubConnection
    override fun onBind(intent: Intent?): IBinder? {

        return null
    }
    private fun startSignalRLatest() {
        try {

            hubConnection = HubConnectionBuilder.create(serverUrlSignalR)
                .withTransport(TransportEnum.WEBSOCKETS).build()

            hubConnection.on("device_" + sessionManager.getRftDriverId(), { message ->
                try {
                    if(!message.equals("Reload Page")){
                        getTrailesTagsStatus(100)
                    }else{
                        getTrailesTagsStatus(100)
                        /*try {
                            if (isOnline(applicationContext)) {
                                try {
                                    ApiInterface.createForRFT()
                                        .getTrailerTagsStatus(
                                            sessionManager.getRftDriverId()
                                        ).enqueue(object : Callback<GetTrailerTagsStatusModel> {
                                            override fun onResponse(
                                                call: Call<GetTrailerTagsStatusModel>,
                                                response: Response<GetTrailerTagsStatusModel>
                                            ) {
                                                if (response.isSuccessful) {
                                                    val responseBody = response.body()
                                                    if (responseBody != null && responseBody.status == SUCCESS_STATUS_EDGE) {
                                                        val jsonString = Gson().toJson(responseBody)
                                                        val intent = Intent("TRAILER_STATUS_UPDATE")
                                                        intent.putExtra(
                                                            "trailerList",
                                                            jsonString
                                                        )
                                                        LocalBroadcastManager.getInstance(applicationContext)
                                                            .sendBroadcast(intent)
                                                        Log.e("Service123", "Service")

                                                    } else {
                                                        val intent = Intent("unsucesssfull")
                                                        LocalBroadcastManager.getInstance(applicationContext)
                                                            .sendBroadcast(intent)
                                                    }
                                                } else {
                                                    val intent = Intent("unsucesssfull")
                                                    LocalBroadcastManager.getInstance(applicationContext)
                                                        .sendBroadcast(intent)
                                                    addFlurryErrorEvents(
                                                        "trailerTagService",
                                                        "trailerTagServiceApi",
                                                        sessionManager.getIMEI(),
                                                        version.toString(),
                                                        response.message(),
                                                        "apiUnsuccess"
                                                    )
                                                }
                                            }

                                            override fun onFailure(
                                                call: Call<GetTrailerTagsStatusModel>,
                                                t: Throwable
                                            ) {
                                                val intent = Intent("failure")
                                                LocalBroadcastManager.getInstance(applicationContext)
                                                    .sendBroadcast(intent)
                                                addFlurryErrorEvents(
                                                    "trailerTagService",
                                                    "trailerTagServiceApi",
                                                    sessionManager.getIMEI(),
                                                    version.toString(),
                                                    t.message.toString(),
                                                    "apiFailure"
                                                )
                                            }


                                        })

                                } catch (e: Exception) {
                                }
                            } else {
                            }

                        } catch (e: Exception) {

                        }*/
                    }
                }catch (e:Exception){
                    getTrailesTagsStatus(100)
                }

            }, String::class.java)

            hubConnection.start().blockingAwait()

        } catch (e: NullPointerException) {
            val flurry_parms: MutableMap<String, String> = HashMap()
            flurry_parms["Api"] = "signalR"
            flurry_parms["message"] = e.message.toString()
            flurry_parms["IMEI"] = sessionManager.getIMEI()
            FlurryAgent.logEvent("signalR 1", flurry_parms)
        } catch (e: Exception) {
            val flurry_parms: MutableMap<String, String> = HashMap()
            flurry_parms["Api"] = "signalR"
            flurry_parms["message"] = e.message.toString()
            flurry_parms["IMEI"] = sessionManager.getIMEI()
            FlurryAgent.logEvent("signalR 2", flurry_parms)
        }

    }

    private fun getTrailesTagsStatus(time:Long){
        try {
            /* val manager = LocalBroadcastManager.getInstance(applicationContext)
             val intent1 = Intent()
             intent1.action = "trip_refresh"
             manager.sendBroadcast(intent1)*/
            val handler = Handler(Looper.getMainLooper())

            handler.postDelayed({
                try {
                    if (isOnline(applicationContext)) {
                        try {
                            ApiInterface.createForRFT()
                                .getTrailerTagsStatus(
                                    sessionManager.getRftDriverId()
                                ).enqueue(object : Callback<GetTrailerTagsStatusModel> {
                                    override fun onResponse(
                                        call: Call<GetTrailerTagsStatusModel>,
                                        response: Response<GetTrailerTagsStatusModel>
                                    ) {
                                        if (response.isSuccessful) {
                                            val responseBody = response.body()
                                            if (responseBody != null && responseBody.status == SUCCESS_STATUS_EDGE) {
                                                val jsonString = Gson().toJson(responseBody)
                                                val intent = Intent("TRAILER_STATUS_UPDATE")
                                                intent.putExtra(
                                                    "trailerList",
                                                    jsonString
                                                )
                                                LocalBroadcastManager.getInstance(applicationContext)
                                                    .sendBroadcast(intent)
                                                Log.e("Service123", "Service")

                                            } else {
                                                val intent = Intent("unsucesssfull")
                                                LocalBroadcastManager.getInstance(applicationContext)
                                                    .sendBroadcast(intent)
                                            }
                                        } else {
                                            val intent = Intent("unsucesssfull")
                                            LocalBroadcastManager.getInstance(applicationContext)
                                                .sendBroadcast(intent)
                                            addFlurryErrorEvents(
                                                "trailerTagService",
                                                "trailerTagServiceApi",
                                                sessionManager.getIMEI(),
                                                version.toString(),
                                                response.message(),
                                                "apiUnsuccess"
                                            )
                                        }
                                    }

                                    override fun onFailure(
                                        call: Call<GetTrailerTagsStatusModel>,
                                        t: Throwable
                                    ) {
                                        val intent = Intent("failure")
                                        LocalBroadcastManager.getInstance(applicationContext)
                                            .sendBroadcast(intent)
                                        addFlurryErrorEvents(
                                            "trailerTagService",
                                            "trailerTagServiceApi",
                                            sessionManager.getIMEI(),
                                            version.toString(),
                                            t.message.toString(),
                                            "apiFailure"
                                        )
                                    }


                                })

                        } catch (e: Exception) {
                        }
                    } else {
                    }

                } catch (e: Exception) {

                }
            }, time)




        } catch (e: Exception) {
            val flurry_parms: MutableMap<String, String> = HashMap()
            flurry_parms["Api"] = "signalR"
            flurry_parms["message"] = e.message.toString()
            flurry_parms["IMEI"] = sessionManager.getIMEI()
            FlurryAgent.logEvent("signalR 3", flurry_parms)
        }
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        sessionManager = SessionManager(this)
        startSignalRLatest()
        handler = Handler()
        runnable = Runnable {
            try {
                if (isOnline(applicationContext)) {
                    try {
                        ApiInterface.createForRFT()
                            .getTrailerTagsStatus(
                                sessionManager.getRftDriverId()
                            ).enqueue(object : Callback<GetTrailerTagsStatusModel> {
                                override fun onResponse(
                                    call: Call<GetTrailerTagsStatusModel>,
                                    response: Response<GetTrailerTagsStatusModel>
                                ) {
                                    if (response.isSuccessful) {
                                        val responseBody = response.body()
                                        if (responseBody != null && responseBody.status == SUCCESS_STATUS_EDGE) {
                                            val jsonString = Gson().toJson(responseBody)
                                            val intent = Intent("TRAILER_STATUS_UPDATE")
                                            intent.putExtra(
                                                "trailerList",
                                                jsonString
                                            )
                                            LocalBroadcastManager.getInstance(applicationContext)
                                                .sendBroadcast(intent)
                                            Log.e("Service123", "Service")
                                            handler!!.postDelayed(runnable!!, REQUEST_TIME)

                                        } else {
                                            val intent = Intent("unsucesssfull")
                                            LocalBroadcastManager.getInstance(applicationContext)
                                                .sendBroadcast(intent)
                                        }
                                    } else {
                                        val intent = Intent("unsucesssfull")
                                        LocalBroadcastManager.getInstance(applicationContext)
                                            .sendBroadcast(intent)
                                        addFlurryErrorEvents(
                                            "trailerTagService",
                                            "trailerTagServiceApi",
                                            sessionManager.getIMEI(),
                                            version.toString(),
                                            response.message(),
                                            "apiUnsuccess"
                                        )
                                        runnable?.let {
                                            handler!!.postDelayed(
                                                it,
                                                REQUEST_TIME
                                            )
                                        }
                                    }
                                }

                                override fun onFailure(
                                    call: Call<GetTrailerTagsStatusModel>,
                                    t: Throwable
                                ) {
                                    val intent = Intent("failure")
                                    LocalBroadcastManager.getInstance(applicationContext)
                                        .sendBroadcast(intent)
                                    addFlurryErrorEvents(
                                        "trailerTagService",
                                        "trailerTagServiceApi",
                                        sessionManager.getIMEI(),
                                        version.toString(),
                                        t.message.toString(),
                                        "apiFailure"
                                    )
                                    handler!!.postDelayed(runnable!!, REQUEST_TIME)
                                }


                            })

                    } catch (e: Exception) {
                    }
                } else {
                    handler!!.postDelayed(runnable!!, REQUEST_TIME)
                }

            } catch (e: Exception) {

            }
        }

        handler!!.postDelayed(runnable!!, 10)

        return START_NOT_STICKY
    }

}