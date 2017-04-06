package nl.codecentric.impactmapping.dummyclient;

import nl.codecentric.impactmapping.userservice.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;

@SpringBootApplication
public class JavaDummyClientApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(JavaDummyClientApplication.class)
                .properties(Collections.singletonMap("server.port", "8081"))
                .run(args);
//
//        SpringApplication.run(JavaDummyClientApplication.class, args);

    }


    @Bean
    WebClient client (){
        return WebClient.create("http://localhost:8080");
    }

    @Bean
    public CommandLineRunner demo(WebClient client) {
        return
                args -> client
                        .get()
                        .uri("/userflux")
                        .accept(MediaType.TEXT_EVENT_STREAM)
                        .exchange()
                        .flatMap(it -> it.bodyToFlux(User.class))
                        .subscribe(System.out::println);
    }


}
