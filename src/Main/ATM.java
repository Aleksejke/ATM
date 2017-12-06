package Main;

import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Scanner;


public class ATM {

    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

    private static int checkId (String id, String pw) throws IOException {
        int which = 0;
        String acc = "";
        String pass = "";

        BufferedReader reader = new BufferedReader(new FileReader("Accounts.txt"));
        String line;
        while ((line = reader.readLine()) != null) {
            int option = 1;
            String[] splitted = line.split("[. ]");
            for (String s : splitted) {
                if (option == 1) {
                    which = Integer.parseInt(s);
                    option++;
                } else if(option == 2) {
                    acc = s;
                    option++;
                } else
                    pass = s;
            }
            if (acc.equals(id) && pass.equals(pw))
                break;
        }

        reader.close();
        return which;
    }

    private static void menu() {
        System.out.println("Please Choose From the Following Options: ");
        System.out.println("1. Display Balance");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Logout");
    }

    private static double startingBalance (int option) throws IOException {
        double balancing = 0.0;
        BufferedReader reader = new BufferedReader(new FileReader("Balance.txt"));
        int count = 1;
        String line;
        while ((line = reader.readLine()) != null) {
            if (option == count) {
                balancing = Double.parseDouble(line);
                break;
            }
            else count++;
        }
        reader.close();
        return balancing;
    }

    private static void displayBalance (double balancing) {
        System.out.println(ANSI_RED + balancing + ANSI_RESET);
    }

    private static double deposit (double balancing) {
        double deposit_amount = 0.0;
        boolean answer = false;
        Scanner sc = new Scanner(System.in);
        while (!answer) {
            System.out.println("You can deposit only integer amount!");
            System.out.println("Please enter the amount: ");
            deposit_amount = sc.nextDouble();
            if ((deposit_amount % 5 == 0) || (deposit_amount % 10 == 0)) {
                balancing +=  deposit_amount;
                answer = true;
            }
            else System.out.println("You entered the wrong amount!");
        }
        return balancing;
    }

    private static double withdraw (double balancing) {
        double amount = 0.0;
        boolean answer = false;
        Scanner sc = new Scanner(System.in);
        while (!answer) {
            System.out.println("You can withdraw only integer amount!");
            System.out.println("Please enter the amount: ");
            amount = sc.nextDouble();
            if (((amount % 10 == 0) || (amount % 5 == 0)) && (amount <= balancing))
                answer = true;
            else System.out.println("There is no possibility to withdraw such amount");
        }
        return amount;
    }

    public static void main(String[] args) throws IOException {

        String account, password;
        int checkTime = 0;
        int answer = 0;
        double balance;
        int choice = 0;

        while (checkTime != 3) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int count = 0;

            System.out.println("Please enter your Account number: ");
            account = br.readLine();
            if (account.length() == 4)
                count++;

            System.out.println("Please enter your Password: ");
            password = br.readLine();
            if (password.length() == 4)
                count++;

            if (count == 2)
                answer = checkId(account, password);

            if (answer == 0) {
                System.out.println("You have entered wrong data.");
                checkTime++;
                if (checkTime == 3) {
                    System.out.println("Maximum Login Attempts Reached!");
                    System.exit(0);
                }
            } else {
                balance = startingBalance(answer);
                while (choice != 4) {
                    menu();
                    choice = Integer.parseInt(br.readLine());
                    if ((choice <= 0) || (choice > 4))
                        System.out.println("Wrong choice!");
                    switch (choice) {
                        case 1:
                            System.out.println("Your balance:");
                            displayBalance(balance);
                            break;
                        case 2:
                            balance = deposit(balance);
                            break;
                        case 3:
                            balance = balance - withdraw(balance);
                            NumberFormat formatter = new DecimalFormat("#0.00");
                            balance = Double.valueOf(formatter.format(balance));
                    }
                }

                if (choice == 4) {
                    System.out.println("Thank You for Using ATM. Have a Nice Day. Good-Bye!");
                    System.exit(0);
                }
            }
        }
    }
}
