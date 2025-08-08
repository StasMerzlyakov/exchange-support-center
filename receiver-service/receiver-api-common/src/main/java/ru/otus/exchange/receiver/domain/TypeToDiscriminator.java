package ru.otus.exchange.receiver.domain;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import ru.otus.exchange.common.Discriminator;

import javax.xml.namespace.QName;

@Data
@Builder
public class TypeToDiscriminator {
    @NotEmpty(message = "Body content is empty")
    private QName bodyQName;
    @NotEmpty
    private Discriminator discriminator;
}
