class orchestrator implements Serializable {

    def runJob(String jobId) {
        def job = build job: jobId, parameters: [], propagate: false
        return job.getResult()
    }
}
