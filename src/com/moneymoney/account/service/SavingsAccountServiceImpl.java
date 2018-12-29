package com.moneymoney.account.service;

import java.sql.SQLException;
import java.util.List;

import com.moneymoney.account.SavingsAccount;
import com.moneymoney.account.dao.SavingsAccountDAO;
import com.moneymoney.account.dao.SavingsAccountDAOImpl;
import com.moneymoney.account.factory.AccountFactory;
import com.moneymoney.account.util.DBUtil;
import com.moneymoney.exception.AccountNotFoundException;
import com.moneymoney.exception.InsufficientFundsException;
import com.moneymoney.exception.InvalidInputException;

public class SavingsAccountServiceImpl implements SavingsAccountService {

	private AccountFactory factory;
	private SavingsAccountDAO savingsAccountDAO;

	public SavingsAccountServiceImpl() {
		factory = AccountFactory.getInstance();
		savingsAccountDAO = new SavingsAccountDAOImpl();
	}

	@Override
	public SavingsAccount createNewAccount(String accountHolderName, double accountBalance, boolean salary)
			throws ClassNotFoundException, SQLException {
		SavingsAccount account = factory.createNewSavingsAccount(accountHolderName, accountBalance, salary);
		savingsAccountDAO.createNewAccount(account);
		return null;
	}

	@Override
	public List<SavingsAccount> getAllSavingsAccount() throws ClassNotFoundException, SQLException {
		return savingsAccountDAO.getAllSavingsAccount();
	}

	@Override
	public void deposit(SavingsAccount account, double amount) throws ClassNotFoundException, SQLException {
		if (amount > 0) {
			double currentBalance = account.getBankAccount().getAccountBalance();
			currentBalance += amount;
			savingsAccountDAO.updateBalance(account.getBankAccount().getAccountNumber(), currentBalance);
			//savingsAccountDAO.commit();
		}else {
			throw new InvalidInputException("Invalid Input Amount!");
		}
	}
	@Override
	public void withdraw(SavingsAccount account, double amount) throws ClassNotFoundException, SQLException {
		double currentBalance = account.getBankAccount().getAccountBalance();
		if (amount > 0 && currentBalance >= amount) {
			currentBalance -= amount;
			savingsAccountDAO.updateBalance(account.getBankAccount().getAccountNumber(), currentBalance);
			//savingsAccountDAO.commit();
		} else {
			throw new InsufficientFundsException("Invalid Input or Insufficient Funds!");
		}
	}

	@Override
	public void fundTransfer(SavingsAccount sender, SavingsAccount receiver, double amount)
			throws ClassNotFoundException, SQLException {
		try {
			withdraw(sender, amount);
			deposit(receiver, amount);
			DBUtil.commit();
		} catch (InvalidInputException | InsufficientFundsException e) {
			e.printStackTrace();
			DBUtil.rollback();
		} catch(Exception e) {
			e.printStackTrace();
			DBUtil.rollback();
		}
	}

	@Override
	public SavingsAccount updateAccount(SavingsAccount account,String name,boolean value) throws ClassNotFoundException, SQLException {
		savingsAccountDAO.updateAccount(account,name,value);
		return null;
	}

	@Override
	public SavingsAccount getAccountById(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException {
		return savingsAccountDAO.getAccountById(accountNumber);
	}

	@Override
	public String deleteAccount(int accountNumber) throws ClassNotFoundException, SQLException {
		return savingsAccountDAO.deleteAccount(accountNumber);
	
	}

	@Override
	public SavingsAccount searchAccount(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException {
		return savingsAccountDAO.searchAccount(accountNumber);
	}

	@Override
	public double checkCurrentBalance(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException {
		return savingsAccountDAO.checkCurrentBalance(accountNumber);
	}

	@Override
	public SavingsAccount searchAccountByName(String accountHolderName) throws ClassNotFoundException, SQLException, AccountNotFoundException {
		return savingsAccountDAO.searchAccountByName(accountHolderName);
		
	}


	@Override
	public List<SavingsAccount> sortByAccountHolderName() throws ClassNotFoundException, SQLException {
		return savingsAccountDAO.sortByAccountHolderName();
	}

	@Override
	public List<SavingsAccount> sortBySalaryRange(int minimunbalance,
			int maximumbalance) throws ClassNotFoundException, SQLException {
		return  savingsAccountDAO.sortBySalaryRange(minimunbalance,maximumbalance);
		
	}

	@Override
	public List<SavingsAccount> sortBySalaryLessthanGivenInput(int amount) throws ClassNotFoundException, SQLException {
		return savingsAccountDAO.sortByLessthanGivenSalary(amount);
	}

	@Override
	public List<SavingsAccount> sortBySalaryGreaterthanGivenInput(
			int maximumAmount) throws ClassNotFoundException, SQLException {
		return savingsAccountDAO.sortByGreaterthanGivenSalary(maximumAmount);
	}

	

}
