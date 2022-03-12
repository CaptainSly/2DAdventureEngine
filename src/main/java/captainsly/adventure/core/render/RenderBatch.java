package captainsly.adventure.core.render;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector4f;

import captainsly.adventure.Adventure;
import captainsly.adventure.core.AssetPool;
import captainsly.adventure.core.entity.components.SpriteRenderer;
import captainsly.adventure.core.impl.Disposable;
import captainsly.adventure.core.render.shaders.ShaderProgram;

public class RenderBatch implements Disposable, Comparable<RenderBatch> {

	// Vertex
	// ======

	// Pos Color UvCoords TexId
	// Float, Float | Float, Float, Float, Float | Float, Float | Float

	private final int POS_SIZE = 2;
	private final int COLOR_SIZE = 4;
	private final int UV_COORDS_SIZE = 2;
	private final int TEX_ID_SIZE = 1;

	private final int POS_OFFSET = 0;
	private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
	private final int UV_COORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
	private final int TEX_ID_OFFSET = UV_COORDS_OFFSET + UV_COORDS_SIZE * Float.BYTES;

	private final int VERTEX_SIZE = 9;
	private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

	private SpriteRenderer[] sprites;
	private List<Texture> batchTextures;
	private int[] textureSlots = { 0, 1, 2, 3, 4, 5, 6, 7 };
	private int numSprites;
	private boolean hasRoom;
	private float[] vertices;
	private int zIndex;

	private int vaoId, vboId;
	private int maxBatchSize;
	private ShaderProgram shader;

	public RenderBatch(int maxBatchSize, int zIndex) {
		// Create Shader
		shader = AssetPool.getShader("defaultShader");
		try {
			shader.addUniform("uProjectionMatrix");
			shader.addUniform("uViewMatrix");
			shader.addUniform("uTextures");
		} catch (Exception e) {
			e.printStackTrace();
		}

		sprites = new SpriteRenderer[maxBatchSize];
		this.maxBatchSize = maxBatchSize;
		this.zIndex = zIndex;
		numSprites = 0;
		hasRoom = true;

		batchTextures = new ArrayList<>();

		// 4 Vertices | Quads
		vertices = new float[this.maxBatchSize * 4 * VERTEX_SIZE];
	}

	public void start() {
		// Generate and Bind a Vertex Array Object
		vaoId = glGenVertexArrays();
		glBindVertexArray(vaoId);

		// Allocate Space for Vertices
		vboId = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboId);
		glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

		// Create and Upload Indices Buffer
		int eboId = glGenBuffers();
		int[] indices = generateIndices();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

		// Enable the Buffer Attribute Pointers
		glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
		glEnableVertexAttribArray(0);

		glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
		glEnableVertexAttribArray(1);

		glVertexAttribPointer(2, UV_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, UV_COORDS_OFFSET);
		glEnableVertexAttribArray(2);

		glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
		glEnableVertexAttribArray(3);

	}

	public void render() {
		boolean rebufferData = false;

		for (int i = 0; i < numSprites; i++) {
			SpriteRenderer sprite = sprites[i];
			if (sprite.isDirty()) {
				loadVertexProperties(i);
				sprite.clean();
				rebufferData = true;
			}
		}
		
		if (rebufferData) {
			// For now, rebuffer all data every frame
			glBindBuffer(GL_ARRAY_BUFFER, vboId);
			glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
		}
		
		// Bind Shader and Set Uniforms
		shader.bind();
		shader.setUniform("uProjectionMatrix", Adventure.currentScene.getSceneCamera().getProjectionMatrix());
		shader.setUniform("uViewMatrix", Adventure.currentScene.getSceneCamera().getViewMatrix());

		for (int i = 0; i < batchTextures.size(); i++) {
			glActiveTexture(GL_TEXTURE0 + (i + 1));
			batchTextures.get(i).bind();
		}

		shader.setUniform("uTextures", textureSlots);

		glBindVertexArray(vaoId);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);

		glDrawElements(GL_TRIANGLES, numSprites * 6, GL_UNSIGNED_INT, 0);

		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);

		for (int i = 0; i < batchTextures.size(); i++) {
			batchTextures.get(i).unbind();
		}

		shader.unbind();
	}

	public void addSprite(SpriteRenderer sprite) {
		int index = numSprites;
		sprites[index] = sprite;
		numSprites++;

		Texture spriteTexture = sprite.getTexture();
		if (spriteTexture != null) {
			if (!batchTextures.contains(spriteTexture))
				batchTextures.add(spriteTexture);
		}

		loadVertexProperties(index);

		if (numSprites >= maxBatchSize)
			hasRoom = false;
	}

	public boolean hasRoom() {
		return hasRoom;
	}

	private void loadVertexProperties(int index) {
		SpriteRenderer sprite = sprites[index];
		int offset = index * 4 * VERTEX_SIZE;

		Vector4f color = sprite.getSpriteColor();
		Vector2f[] uvCoords = sprite.getTextureCoords();

		int texId = 0;
		if (sprite.getTexture() != null) {
			for (int i = 0; i < batchTextures.size(); i++) {
				if (batchTextures.get(i) == sprite.getTexture()) {
					texId = i + 1;
					break;
				}
			}
		}

		float xAdd = 1.0f;
		float yAdd = 1.0f;

		for (int i = 0; i < 4; i++) {
			if (i == 1)
				yAdd = 0.0f;
			else if (i == 2)
				xAdd = 0.0f;
			else if (i == 3)
				yAdd = 1.0f;

			// Load Vertices
			vertices[offset] = sprite.gameObject.getObjectTransform().position.x
					+ (xAdd * sprite.gameObject.getObjectTransform().scale.x);
			vertices[offset + 1] = sprite.gameObject.getObjectTransform().position.y
					+ (yAdd * sprite.gameObject.getObjectTransform().scale.y);

			// Load Color
			vertices[offset + 2] = color.x;
			vertices[offset + 3] = color.y;
			vertices[offset + 4] = color.z;
			vertices[offset + 5] = color.w;

			// UV Coordinates
			vertices[offset + 6] = uvCoords[i].x;
			vertices[offset + 7] = uvCoords[i].y;

			// Texture Id
			vertices[offset + 8] = texId;

			offset += VERTEX_SIZE;
		}

	}

	private int[] generateIndices() {
		// 6 indices per quad (3 per triangle)
		int[] elements = new int[6 * maxBatchSize];
		for (int i = 0; i < maxBatchSize; i++) {
			loadElementIndices(elements, i);

		}

		return elements;
	}

	private void loadElementIndices(int[] elements, int index) {
		int offsetArrayIndex = 6 * index;
		int offset = 4 * index;

		// 3, 2, 0, 0, 2, 1 7, 6, 4, 4, 6, 5
		// Triangle 1
		elements[offsetArrayIndex] = offset + 3;
		elements[offsetArrayIndex + 1] = offset + 2;
		elements[offsetArrayIndex + 2] = offset + 0;

		// Triangle 2
		elements[offsetArrayIndex + 3] = offset + 0;
		elements[offsetArrayIndex + 4] = offset + 2;
		elements[offsetArrayIndex + 5] = offset + 1;
	}

	public boolean hasTextureRoom() {
		return batchTextures.size() < 8;
	}

	public boolean hasTexture(Texture texture) {
		return batchTextures.contains(texture);
	}
	
	public int getZIndex() {
		return zIndex;
	}

	@Override
	public void onDispose() {

	}

	@Override
	public int compareTo(RenderBatch o) {
		return Integer.compare(this.getZIndex(), o.getZIndex());
	}

}
