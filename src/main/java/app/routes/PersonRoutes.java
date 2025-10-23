package app.routes;

import app.controllers.PersonController;
import app.entities.Person;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class PersonRoutes {

    private final PersonController personController;

    public PersonRoutes(PersonController personController) {
        this.personController = personController;
    }

    public EndpointGroup getRoutes() {
        return () -> {

            // GET /people → all persons
            get("/", ctx -> ctx.json(personController.getAllPersons()));

            // GET /people/:id → person by ID
            get(":id", ctx -> ctx.json(
                    personController.getPersonById(Integer.parseInt(ctx.pathParam("id")))
            ));

            // GET /people?name=... → person by name
            get("/search", ctx -> {
                String name = ctx.queryParam("name");
                ctx.json(personController.getPersonByName(name));
            });

            // POST /people → add person
            post("/", ctx -> personController.addPerson(
                    ctx.bodyAsClass(Person.class)
            ));

            // PUT /people/:id → update person
            put(":id", ctx -> {
                Person p = ctx.bodyAsClass(Person.class);
                p.setId(Integer.parseInt(ctx.pathParam("id")));
                personController.updatePerson(p);
            });

            // DELETE /people/:id → delete person
            delete(":id", ctx -> personController.deletePerson(
                    Integer.parseInt(ctx.pathParam("id"))
            ));
        };
    }
}
