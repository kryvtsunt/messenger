package edu.northeastern.ccs.im;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

public class SlackMessage {
    public void slackMessage(String msg) {
        String url = "https://hooks.slack.com/services/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";
        String payload="{\"username\": \"webhookbot\", \"text\": \""+ msg +"\", \"icon_emoji\": \":ghost:\"}";

        
        StringEntity entity = new StringEntity(payload,
                ContentType.APPLICATION_FORM_URLENCODED);

        HttpPost request = new HttpPost(url);
        request.setEntity(entity);
    }
}