package app.daos;

import app.dtos.PersonDTO;
import app.dtos.QuoteDTO;
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
    public QuoteDTO createQuote(QuoteDTO quoteDTO) {
        try(EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Quote quote = new Quote(quoteDTO);
            em.persist(quote);
            em.getTransaction().commit();
            return new QuoteDTO(quote);
        }
    }

    // Read all quotes
    public List<QuoteDTO> getAllQuotes() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<QuoteDTO> query = em.createQuery("SELECT q FROM Quote q", QuoteDTO.class);
            return query.getResultList();
        }
    }

    // Read by ID
    public QuoteDTO getQuoteById(int id) {
            try(EntityManager em = emf.createEntityManager()){
                Quote quote = em.find(Quote.class, id);
                return new QuoteDTO(quote);
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
    public QuoteDTO updateQuote(Integer integer, QuoteDTO quoteDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Quote quote = em.find(Quote.class, integer);
            quote.setText(quoteDTO.getQuote());

            Quote updated = em.merge(quote);
            em.getTransaction().commit();
            return updated != null ? new QuoteDTO(updated) : null;
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

    public List<QuoteDTO> getQuotesByPersonName(String name) {
        try (EntityManager em = emf.createEntityManager()) {
            //Find personen
            Person person = em.createQuery(
                            "SELECT p FROM Person p WHERE p.name = :name", Person.class)
                    .setParameter("name", name)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if (person == null) {
                throw new IllegalArgumentException("Person not found: " + name);
            }

            //Hent alle citater for den person
            TypedQuery<Quote> query = em.createQuery(
                    "SELECT q FROM Quote q WHERE q.person = :person", Quote.class);
            query.setParameter("person", person);
            List<Quote> results = query.getResultList();

            //Map alle entities til DTO’er
            return results.stream()
                    .map(QuoteDTO::new)
                    .toList();
        }
    }



    //Validate
    public boolean validatePrimaryKey(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Quote quote = em.find(Quote.class, id);
            return quote != null;
        }
    }
}
