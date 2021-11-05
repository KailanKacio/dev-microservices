package academy.devdojo.springboot.util;

import academy.devdojo.springboot.request.BookPostRequestBody;

public class BookPostRequestBodyCreator {

    public static BookPostRequestBody createBookPostRequestBody() {
        return BookPostRequestBody.builder()
                .name(BookCreator.createBookToBeSaved().getName())
                .build();
    }
}
