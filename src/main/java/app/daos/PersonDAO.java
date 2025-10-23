package app.daos;

import app.entities.Person;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class PersonDAO {

    private final EntityManagerFactory emf;

    public PersonDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // Create
    public void insertPerson(Person person) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(person);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    // Read by ID
    public Person getPersonById(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Person.class, id);
        } finally {
            em.close();
        }
    }

    // Read by name
    public Person getPersonByName(String name) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Person> query = em.createQuery(
                    "SELECT p FROM Person p WHERE p.name = :name", Person.class);
            query.setParameter("name", name);
            List<Person> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } finally {
            em.close();
        }
    }

    // Update
    public void updatePerson(Person person) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(person);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    // Delete
    public void deletePerson(int id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Person person = em.find(Person.class, id);
            if (person != null) {
                em.remove(person);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    // Read all persons
    public List<Person> getAllPersons() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p", Person.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
