/*
 * Classname: Resources
 * Programmer: Kyle Dryden
 * Version: Java 14 (JDK and JRE), LWJGL 3.2.3
 * Date: 29/10/2021
 * Description: File reading class used for reading files such as the vertex/fragment shaders and the OBJ files.
 */

package utility;

import java.io.InputStream;
import java.util.Scanner;

public class Resources {
	
	//Method for loading resources through any file reading format. The resources loaded currently are shaders.
	public static String loadResource(String fileName) throws Exception {
		String result = "";
		
		try (InputStream input = Resources.class.getResourceAsStream(fileName);
				Scanner scanner = new Scanner(input, java.nio.charset.StandardCharsets.UTF_8.name())) {
			result = scanner.useDelimiter("\\A").next();
		}
		
		return result;
	}
}
