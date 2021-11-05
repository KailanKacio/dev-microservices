package academy.devdojo.springboot.util;

import academy.devdojo.springboot.domain.Book;

public class BookCreator {

    public static Book createBookToBeSaved() {
        return Book.builder()
                .name("O cururu saltitante O RETORNO")
                .build();
    }

    public static Book createValidBook() {
        return Book.builder()
                .name("O cururu saltitante O RETORNO")
                .id(1L)
                .build();
    }

    public static Book createValidUpdatedBook() {
        return Book.builder()
                .name("O cururu saltitante O RETORNO 2")
                .id(1L)
                .build();
    }
}
