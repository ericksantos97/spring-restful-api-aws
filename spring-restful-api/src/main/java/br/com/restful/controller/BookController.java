package br.com.restful.controller;

import br.com.restful.data.BookVO;
import br.com.restful.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(value = "Book Endpoint", description = "Api for book management", tags = "book-api")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/book")
public class BookController {

    private final BookService bookService;

    @ApiOperation(value = "Find a single book")
    @GetMapping(value = "/{id}", produces = {"application/json", "application/xml", "application/x-yaml"})
    public BookVO findById(@ApiParam(value = "Id referring to book", required = true) @PathVariable("id") Long id) {
        var bookVO = bookService.findById(id);
        bookVO.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BookController.class).findById(id)).withSelfRel());
        return bookVO;
    }

    @ApiOperation(value = "Find all books recorded")
    @GetMapping(produces = {"application/json", "application/xml", "application/x-yaml"})
    public List<BookVO> findAll() {
        var booksVO = bookService.findAll();
        booksVO.forEach(p -> p.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BookController.class).findById(p.getKey())).withSelfRel()));
        return booksVO;
    }

    @ApiOperation(value = "Create a new book")
    @PostMapping(produces = {"application/json", "application/xml", "application/x-yaml"},
            consumes = {"application/json", "application/xml", "application/x-yaml"})
    public BookVO create(@RequestBody @Valid BookVO book) {
        var bookVO = bookService.create(book);
        bookVO.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BookController.class).findById(bookVO.getKey())).withSelfRel());
        return bookVO;
    }

    @ApiOperation(value = "Update an existing book")
    @PutMapping(produces = {"application/json", "application/xml", "application/x-yaml"},
            consumes = {"application/json", "application/xml", "application/x-yaml"})
    public BookVO update(@RequestBody @Valid BookVO book) {
        var bookVO = bookService.update(book);
        bookVO.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BookController.class).findById(bookVO.getKey())).withSelfRel());
        return bookVO;
    }

    @ApiOperation(value = "Delete a book")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@ApiParam(value = "Id referring to book", required = true) @PathVariable("id") Long id) {
        bookService.delete(id);
        return ResponseEntity.ok().build();
    }
}
