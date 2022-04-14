package captainsly.adventure.core.map;

import captainsly.adventure.utils.Settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Map {

	private String mapId;
	private String mapName;
	private int width, height; // Map Size Not in Tiles
	private int widthInTiles, heightInTiles; // Map Size in Tiles

	private List<MapLayer> mapLayers;

	// NoArg Constructor For Serialization
	public Map() {
		mapLayers = new ArrayList<>();
		Collections.sort(mapLayers);

	}

	public Map(String mapId, String mapName, int tileWidth, int tileHeight) {
		this();
		this.mapId = mapId;
		this.mapName = mapName;
		setTileWidth(tileWidth);
		setTileHeight(tileHeight);
	}

	public void addLayer(MapLayer layer) {
		mapLayers.add(layer);
	}

	public void removeLayer(int index) {
		mapLayers.remove(index);
	}

	public void setTileWidth(int tileWidth) {
		this.widthInTiles = tileWidth;
		this.width = tileWidth * Settings.GRID_SIZE;
	}

	public void setTileHeight(int tileHeight) {
		this.heightInTiles = tileHeight;
		this.height = tileHeight * Settings.GRID_SIZE;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public String getMapId() {
		return mapId;
	}

	public String getMapName() {
		return mapName;
	}

	public int getWidthInTiles() {
		return widthInTiles;
	}

	public int getHeightInTiles() {
		return heightInTiles;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public MapLayer getLayer(int index) {
		return mapLayers.get(index);
	}

}
