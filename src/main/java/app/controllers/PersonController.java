package app.controllers;

import app.config.HibernateConfig;
import app.daos.PersonDAO;

import app.dtos.PersonDTO;
import app.entities.Person;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;


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
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // DTO
        PersonDTO personDTO = personDAO.getPersonById(id);
        // response
        ctx.res().setStatus(200);
        ctx.json(personDTO, PersonDTO.class);

    }

    @Override
    public void readAll(Context ctx) {
        //List of dtos
        List<PersonDTO> personDTOS = personDAO.getAllPersons();
        // response
        ctx.res().setStatus(200);
        ctx.json(personDTOS, PersonDTO.class);
    }

    @Override
    public void create(Context ctx) {
        //Request
        PersonDTO jsonRequest = ctx.bodyAsClass(PersonDTO.class);
        //DTO
        PersonDTO personDTO = personDAO.createPerson(jsonRequest);
        //Response
        ctx.res().setStatus(201);
        ctx.json(personDTO,PersonDTO.class);

    }

    @Override
    public void update(Context ctx) {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // dto
        PersonDTO personDTO = personDAO.updatePerson(id, validateEntity(ctx));
        // response
        ctx.res().setStatus(200);
        ctx.json(personDTO, Person.class);
    }

    @Override
    public void delete(Context ctx) {
        //Request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        personDAO.deletePerson(id);
        // response
        ctx.res().setStatus(204);

    }

    public void readByName(Context ctx){
        //Request
        String name = ctx.pathParam("name");
        PersonDTO personDTO = personDAO.getPersonByName(name);
        //response
        if (personDTO == null) {
            ctx.status(404).result("No person found with name: " + name);
        } else {
            ctx.status(200).json(personDTO);
        }
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
