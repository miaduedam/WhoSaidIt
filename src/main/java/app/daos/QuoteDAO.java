package app.daos;

import app.entities.Quote;
import app.entities.Person;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class QuoteDAO {

    private final EntityManagerFactory emf;

    public QuoteDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // Create
    public void insertQuote(Quote quote) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(quote);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    // Read all quotes
    public List<Quote> getAllQuotes() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Quote> query = em.createQuery("SELECT q FROM Quote q", Quote.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Read by ID
    public Quote getQuoteById(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Quote.class, id);
        } finally {
            em.close();
        }
    }

    // Read by person
    public List<Quote> getQuotesByPerson(Person person) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Quote> query = em.createQuery(
                    "SELECT q FROM Quote q WHERE q.person = :person", Quote.class);
            query.setParameter("person", person);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Update
    public void updateQuote(Quote quote) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(quote);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    // Delete
    public void deleteQuote(int id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Quote quote = em.find(Quote.class, id);
            if (quote != null) {
                em.remove(quote);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
