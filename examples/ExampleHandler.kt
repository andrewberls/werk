package examples

import com.andrewberls.werk.IJobHandler
import com.andrewberls.werk.Job

class ExampleHandler : IJobHandler {
    override fun handle(job: Job): Unit {
        println("ExampleHandler working on ${job.jobId}")
    }
}
