package com.moneymoney.account.ui;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.moneymoney.account.SavingsAccount;
import com.moneymoney.account.service.SavingsAccountService;
import com.moneymoney.account.service.SavingsAccountServiceImpl;
import com.moneymoney.account.util.DBUtil;
import com.moneymoney.exception.AccountNotFoundException;

public class AccountCUI {
	private static Scanner scanner = new Scanner(System.in);
	private static SavingsAccountService savingsAccountService = new SavingsAccountServiceImpl();

	public static void startSavingsAccount() {

		do {
			System.out.println("****** Welcome to Money Money Bank********");
			System.out.println("1. Open New Savings Account");
			System.out.println("2. Update Account");
			System.out.println("3. Close Account");
			System.out.println("4. Search Account");
			System.out.println("5. Withdraw");
			System.out.println("6. Deposit");
			System.out.println("7. FundTransfer");
			System.out.println("8. Check Current Balance");
			System.out.println("9. Get All Savings Account Details");
			System.out.println("10. Sort Accounts");
			System.out.println("11. Exit");
			System.out.println();
			System.out.println("Make your choice: ");

			int choice = scanner.nextInt();

			performOperation(choice);

		} while (true);
	}
	
	private static void performOperation(int choice) {
		switch (choice) {
		case 1:
			acceptInput("SA");
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

	

	private static void updateAccount() {
		System.out.println("Enter 1 to update the name");
		System.out.println("Enter 2 to update the salaried");
		System.out.println("Enter 3 to update Both");
		int choice = scanner.nextInt();
		System.out.println("Enter the account number");
		int accountNumber = scanner.nextInt();
		String accountHolderName="";
		boolean value = true;
			switch(choice)
			{
			case 1:
			System.out.println("Enter the name to update");
			accountHolderName = scanner.nextLine();
			accountHolderName = scanner.nextLine();
				SavingsAccount account;
				try {
					account = savingsAccountService.getAccountById(accountNumber);
					savingsAccountService.updateAccount(account,accountHolderName,value);
				} catch (ClassNotFoundException | SQLException
						| AccountNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			break;
			case 2:
				System.out.println("Enter the salaried to update");
				value = scanner.nextBoolean();
				SavingsAccount account1;
				try {
					account1 = savingsAccountService.getAccountById(accountNumber);
					savingsAccountService.updateAccount(account1,accountHolderName,value);
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
				value = scanner.nextBoolean();
				SavingsAccount account2;
				try {
					account2 = savingsAccountService.getAccountById(accountNumber);
					savingsAccountService.updateAccount(account2,accountHolderName,value);
				} catch (ClassNotFoundException | SQLException
						| AccountNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		

	}

	private static void checkCurrentBalance() {

		System.out.println("Enter the accountnumber:");
		int accountNumber = scanner.nextInt();
		try {
			double balance = savingsAccountService
					.checkCurrentBalance(accountNumber);
			System.out.println(balance);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AccountNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			SavingsAccount savingsAccounts;
			try {
				savingsAccounts = savingsAccountService
						.searchAccount(accountNumber);

				System.out.println(savingsAccounts.toString());
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
			SavingsAccount savingsAccounts1;
			try {
				savingsAccounts1 = savingsAccountService
						.searchAccountByName(accountHolderName);
				System.out.println(savingsAccounts1);
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
		System.out.println("Enter the accountnumber:");
		int accountNumber = scanner.nextInt();
		try {
			String result = savingsAccountService.deleteAccount(accountNumber);
			System.out.println(result);
		} catch (ClassNotFoundException | SQLException e) {
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
			SavingsAccount senderSavingsAccount = savingsAccountService
					.getAccountById(senderAccountNumber);
			SavingsAccount receiverSavingsAccount = savingsAccountService
					.getAccountById(receiverAccountNumber);
			savingsAccountService.fundTransfer(senderSavingsAccount,
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
		SavingsAccount savingsAccount = null;
		try {
			savingsAccount = savingsAccountService
					.getAccountById(accountNumber);
			savingsAccountService.deposit(savingsAccount, amount);
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
		SavingsAccount savingsAccount = null;
		try {
			savingsAccount = savingsAccountService
					.getAccountById(accountNumber);
			savingsAccountService.withdraw(savingsAccount, amount);
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

	private static void showAllAccounts() {
		List<SavingsAccount> savingsAccounts;
		try {
			savingsAccounts = savingsAccountService.getAllSavingsAccount();
			for (SavingsAccount savingsAccount : savingsAccounts) {
				System.out.println(savingsAccount);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

	}

	private static void acceptInput(String type) {
		if (type.equalsIgnoreCase("SA")) {
			System.out.println("Enter your Full Name: ");
			String accountHolderName = scanner.nextLine();
			accountHolderName = scanner.nextLine();
			System.out.println("Enter Initial Balance(type na for Zero Balance): ");
			String accountBalanceStr = scanner.next();
			double accountBalance = 0.0;
			if (!accountBalanceStr.equalsIgnoreCase("na")) {
				accountBalance = Double.parseDouble(accountBalanceStr);
			}
			System.out.println("Salaried?(y/n): ");
			boolean salary = scanner.next().equalsIgnoreCase("n") ? false
					: true;
			createSavingsAccount(accountHolderName, accountBalance, salary);
		}
	}

	private static void createSavingsAccount(String accountHolderName,
			double accountBalance, boolean salary) {
		try {
			savingsAccountService.createNewAccount(accountHolderName,
					accountBalance, salary);
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
			List<SavingsAccount> savingsAccounts;
			switch (choice) {
			case 1:
				try {
					savingsAccounts = savingsAccountService.sortByAccountHolderName();
					for (SavingsAccount savingsAccount : savingsAccounts) {
						System.out.println(savingsAccount);
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
					savingsAccounts = savingsAccountService.sortBySalaryRange(minimunbalance,maximumbalance);
					for (SavingsAccount savingsAccount : savingsAccounts) {
						System.out.println(savingsAccount);
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
					savingsAccounts = savingsAccountService.sortBySalaryLessthanGivenInput(amount);
					for (SavingsAccount savingsAccount : savingsAccounts) {
						System.out.println(savingsAccount);
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
					savingsAccounts = savingsAccountService.sortBySalaryGreaterthanGivenInput(maximumAmount);
					for (SavingsAccount savingsAccount : savingsAccounts) {
						System.out.println(savingsAccount);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 5:
				System.exit(0);
				
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
