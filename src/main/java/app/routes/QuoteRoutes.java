package app.routes;

import app.controllers.QuoteController;
import app.entities.Quote;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class QuoteRoutes {

    private final QuoteController quoteController = new QuoteController();

    public EndpointGroup getRoutes() {
        return () -> {

            // GET /quotes
            get("/", ctx -> ctx.json(quoteController.getAllQuotes()));

// GET /quotes/{id}
            get("/{id}", ctx -> ctx.json(
                    quoteController.getQuoteById(Integer.parseInt(ctx.pathParam("id")))
            ));

// POST /quotes
            post("/", ctx -> quoteController.addQuote(
                    ctx.bodyAsClass(Quote.class)
            ));

// PUT /quotes/{id}
            put("/{id}", ctx -> {
                Quote q = ctx.bodyAsClass(Quote.class);
                q.setId(Integer.parseInt(ctx.pathParam("id")));
                quoteController.updateQuote(q);
            });

// DELETE /quotes/{id}
            delete("/{id}", ctx -> quoteController.deleteQuote(
                    Integer.parseInt(ctx.pathParam("id"))
            ));
        };
    }
}
