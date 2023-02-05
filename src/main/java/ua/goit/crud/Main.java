package ua.goit.crud;

import org.flywaydb.core.Flyway;
import ua.goit.crud.model.Client;
import ua.goit.crud.service.ClientService;

import java.util.List;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws Exception {

        Properties properties = new Properties();
        properties.load(Database.class.getClassLoader().getResourceAsStream("application.properties"));
        String url = properties.getProperty("url");
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");

        Flyway flyway = Flyway
                .configure()
                .dataSource(url,user,password)
                .locations("db/migration")
                .load();

        flyway.migrate();

        ClientService clientService = new ClientService();
        long id = clientService.create("Dmytro");
        System.out.println("\u001B[33m"+"A new client is created with id: " + id + "\u001B[34m");
        String name = clientService.getById(id);
        System.out.println("Сlient name with id: " + name + "\u001B[31m");
        clientService.setName(id,"LDV");
        System.out.println("New name with id: " + id + "\u001B[32m");
        clientService.deleteById(id);
        System.out.println("Client deleted" + id + "\u001B[33m");
        List<Client> clients = clientService.listAll();
        System.out.println(clients + "\u001B[0m");

    }
}
