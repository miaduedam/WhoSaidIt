package app.controllers;

import io.javalin.http.Context;

public interface IController {

    void read(Context ctx);
    void readAll(Context ctx);
    void create(Context ctx);
    void update(Context ctx);
    void delete(Context ctx);
}
