package rmi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	static String delimiter;
	private String finalmessage;
	private DataInputStream in;
	private DataOutputStream out;
	private Socket socket;

	public Client() {
		this.in = null;
		this.out = null;
		this.socket = null;
		this.finalmessage = "";
		delimiter = "-";
	}

	Client(String address, int port) throws Exception {
		this();
		socket = new Socket(address, port);
		in = new DataInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());
		System.out.println("connection successful");
	}

	void invoke(String message) throws Exception {
		this.out.writeUTF(message);
		System.out.println("answer: " + this.in.readUTF());
	}

	public static void main(String args[]) {
		Scanner scn = new Scanner(System.in);

		System.out.println("enter ip address of server:");
		String address = scn.next();

		System.out.println("enter listening port of server:");
		int port = scn.nextInt();

		try {
			Client obj = new Client(address, port);

			obj.getInput(new Scanner(System.in));
			// System.out.println(obj.finalmessage);

			obj.invoke(obj.finalmessage);
		} catch (Exception e) {
			System.out.print("error: ");
			e.printStackTrace();
			System.out.println();
		}
	}

	private void getInput(Scanner scn) {
		System.out.println("Enter class name:");
		this.finalmessage += scn.next() + delimiter;

		System.out.println("enter return type of method:");
		this.finalmessage += scn.next() + delimiter;

		System.out.println("Enter name of method:");
		this.finalmessage += scn.next() + delimiter;

		System.out.println("Enter number of parameters of method:");
		int n = scn.nextInt();
		this.finalmessage += n + delimiter;
		while (n > 0) {
			System.out.println("Enter type of argument:");
			String arg = scn.next();

			System.out.println("Enter val of argument:");
			String val = scn.next();

			this.finalmessage += arg + delimiter + val + delimiter;
			n--;
		}
	}
}
