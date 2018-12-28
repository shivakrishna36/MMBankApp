package com.moneymoney.account.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.moneymoney.account.SavingsAccount;
import com.moneymoney.exception.AccountNotFoundException;

public interface SavingsAccountDAO {
	
	SavingsAccount createNewAccount(SavingsAccount account) throws ClassNotFoundException, SQLException;
	SavingsAccount updateAccount(int account,String name) throws SQLException, ClassNotFoundException;
	SavingsAccount getAccountById(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException;
	void deleteAccount(int accountNumber) throws ClassNotFoundException, SQLException;
	List<SavingsAccount> getAllSavingsAccount() throws ClassNotFoundException, SQLException;
	void updateBalance(int accountNumber, double currentBalance) throws ClassNotFoundException, SQLException;
	void commit() throws SQLException;
	SavingsAccount searchAccount(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException;
	double checkCurrentBalance(int accountNumber) throws SQLException, ClassNotFoundException, AccountNotFoundException;
	SavingsAccount searchAccountByName(String accountHolderName) throws SQLException, AccountNotFoundException, ClassNotFoundException;
	List<SavingsAccount> sortByAccountNumber() throws SQLException, ClassNotFoundException, AccountNotFoundException;
	List<SavingsAccount> sortByAccountHolderName() throws SQLException, ClassNotFoundException;
	
	
}