package io.github.uoyteamsix.map;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

/**
 * A class to represent the playable game map. Holds the underlying tiled map and keeps track
 * of placed buildings.
 */
public class GameMap {
    private final TiledMap tiledMap;
    private final TiledMapTileLayer buildingLayer;

    private final int width;
    private final int height;
    private final int widthPx;
    private final int heightPx;
    private final int tileWidthPx;
    private final int tileHeightPx;

    public GameMap(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
        buildingLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Buildings");

        width = buildingLayer.getWidth();
        height = buildingLayer.getHeight();
        tileWidthPx = buildingLayer.getTileWidth();
        tileHeightPx = buildingLayer.getTileHeight();
        widthPx = width * tileWidthPx;
        heightPx = height * tileHeightPx;
    }

    public void constructBuilding(int x, int y) {
        var prefabLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Prefab: House");
        for (int cellX = 0; cellX < prefabLayer.getWidth(); cellX++) {
            for (int cellY = 0; cellY < prefabLayer.getHeight(); cellY++) {
                var cell = prefabLayer.getCell(cellX, cellY);
                if (cell != null) {
                    buildingLayer.setCell(x + cellX, y + cellY, cell);
                }
            }
        }
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    /**
     * @return the width of the map in tiles
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return the height of the map in tiles
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return the width of the map in pixels
     */
    public int getWidthPx() {
        return widthPx;
    }

    /**
     * @return the height of the map in pixels
     */
    public int getHeightPx() {
        return heightPx;
    }

    /**
     * @return the width of a single map tile in pixels
     */
    public int getTileWidthPx() {
        return tileWidthPx;
    }

    /**
     * @return the height of a single map tile in pixels
     */
    public int getTileHeightPx() {
        return tileHeightPx;
    }
}
