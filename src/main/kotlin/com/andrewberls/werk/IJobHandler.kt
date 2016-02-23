package com.andrewberls.werk

import com.andrewberls.werk.Job

interface IJobHandler {
    fun handle(job: Job): Unit
}
