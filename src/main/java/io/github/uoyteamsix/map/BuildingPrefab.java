package io.github.uoyteamsix.map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

/**
 * A class which represents the information about a building.
 */
public class BuildingPrefab {
    private final TiledMapTileLayer tiledLayer;
    private int width;
    private int height;
    private TextureRegion transparentTexture;
    private TextureRegion redTexture;

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
     * Generate transparent and red textures for this building.
     *
     * @param renderer an instance of an OfflineBuildingRenderer
     */
    public void generateTextures(OfflineBuildingRenderer renderer) {
        transparentTexture = renderer.render(this, new Color(1.0f, 1.0f, 1.0f, 0.8f));
        redTexture = renderer.render(this, new Color(1.0f, 0.1f, 0.1f, 0.8f));
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

    public TextureRegion getTransparentTexture() {
        return transparentTexture;
    }

    public TextureRegion getRedTexture() {
        return redTexture;
    }
}
