package app.daos;

import app.entities.Quote;
import app.entities.Person;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class QuoteDAO {
    private static QuoteDAO instance;
    private static EntityManagerFactory emf;


    public static QuoteDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new QuoteDAO();
        }
        return instance;
    }


    // Create
    public void insertQuote(Quote quote) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(quote);
            em.getTransaction().commit();
        }
    }

    // Read all quotes
    public List<Quote> getAllQuotes() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Quote> query = em.createQuery("SELECT q FROM Quote q", Quote.class);
            return query.getResultList();
        }
    }

    // Read by ID
    public Quote getQuoteById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Quote.class, id);
        }
    }

    // Read by person
    public List<Quote> getQuotesByPerson(Person person) {
        try (EntityManager em = emf.createEntityManager()) {
            try {
                TypedQuery<Quote> query = em.createQuery(
                        "SELECT q FROM Quote q WHERE q.person = :person", Quote.class);
                query.setParameter("person", person);
                return query.getResultList();
            } finally {
                em.close();
            }
        }
    }

    // Update
    public void updateQuote(Quote quote) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(quote);
            em.getTransaction().commit();
        }
    }

    // Delete
    public void deleteQuote(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Quote quote = em.find(Quote.class, id);
            if (quote != null) {
                em.remove(quote);
            }
            em.getTransaction().commit();
        }
    }
}
