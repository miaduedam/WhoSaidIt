package app.routes;

import app.controllers.QuoteController;
import app.entities.Quote;
import app.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class QuoteRoutes {

    private final QuoteController quoteController = new QuoteController();

    public EndpointGroup getRoutes() {
        return () -> {

            // GET /quotes
            get("/", quoteController::readAll, Role.ADMIN);

            // GET /quotes/{id}
            get("/{id}", quoteController::read);

            // POST /quotes
            post("/", quoteController::create);

            // PUT /quotes/{id}
            put("/{id}", quoteController::update);

            // DELETE /quotes/{id}
            delete("/{id}", quoteController::delete);

            // GET / quotes/
            get("/person/{name}", quoteController::readQuotesByPerson);



        };
    }
}
