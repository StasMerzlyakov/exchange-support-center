package ru.otus.exchange.gateway.filters.validation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageInfo {
    private String lifecycleID;
    private String messageID;
    private String from;
    private String to;
    private String replyTo;
    private String bodyQName;
}
