package academy.devdojo.springboot.repository;

import academy.devdojo.springboot.domain.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("Tests for Book Repository")
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Save creates book when successful")
     void save_PersistBook_WhenSuccessful(){
        Book bookToBeSaved = creatBook();
        Book savedBook = this.bookRepository.save(bookToBeSaved);
        Assertions.assertThat(savedBook).isNotNull();
        Assertions.assertThat(savedBook.getId()).isNotNull();
        Assertions.assertThat(savedBook.getName()).isEqualTo(bookToBeSaved.getName());
     }

     private Book creatBook(){
        return Book.builder()
                .name("O cururu saltitante O RETORNO")
                .build();
     }
}