package com.sicco.erp.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.sicco.erp.R;
import com.sicco.erp.view.TouchImageView;

public class TouchImageAdapter extends PagerAdapter {
	private Context context;
	private ArrayList<String> data;

	public TouchImageAdapter(Context context, ArrayList<String> data) {
		this.context = context;
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	public String getItem(int i) {
		return data.get(i);
	}

	@Override
	public View instantiateItem(ViewGroup container, int position) {
		TouchImageView img = new TouchImageView(container.getContext());
		Glide.with(context).load(data.get(position))
				.placeholder(R.drawable.image_loading)
				.error(R.drawable.image_error).crossFade().into(img);
		container.addView(img, LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		return img;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

}
