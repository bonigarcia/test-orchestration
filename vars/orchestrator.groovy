class orchestrator {

    def context

    def runJob(String jobId) {
        this.@context.stage(jobId) { buildJob(jobId) }
    }

    def runJobsInParallel(String... jobs) {
        this.@context.stage("Parallel jobs : " + jobs.join(", ")) {
            for (job in jobs) {
                buildJob(job)
            }
        }
    }

    def buildJob(String jobId) {
        def job = this.@context.build job: jobId, propagate: false
        return job.getResult()
    }

    def setContext(ctx){
        this.@context = ctx
    }
}
