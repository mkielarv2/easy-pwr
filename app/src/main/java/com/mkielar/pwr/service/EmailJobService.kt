package com.mkielar.pwr.service

import android.app.job.JobParameters
import android.app.job.JobService

class EmailJobService : JobService() {
    override fun onStartJob(params: JobParameters?): Boolean {
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }

}
