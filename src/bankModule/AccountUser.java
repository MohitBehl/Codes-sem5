
package bankModule;

import java.text.DateFormat;
import java.util.Date;
import java.util.Scanner;

public class AccountUser {
	int acID;
	String name;
	Date date;
	double currBalance;
	private static Scanner scn = new Scanner(System.in);

	AccountUser() {
		System.out.println("enter the name of user:");
		name = scn.nextLine();
		date = new Date();
		System.out.println(date);
	}

	public static void main(String[] args) {
		new AccountUser();
	}
}
