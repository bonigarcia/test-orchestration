class orchestrator {

    def context
    boolean resultParallel
    ParallelResultStrategy parallelResultStrategy = ParallelResultStrategy.AND

    def runJob(String jobId) {
        this.@context.stage(jobId) { return buildJob(jobId) }
    }

    def runJobDependingOn(boolean result, String job1Id, String job2Id) {
        if (result) {
            return runJob(job1Id)
        }
        else {
            return runJob(job2Id)
        }
    }

    def runJobsInParallel(String... jobs) {
        initResultParallel()
        this.@context.stage(jobs.join(", ")) {
            def stepsForParallel = [:]
            for (job in jobs) {
                def index = job
                stepsForParallel["${index}"] = { -> buildParalleJob("${index}") }
            }
            this.@context.parallel stepsForParallel
            return this.resultParallel
        }
    }

    def initResultParallel() {
        if (this.parallelResultStrategy == ParallelResultStrategy.AND) {
            this.resultParallel = true
        }
        else if (this.parallelResultStrategy == ParallelResultStrategy.OR) {
            this.resultParallel = false
        }
    }

    def updateResultParallel(boolean result) {
        if (this.parallelResultStrategy == ParallelResultStrategy.AND) {
            this.resultParallel &= result
        }
        else if (this.parallelResultStrategy == ParallelResultStrategy.OR) {
            this.resultParallel |= result
        }
    }

    def buildParalleJob(String jobId) {
        updateResultParallel(buildJob(jobId))
    }

    def buildJob(String jobId) {
        def job = this.@context.build job: jobId, propagate: false
        return job.getResult() == 'SUCCESS'
    }

    def setContext(ctx){
        this.@context = ctx
    }

    def setParallelResultStrategy(strategy){
        this.parallelResultStrategy = strategy
    }
}
