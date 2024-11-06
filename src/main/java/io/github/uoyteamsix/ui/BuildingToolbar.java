package io.github.uoyteamsix.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BuildingToolbar extends Table {
    private final UiAssets uiAssets;
    private final List<Image> iconImages;
    private Skin skin; // Skin for styling the tooltips

    public BuildingToolbar(UiAssets uiAssets) {
        this.uiAssets = uiAssets;
        this.iconImages = new ArrayList<>();

        // Load the default LibGDX skin
        loadDefaultSkin();
    }

    private void loadDefaultSkin() {
        // Load the default skin from assets
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Load the icon images once the icons are loaded
        if (iconImages.isEmpty() && uiAssets.haveIconsLoaded()) {
            Map<String, Texture> iconTextures = uiAssets.getIconTextures();

            // Define the order in which icons should appear and their tooltips
            String[] iconOrder = {"Accom", "Stud", "Cante", "Recreation"};
            String[] tooltips = {"Accommodation", "Study Building", "Canteen", "Recreation Center"};

            for (int i = 0; i < iconOrder.length; i++) {
                String iconName = iconOrder[i];
                Texture texture = iconTextures.get(iconName);
                if (texture != null) {
                    Image iconImage = new Image(texture);

                    // Create a TextTooltip and attach it to the iconImage
                    TextTooltip tooltip = new TextTooltip(tooltips[i], skin);
                    tooltip.setInstant(true); // Optional: show tooltip instantly
                    iconImage.addListener(tooltip);

                    iconImages.add(iconImage);
                }
            }
        }

        // Add the icon images to the toolbar
        if (getChildren().isEmpty() && !iconImages.isEmpty()) {
            for (int i = 0; i < iconImages.size(); i++) {
                var cell = add(iconImages.get(i)).size(64.0f, 64.0f);
                if (i != 0) {
                    cell.padLeft(20.0f);
                }
            }
        }
    }

    // Remove the @Override annotation here
    public void dispose() {
        // Dispose of the skin when no longer needed
        if (skin != null) {
            skin.dispose();
            skin = null;
        }
    }
}
