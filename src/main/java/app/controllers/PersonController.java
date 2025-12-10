package app.controllers;

import app.config.HibernateConfig;
import app.daos.PersonDAO;

import app.dtos.PersonDTO;
import app.entities.Person;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;


import java.util.ArrayList;
import java.util.List;

public class PersonController implements IController{

    private final PersonDAO personDAO;

    public PersonController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.personDAO = PersonDAO.getInstance(emf);
    }

    @Override
    public void read(Context ctx) {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class)
                .check(this::validatePrimaryKey, "Not a valid id")
                .get();
        Person person = personDAO.getPersonById(id);
        if (person == null) {
            ctx.status(404).result("Person not found with id: " + id);
            return;
        }
        // DTO
        PersonDTO personDTO = new PersonDTO(person);
        // response
        ctx.status(200);
        ctx.json(personDTO);

    }

    @Override
    public void readAll(Context ctx) {

        List<Person> persons = personDAO.getAllPersons();

        if (persons == null || persons.isEmpty()) {
            ctx.status(404).result("there is no list of people");
            return;
        }
        // Map entities -> DTOs
        List<PersonDTO> personDTOS = persons.stream()
                .map(PersonDTO::new)   // bruger din PersonDTO(Person person)-constructor
                .toList();
        ctx.status(200).json(personDTOS);
    }


    @Override
    public void create(Context ctx) {
        //Læs request-body som DTO
        PersonDTO jsonRequest = ctx.bodyAsClass(PersonDTO.class);
        //Map DTO -> Entity
        Person personEntity = new Person(jsonRequest);   // bruger din constructor Person(PersonDTO)
        //Gem via DAO (arbejder med entiteter)
        Person created = personDAO.createPerson(personEntity);
        //Map tilbage til DTO til response
        PersonDTO responseDTO = new PersonDTO(created);
        //Response
        ctx.res().setStatus(201);
        ctx.json(responseDTO);
    }


    @Override
    public void update(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class)
                .check(this::validatePrimaryKey, "Not a valid id")
                .get();
        // Request DTO (valideret)
        PersonDTO requestDTO = validateEntity(ctx);
        // DTO -> Entity
        Person updatedData = new Person(requestDTO);
        Person updated = personDAO.updatePerson(id, updatedData);
        if (updated == null) {
            ctx.status(404).result("Person not found with id: " + id);
            return;
        }
        // Entity -> DTO
        PersonDTO responseDTO = new PersonDTO(updated);
        ctx.status(200).json(responseDTO);
    }

    @Override
    public void delete(Context ctx) {
        //Request
        int id = ctx.pathParamAsClass("id", Integer.class)
                .check(this::validatePrimaryKey, "Not a valid id")
                .get();
        personDAO.deletePerson(id);
        // response
        ctx.status(204);

    }

    public void readByName(Context ctx){
        String name = ctx.pathParam("name");
        Person person = personDAO.getPersonByName(name);

        if (person == null) {
            ctx.status(404).result("No person found with name: " + name);
            return;
        }

        PersonDTO personDTO = new PersonDTO(person);
        ctx.status(200).json(personDTO);
    }


    public boolean validatePrimaryKey(Integer integer) {
        return personDAO.validatePrimaryKey(integer);
    }

    public PersonDTO validateEntity(Context ctx) {
        return ctx.bodyValidator(PersonDTO.class)
                .check( p -> p.getAuthor() != null && !p.getAuthor().isEmpty(), "Authors name must be set")
                .get();
    }
}
