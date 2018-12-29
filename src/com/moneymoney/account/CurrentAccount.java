package com.moneymoney.account;

public class CurrentAccount {
		private double odlimit;
		private BankAccount bankAccount;

		public CurrentAccount(String accountHolderName, double accountBalance, double odlimit) {
			bankAccount = new BankAccount(accountHolderName, accountBalance);
			this.odlimit = odlimit;
		}
		public CurrentAccount(String accountHolderName, double odlimit) {
			bankAccount = new BankAccount(accountHolderName);
			this.odlimit = odlimit;
		}

		public CurrentAccount(int accountNumber, String accountHolderName, double accountBalance, double odlimit) {
			bankAccount = new BankAccount(accountNumber, accountHolderName, accountBalance);
			this.odlimit = odlimit;
		}
		
		/**
		 * @return the odlimit
		 */
		public double getOdlimit() {
			return odlimit;
		}
		/**
		 * @param odlimit the odlimit to set
		 */
		public void setOdlimit(double odlimit) {
			this.odlimit = odlimit;
		}
		/**
		 * @return the bankAccount
		 */
		public BankAccount getBankAccount() {
			return bankAccount;
		}
		/**
		 * @param bankAccount the bankAccount to set
		 */
		public void setBankAccount(BankAccount bankAccount) {
			this.bankAccount = bankAccount;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "CurrentAccount [odlimit=" + odlimit + ", bankAccount="
					+ bankAccount + "]";
		}
		
		
		
	}
