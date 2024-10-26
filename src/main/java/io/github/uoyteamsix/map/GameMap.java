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

    private final int tileWidthPx;
    private final int tileHeightPx;
    private final int mapWidthPx;
    private final int mapHeightPx;

    public GameMap(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
        buildingLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Buildings");

        // Calculate map width and height in pixels.
        var props = tiledMap.getProperties();
        tileWidthPx = props.get("tilewidth", Integer.class);
        tileHeightPx = props.get("tileheight", Integer.class);
        mapWidthPx = tileWidthPx * props.get("width", Integer.class);
        mapHeightPx = tileHeightPx * props.get("height", Integer.class);
    }

    public void constructBuilding(int x, int y) {
        var prefabLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Prefab: House");
        for (int cellX = 0; cellX < prefabLayer.getWidth(); cellX++) {
            for (int cellY = 0; cellY < prefabLayer.getHeight(); cellY++) {
                var cell = prefabLayer.getCell(cellX, cellY);
                if (cell != null) {
                    buildingLayer.setCell(x + cellX, y - (prefabLayer.getHeight() - cellY) + 3, cell);
                }
            }
        }
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public int getTileWidthPx() {
        return tileWidthPx;
    }

    public int getTileHeightPx() {
        return tileHeightPx;
    }

    public int getMapWidthPx() {
        return mapWidthPx;
    }

    public int getMapHeightPx() {
        return mapHeightPx;
    }
}
