package app.daos;

import app.PersonPopulator;
import app.entities.Person;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuoteDAOTest {

    private EntityManagerFactory emf;
    private QuoteDAO quoteDAO;
    private Person p1, p2, p3;
    private List<Person> persons;


    @BeforeEach
    void setUp() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createNativeQuery("TRUNCATE TABLE fee, person_detail, person RESTART IDENTITY CASCADE")
                    .executeUpdate();
            em.getTransaction().commit();
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to truncate tables", e);
        }

        persons = PersonPopulator.populatePersons(quoteDAO);
        if (persons.size() == 3) {
            p1 = persons.get(0);
            p2 = persons.get(1);
            p3 = persons.get(2);
        } else {
            throw new ApiException(500, "Populator doesnt work");
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void insertQuote() {
    }

    @Test
    void getAllQuotes() {
    }

    @Test
    void getQuotesByPerson() {
    }

    @Test
    void getQuoteById() {
    }

    @Test
    void updateQuote() {
    }

    @Test
    void deleteQuote() {
    }
}