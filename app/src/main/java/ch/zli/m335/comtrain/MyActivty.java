package ch.zli.m335.comtrain;

import android.content.Intent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public abstract class MyActivty extends AppCompatActivity {

    public Intent startMainActivity;
    public Intent startStepCounterActivity;
    public Intent startWorkoutActivity;
    public Intent startPushUpActivity;

    public boolean isPaused = false;
    ImageView mainSvg;
    ImageView pushUpSvg;
    ImageView stepCounterSvg;
    ImageView workoutSvg;


    public String toString(Object number) {
        return String.valueOf(number);
    }

    public void changeActivities(Intent startactivity){
        startActivity(startactivity);
        finish();
    }

    public int resetCounter(TextView counterView){
        counterView.setText(toString(0));
        return 0;
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

    public void renderTextView(TextView renderView, String newText) {
        if (renderView != null) {
            renderView.setText(newText);
        }
    }

    public void createNavigation() {
        startMainActivity = new Intent(this, MainActivity.class);
        startPushUpActivity = new Intent(this, PushUpActivity.class);
        startStepCounterActivity = new Intent(this, StepCoutnerActivity.class);
        startWorkoutActivity = new Intent(this, WorkoutActivity.class);

        ImageView mainSvg = findViewById(R.id.MainToMain);
        ImageView pushUpSvg = findViewById(R.id.MainToPushUp);
        ImageView stepCounterSvg = findViewById(R.id.MainToStepCounter);
        ImageView workoutSvg = findViewById(R.id.MainToWorkout);

        mainSvg.setOnClickListener(v -> changeActivities(startMainActivity));
        pushUpSvg.setOnClickListener(v -> changeActivities(startPushUpActivity));
        stepCounterSvg.setOnClickListener(v -> changeActivities(startStepCounterActivity));
        workoutSvg.setOnClickListener(v -> changeActivities(startWorkoutActivity));
    }


}
