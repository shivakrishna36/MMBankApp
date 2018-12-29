package com.moneymoney.account.ui;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.moneymoney.account.CurrentAccount;
import com.moneymoney.account.SavingsAccount;
import com.moneymoney.account.service.CurrentAccountService;
import com.moneymoney.account.service.CurrentAccountServiceImpl;
import com.moneymoney.account.util.DBUtil;
import com.moneymoney.exception.AccountNotFoundException;

public class CurrentAccountCUI
{
	private static Scanner scanner = new Scanner(System.in);
	private static CurrentAccountService currentAccountService = new CurrentAccountServiceImpl();



	public static void startCurrentAccount() {

		do {
			System.out.println("****** Welcome to Money Money Bank********");
			System.out.println("1. Open New Current Account");
			System.out.println("2. Update Account");
			System.out.println("3. Close Account");
			System.out.println("4. Search Account");
			System.out.println("5. Withdraw");
			System.out.println("6. Deposit");
			System.out.println("7. FundTransfer");
			System.out.println("8. Check Current Balance");
			System.out.println("9. Get All Current Account Details");
			System.out.println("10. Sort Accounts");
			System.out.println("11. Exit");
			System.out.println();
			System.out.println("Make your choice: ");

			int choice = scanner.nextInt();

			performCurrentOperation(choice);

		} while (true);
	}

	
	private static void performCurrentOperation(int choice) {
		switch (choice) {
		case 1:
			acceptInput("CA");
			break;
		case 2:
			updateAccount();
			break;
		case 3:
			deleteAccount();
			break;
		case 4:
			searchAccount();
			break;
		case 5:
			withdraw();
			break;
		case 6:
			deposit();
			break;
		case 7:
			fundTransfer();
			break;
		case 8:
			checkCurrentBalance();
			break;
		case 9:
			showAllAccounts();
			break;
		case 10:
			sortAccounts();
			break;
		case 11:
			try {
				DBUtil.closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.exit(0);
			break;
		default:
			System.err.println("Invalid Choice!");
			break;
		}

	}


	private static void sortAccounts() {
		// TODO Auto-generated method stub
		
	}


	private static void showAllAccounts() {
		List<CurrentAccount> currentAccounts = null;
			try {
				currentAccounts = currentAccountService.getAllCurrentAccounts();
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (CurrentAccount currentAccount : currentAccounts) {
				System.out.println(currentAccount);
			}
	}

	private static void checkCurrentBalance() {
		System.out.println("Enter the account Number:");
		int accountNumber = scanner.nextInt();
		try {
			double balance = currentAccountService.checkCurrentBalance(accountNumber);
			System.out.print(balance);
		} catch (ClassNotFoundException | SQLException
				| AccountNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private static void fundTransfer() {
		// TODO Auto-generated method stub
		
	}


	private static void deposit() {
		System.out.println("Enter Account Number: ");
		int accountNumber = scanner.nextInt();
		System.out.println("Enter Amount: ");
		double amount = scanner.nextDouble();
		CurrentAccount currentAccount = null;
		try {
			currentAccount = currentAccountService
					.getAccountById(accountNumber);
			currentAccountService.deposit(currentAccount, amount);
			DBUtil.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			try {
				DBUtil.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			try {
				DBUtil.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}


	private static void withdraw() {
		System.out.println("Enter Account Number: ");
		int accountNumber = scanner.nextInt();
		System.out.println("Enter Amount: ");
		double amount = scanner.nextDouble();
		CurrentAccount currentAccount = null;
		try {
			currentAccount = currentAccountService
					.getAccountById(accountNumber);
			currentAccountService.withdraw(currentAccount, amount);
			DBUtil.commit();
		} catch (ClassNotFoundException | SQLException
				| AccountNotFoundException e) {
			try {
				DBUtil.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (Exception e) {
			try {
				DBUtil.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}


	private static void searchAccount() {
		
		System.out.println("1.To Search by using account number");
		System.out.println("2.To Search by using account holder name");

		System.out.println("Enter the choice:");
		int choice = scanner.nextInt();
		switch (choice) {
		case 1:
			System.out.println("Enter the accountNumber");
			int accountNumber = scanner.nextInt();
			CurrentAccount currentAccounts;
			try {
				currentAccounts = currentAccountService
						.searchAccount(accountNumber);

				System.out.println(currentAccounts.toString());
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (AccountNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 2:
			System.out.println("Enter the accountHolderName:");
			String accountHolderName = scanner.next();
			CurrentAccount currentAccounts1;
			try {
				currentAccounts1 = currentAccountService
						.searchAccountByName(accountHolderName);
				System.out.println(currentAccounts1);
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (AccountNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			try {
				throw new AccountNotFoundException("Not a valid account");
			} catch (AccountNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	private static void deleteAccount() {
		System.out.println("Enter the account NUumber:");
		int accountNumber = scanner.nextInt();
		try {
			currentAccountService.deleteAccount(accountNumber);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}


	private static void updateAccount() {
		System.out.println("Enter 1 to update the name");
		System.out.println("Enter 2 to update the salaried");
		System.out.println("Enter 3 to update Both");
		int choice = scanner.nextInt();
		System.out.println("Enter the account number");
		int accountNumber = scanner.nextInt();
		String accountHolderName="";
		double value = 500;
			switch(choice)
			{
			case 1:
			System.out.println("Enter the name to update");
			accountHolderName = scanner.nextLine();
			accountHolderName = scanner.nextLine();
				CurrentAccount account;
				try {
					account = currentAccountService.getAccountById(accountNumber);
					currentAccountService.updateAccount(account,accountHolderName,value);
				} catch (ClassNotFoundException | SQLException
						| AccountNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			break;
			case 2:
				System.out.println("Enter the salaried to update");
				value = scanner.nextDouble();
				CurrentAccount account1;
				try {
					account1 = currentAccountService.getAccountById(accountNumber);
					currentAccountService.updateAccount(account1,accountHolderName,value);
				} catch (ClassNotFoundException | SQLException
						| AccountNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			break;
			case 3:
				System.out.println("Enter the name to update");
				accountHolderName = scanner.nextLine();
				accountHolderName = scanner.nextLine();
				System.out.println("Enter the salaried to update");
				value = scanner.nextDouble();
				CurrentAccount account2;
				try {
					account2 = currentAccountService.getAccountById(accountNumber);
					currentAccountService.updateAccount(account2,accountHolderName,value);
				} catch (ClassNotFoundException | SQLException
						| AccountNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}


	private static void acceptInput(String type) {
			if (type.equalsIgnoreCase("CA")) {
				System.out.println("Enter your Full Name: ");
				String accountHolderName = scanner.nextLine();
				accountHolderName = scanner.nextLine();
				System.out.println("Enter Initial Balance: ");
				double accountBalance = scanner.nextDouble();
				
				System.out.println("Enter odLimit");
				double odlimit = scanner.nextDouble();
				createCurrentAccount(accountHolderName, accountBalance, odlimit);
			}
		}
	
	private static void createCurrentAccount(String accountHolderName,
			double accountBalance, double odlimit) {
		try {
			currentAccountService.createNewAccount(accountHolderName,
					accountBalance, odlimit);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
