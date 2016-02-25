package com.sicco.erp.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sicco.erp.R;
import com.sicco.erp.SendApprovalActivity;
import com.sicco.erp.model.Dispatch;

public class DispatchAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Dispatch> data;

	public DispatchAdapter(Context context, ArrayList<Dispatch> data) {
		this.context = context;
		this.data = data;
	}

	public void setData(ArrayList<Dispatch> data) {
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Dispatch getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return data.get(arg0).getId();
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		ViewHolder holder;
		final Dispatch dispatch = getItem(position);
		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.item_dispatch,
					viewGroup, false);
			holder = new ViewHolder();
			holder.title = (TextView) view.findViewById(R.id.title);
			holder.description = (TextView) view.findViewById(R.id.description);
			holder.approval = (TextView) view.findViewById(R.id.approval);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		holder.title.setText(dispatch.getNumberDispatch());
		holder.description.setText(dispatch.getDescription());
		
		GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{0, 0});
        drawable.setColor(Color.parseColor(context.getResources().getString(R.color.actionbar_color)));
        drawable.setCornerRadius(context.getResources().getDimension(R.dimen.item_size));
		holder.approval.setBackgroundDrawable(drawable);
		
		holder.approval.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context, SendApprovalActivity.class);
				intent.putExtra("dispatch", dispatch);
				context.startActivity(intent);
			}
		});
		return view;
	}

	private class ViewHolder {
		TextView title;
		TextView description;
		TextView approval;
	}
}
