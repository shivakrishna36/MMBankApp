package com.moneymoney.account.service;

import java.sql.SQLException;
import java.util.List;

import com.moneymoney.account.CurrentAccount;
import com.moneymoney.exception.AccountNotFoundException;

public interface CurrentAccountService 
{
	 CurrentAccount createNewAccount(String accountHolderName, double accountBalance, double odlimit) throws ClassNotFoundException, SQLException ;

	 CurrentAccount updateAccount(CurrentAccount account, String accountHolderName, double odlimit) throws ClassNotFoundException, SQLException;

	CurrentAccount getAccountById(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException;

	String deleteAccount(int accountNumber) throws ClassNotFoundException, SQLException;
	
	List<CurrentAccount> getAllCurrentAccounts() throws ClassNotFoundException, SQLException;

	void fundTransfer(CurrentAccount sender, CurrentAccount receiver, double amount) throws ClassNotFoundException, SQLException;
	void deposit(CurrentAccount account, double amount) throws ClassNotFoundException, SQLException;
	void withdraw(CurrentAccount account, double amount) throws ClassNotFoundException, SQLException;

	CurrentAccount searchAccount(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException;

	double checkCurrentBalance(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException;
		

	CurrentAccount searchAccountByName(String accountHolderName) throws ClassNotFoundException, SQLException, AccountNotFoundException;

	List<CurrentAccount> sortByAccountHolderName() throws ClassNotFoundException, SQLException;

	List<CurrentAccount> sortBySalaryRange(int minimunbalance,
			int maximumbalance) throws ClassNotFoundException, SQLException;

	List<CurrentAccount> sortBySalaryLessthanGivenInput(int amount) throws ClassNotFoundException, SQLException;

	List<CurrentAccount> sortBySalaryGreaterthanGivenInput(int maximumAmount) throws ClassNotFoundException, SQLException;

}
