/*
 * Classname: ModelLoader
 * Programmer: Kyle Dryden
 * Version: Java 14 (JDK and JRE), LWJGL 3.2.3
 * Date: 1/12/2021
 * Description: 
 */

package objects;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import utility.Resources;

public class ModelLoader {
	public static Mesh loadMesh(String fileName) throws Exception {
		List<String> allLines = Resources.readAllLines(fileName);
		List<Vector3f> vertices = new ArrayList<>();
		List<Vector2f> textures = new ArrayList<>();
		List<Vector3f> normals = new ArrayList<>();
		List<Face> faces = new ArrayList<>();
		
		for (String line: allLines) {
			String[] information = line.split("\\s+");
			
			switch (information[0]) {
			case "v":
				Vector3f newVert = new Vector3f(Float.parseFloat(information[1]), 
					Float.parseFloat(information[2]), 
					Float.parseFloat(information[3]));
				vertices.add(newVert);
				break;
				
			case "vt":
				Vector2f newText = new Vector2f(Float.parseFloat(information[1]),
					Float.parseFloat(information[2]));
				textures.add(newText);
				break;
				
			case "vn":
				Vector3f newNorm = new Vector3f(Float.parseFloat(information[1]),
					Float.parseFloat(information[2]),
					Float.parseFloat(information[3]));
				normals.add(newNorm);
				break;
				
			case "f":
				Face newFace = new Face(information[1], information[2], information[3]);
				faces.add(newFace);
				break;
				
			default:
				break;
			}
		}
		
		return reorderLists(vertices, textures, normals, faces);
	}
	
	private static Mesh reorderLists(List<Vector3f> vertices, List<Vector2f> textures, List<Vector3f> normals, List<Face> faces) {
		List<Integer> indices = new ArrayList<>();
		float[] positionArray = new float[vertices.size() * 3];
		int i = 0;
		for (Vector3f position: vertices) {
			positionArray[i*3] = position.x;
			positionArray[(i*3)+1] = position.y;
			positionArray[(i*3)+2] = position.z;
			i++;
		}
		
		float[] textureArray = new float[vertices.size()*2];
		float[] normalArray = new float[vertices.size()*3];
		
		for (Face face : faces) {
			IdxGroup[] faceVertexIndices = face.getFaceVertexIndices();
			for (IdxGroup indValue: faceVertexIndices) {
				indices.add(indValue.idxPos);
				processFaceTexture(indValue, textures, textureArray);
				processFaceNormal(indValue, normals, normalArray);
			}
		}
		
		int[] indicesArray = new int[indices.size()];
		indicesArray = indices.stream().mapToInt((Integer v) -> v).toArray();
		Mesh mesh = new Mesh(indicesArray);
		mesh.loadVaos(positionArray, textureArray, normalArray);
		return mesh;
		
	}
	
	private static void processFaceTexture(IdxGroup indices, List<Vector2f> textures, float[] textureArray) {
		if (indices.idxTextCoord >= 0) {
			Vector2f textureCoord = textures.get(indices.idxTextCoord);
			textureArray[indices.idxPos*2] = textureCoord.x;
			textureArray[(indices.idxPos*2)+1] = 1 - textureCoord.y;
		}
	}
	
	private static void processFaceNormal(IdxGroup indices,  List<Vector3f> normals, float[] normalArray) {
		if (indices.idxVecNormal >= 0) {
			Vector3f vectNormal = normals.get(indices.idxVecNormal);
			normalArray[indices.idxPos*3] = vectNormal.x;
			normalArray[(indices.idxPos*3)+1] = vectNormal.y;
			normalArray[(indices.idxPos*3)+2] = vectNormal.z;
		}
	}
	
	protected static class IdxGroup {
		public int idxPos;
		public int idxTextCoord;
		public int idxVecNormal;
		
		public IdxGroup() {
			idxPos = -1;
			idxTextCoord = -1;
			idxVecNormal = -1;
		}
	}
	
	protected static class Face {
		private IdxGroup[] idxGroups = new IdxGroup[3];
		
		public Face(String v1, String v2, String v3) {
			idxGroups = new IdxGroup[3];
			idxGroups[0] = parseLine(v1);
			idxGroups[1] = parseLine(v2);
			idxGroups[2] = parseLine(v3);
		}
		
		private IdxGroup parseLine(String line) {
			IdxGroup idxGroup = new IdxGroup();
			
			String[] lineInfo = line.split("/");
			int length = lineInfo.length;
			idxGroup.idxPos = Integer.parseInt(lineInfo[0]) - 1;
			
			if (length > 1) {
				String textCoord = lineInfo[1];
				idxGroup.idxTextCoord  = textCoord.length() > 0 ? Integer.parseInt(textCoord) - 1 : -1;
				
				if (length > 2) {
					idxGroup.idxVecNormal = Integer.parseInt(lineInfo[2]) - 1;
				}
			}
			
			return idxGroup;
		}
		
		public IdxGroup[] getFaceVertexIndices() {
			return idxGroups;
		}
	}
}
