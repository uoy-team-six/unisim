package io.github.uoyteamsix.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import io.github.uoyteamsix.SelectedPrefab;

import java.util.ArrayList;
import java.util.List;

public class BuildingStatsBox extends Table {
    private final UiAssets uiAssets;
    private final SelectedPrefab selectedPrefab;
    private final List<Label> labels;
    private Image boxImage;

    public BuildingStatsBox(UiAssets uiAssets, SelectedPrefab selectedPrefab) {
        this.uiAssets = uiAssets;
        this.selectedPrefab = selectedPrefab;
        labels = new ArrayList<>();
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Create labels once fonts have been loaded.
        if (labels.isEmpty() && uiAssets.hasFontsLoaded()) {
            var labelStyle = new Label.LabelStyle(uiAssets.getSmallFont(), Color.BLACK);
            for (int i = 0; i < selectedPrefab.getMap().getAvailablePrefabs().size(); i++) {
                labels.add(new Label("", labelStyle));
            }
        }

        // Create image once spritesheet has been loaded.
        if (boxImage == null && uiAssets.hasSpritesheetLoaded()) {
            var textureRegion = new TextureRegion(uiAssets.getSpritesheet(), 320, 64, 64, 32);
            boxImage = new Image(textureRegion);
        }

        // TODO: This code assumes 4 labels (building types).
        if (getChildren().isEmpty() && !labels.isEmpty() && boxImage != null) {
            add(boxImage).size(64.0f * 3.0f, 32.0f * 3.0f);
            row();
            add(labels.get(0)).align(Align.left).padLeft(12.0f).padTop(-165.0f);
            row();
            add(labels.get(1)).align(Align.left).padLeft(12.0f).padTop(-116.0f);
            row();
            add(labels.get(2)).align(Align.left).padLeft(12.0f).padTop(-68.0f);
            row();
            add(labels.get(3)).align(Align.left).padLeft(12.0f).padTop(-23.0f);
        }

        // Update label text.
        if (!labels.isEmpty()) {
            var map = selectedPrefab.getMap();
            for (int i = 0; i < map.getAvailablePrefabs().size(); i++) {
                var prefab = map.getAvailablePrefabs().get(i);
                var count = map.getBuildingCount(prefab);
                labels.get(i).setText(String.format("%s: %d", prefab.getName(), count));
            }
        }
    }
}
