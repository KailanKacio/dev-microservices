package academy.devdojo.springboot.controller;

import academy.devdojo.springboot.domain.Book;
import academy.devdojo.springboot.request.BookPostRequestBody;
import academy.devdojo.springboot.request.BookPutRequestBody;
import academy.devdojo.springboot.service.BookService;
import academy.devdojo.springboot.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("books")
@Log4j2
@RequiredArgsConstructor
public class BookController {
    private final DateUtil dateUtil;
    private final BookService bookService;

    @GetMapping
    public ResponseEntity<Page<Book>> list(Pageable pageable) {
        log.info((dateUtil.formatLocalDateTimeToDarabasesStyle(LocalDateTime.now())));
        return ResponseEntity.ok(bookService.listAll(pageable));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Book> findById(@PathVariable long id) {
        return new ResponseEntity<>(bookService.findByIdOrThrowBadRequestException(id), HttpStatus.OK);
    }

    @GetMapping(path = "/find")
    public ResponseEntity<List<Book>> findByName(@RequestParam String name) {
        return ResponseEntity.ok(bookService.findByName(name));
    }

    @PostMapping
    public ResponseEntity<Book> save(@RequestBody @Valid BookPostRequestBody bookPostRequestBody){
        return new ResponseEntity<>(bookService.save(bookPostRequestBody), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        bookService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody BookPutRequestBody bookPutRequestBody) {
        bookService.replace(bookPutRequestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}