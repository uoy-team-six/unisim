package io.github.uoyteamsix;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.uoyteamsix.map.GameMap;
import io.github.uoyteamsix.map.GameMapInput;
import io.github.uoyteamsix.ui.UiStage;

/**
 * A class representing the main gameplay screen.
 */
public class GameScreen extends ScreenAdapter {
    private final AssetManager assetManager;
    private final CursorManager cursorManager;
    private final SpriteBatch batch;
    private final CameraController cameraController;
    private final ShapeRenderer shapeRenderer;
    private final UiStage uiStage;
    private GameMap map;
    private GameMapInput mapInput;
    private MapRenderer mapRenderer;

    public GameScreen(AssetManager assetManager, CursorManager cursorManager) {
        this.assetManager = assetManager;
        this.cursorManager = cursorManager;
        batch = new SpriteBatch();
        cameraController = new CameraController();
        shapeRenderer = new ShapeRenderer();
        uiStage = new UiStage(assetManager);

        // Create an input multiplexer to chain together our input adapters.
        // Add the UI stage first, then the camera controller.
        var inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(uiStage);
        inputMultiplexer.addProcessor(cameraController);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void resize(int width, int height) {
        // Propagate resize to other systems.
        cameraController.setViewportDimensions(width, height);
        uiStage.getViewport().update(width, height, true);
    }

    @Override
    public void render(float deltaTime) {
        // Clear the screen with a solid color (black).
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1.0f);

        // Create map and map renderer if first run.
        if (map == null) {
            initializeMap();
        }

        // Set cursor based on camera behavior.
        updateCursorState();

        // Update camera and UI.
        cameraController.update(deltaTime);
        uiStage.act(deltaTime);

        // Render the map.
        mapRenderer.setView(cameraController.getCamera());
        mapRenderer.render();

        // Render the tile highlight if a tile is selected.
        renderTileHighlight();

        // Render the UI last.
        uiStage.draw();
    }

    /**
     * Initializes the map and map renderer and sets up the camera position.
     */
    private void initializeMap() {
        try {
            var tiledMap = assetManager.get("maps/map.tmx", TiledMap.class);
            map = new GameMap(tiledMap);
            mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, batch);

            // Center the camera on the map.
            cameraController.getCamera().position.set(map.getWidthPx() / 2.0f, map.getHeightPx() / 2.0f, 0.0f);
            cameraController.setMapDimensions(map.getWidthPx(), map.getHeightPx());

            // Add input handler for map.
            mapInput = new GameMapInput(map, cameraController);
            ((InputMultiplexer) Gdx.input.getInputProcessor()).addProcessor(mapInput);
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

    /**
     * Renders a highlight around the currently selected tile.
     */
    private void renderTileHighlight() {
        // Enable blending so we can render a tint on top of the selected tile.
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        var selectedTileX = mapInput.getSelectedTileX();
        var selectedTileY = mapInput.getSelectedTileY();
        if (selectedTileX >= 0 && selectedTileY >= 0) {
            shapeRenderer.setProjectionMatrix(cameraController.getCamera().combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(1.0f, 1.0f, 1.0f, 0.5f);

            // Draw a rectangle around the selected tile
            float tileWidth = map.getTileWidthPx();
            float tileHeight = map.getTileHeightPx();
            shapeRenderer.rect(selectedTileX * tileWidth, selectedTileY * tileHeight, tileWidth, tileHeight);

            shapeRenderer.end();
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        uiStage.dispose();
    }
}
