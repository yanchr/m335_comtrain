package ch.zli.m335.comtrain;

import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.chrono.HijrahChronology;

public class PushUpActivity extends MyActivty implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;

    private Button calibratorBtn;
    private Button upCalibratorBtn;
    private Button resetBtn;
    private Button pauseBtn;
    private Button downCalibratorBtn;
    private TextView pushUpCounterView;
    private TextView lightNumberView;
    private TextView highscoreView;
    private TextView upTextView;
    private TextView downTextView;


    private double downLightNumber;
    private double upLightNumber;
    private int pushUps = 0;
    private boolean wasDown = false;
    private int highscore = 0;

    private static final String HISCORE_STATE = "hiscore";
    private static final String SCORE_STATE = "score";
    private static final String UP_STATE = "up";
    private static final String DOWN_STATE = "down";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_up);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        this.pushUpCounterView = findViewById(R.id.pushUpCounterView);
        this.downCalibratorBtn = findViewById(R.id.downCalibratorBtn);
        this.upCalibratorBtn = findViewById(R.id.upCalibratorBtn);
        this.calibratorBtn = findViewById(R.id.calibratorBtn);
        this.pauseBtn = findViewById(R.id.pushUpPauseBtn);
        this.resetBtn = findViewById(R.id.pushUpResetBtn);
        this.lightNumberView = findViewById(R.id.lightNumberView);
        this.highscoreView = findViewById(R.id.highscoreView);
        this.preferences = getPreferences(MODE_PRIVATE);
        this.upTextView = findViewById(R.id.upTextView);
        this.downTextView = findViewById(R.id.downTextView);

        activateLightSensor();
        createNavigation();
        enableResetBtnListener();
        enablePauseBtnListener();
        enableCalibratorBtnListener();


        // Preserve UI state
        if (savedInstanceState != null && savedInstanceState.containsKey(HISCORE_STATE)) {
            this.highscore = savedInstanceState.getInt(HISCORE_STATE);
            this.pushUps = savedInstanceState.getInt(SCORE_STATE);
            this.upLightNumber = savedInstanceState.getInt(UP_STATE);
            this.downLightNumber = savedInstanceState.getInt(DOWN_STATE);
        } else {
            // Restore saved application data
            this.highscore = preferences.getInt(HISCORE_STATE, this.highscore);
            this.pushUps = preferences.getInt(SCORE_STATE, this.pushUps);
            this.upLightNumber = preferences.getInt(UP_STATE, (int) this.upLightNumber);
            this.downLightNumber = preferences.getInt(DOWN_STATE, (int) this.downLightNumber);
        }

        renderTextView(this.highscoreView, toString(this.highscore));
        renderTextView(this.pushUpCounterView, toString(this.pushUps));
        renderTextView(this.upCalibratorBtn, toString(this.upLightNumber));
        renderTextView(this.downCalibratorBtn, toString(this.downLightNumber));

        saveScore(HISCORE_STATE, highscore);
        saveScore(SCORE_STATE, pushUps);
        saveScore(UP_STATE, (int) upLightNumber);
        saveScore(DOWN_STATE, (int) downLightNumber);
    }

    public void activateLightSensor() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener((SensorEventListener) this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event) {
        lightNumberView.setText(toString(event.values[0]));

        downCalibratorBtn.setOnClickListener(v -> {
            downCalibratorBtn.setText(toString(event.values[0]));
            downLightNumber = event.values[0];
            saveScore(DOWN_STATE, (int) downLightNumber);

        });
        upCalibratorBtn.setOnClickListener(v -> {
            upCalibratorBtn.setText(toString(event.values[0]));
            upLightNumber = event.values[0];
            saveScore(UP_STATE, (int) upLightNumber);
        });

        if (event.values[0] < downLightNumber) {
            wasDown = true;
        }
        if (event.values[0] > upLightNumber && wasDown) {
            wasDown = false;
            pushUps++;
            pushUpCounterView.setText(toString(pushUps));
            saveScore(SCORE_STATE, pushUps);
            if (pushUps > highscore) {
                highscoreView = findViewById(R.id.highscoreView);
                highscore = pushUps;
                highscoreView.setText(toString(highscore));
                saveScore(HISCORE_STATE, highscore);
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    // Register listener
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor,
                SensorManager.SENSOR_DELAY_UI);
    }

    // Unregister listener
    @Override
    protected void onPause() {
        sensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(HISCORE_STATE, this.highscore);
        outState.putInt(SCORE_STATE, this.pushUps);
        outState.putInt(UP_STATE, (int) this.upLightNumber);
        outState.putInt(DOWN_STATE, (int) this.downLightNumber);
        super.onSaveInstanceState(outState);
    }


    @SuppressLint("SetTextI18n")
    public void enableResetBtnListener() {
        resetBtn.setOnClickListener(v -> {
            pushUps = resetCounter(pushUpCounterView);
            saveScore(SCORE_STATE, pushUps);
        });

        resetBtn.setOnLongClickListener(v -> {
            pushUps = resetCounter(pushUpCounterView);
            highscore = resetCounter(highscoreView);
            saveScore(HISCORE_STATE, highscore);
            saveScore(SCORE_STATE, pushUps);
            saveScore(UP_STATE, (int) upLightNumber);
            saveScore(DOWN_STATE, (int) downLightNumber);

            return false;
        });
    }

    public void enablePauseBtnListener() {
        pauseBtn.setOnClickListener(v -> {
            isPaused = !isPaused;
            if (!isPaused) {
                onPause();
                pauseBtn.setText("UNPAUSE");
            } else {
                onResume();
                pauseBtn.setText("PAUSE");
            }
        });

    }


    protected void enableCalibratorBtnListener() {
        upCalibratorBtn.setVisibility(View.INVISIBLE);
        downCalibratorBtn.setVisibility(View.INVISIBLE);
        upTextView.setVisibility(View.INVISIBLE);
        downTextView.setVisibility(View.INVISIBLE);

        calibratorBtn.setOnClickListener(v -> {
            if (upCalibratorBtn.getVisibility() == View.INVISIBLE) {
                upCalibratorBtn.setVisibility(View.VISIBLE);
                downCalibratorBtn.setVisibility(View.VISIBLE);
                upTextView.setVisibility(View.VISIBLE);
                downTextView.setVisibility(View.VISIBLE);
            } else {
                upCalibratorBtn.setVisibility(View.INVISIBLE);
                downCalibratorBtn.setVisibility(View.INVISIBLE);
                upTextView.setVisibility(View.INVISIBLE);
                downTextView.setVisibility(View.INVISIBLE);
            }
        });
    }


}