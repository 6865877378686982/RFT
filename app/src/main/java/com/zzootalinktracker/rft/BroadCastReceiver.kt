package com.zzootalinktracker.rft

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.zzootalinktracker.android.Ui.Activity.SplashActivity

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(k1: Context?, k2: Intent?) {

        if ("android.intent.action.BOOT_COMPLETED" == k2!!.action) {
            val intent = Intent(k1, SplashActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            k1!!.startActivity(intent)
        } else {
            val bundle: Bundle = k2.extras!!
            val text = bundle.getString("data")
            if (text == "auto_logout") {
                broadcastAlert(k1!!, text)
            } else {
                // broadcastAlertForBreak(k1!!, text!!)
            }
        }

    }

    private fun broadcastAlert(context: Context, event: String) {
        val manager = LocalBroadcastManager.getInstance(context)
        val intent = Intent()
        intent.action = "autoLogout"
        intent.putExtra("event", event)
        manager.sendBroadcast(intent)
    }

    private fun broadcastAlertForBreak(context: Context, event: String) {
        val manager = LocalBroadcastManager.getInstance(context)
        val intent = Intent()
        intent.action = "Break_alert"
        intent.putExtra("event", event)
        manager.sendBroadcast(intent)
    }
}