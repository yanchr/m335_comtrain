package ch.zli.m335.comtrain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class StepCoutnerActivity extends MyActivty {

    private SensorManager sensorManager;
    private Sensor sensor;

    Button resetBtn;
    Button pauseBtn;
    TextView stepCounterView;

    private double magnitudePrevious = 0;
    private double magnitudeDelta;
    private Integer stepCounterInt = 0;
    private int stepsuntilShake = 10;
    boolean alreadyShaken = false;

    private static final String STEP_COUNT_STATE = "stepCount";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_coutner);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        stepCounterView = findViewById(R.id.stepCounterView);
        pauseBtn = findViewById(R.id.stepCounterPauseBtn);
        resetBtn = findViewById(R.id.stepCounterResetBtn);
        preferences = getPreferences(MODE_PRIVATE);

        this.stepCounterInt = loadSteps(savedInstanceState);

        activateStepSensor();
        createNavigation();
        enableResetBtnListener();
        enablePauseBtnListener();

        // Preserve UI state
        renderTextView(this.stepCounterView, toString(this.stepCounterInt));

        saveScore(STEP_COUNT_STATE, stepCounterInt);

    }

    private int loadSteps(Bundle savedInstanceState) {

        if (savedInstanceState != null && savedInstanceState.containsKey(STEP_COUNT_STATE)) {
            abstractSteps = savedInstanceState.getInt(STEP_COUNT_STATE);
            return savedInstanceState.getInt(STEP_COUNT_STATE);
        } else {
            // Restore saved application data
            abstractSteps = preferences.getInt(STEP_COUNT_STATE, this.stepCounterInt);
            return preferences.getInt(STEP_COUNT_STATE, this.stepCounterInt);
        }
    }

    public void activateStepSensor() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(stepDetector, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    // from https://programmerworld.co/android/how-to-create-walking-step-counter-app-using-accelerometer-sensor-and-shared-preference-in-android/
    public SensorEventListener stepDetector = new SensorEventListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent != null) {
                float x_acceleration = sensorEvent.values[0];
                float y_acceleration = sensorEvent.values[1];
                float z_acceleration = sensorEvent.values[2];

                double Magnitude = Math.sqrt(x_acceleration * x_acceleration + y_acceleration * y_acceleration + z_acceleration * z_acceleration);
                magnitudeDelta = Magnitude - magnitudePrevious;
                magnitudePrevious = Magnitude;

                countUpSteps();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };

    @Override
    protected void onResume() {
        sensorManager.registerListener(stepDetector, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(stepDetector);
        super.onPause();
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(STEP_COUNT_STATE, this.stepCounterInt);
        super.onSaveInstanceState(outState);
    }


    //from  https://stackoverflow.com/questions/13950338/how-to-make-an-android-device-vibrate-with-different-frequency
    public void shakePhone() {
        Vibrator vibration = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibration.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibration.vibrate(500);
        }
    }

    public void enableResetBtnListener() {
        resetBtn.setOnClickListener(v -> {
            stepCounterInt = resetCounter(stepCounterView);
            saveScore(STEP_COUNT_STATE, stepCounterInt);
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

    public void countUpSteps() {
        if (magnitudeDelta > 4) {
            stepCounterInt++;
            abstractSteps++;
            alreadyShaken = false;
        }
        if (stepCounterInt == 1) {
            stepCounterView.setText(stepCounterInt.toString() + " Step");
            saveScore(STEP_COUNT_STATE, stepCounterInt);
        }
        if (stepCounterInt % stepsuntilShake == 0 && !alreadyShaken) {
            alreadyShaken = true;
            shakePhone();
        }
        stepCounterView.setText(stepCounterInt.toString() + " Steps");
        saveScore(STEP_COUNT_STATE, stepCounterInt);
    }
}

