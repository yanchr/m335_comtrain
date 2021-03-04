package ch.zli.m335.comtrain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class PushUpActivity extends MyActivty implements SensorEventListener {

    public Intent startMainActivity;
    public Intent startStepCounterActivity;
    public Intent startWorkoutActivity;

    public SensorManager sensorManager;
    public Sensor sensor;

    public ImageView MainSvg;
    public ImageView StepCounterSvg ;
    public ImageView WorkoutSvg;
    public Button calibratorBtn;
    public Button upCalibratorBtn;
    public Button resetBtn;
    public Button downCalibratorBtn;
    public TextView pushUpCounterView;
    public TextView lightNumberView;
    public TextView highscoreView;

    public double downLightNumber;
    public double upLightNumber;
    public int pushUps;
    public boolean wasDown = false;
    public int highscore = 0;
    private static final String COUNTER_STATE = "counter";
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_up);

        startMainActivity = new Intent(this, MainActivity.class);
        startStepCounterActivity = new Intent(this, StepCoutnerActivity.class);
        startWorkoutActivity = new Intent(this, WorkoutActivity.class);

        this.MainSvg = findViewById(R.id.MainToMain);
        this.StepCounterSvg = findViewById(R.id.MainToStepCounter);
        this.WorkoutSvg = findViewById(R.id.MainToWorkout);
        this.pushUpCounterView = findViewById(R.id.pushUpCounterView);
        this.downCalibratorBtn = findViewById(R.id.downCalibratorBtn);
        this.upCalibratorBtn = findViewById(R.id.upCalibratorBtn);
        this.calibratorBtn = findViewById(R.id.calibratorBtn);
        this.resetBtn = findViewById(R.id.pushUpResetCounterBtn);
        this.lightNumberView = findViewById(R.id.lightNumberView);
        this.highscoreView = findViewById(R.id.highscoreView);
        this.preferences = getPreferences(MODE_PRIVATE);

        activateLightSensor();


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

    @Override
    protected void onStart() {
        super.onStart();
        enableResetBtnListener();
        enablePauseBtnListener();

        MainSvg.setOnClickListener(v -> changeActivities(startMainActivity));
        StepCounterSvg.setOnClickListener(v -> changeActivities(startStepCounterActivity));
        WorkoutSvg.setOnClickListener(v -> changeActivities(startWorkoutActivity));

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
            return false;
        });
    }

}