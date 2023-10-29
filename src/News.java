import java.sql.*;
import java.util.Scanner;
import java.io.*;

public class News extends AbstractNews implements logSQL {
    Scanner input = new Scanner(System.in);

    private static News instance;

    private News() {
    }

    public static News getInstance() {
        if (instance == null) {
            instance = new News();
        }
        return instance;
    }

    public void NewsUpload() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "INSERT INTO news(heading,description,author,category)" + " VALUES(?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            while (true) {

                System.out.println("Input author: ");
                super.setAuthor(input.nextLine());

                System.out.println("Input heading: ");
                super.setHeading(input.nextLine());

                System.out.println("Choose category:");
                System.out.println("1. Volunteering");
                System.out.println("2. Advertisement");
                System.out.println("3. Concert");
                System.out.println("4. Administration Announcements");
                String categoryString = input.nextLine();
                switch (categoryString) {
                    case "1":
                        super.setCategory(1);
                        break;
                    case "2":
                        super.setCategory(2);
                        break;
                    case "3":
                        super.setCategory(3);
                        break;
                    case "4":
                        super.setCategory(4);
                        break;
                    default:
                        System.out.println("Invalid input for category");
                        continue;  // Go back to the beginning of the loop
                }

                System.out.println("Input description: ");
                super.setDescription(input.nextLine());

                if (super.getHeading().length() >= 5 && super.getHeading().length() <= 150 &&
                        super.getDescription().length() >= 5 && super.getDescription().length() <= 500 &&
                        super.getCategory() >= 1 && super.getCategory() <= 4) {
                    break;
                } else {
                    if (super.getHeading().length() < 5 || super.getHeading().length() > 150) {
                        System.out.println("Heading should be from 5 to 150 characters");
                    }
                    if (super.getDescription().length() < 5 || super.getDescription().length() > 500) {
                        System.out.println("Description should be from 5 to 500 characters");
                    }
                    if (super.getCategory() < 1 || super.getCategory() > 4) {
                        System.out.println("Invalid input for category");
                    }
                }
            }

            statement.setString(1, super.getHeading());
            statement.setString(2, super.getDescription());
            statement.setString(3, super.getAuthor());
            if (getCategory() == 1) {
                statement.setString(4, "Volunteering");
            } else if (getCategory() == 2) {
                statement.setString(4, "Advertisement");
            } else if (getCategory() == 3) {
                statement.setString(4, "Concert");
            } else if (getCategory() == 4) {
                statement.setString(4, "Administration Announcements");
            }

            int rows = statement.executeUpdate();
            if (rows > 0) {
                System.out.println("News has been inserted");
            }

        } catch (SQLException e) {
            System.out.println("Error in connecting to postgresql server");
            e.printStackTrace();
        }
    }


    public void NewsUpdate() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {

            String sql = "UPDATE news SET heading=?, description=? WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(sql);

            System.out.println("Input id: ");
            int id = input.nextInt();
            input.nextLine();
            System.out.println("Input new heading: ");
            super.setHeading(input.nextLine());
            System.out.println("Input new description: ");
            super.setDescription(input.nextLine());


            statement.setString(1, super.getHeading());
            statement.setString(2, super.getDescription());
            statement.setInt(3, id);

            int rows = statement.executeUpdate();
            if (rows > 0) {
                System.out.println("News has been updated");
            }

        } catch (SQLException e) {
            System.out.println("Error in connecting to postgresql server");
            e.printStackTrace();
        }
    }


    public void NewsDelete() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "SELECT * FROM news ORDER BY id ASC";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            while (result.next()) {
                System.out.println(result.getInt("id") + " " + result.getString("heading"));
            }

            System.out.println("Input id ");
            String news_id = input.next();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM news WHERE id = '" + news_id + "';");
            preparedStatement.executeUpdate();
        } catch (
                SQLException e) {
            System.out.println("Error in connecting to postgresql server");
            e.printStackTrace();
        }
    }

    public void NewsRead() {      //Show all news
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "SELECT * FROM news ORDER BY id ASC";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            while (result.next()) {
                System.out.println("-------------------------\n" + result.getInt("id") + ") " + result.getString("heading") + " | Category: " + result.getString("category") + " | Author: " + result.getString("author"));
            }
            System.out.println("-------------------------");
        } catch (
                SQLException e) {
            System.out.println("Error in connecting to postgresql server");
            e.printStackTrace();
        }
    }

    public void NewsView() {   //Show by id
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            Statement statement = connection.createStatement();
            System.out.println("Input id: ");
            int id = input.nextInt();
            String sql = "SELECT * FROM news WHERE id='" + id + "'";
            ResultSet result = statement.executeQuery(sql);

            while (result.next()) {
                System.out.println("-------------------------\n" + result.getString("heading") + "\n" + "Author: " + result.getString("author") + "\n" + "Category: " + result.getString("category") +"\n"+ "Description:\n" + result.getString("description") + "\n-------------------------\n");
            }
        } catch (
                SQLException e) {
            System.out.println("Error in connecting to postgresql server");
            e.printStackTrace();
        }
    }

    public void NewsCategory() {  //Show by category
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            Statement statement = connection.createStatement();

            System.out.println("Choose category: \n" +
                    "1.Volunteering\n" +
                    "2.Advertisement\n" +
                    "3.Concert\n" +
                    "4.Administration Announcements");

            String cat = input.next();

            switch (cat) {
                case "1":
                    setStrCategory("Volunteering");
                    break;
                case "2":
                    setStrCategory("Advertisement");
                    break;
                case "3":
                    setStrCategory("Concert");
                    break;
                case "4":
                    setStrCategory("Administration Announcements");
                    break;
                default:
                    System.out.println("Invalid input for category");
            }

            String sql = "SELECT * FROM news WHERE category='" + getStrCategory() + "'";
            ResultSet result = statement.executeQuery(sql);

            while (result.next()) {
                System.out.println("-------------------------\n" + result.getString("heading") + "\n" + "Author: " + result.getString("author") + "\nCategory: " + result.getString("category") + "\n" + "Description:\n" + result.getString("description") + "\n-------------------------");
            }
        } catch (
                SQLException e) {
            System.out.println("Error in connecting to postgresql server");
            e.printStackTrace();
        }
    }

    public void NewsToTxt() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "SELECT * FROM news";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            BufferedWriter writer = new BufferedWriter(new FileWriter("news.txt"));

            while (result.next()) {
                writer.write("ID: " + result.getInt("id"));
                writer.newLine();
                writer.write("Heading: " + result.getString("heading"));
                writer.newLine();
                writer.write("Description: " + result.getString("description"));
                writer.newLine();
                writer.write("Author: " + result.getString("author"));
                writer.newLine();
                writer.write("Category: " + result.getString("category"));
                writer.newLine();
                writer.newLine();
            }

            writer.close();

            System.out.println("News has been exported to news.txt");

        } catch (SQLException e) {
            System.out.println("Error in connecting to postgresql server");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error in writing to file news.txt");
            e.printStackTrace();
        }
    }
}

