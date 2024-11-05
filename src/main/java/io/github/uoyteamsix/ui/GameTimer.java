package io.github.uoyteamsix.ui;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

/**
 * A class to represent the game timer UI element.
 */
public class GameTimer extends Table {
    // Multiplier for the background image size.
    // TODO: Make configurable in settings.
    private static final float IMAGE_SCALE = 3.0f;

    private final AssetManager assetManager;
    private final AssetDescriptor<Texture> spritesheetDescriptor;
    private final Label timeLabel;

    // Start timer at 5 minutes.
    private float timeRemaining = 5.0f * 60.0f;

    public GameTimer(AssetManager assetManager, BitmapFont font) {
        this.assetManager = assetManager;
        spritesheetDescriptor = new AssetDescriptor<>("ui/spritesheet.png", Texture.class);
        assetManager.load(spritesheetDescriptor);

        // Create time label.
        var labelStyle = new Label.LabelStyle(font, Color.BLACK);
        timeLabel = new Label("", labelStyle);
        timeLabel.setAlignment(Align.center);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        timeRemaining -= delta;

        // Convert time in seconds to minutes and seconds.
        int minutes = (int) (timeRemaining / 60.0f);
        int seconds = ((int) timeRemaining) % 60;
        timeLabel.setText(String.format("%d:%02d", minutes, seconds));

        // Create image and label once the spritesheet has been loaded.
        if (getChildren().isEmpty() && assetManager.isLoaded(spritesheetDescriptor)) {
            // Creaet background image from the UI spritesheet.
            var texture = assetManager.get(spritesheetDescriptor);
            var textureRegion = new TextureRegion(texture, 288, 128, 64, 32);
            var backgroundImage = new Image(textureRegion);

            // Add the background image and time label to the stack.
            add(backgroundImage).size(textureRegion.getRegionWidth() * IMAGE_SCALE,
                    textureRegion.getRegionHeight() * IMAGE_SCALE);
            row();
            add(timeLabel).align(Align.top).padTop(-48.0f * 1.65f);
        }
    }
}
