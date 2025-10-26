package app.controllers;

import app.config.HibernateConfig;
import app.daos.QuoteDAO;
import app.dtos.PersonDTO;
import app.dtos.QuoteDTO;
import app.entities.Person;

import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class QuoteController implements IController {

    private final QuoteDAO quoteDAO;

    public QuoteController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.quoteDAO = QuoteDAO.getInstance(emf);
    }


    @Override
    public void read(Context ctx) {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // DTO
        QuoteDTO quoteDTO = quoteDAO.getQuoteById(id);
        // response
        ctx.res().setStatus(200);
        ctx.json(quoteDTO, PersonDTO.class);

    }

    @Override
    public void readAll(Context ctx) {
        //List of dtos
        List<QuoteDTO> quoteDTOS = quoteDAO.getAllQuotes();
        // response
        ctx.res().setStatus(200);
        ctx.json(quoteDTOS, PersonDTO.class);
    }

    @Override
    public void create(Context ctx) {
        //Request
        QuoteDTO jsonRequest = ctx.bodyAsClass(QuoteDTO.class);
        //DTO
        QuoteDTO quoteDTO = quoteDAO.createQuote(jsonRequest);
        //Response
        ctx.res().setStatus(201);
        ctx.json(quoteDTO,QuoteDTO.class);
    }

    @Override
    public void update(Context ctx) {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // dto
        QuoteDTO quoteDTO = quoteDAO.updateQuote(id, validateEntity(ctx));
        // response
        ctx.res().setStatus(200);
        ctx.json(quoteDTO, Person.class);
    }

    @Override
    public void delete(Context ctx) {
        //Request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        quoteDAO.deleteQuote(id);
        // response
        ctx.res().setStatus(204);

    }

    public void readQuotesByPerson(Context ctx) {
        String name = ctx.pathParam("name");
        List<QuoteDTO> quotes = quoteDAO.getQuotesByPersonName(name);

        if (quotes.isEmpty()) {
            ctx.status(404).result("No quotes found for person: " + name);
        } else {
            ctx.status(200).json(quotes);
        }
    }



    public boolean validatePrimaryKey(Integer integer) {
        return quoteDAO.validatePrimaryKey(integer);
    }

    public QuoteDTO validateEntity(Context ctx) {
        return ctx.bodyValidator(QuoteDTO.class)
                .check( q -> q.getAuthor() != null && !q.getAuthor().isEmpty(), "Authors name must be set")
                .check( q -> q.getQuote() != null && !q.getQuote().isEmpty(), "Quote name must be set")
                .get();
    }
}
