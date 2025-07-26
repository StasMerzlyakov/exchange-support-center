package ru.otus.exchange.gateway.filters.validation;

import io.micrometer.observation.annotation.Observed;
import org.springframework.stereotype.Component;
import ru.otus.exchange.fxml.NotXmlException;
import ru.otus.exchange.fxml.XPathSearcher;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static ru.otus.exchange.common.Constants.*;

@Component
public class MessageInfoExtractor {

    @Observed(name = "extractInfo")
    public MessageInfo extractInfo(byte[] message) throws NotXmlException, IOException {
        XPathSearcher searcher = new XPathSearcher(new ByteArrayInputStream(message));
        List<String> qnamePathList = new LinkedList<>() {
            {
                add(LifecycleIdPath);
                add(MessageIdPath);
                add(FromPath);
                add(ToPath);
                add(ReplyToPath);
                add(BodyContentPath);
            }
        };

        Map<String, String> result = searcher.getValuesByPath(qnamePathList);

        return MessageInfo.builder()
                .lifecycleID(result.get(LifecycleIdPath))
                .messageID(result.get(MessageIdPath))
                .from(result.get(FromPath))
                .to(result.get(ToPath))
                .replyTo(result.get(ReplyToPath))
                .bodyQName(result.get(BodyContentPath))
                .build();
    }
}
