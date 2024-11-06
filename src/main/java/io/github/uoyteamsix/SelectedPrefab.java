package io.github.uoyteamsix;

import io.github.uoyteamsix.map.BuildingPrefab;
import io.github.uoyteamsix.map.GameMap;

/**
 * A class which holds an index to the currently selected building prefab so that both GameMapInput and
 * BuildingToolbar can modify and access it.
 */
public class SelectedPrefab {
    private GameMap map;
    private int index = -1;

    public void setMap(GameMap map) {
        this.map = map;
    }

    public void setIndex(int index) {
        if (map != null && index < map.getAvailablePrefabs().size()) {
            this.index = index;
        } else {
            this.index = -1;
        }
    }

    public BuildingPrefab getPrefab() {
        if (map == null || index < 0) {
            return null;
        }
        return map.getAvailablePrefabs().get(index);
    }

    public GameMap getMap() {
        return map;
    }

    public int getIndex() {
        return index;
    }
}
