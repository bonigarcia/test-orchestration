class orchestrator implements Serializable {

    def context

    def runJob(String jobId) {
        def job = this.@context.build job: jobId, propagate: false
        return job.getResult()
    }

    def setContext(ctx){
        this.@context = ctx
    }
}
