package io.github.uoyteamsix.ui;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.HashMap;
import java.util.Map;

public class UiAssets {
    private final AssetManager assetManager;
    private final AssetDescriptor<FreeTypeFontGenerator> fontDescriptor;
    private final AssetDescriptor<Texture> spritesheetDescriptor;

    // Map to store icon descriptors and textures
    private final Map<String, AssetDescriptor<Texture>> iconDescriptors = new HashMap<>();
    private final Map<String, Texture> iconTextures = new HashMap<>();

    private BitmapFont largeFont;
    private BitmapFont smallFont;
    private Texture spritesheet;

    public UiAssets(AssetManager assetManager) {
        this.assetManager = assetManager;

        // Queue the font loading
        fontDescriptor = new AssetDescriptor<>("ui/font.ttf", FreeTypeFontGenerator.class);
        assetManager.load(fontDescriptor);

        // Queue the spritesheet loading
        spritesheetDescriptor = new AssetDescriptor<>("ui/spritesheet.png", Texture.class);
        assetManager.load(spritesheetDescriptor);

        // Load the icon textures
        loadIconTextures();
    }

    private void loadIconTextures() {
        // The icons are named Accom.png, Stud.png, Cante.png, Recreation.png in the "buildings/" directory
        String[] iconNames = {"Accom", "Stud", "Cante", "Recreation"};
        for (String iconName : iconNames) {
            String filePath = "buildings/" + iconName + ".png";
            AssetDescriptor<Texture> iconDescriptor = new AssetDescriptor<>(filePath, Texture.class);
            iconDescriptors.put(iconName, iconDescriptor);
            assetManager.load(iconDescriptor);
        }
    }

    private BitmapFont createFont(FreeTypeFontGenerator generator, int size) {
        var parameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameters.size = size;
        return generator.generateFont(parameters);
    }

    public void update() {
        // Check if the font file has been loaded
        if (largeFont == null && assetManager.isLoaded(fontDescriptor)) {
            // Generate bitmap fonts from the TrueType font
            var generator = assetManager.get(fontDescriptor);
            largeFont = createFont(generator, 72);
            smallFont = createFont(generator, 32);
        }

        // Check if the spritesheet has been loaded
        if (spritesheet == null && assetManager.isLoaded(spritesheetDescriptor)) {
            spritesheet = assetManager.get(spritesheetDescriptor);
        }

        // Check if the icon textures have been loaded
        if (iconTextures.size() < iconDescriptors.size()) {
            for (Map.Entry<String, AssetDescriptor<Texture>> entry : iconDescriptors.entrySet()) {
                String iconName = entry.getKey();
                AssetDescriptor<Texture> iconDescriptor = entry.getValue();
                if (!iconTextures.containsKey(iconName) && assetManager.isLoaded(iconDescriptor)) {
                    iconTextures.put(iconName, assetManager.get(iconDescriptor));
                }
            }
        }
    }

    public boolean hasFontsLoaded() {
        return largeFont != null;
    }

    public boolean hasSpritesheetLoaded() {
        return spritesheet != null;
    }

    // New method to check if all icons are loaded
    public boolean haveIconsLoaded() {
        return iconTextures.size() == iconDescriptors.size();
    }

    public BitmapFont getLargeFont() {
        return largeFont;
    }

    public BitmapFont getSmallFont() {
        return smallFont;
    }

    public Texture getSpritesheet() {
        return spritesheet;
    }

    // New method to get icon textures
    public Map<String, Texture> getIconTextures() {
        return iconTextures;
    }
}
