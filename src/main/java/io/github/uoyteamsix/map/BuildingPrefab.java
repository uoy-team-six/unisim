package io.github.uoyteamsix.map;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

/**
 * A class which represents the information about a building.
 */
public class BuildingPrefab {
    private final TiledMapTileLayer tiledLayer;
    private int width;
    private int height;

    public BuildingPrefab(TiledMapTileLayer tiledLayer) {
        this.tiledLayer = tiledLayer;

        // Compute width and height. Assumes building is rectangular.
        for (width = 0; width < tiledLayer.getWidth(); width++) {
            if (tiledLayer.getCell(width, 0) == null) {
                break;
            }
        }
        for (height = 0; height < tiledLayer.getHeight(); height++) {
            if (tiledLayer.getCell(0, height) == null) {
                break;
            }
        }
    }

    /**
     * @return the tiled layer corresponding to this prefab
     */
    public TiledMapTileLayer getTiledLayer() {
        return tiledLayer;
    }

    /**
     * @return the width of the building in tiles
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return the height of the building in tiles
     */
    public int getHeight() {
        return height;
    }
}
