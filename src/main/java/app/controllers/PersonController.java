package app.controllers;

import app.config.HibernateConfig;
import app.daos.PersonDAO;
import app.daos.QuoteDAO;
import app.entities.Person;
import jakarta.persistence.EntityManagerFactory;


import java.util.List;

public class PersonController {

    private final PersonDAO personDAO;

    public PersonController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.personDAO = PersonDAO.getInstance(emf);
    }

    // CREATE
    public void addPerson(Person person) {
        personDAO.insertPerson(person);
    }

    // READ
    public Person getPersonById(int id) {
        return personDAO.getPersonById(id);
    }

    public Person getPersonByName(String name) {
        return personDAO.getPersonByName(name);
    }

    public List<Person> getAllPersons() {
        return personDAO.getAllPersons();
    }

    // UPDATE
    public void updatePerson(Person person) {
        personDAO.updatePerson(person);
    }

    // DELETE
    public void deletePerson(int id) {
        personDAO.deletePerson(id);
    }
}
