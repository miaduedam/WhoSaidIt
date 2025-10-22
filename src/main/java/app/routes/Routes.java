package app.routes;

import app.controllers.QuoteController;
import app.entities.Quote;
import app.security.controllers.AccessController;
import app.security.controllers.SecurityController;
import app.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.apibuilder.EndpointGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public final class Routes {


    private static final ObjectMapper JSON_MAPPER = new Utils().getObjectMapper();
    private static final SecurityController SECURITY_CONTROLLER = SecurityController.getInstance();
    private static final AccessController ACCESS_CONTROLLER = new AccessController();
    private static final Logger LOGGER = LoggerFactory.getLogger(Routes.class);


    // Instance controller for CRUD operations
    private final QuoteController quoteController;

    public Routes(QuoteController quoteController) {
        this.quoteController = quoteController;
    }

    // Entry point for registering all routes
    public EndpointGroup getRoutes() {
        return () -> {

            get("/", ctx -> ctx.result("Who Said It API is running"));

            // Example: all quote routes grouped under /quotes
            path("quotes", () -> {

                // GET /quotes → list all quotes
                get(ctx -> {
                    try {
                        ctx.json(quoteController.getAllQuotes());
                    } catch (Exception e) {
                        LOGGER.error("Error fetching all quotes", e);
                        ctx.status(500).result("Internal server error");
                    }
                });

                // GET /quotes/{id} → get a specific quote
                get("{id}", ctx -> {
                    try {
                        int id = Integer.parseInt(ctx.pathParam("id"));
                        Quote quote = quoteController.getQuoteById(id);
                        if (quote == null) {
                            ctx.status(404).result("Quote not found");
                        } else {
                            ctx.json(quote);
                        }
                    } catch (Exception e) {
                        LOGGER.error("Error fetching quote by id", e);
                        ctx.status(500).result("Internal server error");
                    }
                });

                // POST /quotes → add new quote
                post(ctx -> {
                    try {
                        Quote quote = ctx.bodyAsClass(Quote.class);
                        quoteController.addQuote(quote);
                        ctx.status(201).result("Quote added successfully");
                    } catch (Exception e) {
                        LOGGER.error("Error adding quote", e);
                        ctx.status(500).result("Internal server error");
                    }
                });

                // PUT /quotes/{id} → update quote
                put("{id}", ctx -> {
                    try {
                        int id = Integer.parseInt(ctx.pathParam("id"));
                        Quote quote = ctx.bodyAsClass(Quote.class);
                        quote.setId(id);
                        quoteController.updateQuote(quote);
                        ctx.result("Quote updated successfully");
                    } catch (Exception e) {
                        LOGGER.error("Error updating quote", e);
                        ctx.status(500).result("Internal server error");
                    }
                });

                // DELETE /quotes/{id} → delete quote
                delete("{id}", ctx -> {
                    try {
                        int id = Integer.parseInt(ctx.pathParam("id"));
                        quoteController.deleteQuote(id);
                        ctx.status(204);
                    } catch (Exception e) {
                        LOGGER.error("Error deleting quote", e);
                        ctx.status(500).result("Internal server error");
                    }
                });
            });

            // Future route groups can be added here, e.g.
            // path("people", ...);
            // path("admin", ...);
        };
    }
}
