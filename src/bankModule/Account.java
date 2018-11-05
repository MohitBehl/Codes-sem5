
public class Account {

	private String name;
	int id;
	private double balance;
	private String date;

	void setId(int id) {
		this.id = id;
	}

	void setName(String name) {
		this.name = name;
	}

	void setBalance(double bal) {
		this.balance = bal;
	}

	void setDate(String date) {
		this.date = date;
	}

	int getId() {
		return id;
	}

	String getName() {
		return name;
	}

	double getBalance() {
		return balance;
	}

	String getDate() {
		return date;
	}
}
