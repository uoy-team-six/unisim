package io.github.uoyteamsix.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.uoyteamsix.SelectedPrefab;

import java.util.ArrayList;
import java.util.List;

public class BuildingToolbar extends Table {
    private final UiAssets uiAssets;
    private final SelectedPrefab selectedPrefab;
    private final List<Image> backgroundImages;
    private TextureRegion selectionBoxTexture;

    public BuildingToolbar(UiAssets uiAssets, SelectedPrefab selectedPrefab) {
        this.uiAssets = uiAssets;
        this.selectedPrefab = selectedPrefab;
        backgroundImages = new ArrayList<>();
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (backgroundImages.isEmpty() && uiAssets.hasSpritesheetLoaded()) {
            var textureRegion = new TextureRegion(uiAssets.getSpritesheet(), 32, 160, 32, 32);
            for (int i = 0; i < selectedPrefab.getMap().getAvailablePrefabs().size(); i++) {
                final int index = i;
                var image = new Image(textureRegion);
                image.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        selectedPrefab.setIndex(index);
                    }
                });
                backgroundImages.add(image);
            }

            selectionBoxTexture = new TextureRegion(uiAssets.getSpritesheet(), 384, 32, 32, 32);
        }

        if (getChildren().isEmpty() && !backgroundImages.isEmpty()) {
            for (int i = 0; i < backgroundImages.size(); i++) {
                var cell = add(backgroundImages.get(i)).size(64.0f, 64.0f);
                if (i != 0) {
                    cell.padLeft(20.0f);
                }
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        // Draw building icons in each box.
        for (int i = 0; i < backgroundImages.size(); i++) {
            var image = backgroundImages.get(i);
            var coords = image.localToScreenCoordinates(new Vector2(0, 0));

            // Offset slightly.
            coords.add(8.0f, -10.0f);

            var prefab = selectedPrefab.getMap().getAvailablePrefabs().get(i);
            batch.draw(prefab.getNormalTexture(), coords.x, Gdx.graphics.getHeight() - coords.y, 48.0f, 48.0f);
        }

        if (selectedPrefab.getIndex() < 0 || selectionBoxTexture == null) {
            // No selected prefab.
            return;
        }

        // Draw selection image on top of clicked cell.
        var image = backgroundImages.get(selectedPrefab.getIndex());
        var coords = image.localToScreenCoordinates(new Vector2(0, 0));
        batch.draw(selectionBoxTexture, coords.x, Gdx.graphics.getHeight() - coords.y, 64.0f, 64.0f);
    }
}
