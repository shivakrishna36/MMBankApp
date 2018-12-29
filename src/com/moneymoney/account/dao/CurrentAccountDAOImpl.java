package com.moneymoney.account.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.moneymoney.account.CurrentAccount;
import com.moneymoney.account.SavingsAccount;
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
		}

		@Override
		public CurrentAccount searchAccountByName(String accountHolderName) {
			// TODO Auto-generated method stub
			return null;
		}
	}


