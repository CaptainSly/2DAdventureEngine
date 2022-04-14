package captainsly.adventure.core.map.tiles;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import captainsly.adventure.core.entity.GameObject;
import captainsly.adventure.core.render.sprite.SpriteSheet;

public class Tile extends GameObject {

	private Rectangle tileCollisionBox;
	private Map<String, TileProperty> tileProperties;
	private SpriteSheet tileSheet;
	private int tileIndex;

	public Tile() {
		tileProperties = new HashMap<>();

		// Setup The Collision Box for the Tile
		tileCollisionBox = new Rectangle();
	}

	public void setTileIndex(int tileIndex) {
		this.tileIndex = tileIndex;
	}
	
	public int getTileIndex() {
		return tileIndex;
	}
	
	public void setSpriteSheet(SpriteSheet tileSheet) {
		this.tileSheet = tileSheet;
	}

	public SpriteSheet getTileSheet() {
		return tileSheet;
	}
	
	public Rectangle getTileCollisionBox() {
		return tileCollisionBox;
	}

	public TileProperty getTileProperty(String tilePropertyId) {
		return tileProperties.get(tilePropertyId);
	}

	public Map<String, TileProperty> getTileProperties() {
		return tileProperties;
	}

}
