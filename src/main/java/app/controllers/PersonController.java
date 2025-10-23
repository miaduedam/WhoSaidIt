package app.controllers;

import app.daos.PersonDAO;
import app.entities.Person;

import java.util.List;

public class PersonController {

    private final PersonDAO personDAO;

    public PersonController(PersonDAO personDAO) {
        this.personDAO = personDAO;
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
