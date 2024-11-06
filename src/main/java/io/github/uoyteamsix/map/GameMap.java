package io.github.uoyteamsix.map;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.HashMap;
import java.util.Map;

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

    private final boolean[][] usableTiles;
    private final Map<String, BuildingPrefab> availablePrefabs;

    public GameMap(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
        buildingLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Buildings");

        width = buildingLayer.getWidth();
        height = buildingLayer.getHeight();
        tileWidthPx = buildingLayer.getTileWidth();
        tileHeightPx = buildingLayer.getTileHeight();
        widthPx = width * tileWidthPx;
        heightPx = height * tileHeightPx;

        // Compute which tiles are allowed to be placed on.
        usableTiles = new boolean[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                usableTiles[x][y] = true;
                for (var layer : tiledMap.getLayers()) {
                    if (layer.getName().equals("Terrain")) {
                        continue;
                    }
                    if (((TiledMapTileLayer) layer).getCell(x, y) != null) {
                        usableTiles[x][y] = false;
                    }
                }
            }
        }

        // Create building types for each prefab layer in the map.
        availablePrefabs = new HashMap<>();
        for (var layer : tiledMap.getLayers()) {
            if (layer.getName().startsWith("Prefab: ")) {
                // Extract prefab name, e.g. Accomodation.
                var prefabName = layer.getName().substring("Prefab: ".length());
                availablePrefabs.put(prefabName, new BuildingPrefab((TiledMapTileLayer) layer));
            }
        }

        // Generate textures for each building prefab.
        var offlineBuildingRenderer = new OfflineBuildingRenderer(this);
        for (var prefab : availablePrefabs.values()) {
            prefab.generateTextures(offlineBuildingRenderer);
        }
    }

    /**
     * Checks whether a building prefab can be placed at the given coordinates.
     *
     * @param prefab the building prefab
     * @param x      the x coordinate in world space
     * @param y      the y coordinate in world space
     * @return true if the building can be placed, false otherwise
     */
    public boolean canPlaceBuilding(BuildingPrefab prefab, int x, int y) {
        for (int prefabX = 0; prefabX < prefab.getWidth(); prefabX++) {
            for (int prefabY = 0; prefabY < prefab.getHeight(); prefabY++) {
                int mapX = x + prefabX;
                int mapY = y + prefabY;

                // Check out of bounds.
                if (mapX < 0 || mapY < 0 || mapX >= width || mapY >= height) {
                    return false;
                }

                // Check if on top of a disallowed tile.
                if (!usableTiles[mapX][mapY]) {
                    return false;
                }
            }
        }
        return true;
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
