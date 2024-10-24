package io.github.uoyteamsix;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * A class representing the main gameplay screen.
 */
public class GameScreen extends ScreenAdapter {
    private final AssetManager assetManager;
    private final CursorManager cursorManager;
    private SpriteBatch batch;
    private CameraController cameraController;
    private MapRenderer mapRenderer;
    private ShapeRenderer shapeRenderer; // Used for highlighting selected tiles
    private TiledMapTileLayer mapLayer;  // Reference to the map's tile layer

    // Variables for tile selection
    private int selectedTileX = -1, selectedTileY = -1;

    public GameScreen(AssetManager assetManager, CursorManager cursorManager) {
        this.assetManager = assetManager;
        this.cursorManager = cursorManager;
        initializeGraphics();

        // Create an input multiplexer to chain together our input adapters. For now, we only have the camera.
        var inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(cameraController);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    /**
     * Initializes graphics resources like the SpriteBatch, Camera, and ShapeRenderer.
     */
    private void initializeGraphics() {
        if (batch != null) batch.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        cameraController = new CameraController();
    }

    @Override
    public void resize(int width, int height) {
        cameraController.resizeViewport(width, height);
    }

    @Override
    public void render(float deltaTime) {
        // Clear the screen with a solid color (black)
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1.0f);

        // Create map renderer if first run.
        if (mapRenderer == null) {
            initializeMapRenderer();
        }

        // Handle tile selection based on mouse input
        handleTileSelection();

        // Set cursor based on camera behavior
        updateCursorState();

        // Update camera.
        cameraController.update(deltaTime);

        // Render the map
        mapRenderer.setView(cameraController.getCamera());
        mapRenderer.render();

        // Render the tile highlight if a tile is selected
        renderTileHighlight();
    }

    /**
     * Initializes the map renderer and sets up the camera position.
     */
    private void initializeMapRenderer() {
        try {
            var map = assetManager.get("maps/Map.tmx", TiledMap.class);
            mapRenderer = new OrthogonalTiledMapRenderer(map, batch);

            // Get the main layer of the map for tile selection
            mapLayer = (TiledMapTileLayer) map.getLayers().get(0);

            // Calculate the width and height of the map in pixels and use that to center the camera on the map.
            var props = map.getProperties();
            int widthPx = props.get("width", Integer.class) * props.get("tilewidth", Integer.class);
            int heightPx = props.get("height", Integer.class) * props.get("tileheight", Integer.class);
            cameraController.getCamera().position.set(widthPx / 2.0f, heightPx / 2.0f, 0.0f);
            cameraController.setMapDimensions(widthPx, heightPx);  // Set map dimensions here
        } catch (Exception e) {
            Gdx.app.error("GameScreen", "Failed to initialize the map renderer: " + e.getMessage());
        }
    }

    /**
     * Handles tile selection based on mouse input.
     */
    private void handleTileSelection() {
        if (Gdx.input.justTouched()) {
            Vector3 worldCoordinates = cameraController.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            int tileX = (int) (worldCoordinates.x / mapLayer.getTileWidth());
            int tileY = (int) (worldCoordinates.y / mapLayer.getTileHeight());

            // Check if the clicked tile is within bounds
            if (tileX >= 0 && tileX < mapLayer.getWidth() && tileY >= 0 && tileY < mapLayer.getHeight()) {
                selectedTileX = tileX;
                selectedTileY = tileY;
            } else {
                // If out of bounds, deselect
                selectedTileX = -1;
                selectedTileY = -1;
            }
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
        if (selectedTileX >= 0 && selectedTileY >= 0) {
            shapeRenderer.setProjectionMatrix(cameraController.getCamera().combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.RED); // Highlight color

            // Draw a rectangle around the selected tile
            float tileWidth = mapLayer.getTileWidth();
            float tileHeight = mapLayer.getTileHeight();
            shapeRenderer.rect(selectedTileX * tileWidth, selectedTileY * tileHeight, tileWidth, tileHeight);

            shapeRenderer.end();
        }
    }

    @Override
    public void resume() {
        // Re-initialize graphics resources to handle context loss
        Gdx.app.log("GameScreen", "Resuming and re-initializing graphics resources.");
        initializeGraphics();
        initializeMapRenderer();
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
    }
}
