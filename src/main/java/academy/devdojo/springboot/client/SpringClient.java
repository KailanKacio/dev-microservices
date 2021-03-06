package academy.devdojo.springboot.client;

import academy.devdojo.springboot.domain.Book;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {
        ResponseEntity<Book> entity = new RestTemplate().getForEntity("http://localhost:8080/books/2", Book.class);
        log.info(entity);

        Book object = new RestTemplate().getForObject("http://localhost:8080/books/2", Book.class);
        log.info(object);

        Book[] books = new RestTemplate().getForObject("http://localhost:8080/books/all", Book[].class);
        log.info(Arrays.toString(books));

        ResponseEntity<List<Book>> exchange = new RestTemplate().exchange("http://localhost:8080/books/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        log.info(exchange.getBody());

        Book kingdom = Book.builder().name("Kingdom").build();
        ResponseEntity<Book> kingdomSaved = new RestTemplate().exchange("http://localhost:8080/books/", HttpMethod.POST,
                new HttpEntity<>(kingdom, createJsonHeader()),
                Book.class);
        log.info("Saved Book {}", kingdomSaved);
    }

    private static HttpHeaders createJsonHeader(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}
