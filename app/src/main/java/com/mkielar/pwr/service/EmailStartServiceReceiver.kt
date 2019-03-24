package com.mkielar.pwr.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class EmailStartServiceReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            SchedulerUtil.scheduleJob(context)
        }
    }
}