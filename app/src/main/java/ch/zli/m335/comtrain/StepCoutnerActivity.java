package ch.zli.m335.comtrain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class StepCoutnerActivity extends MyActivty implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;

    Button shakerBtn;
    Button resetBtn;
    Button pauseBtn;
    TextView stepCounterView;

    private int stepCounterInt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_coutner);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        shakerBtn = findViewById(R.id.shakerBtn);
        stepCounterView = findViewById(R.id.stepCounterView);
        pauseBtn = findViewById(R.id.stepCounterPauseBtn);
        resetBtn = findViewById(R.id.stepCounterResetBtn);

        activateStepSensor();
        createNavigation();
        enableResetBtnListener();
        enablePauseBtnListener();


        shakerBtn.setOnClickListener(v -> {
            shakePhone();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void activateStepSensor() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorManager.registerListener((SensorEventListener) this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {

            stepCounterInt++;
            stepCounterView.setText(toString(stepCounterInt));
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
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    // Unregister listener
    @Override
    protected void onPause() {
        sensorManager.unregisterListener(this);
        super.onPause();
    }

    public void shakePhone() {
    }

    public void enableResetBtnListener() {
        resetBtn.setOnClickListener(v -> {
            stepCounterInt = resetCounter(stepCounterView);
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
}

