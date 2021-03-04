package ch.zli.m335.comtrain;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

public class PushUpActivity extends AppCompatActivity implements SensorEventListener {

    Intent startMainActivity;
    Intent startStepCounterActivity;
    Intent startWorkoutActivity;

    SensorManager sensorManager;
    Sensor sensor;

    double downLightNumber;
    double upLightNumber;
    int pushUps;
    boolean wasDown = false;
    boolean isPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_up);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startMainActivity = new Intent(this, MainActivity.class);
        startStepCounterActivity = new Intent(this, StepCoutnerActivity.class);
        startWorkoutActivity = new Intent(this, WorkoutActivity.class);

        activateLightSensor();
        enableResetBtnListener();
        enablePauseBtnListener();


        ImageView MainSvg = findViewById(R.id.MainToMain);
        ImageView StepCounterSvg = findViewById(R.id.MainToStepCounter);
        ImageView WorkoutSvg = findViewById(R.id.MainToWorkout);

        MainSvg.setOnClickListener(v -> changeActivities(startMainActivity));
        StepCounterSvg.setOnClickListener(v -> changeActivities(startStepCounterActivity));
        WorkoutSvg.setOnClickListener(v -> changeActivities(startWorkoutActivity));

        Button calibratorBtn = findViewById(R.id.calibratorBtn);
        Button upCalibratorBtn = findViewById(R.id.upCalibratorBtn);
        Button downCalibratorBtn = findViewById(R.id.downCalibratorBtn);

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

    public void changeActivities(Intent startactivity) {
        startActivity(startactivity);
        finish();
    }

    public void activateLightSensor() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sensorManager.registerListener((SensorEventListener) this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event) {
        TextView pushUpCounterView = findViewById(R.id.pushupCounterView);
        Button downCalibratorBtn = findViewById(R.id.downCalibratorBtn);
        Button upCalibratorBtn = findViewById(R.id.upCalibratorBtn);
        TextView lightNumberView = findViewById(R.id.lightNumberView);

        lightNumberView.setText(toString(event.values[0]) + " cm");

        downCalibratorBtn.setOnClickListener(v -> {
            downCalibratorBtn.setText(toString(event.values[0]) + " cm");
            downLightNumber = event.values[0];
        });
        upCalibratorBtn.setOnClickListener(v -> {
            upCalibratorBtn.setText(toString(event.values[0]) + " cm");
            upLightNumber = event.values[0];
        });

        if (event.values[0] <= downLightNumber) {
            wasDown = true;
        }
        if (event.values[0] >= upLightNumber && wasDown) {
            wasDown = false;
            pushUps++;
            pushUpCounterView.setText(toString(pushUps));
        }

    }

    public void enableResetBtnListener() {
        Button resetBtn = findViewById(R.id.pushUpResetCounterBtn);
        TextView counterView = findViewById(R.id.pushupCounterView);

        resetBtn.setOnClickListener(v -> {
            pushUps = 0;
            counterView.setText(toString(pushUps));

        });
    }

    public void enablePauseBtnListener() {
        Button pauseBtn = findViewById(R.id.pauseCounterBtn);
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

    public String toString(Object number) {
        return String.valueOf(number);
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

}