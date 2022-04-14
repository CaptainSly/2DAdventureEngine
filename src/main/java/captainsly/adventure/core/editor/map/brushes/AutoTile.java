package captainsly.adventure.core.editor.map.brushes;

import java.util.HashMap;

import org.joml.Vector2f;

import captainsly.adventure.core.map.tiles.Tile;
import captainsly.adventure.core.render.sprite.SpriteSheet;

/**
 * 
 * The AutoTile Code was adapted from the tIDE Map Editor Program
 * <p>
 * The code is located here:
 * </p>
 * 
 * <a href=
 * "https://github.com/colinvella/tIDE/blob/master/TileMapEditor/tIDE/AutoTiles/AutoTile.cs">Github
 * Link</a>
 * 
 */
public class AutoTile extends Brush {

	private String autoTileId;
	private SpriteSheet tileSheet;
	private int[] indexSet;

	public AutoTile(String autoTileId, SpriteSheet tileSheet, int[] indexSet) throws Exception {
		this.autoTileId = autoTileId;
		this.tileSheet = tileSheet;
		this.indexSet = indexSet;

		if (indexSet.length != 16)
			throw new Exception("AutoTile index set must contain exactly 16 indices");

		this.indexSet = indexSet;
	}

	public boolean isAfflicted(int tileIndex) {

		for (int i : indexSet) {
			if (tileIndex == i)
				return true;
		}

		return false;
	}

	public HashMap<Vector2f, Tile> determineTileAssignments(Vector2f tileLocation, int tileIndex) {
		// Prepare Blank Assignment Lines
		HashMap<Vector2f, Tile> autoTileAssignments = new HashMap<>();

		return autoTileAssignments;
	}

	private int getSetIndex(int tileIndex) {
		for (int setIndex = 0; setIndex < 16; setIndex++) {
			if (indexSet[setIndex] == tileIndex)
				return setIndex;
		}

		return -1;
	}

	private int getSetIndex(Tile tile) {
		if (tile == null)
			return -1;
		if (tile.getTileSheet() != tileSheet)
			return -1;

		return getSetIndex(tile.getTileIndex());
	}

	private boolean[] getCorners(int setIndex) {
		boolean[] corners = new boolean[4];

		corners[0] = (setIndex & 0x01) != 0;
		corners[1] = (setIndex & 0x02) != 0;
		corners[2] = (setIndex & 0x04) != 0;
		corners[3] = (setIndex & 0x08) != 0;

		return corners;
	}

	private int makeSetIndex(boolean[] corners) throws Exception {
		if (corners.length != 4)
			throw new Exception("Corner array must contain exactly four elements");

		int setIndex = 0;

		if (corners[0])
			setIndex |= 0x01;
		if (corners[1])
			setIndex |= 0x02;
		if (corners[2])
			setIndex |= 0x04;
		if (corners[3])
			setIndex |= 0x08;

		return setIndex;
	}

}