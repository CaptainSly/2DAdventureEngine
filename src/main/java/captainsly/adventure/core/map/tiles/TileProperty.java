package captainsly.adventure.core.map.tiles;

public class TileProperty {

	private String tilePropertyId;
	private Object tilePropertyValue;
	
	public TileProperty(String tilePropertyId, Object tilePropertyValue) {
		this.tilePropertyId = tilePropertyId;
		this.tilePropertyValue = tilePropertyValue;
	}
	
	public String getTilePropertyId() {
		return tilePropertyId;
	}
	
	public Object getTilePropertyValue() {
		return tilePropertyValue;
	}
	
	public void setTilePropertyValue(Object tilePropertyValue) {
		this.tilePropertyValue = tilePropertyValue;
	}
	
}
