import java.io.*;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/* Handles connection with the clients. */
public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private Database database;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        database = new Database();
    }

    /* Handles client requests. */
    @Override
    public void run() {
        try (DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream())) {

            while (clientSocket.isConnected()) {
                try {
                    String messageFromClient = inputStream.readUTF();
                    String[] requestDissection = messageFromClient.split("\\|");
                    System.out.println("Message from client: " + messageFromClient);

                    /* Handles login request. */
                    if (requestDissection[0].equals("LoginRequest")) {
                        String username = requestDissection[1];
                        String password = requestDissection[2];

                        System.out.println("Login request from client: " + username);

                        if (database.userExists(username, password)) {
                            outputStream.writeUTF("login successful");
                            outputStream.flush();
                        } else {
                            outputStream.writeUTF("login failed");
                            outputStream.flush();
                        }
                    }

                    /* Handles animal addition requests. */
                    if (requestDissection[0].equals("addAnimalToDatabase")) {
                        String username = requestDissection[1], animalName = requestDissection[2], animalType = requestDissection[3],
                        animalAge = requestDissection[4], animalBreed = requestDissection[5], animalSex = requestDissection[6], animalImage = requestDissection[7],
                        description = requestDissection[8];
                        int ownerID = database.getOwnerID(username);

                        database.addAnimalToDatabase(animalName, animalType, animalBreed, animalSex, animalAge, animalImage, ownerID, description);
                    }

                    /* Handles get-random-animal requests. */
                    if (requestDissection[0].equals("getRandomAnimal")) {
                        Animal randomAnimal = database.getRandomAnimal();
                        if (randomAnimal != null) {
                            outputStream.writeUTF(randomAnimal.toString()); // Serialize the animal to string format
                            outputStream.flush();
                        } else {
                            outputStream.writeUTF("No animals found");
                            outputStream.flush();
                        }
                    }

                    /* Handles get-animal-by-attributes requests. */
                    if (requestDissection[0].equals("getAnimalByAttributes")) {
                        String age = requestDissection[1].isEmpty() ? null : requestDissection[1];
                        String breed = requestDissection[2].isEmpty() ? null : requestDissection[2];
                        String sex = requestDissection[3].isEmpty() ? null : requestDissection[3];
                        String type = requestDissection[4].isEmpty() ? null : requestDissection[4];

                        Animal animal = database.getAnimalByAttributes(age, breed, sex, type);
                        if (animal != null) {
                            System.out.println("message to client: " + animal.toString());
                            outputStream.writeUTF(animal.toString()); // Serialize the animal object into a string
                        } else {
                            outputStream.writeUTF("null");
                        }
                    }

                    /* Handles signup requests. */
                    if(requestDissection[0].equals("SignUp")) {
                        String username = requestDissection[1], password = requestDissection[2], email = requestDissection[3];
                        System.out.println("SignUp request from client: " + username + " " + password + " " + email);
                        boolean insertionSuccessful = database.insertUser(username, password, email);
                        if(!insertionSuccessful){
                            outputStream.writeUTF("user already exists");
                            outputStream.flush();
                        }
                    }

                } catch (IOException e) {
                    System.out.println("Connection lost with client.");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (!clientSocket.isClosed()) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* Returns a string describing an animal. */
    private String serializeAnimal(Animal animal) {
        return animal.getName() + "|" + animal.getType() + "|" + animal.getBreed() + "|" +
                animal.getAge() + "|" + animal.getSex() + "|" + animal.getImage() + "|" +
                animal.getDescription() + "|" + animal.getUsername() + "|" + animal.getEmail();
    }
}
