package br.com.restful.controller;

import br.com.restful.data.PersonVO;
import br.com.restful.service.PersonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(value = "Person Endpoint", description = "Api for person management", tags = "person-api")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/person")
public class PersonController {

    private final PersonService personService;

    @ApiOperation(value = "Find a single person")
    @GetMapping(value = "/{id}", produces = {"application/json", "application/xml", "application/x-yaml"})
    public PersonVO findById(@ApiParam(value = "Id referring to person", required = true) @PathVariable("id") Long id) {
        var personVO = personService.findById(id);
        personVO.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).findById(id)).withSelfRel());
        return personVO;
    }

    // @CrossOrigin(origins = {"http://localhost:8080", "http://www.erudio.com.br"})
    @ApiOperation(value = "Find all people recorded")
    @GetMapping(produces = {"application/json", "application/xml", "application/x-yaml"})
    public List<PersonVO> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {

        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "firstName"));

        var personsVO = personService.findAll(pageable);
        personsVO.forEach(p -> p.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));
        return personsVO;
    }

    @ApiOperation(value = "Find a single person by firstName")
    @GetMapping(value = "/findPersonByName/{firstName}",
            produces = {"application/json", "application/xml", "application/x-yaml"})
    public List<PersonVO> findPersonByName(
            @PathVariable("firstName") String firstName,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {

        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "firstName"));

        var personsVO = personService.findPersonByName(firstName, pageable);
        personsVO.forEach(p -> p.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));
        return personsVO;
    }

    @ApiOperation(value = "Create a new person")
    @PostMapping(produces = {"application/json", "application/xml", "application/x-yaml"},
            consumes = {"application/json", "application/xml", "application/x-yaml"})
    public PersonVO create(@RequestBody @Valid PersonVO person) {
        var personVO = personService.create(person);
        personVO.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).findById(personVO.getKey())).withSelfRel());
        return personVO;
    }

    @ApiOperation(value = "Update an existing person")
    @PutMapping(produces = {"application/json", "application/xml", "application/x-yaml"},
            consumes = {"application/json", "application/xml", "application/x-yaml"})
    public PersonVO update(@RequestBody @Valid PersonVO person) {
        var personVO = personService.update(person);
        personVO.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).findById(personVO.getKey())).withSelfRel());
        return personVO;
    }

    @ApiOperation(value = "Delete a person")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@ApiParam(value = "Id referring to person", required = true) @PathVariable("id") Long id) {
        personService.delete(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Disable a single person")
    @PatchMapping(value = "/{id}", produces = {"application/json", "application/xml", "application/x-yaml"})
    public PersonVO disablePerson(@ApiParam(value = "Id referring to person", required = true) @PathVariable("id") Long id) {
        var personVO = personService.disablePerson(id);
        personVO.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).findById(id)).withSelfRel());
        return personVO;
    }
}
