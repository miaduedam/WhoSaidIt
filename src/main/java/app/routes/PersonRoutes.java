package app.routes;

import app.controllers.PersonController;
import app.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class PersonRoutes {

    private final PersonController personController = new PersonController();

    public EndpointGroup getRoutes() {
        return () -> {

            // GET /people → all persons
            get("/", personController::readAll, Role.ANYONE);

            // GET /people/{id} → person by ID
            get("/{id}", personController::read, Role.ANYONE);

            // GET /people?name=... → person by name
            get("/search", personController::readByName, Role.ANYONE);

            // POST /people → add person
            post("/", personController::create, Role.ADMIN);

            // PUT /people/{id} → update person
            put("/{id}", personController::update, Role.ADMIN);

            // DELETE /people/{id} → delete person
            delete("/{id}", personController::delete, Role.ADMIN);

        };
    }
}
