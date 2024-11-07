package io.github.uoyteamsix.map;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;
import io.github.uoyteamsix.CameraController;
import io.github.uoyteamsix.SelectedPrefab;

/**
 * A class which handles user input events on the game map.
 */
public class GameMapInput extends InputAdapter {
    private final GameMap map;
    private final CameraController cameraController;
    private final SelectedPrefab selectedPrefab;
    private int selectedTileX = -1;
    private int selectedTileY = -1;

    public GameMapInput(GameMap map, CameraController cameraController, SelectedPrefab selectedPrefab) {
        this.map = map;
        this.cameraController = cameraController;
        this.selectedPrefab = selectedPrefab;
    }

    @Override
    public boolean keyDown(int keycode) {
        // Allow deselecting the current prefab either by pressing escape, pressing a number out of range, or pressing
        // the same key again.
        if (keycode == Input.Keys.ESCAPE) {
            selectedPrefab.setIndex(-1);
            return true;
        }
        if (keycode >= Input.Keys.NUM_1 && keycode <= Input.Keys.NUM_9) {
            int index = keycode - Input.Keys.NUM_1;
            if (index == selectedPrefab.getIndex()) {
                selectedPrefab.setIndex(-1);
            } else {
                selectedPrefab.setIndex(index);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        Vector3 worldCoordinates = cameraController.getCamera().unproject(new Vector3(screenX, screenY, 0));
        selectedTileX = (int) (worldCoordinates.x / map.getTileWidthPx());
        selectedTileY = (int) (worldCoordinates.y / map.getTileHeightPx());

        // Set value to -1 if outside the map bounds.
        if (selectedTileX < 0 || selectedTileX >= map.getWidth()) {
            selectedTileX = -1;
        }
        if (selectedTileY < 0 || selectedTileY >= map.getHeight()) {
            selectedTileY = -1;
        }
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        var prefab = selectedPrefab.getPrefab();
        if (button == Input.Buttons.LEFT && selectedTileX >= 0 && selectedTileY >= 0 && prefab != null) {
            int placementX = getPlacementTileX();
            int placementY = getPlacementTileY();
            if (map.canPlaceBuilding(prefab, placementX, placementY)) {
                map.placeBuilding(prefab, placementX, placementY);
            }
        }
        return true;
    }

    public int getSelectedTileX() {
        return selectedTileX;
    }

    public int getSelectedTileY() {
        return selectedTileY;
    }

    public int getPlacementTileX() {
        var prefab = selectedPrefab.getPrefab();
        if (prefab == null) {
            return selectedTileX;
        }
        return selectedTileX - prefab.getWidth() / 2;
    }

    public int getPlacementTileY() {
        var prefab = selectedPrefab.getPrefab();
        if (prefab == null) {
            return selectedTileY;
        }
        return selectedTileY - prefab.getHeight() / 2;
    }
}
