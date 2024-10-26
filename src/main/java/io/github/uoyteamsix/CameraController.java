package io.github.uoyteamsix;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * A class which holds the {@link Camera} object and implements the camera movement logic.
 */
public class CameraController extends InputAdapter {
    private final OrthographicCamera camera;
    private final Vector2 lastDragPosition;

    private boolean isCurrentlyDragging = false;
    private float desiredZoomLevel = 0.5f;

    public CameraController() {
        camera = new OrthographicCamera();
        camera.zoom = desiredZoomLevel;
        lastDragPosition = new Vector2();
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        desiredZoomLevel += amountY * 0.03f;
        desiredZoomLevel = MathUtils.clamp(desiredZoomLevel, 0.2f, 1.5f);
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.RIGHT) {
            isCurrentlyDragging = true;
            lastDragPosition.set(screenX, screenY);
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        isCurrentlyDragging = false;
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (!isCurrentlyDragging) {
            return false;
        }

        // Convert mouse movement into world space which allows movement of the camera whilst keeping the mouse cursor
        // fixed relative to the same point in world space.
        var mouseNow = camera.unproject(new Vector3(screenX, screenY, 0));
        camera.translate(camera.unproject(new Vector3(lastDragPosition, 0)).sub(mouseNow));
        lastDragPosition.set(screenX, screenY);
        return true;
    }

    /**
     * Updates the camera's viewport size.
     *
     * @param width  the new viewport width
     * @param height the new viewport height
     */
    public void resizeViewport(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
    }

    /**
     * Handles any input events and updates the camera accordingly. Should be called once per frame.
     *
     * @param deltaTime the delta time between the last call of update
     */
    public void update(float deltaTime) {
        // Allow use of WSAD keys to move camera if not currently dragging the camera. Decide speed factor based on
        // whether shift is currently held.
        if (!isCurrentlyDragging) {
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
        }
        camera.update();

        // Handle camera zooming. The code below gives the effect of zooming into a point by keeping the mouse position
        // constant relative to world space.
        var mouseStart = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

        // Update zoom level and call update so that the next call to unproject has updated values.
        camera.zoom = Interpolation.linear.apply(camera.zoom, desiredZoomLevel, deltaTime * 12.0f);
        camera.update();

        // The difference in mouse position in world space before and after zooming is the amount we need to
        // translate by to keep the mouse position constant.
        var mouseEnd = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        camera.translate(mouseStart.sub(mouseEnd));
        camera.update();
    }

    /**
     * @return the orthographic camera controlled by this class
     */
    public OrthographicCamera getCamera() {
        return camera;
    }

    /**
     * @return whether the user is currently panning the camera with the mouse
     */
    public boolean isPanning() {
        return isCurrentlyDragging;
    }

    /**
     * @return whether the camera is currently zooming in
     */
    public boolean isZoomingIn() {
        return (desiredZoomLevel - camera.zoom) < -0.01f;
    }

    /**
     * @return whether the camera is currently zooming out
     */
    public boolean isZoomingOut() {
        return (desiredZoomLevel - camera.zoom) > 0.01f;
    }
}
