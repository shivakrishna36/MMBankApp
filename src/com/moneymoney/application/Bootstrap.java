package com.moneymoney.application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

import com.moneymoney.account.ui.AccountCUI;
import com.moneymoney.account.ui.CurrentAccountCUI;

public class Bootstrap {
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection
					("jdbc:mysql://localhost:3306/bankapp_db", "root", "root");
			PreparedStatement preparedStatement = 
					connection.prepareStatement("DELETE FROM ACCOUNT");
			preparedStatement.execute();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		do
		{
			Scanner scanner = new Scanner(System.in);
			System.out.println("Savings Account Menu");
			System.out.println("Current Account Menu");
			System.out.println("Select Your Choice");
			int number = scanner.nextInt();
			if(number == 1)
					AccountCUI.startSavingsAccount();
			else
				CurrentAccountCUI.startCurrentAccount();
		}while(true);
	}

}
