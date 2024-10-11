package io.github.uoyteamsix;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * A class representing the main gameplay screen.
 */
public class GameScreen extends ScreenAdapter {
    private final AssetManager assetManager;
    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private MapRenderer mapRenderer;
    private float lastScrollAmount;

    public GameScreen(AssetManager assetManager) {
        this.assetManager = assetManager;
        batch = new SpriteBatch();
        camera = new OrthographicCamera();

        // Create an input processor which just captures mouse scroll events.
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                lastScrollAmount = amountY;
                return true;
            }
        });
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
    }

    @Override
    public void render(float deltaTime) {
        // Create map renderer.
        if (mapRenderer == null) {
            var map = assetManager.get("maps/hexagonal-mini.tmx", TiledMap.class);
            mapRenderer = new HexagonalTiledMapRenderer(map, batch);
        }

        // Handle camera movement. Decide speed factor based on whether shift is currently held.
        final var cameraMovementSpeed = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? 250.0f : 100.0f;
        final var cameraMovementDelta = deltaTime * cameraMovementSpeed;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera.translate(0.0f, cameraMovementDelta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            camera.translate(0.0f, -cameraMovementDelta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.translate(-cameraMovementDelta, 0.0f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            camera.translate(cameraMovementDelta, 0.0f);
        }
        camera.update();

        // Handle camera zooming. The code below gives the effect of zooming into a point by keeping the mouse position
        // constant relative to world space.
        var mouseStart = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

        // Update zoom factor and call update so that the next call to unproject has updated values.
        camera.zoom += lastScrollAmount * 0.05f;
        lastScrollAmount = 0.0f;
        camera.update();

        // The difference between in mouse position in world space before and after zooming is the amount we need to
        // translate by to keep the mouse position constant.
        var mouseEnd = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        camera.translate(mouseStart.sub(mouseEnd));
        camera.update();

        // Clear screen with black and draw map.
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1.0f);
        mapRenderer.setView(camera);
        mapRenderer.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
