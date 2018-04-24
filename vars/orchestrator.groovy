class orchestrator implements Serializable {

    def runJob(String jobId) {
        def job = this.@ctx.build job: jobId, propagate: false
        return job.getResult()
    }
}
