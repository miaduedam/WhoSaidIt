package app.daos;

import app.dtos.PersonDTO;
import app.entities.Person;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class PersonDAO {

    private static PersonDAO instance;
    private static EntityManagerFactory emf;


    public static PersonDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonDAO();
        }
        return instance;
    }

    // Create
    public PersonDTO createPerson(PersonDTO personDTO) {
        try(EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Person person = new Person(personDTO);
            em.persist(person);
            em.getTransaction().commit();
            return new PersonDTO(person);
        }
    }

    // Read by ID
    public PersonDTO getPersonById(Integer id) {
        try(EntityManager em = emf.createEntityManager()){
            Person person = em.find(Person.class, id);
            return new PersonDTO(person);
        }
    }

    // Read by name
    public PersonDTO getPersonByName(String name) {
        try(EntityManager em = emf.createEntityManager()){
            TypedQuery<Person> query = em.createQuery(
                    "SELECT p FROM Person p WHERE p.name = :name", Person.class);
            query.setParameter("name", name);
            List<Person> results = query.getResultList();
            if (results.isEmpty())
                return null;
            return new PersonDTO(results.get(0));
        }
    }

    // Update
    public PersonDTO updatePerson(Integer integer, PersonDTO personDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Person person = em.find(Person.class, integer);
            person.setName(personDTO.getAuthor());

            Person updated = em.merge(person);
            em.getTransaction().commit();
            return updated != null ? new PersonDTO(updated) : null;
        }
    }


    // Delete
    public void deletePerson(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Person person = em.find(Person.class, id);
            if (person != null) {
                em.remove(person);
            }
            em.getTransaction().commit();
        }
    }

    // Read all persons
    public List<PersonDTO> getAllPersons() {
            try (EntityManager em = emf.createEntityManager()) {
                TypedQuery<PersonDTO> query = em.createQuery("SELECT p FROM Person p", PersonDTO.class);
                return query.getResultList();
            }
        }

    //Validate
    public boolean validatePrimaryKey(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Person person = em.find(Person.class, id);
            return person != null;
        }
    }

}
