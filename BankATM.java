import java.util.*;
import java.util.Arrays;

class BankAccount {
   private String accountID = "";
   private double balance = 0.0;
   
   BankAccount() { }
   
   BankAccount(String accountID) { this.accountID = accountID; }
   
   BankAccount(String accountID, double balance) {
      this (accountID);
      this.balance = balance;
   }
   
   public String getAccountID() {
      return accountID;
   }
   
   public double getBalance() {
      return balance;
   }
   
   public String setAccountID(String accountID) {
      return this.accountID = accountID;
   }
   
   public double setBalance(double balance) {
      return this.balance = balance;
   }
   
   public void closeAccount() {
      this.accountID = "";
      this.balance = 0;
   }
   
   public String displayAccounts() {
      if(!(this.accountID.equals(null) || this.accountID.equals(""))) {
         return "Account # "+accountID.substring(0,3)+"-"+accountID.substring(3,6)+
                " Balance: $"+balance+"0";
      } else {
         return "\n";
      }      
   }
   
   public double deposit(double deposit) {
      return this.balance = deposit + balance;
   }
   
   public double withdraw(double withdraw) {
      return this.balance = balance - withdraw;
   }
}

class Bank {

   public static Scanner input = new Scanner(System.in);
   public static BankAccount[] accounts = new BankAccount[1]; //intialize accounts array to size of 1
   public static int count = 0;
   public static String currentAccount = "NONE";
   
   public static void main(String args[]) {
      startDisplay(input);
   }
   //start menu
   public static void startDisplay(Scanner input) {
      String user;
      do {
         System.out.println();
         System.out.print("O: Open Account\n"+
                          "D: Deposit\n"+
                          "S: Select Account\n"+
                          "C: Close Account\n"+
                          "W: Withdraw\n"+
                          "L: List All Accounts\n"+
                          "Q: Quit\n"+
                          "\n");
         System.out.println("Number of Accounts: "+count);
         System.out.println("Current Account Selected: "+currentAccount);
         
         System.out.print("Enter Command: ");
         user = input.next();
         System.out.println();
         user = checkInput(user);
      } while (user != "Q");
   }
   //user choice
   public static String checkInput(String user) {
      user = user.toUpperCase();
      switch (user) {
         case "O": user = "O";
                   openAccount();
         break;
         case "D": user = "D";
                   deposit();
         break;
         case "S": user = "S";
                   selectAccount();
         break;
         case "C": user = "C";
                   closeAccount();
         break;
         case "W": user = "W";
                   withdraw();
         break;
         case "L": user = "L";
                   listAccounts();
         break;
         case "Q": user = "Q";
         break;
         default: user = "";
                  System.out.println("Enter a valid selection.");
         break;
      }
      return user;
   }
   //create new account: check for dups, store in array
   public static void openAccount() {
      boolean dup = true;
      int account;
      String accountID;
      double balance;
      //validate user input
      do {
         do {
            System.out.print("Enter a new, 6 digit account number: ");
            while (!input.hasNextInt()) {
               System.out.print("Must be a 6 digit number: ");
               input.next();
            }
            account = input.nextInt();
         } while ((String.valueOf(account).length()) != 6);
         accountID = Integer.toString(account);
         dup = checkDup(accountID);
      } while (!dup);
      //validate user input
      do {
         System.out.print("Enter initial balance for account: ");
         while (!input.hasNextDouble()) {
            System.out.print("Must be a positive, real number: ");
            input.next();
         }
         balance = input.nextDouble();
      } while (balance <= 0);
      
      BankAccount a = new BankAccount(accountID, balance);
      storeAccount(a);
      currentAccount = a.displayAccounts();
      count++;
   }
   // check for duplicate account numbers
   public static boolean checkDup(String accountID) {
      for(int i = 0; i < count; i++) {
         if(accounts[i].getAccountID().equals(accountID)) {
            return false;
         }
      }
      return true;
   }
   //move new accounts into the next incremation in the array
   public static void storeAccount(BankAccount a) {
      BankAccount[] tempAccounts = new BankAccount[accounts.length];
      //if the array accounts is too small, store contents in temp and multiply size by 2
      while ( tempAccounts.length <= count) {
         tempAccounts = new BankAccount[accounts.length * 2];
      }
      System.arraycopy(accounts,0,tempAccounts,0,accounts.length);
      accounts = new BankAccount[tempAccounts.length];
      System.arraycopy(tempAccounts,0,accounts,0,tempAccounts.length);
      accounts[count] = a;
   }
   //select account, add money to account, verify for non-numbers and negatives
   public static void deposit() {
      double deposit;
      System.out.print("Please select an account: ");
      String accountID = input.next();
      //validate user input for non-numbers & negatives
      for(int i = 0; i < count; i++) {
         if(accounts[i].getAccountID().equals(accountID)) {
            do {
               System.out.print("Enter amount of Deposit: ");
               while (!input.hasNextDouble()) {
                  System.out.print("Enter amount of Deposit: ");
                  input.next();
               }
               deposit = input.nextDouble();
            } while (deposit <= 0);
           
            accounts[i].deposit(deposit);
            System.out.println("New Balance: "+accounts[i].getBalance());
            currentAccount = accounts[i].displayAccounts();
         } 
      } 
   } 
   //select current account to display
   public static String selectAccount() {
      System.out.print("Enter an account number: ");
      String accountID = input.next();
      for(int i = 0; i < count; i++) {
         if(accounts[i].getAccountID().equals(accountID)) {
            currentAccount = accounts[i].displayAccounts();
         } else {
            currentAccount = "NONE";
         }
      }
      return currentAccount;
            
   }
   //select account and close, move account at last index to closed account index
   public static void closeAccount() {

      System.out.print("Enter an account number to close: ");
      BankAccount tempBank;
      String accountID = input.next();
      for(int i = 0; i < count; i++) {
         if(accounts[i].getAccountID().equals(accountID)) {
            accounts[i].closeAccount();
            tempBank = new BankAccount((accounts[count-1].getAccountID()),(accounts[count-1].getBalance()));
            accounts[i] = new BankAccount(tempBank.getAccountID(),tempBank.getBalance());
            accounts[count-1].closeAccount();
            count--;
            currentAccount = "NONE";
            break; 
         }
      }
   }

   //select account and take out money, stop if withdraw exceeds 1.00 remaining in account
   public static void withdraw() {
      System.out.print("Please select an account: ");
      String accountID = input.next();
      double withdraw;
      //validate user input for non-numbers & negatives
      for(int i = 0; i < count; i++) {
         if(accounts[i].getAccountID().equals(accountID)) {
            do {
               System.out.print("Enter amount to Withdraw: ");
               while (!input.hasNextDouble()) {
                  System.out.print("Enter amount to Withdraw: ");
                  input.next();
               }
            withdraw = input.nextDouble();
            } while (withdraw <= 0);
            
            if ((accounts[i].getBalance() - withdraw) < 1.00) {
               System.out.println("Not enough in account #"+accounts[i].getAccountID()+" to withdraw $"+withdraw);
            } else {
               accounts[i].withdraw(withdraw);
               System.out.println("New Balance: "+accounts[i].getBalance());
            }
         } 
      }
      
   }
   //list all accounts
   public static void listAccounts() {
      int num = 1;
      if(accounts[0] == null) {
         System.out.println("No Accounts to Display.");
      } else {
         for(int i = 0; i < count; i++) {
            System.out.println(num+") "+accounts[i].displayAccounts());
            num++;
         }
      }
   }
}
