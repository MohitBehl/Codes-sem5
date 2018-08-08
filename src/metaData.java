import java.io.File;
import java.util.Scanner;

public class metaData {

	public static void main(String[] args) {
		Scanner scn = new Scanner(System.in);
		String path = scn.next();
		File obj = new File(path);
		if (obj.exists()) {
			System.out.println(path + " :exists");
			System.out.println(obj.isDirectory() ? "directory path found" : "file path found");
			if (obj.isDirectory()) {
				System.out.println("Directory Listing:");
				String[] arr = obj.list();
				for (String str : arr) {
					System.out.println(str);
				}

			}
		}
		scn.close();
	}

}
