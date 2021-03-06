package academy.devdojo.springboot.repository;

import academy.devdojo.springboot.domain.Book;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static academy.devdojo.springboot.util.BookCreator.createBookToBeSaved;

@DataJpaTest
@DisplayName("Tests for Book Repository")
@Log4j2
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Save creates book when successful")
    void save_PersistBook_WhenSuccessful() {
        Book bookToBeSaved = createBookToBeSaved();
        Book bookSaved = this.bookRepository.save(bookToBeSaved);

        Assertions.assertThat(bookSaved).isNotNull();
        Assertions.assertThat(bookSaved.getId()).isNotNull();
        Assertions.assertThat(bookSaved.getName()).isEqualTo(bookToBeSaved.getName());
    }

    @Test
    @DisplayName("Save updates book when successful")
    void save_UpdatesBook_WhenSuccessful() {
        Book bookToBeSaved = createBookToBeSaved();
        Book bookSaved = this.bookRepository.save(bookToBeSaved);
        bookSaved.setName("O Peregrino");
        Book bookUpdated = this.bookRepository.save(bookSaved);

        Assertions.assertThat(bookUpdated).isNotNull();
        Assertions.assertThat(bookUpdated.getId()).isNotNull();
        Assertions.assertThat(bookUpdated.getName()).isEqualTo(bookSaved.getName());
    }

    @Test
    @DisplayName("Delete removes book when successful")
    void delete_RemovesBook_WhenSuccessful() {
        Book bookToBeSaved = createBookToBeSaved();
        Book bookSaved = this.bookRepository.save(bookToBeSaved);

        this.bookRepository.delete(bookSaved);
        Optional<Book> bookOptional = this.bookRepository.findById(bookSaved.getId());

        Assertions.assertThat(bookOptional).isEmpty();
    }

    @Test
    @DisplayName("Find by name returns list of book when successful")
    void findByName_ReturnsListOfBook_WhenSuccessful() {
        Book bookToBeSaved = createBookToBeSaved();
        Book bookSaved = this.bookRepository.save(bookToBeSaved);

        String name = bookSaved.getName();
        List<Book> books = this.bookRepository.findByName(name);

        Assertions.assertThat(books).isNotEmpty().contains(bookSaved);

    }

    @Test
    @DisplayName("Save throw ConstrainViolationException when name is empty")
    void save_ThrowConstrainViolationException_WhenNameIsEmpty() {
        Book book = new Book();

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> this.bookRepository.save(book))
                .withMessageContaining("The book cannot be empty");
    }

}