package io.github.uoyteamsix.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;
import java.util.List;

public class BuildingToolbar extends Table {
    private final UiAssets uiAssets;
    private final List<Image> backgroundImages;

    public BuildingToolbar(UiAssets uiAssets) {
        this.uiAssets = uiAssets;
        backgroundImages = new ArrayList<>();
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (backgroundImages.isEmpty() && uiAssets.hasSpritesheetLoaded()) {
            var textureRegion = new TextureRegion(uiAssets.getSpritesheet(), 32, 160, 32, 32);
            for (int i = 0; i < 5; i++) {
                backgroundImages.add(new Image(textureRegion));
            }
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
}
