package com.moneymoney.account.dao;

import java.sql.SQLException;
import java.util.List;

import com.moneymoney.account.CurrentAccount;
import com.moneymoney.exception.AccountNotFoundException;

public interface CurrentAccountDAO {

	public CurrentAccount createNewAccount(CurrentAccount account) throws SQLException, ClassNotFoundException;

	public List<CurrentAccount> getAllCurrentAccounts() throws SQLException, ClassNotFoundException;

	public double checkCurrentBalance(int accountNumber) throws SQLException, ClassNotFoundException;

	public CurrentAccount getAccountById(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException;

	public CurrentAccount updateAccount(CurrentAccount account,
			String accountHolderName, double odlimit) throws SQLException, ClassNotFoundException;

	public String deleteAccount(int accountNumber) throws SQLException, ClassNotFoundException;

	public void updateBalance(int accountNumber, double currentBalance) throws SQLException, ClassNotFoundException;

	public CurrentAccount searchAccountByName(String accountHolderName) throws ClassNotFoundException, SQLException, AccountNotFoundException;

	public CurrentAccount searchAccountByAccountNumber(int accountNumber) throws SQLException, ClassNotFoundException, AccountNotFoundException;

	public List<CurrentAccount> getAccountByRange(double minimum, double minimum2) throws SQLException, ClassNotFoundException, AccountNotFoundException;

	public List<CurrentAccount> sortByAccountHolderName() throws SQLException, ClassNotFoundException;

	public List<CurrentAccount> sortBySalaryRange(int minimunbalance,
			int maximumbalance) throws SQLException, ClassNotFoundException;

	public List<CurrentAccount> sortBySalaryLessthanGivenInput(int amount) throws ClassNotFoundException, SQLException;

	public List<CurrentAccount> sortBySalaryGreaterthanGivenInput(
			int maximumAmount) throws ClassNotFoundException, SQLException;
}