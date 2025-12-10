package app.controllers;

import app.config.HibernateConfig;
import app.daos.QuoteDAO;
import app.dtos.PersonDTO;
import app.dtos.QuoteDTO;
import app.entities.Person;

import app.entities.Quote;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class QuoteController implements IController {

    private final QuoteDAO quoteDAO;

    public QuoteController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.quoteDAO = QuoteDAO.getInstance(emf);
    }


    @Override
    public void read(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class)
                .check(this::validatePrimaryKey, "Not a valid id")
                .get();

        Quote quote = quoteDAO.getQuoteById(id);
        if (quote == null) {
            ctx.status(404).result("Quote not found with id: " + id);
            return;
        }

        QuoteDTO quoteDTO = new QuoteDTO(quote);
        ctx.status(200).json(quoteDTO);
    }

    @Override
    public void readAll(Context ctx) {
        List<Quote> quotes = quoteDAO.getAllQuotes();

        List<QuoteDTO> quoteDTOS = quotes.stream()
                .map(QuoteDTO::new)
                .collect(Collectors.toList());

        ctx.status(200).json(quoteDTOS);
    }

    @Override
    public void create(Context ctx) {
        // Request (DTO ind fra klienten)
        QuoteDTO jsonRequest = ctx.bodyAsClass(QuoteDTO.class);
        // Evt. validering
        QuoteDTO validDTO = validateEntity(ctx); // bruger getAuthor() og getQuote()
        // DTO -> ENTITY
        Quote quoteToSave = new Quote(validDTO); // bruger din Quote(QuoteDTO) ctor
        // Gem i DB (DAO arbejder kun med entities)
        Quote saved = quoteDAO.createQuote(quoteToSave);
        // ENTITY -> DTO til svar
        QuoteDTO responseDTO = new QuoteDTO(saved);

        ctx.status(201).json(responseDTO);
    }



    @Override
    public void update(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class)
                .check(this::validatePrimaryKey, "Not a valid id")
                .get();

        // DTO fra request
        QuoteDTO requestDTO = validateEntity(ctx);

        // Hent eksisterende entity
        Quote existing = quoteDAO.getQuoteById(id);
        if (existing == null) {
            ctx.status(404).result("Quote not found with id: " + id);
            return;
        }

        // Opdatér fields på entity
        existing.setText(requestDTO.getQuote());

        // Gem ændringer via DAO (entity-baseret)
        Quote updated = quoteDAO.updateQuote(id, existing);

        QuoteDTO responseDTO = new QuoteDTO(updated);
        ctx.status(200).json(responseDTO);
    }

    @Override
    public void delete(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class)
                .check(this::validatePrimaryKey, "Not a valid id")
                .get();

        quoteDAO.deleteQuote(id);
        ctx.status(204);
    }


    public void readQuotesByPerson(Context ctx) {
        String name = ctx.pathParam("name");

        // DAO returnerer entities
        List<Quote> quotes = quoteDAO.getQuotesByPersonNameEntity(name);

        if (quotes.isEmpty()) {
            ctx.status(404).result("No quotes found for person: " + name);
            return;
        }

        // ENTITY -> DTO
        List<QuoteDTO> dtos = quotes.stream()
                .map(QuoteDTO::new)
                .collect(Collectors.toList());

        ctx.status(200).json(dtos);
    }

    public boolean validatePrimaryKey(Integer id) {
        return quoteDAO.validatePrimaryKey(id);
    }

    public QuoteDTO validateEntity(Context ctx) {
        return ctx.bodyValidator(QuoteDTO.class)
                .check(q -> q.getAuthor() != null && !q.getAuthor().isEmpty(), "Authors name must be set")
                .check(q -> q.getQuote() != null && !q.getQuote().isEmpty(), "Quote text must be set")
                .get();
    }
}