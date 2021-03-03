package ch.zli.m335.comtrain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class WorkoutActivity extends AppCompatActivity {

    Intent startMainActivity;
    Intent startPushUpActivity;
    Intent startStepCounterActivity;

    private final int exerciseGoalInt = 3;
    private final int roundGoalInt = 6;

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

        inputGoals();
    }

    public void changeActivities(Intent startactivity){
        startActivity(startactivity);
        finish();
    }

    public void inputGoals(){
        TextView exerciseGoalView = findViewById(R.id.exerciseGoal);
        TextView roundGoalView = findViewById(R.id.roundGoal);
        TextInputEditText exerciseInput = findViewById(R.id.exerciseInput);
        TextInputEditText roundInput = findViewById(R.id.roundInput);
        Button startWorkoutBtn = findViewById(R.id.startWorkoutBtn);

        startWorkoutBtn.setOnClickListener(v -> {
            inputGoal(exerciseGoalView, exerciseInput, exerciseGoalInt);
            inputGoal(roundGoalView, roundInput, roundGoalInt);
        });
    }

    public void inputGoal(TextView goalView, TextInputEditText goalInput, int defaultInt){
            if (Objects.requireNonNull(goalInput.getText()).length() > 0) {
                goalView.setText(String.valueOf(goalInput.getText()));
            } else {
                goalView.setText(String.valueOf(defaultInt));
            }
    }


}