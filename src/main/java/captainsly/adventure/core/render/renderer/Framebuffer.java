package captainsly.adventure.core.render.renderer;

import static org.lwjgl.opengl.GL30.*;

import captainsly.adventure.core.render.Texture;

public class Framebuffer {

	private int fboId = 0;
	private Texture texture = null;

	public Framebuffer(int width, int height) {
		fboId = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, fboId);

		// Create texture to render data to and attach it to our framebuffer
		this.texture = new Texture(width, height);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, this.texture.getTextureId(), 0);

		// Create renderbuffer stores depth info
		int rboId = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, rboId);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32, width, height);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rboId);

		if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
			assert false : "Error: Framebuffer is not complete";

		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}
	
	public void bind() {
		glBindFramebuffer(GL_FRAMEBUFFER, fboId);
	}
	
	public void unbind() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

	public int getFboId() {
		return fboId;
	}

	public int getTextureId() {
		return texture.getTextureId();
	}

}
