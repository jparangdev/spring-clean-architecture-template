package kr.co.jparangdev.notification.slack;

import kr.co.jparangdev.application.common.port.SlackPort;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Adapter implementation for sending Slack notifications
 */
@Component
public class SlackNotificationAdapter implements SlackPort {

    private final RestTemplate restTemplate = new RestTemplate();
    // In a real environment, this should be managed via external configuration
    // (e.g., yml)
    private static final String SLACK_WEBHOOK_URL = "https://hooks.slack.com/services/test/url";

    @Override
    public void sendNotification(String message) {
        Map<String, String> payload = new HashMap<>();
        payload.put("text", message);

        try {
            // Actual Slack integration (performed only for logging in this demo)
            // restTemplate.postForEntity(SLACK_WEBHOOK_URL, payload, String.class);
            System.out.println("[SLACK NOTIFICATION] " + message);
        } catch (Exception e) {
            System.err.println("Failed to send slack notification: " + e.getMessage());
        }
    }
}
