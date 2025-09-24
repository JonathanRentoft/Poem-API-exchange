package app;

import app.config.HibernateConfig;
import app.controller.PoemController;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;

public class Main {
    public static void main(String[] args) {
        // Initialiser Hibernate
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        // Opret Javalin app
        Javalin app = Javalin.create(config -> {
            config.http.defaultContentType = "application/json";
        }).start(7071);

        // Opret controller instance
        PoemController poemController = new PoemController();

        // Definer API routes direkte (den mest robuste metode)
        app.get("/api/poem", poemController::getAllPoems);
        app.get("/api/poem/{id}", poemController::getPoemById);
        app.post("/api/poem", poemController::createPoem);
        app.put("/api/poem/{id}", poemController::updatePoem);
        app.delete("/api/poem/{id}", poemController::deletePoem);
        app.get("/api/poems/friends", poemController::getCombinedPoems);
    }
}

