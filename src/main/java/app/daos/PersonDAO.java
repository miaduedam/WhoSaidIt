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
    public Person createPerson(Person person) {
        try(EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(person);
            em.getTransaction().commit();
            return person;
        }
    }

    // Read by ID
    public Person getPersonById(Integer id) {
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
            if (results.isEmpty())
                return null;
            return results.get(0);
        }
    }

    // Update
    public Person updatePerson(Integer id, Person updatedData) {
        try (EntityManager em = emf.createEntityManager()) {

            em.getTransaction().begin();
            // 1️⃣ Find eksisterende entity (managed)
            Person existing = em.find(Person.class, id);
            if (existing == null) {
                em.getTransaction().rollback();
                return null;
            }
            // 2️⃣ Opdater felter på den MANAGED entity
            existing.setName(updatedData.getName());
            // 3️⃣ merge er egentlig ikke engang nødvendigt fordi 'existing' ER managed
            // men du kan bruge det for sikkerhed:
            Person merged = em.merge(existing);
            em.getTransaction().commit();
            return merged;
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

    //Validate
    public boolean validatePrimaryKey(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Person person = em.find(Person.class, id);
            return person != null;
        }
    }

}
