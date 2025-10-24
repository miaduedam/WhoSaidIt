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
            get("/", ctx -> ctx.json(personController.getAllPersons()));

// GET /people/{id} → person by ID
            get("/{id}", ctx -> {
                int id = ctx.pathParamAsClass("id", Integer.class).get();
                ctx.json(personController.getPersonById(id));
            });

// GET /people?name=... → person by name
            get("/search", ctx -> {
                String name = ctx.queryParam("name");
                ctx.json(personController.getPersonByName(name));
            });

// POST /people → add person
            post("/", ctx -> personController.addPerson(
                    ctx.bodyAsClass(Person.class)
            ));

// PUT /people/{id} → update person
            put("/{id}", ctx -> {
                Person p = ctx.bodyAsClass(Person.class);
                int id = ctx.pathParamAsClass("id", Integer.class).get();
                p.setId(id);
                personController.updatePerson(p);
            });

// DELETE /people/{id} → delete person
            delete("/{id}", ctx -> {
                int id = ctx.pathParamAsClass("id", Integer.class).get();
                personController.deletePerson(id);
            });

            };
    }
}
