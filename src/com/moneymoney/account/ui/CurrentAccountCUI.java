package com.moneymoney.account.ui;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.moneymoney.account.CurrentAccount;
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

	private static void showAllAccounts() {
		List<CurrentAccount> currentAccounts = null;
			try {
				currentAccounts = currentAccountService.getAllCurrentAccounts();
				for (CurrentAccount currentAccount : currentAccounts) {
					System.out.println(currentAccount);
				}
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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

		System.out.println("Enter Account Sender's Number: ");
		int senderAccountNumber = scanner.nextInt();
		System.out.println("Enter Account Receiver's Number: ");
		int receiverAccountNumber = scanner.nextInt();
		System.out.println("Enter Amount: ");
		double amount = scanner.nextDouble();
		try {
			CurrentAccount senderCurrentAccount = currentAccountService
					.getAccountById(senderAccountNumber);
			CurrentAccount receiverSavingsAccount = currentAccountService
					.getAccountById(receiverAccountNumber);
			currentAccountService.fundTransfer(senderCurrentAccount,
					receiverSavingsAccount, amount);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		System.out.println("3.To search by using account balance range");

		System.out.println("Enter the choice:");
		int choice = scanner.nextInt();
		switch (choice) {
		case 1:
			System.out.println("Enter the accountNumber");
			int accountNumber = scanner.nextInt();
			CurrentAccount currentAccounts;
			try {
				currentAccounts = currentAccountService
						.searchAccountById(accountNumber);

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
		case 3:
			System.out.println("Enter minimum balance:");
			double minimum = scanner.nextDouble();
			System.out.println("Enter maximum balance:");
			double maximum = scanner.nextDouble();
			List<CurrentAccount> currentaccount = null;
			try {
				currentaccount = currentAccountService.getAccountByRange(minimum,maximum);
				for (CurrentAccount currentAccount : currentaccount) {
					System.out.println(currentAccount);
				}
			} catch (ClassNotFoundException | SQLException
					| AccountNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
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
	
	private static void sortAccounts() {
		do {
			System.out.println("+++++Ways of Sorting+++++++");
			System.out.println("1. Account Holder Name");
			System.out.println("2. Enter the account balance range to sort");
			System.out.println("3. Enter the amount that returns lessthan given amount accounts");
			System.out.println("4. Enter the amount that returns greaterthan given amount accounts");
			System.out.println("5. Exit");
			System.out.println("Enter the option number to sort accounts");
			int choice = scanner.nextInt();
			List<CurrentAccount> currentAccounts;
			switch (choice) {
			case 1:
				try {
					currentAccounts = currentAccountService.sortByAccountHolderName();
					for (CurrentAccount currentAccount : currentAccounts) {
						System.out.println(currentAccount);
					}
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
				break;

			case 2:
				System.out.println("Enter Minimum Range");
				int minimunbalance = scanner.nextInt();
				System.out.println("Enter Maximun Range");
				int maximumbalance = scanner.nextInt();
				try {
					currentAccounts = currentAccountService.sortBySalaryRange(minimunbalance,maximumbalance);
					for (CurrentAccount currentAccount : currentAccounts) {
						System.out.println(currentAccount);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
				
			case 3:
				System.out.println("Enter amount");
				int amount = scanner.nextInt();
				try {
					currentAccounts = currentAccountService.sortBySalaryLessthanGivenInput(amount);
					for (CurrentAccount currentAccount : currentAccounts) {
						System.out.println(currentAccount);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
				
			case 4:
				System.out.println("Enter amount");
				int maximumAmount = scanner.nextInt();
				try {
					currentAccounts = currentAccountService.sortBySalaryGreaterthanGivenInput(maximumAmount);
					for (CurrentAccount currentAccount : currentAccounts) {
						System.out.println(currentAccount);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 5:
				startCurrentAccount();
				
			default:
				try {
					throw new AccountNotFoundException("not valid option");
				} catch (AccountNotFoundException e) {
					e.printStackTrace();
				}
			}

		} while (true);

		
	}

}

