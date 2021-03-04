package ch.zli.m335.comtrain;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public abstract class MyActivty extends AppCompatActivity {

    boolean isPaused = false;


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

}
