package com.koritsi.quadrivium;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

import android.content.Context;
import android.util.Log;
import com.koritsi.quadrivium.es.R;

public class Questions {

	public static final int CATEGORY_ART             = 0;
	public static final int CATEGORY_HERITAGE        = 1;
	public static final int CATEGORY_PUBLIC_LIFE     = 2;
	public static final int CATEGORY_WAR             = 3;
	public static final int CATEGORY_MITHOLOGY       = 4;
	public static final int CATEGORY_PRIVATE_LIFE    = 5;
	public static final int CATEGORY_RANDOM          = 6;
	
	public static Integer[] CATEGORY_IMAGES = {
		R.drawable.art,
		R.drawable.heritage,
		R.drawable.public_life,
		R.drawable.war,
		R.drawable.mithology,
		R.drawable.private_life,
		R.drawable.random
	};
	
	public static Integer[] CATEGORY_STRINGS = {
		R.string.category_art,
		R.string.category_heritage,
		R.string.category_public_life,
		R.string.category_war,
		R.string.category_mithology,
		R.string.category_private_life,
		R.string.category_random
	};
	
//	public static Bitmap bitmaps[];

//	static {
//		bitmaps = new Bitmap[CATEGORY_IMAGES.length];
//		for (int i = 0; i < CATEGORY_IMAGES.length; i++) {
//			bitmaps[i] = Bitmap.c
//		}
//	}
	
	int category, start, end;
	String question, correctAnswer, str;
	
	void nextQuestion(Context context, int cat) {
		category = cat;
		
		Random r = new Random(System.currentTimeMillis());

		if (category == CATEGORY_RANDOM) {
			category = (int) Math.floor(r.nextFloat() * (CATEGORY_STRINGS.length-1));	
		}
		
		String questionFile = "";
		int questionCount = 0;

		switch(category) {
		case CATEGORY_ART:
			questionFile = "Art.txt";
			questionCount = 4114;
			break;
		case CATEGORY_HERITAGE:
			questionFile = "Heritage.txt";
			questionCount = 16;
			break;
		case CATEGORY_PUBLIC_LIFE:
			questionFile = "Public_life.txt";
			questionCount = 5212;
			break;
		case CATEGORY_WAR:
			questionFile = "War.txt";
			questionCount = 17337;
			break;
		case CATEGORY_MITHOLOGY:
			questionFile = "Mithology.txt";
			questionCount = 5365;
			break;
		case CATEGORY_PRIVATE_LIFE:
			questionFile = "Private_life.txt";
			questionCount = 13528;
			break;
		}
		
		int questionNumber = (int) Math.floor(r.nextFloat() * questionCount);
		if ((category == CATEGORY_WAR || category == CATEGORY_PRIVATE_LIFE) && questionNumber > 8000) {
			questionNumber -= 8000;
			questionFile = questionFile + "2";
		}
		
		Log.i("Question", questionFile + ": " + questionNumber);
		try {
			InputStream is = context.getAssets().open(questionFile);
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			// Skips lines
			for (int i = 0; i< questionNumber; i++) {
				reader.readLine();
			}
			question = reader.readLine();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getCategory() {
		return category;
	}
	
	public String getQuestion() {
		return question.substring(0, question.indexOf("*"));
	}
	
	public String[] getAnswer() {
		String string = question.substring(question.indexOf("*") + 1);
		String[] parts = string.split("-");
	
		return parts; // 0-2
	}
	public String getWrongAnswer1() {
		return question.substring(question.indexOf("*") + 2);
	}
	public String getWrongAnswer2() {
		return question.substring(question.indexOf("*") + 3);
	}
}
