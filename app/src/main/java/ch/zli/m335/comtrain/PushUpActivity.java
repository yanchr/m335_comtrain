package ch.zli.m335.comtrain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class PushUpActivity extends AppCompatActivity {

    Intent startMainActivity;
    Intent startStepCounterActivity;
    Intent startWorkoutActivity;

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

        ImageView MainSvg = findViewById(R.id.MainToMain);
        ImageView StepCounterSvg = findViewById(R.id.MainToStepCounter);
        ImageView WorkoutSvg = findViewById(R.id.MainToWorkout);

        MainSvg.setOnClickListener(v -> changeActivities(startMainActivity));
        StepCounterSvg.setOnClickListener(v -> changeActivities(startStepCounterActivity));
        WorkoutSvg.setOnClickListener(v -> changeActivities(startWorkoutActivity));
    }

    public void changeActivities(Intent startactivity){
        startActivity(startactivity);
        finish();
    }


}