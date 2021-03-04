package ch.zli.m335.comtrain;

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

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class WorkoutActivity extends MyActivty{

    private SensorManager sensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;

    Button startWorkoutBtn;
    Button resetBtn;
    Button pauseBtn;
    TextInputEditText exerciseInput;
    TextInputEditText roundInput;
    TextView exerciseWorkView;
    TextView roundWorkView;
    TextView exerciseGoalView;
    TextView roundGoalView;

    private final int exerciseDefaultGoal = 3;
    private final int roundDefaultGoal = 6;
    private int exerciseWorkInt = -11;
    private int roundWorkInt = 0;


    private boolean isPaused;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(sensorManager).registerListener(mSensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        exerciseInput = findViewById(R.id.exerciseInput);
        roundInput = findViewById(R.id.roundInput);
        startWorkoutBtn = findViewById(R.id.startWorkoutBtn);
        resetBtn = findViewById(R.id.workoutResetBtn);
        pauseBtn = findViewById(R.id.workoutPauseBtn);
        exerciseWorkView = findViewById(R.id.exerciseWork);
        roundWorkView = findViewById(R.id.roundWork);
        exerciseGoalView = findViewById(R.id.exerciseGoal);
        roundGoalView = findViewById(R.id.roundGoal);

        createNavigation();
        enableResetBtnListener();
        enablePauseBtnListener();

        enableStartBtnListener(exerciseGoalView, roundGoalView);

    }

    //from: https://www.tutorialspoint.com/how-to-detect-shake-event-in-android-app
    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            if (mAccel > 3) {
                countUpExerciseAndRound();
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    @Override
    protected void onResume() {
        sensorManager.registerListener(mSensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }
    @Override
    protected void onPause() {
        sensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    public void enableStartBtnListener(TextView exerciseGoalView, TextView roundGoalView){
           startWorkoutBtn.setOnClickListener(v -> {
            inputGoal(exerciseGoalView, exerciseInput, exerciseDefaultGoal);
            inputGoal(roundGoalView, roundInput, roundDefaultGoal);
        });
    }

    public void inputGoal(TextView goalView, TextInputEditText goalInput, int defaultInt){
            if (Objects.requireNonNull(goalInput.getText()).length() > 0) {
                goalView.setText(toString(goalInput.getText()));
            } else {
                goalView.setText(toString(defaultInt));
            }
    }

    public void enableResetBtnListener(){
        resetBtn.setOnClickListener(v -> {
            exerciseWorkInt = resetCounter(exerciseWorkView);
            roundWorkInt = resetCounter(roundWorkView);

        });
        resetBtn.setOnLongClickListener(v -> {
            exerciseWorkInt = resetCounter(exerciseWorkView);
            roundWorkInt = resetCounter(roundWorkView);
            exerciseGoalView.setText(toString(0));
            roundGoalView.setText(toString(0));
            return false;
        });
    }

    /**
     * Counts up Exercise
     * if Exercise work is equal to Exercise Goal Round counts up
     * if Exercise work is equal to Exercise Goal & Round Work is equal to Round Goal everything resets to 0
     */
    public void countUpExerciseAndRound() {

       if (textViewtoInt(exerciseGoalView) > 0 || exerciseWorkInt < 0) {
            exerciseWorkInt++;
        }
        if (exerciseWorkInt >= 0){
            exerciseWorkView.setText(toString(exerciseWorkInt));
            try {
                onPause();
                TimeUnit.SECONDS.sleep(1);
                onResume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (exerciseWorkInt == textViewtoInt(exerciseGoalView) && textViewtoInt(exerciseGoalView) != 0){
            roundWorkInt++;
            roundWorkView.setText(toString(roundWorkInt));
            exerciseWorkInt = resetCounter(exerciseWorkView);
            if (roundWorkInt == textViewtoInt(roundGoalView)) {
                exerciseWorkInt = resetCounter(exerciseWorkView);
                roundWorkInt = resetCounter(roundWorkView);
            }
        }

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


}