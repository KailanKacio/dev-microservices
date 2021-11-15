package academy.devdojo.springboot.integration;

import academy.devdojo.springboot.domain.Book;
import academy.devdojo.springboot.repository.BookRepository;
import academy.devdojo.springboot.request.BookPostRequestBody;
import academy.devdojo.springboot.util.BookCreator;
import academy.devdojo.springboot.util.BookPostRequestBodyCreator;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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

    @Test
    @DisplayName("listAll returns list of book when successful")
    void listAll_ReturnsListOfBook_WhenSuccessful() {
        bookRepository.save(BookCreator.createBookToBeSaved());
        List<Book> books = testRestTemplate.exchange("/books/all", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Book>>() {
                }).getBody();

        Assertions.assertThat(books)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(books.get(0).getName());
    }

    @Test
    @DisplayName("findById returns book when successful")
    void findById_ReturnsBook_WhenSuccessful() {
        Book savedBook = bookRepository.save(BookCreator.createBookToBeSaved());
        Long expectedId = savedBook.getId();
        Book book = testRestTemplate.getForObject("/books/{id}", Book.class, expectedId);

        Assertions.assertThat(book)
                .isNotNull();

        Assertions.assertThat(book.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns list of book when successful")
    void findByName_ReturnsListOfBook_WhenSuccessful() {
        Book savedBook = bookRepository.save(BookCreator.createBookToBeSaved());
        String expectedName = savedBook.getName();
        String url = String.format("/books/find?name=%s", expectedName);

        List<Book> books = testRestTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Book>>() {
                }).getBody();

        Assertions.assertThat(books)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(books.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns empty list of book when book is not found")
    void findByName_ReturnsEmptyListOfBook_WhenBookIsNotFound() {
        List<Book> books = testRestTemplate.exchange("/books/find?name=dbz", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Book>>() {
                }).getBody();

        Assertions.assertThat(books)
                .isNotNull();
    }

    @Test
    @DisplayName("save returns book when successful")
    void save_ReturnsBook_WhenSuccessful() {
        BookPostRequestBody bookPostRequestBody = BookPostRequestBodyCreator.createBookPostRequestBody();

        ResponseEntity<Book> bookResponseEntity = testRestTemplate.postForEntity("/books",
                bookPostRequestBody, Book.class);

        Assertions.assertThat(bookResponseEntity)
                .isNotNull();

        Assertions.assertThat(bookResponseEntity
                        .getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Assertions.assertThat(bookResponseEntity
                .getBody()
                .getId())
                .isNotNull();
    }

    @Test
    @DisplayName("replace updates book when successful")
    void replace_UpdatesBook_WhenSuccessful() {
        Book savedBook = bookRepository.save(BookCreator.createBookToBeSaved());

        savedBook.setName("book");

        ResponseEntity<Void> bookResponseEntity = testRestTemplate.exchange("/books", HttpMethod.PUT,
                new HttpEntity<>(savedBook), Void.class);

        Assertions.assertThat(bookResponseEntity).isNotNull();
        Assertions.assertThat(bookResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete removes book when successful")
    void delete_RemovesBook_WhenSuccessful() {
        Book savedBook = bookRepository.save(BookCreator.createBookToBeSaved());

        ResponseEntity<Void> bookResponseEntity = testRestTemplate.exchange("/books/{id}", HttpMethod.DELETE,
                null, Void.class, savedBook.getId());

        Assertions.assertThat(bookResponseEntity).isNotNull();
        Assertions.assertThat(bookResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
