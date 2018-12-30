package com.moneymoney.account.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.moneymoney.account.CurrentAccount;
import com.moneymoney.account.util.DBUtil;
import com.moneymoney.exception.AccountNotFoundException;

public class CurrentAccountDAOImpl implements CurrentAccountDAO{

	@Override
	public CurrentAccount createNewAccount(CurrentAccount account) throws SQLException, ClassNotFoundException {
		Connection connection = DBUtil.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO ACCOUNT VALUES(?,?,?,?,?,?)");
		preparedStatement.setInt(1, account.getBankAccount().getAccountNumber());
		preparedStatement.setString(2, account.getBankAccount().getAccountHolderName());
		preparedStatement.setDouble(3, account.getBankAccount().getAccountBalance());
		preparedStatement.setObject(4, null);
		preparedStatement.setDouble(5,account.getOdlimit() );
		preparedStatement.setString(6, "CA");
		preparedStatement.executeUpdate();
		preparedStatement.close();
		DBUtil.commit();
		return account;
	}

	@Override
	public List<CurrentAccount> getAllCurrentAccounts() throws SQLException, ClassNotFoundException {
		List<CurrentAccount> currentaccount = new ArrayList<CurrentAccount>();
		Connection connection = DBUtil.getConnection();
		Statement statement = connection.createStatement();
		ResultSet result = statement.executeQuery("select * from account");
		while(result.next())
		{
			int accountNumber = result.getInt(1);
			String name = result.getString(2);
			double balance = result.getDouble(3);
			double odlimit = result.getDouble(5);
			CurrentAccount currentAccount1 = new CurrentAccount(accountNumber,name,balance,odlimit);
			currentaccount.add(currentAccount1);
		}
		statement.close();
		return currentaccount;
	}

	@Override
	public double checkCurrentBalance(int accountNumber) throws SQLException, ClassNotFoundException {
		Connection connection = DBUtil.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement("select account_bal from account where account_id=?");
		preparedStatement.setInt(1, accountNumber);
		ResultSet balance = preparedStatement.executeQuery();
		double accountbalance = 0;
		if(balance.next())
		{
			accountbalance =  balance.getDouble(1);
		}
		preparedStatement.close();
		return accountbalance;
	}

		@Override
		public CurrentAccount getAccountById(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException {
			Connection connection = DBUtil.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement
					("SELECT * FROM account where account_id=?");
			preparedStatement.setInt(1, accountNumber);
			ResultSet resultSet = preparedStatement.executeQuery();
			CurrentAccount currentAccount = null;
			if(resultSet.next()) {
				String accountHolderName = resultSet.getString("account_hn");
				double accountBalance = resultSet.getDouble(3);
				double odlimit = resultSet.getDouble("odlimit");
				currentAccount = new CurrentAccount(accountNumber, accountHolderName, accountBalance,odlimit);
				return currentAccount;
			}
			preparedStatement.close();
			throw new AccountNotFoundException("Account with account number "+accountNumber+" does not exist.");
		}

		@Override
		public CurrentAccount updateAccount(CurrentAccount account,
			String accountHolderName, double odlimit) throws SQLException, ClassNotFoundException {
			Connection connection = DBUtil.getConnection();
			PreparedStatement statement = connection.prepareStatement("update account set account_hn=?,odlimit=? where account_id=?");
			statement.setString(1, accountHolderName);
			statement.setDouble(2, odlimit);
			statement.setInt(3,account.getBankAccount().getAccountNumber());
			statement.executeUpdate();
			DBUtil.commit();
			statement.close();
			return null;
		}

		@Override
		public String deleteAccount(int accountNumber) throws SQLException, ClassNotFoundException {
			Connection connection = DBUtil.getConnection();
			PreparedStatement statement = connection.prepareStatement("delete from account where account_id=?");
			statement.setInt(1, accountNumber);
			DBUtil.commit();
			statement.close();
			return null;
		}

		@Override
		public void updateBalance(int accountNumber, double currentBalance) throws SQLException, ClassNotFoundException {
			Connection connection = DBUtil.getConnection();
			connection.setAutoCommit(false);
			PreparedStatement preparedStatement = connection.prepareStatement
					("UPDATE ACCOUNT SET account_bal=? where account_id=?");
			preparedStatement.setDouble(1, currentBalance);
			preparedStatement.setInt(2, accountNumber);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		}

		@Override
		public CurrentAccount searchAccountByName(String accountHolderName) throws ClassNotFoundException, SQLException, AccountNotFoundException {
			Connection connection = DBUtil.getConnection();
			PreparedStatement preparestatement = connection.prepareStatement("select * from account where account_hn=?");
			preparestatement.setString(1, accountHolderName);
			ResultSet resultset = preparestatement.executeQuery();
			while(resultset.next())
			{
				int accountNumber = resultset.getInt(1);
				double accountBalance = resultset.getDouble(3);
				double odlimit = resultset.getDouble("odlimit");
				CurrentAccount currentAccount = new CurrentAccount(accountNumber, accountHolderName, accountBalance,odlimit);
				return currentAccount;
			}
			preparestatement.close();
			throw new AccountNotFoundException("not a valid account");
		}

		@Override
		public CurrentAccount searchAccountByAccountNumber(int accountNumber) throws SQLException, ClassNotFoundException, AccountNotFoundException {
			Connection connection = DBUtil.getConnection();
			PreparedStatement preparestatement = connection.prepareStatement("select * from account where account_id=?");
			preparestatement.setInt(1, accountNumber);
			ResultSet resultset = preparestatement.executeQuery();
			if(resultset.next())
			{
				String accountHolderName = resultset.getString("account_hn");
				double accountBalance = resultset.getDouble(3);
				double odlimit= resultset.getDouble("odlimit");
				CurrentAccount currentAccount = new CurrentAccount(accountNumber, accountHolderName, accountBalance,odlimit);
				return currentAccount;
			}
			preparestatement.close();
			throw new AccountNotFoundException("Not a Valid Account");			
		}

		@Override
		public List<CurrentAccount> getAccountByRange(double minimum, double maximum) throws SQLException, ClassNotFoundException, AccountNotFoundException {
			Connection connection = DBUtil.getConnection();
			List<CurrentAccount> account = new ArrayList<CurrentAccount>();
			PreparedStatement preparestatement = connection.prepareStatement("select * from account WHERE account_bal BETWEEN ? AND ?");
			preparestatement.setDouble(1, minimum);
			preparestatement.setDouble(2, maximum);
			ResultSet resultset = preparestatement.executeQuery();
			while(resultset.next())
			{
				int accountNumber = resultset.getInt(1);
				String accountHolderName = resultset.getString("account_hn");
				double accountBalance = resultset.getDouble(3);
				double odlimit= resultset.getDouble("odlimit");
				CurrentAccount currentAccount = new CurrentAccount(accountNumber, accountHolderName, accountBalance,odlimit);
				
				account.add(currentAccount);
				
			}
			preparestatement.close();
			return account;	
		}

		@Override
		public List<CurrentAccount> sortByAccountHolderName() throws SQLException, ClassNotFoundException {
			List<CurrentAccount> currentAccounts = new ArrayList<CurrentAccount>();
			Connection connection = DBUtil.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultset = statement.executeQuery("select * from account ORDER BY account_hn");
			while(resultset.next())
			{
				int accountNumber = resultset.getInt(1);
				String accountHolderName = resultset.getString("account_hn");
				double accountBalance = resultset.getDouble(3);
				double odlimit = resultset.getDouble("odlimit");
				CurrentAccount currentAccount = new CurrentAccount(accountNumber, accountHolderName, accountBalance,odlimit);
				currentAccounts.add(currentAccount);
			}
			statement.close();
			return currentAccounts;
		}

		@Override
		public List<CurrentAccount> sortBySalaryRange(int minimunbalance,
				int maximumbalance) throws SQLException, ClassNotFoundException {
			List<CurrentAccount> currentAccounts = new ArrayList<CurrentAccount>();
			Connection connection = DBUtil.getConnection();
			PreparedStatement statement = connection.prepareStatement("select * from account WHERE account_bal BETWEEN ? and ?");
			statement.setDouble(1, minimunbalance);
			statement.setDouble(2, maximumbalance);
			ResultSet resultset = statement.executeQuery();
			while(resultset.next())
			{
				int accountNumber = resultset.getInt(1);
				String accountHolderName = resultset.getString("account_hn");
				double accountBalance = resultset.getDouble(3);
				double odlimit = resultset.getDouble("odlimit");
				CurrentAccount currentAccount = new CurrentAccount(accountNumber, accountHolderName, accountBalance,odlimit);
				currentAccounts.add(currentAccount);
			}
			statement.close();
			return currentAccounts;
		}

		@Override
		public List<CurrentAccount> sortBySalaryLessthanGivenInput(int amount) throws ClassNotFoundException, SQLException {
			List<CurrentAccount> currentAccounts = new ArrayList<CurrentAccount>();
			Connection connection = DBUtil.getConnection();
			PreparedStatement statement = connection.prepareStatement("select * from account WHERE account_bal <= ? ORDER BY account_bal");
			statement.setInt(1, amount);
			ResultSet resultset = statement.executeQuery();
			while(resultset.next())
			{
				int accountNumber = resultset.getInt(1);
				String accountHolderName = resultset.getString("account_hn");
				double accountBalance = resultset.getDouble(3);
				double odlimit = resultset.getDouble("odlimit");
				CurrentAccount currentAccount = new CurrentAccount(accountNumber, accountHolderName, accountBalance,odlimit);
				currentAccounts.add(currentAccount);
			}
			statement.close();
			return currentAccounts;
		}

		@Override
		public List<CurrentAccount> sortBySalaryGreaterthanGivenInput(int maximumAmount) throws ClassNotFoundException, SQLException {
			List<CurrentAccount> currentAccounts = new ArrayList<CurrentAccount>();
			Connection connection = DBUtil.getConnection();
			PreparedStatement statement = connection.prepareStatement("select * from account WHERE account_bal >= ? ORDER BY account_bal");
			statement.setInt(1, maximumAmount);
			ResultSet resultset = statement.executeQuery();
			while(resultset.next())
			{
				int accountNumber = resultset.getInt(1);
				String accountHolderName = resultset.getString("account_hn");
				double accountBalance = resultset.getDouble(3);
				double odlimit = resultset.getDouble("odlimit");
				CurrentAccount currentAccount = new CurrentAccount(accountNumber, accountHolderName, accountBalance,odlimit);
				currentAccounts.add(currentAccount);
			}
			statement.close();
			return currentAccounts;
		}
	}


