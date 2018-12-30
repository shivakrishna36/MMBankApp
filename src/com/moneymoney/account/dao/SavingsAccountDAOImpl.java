package com.moneymoney.account.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.moneymoney.account.SavingsAccount;
import com.moneymoney.account.util.DBUtil;
import com.moneymoney.exception.AccountNotFoundException;

public class SavingsAccountDAOImpl implements SavingsAccountDAO {

	public SavingsAccount createNewAccount(SavingsAccount account) throws ClassNotFoundException, SQLException {
		Connection connection = DBUtil.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO ACCOUNT VALUES(?,?,?,?,?,?)");
		preparedStatement.setInt(1, account.getBankAccount().getAccountNumber());
		preparedStatement.setString(2, account.getBankAccount().getAccountHolderName());
		preparedStatement.setDouble(3, account.getBankAccount().getAccountBalance());
		preparedStatement.setBoolean(4, account.isSalary());
		preparedStatement.setObject(5, null);
		preparedStatement.setString(6, "SA");
		preparedStatement.executeUpdate();
		preparedStatement.close();
		DBUtil.commit();
		return account;
	}

	public List<SavingsAccount> getAllSavingsAccount() throws ClassNotFoundException, SQLException {
		List<SavingsAccount> savingsAccounts = new ArrayList<>();
		Connection connection = DBUtil.getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM ACCOUNT");
		while (resultSet.next()) {// Check if row(s) is present in table
			int accountNumber = resultSet.getInt(1);
			String accountHolderName = resultSet.getString("account_hn");
			double accountBalance = resultSet.getDouble(3);
			boolean salary = resultSet.getBoolean("salary");
			SavingsAccount savingsAccount = new SavingsAccount(accountNumber, accountHolderName, accountBalance,
					salary);
			savingsAccounts.add(savingsAccount);
		}
		DBUtil.commit();
		return savingsAccounts;
	}
	@Override
	public void updateBalance(int accountNumber, double currentBalance) throws ClassNotFoundException, SQLException {
		Connection connection = DBUtil.getConnection();
		connection.setAutoCommit(false);
		PreparedStatement preparedStatement = connection.prepareStatement
				("UPDATE ACCOUNT SET account_bal=? where account_id=?");
		preparedStatement.setDouble(1, currentBalance);
		preparedStatement.setInt(2, accountNumber);
		preparedStatement.executeUpdate();
	}
	
	@Override
	public SavingsAccount getAccountById(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException {
		Connection connection = DBUtil.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement
				("SELECT * FROM account where account_id=?");
		preparedStatement.setInt(1, accountNumber);
		ResultSet resultSet = preparedStatement.executeQuery();
		SavingsAccount savingsAccount = null;
		if(resultSet.next()) {
			String accountHolderName = resultSet.getString("account_hn");
			double accountBalance = resultSet.getDouble(3);
			boolean salary = resultSet.getBoolean("salary");
			savingsAccount = new SavingsAccount(accountNumber, accountHolderName, accountBalance,
					salary);
			return savingsAccount;
		}
		throw new AccountNotFoundException("Account with account number "+accountNumber+" does not exist.");
	}
	
	public SavingsAccount updateAccount(SavingsAccount account,String name,boolean value) throws SQLException, ClassNotFoundException {
		Connection connection = DBUtil.getConnection();
		PreparedStatement statement = connection.prepareStatement("update account set account_hn=?,salary=? where account_id=?");
		statement.setString(1, name);
		statement.setBoolean(2, value);
		statement.setInt(3,account.getBankAccount().getAccountNumber());
		statement.executeUpdate();
		statement.close();
		return null;
	}

	
	@Override
	public String deleteAccount(int accountNumber) throws ClassNotFoundException, SQLException {
		Connection connection = DBUtil.getConnection();
		PreparedStatement statement = connection.prepareStatement("delete from account where account_id=?");
		statement.setInt(1, accountNumber);
		boolean result = statement.execute();
		String answer = "";
		if(!result)
		{
			answer = "account deleted";
		}
		statement.close();
		DBUtil.commit();
		return answer;
	}

	

	@Override
	public SavingsAccount searchAccount(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException {
		Connection connection = DBUtil.getConnection();
		PreparedStatement preparestatement = connection.prepareStatement("select * from account where account_id=?");
		preparestatement.setInt(1, accountNumber);
		ResultSet resultset = preparestatement.executeQuery();
		if(resultset.next())
		{
			String accountHolderName = resultset.getString("account_hn");
			double accountBalance = resultset.getDouble(3);
			boolean salary = resultset.getBoolean("salary");
			SavingsAccount savingsAccount = new SavingsAccount(accountNumber, accountHolderName, accountBalance,salary);
			return savingsAccount;
		}
		throw new AccountNotFoundException("Not a Valid Account");
	}


	@Override
	public double checkCurrentBalance(int accountNumber) throws SQLException, ClassNotFoundException, AccountNotFoundException {
		Connection connection = DBUtil.getConnection();
		PreparedStatement preparestatement = connection.prepareStatement("select account_bal from account where account_id=?");
		preparestatement.setInt(1, accountNumber);
		ResultSet result = preparestatement.executeQuery();
		if(result.next())
		{
			double balance = result.getDouble(1);
			return balance;
		}
		throw new AccountNotFoundException("not a valid account");
	}

	@Override
	public void commit() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SavingsAccount searchAccountByName(String accountHolderName) throws SQLException, AccountNotFoundException, ClassNotFoundException {
		Connection connection = DBUtil.getConnection();
		PreparedStatement preparestatement = connection.prepareStatement("select * from account where account_hn=?");
		preparestatement.setString(1, accountHolderName);
		ResultSet resultset = preparestatement.executeQuery();
		while(resultset.next())
		{
			int accountNumber = resultset.getInt(1);
			double accountBalance = resultset.getDouble(3);
			boolean salary = resultset.getBoolean("salary");
			SavingsAccount savingsAccount = new SavingsAccount(accountNumber, accountHolderName, accountBalance,salary);
			return savingsAccount;
		}
		throw new AccountNotFoundException("not a valid account");
	}


	@Override
	public List<SavingsAccount> sortByAccountHolderName() throws SQLException, ClassNotFoundException {
		List<SavingsAccount> savingsAccounts = new ArrayList<SavingsAccount>();
		Connection connection = DBUtil.getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultset = statement.executeQuery("select * from account ORDER BY account_hn");
		while(resultset.next())
		{
			int accountNumber = resultset.getInt(1);
			String accountHolderName = resultset.getString("account_hn");
			double accountBalance = resultset.getDouble(3);
			boolean salary = resultset.getBoolean("salary");
			SavingsAccount savingsAccount = new SavingsAccount(accountNumber, accountHolderName, accountBalance,salary);
			savingsAccounts.add(savingsAccount);
		}
		return savingsAccounts;
	}

	@Override
	public  List<SavingsAccount> sortBySalaryRange(int minimunbalance, int maximumbalance) throws ClassNotFoundException, SQLException {
		List<SavingsAccount> savingsAccounts = new ArrayList<SavingsAccount>();
		Connection connection = DBUtil.getConnection();
		PreparedStatement statement = connection.prepareStatement("select * from account WHERE account_bal BETWEEN ? and ? ORDER BY account_bal");
		statement.setDouble(1, minimunbalance);
		statement.setDouble(2, maximumbalance);
		ResultSet resultset = statement.executeQuery();
		while(resultset.next())
		{
			int accountNumber = resultset.getInt(1);
			String accountHolderName = resultset.getString("account_hn");
			double accountBalance = resultset.getDouble(3);
			boolean salary = resultset.getBoolean("salary");
			SavingsAccount savingsAccount = new SavingsAccount(accountNumber, accountHolderName, accountBalance,salary);
			savingsAccounts.add(savingsAccount);
		}
		return savingsAccounts;
	}

	@Override
	public  List<SavingsAccount> sortByLessthanGivenSalary(int amount) throws ClassNotFoundException, SQLException {
		List<SavingsAccount> savingsAccounts = new ArrayList<SavingsAccount>();
		Connection connection = DBUtil.getConnection();
		PreparedStatement statement = connection.prepareStatement("select * from account WHERE account_bal <=? ORDER BY account_bal");
		statement.setDouble(1, amount);
		ResultSet resultset = statement.executeQuery();
		while(resultset.next())
		{
			int accountNumber = resultset.getInt(1);
			String accountHolderName = resultset.getString("account_hn");
			double accountBalance = resultset.getDouble(3);
			boolean salary = resultset.getBoolean("salary");
			SavingsAccount savingsAccount = new SavingsAccount(accountNumber, accountHolderName, accountBalance,salary);
			savingsAccounts.add(savingsAccount);
		}
		return savingsAccounts;
	}
	
	@Override
	public  List<SavingsAccount> sortByGreaterthanGivenSalary(int amount) throws ClassNotFoundException, SQLException {
		List<SavingsAccount> savingsAccounts = new ArrayList<SavingsAccount>();
		Connection connection = DBUtil.getConnection();
		PreparedStatement statement = connection.prepareStatement("select * from account WHERE account_bal >=? ORDER BY account_bal");
		statement.setDouble(1, amount);
		ResultSet resultset = statement.executeQuery();
		while(resultset.next())
		{
			int accountNumber = resultset.getInt(1);
			String accountHolderName = resultset.getString("account_hn");
			double accountBalance = resultset.getDouble(3);
			boolean salary = resultset.getBoolean("salary");
			SavingsAccount savingsAccount = new SavingsAccount(accountNumber, accountHolderName, accountBalance,salary);
			savingsAccounts.add(savingsAccount);
		}
		return savingsAccounts;
	}

	@Override
	public List<SavingsAccount> getByAccountBalanceRange(double minimum,double maximum) throws SQLException, ClassNotFoundException {
		List<SavingsAccount> savingsAccounts = new ArrayList<SavingsAccount>();
		Connection connection = DBUtil.getConnection();
		PreparedStatement statement = connection.prepareStatement("select * from account WHERE account_bal BETWEEN ? and ?");
		statement.setDouble(1, minimum);
		statement.setDouble(2, maximum);
		ResultSet resultset = statement.executeQuery();
		while(resultset.next())
		{
			int accountNumber = resultset.getInt(1);
			String accountHolderName = resultset.getString("account_hn");
			double accountBalance = resultset.getDouble(3);
			boolean salary = resultset.getBoolean("salary");
			SavingsAccount savingsAccount = new SavingsAccount(accountNumber, accountHolderName, accountBalance,salary);
			savingsAccounts.add(savingsAccount);
		}
		return savingsAccounts;
	}

}
