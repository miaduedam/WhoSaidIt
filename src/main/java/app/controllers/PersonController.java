package app.controllers;

import app.daos.PersonDAO;
import app.entities.Person;

import java.sql.SQLException;

public class PersonController {

    private final PersonDAO personDAO;

    public PersonController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    public void addPerson(Person person) throws SQLException {
        personDAO.insertPerson(person);
    }

    public Person getPersonByName(String name) throws SQLException {
        return personDAO.getPersonByName(name);
    }
}
