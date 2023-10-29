import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        
        User user = User.getInstance();
        News news = News.getInstance();

        System.out.println("\n" +
                "                   ___    ____________  __   _   _________       _______\n" +
                "                  /   |  /  _/_  __/ / / /  / | / / ____/ |     / / ___/\n" +
                "                 / /| |  / /  / / / / / /  /  |/ / __/  | | /| / /\\__ \\ \n" +
                "                / ___ |_/ /  / / / /_/ /  / /|  / /___  | |/ |/ /___/ / \n" +
                "               /_/  |_/___/ /_/  \\____/  /_/ |_/_____/  |__/|__//____/  \n" +
                "                                                                        \n\n");

        while (true) {
            System.out.println("1.Sign in");
            System.out.println("2.Log in");
            System.out.println("3.Exit");

            int command = input.nextInt();

            switch (command) {
                case 1 -> user.UserRegister();
                case 2 -> {
                    user.UserLogin();
                    label:
                    while (true) {
                        if (user.loggedIn) {
                            System.out.println("\n1. To add news");
                            System.out.println("2. To delete news");
                            System.out.println("3. To update news");
                            System.out.println("4. Show news");
                            System.out.println("5. Update profile");
                            System.out.println("6. Export news to txt");
                            System.out.println("7. Exit");
                            int x = input.nextInt();
                            switch (x) {
                                case 1:
                                    news.NewsUpload();
                                    break;
                                case 2:
                                    news.NewsDelete();
                                    break;
                                case 3:
                                    news.NewsUpdate();
                                    break;
                                case 4:
                                    System.out.println("1.Show all news");
                                    System.out.println("2.Show news by id");
                                    System.out.println("3.Show news by category");
                                    System.out.println("4.Exit");
                                    int c = input.nextInt();
                                    if (c == 1) {
                                        news.NewsRead();
                                        break;
                                    } else if (c == 2) {
                                        news.NewsView();
                                        break;
                                    } else if (c == 3) {
                                        news.NewsCategory();
                                        break;
                                    } else if (c == 4) {
                                        break;
                                    }
                                    break;
                                case 5:
                                    user.UserUpdate();
                                    break;
                                case 6:
                                    news.NewsToTxt();
                                    break;
                                case 7:
                                    break label;
                            }
                        }
                    }
                }
                case 3 -> System.exit(0);
            }
        }
    }
}

