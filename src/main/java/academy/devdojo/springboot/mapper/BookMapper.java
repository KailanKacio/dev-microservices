package academy.devdojo.springboot.mapper;


import academy.devdojo.springboot.domain.Book;
import academy.devdojo.springboot.request.BookPostRequestBody;
import academy.devdojo.springboot.request.BookPutRequestBody;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class BookMapper {

    public static final BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    public abstract Book toBook(BookPostRequestBody bookPostRequestBody);

    public abstract Book toBook(BookPutRequestBody bookPutRequestBody);
}
