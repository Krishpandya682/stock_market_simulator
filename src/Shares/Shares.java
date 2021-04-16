package Shares;

import DataManagement.DatabaseReader;
import Exceptions.DoesNotExistException;
import Users.User;

public class Shares {
	private User currUser;
	private int user_id;
	private String companyName;
	private double ltp;
	private int quantity;
	private double quote;

	public Shares(String company, int quantity, User currUser) {
		this.currUser = currUser;
		this.companyName = company;
		this.quantity = quantity;
		this.setLtp();
		this.quote = this.getQuantity() * this.getLtp();
	}

	public Shares(String company, int quantity, int user_idIn) {
		this.user_id = user_idIn;
		this.companyName = company;
		this.quantity = quantity;
		this.setLtp();
		this.quote = this.getQuantity() * this.getLtp();
	}

	public int getQuantity() {
		return this.quantity;
	}

	public double getLtp() {
		return this.ltp;
	}

	public double getQuote() {
		return this.quote;
	}

	public void setLtp() {
		String ltpStr = null;
		try {
			ltpStr = DatabaseReader.getShareInfo(this.companyName)[2];
		} catch (DoesNotExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.ltp = Double.parseDouble(ltpStr);
	}

	public String getCompanyName() {
		return this.companyName;
	}

	public User getCurrUser() {
		return currUser;
	}

	@Override
	public String toString() {
		return "(" + this.getCompanyName() + " " + this.getQuantity() + ")";
	}

	public void setQuantity(int i) {
		this.quantity = i;

	}
}
