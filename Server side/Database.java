import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/* Handles databse connecion and request. */
public class Database {
    public Connection connection;

    /* Establishes connections with MYSQL databse. */
    public Database(){
        try{
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/app_schema", "root", "mypassword");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /* Gets owner ID corresponding to username. */
    public int getOwnerID(String username){
        try{
            Statement statement = connection.createStatement();
            System.out.println("username is: " + username);
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users WHERE username = '" + username + "'");

            if(resultSet.next()){
                return resultSet.getInt("userID");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return -1;
    }

    /* Gets owner username corresponding to ID. */
    public String getOwnerUsername(int userID){
        try{
            Statement statement = connection.createStatement();
            System.out.println("userID is: " + userID);
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users WHERE userid = " + userID);

            if(resultSet.next()){
                return resultSet.getString("username");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return "no username found";
    }

    /* Gets owner email corresponding to user ID. */
    public String getOwnerEmail(int userID){
        try{
            Statement statement = connection.createStatement();
            System.out.println("userID is: " + userID);
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users WHERE userid = " + userID);

            if(resultSet.next()){
                return resultSet.getString("email");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return "no email found";
    }

    /* Gets a random animal from the database. */
    public Animal getRandomAnimal() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM animals ORDER BY RAND() LIMIT 1");

            if (resultSet.next()) {
                int animalId = resultSet.getInt("animalid");
                String name = resultSet.getString("name");
                String type = resultSet.getString("type");
                String breed = resultSet.getString("breed");
                String sex = resultSet.getString("sex");
                String age = resultSet.getString("age");
                String image = resultSet.getString("image");
                int ownerId = resultSet.getInt("ownerid");
                String animalDesc = resultSet.getString("animaldesc");

                return new Animal(name, type, breed, age, sex, image, animalDesc, getOwnerUsername(ownerId), getOwnerEmail(ownerId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /* Gets animal corresponding to given attributes. */
    public Animal getAnimalByAttributes(String age, String breed, String sex, String type) {
        Animal animal = null;
        try {
            String query = "SELECT * FROM animals WHERE " +
                    "(? IS NULL OR age = ?) AND " +
                    "(? IS NULL OR breed = ?) AND " +
                    "(? IS NULL OR sex = ?) AND " +
                    "(? IS NULL OR type = ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, age);
            stmt.setString(2, age);
            stmt.setString(3, breed);
            stmt.setString(4, breed);
            stmt.setString(5, sex);
            stmt.setString(6, sex);
            stmt.setString(7, type);
            stmt.setString(8, type);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Assuming the order of columns in your table
                animal = new Animal(
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("breed"),
                        rs.getString("age"),
                        rs.getString("sex"),
                        rs.getString("image"),
                        rs.getString("animaldesc"),
                        null, // username not needed
                        null  // email not needed
                );
                System.out.println(animal.toString());
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return animal;
    }

    /* Adds animal to databse. */
    public void addAnimalToDatabase(String animalName, String animalType, String animalBreed, String animalSex, String animalAge, String animalImage, int ownerID, String description){
        try{
            Statement statement = connection.createStatement();
            int id = countAnimalsInDatabase() + 1;
            PreparedStatement ps = connection.prepareStatement("INSERT INTO animals (animalid, name, type, breed, sex, age, image, ownerid, animaldesc) VALUES (?,?,?,?,?,?,?,?,?)");
            ps.setInt(1, id);
            ps.setString(2, animalName);
            ps.setString(3, animalType);
            ps.setString(4, animalBreed);
            ps.setString(5, animalSex);
            ps.setString(6, animalAge);
            ps.setString(7, animalImage);
            ps.setInt(8, ownerID);
            ps.setString(9, description);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* Counts the number of current animals in database. */
    public int countAnimalsInDatabase(){
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM animals");
            int counter = 0;
            while(resultSet.next()){
                counter++;
            }
            return counter;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /* Checks if a user with given username and password exists. */
    public Boolean userExists(String username, String password){
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users WHERE username = '" + username + "'" + " AND password = '" + password + "'");

            if (resultSet.next()) {
                int userId = resultSet.getInt("userId");
                return true;
            } else {
                System.out.println("No user found with the given username and password.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    /* Checks if this username or email already exists. */
    public boolean userOrEmailExists(String username, String email) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ? OR email = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, email);

            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            resultSet.close();
            stmt.close();

            return count > 0; // Return true if any matching user is found

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* Inserts user to database. */
    public boolean insertUser(String username, String password, String email) {
        if (userOrEmailExists(username, email)) {
            System.out.println("Username or Email already exists.");
            return false; // Early exit if user or email already exists
        }

        String query = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, email);

            int rowsInserted = stmt.executeUpdate();
            stmt.close();

            return rowsInserted > 0; // Return true if the insertion was successful

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
