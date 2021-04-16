package GUI;

import Users.User;

public class MainProgram {

	private static boolean isLoggedIn;
	private static User loggedInAsUser;
	private int failedAttempt;

	public boolean getIsLoggedIn() {
		return isLoggedIn;
	}

	public void setLoggedIn(boolean isLoggedInIN) {
		MainProgram.isLoggedIn = isLoggedInIN;
		System.out.println("isLoggedIn is set to " + isLoggedIn);

	}

	public User getLoggedInAsUser() {
		return loggedInAsUser;
	}

	public void setLoggedInAsUser(User user) {

		MainProgram.loggedInAsUser = user;

	}

	public int getFailedAttempt() {
		return failedAttempt;
	}

	public void setFailedAttempt(int failedAttempt) {
		this.failedAttempt = failedAttempt;
	}

}
