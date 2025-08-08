package ru.otus.exchange.receiver.content;

import ru.otus.exchange.blobutils.BlobXmlPathHolder;

import java.util.Set;

public class ConstantBlobXmlPathHolderImpl implements BlobXmlPathHolder {
    @Override
    public Set<String> getBlobPath(String messageType) {

        String envNamespace = "http://exchange.support/envelope";
        String excNamespace = "http://exchange.support/messages/exchange";
        return Set.of(String.format(
                "{%1$s}Envelope/{%1$s}Body/{%2$s}ExchangeMessage/{%2$s}Photo", envNamespace, excNamespace));
    }
}
