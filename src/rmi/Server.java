package rmi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
	private ServerSocket sSocket = null;
	private Socket socket = null;
	private DataInputStream in = null;
	private DataOutputStream out = null;

	Server(int port) throws Exception {
		sSocket = new ServerSocket(port);
		System.out.println("Server Established");

		socket = sSocket.accept();
		System.out.println("Communication successful");

		in = new DataInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());

	}

	void invoke(String message) throws Exception {
		String[] arr = message.split("-");

		String className = arr[0];
		String methodType = arr[1];
		String methodName = arr[2];
		int numOfargs = Integer.parseInt(arr[3]);

		Class<?> anomynous = Class.forName(arr[0]);
		Class<?> parameterTypes[] = null;
		if (numOfargs > 0) {
			parameterTypes = new Class[numOfargs];
			int i = 4;
			int j = 0;
			while (i < arr.length) {
				String type = arr[i];
				if (type.equals("int"))
					parameterTypes[j] = int.class;
				else if (type.equals("double"))
					parameterTypes[j] = double.class;
				else if (type.equals("float"))
					parameterTypes[j] = float.class;
				else if (type.equals("boolean"))
					parameterTypes[j] = boolean.class;
				else if (type.equals("char"))
					parameterTypes[j] = char.class;
				else if (type.equals("String"))
					parameterTypes[j] = String.class;
				else
					throw new Exception("invalid type found");
				i += 2;
				j++;
			}
		}
		Object obj = anomynous.newInstance();
		Method method = obj.getClass().getMethod(methodName, parameterTypes);
		// method calling is made static , will come up with some solution soon
		out.writeUTF(method.invoke(obj, Integer.parseInt(arr[5])) + "");
	}

	public static void main(String args[]) {
		int port;
		Scanner scn = new Scanner(System.in);
		System.out.println("enter post number:");
		port = scn.nextInt();
		try {
			Server server = new Server(port);
			String message = server.in.readUTF();
			System.out.println(message);
			server.invoke(message);
		} catch (Exception e) {
			System.out.print("error: ");
			e.printStackTrace();
			System.out.println();
		}
		scn.close();
	}
}
