package academy.devdojo.springboot.service;

import academy.devdojo.springboot.domain.Book;
import academy.devdojo.springboot.exceptions.BadRequestException;
import academy.devdojo.springboot.mapper.BookMapper;
import academy.devdojo.springboot.repository.BookRepository;
import academy.devdojo.springboot.request.BookPostRequestBody;
import academy.devdojo.springboot.request.BookPutRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public Page<Book> listAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public List<Book> listAllNonPageable() {
        return bookRepository.findAll();
    }

    public List<Book> findByName(String name) {
        return bookRepository.findByName(name);
    }

    public Book findByIdOrThrowBadRequestException(long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Book not found"));
    }

    @Transactional
    public Book save(BookPostRequestBody bookPostRequestBody) {
       return bookRepository.save(BookMapper.INSTANCE.toBook(bookPostRequestBody));
    }

    public void delete(long id) {
        bookRepository.delete(findByIdOrThrowBadRequestException(id));
    }

    public void replace(BookPutRequestBody bookPutRequestBody) {
        Book savedBook = findByIdOrThrowBadRequestException(bookPutRequestBody.getId());
        Book book = BookMapper.INSTANCE.toBook(bookPutRequestBody);
        book.setId(savedBook.getId());
        bookRepository.save(book);
    }
}