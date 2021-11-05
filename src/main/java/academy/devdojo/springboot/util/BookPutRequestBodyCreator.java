package academy.devdojo.springboot.util;

import academy.devdojo.springboot.request.BookPutRequestBody;

public class BookPutRequestBodyCreator {

    public static BookPutRequestBody createBookPutRequestBody() {
        return BookPutRequestBody.builder()
                .id(BookCreator.createValidUpdatedBook().getId())
                .name(BookCreator.createValidUpdatedBook().getName())
                .build();
    }
}
