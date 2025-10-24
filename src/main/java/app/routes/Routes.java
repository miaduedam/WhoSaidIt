package app.routes;

import app.controllers.QuoteController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {

    private final QuoteRoutes quoteRoutes = new QuoteRoutes();
    private final PersonRoutes personRoutes = new PersonRoutes();

    public EndpointGroup getRoutes() {
        return () -> {

            // Root endpoint for API health check
            get("/", ctx -> ctx.result("Who Said It API is running"));

            // Group quote endpoints under /quotes
            path("quotes", quoteRoutes.getRoutes());

            // Group person endpoints under /people
            path("people", personRoutes.getRoutes());
        };
    }
}
