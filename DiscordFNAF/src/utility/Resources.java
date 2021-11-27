/*
 * Classname: Resources
 * Programmer: Kyle Dryden
 * Version: Java 14 (JDK and JRE), LWJGL 3.2.3
 * Date: 27/11/2021
 * Description: File reading class used for reading files such as the vertex/fragment shaders and the OBJ files.
 */

package utility;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Resources {
	
	//Method for loading resources. The resources loaded currently are shaders.
	public static String loadResource(String fileName) throws Exception {
		String result = "";
		
		try (InputStream input = Resources.class.getResourceAsStream(fileName);
				Scanner scanner = new Scanner(input, java.nio.charset.StandardCharsets.UTF_8.name())) {
			result = scanner.useDelimiter("\\A").next();
		}
		
		return result;
	}
	
	//Method used for loading other types of files. Primarily used for loading OBJ files.
	public static List<String> readAllLines(String fileName) throws Exception {
		List<String> result = new ArrayList<String>();
		
		Scanner fileImport = new Scanner(new File(fileName));
		
		while (fileImport.hasNextLine()) {
			String nextLine = fileImport.nextLine();
			result.add(nextLine);
		}
		
		return result;
	} 
}
