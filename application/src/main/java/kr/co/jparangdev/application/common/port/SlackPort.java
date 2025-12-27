package kr.co.jparangdev.application.common.port;

/**
 * Port interface for sending notifications
 */
public interface SlackPort {
    /**
     * Sends a notification.
     * 
     * @param message contents of the message to be sent
     */
    void sendNotification(String message);
}
