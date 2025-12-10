package app;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.controllers.PersonController;
import app.daos.PersonDAO;
import app.daos.QuoteDAO;
import app.dtos.QuoteDTO;
import app.entities.Person;
import app.entities.Quote;
import app.services.QuoteService;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {

        ApplicationConfig.startServer(7070);

//            String apiKey = System.getenv("API_NINJA_KEY");
//            if (apiKey == null) throw new IllegalStateException("Set API_NINJAS_KEY environment variable!");
//
//            QuoteService quoteService = new QuoteService(apiKey);
//            List<QuoteDTO> quotes = quoteService.fetchQuotes(50);
//
//            System.out.println("Got " + quotes.size() + " quotes from API");
//            for (QuoteDTO q : quotes) {
//                System.out.println(q.getAuthor() + " : " + q.getQuote());
//            }





//        // Hold main-tråden i live
//
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        QuoteDAO quoteDAO = QuoteDAO.getInstance(emf);
        PersonDAO personDAO = PersonDAO.getInstance(emf);
//
//        PersonController personController = new PersonController();
//        List<Person> persons = personDAO.getAllPersons();
//
//        System.out.println(persons.size());
//
//
//
        String apiKey = System.getenv("API_NINJA_KEY");
        if (apiKey == null) throw new IllegalStateException("Set API_NINJA_KEY environment variable!");
        QuoteService quoteService = new QuoteService(apiKey);
        List<QuoteDTO> quotes = quoteService.fetchQuotes(5);

        for (QuoteDTO dto : quotes) {
            // tjekker om personen findes
            Person author = personDAO.getPersonByName(dto.getAuthor());
            // hvis ikke så laver vi en ny person
            if (author == null) {
                author = new Person();
                author.setName(dto.getAuthor());
                personDAO.createPerson(author);
            }
            //opretter en quote med personen
            Quote quoteEntity = new Quote(dto.getQuote(), author);
            quoteDAO.insertQuote(quoteEntity);
        }



    }
}
