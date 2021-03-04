package ch.zli.m335.comtrain;

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

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class WorkoutActivity extends MyActivty {

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
    private int exerciseWorkInt = 0;
    private int roundWorkInt = 0;
    private int exerciseGoalInt = 0;
    private int roundGoalInt = 0;

    private int preshaker = -11;

    private static final String EXERCISE_WORK_STATE = "exerciseWork";
    private static final String EXERCISE_GOAL_STATE = "exerciseGoal";
    private static final String ROUND_WORK_STATE = "roundWork";
    private static final String ROUND_GOAL_STATE = "roundGoal";

    private boolean isPaused;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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
        preferences = getPreferences(MODE_PRIVATE);


        createNavigation();
        enableResetBtnListener();
        enablePauseBtnListener();

        enableStartBtnListener();

        // Preserve UI state
        if (savedInstanceState != null && savedInstanceState.containsKey(EXERCISE_WORK_STATE)) {
            this.exerciseWorkInt = savedInstanceState.getInt(EXERCISE_WORK_STATE);
            this.exerciseGoalInt = savedInstanceState.getInt(EXERCISE_GOAL_STATE);
            this.roundWorkInt = savedInstanceState.getInt(ROUND_WORK_STATE);
            this.roundGoalInt = savedInstanceState.getInt(ROUND_GOAL_STATE);
        } else {
            // Restore saved application data
            this.exerciseWorkInt = preferences.getInt(EXERCISE_WORK_STATE, this.exerciseWorkInt);
            this.exerciseGoalInt = preferences.getInt(EXERCISE_GOAL_STATE, this.exerciseGoalInt);
            this.roundWorkInt = preferences.getInt(ROUND_WORK_STATE, (int) this.roundWorkInt);
            this.roundGoalInt = preferences.getInt(ROUND_GOAL_STATE, (int) this.roundGoalInt);
        }

        renderTextView(this.exerciseWorkView, toString(this.exerciseWorkInt));
        renderTextView(this.exerciseGoalView, toString(this.exerciseGoalInt));
        renderTextView(this.roundWorkView, toString(this.roundWorkInt));
        renderTextView(this.roundGoalView, toString(this.roundGoalInt));

        saveScore(EXERCISE_WORK_STATE, exerciseWorkInt);
        saveScore(EXERCISE_GOAL_STATE, exerciseGoalInt);
        saveScore(ROUND_WORK_STATE, (int) roundWorkInt);
        saveScore(ROUND_GOAL_STATE, (int) roundGoalInt);

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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(EXERCISE_WORK_STATE, this.exerciseWorkInt);
        outState.putInt(EXERCISE_GOAL_STATE, this.exerciseGoalInt);
        outState.putInt(ROUND_WORK_STATE, (int) this.roundWorkInt);
        outState.putInt(ROUND_GOAL_STATE, (int) this.roundGoalInt);
        super.onSaveInstanceState(outState);
    }

    public void enableStartBtnListener() {
        startWorkoutBtn.setOnClickListener(v -> {
            exerciseGoalInt = inputGoal(exerciseGoalView, exerciseInput, exerciseDefaultGoal);
            roundGoalInt = inputGoal(roundGoalView, roundInput, roundDefaultGoal);
            saveScore(EXERCISE_GOAL_STATE, exerciseGoalInt);
            saveScore(ROUND_GOAL_STATE, roundGoalInt);

        });
    }

    public int inputGoal(TextView goalView, TextInputEditText goalInput, int defaultInt) {
        if (Objects.requireNonNull(goalInput.getText()).length() > 0) {
            goalView.setText(toString(goalInput.getText()));
            return textViewtoInt(goalInput);
        } else {
            goalView.setText(toString(defaultInt));
            return defaultInt;
        }
    }

    public void enableResetBtnListener() {
        resetBtn.setOnClickListener(v -> {
            exerciseWorkInt = resetCounter(exerciseWorkView);
            roundWorkInt = resetCounter(roundWorkView);
            saveScore(ROUND_WORK_STATE, roundWorkInt);
            saveScore(EXERCISE_WORK_STATE, exerciseWorkInt);

        });
        resetBtn.setOnLongClickListener(v -> {
            exerciseWorkInt = resetCounter(exerciseWorkView);
            roundWorkInt = resetCounter(roundWorkView);
            exerciseGoalInt = resetCounter(exerciseGoalView);
            roundGoalInt = resetCounter(roundGoalView);
            saveScore(EXERCISE_WORK_STATE, exerciseWorkInt);
            saveScore(EXERCISE_GOAL_STATE, exerciseGoalInt);
            saveScore(ROUND_WORK_STATE, roundWorkInt);
            saveScore(ROUND_GOAL_STATE, roundGoalInt);
            return false;
        });
    }

    /**
     * Counts up Exercise
     * if Exercise work is equal to Exercise Goal Round counts up
     * if Exercise work is equal to Exercise Goal & Round Work is equal to Round Goal everything resets to 0
     */
    public void countUpExerciseAndRound() {

        if (preshaker < 0) {
            preshaker++;
        } else {
            if (textViewtoInt(exerciseGoalView) > 0 || exerciseWorkInt < 0) {
                exerciseWorkInt++;
            }
            if (exerciseWorkInt >= 0) {
                exerciseWorkView.setText(toString(exerciseWorkInt));
                saveScore(EXERCISE_WORK_STATE, exerciseWorkInt);
                try {
                    onPause();
                    TimeUnit.SECONDS.sleep(1);
                    onResume();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (exerciseWorkInt == textViewtoInt(exerciseGoalView) && textViewtoInt(exerciseGoalView) != 0) {
                roundWorkInt++;
                roundWorkView.setText(toString(roundWorkInt));
                saveScore(ROUND_WORK_STATE, roundWorkInt);
                exerciseWorkInt = resetCounter(exerciseWorkView);
                saveScore(EXERCISE_WORK_STATE, exerciseWorkInt);
                if (roundWorkInt == textViewtoInt(roundGoalView)) {
                    exerciseWorkInt = resetCounter(exerciseWorkView);
                    roundWorkInt = resetCounter(roundWorkView);
                    saveScore(EXERCISE_WORK_STATE, exerciseWorkInt);
                    saveScore(ROUND_WORK_STATE, exerciseWorkInt);
                }
            }
        }


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