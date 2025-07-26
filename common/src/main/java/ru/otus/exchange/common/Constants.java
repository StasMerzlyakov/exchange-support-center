package ru.otus.exchange.common;

import javax.xml.namespace.QName;
import java.util.LinkedList;

import static ru.otus.exchange.fxml.XPathSearcher.joinStringStack;

public class Constants {
    private Constants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String SERVICE_ID = "ServiceId";
    public static final String REQUEST_ID = "RequestId";

    public static final String LifecycleIdPath = joinStringStack(new LinkedList<>() {
        {
            add(new QName("http://exchange.support/envelope", "Envelope").toString());
            add(new QName("http://exchange.support/envelope", "Header").toString());
            add(new QName("http://exchange.support/header/addressing", "Addressing").toString());
            add(new QName("http://exchange.support/header/addressing", "LifecycleID").toString());
        }
    });

    public static final String MessageIdPath = joinStringStack(new LinkedList<>() {
        {
            add(new QName("http://exchange.support/envelope", "Envelope").toString());
            add(new QName("http://exchange.support/envelope", "Header").toString());
            add(new QName("http://exchange.support/header/addressing", "Addressing").toString());
            add(new QName("http://exchange.support/header/addressing", "MessageID").toString());
        }
    });

    public static final String FromPath = joinStringStack(new LinkedList<>() {
        {
            add(new QName("http://exchange.support/envelope", "Envelope").toString());
            add(new QName("http://exchange.support/envelope", "Header").toString());
            add(new QName("http://exchange.support/header/addressing", "Addressing").toString());
            add(new QName("http://exchange.support/header/addressing", "From").toString());
        }
    });

    public static final String ToPath = joinStringStack(new LinkedList<>() {
        {
            add(new QName("http://exchange.support/envelope", "Envelope").toString());
            add(new QName("http://exchange.support/envelope", "Header").toString());
            add(new QName("http://exchange.support/header/addressing", "Addressing").toString());
            add(new QName("http://exchange.support/header/addressing", "To").toString());
        }
    });

    public static final String ReplyToPath = joinStringStack(new LinkedList<>() {
        {
            add(new QName("http://exchange.support/envelope", "Envelope").toString());
            add(new QName("http://exchange.support/envelope", "Header").toString());
            add(new QName("http://exchange.support/header/addressing", "Addressing").toString());
            add(new QName("http://exchange.support/header/addressing", "ReplyTo").toString());
        }
    });

    public static final String BodyContentPath = joinStringStack(new LinkedList<>() {
        {
            add(new QName("http://exchange.support/envelope", "Envelope").toString());
            add(new QName("http://exchange.support/envelope", "Body").toString());
        }
    });
}
