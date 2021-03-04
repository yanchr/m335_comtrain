package ch.zli.m335.comtrain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class StepCoutnerActivity extends AppCompatActivity implements SensorEventListener{

    Intent startMainActivity;
    Intent startPushUpActivity;
    Intent startWorkoutActivity;

    private SensorManager sensorManager;
    private Sensor sensor;

    private int stepCounterInt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_coutner);

        Button shaker = findViewById(R.id.shakerBtn);
        shaker.setOnClickListener(v -> {
            shakePhone();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


        startMainActivity = new Intent(this, MainActivity.class);
        startPushUpActivity = new Intent(this, PushUpActivity.class);
        startWorkoutActivity = new Intent(this, WorkoutActivity.class);

        ImageView MainSvg = findViewById(R.id.MainToMain);
        ImageView PushUpSvg = findViewById(R.id.MainToPushUp);
        ImageView WorkoutSvg = findViewById(R.id.MainToWorkout);

        MainSvg.setOnClickListener(v -> changeActivities(startMainActivity));
        PushUpSvg.setOnClickListener(v -> changeActivities(startPushUpActivity));
        WorkoutSvg.setOnClickListener(v -> changeActivities(startWorkoutActivity));

        activateStepSensor();
    }

    public void changeActivities(Intent startactivity) {
        startActivity(startactivity);
        finish();
    }

    public void activateStepSensor() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorManager.registerListener((SensorEventListener) this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (sensor.getType() == Sensor.TYPE_STEP_COUNTER){
            TextView stepCounter = findViewById(R.id.stepCounterView);
            stepCounterInt++;
            stepCounter.setText(toString(stepCounterInt));
        }
    }


    public String toString(Object number) {
        return String.valueOf(number);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void shakePhone() {
    }
}

