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
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

import java.util.Objects;

public class WorkoutActivity extends AppCompatActivity{

    Intent startMainActivity;
    Intent startPushUpActivity;
    Intent startStepCounterActivity;

    Sensor sensor;
    SensorManager sensorManager;

    //default
    private final int exerciseDefaultGoal = 3;
    private final int roundDefaultGoal = 6;
    private int exerciseWorkInt = 0;
    private int roundWorkInt = 0;

    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }

    @Override
    protected void onStart() {
        super.onStart();
        /**
         * creates the Navigation and intents to change the Activity
         */
        createNavigation();

        /**
         * gets all Items from the xml related to Exercise and Round
         */
        createWorkAndGoalViews();

        // activateShakeSensor();
    }

    public String toString(Object number) {
        return String.valueOf(number);
    }

    public void createNavigation() {
        startMainActivity = new Intent(this, MainActivity.class);
        startPushUpActivity = new Intent(this, PushUpActivity.class);
        startStepCounterActivity = new Intent(this, StepCoutnerActivity.class);

        ImageView MainSvg = findViewById(R.id.MainToMain);
        ImageView PushUpSvg = findViewById(R.id.MainToPushUp);
        ImageView StepCounterSvg = findViewById(R.id.MainToStepCounter);

        MainSvg.setOnClickListener(v -> changeActivities(startMainActivity));
        PushUpSvg.setOnClickListener(v -> changeActivities(startPushUpActivity));
        StepCounterSvg.setOnClickListener(v -> changeActivities(startStepCounterActivity));
    }

    public void changeActivities(Intent startactivity){
        startActivity(startactivity);
        finish();
    }

    public void createWorkAndGoalViews() {
        TextView exerciseWorkView = findViewById(R.id.exerciseWork);
        TextView roundWorkView = findViewById(R.id.roundWork);
        TextView exerciseGoalView = findViewById(R.id.exerciseGoal);
        TextView roundGoalView = findViewById(R.id.roundGoal);

        //inputs Goals of Exercise and Round from Input Field or the default value
        inputGoals(exerciseGoalView, roundGoalView);
        enableResetBtnListener(exerciseWorkView, roundWorkView, exerciseGoalView, roundGoalView);
    }

    public void inputGoals(TextView exerciseGoalView, TextView roundGoalView){
        TextInputEditText exerciseInput = findViewById(R.id.exerciseInput);
        TextInputEditText roundInput = findViewById(R.id.roundInput);
        Button startWorkoutBtn = findViewById(R.id.startWorkoutBtn);

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



    public void enableResetBtnListener(TextView exerciseWorkView, TextView roundWorkView, TextView exerciseGoalView, TextView roundGoalView){
        Button resetBtn = findViewById(R.id.workoutResetBtn);
        resetBtn.setOnClickListener(v -> {
            exerciseWorkView.setText(toString(0));
            roundWorkView.setText(toString(0));
        });

        resetBtn.setOnLongClickListener(v -> {
            exerciseWorkView.setText(toString(0));
            roundWorkView.setText(toString(0));
            exerciseGoalView.setText(toString(0));
            roundGoalView.setText(toString(0));
            return false;
        });
    }

   /* public void activateShakeSensor() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }*/

    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            if (mAccel > 12) {
                Toast.makeText(getApplicationContext(), "Shake event detected", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    @Override
    protected void onResume() {
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }
    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }
}