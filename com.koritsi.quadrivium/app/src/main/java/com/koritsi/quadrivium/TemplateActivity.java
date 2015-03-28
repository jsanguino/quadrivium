package com.koritsi.quadrivium;

import java.util.HashMap;

import android.R.drawable;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.koritsi.quadrivium.es.R;

public class TemplateActivity extends Activity implements OnInitListener
 {
	static final int MENU_SETTINGS = 1;
	static final int MENU_ABOUT = 2;
	static final int MENU_MORE = 3;
	
	public static final int SOUND_CLICK = 1;
	
	static TextToSpeech mTts;
	static SharedPreferences sharedPrefs;

	static boolean sound = true;
	static boolean vibration = true;
	static boolean tts = false;
	static boolean keepon = false;
	static boolean clock = false;
	static int clock_answer = 10;
	static int clock_question = 3;
	
	static SoundPool soundPool;  
	static HashMap<Integer, Integer> soundPoolMap;  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// No Statusbar
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// No Titlebar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		requestWindowFeature(Window.FEATURE_PROGRESS);
	}
	
	@Override
	protected void onResume() {
		
		if (sharedPrefs == null) sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		sound = sharedPrefs.getBoolean("sound", true);
		vibration = sharedPrefs.getBoolean("vibration", true);
		tts = sharedPrefs.getBoolean("tts", false);
		keepon = sharedPrefs.getBoolean("keepon", false);
		clock = sharedPrefs.getBoolean("clock", false);
		try {
			clock_answer = Integer.parseInt(sharedPrefs.getString("clock_answer", "15"));
		} catch (NumberFormatException e) {
			clock_answer = 15;
		}
		try {
			clock_question = Integer.parseInt(sharedPrefs.getString("clock_question", "5"));
		} catch (NumberFormatException e) {
			clock_question = 5;
		}
		
		if (sound) {
			if (soundPool == null) initSounds();
		}
		
		if (tts) {
			if (mTts == null) mTts = new TextToSpeech(getApplicationContext(), this);
		} else {
			mTts = null;
		}
		
		super.onResume();
	}
	
	void speak(String text) {
		if (tts && mTts != null) {
			mTts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
		}
	}
	
	void vibrate() {
		if (!vibration) return;
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(40);
	}
	
	void notify(int text) {
		if (mTts != null) {
			mTts.speak(getResources().getText(text).toString(), TextToSpeech.QUEUE_ADD, null);
		}
		
		Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP, 0, 0);
		toast.show();
	}
	
	public void onInit(int status) {
		if (status == TextToSpeech.ERROR) {
			notify(R.string.error_tts);
		}
	}
	
	@SuppressLint("UseSparseArrays")
	private void initSounds() {  
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);  
		soundPoolMap =new HashMap<Integer, Integer>();
		soundPoolMap.put(SOUND_CLICK, soundPool.load(getApplicationContext(), R.raw.sound_click, 1));
	}  

	public void playSound(int soundId) {
		if (!sound) return;
	    // The next 4 lines calculate the current volume in a scale of 0.0 to 1.0  
	    AudioManager mgr = (AudioManager)getApplicationContext().getSystemService(Context.AUDIO_SERVICE);  
	    float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);  
	    float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);      
	    float volume = streamVolumeCurrent / streamVolumeMax;  
	        
	    // Play the sound with the correct volume  
	    soundPool.play(soundPoolMap.get(soundId), volume, volume, 1, 0, 1f);       
	}  
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(0, MENU_SETTINGS, 0, R.string.menu_settings).setIcon(drawable.ic_menu_preferences);
		menu.add(0, MENU_ABOUT, 0, R.string.menu_about).setIcon(drawable.ic_menu_info_details);
		menu.add(0, MENU_MORE, 0, R.string.menu_more).setIcon(drawable.ic_menu_add);

		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_SETTINGS:
			Intent preferencesIntent = new Intent(getApplicationContext(),
					PreferencesActivity.class);
			startActivity(preferencesIntent);
			return true;
		case MENU_ABOUT:
			Intent aboutIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://jabara.no-ip.org/koritsi/"));
			startActivity(aboutIntent);
			break;
		case MENU_MORE:
			Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:Koritsi"));
			startActivity(marketIntent);
			break;
		}
		return false;
	}

}