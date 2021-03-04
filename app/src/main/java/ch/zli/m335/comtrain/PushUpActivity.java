package ch.zli.m335.comtrain;

import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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


    private double downLightNumber;
    private double upLightNumber;
    private int pushUps;
    private boolean wasDown = false;
    private int highscore = 0;
    private static final String COUNTER_STATE = "counter";
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_up);

        this.pushUpCounterView = findViewById(R.id.pushUpCounterView);
        this.downCalibratorBtn = findViewById(R.id.downCalibratorBtn);
        this.upCalibratorBtn = findViewById(R.id.upCalibratorBtn);
        this.calibratorBtn = findViewById(R.id.calibratorBtn);
        this.pauseBtn = findViewById(R.id.pushUpPauseBtn);
        this.resetBtn = findViewById(R.id.pushUpResetBtn);
        this.lightNumberView = findViewById(R.id.lightNumberView);
        this.highscoreView = findViewById(R.id.highscoreView);
        this.preferences = getPreferences(MODE_PRIVATE);

        activateLightSensor();
        createNavigation();
        enableResetBtnListener();
        enablePauseBtnListener();
        enableCalibratorBtnListener();


        // Preserve UI state
        if(savedInstanceState != null && savedInstanceState.containsKey(COUNTER_STATE)) {
            this.highscore = savedInstanceState.getInt(COUNTER_STATE);
        } else {
            // Restore saved application data
            this.highscore = preferences.getInt(COUNTER_STATE, this.highscore);
        }

        renderTextView(this.highscoreView,toString(this.highscore));
        saveHighScore();
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
        });
        upCalibratorBtn.setOnClickListener(v -> {
            upCalibratorBtn.setText(toString(event.values[0]));
            upLightNumber = event.values[0];
        });

        if (event.values[0] < downLightNumber) {
            wasDown = true;
        }
        if (event.values[0] > upLightNumber && wasDown) {
            wasDown = false;
            pushUps++;
            pushUpCounterView.setText(toString(pushUps));
            if (pushUps > highscore) {
                TextView highscoreView = findViewById(R.id.highscoreView);
                highscore = pushUps;
                highscoreView.setText(toString(highscore));
                saveHighScore();
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
        outState.putInt(COUNTER_STATE, this.highscore);
        super.onSaveInstanceState(outState);
    }

    private void saveHighScore() {
        SharedPreferences.Editor preferencesEditor = this.preferences.edit();
        preferencesEditor.putInt(COUNTER_STATE, this.highscore);
        preferencesEditor.apply();
    }

    public void enableResetBtnListener() {
        resetBtn.setOnClickListener(v -> {
            pushUps = resetCounter(pushUpCounterView);
        });

        resetBtn.setOnLongClickListener(v -> {
            pushUps = resetCounter(pushUpCounterView);
            highscore = resetCounter(highscoreView);
            saveHighScore();
            return false;
        });
    }

    public void enablePauseBtnListener() {
        pauseBtn.setOnClickListener(v -> {
            isPaused = !isPaused;
            if (!isPaused) {
                onPause();
                pauseBtn.setText("UNPAUSE");
            } else{
                onResume();
                pauseBtn.setText("PAUSE");
            }
        });

    }


    protected void enableCalibratorBtnListener(){
        upCalibratorBtn.setVisibility(View.INVISIBLE);
        downCalibratorBtn.setVisibility(View.INVISIBLE);

        calibratorBtn.setOnClickListener(v -> {
            if (upCalibratorBtn.getVisibility() == View.INVISIBLE) {
                upCalibratorBtn.setVisibility(View.VISIBLE);
                downCalibratorBtn.setVisibility(View.VISIBLE);
            } else {
                upCalibratorBtn.setVisibility(View.INVISIBLE);
                downCalibratorBtn.setVisibility(View.INVISIBLE);
            }
        });
    }





}