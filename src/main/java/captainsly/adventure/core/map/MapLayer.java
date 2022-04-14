package captainsly.adventure.core.map;

import captainsly.adventure.core.map.tiles.Tile;

public class MapLayer implements Comparable<MapLayer> {

	private int layerWidth, layerHeight;
	private int layerDepth;
	private Tile[][] layerTiles;

	public MapLayer() {
	}

	public MapLayer(int layerWidth, int layerHeight, int layerDepth) {
		this.layerWidth = layerWidth;
		this.layerHeight = layerHeight;
		this.layerDepth = layerDepth;

		layerTiles = new Tile[layerWidth][layerHeight];
	}

	public void setLayerWidth(int layerWidth) {
		this.layerWidth = layerWidth;
	}

	public void setLayerHeight(int layerHeight) {
		this.layerHeight = layerHeight;
	}

	public void setLayerDepth(int layerDepth) {
		this.layerDepth = layerDepth;
	}

	public void setLayerTiles(Tile[][] layerTiles) {
		this.layerTiles = layerTiles;
	}

	public void setTile(int x, int y, Tile tile) {
		layerTiles[x][y] = tile;
	}

	public int getLayerDepth() {
		return layerDepth;
	}

	public int getLayerWidth() {
		return layerWidth;
	}

	public int getLayerHeight() {
		return layerHeight;
	}

	public Tile getTile(int x, int y) {
		return layerTiles[x][y];
	}

	public Tile[][] getLayerTiles() {
		return layerTiles;
	}

	@Override
	public int compareTo(MapLayer layer) {
		return Integer.compare(layerDepth, layer.getLayerDepth());
	}

}
