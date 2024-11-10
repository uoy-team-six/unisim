package io.github.uoyteamsix;

import io.github.uoyteamsix.map.BuildingPrefab;
import io.github.uoyteamsix.map.GameMap;

/**
 * A class which manages the gameplay logic, including the remaining game time, placing buildings, and calculating
 * satisfaction and score.
 */
public class GameLogic {
    private static final float TOTAL_GAME_TIME = 5.0f * 60.0f;
    private static final float BUILDING_TIME = 20.0f;

    private GameMap gameMap;
    private int maximumAllowedBuildings;
    private int selectedPrefabIndex = -1;

    // Timers.
    private float remainingTime;
    private float nextBuildingTime;

    public GameLogic() {
        remainingTime = TOTAL_GAME_TIME;
        nextBuildingTime = 0.0f;
    }

    public void setMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    /**
     * Updates the game logic.
     *
     * @param deltaTime the delta time between the last call of update
     */
    public void update(float deltaTime) {
        remainingTime -= deltaTime;
        nextBuildingTime -= deltaTime;
        if (nextBuildingTime < 0.0f) {
            // User can place another building.
            maximumAllowedBuildings++;
            nextBuildingTime = BUILDING_TIME;
        }
    }

    public void setSelectedPrefabIndex(int prefabIndex) {
        if (gameMap != null && prefabIndex < gameMap.getAvailablePrefabs().size()) {
            selectedPrefabIndex = prefabIndex;
        } else {
            selectedPrefabIndex = -1;
        }
    }

    /**
     * @return true if the player is allowed to place another building
     */
    public boolean canPlaceBuilding() {
        return gameMap.getTotalBuildingCount() < maximumAllowedBuildings;
    }

    public BuildingPrefab getSelectedPrefab() {
        if (gameMap == null || selectedPrefabIndex < 0) {
            return null;
        }
        return gameMap.getAvailablePrefabs().get(selectedPrefabIndex);
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public int getSelectedPrefabIndex() {
        return selectedPrefabIndex;
    }

    public float getRemainingTime() {
        return remainingTime;
    }

    public float getNextBuildingTime() {
        return nextBuildingTime;
    }
}
