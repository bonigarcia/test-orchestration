class orchestrator {

    def context
    def resultParallel

    def runJob(String jobId) {
        this.@context.stage(jobId) { return buildJob(jobId) }
    }

    def runJobDependingOn(String result, String job1Id, String job2Id) {
        if (result == 'SUCCESS') {
            return runJob(job1Id)
        }
        else {
            return runJob(job2Id)
        }
    }

    def runJobsInParallel(String... jobs) {
        this.resultParallel = true;
        this.@context.stage(jobs.join(", ")) {
            def stepsForParallel = [:]
            for (job in jobs) {
                def index = job
                def stepName = "Running ${index}"
                stepsForParallel[stepName] = { -> buildParalleJob("${index}") }
            }
            this.@context.parallel stepsForParallel
            return resultParallel
        }
    }

    def buildParalleJob(String jobId) {
        resultParallel &= buildJob(jobId)
    }

    def buildJob(String jobId) {
        def job = this.@context.build job: jobId, propagate: false
        return job.getResult()
    }

    def setContext(ctx){
        this.@context = ctx
    }
}
