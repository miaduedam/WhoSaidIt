package app.daos;

import app.config.HibernateConfig;
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
    public void insertPerson(Person person) {
        try(EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(person);
            em.getTransaction().commit();
        }
    }

    // Read by ID
    public Person getPersonById(int id) {
        try(EntityManager em = emf.createEntityManager()){
            return em.find(Person.class, id);
        }
    }

    // Read by name
    public Person getPersonByName(String name) {
        try(EntityManager em = emf.createEntityManager()){
            TypedQuery<Person> query = em.createQuery(
                    "SELECT p FROM Person p WHERE p.name = :name", Person.class);
            query.setParameter("name", name);
            List<Person> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        }
    }

    // Update
    public Person updatePerson(Person person) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Person update = em.merge(person);
            em.getTransaction().commit();
            return update;
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
    public List<Person> getAllPersons() {
            try (EntityManager em = emf.createEntityManager()) {
                TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p", Person.class);
                return query.getResultList();
            }
        }

}
