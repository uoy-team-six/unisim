package io.github.uoyteamsix.ui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * A class which holds all the UI elements.
 */
public class UiStage extends Stage {
    private final UiAssets assets;
    private BitmapFont bitmapFont;

    public UiStage(AssetManager assetManager) {
        // The UI spans the whole screen.
        super(new ScreenViewport());
        assets = new UiAssets(assetManager);

        // Create widget group.
        var widgetGroup = new VerticalGroup();
        widgetGroup.setFillParent(true);
        widgetGroup.setPosition(25.0f, -25.0f);
        widgetGroup.align(Align.topLeft);
        addActor(widgetGroup);

        // Create all of our UI widgets.
        widgetGroup.addActor(new GameTimer(assets));
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        assets.update();
    }
}
