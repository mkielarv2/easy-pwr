package com.mkielar.pwr.service

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context


class SchedulerUtil {
    companion object {
        fun scheduleJob(context: Context) {
            val serviceComponent = ComponentName(context, EmailJobService::class.java)

            val builder = JobInfo.Builder(0, serviceComponent)
            builder.setPeriodic(60000)
            builder.setMinimumLatency((1 * 1000).toLong())
            builder.setOverrideDeadline((3 * 1000).toLong())
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)

            val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            jobScheduler.schedule(builder.build())
        }
    }
}