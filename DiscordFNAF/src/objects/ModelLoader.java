/*
 * Classname: ModelLoader
 * Programmer: Kyle Dryden
 * Version: Java 14 (JDK and JRE), LWJGL 3.2.3
 * Date: 21/06/2022
 * Description: Class used to load the OBJ model files. Uses private classes to create the faces and index groups. Creates
 * separate lists for the vertices (v), the textures (vt), the normals (vn), and the faces (f). When importing, the models
 * need to be triangulated to comply with the current implementation of the model loader.
 */

package objects;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import utility.Resources;

public class ModelLoader {
	
	private static float[] textureArray; //Private variable used to escape the scope of certain functions.
	private static float[] normalArray; //Private variable used to escape the scope of certain functions.
	
	//Method called for each model to load the model from the OBJ file.
	public static Mesh loadMesh(String fileName) throws Exception {
		List<String> allLines = Resources.readAllLines(fileName); //List variable that holds all lines of the OBJ model file.
		
		//List variables for the vertices (v), the textures (vt), the normals (vn), and the faces (f).
		List<Vector3f> vertices = new ArrayList<>();
		List<Vector2f> textures = new ArrayList<>();
		List<Vector3f> normals = new ArrayList<>();
		List<Face> faces = new ArrayList<>();
		
		//For loop that goes through all of the lines in the list for the OBJ file.
		for (String line: allLines) {
			String[] information = line.split("\\s+"); //Splits each line up according to a space or multiple spaces.
			
			//Switch statement for the different types of vertices.
			switch (information[0]) {
			
			//Simply a vertex co-ordinate value. Converted appropriately into a V3.
			case "v":
				Vector3f newVert = new Vector3f(Float.parseFloat(information[1]), 
					Float.parseFloat(information[2]), 
					Float.parseFloat(information[3]));
				vertices.add(newVert);
				break;
			
			//Handles texture vertex co-ordinates for mapping appropriate textures.
			case "vt":
				Vector2f newText = new Vector2f(Float.parseFloat(information[1]),
					Float.parseFloat(information[2]));
				textures.add(newText);
				break;
			
			//Handles the vertex normal for the co-ordinates.	
			case "vn":
				Vector3f newNorm = new Vector3f(Float.parseFloat(information[1]),
					Float.parseFloat(information[2]),
					Float.parseFloat(information[3]));
				normals.add(newNorm);
				break;
			
			//Handles all of the face values in the files. V, VT, and VNs are combined together to create the faces for a mesh.	
			case "f":
				Face newFace = new Face(information[1], information[2], information[3]);
				faces.add(newFace);
				break;
				
			default:
				break;
			}
		}
		
		//Arrays used for the reordered arrays required for the mesh class.
		float[] positionArray = reorderVertices(vertices);
		textureArray = new float[vertices.size()*2];
		normalArray = new float[vertices.size()*3];
		
		Mesh mesh = reorderLists(textures, normals, faces);
		mesh.loadVaos(positionArray, textureArray, normalArray);
		return mesh;
	}
	
	//Private method used to reorder the vertices. Mainly employed to separate the concerns and remove the number of parameters.
	private static float[] reorderVertices(List<Vector3f> vertices) {
		float[] positionArray = new float[vertices.size() * 3];
		int i = 0;
		for (Vector3f position: vertices) {
			positionArray[i*3] = position.x;
			positionArray[(i*3)+1] = position.y;
			positionArray[(i*3)+2] = position.z;
			i++;
		}
		return positionArray;
	}
	
	//Private method used to appropriately reorder the lists (without the ordering, these models would not render properly).
	private static Mesh reorderLists(List<Vector2f> textures, List<Vector3f> normals, List<Face> faces) {
		
		List<Integer> indices = new ArrayList<>(); //Integer list for the indices (reduces the number of vertices required).
		
		//For loop that corresponds to each of the faces to appropriately map the texture and normal to that face.
		for (Face face : faces) {
			IdxGroup[] faceVertexIndices = face.getFaceVertexIndices();
			
			//For loop for each index value (meaning three of them).
			for (IdxGroup indexValue: faceVertexIndices) {
				indices.add(indexValue.idxPos);
				processFaceTexture(indexValue, textures, textureArray); //Method call for processing the texture.
				processFaceNormal(indexValue, normals, normalArray); //Method call for processing the normal.
			}
		}
		
		//Appropriately creates the reordered indices array and sets it according to the stream.
		int[] indicesArray = new int[indices.size()];
		indicesArray = indices.stream().mapToInt((Integer v) -> v).toArray();
		
		//Creates and returns the mesh according to the indices.
		Mesh mesh = new Mesh(indicesArray);
		return mesh;
		
	}
	
	//Private method for processing each faces texture.
	private static void processFaceTexture(IdxGroup indices, List<Vector2f> textures, float[] textureArray) {
		
		//If statemnet that will replace the texture coordinate so long as the index has actually been changed (not -1).
		if (indices.idxTextCoord >= 0) {
			Vector2f textureCoord = textures.get(indices.idxTextCoord);
			textureArray[indices.idxPos*2] = textureCoord.x;
			textureArray[(indices.idxPos*2)+1] = 1 - textureCoord.y;
		}
	}
	
	//Private method for processing each faces normal.
	private static void processFaceNormal(IdxGroup indices,  List<Vector3f> normals, float[] normalArray) {
		
		//If statemnet that will replace the normal vector so long as the index has actually been changed (not -1).
		if (indices.idxVecNormal >= 0) {
			Vector3f vectNormal = normals.get(indices.idxVecNormal);
			normalArray[indices.idxPos*3] = vectNormal.x;
			normalArray[(indices.idxPos*3)+1] = vectNormal.y;
			normalArray[(indices.idxPos*3)+2] = vectNormal.z;
		}
	}
	
	//Inner class for the index group.
	protected static class IdxGroup {
		
		private static final int NO_VALUE = -1; //Current default value for an inactive value.
		
		//Variables for the index vertex position, the texture coordinate, and the vector normal.
		public int idxPos; 
		public int idxTextCoord;
		public int idxVecNormal;
		
		public IdxGroup() {
			idxPos = NO_VALUE;
			idxTextCoord = NO_VALUE;
			idxVecNormal = NO_VALUE;
		}
	}
	
	/* Inner class for the faces of the models. Will be used in conjunction with the textures and the normals. The format
	 * involves three values separated by lines, such as 11/1/1. In the case that two values are filled, such as 11//1,
	 * implies that there is no texture for the face. With a triangulated model, three of these kinds of values are
	 * supplied and stored in the index groups. With these values, the texture and face are created and interpolated to
	 * make a single face. These are then combined to create the complex models.
	 */
	protected static class Face {
		private IdxGroup[] idxGroups = new IdxGroup[3]; //Index group variable for each face (three are supplied for each vertex in a triangle).
		
		//Constructor for each face class.
		public Face(String v1, String v2, String v3) {
			idxGroups = new IdxGroup[3];
			idxGroups[0] = parseLine(v1);
			idxGroups[1] = parseLine(v2);
			idxGroups[2] = parseLine(v3);
		}
		
		//Private method for parsing each line of the faces. Accounts for having missing textures (example: 11//2).
		private IdxGroup parseLine(String line) {
			IdxGroup idxGroup = new IdxGroup(); //Creates one new index group for the indexes of the vertex, texture coord, and normal vector.
			
			String[] lineInfo = line.split("/"); //Variable for splitting up the line.
			int length = lineInfo.length; //Obtains the length of the variable. Used for if a face does not contain a texture.
			idxGroup.idxPos = Integer.parseInt(lineInfo[0]) - 1;
			
			//If the length is found to be one, then only the texture coordinate is given. If the length is two, then a vector normal is also supplied.
			if (length > 1) {
				String textCoord = lineInfo[1];
				idxGroup.idxTextCoord  = textCoord.length() > 0 ? Integer.parseInt(textCoord) - 1 : -1;
				
				if (length > 2) {
					idxGroup.idxVecNormal = Integer.parseInt(lineInfo[2]) - 1;
				}
			}
			
			return idxGroup;
		}
		
		//Returns all of the index group values.
		public IdxGroup[] getFaceVertexIndices() {
			return idxGroups;
		}
	}
}
