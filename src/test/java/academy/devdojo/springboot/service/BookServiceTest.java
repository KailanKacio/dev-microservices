package academy.devdojo.springboot.service;

import academy.devdojo.springboot.domain.Book;
import academy.devdojo.springboot.repository.BookRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class BookServiceTest {

    @InjectMocks //utiliza quando quer testar a classe em sí;
    private BookService bookService;

    @Mock
    //utiliza quando quer testar todas as classes dentro da classe que quer testar; define o comportamento dos métodos;
    private BookRepository bookRepositoryMock;

    @BeforeEach
    void setUp() {
        PageImpl<Book> bookPage = new PageImpl<>(List.of(BookCreator.createValidBook()));
        BDDMockito.when(bookRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(bookPage);

        BDDMockito.when(bookRepositoryMock.findAll())
                .thenReturn(List.of(BookCreator.createValidBook()));

        BDDMockito.when(bookRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(BookCreator.createValidBook()));

        BDDMockito.when(bookRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(BookCreator.createValidBook()));

        BDDMockito.when(bookRepositoryMock.save(ArgumentMatchers.any(Book.class)))
                .thenReturn(BookCreator.createValidBook());

        BDDMockito.doNothing().when(bookRepositoryMock).delete(ArgumentMatchers.any(Book.class));
    }

    @Test
    @DisplayName("listAll returns list of book inside page object when successful")
    void listAll_ReturnsListOfBookInsidePageObject_WhenSuccessful() {
        Page<Book> bookPage = bookService.listAll(PageRequest.of(1,1));

        Assertions.assertThat(bookPage).isNotNull();

        Assertions.assertThat(bookPage.toList())
                .isNotEmpty();

        Assertions.assertThat(bookPage.toList().get(0));
    }

    @Test
    @DisplayName("listAllNonPageable returns list of book when successful")
    void listAllNonPageable_ReturnsListOfBook_WhenSuccessful() {
        List<Book> books = bookService.listAllNonPageable();

        Assertions.assertThat(books)
                .isNotNull()
                .isNotEmpty();
        Assertions.assertThat(books.get(0));
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException returns book when successful")
    void findByIdOrThrowBadRequestException_ReturnsBook_WhenSuccessful() {
        Long expectedId = BookCreator.createValidBook().getId();
        Book book = bookService.findByIdOrThrowBadRequestException(1);

        Assertions.assertThat(book)
                .isNotNull();

        Assertions.assertThat(book.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns list of book when successful")
    void findByName_ReturnsListOfBook_WhenSuccessful() {
        List<Book> books = bookService.findByName("O cururu saltitante O RETORNO");

        Assertions.assertThat(books)
                .isNotNull()
                .isNotEmpty();
        Assertions.assertThat(books.get(0));
    }

    @Test
    @DisplayName("findByName returns empty list of book when book is not found")
    void findByName_ReturnsEmptyListOfBook_WhenBookIsNotFound() {
        BDDMockito.when(bookRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Book> books = bookService.findByName("O cururu saltitante O RETORNO");

        Assertions.assertThat(books)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("save returns book when successful")
    void save_ReturnsBook_WhenSuccessful() {
        Book book = bookService.save(BookPostRequestBodyCreator.createBookPostRequestBody());

        Assertions.assertThat(book)
                .isNotNull().isEqualTo(BookCreator.createValidBook());
    }

    @Test
    @DisplayName("replace updates book when successful")
    void replace_UpdatesBook_WhenSuccessful() {

        Assertions.assertThatCode(() -> bookService.replace(BookPutRequestBodyCreator.createBookPutRequestBody()))
                .doesNotThrowAnyException();

    }

    @Test
    @DisplayName("delete removes book when successful")
    void delete_RemovesBook_WhenSuccessful() {

        Assertions.assertThatCode(()-> bookService.delete(1))
                .doesNotThrowAnyException();

    }
}