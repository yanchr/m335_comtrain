package ch.zli.m335.comtrain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    Intent startPushUpActivity;
    Intent startStepCounterActivity;
    Intent startWorkoutActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startPushUpActivity = new Intent(this, PushUpActivity.class);
        startStepCounterActivity = new Intent(this, StepCoutnerActivity.class);
        startWorkoutActivity = new Intent(this, WorkoutActivity.class);

        ImageView PushUpSvg = findViewById(R.id.MainToPushUp);
        ImageView StepCounterSvg = findViewById(R.id.MainToStepCounter);
        ImageView WorkoutSvg = findViewById(R.id.MainToWorkout);

        PushUpSvg.setOnClickListener(v -> changeActivities(startPushUpActivity));
        StepCounterSvg.setOnClickListener(v -> changeActivities(startStepCounterActivity));
        WorkoutSvg.setOnClickListener(v -> changeActivities(startWorkoutActivity));
    }

    public void changeActivities(Intent startactivity){
        startActivity(startactivity);
        finish();
    }

}