package ch.zli.m335.comtrain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class WorkoutActivity extends AppCompatActivity {

    Intent startMainActivity;
    Intent startPushUpActivity;
    Intent startStepCounterActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
    }

    @Override
    protected void onStart() {
        super.onStart();
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
}