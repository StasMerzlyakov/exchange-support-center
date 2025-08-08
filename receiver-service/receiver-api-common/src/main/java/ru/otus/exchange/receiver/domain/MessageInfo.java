package ru.otus.exchange.receiver.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import javax.xml.namespace.QName;
import java.util.UUID;

@Data
@Builder
public class MessageInfo {

    @NotNull(message = "Field MessageID is null")
    @Size(max = 25, min = 25, message = "Field MessageID have wrong size")
    @Pattern(regexp = "\\d+", message = "Field MessageID does not match regexp pattern")
    private String messageID;

    @NotNull(message = "Field ProcessGUID is null")
    UUID processGUID;

    @NotNull(message = "Field From is null")
    @Size(max = 9, min = 9, message = "Field From have wrong size")
    @Pattern(regexp = "\\d+", message = "Field From does not match regexp pattern")
    private String creator;

    @NotEmpty(message = "Body content is empty")
    private QName bodyQName;
}
