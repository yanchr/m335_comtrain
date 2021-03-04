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

    ImageView mainSvg;
    ImageView pushUpSvg;
    ImageView stepCounterSvg;
    ImageView workoutSvg;

    public boolean isPaused = false;

    public String toString(Object number) {
        return String.valueOf(number);
    }

    public void changeActivities(Intent startactivity){
        startActivity(startactivity);
        finish();
    }

    public int textViewtoInt(TextView textView){
        return Integer.parseInt(textView.getText().toString());
    }

    public int resetCounter(TextView counterView){
        counterView.setText(toString(0));
        return 0;
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

        mainSvg = findViewById(R.id.MainToMain);
        pushUpSvg = findViewById(R.id.MainToPushUp);
        stepCounterSvg = findViewById(R.id.MainToStepCounter);
        workoutSvg = findViewById(R.id.MainToWorkout);

        mainSvg.setOnClickListener(v -> changeActivities(startMainActivity));
        pushUpSvg.setOnClickListener(v -> changeActivities(startPushUpActivity));
        stepCounterSvg.setOnClickListener(v -> changeActivities(startStepCounterActivity));
        workoutSvg.setOnClickListener(v -> changeActivities(startWorkoutActivity));
    }





}
