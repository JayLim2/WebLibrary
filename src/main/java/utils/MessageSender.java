package utils;

import models.MessageType;

import javax.servlet.http.HttpServletRequest;

public class MessageSender {
    public static void sendMessage(HttpServletRequest request, MessageType type, String message) {
        switch (type) {
            case ERROR:
                request.setAttribute("error", message);
                break;
            case INFORMATION:
                request.setAttribute("info", message);
                break;
            case WARNING:
                request.setAttribute("warn", message);
                break;
        }
    }
}
