package academy.devdojo.springboot.client;

import academy.devdojo.springboot.domain.Book;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {
        ResponseEntity<Book> entity = new RestTemplate().getForEntity("http://localhost:8080/books/2", Book.class);
        log.info(entity);
    }
}
