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

	public CurrentAccount searchAccountByName(String accountHolderName);
}