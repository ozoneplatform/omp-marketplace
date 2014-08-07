package marketplace.scheduledimport

protected class ScheduledImportHttpService {

    private static final DateFormat QUERY_PARAM_DATE_FORMAT =
        new SimpleDateFormat(Constants.EXTERNAL_DATE_FORMAT)

    /**
     * Performs an http GET request using the parameters in the task.
     * @param task the ImportTask to get the request configuration from
     * @return The executed HttpMethod, from which response information
     * can be retrieved
     */
    private HttpMethod performRequest(ImportTask task) {
        URL keystoreUrl = new URL("file:${task.keystorePath}")
        URL truststoreUrl = new URL("file:${task.truststorePath}")
        URL remoteUrl = getRequestURL(task)

        ProtocolSocketFactory socketFactory = new AuthSSLProtocolSocketFactory(
            keystoreUrl, task.keystorePass,
            truststoreUrl, null)

        int port = remoteUrl.port == -1 ? remoteUrl.defaultPort : remoteUrl.port
        Protocol protocol = new Protocol(remoteUrl.getProtocol(), socketFactory, port)
        HttpClient client = new HttpClient()

        client.getHostConfiguration().setHost(null, 0, protocol)

        GetMethod httpget = new GetMethod("${remoteUrl.path}?${remoteUrl.query}");

        client.executeMethod(httpget);
    }

    /**
     * @return the URL to use to fetch import data for this ImportTask
     */
    private URL getRemoteUrl(ImportTask task) {
        URIBuilder uriBuilder = new URIBuilder(task.url)
        Date lastRunDate = task.lastRunResult.runDate

        if (task.updateType == Constants.IMPORT_TYPE_DELTA &&
            lastRunDate != null) {

            uriBuilder.addQueryParam(Constants.OMP_IMPORT_DELTA_DATE_FIELD,
                QUERY_PARAM_DATE_FORMAT.format(lastRunDate)
        }

        return uriBuilder.toURL()
    }

    public JSONObject retrieveRemoteImportData(ImportTask task) {
        HttpMethod method = performRequest(task)

        if (method.getStatusCode() != 200) {
            //TODO error handling
        }
        else {
            return new JSONObject(method.responseAsString())
        }
    }
}
