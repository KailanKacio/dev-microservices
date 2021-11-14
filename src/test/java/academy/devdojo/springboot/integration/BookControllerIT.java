package academy.devdojo.springboot.integration;

import academy.devdojo.springboot.domain.Book;
import academy.devdojo.springboot.repository.BookRepository;
import academy.devdojo.springboot.util.BookCreator;
import academy.devdojo.springboot.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class BookControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("list returns list of book inside page object when successful")
    void list_ReturnsListOfBookInsidePageObject_WhenSuccessful() {
        bookRepository.save(BookCreator.createBookToBeSaved());
        PageableResponse<Book> bookPage = testRestTemplate.exchange("/books", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Book>>() {
                }).getBody();

        Assertions.assertThat(bookPage).isNotNull();

        Assertions.assertThat(bookPage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(bookPage.toList().get(0).getName());
    }
}
