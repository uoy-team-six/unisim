package io.github.uoyteamsix.ui;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * A class to represent the game timer UI element.
 */
public class GameTimer extends HorizontalGroup {
    private final Label timeLabel;

    // Start timer at 5 minutes.
    private float timeRemaining = 5.0f * 60.0f;

    public GameTimer(Label.LabelStyle labelStyle) {
        timeLabel = new Label("5:00", labelStyle);

        // Add actors to horizontal group.
        addActor(timeLabel);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        timeRemaining -= delta;

        // Convert time in seconds to minutes and seconds.
        int minutes = (int) (timeRemaining / 60.0f);
        int seconds = ((int) timeRemaining) % 60;
        timeLabel.setText(String.format("%d:%02d", minutes, seconds));
    }
}
