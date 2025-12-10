package app.controllers;

import app.config.HibernateConfig;
import app.daos.PersonDAO;
import app.daos.QuoteDAO;
import app.dtos.QuoteDTO;
import app.entities.Person;
import app.entities.Quote;
import app.services.QuoteService;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class QuoteAPIController {

    private final PersonDAO personDAO;
    private final QuoteDAO quoteDAO;
    private final QuoteService quoteService;

    public QuoteAPIController(PersonDAO personDAO, QuoteDAO quoteDAO, QuoteService quoteService) {
        this.personDAO = personDAO;
        this.quoteDAO = quoteDAO;
        this.quoteService = quoteService;
    }


    // Denne metode bruges som handler for POST /quotes/import/{limit}
    public void fetchAndSaveQuotes(Context ctx) throws Exception {
        int limit = ctx.pathParamAsClass("limit", Integer.class).get();

        List<QuoteDTO> quotes = quoteService.fetchQuotes(limit);

        int saved = 0;

        for (QuoteDTO dto : quotes) {
            // 1️⃣ Find eller opret person
            Person author = personDAO.getPersonByName(dto.getAuthor());
            if (author == null) {
                author = new Person(dto.getAuthor());
                author = personDAO.createPerson(author);  // arbejder kun med entities
            }

            // 2️⃣ Lav quote MED PERSON
            Quote quote = new Quote(dto.getQuote(), author);

            // 3️⃣ Gem quote (din insertQuote skal selvfølgelig lave persist/commit)
            quoteDAO.insertQuote(quote);
            saved++;
        }

        ctx.status(201).json("Imported " + saved + " quotes");
    }
}
