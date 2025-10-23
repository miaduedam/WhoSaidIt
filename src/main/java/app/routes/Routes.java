package app.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {

    private final QuoteRoutes quoteRoutes;
    private final PersonRoutes personRoutes;

    public Routes(QuoteRoutes quoteRoutes, PersonRoutes personRoutes) {
        this.quoteRoutes = quoteRoutes;
        this.personRoutes = personRoutes;
    }

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
