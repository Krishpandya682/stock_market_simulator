package Users;

import java.math.BigDecimal;
import java.util.ArrayList;

import Exceptions.NotEnoughCapitalException;
import Exceptions.SellingMoreThanYouHaveException;
import Exceptions.ThisShareIsNotOwnedException;
import Shares.Shares;
import TempDriver.UsersDatabaseGenerator;

public class User {

	private String name;
	private String emailID;
	private int age;
	private BigDecimal capital;
	private ArrayList<Shares> shares;

	private BigDecimal capitalAsShares;
	private String password;
	private BigDecimal BEGINNING_CAPITAL = new BigDecimal(100000);
	int uid = UsersDatabaseGenerator.userIDGetter(this.getName());

	public User(String nameIn, String pwdIn, String emailIDIN, int ageIn,
			ArrayList<Shares> sharesIn) {
		this.name = nameIn;
		this.age = ageIn;
		this.emailID = emailIDIN;
		this.capital = BEGINNING_CAPITAL;
		this.setShares(sharesIn);
		this.setCapitalAsShares();
		this.password = pwdIn;
	}

	public User(String nameIn, String pwdIn, String emailIDIN, int ageIn,
			double capIn, double capAsSharesIn, ArrayList<Shares> sharesIn) {
		BigDecimal capInBD = new BigDecimal(capIn);
		this.name = nameIn;
		this.age = ageIn;
		this.emailID = emailIDIN;
		this.capital = capInBD;
		this.setShares(sharesIn);
		this.setCapitalAsShares();
		this.password = pwdIn;
	}

	public String getName() {
		return name;
	}

	public String getPwd() {
		return password;
	}

	public int getAge() {
		return age;
	}

	public double getCapital() {

		return capital.doubleValue();
	}

	public ArrayList<Shares> getShares() {
		return shares;
	}

	public double getCapitalAsShares() {
		return capitalAsShares.doubleValue();
	}

	public void setShares(ArrayList<Shares> shares) {
		this.shares = shares;
	}

	public void setCapitalAsShares() {

		ArrayList<Shares> s = new ArrayList<Shares>();
		s = SharesDatabaseGenerator.returnSharesOfUser(this.getName());
		System.out.println(s);
		double sum = 0.0;

		for (int i = 0; i < s.size(); i++) {
			sum += (s.get(i).getLtp() * s.get(i).getQuantity());
		}
		this.capitalAsShares = new BigDecimal(sum);
	}

	public boolean buy(String company, int quantity)
			throws NotEnoughCapitalException {
		System.out.println("Buy=>" + this.getShares().toString());

		Shares tempBuyShare = new Shares(company, quantity, this);
		System.out.println(" ->" + tempBuyShare.getQuote());
		System.out.println(" ->" + this.getCapital());

		if (tempBuyShare.getQuote() > this.getCapital()) {
			throw new NotEnoughCapitalException("Not enough capital");

		}
		int initQuant = 0;
		for (Shares s : this.getShares()) {
			if (s.getCompanyName().equals(company)) {
				initQuant = s.getQuantity();
			}
		}
		Shares buyShare = new Shares(company, initQuant + quantity, this);
		if (this.getShareOfCompany(company) != null) {
			int index = this.getIndexOfShare(company);
			this.shares.get(index).setQuantity(initQuant + quantity);
		} else {

			this.shares.add(buyShare);
		}
		this.capital = new BigDecimal(
				this.capital.subtract(new BigDecimal(tempBuyShare.getQuote()))
						.doubleValue());
		this.capitalAsShares = new BigDecimal(this.capitalAsShares
				.add(new BigDecimal(tempBuyShare.getQuote())).doubleValue());

		SharesDatabaseGenerator.sharesDatabaseUpdater(buyShare);
		UsersDatabaseGenerator.userDatabasePopulator(this);
		TransactionHistoryDatabaseGenerator.transactionHistoryDatabaseUpdater(
				true, quantity, buyShare, this);
		return true;
	}

	public int getIndexOfShare(String companyName) {

		for (int i = 0; i < this.shares.size(); i++) {
			Shares s = this.shares.get(i);
			if (s.getCompanyName().equals(companyName)) {
				return i;
			}
		}
		return -1;
	}

	public Shares getShareOfCompany(String companyName) {
		ArrayList<Shares> initShareList = SharesDatabaseGenerator
				.returnSharesOfUser(this.getName());
		System.out.println("initShareList= " + initShareList);
		for (Shares s : initShareList) {
			if (s.getCompanyName().equals(companyName) && s.getQuantity() > 0) {
				return s;
			}
		}
		return null;
	}

	public boolean sell(String company, int quantity)
			throws ThisShareIsNotOwnedException,
			SellingMoreThanYouHaveException {
		System.out.println("Sell=>" + this.getShares().toString());

		Shares initShare = getShareOfCompany(company);
		if (initShare == null) {
			throw new ThisShareIsNotOwnedException(
					"You do not own the share you are trying to sell");
		}
		int initQuant = initShare.getQuantity();

		System.out.println("initShare=" + initShare);
		int index = this.getIndexOfShare(company);

		Shares tempSellShare = new Shares(company, quantity, this);
		Shares sellShare = new Shares(company, initQuant - quantity, this);
		if (initShare.getQuantity() == quantity) {
			this.shares.remove(index);

		} else if (initShare.getQuantity() > quantity) {
			System.out.println("Sell=>1");
			this.shares.remove(index);
			this.shares.add(sellShare);
		} else {

			throw new SellingMoreThanYouHaveException(
					"You are trying to sell more shares than you own");
		}
		this.capital = new BigDecimal(this.capital
				.add(new BigDecimal(tempSellShare.getQuote())).doubleValue());
		this.capitalAsShares = new BigDecimal(this.capitalAsShares
				.subtract(new BigDecimal(tempSellShare.getQuote()))
				.doubleValue());
		System.out.println("****" + this.getCapital());

		SharesDatabaseGenerator.sharesDatabaseUpdater(sellShare);
		UsersDatabaseGenerator.userDatabasePopulator(this);
		TransactionHistoryDatabaseGenerator.transactionHistoryDatabaseUpdater(
				false, quantity, sellShare, this);
		return true;
	}

	public String getEmail() {
		return this.emailID;
	}

}
