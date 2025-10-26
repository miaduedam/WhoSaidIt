package app.routes;

import app.controllers.PersonController;
import app.entities.Person;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class PersonRoutes {

    private final PersonController personController = new PersonController();

    public EndpointGroup getRoutes() {
        return () -> {


// GET /people → all persons
            get("/", personController::readAll);

// GET /people/{id} → person by ID
            get("/{id}", personController::read);

// GET /people?name=... → person by name
            get("/search", personController::readByName);

// POST /people → add person
            post("/", personController::create);

// PUT /people/{id} → update person
            put("/{id}", personController::update);

// DELETE /people/{id} → delete person
            delete("/{id}", personController::delete);

        };
    }
}
