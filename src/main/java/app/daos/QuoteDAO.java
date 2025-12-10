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
    public Quote createQuote(Quote quote) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            // Sørg for at person er managed
            Person person = quote.getPerson();
            if (person != null && person.getId() != null) {
                Person managedPerson = em.find(Person.class, person.getId());
                quote.setPerson(managedPerson);
            }
            em.persist(quote);
            em.getTransaction().commit();
            return quote;
        }
    }

    // Bruges af dit QuoteAPIController
    public void insertQuote(Quote quote) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Person person = quote.getPerson();
            if (person != null && person.getId() != null) {
                Person managedPerson = em.find(Person.class, person.getId());
                quote.setPerson(managedPerson);
            }
            em.persist(quote);
            em.getTransaction().commit();
        }
    }

    // Read all
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

    // Read by person entity
    public List<Quote> getQuotesByPerson(Person person) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Quote> query = em.createQuery(
                    "SELECT q FROM Quote q WHERE q.person = :person", Quote.class);
            query.setParameter("person", person);
            return query.getResultList();
        }
    }

    // Read by person name
    public List<Quote> getQuotesByPersonNameEntity(String name) {
        try (EntityManager em = emf.createEntityManager()) {
            Person person = em.createQuery(
                            "SELECT p FROM Person p WHERE p.name = :name", Person.class)
                    .setParameter("name", name)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (person == null) {
                return List.of();
            }

            TypedQuery<Quote> query = em.createQuery(
                    "SELECT q FROM Quote q WHERE q.person = :person", Quote.class);
            query.setParameter("person", person);
            return query.getResultList();
        }
    }

    // Update
    public Quote updateQuote(Integer id, Quote updatedData) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Quote existing = em.find(Quote.class, id);
            if (existing == null) {
                em.getTransaction().rollback();
                return null;
            }
            existing.setText(updatedData.getText());
            Quote merged = em.merge(existing);
            em.getTransaction().commit();
            return merged;
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

    public boolean validatePrimaryKey(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Quote quote = em.find(Quote.class, id);
            return quote != null;
        }
    }
}