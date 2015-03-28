package com.koritsi.quadrivium;

import java.util.Locale;
import java.util.Random;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.RadioButton;
import com.koritsi.quadrivium.es.R;

public class QuestionActivity extends TemplateActivity implements OnClickListener {

	public static int STATUS_QUESTION = 0;
	public static int STATUS_ANSWER = 1;
	
	public static int category;
	static int status = 0;
		
	ImageView imageView;
	ImageButton showAnswer;
	TextView categoryTextView, question, answer;
	RadioButton respuesta, respuesta2, respuesta3;
	Questions questions;
	PowerManager powerManagement = null;
	PowerManager.WakeLock wakeLock = null;
	String resp_1, resp_2, resp_3, correcta;
	int start, end;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		
        setContentView(R.layout.question);
	    
        imageView = (ImageView) findViewById(R.id.ImageView01);
        imageView.setImageResource(Questions.CATEGORY_IMAGES[0]);

        categoryTextView = (TextView) findViewById(R.id.Category);
        question = (TextView) findViewById(R.id.Question);
        answer = (TextView) findViewById(R.id.Answer);
        respuesta = (RadioButton) findViewById(R.id.resp_1);
        respuesta2 = (RadioButton) findViewById(R.id.resp_2);
        respuesta3 = (RadioButton) findViewById(R.id.resp_3);
        showAnswer = (ImageButton) findViewById(R.id.ImageButton);
        showAnswer.setOnClickListener(this);
        //Reaccionar a eventos del RadioButton
        respuesta.setOnClickListener(new RadioButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                //tvRadioGroup.setText("RadioGroup: Radio 1 seleccionado");
            	questions = new Questions();
                nextQuestion();
            }
        });
        respuesta2.setOnClickListener(new RadioButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                //tvRadioGroup.setText("RadioGroup: Radio 2 seleccionado");
            	questions = new Questions();
                nextQuestion();
            }
        });
        respuesta3.setOnClickListener(new RadioButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                //tvRadioGroup.setText("RadioGroup: Radio 3 seleccionado");
            	questions = new Questions();
                nextQuestion();
            }
        });
        //Fin respecto al RadioGroup
        
        //questions = new Questions();
        //nextQuestion();
    }

    /*
    public void onClick(View v) {
		if (v == showAnswer) {
			vibrate();
			playSound(SOUND_CLICK);
			if (status == STATUS_QUESTION) {
				answer.setText(questions.getAnswer().toUpperCase(Locale.getDefault()));
				speak(questions.getAnswer());
				status = STATUS_ANSWER;
			} else {
				nextQuestion();
			}
			restartTimer();
		}
	}*/
    public void onClick(View v) {
		if (v == showAnswer) {
			vibrate();
			playSound(SOUND_CLICK);
			nextQuestion();
			restartTimer();
		}
	}
	private void nextQuestion() {
		start= 0;
		end= 3;
		status = STATUS_QUESTION;
		questions.nextQuestion(this, category);
        
        imageView.setImageResource(Questions.CATEGORY_IMAGES[questions.getCategory()]);
        categoryTextView.setText(Questions.CATEGORY_STRINGS[questions.getCategory()]);
        question.setText(questions.getQuestion());
        int r; 
		int rr = 0; 
		int rrr = 0;
	
        Random random = new Random();	
		r = random.nextInt(end - start);
		if (r==0){rr=1; rrr=2;}
		if (r==1){rr=2; rrr=0;}
		if (r==2){rr=0; rrr=1;}
		
        respuesta.setText(questions.getAnswer()[r].toUpperCase(Locale.getDefault()));
        respuesta2.setText(questions.getAnswer()[rr].toUpperCase(Locale.getDefault()));
        respuesta3.setText(questions.getAnswer()[rrr].toUpperCase(Locale.getDefault()));
   
        answer.setText(null);
        speak(questions.getQuestion());
	}

	@Override
	protected void onPause() {
		keepOnStop();
		stopTimer();
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		keepOnStart();
		startTimer();
		super.onResume();
	}
	
	private Handler timer = new Handler();
	private Runnable onTimerRunnable = new Runnable() {
		public void run() {
			onClick(showAnswer);
		}
	};
	private void startTimer() {
		if (!clock) return;
		if (status == STATUS_QUESTION) {
			timer.postDelayed(onTimerRunnable, clock_answer*1000);
		} else if (status == STATUS_ANSWER) {
			timer.postDelayed(onTimerRunnable, clock_question*1000);
		} else { return; } // Just in case
	}
	private void stopTimer() {
		timer.removeCallbacks(onTimerRunnable);
	}
	private void restartTimer() {
		stopTimer();
		startTimer();
	}

	private void keepOnStart() {
		if (keepon) {
			if (powerManagement == null) {
				powerManagement = (PowerManager) getSystemService(Context.POWER_SERVICE);
			}
			if (wakeLock == null) {
				wakeLock = powerManagement.newWakeLock(
						PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE,
						"Quadrivium keep on");
			}
			wakeLock.acquire();
		}
	}
	
	private void keepOnStop() {
		if ((wakeLock != null) && (wakeLock.isHeld())) {
			wakeLock.release();
		}
	}
}