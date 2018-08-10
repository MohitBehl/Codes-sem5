import java.io.File;
import java.util.Scanner;

public class metaData {

	public static void main(String[] args) {
		Scanner scn = new Scanner(System.in);
		String path = scn.next();
		solve(path);
		scn.close();
	}

	private static void solve(String pathName) {
		File obj = new File(pathName);
		if (obj.exists()) {
			System.out.println(pathName + " :exists");
			if (obj.isDirectory()) {
				System.out.println("directory path found");
				directoryHandler(obj);
			} else {
				System.out.println("file path found");
				fileHandler(obj);
			}
		} else
			System.out.println(pathName + " :doesn't exist");
	}

	private static void directoryHandler(File obj) {
		System.out.println("Directory Listing:");
		String[] arr = obj.list();
		for (String str : arr) {
			System.out.println(str);
		}
		commonHandler(obj);
	}

	private static void fileHandler(File obj) {
		commonHandler(obj);
	}

	private static void commonHandler(File obj) {

	}
}
