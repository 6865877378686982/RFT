package com.zzootalinktracker.rft.Utils

import android.app.Application
import android.util.Log
import com.flurry.android.FlurryAgent
import com.flurry.android.FlurryPerformance

class FlurryApplication : Application() {


    override fun onCreate() {
        super.onCreate()

        FlurryAgent.Builder()
            .withCaptureUncaughtExceptions(true)
            .withIncludeBackgroundSessionsInMetrics(true)
            .withLogLevel(Log.ERROR)
            .withPerformanceMetrics(FlurryPerformance.ALL)
            .build(this, "KRV7NB73YV2YKQ8WD6ZX")
    }
}