package my.client.main;

import my.spring.config.ApplicationContextXml;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(ApplicationContextXml.class);
    }
}
