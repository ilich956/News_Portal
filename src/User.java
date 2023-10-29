import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class User extends Person implements logSQL, UserI {
    boolean loggedIn = false;
    int userID;

    Scanner input = new Scanner(System.in);

    private static User instance;

    private User() {
    }

    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }

    @Override
    public void UserRegister() {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            String sql = "INSERT INTO public.user(name,login,password)" + " VALUES(?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            while (true) {
                System.out.println("Input your name: ");
                super.setName(input.next());
                System.out.println("Input your login: ");
                super.setLogin(input.next());
                System.out.println("Input your password: ");
                super.setPassword(input.next());
                if (super.getLogin().length() >= 5 && super.getPassword().length() >= 5) {
                    break;
                } else if (super.getLogin().length() < 5 || super.getLogin().length() > 51) {
                    System.out.println("Login should be from 5 to 50 characters");
                } else if (super.getPassword().length() < 5 || super.getPassword().length() > 51) {
                    System.out.println("Password should be from 5 to 50 characters");
                }
            }
            super.hashPassword();
            statement.setString(1, super.getName());
            statement.setString(2, super.getLogin());
            statement.setString(3, super.getPassword());

            int rows = statement.executeUpdate();
            if (rows > 0) {
                System.out.println("Registration complete");
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error in connecting to postgresql server");
            e.printStackTrace();
        }
    }

    @Override
    public void UserLogin() {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            String sql = "SELECT * FROM public.user";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            ArrayList<String> passwords = new ArrayList<>();
            ArrayList<String> logins = new ArrayList<>();
            ArrayList<Integer> ID = new ArrayList<>();
            while (result.next()) {
                ID.add(result.getInt("id"));
                logins.add(result.getString("login"));
                passwords.add(result.getString("password"));
            }

            outerloop:
            while (true) {
                System.out.println("Enter your login");
                String logLogin = input.next();
                System.out.println("Enter your password");
                String logPassword = hashPassword(input.next());
                int i = 0;
                while (i < logins.size()) {
                    if (logLogin.equals(logins.get(i)) && logPassword.equals(passwords.get(i))) {
                        userID = ID.get(i);
                        System.out.println("Logged in succesfully!\n");

                        loggedIn = true;
                        break outerloop;
                    }
                    i++;
                }
                if (!loggedIn) {
                    System.out.println("Invalid login or password");
                }

            }
        } catch (
                SQLException e) {
            System.out.println("Error in connecting to postgresql server");
            e.printStackTrace();
        }

    }

    @Override
    public void UserUpdate() {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            String sql = "UPDATE public.user SET name=?, login=? WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(sql);

            System.out.println("Input new name: ");
            super.setName(input.next());
            System.out.println("Input new login: ");
            super.setLogin(input.next());

            statement.setString(1, super.getName());
            statement.setString(2, super.getLogin());
            statement.setInt(3, userID);

            int rows = statement.executeUpdate();
            if (rows > 0) {
                System.out.println("Profile has been updated");
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error in connecting to postgresql server");
            e.printStackTrace();
        }
    }
}



