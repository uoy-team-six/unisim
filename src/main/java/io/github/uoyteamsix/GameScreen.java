package io.github.uoyteamsix;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * A class representing the main gameplay screen.
 */
public class GameScreen extends ScreenAdapter {
    private final AssetManager assetManager;
    private final CursorManager cursorManager;
    private final SpriteBatch batch;
    private final CameraController cameraController;
    private MapRenderer mapRenderer;

    public GameScreen(AssetManager assetManager, CursorManager cursorManager) {
        this.assetManager = assetManager;
        this.cursorManager = cursorManager;
        batch = new SpriteBatch();
        cameraController = new CameraController();

        // Create an input multiplexer to chain together our input adapters. For now, we only have the camera.
        var inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(cameraController);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void resize(int width, int height) {
        cameraController.resizeViewport(width, height);
    }

    @Override
    public void render(float deltaTime) {
        // Clear the screen with a solid color (black).
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1.0f);

        // Create map renderer if first run.
        if (mapRenderer == null) {
            initializeMapRenderer();
        }

        // Set cursor based on camera behavior.
        updateCursorState();

        // Update camera.
        cameraController.update(deltaTime);

        // Render the map.
        mapRenderer.setView(cameraController.getCamera());
        mapRenderer.render();
    }

    /**
     * Initializes the map renderer and sets up the camera position.
     */
    private void initializeMapRenderer() {
        try {
            var map = assetManager.get("maps/Map.tmx", TiledMap.class);
            mapRenderer = new OrthogonalTiledMapRenderer(map, batch);

            // Calculate the width and height of the map in pixels and use that to center the camera on the map.
            var props = map.getProperties();
            int widthPx = props.get("width", Integer.class) * props.get("tilewidth", Integer.class);
            int heightPx = props.get("height", Integer.class) * props.get("tileheight", Integer.class);
            cameraController.getCamera().position.set(widthPx / 2.0f, heightPx / 2.0f, 0.0f);
        } catch (Exception e) {
            Gdx.app.error("GameScreen", "Failed to initialize the map renderer: " + e.getMessage());
        }
    }

    /**
     * Updates the cursor state based on camera actions.
     */
    private void updateCursorState() {
        if (cameraController.isZoomingIn()) {
            cursorManager.setCursor(GameCursor.ZOOM_IN);
        } else if (cameraController.isZoomingOut()) {
            cursorManager.setCursor(GameCursor.ZOOM_OUT);
        } else if (cameraController.isPanning()) {
            cursorManager.setCursor(GameCursor.HAND_OPEN);
        } else {
            cursorManager.setCursor(GameCursor.POINTER);
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
