package ch.zli.m335.comtrain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class StepCoutnerActivity extends AppCompatActivity {

    Intent startMainActivity;
    Intent startPushUpActivity;
    Intent startWorkoutActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_coutner);
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
    }

    public void changeActivities(Intent startactivity){
        startActivity(startactivity);
        finish();
    }
}