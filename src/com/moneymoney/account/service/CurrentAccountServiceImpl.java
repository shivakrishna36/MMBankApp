package com.moneymoney.account.service;

import java.sql.SQLException;
import java.util.List;

import com.moneymoney.account.CurrentAccount;
import com.moneymoney.account.dao.CurrentAccountDAO;
import com.moneymoney.account.dao.CurrentAccountDAOImpl;
import com.moneymoney.account.factory.AccountFactory;
import com.moneymoney.account.util.DBUtil;
import com.moneymoney.exception.AccountNotFoundException;
import com.moneymoney.exception.InsufficientFundsException;
import com.moneymoney.exception.InvalidInputException;

public class CurrentAccountServiceImpl implements CurrentAccountService{

	private AccountFactory factory;
	private CurrentAccountDAO currentAccountDAO;

	public CurrentAccountServiceImpl() {
		factory = AccountFactory.getInstance();
		currentAccountDAO = new CurrentAccountDAOImpl();
	}

	@Override
	public CurrentAccount createNewAccount(String accountHolderName,double accountBalance, double odlimit)throws ClassNotFoundException, SQLException {
		CurrentAccount account = factory.createNewCurrentAccount(accountHolderName, accountBalance, odlimit);
		currentAccountDAO.createNewAccount(account);
		return null;
	}

	@Override
	public CurrentAccount updateAccount(CurrentAccount account,String accountHolderName, double odlimit)throws ClassNotFoundException, SQLException {
		return currentAccountDAO.updateAccount(account,accountHolderName,odlimit);
	}

	@Override
	public CurrentAccount getAccountById(int accountNumber) throws ClassNotFoundException, SQLException,AccountNotFoundException {
		return currentAccountDAO.getAccountById(accountNumber);
	}

	@Override
	public String deleteAccount(int accountNumber)throws ClassNotFoundException, SQLException {
		return currentAccountDAO.deleteAccount(accountNumber);
	}

	@Override
	public List<CurrentAccount> getAllCurrentAccounts()throws ClassNotFoundException, SQLException {
		return currentAccountDAO.getAllCurrentAccounts();
	}

	@Override
	public void fundTransfer(CurrentAccount sender, CurrentAccount receiver,double amount) throws ClassNotFoundException, SQLException {
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
	public void deposit(CurrentAccount account, double amount)throws ClassNotFoundException, SQLException {
		if (amount > 0) {
			double currentBalance = account.getBankAccount().getAccountBalance();
			currentBalance += amount;
			currentAccountDAO.updateBalance(account.getBankAccount().getAccountNumber(), currentBalance);
			//savingsAccountDAO.commit();
		}else {
			throw new InvalidInputException("Invalid Input Amount!");
		}
	}

	@Override
	public void withdraw(CurrentAccount account, double amount)throws ClassNotFoundException, SQLException {
		double currentBalance = account.getBankAccount().getAccountBalance();
		if (amount > 0 && amount <= currentBalance + account.getOdlimit()) {
			currentBalance -= amount;
			currentAccountDAO.updateBalance(account.getBankAccount().getAccountNumber(), currentBalance);
			//savingsAccountDAO.commit();
		} else {
			throw new InsufficientFundsException("Invalid Input or Insufficient Funds!");
		}
	}

	@Override
	public CurrentAccount searchAccountById(int accountNumber)throws ClassNotFoundException, SQLException,AccountNotFoundException {
		return currentAccountDAO.searchAccountByAccountNumber(accountNumber);
	}

	@Override
	public double checkCurrentBalance(int accountNumber)throws ClassNotFoundException, SQLException,AccountNotFoundException {
		return currentAccountDAO.checkCurrentBalance(accountNumber);
	}

	@Override
	public CurrentAccount searchAccountByName(String accountHolderName)throws ClassNotFoundException, SQLException,AccountNotFoundException {
		return currentAccountDAO.searchAccountByName(accountHolderName);
	}

	@Override
	public List<CurrentAccount> sortByAccountHolderName()throws ClassNotFoundException, SQLException {
		return currentAccountDAO.sortByAccountHolderName();
	}

	@Override
	public List<CurrentAccount> sortBySalaryRange(int minimunbalance,int maximumbalance) throws ClassNotFoundException, SQLException {
		return currentAccountDAO.sortBySalaryRange(minimunbalance,maximumbalance);
	}

	@Override
	public List<CurrentAccount> sortBySalaryLessthanGivenInput(int amount)	throws ClassNotFoundException, SQLException {
		return currentAccountDAO.sortBySalaryLessthanGivenInput(amount);
	}

	@Override
	public List<CurrentAccount> sortBySalaryGreaterthanGivenInput(int maximumAmount) throws ClassNotFoundException, SQLException {
		return currentAccountDAO.sortBySalaryGreaterthanGivenInput(maximumAmount);
	}

	@Override
	public List<CurrentAccount> getAccountByRange(double minimum, double maximum) throws ClassNotFoundException, SQLException, AccountNotFoundException {
		
		return currentAccountDAO.getAccountByRange(minimum,maximum);
	}
}
