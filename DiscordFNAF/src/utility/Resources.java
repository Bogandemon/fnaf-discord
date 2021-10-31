package utility;

import java.io.InputStream;
import java.util.Scanner;

public class Resources {
	public static String loadResource(String fileName) throws Exception {
		String result = "";
		
		try (InputStream input = Resources.class.getResourceAsStream(fileName);
				Scanner scanner = new Scanner(input, java.nio.charset.StandardCharsets.UTF_8.name())) {
			result = scanner.useDelimiter("\\A").next();
		}
		
		return result;
	}
}
