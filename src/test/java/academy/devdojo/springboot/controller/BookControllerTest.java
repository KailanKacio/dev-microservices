package academy.devdojo.springboot.controller;

import academy.devdojo.springboot.domain.Book;
import academy.devdojo.springboot.request.BookPostRequestBody;
import academy.devdojo.springboot.request.BookPutRequestBody;
import academy.devdojo.springboot.service.BookService;
import academy.devdojo.springboot.util.BookCreator;
import academy.devdojo.springboot.util.BookPostRequestBodyCreator;
import academy.devdojo.springboot.util.BookPutRequestBodyCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
class BookControllerTest {

    @InjectMocks //utiliza quando quer testar a classe em sí;
    private BookController bookController;

    @Mock
    //utiliza quando quer testar todas as classes dentro da classe que quer testar; define o comportamento dos métodos;
    private BookService bookServiceMock;

    @BeforeEach
    void setUp() {
        PageImpl<Book> bookPage = new PageImpl<>(List.of(BookCreator.createValidBook()));
        BDDMockito.when(bookServiceMock.listAll(ArgumentMatchers.any()))
                .thenReturn(bookPage);

        BDDMockito.when(bookServiceMock.listAllNonPageable())
                .thenReturn(List.of(BookCreator.createValidBook()));

        BDDMockito.when(bookServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(BookCreator.createValidBook());

        BDDMockito.when(bookServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(BookCreator.createValidBook()));

        BDDMockito.when(bookServiceMock.save(ArgumentMatchers.any(BookPostRequestBody.class)))
                .thenReturn(BookCreator.createValidBook());

        BDDMockito.doNothing().when(bookServiceMock).replace(ArgumentMatchers.any(BookPutRequestBody.class));

        BDDMockito.doNothing().when(bookServiceMock).delete(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("list returns list of book inside page object when successful")
    void list_ReturnsListOfBookInsidePageObject_WhenSuccessful() {
        Page<Book> bookPage = bookController.list(null).getBody();

        Assertions.assertThat(bookPage).isNotNull();

        Assertions.assertThat(bookPage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(bookPage.toList().get(0));
    }

    @Test
    @DisplayName("listAll returns list of book when successful")
    void listAll_ReturnsListOfBook_WhenSuccessful() {
        List<Book> books = bookController.listAll().getBody();

        Assertions.assertThat(books)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(books.get(0));
    }

    @Test
    @DisplayName("findById returns book when successful")
    void findById_ReturnsBook_WhenSuccessful() {
        Long expectedId = BookCreator.createValidBook().getId();
        Book book = bookController.findById(1).getBody();

        Assertions.assertThat(book)
                .isNotNull();

        Assertions.assertThat(book.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns list of book when successful")
    void findByName_ReturnsListOfBook_WhenSuccessful() {
        List<Book> books = bookController.findByName("name").getBody();

        Assertions.assertThat(books)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(books.get(0));
    }

    @Test
    @DisplayName("findByName returns empty list of book when book is not found")
    void findByName_ReturnsEmptyListOfBook_WhenBookIsNotFound() {
        BDDMockito.when(bookServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Book> books = bookController.findByName("name").getBody();

        Assertions.assertThat(books)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("save returns book when successful")
    void save_ReturnsBook_WhenSuccessful() {
        Book book = bookController.save(BookPostRequestBodyCreator.createBookPostRequestBody()).getBody();

        Assertions.assertThat(book)
                .isNotNull().isEqualTo(BookCreator.createValidBook());
    }

    @Test
    @DisplayName("replace updates book when successful")
    void replace_UpdatesBook_WhenSuccessful() {

        Assertions.assertThatCode(() -> bookController.replace(BookPutRequestBodyCreator.createBookPutRequestBody()))
                .doesNotThrowAnyException();

    }

    @Test
    @DisplayName("delete removes book when successful")
    void delete_RemovesBook_WhenSuccessful() {

        ResponseEntity<Void> entity = bookController.delete(1);

        Assertions.assertThat(entity)
                .isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }
}