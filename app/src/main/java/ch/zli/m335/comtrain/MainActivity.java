package ch.zli.m335.comtrain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends MyActivty {

    int stepCounterInt = 0;
    private int time;
    TextView testDataView;
    Calendar calendar;

    private static final String STEP_COUNT_MAIN_STATE = "stepCountMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        preferences = getPreferences(MODE_PRIVATE);
        calendar = Calendar.getInstance();;



        createNavigation();
        loadSteps(savedInstanceState);
        resetCounterAtMidnight();
    }

    private void loadSteps(Bundle savedInstanceState) {
        testDataView = findViewById(R.id.testData);
        // Preserve UI state
        if (savedInstanceState != null && savedInstanceState.containsKey(STEP_COUNT_MAIN_STATE)) {
            this.stepCounterInt = savedInstanceState.getInt(STEP_COUNT_MAIN_STATE);
        } else {
            // Restore saved application data
            this.stepCounterInt = preferences.getInt(STEP_COUNT_MAIN_STATE, this.stepCounterInt);
        }
        if (stepCounterInt == 0) {
            if (abstractSteps != 0) {
                stepCounterInt = abstractSteps;
            } else {
                renderTextView(this.testDataView, "Make some steps first!");
            }
        }
        if (stepCounterInt != 0) {
            renderTextView(this.testDataView, "You have made " + toString(abstractSteps) + " Steps today");
        }
    }

    public void resetCounterAtMidnight(){
        if (calendar.get(Calendar.HOUR_OF_DAY) * 24 + calendar.get(Calendar.MINUTE) == 0){
            stepCounterInt = resetCounter(testDataView);
            saveScore(STEP_COUNT_MAIN_STATE, stepCounterInt);
        }
    }



}