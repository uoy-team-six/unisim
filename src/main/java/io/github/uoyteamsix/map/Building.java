package io.github.uoyteamsix.map;

/**
 * A class representing a building placed on the map.
 */
public class Building {
    private final BuildingPrefab prefab;
    private final int x;
    private final int y;

    public Building(BuildingPrefab prefab, int x, int y) {
        this.prefab = prefab;
        this.x = x;
        this.y = y;
    }

    public BuildingPrefab getPrefab() {
        return prefab;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
