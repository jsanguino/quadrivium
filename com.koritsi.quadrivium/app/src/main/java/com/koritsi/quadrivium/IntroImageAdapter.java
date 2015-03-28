package com.koritsi.quadrivium;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import com.koritsi.quadrivium.es.R;

public class IntroImageAdapter extends BaseAdapter {
	int mGalleryItemBackground;
	private Context mContext;
	int width, height;
	
	Bitmap bitmaps[] = new Bitmap[Questions.CATEGORY_IMAGES.length];
	ImageView view[] = new ImageView[Questions.CATEGORY_IMAGES.length];

	public IntroImageAdapter(Context c, int width, int height) {
		mContext = c;
		this.width = width;
		this.height = height;
		TypedArray a = c.obtainStyledAttributes(R.styleable.default_gallery);
		mGalleryItemBackground = a.getResourceId(R.styleable.default_gallery_android_galleryItemBackground, 0);
		a.recycle();
	}

	public int getCount() {
		return Questions.CATEGORY_IMAGES.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (view[position] == null) {
			ImageView i = new ImageView(mContext);
			i.setImageBitmap(loadImage(position));
			i.setLayoutParams(new Gallery.LayoutParams(width, height));
			i.setScaleType(ImageView.ScaleType.FIT_XY);
			i.setBackgroundResource(mGalleryItemBackground);
			view[position] = i;
		}
		return view[position];
	}
	
	private Bitmap loadImage(int position) {
		if (bitmaps[position] == null) {
			BitmapFactory.Options ops2 = new BitmapFactory.Options();
			int scale = Math.round(400 / width);
			ops2.inSampleSize = scale;
			Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), Questions.CATEGORY_IMAGES[position], ops2);
			bitmaps[position] = bitmap;
		}
        return bitmaps[position];
	}
	
	public void recycle() {
		for (int i = 0; i < bitmaps.length; i++) {
			if (bitmaps[i] != null) bitmaps[i].recycle();
		}
	}
}