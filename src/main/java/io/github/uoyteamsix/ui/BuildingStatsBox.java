package io.github.uoyteamsix.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

public class BuildingStatsBox extends Table {
    private final UiAssets uiAssets;
    private Label accomodationLabel;
    private Label foodLabel;
    private Label learningLabel;
    private Label recreationLabel;
    private Image boxImage;

    public BuildingStatsBox(UiAssets uiAssets) {
        this.uiAssets = uiAssets;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Create labels once fonts have been loaded.
        if (accomodationLabel == null && uiAssets.hasFontsLoaded()) {
            var labelStyle = new Label.LabelStyle(uiAssets.getSmallFont(), Color.BLACK);
            accomodationLabel = new Label("Accomodation: 0", labelStyle);
            foodLabel = new Label("Food: 0", labelStyle);
            learningLabel = new Label("Learning: 0", labelStyle);
            recreationLabel = new Label("Recreation: 0", labelStyle);
        }

        // Create image once spritesheet has been loaded.
        if (boxImage == null && uiAssets.hasSpritesheetLoaded()) {
            var textureRegion = new TextureRegion(uiAssets.getSpritesheet(), 320, 64, 64, 32);
            boxImage = new Image(textureRegion);
        }

        if (getChildren().isEmpty() && accomodationLabel != null && boxImage != null) {
            add(boxImage).size(64.0f * 3.0f, 32.0f * 3.0f);
            row();
            add(accomodationLabel).align(Align.left).padLeft(15.0f).padTop(-165.0f);
            row();
            add(foodLabel).align(Align.left).padLeft(15.0f).padTop(-116.0f);
            row();
            add(learningLabel).align(Align.left).padLeft(15.0f).padTop(-68.0f);
            row();
            add(recreationLabel).align(Align.left).padLeft(15.0f).padTop(-23.0f);
        }
    }
}
