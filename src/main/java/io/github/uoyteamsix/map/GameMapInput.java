package io.github.uoyteamsix.map;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;
import io.github.uoyteamsix.CameraController;

/**
 * A class which handles user input events on the game map.
 */
public class GameMapInput extends InputAdapter {
    private final GameMap map;
    private final CameraController cameraController;
    private int selectedTileX = -1;
    private int selectedTileY = -1;

    public GameMapInput(GameMap map, CameraController cameraController) {
        this.map = map;
        this.cameraController = cameraController;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        Vector3 worldCoordinates = cameraController.getCamera().unproject(new Vector3(screenX, screenY, 0));
        selectedTileX = (int) (worldCoordinates.x / map.getTileWidthPx());
        selectedTileY = (int) (worldCoordinates.y / map.getTileHeightPx());

        // Set value to -1 if outside of the map bounds.
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
        if (button == Input.Buttons.LEFT && selectedTileX >= 0 && selectedTileY >= 0) {
            map.constructBuilding(selectedTileX, selectedTileY);
        }
        return true;
    }

    public int getSelectedTileX() {
        return selectedTileX;
    }

    public int getSelectedTileY() {
        return selectedTileY;
    }
}
