class orchestrator implements Serializable {

    def runJob(String jobId) {
        node {
            def job = build job: jobId, propagate: false
            return job.getResult()
        }
    }
}
