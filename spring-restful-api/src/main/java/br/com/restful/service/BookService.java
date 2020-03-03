package br.com.restful.service;

import br.com.restful.converter.DozerConverter;
import br.com.restful.data.BookVO;
import br.com.restful.exception.ResourceNotFoundException;
import br.com.restful.model.Book;
import br.com.restful.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public BookVO create(BookVO bookVO) {
        var entity = DozerConverter.parseObject(bookVO, Book.class);
        return DozerConverter.parseObject(bookRepository.save(entity), BookVO.class);
    }

    public BookVO update(BookVO bookVO) {
        var entity = bookRepository.findById(bookVO.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        entity.setAuthor(bookVO.getAuthor());
        entity.setLaunchDate(bookVO.getLaunchDate());
        entity.setPrice(bookVO.getPrice());
        entity.setTitle(bookVO.getTitle());

        return DozerConverter.parseObject(bookRepository.save(entity), BookVO.class);
    }

    public void delete(Long id) {
        var entity = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        bookRepository.delete(entity);
    }

    @Transactional(readOnly = true)
    public BookVO findById(Long id) {
        var entity = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        return DozerConverter.parseObject(entity, BookVO.class);
    }

    @Transactional(readOnly = true)
    public List<BookVO> findAll() {
        return DozerConverter.parseListObjects(bookRepository.findAll(), BookVO.class);
    }


}
