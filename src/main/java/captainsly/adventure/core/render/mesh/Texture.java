package captainsly.adventure.core.render.mesh;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;
import static org.lwjgl.stb.STBImage.stbi_set_flip_vertically_on_load;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.joml.Vector2i;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

public class Texture {

	private final String filePath;
	private final int textureId;
	private final ByteBuffer image;
	private Vector2i imageSize;

	public Texture(String path) {
		this.filePath = path;
		ByteBuffer imageBuffer;
		try {
			imageBuffer = ioResourceToByteBuffer(filePath, 8 * 1024);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		// Generate Texture on GPU
		textureId = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, textureId);

		// Set texture params
		// Repeat image in both directions
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		// When stretching the image, pixelate
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		// when shrinking the image, pixelate
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer channels = BufferUtils.createIntBuffer(1);

		imageSize = new Vector2i(width.get(0), height.get(0));

		stbi_set_flip_vertically_on_load(true);
		image = stbi_load_from_memory(imageBuffer, width, height, channels, 0);

		if (image != null)
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
		else {
			assert false : "Trouble loading image";
		}

		stbi_image_free(image);
	}

	public ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
		ByteBuffer buffer;

		Path path = Paths.get(resource);
		if (Files.isReadable(path)) {
			try (SeekableByteChannel fc = Files.newByteChannel(path)) {
				buffer = BufferUtils.createByteBuffer((int) fc.size() + 1);
				while (fc.read(buffer) != -1) {
					;
				}
			}
		} else {
			try (InputStream source = Texture.class.getClassLoader().getResourceAsStream(resource);
					ReadableByteChannel rbc = Channels.newChannel(source)) {
				buffer = BufferUtils.createByteBuffer(bufferSize);

				while (true) {
					int bytes = rbc.read(buffer);
					if (bytes == -1) {
						break;
					}
					if (buffer.remaining() == 0) {
						buffer = resizeBuffer(buffer, buffer.capacity() * 3 / 2); // 50%
					}
				}
			}
		}

		buffer.flip();
		return MemoryUtil.memSlice(buffer);
	}

	private ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
		ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
		buffer.flip();
		newBuffer.put(buffer);
		return newBuffer;
	}

	public Vector2i getImageSize() {
		return imageSize;
	}

	public void bind() {
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, textureId);
	}

	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}

}
