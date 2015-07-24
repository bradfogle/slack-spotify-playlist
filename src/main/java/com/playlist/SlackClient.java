package com.playlist;

import net.sf.json.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Helper class for posting to the pre-configured Slack Incoming Webhook Integration URL.
 */
public class SlackClient {

    private String slackWebHookUrl = System.getenv("SLACK_WEBHOOK_URL");

    public void postMessageToSlack(String message) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            HttpPost httpPost = new HttpPost(slackWebHookUrl);

            JSONObject json = new JSONObject();
            json.put("text", message);
            json.put("channel", "#test-integrations");//TODO: remove channel override

            StringEntity entity = new StringEntity(json.toString());

            httpPost.setEntity(entity);
            CloseableHttpResponse response = httpClient.execute(httpPost);

            try {
                if(response.getStatusLine().getStatusCode() != 200) {
                    throw new Exception("Error occurred posting message to slack: " + EntityUtils.toString(response.getEntity()));
                }
            } finally {
                response.close();
            }
        } finally {
            httpClient.close();
        }
    }
}
