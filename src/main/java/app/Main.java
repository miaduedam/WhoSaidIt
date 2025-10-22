package app;
import app.config.ApplicationConfig;

public class Main {
    public static void main(String[] args) throws Exception {

        ApplicationConfig.startServer(7070);










//        String apiKey = System.getenv("API_NINJAS_KEY");
//        if (apiKey == null) {
//            throw new IllegalStateException("Set API_NINJAS_KEY environment variable!");
//        }
//
//        String password = System.getenv("db_password");
//        String dbHost = System.getenv("db_host");
//        String dbPort = "5432";
//        String dbName = "sp2";


//        try (Connection connection = DriverManager.getConnection(
//                "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName,
//                "postgres",
//                password)) {
//
//            PersonDAO personDAO = new PersonDAO(connection);
//            QuoteDAO quoteDAO = new QuoteDAO(connection);
//            QuoteService quoteService = new QuoteService(apiKey);
//            QuoteController controller = new QuoteController(personDAO, quoteDAO, quoteService);
//
//            // Fetch 100 quotes and save to DB
//            controller.fetchAndSaveQuotes(5);
//
//        } catch (SQLException e) {
//            System.err.println("Database error: " + e.getMessage());
//            e.printStackTrace();
//        } catch (Exception e) {
//            System.err.println("Unexpected error: " + e.getMessage());
//            e.printStackTrace();
//        }

    }
}
