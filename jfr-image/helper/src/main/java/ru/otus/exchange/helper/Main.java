package ru.otus.exchange.helper;

public class Main {
    public static void main(String... args) throws Exception{
        var className = args[0];
        System.out.println(Class.forName(className).getModule().getName());
    }
}