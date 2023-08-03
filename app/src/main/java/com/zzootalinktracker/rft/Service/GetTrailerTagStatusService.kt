package com.zzootalinktracker.rft.Service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import com.zzootalinktracker.rft.Database.ApiInterface
import com.zzootalinktracker.rft.Database.SessionManager
import com.zzootalinktracker.rft.UI.Fragment.Model.GetTrailerTagsStatusModel

import com.zzootalinktracker.rft.Utils.SUCCESS_STATUS_EDGE
import com.zzootalinktracker.rft.Utils.isOnline
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetTrailerTagStatusService : Service() {

    lateinit var sessionManager: SessionManager
    var handler: Handler? = null
    var runnable: Runnable? = null
    val REQUEST_TIME = 15000 as Long
    override fun onBind(intent: Intent?): IBinder? {

        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        sessionManager = SessionManager(this)
        handler = Handler()
        runnable = object : Runnable {
            override fun run() {
                try {
                    if (isOnline(applicationContext)) {
                        try {
                            ApiInterface.createForRFT()
                                .getTrailerTagsStatus(
                                    //      sessionManager.getApiHash(),
                                    "$2y$10$" + "UxX6IwSI56UNrQGDNDOL/e2MM6fUVUU9LTx.8lnIQEDGFdRt.ZfUu",
                                    "00" + sessionManager.getRftDriverId()
                                ).enqueue(object : Callback<GetTrailerTagsStatusModel> {
                                    override fun onResponse(
                                        call: Call<GetTrailerTagsStatusModel>,
                                        response: Response<GetTrailerTagsStatusModel>
                                    ) {
                                        if (response.isSuccessful) {
                                            val responseBody = response.body()
                                            if (responseBody != null && responseBody.status == SUCCESS_STATUS_EDGE) {

                                                var trailerList = responseBody.data
                                                for (data in trailerList) {

                                                }
                                                val jsonString = Gson().toJson(responseBody)
                                                val intent = Intent("TRAILER_STATUS_UPDATE")
                                                intent.putExtra(
                                                    "trailerList",
                                                    jsonString
                                                )
                                                LocalBroadcastManager.getInstance(applicationContext)
                                                    .sendBroadcast(intent)
                                                Log.e("Service123","Service")
                                                handler!!.postDelayed(runnable!!, REQUEST_TIME)

                                            } else {

                                            }
                                        } else {
                                            runnable?.let { handler!!.postDelayed(it, REQUEST_TIME) }
                                        }
                                    }

                                    override fun onFailure(
                                        call: Call<GetTrailerTagsStatusModel>,
                                        t: Throwable
                                    ) {
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
        }

        handler!!.postDelayed(runnable!!, REQUEST_TIME)

        return START_NOT_STICKY
    }


    private fun getTrailerTagsStatus() {

    }
}