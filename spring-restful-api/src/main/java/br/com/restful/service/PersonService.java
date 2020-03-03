package br.com.restful.service;

import br.com.restful.converter.DozerConverter;
import br.com.restful.data.PersonVO;
import br.com.restful.exception.ResourceNotFoundException;
import br.com.restful.model.Person;
import br.com.restful.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public PersonVO create(PersonVO personVO) {
        var entity = DozerConverter.parseObject(personVO, Person.class);
        return DozerConverter.parseObject(personRepository.save(entity), PersonVO.class);
    }

    public PersonVO update(PersonVO personVO) {
        var entity = personRepository.findById(personVO.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        entity.setFirstName(personVO.getFirstName());
        entity.setLastName(personVO.getLastName());
        entity.setAddress(personVO.getAddress());
        entity.setGender(personVO.getGender());

        return DozerConverter.parseObject(personRepository.save(entity), PersonVO.class);
    }

    public void delete(Long id) {
        var entity = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        personRepository.delete(entity);
    }

    @Transactional(readOnly = true)
    public PersonVO findById(Long id) {
        var entity = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        return DozerConverter.parseObject(entity, PersonVO.class);
    }

    @Transactional(readOnly = true)
    public List<PersonVO> findAll(Pageable pageable) {
        var entities = personRepository.findAll(pageable).getContent();
        return DozerConverter.parseListObjects(entities, PersonVO.class);
    }

    @Transactional(readOnly = true)
    public List<PersonVO> findPersonByName(String firstName, Pageable pageable) {
        var entities = personRepository.findPersonByName(firstName, pageable);
        return DozerConverter.parseListObjects(entities.getContent(), PersonVO.class);
    }

    @Transactional
    public PersonVO disablePerson(Long id) {
        personRepository.disablePerson(id);
        var entity = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        return DozerConverter.parseObject(entity, PersonVO.class);
    }


}
