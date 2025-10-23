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


            get("/", ctx -> {
                String name = ctx.queryParam("name");
                ctx.json(personController.getPersonByName(name));
            });


            post("/", ctx -> personController.addPerson(ctx.bodyAsClass(Person.class)));
        };
    }
}
