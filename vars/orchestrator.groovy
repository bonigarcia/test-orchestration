class orchestrator {

    def context

    def runJob(String jobId) {
        this.@context.stage(jobId) { buildJob(jobId) }
    }

    def runJobDependingOn(String result, String job1Id, String job2Id) {
        if (result == 'SUCCESS') {
            buildJob(job1Id)
        }
        else {
            buildJob(job2Id)
        }
    }

    def runJobsInParallel(String... jobs) {
        this.@context.stage("Parallel jobs: " + jobs.join(", ")) {
            def stepsForParallel = [:]
            for (job in jobs) {
                def index = job
                def stepName = "Running ${index}"
                stepsForParallel[stepName] = { -> buildJob("${index}") }
            }
            this.@context.parallel stepsForParallel
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
