package io.github.uoyteamsix.ui;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * A class which holds all the UI elements.
 */
public class UiStage extends Stage {
    private final AssetManager assetManager;
    private final AssetDescriptor<FreeTypeFontGenerator> fontDescriptor;
    private final VerticalGroup widgetGroup;
    private BitmapFont bitmapFont;

    public UiStage(AssetManager assetManager) {
        // The UI spans the whole screen.
        super(new ScreenViewport());
        this.assetManager = assetManager;

        // Queue the font loading.
        fontDescriptor = new AssetDescriptor<>("ui/font.ttf", FreeTypeFontGenerator.class);
        assetManager.load(fontDescriptor);

        widgetGroup = new VerticalGroup();
        widgetGroup.setFillParent(true);
        widgetGroup.setPosition(25.0f, -25.0f);
        widgetGroup.align(Align.topLeft);
        addActor(widgetGroup);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Check if the font file has been loaded.
        if (bitmapFont == null && assetManager.isLoaded(fontDescriptor)) {
            // Generate a bitmap font from the truetype font.
            var fontParameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
            fontParameters.size = 72;
            var generator = assetManager.get(fontDescriptor);
            bitmapFont = generator.generateFont(fontParameters);

            // Create and add the game timer.
            var labelStyle = new Label.LabelStyle();
            labelStyle.font = bitmapFont;
            widgetGroup.addActor(new GameTimer(labelStyle));
        }
    }
}
