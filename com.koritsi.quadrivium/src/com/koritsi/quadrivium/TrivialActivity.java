package com.koritsi.quadrivium;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.koritsi.quadrivium.es.R;

public class TrivialActivity extends TemplateActivity implements OnClickListener {
	ImageView imageView;
	TextView textView;
	static int selected = 0;
	Bitmap bitmap;
	IntroImageAdapter imageAdapter; 
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
        setContentView(R.layout.intro);
        
        imageView = (ImageView) findViewById(R.id.ImageView01);
        textView = (TextView) findViewById(R.id.Category);

        setCategory(selected);

        ImageButton button = (ImageButton) findViewById(R.id.ImageButton);
        button.setOnClickListener(this);
        
        Gallery g = (Gallery) findViewById(R.id.Gallery);
        imageAdapter = new IntroImageAdapter(this, (int) (dm.widthPixels/3.5), (int) (dm.heightPixels/3.5));
        g.setAdapter(imageAdapter);
        g.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                setCategory(position);
            }
        });
        g.setSelection(selected);
//        g.setOnItemSelectedListener(new OnItemSelectedListener() {
//			public void onNothingSelected(AdapterView<?> arg0) {
//			}
//
//			public void onItemSelected(AdapterView<?> arg0, View arg1,
//					int position, long id) {
//	            	selected = position;
//	                imageView.setImageResource(Questions.CATEGORY_IMAGES[position]);
//	                textView.setText(Questions.CATEGORY_STRINGS[position]);
//			}
//        });
    }

    private void setCategory(int position) {
    	//if (lastSelected == selected) return;
    	selected = position;
        textView.setText(Questions.CATEGORY_STRINGS[position]);

        //imageView.setImageResource(Questions.CATEGORY_IMAGES[position]);
    	Bitmap bitmapNew = BitmapFactory.decodeResource(getResources(), Questions.CATEGORY_IMAGES[position]);
        imageView.setImageBitmap(bitmapNew);
        if (bitmap != null) bitmap.recycle();
        bitmap = bitmapNew;
    }
    
	public void onClick(View v) {
		vibrate();
		playSound(SOUND_CLICK);
		QuestionActivity.category = selected;
		Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		textView = null;
		imageView = null;
		if (bitmap != null) bitmap.recycle();
		if (imageAdapter != null) imageAdapter.recycle();
		super.onDestroy();
		System.gc();
	}
	
	
}