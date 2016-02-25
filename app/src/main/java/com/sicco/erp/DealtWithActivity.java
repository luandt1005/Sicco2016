package com.sicco.erp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.sicco.erp.adapter.SpinnerStatusAdapter;
import com.sicco.erp.adapter.ActionAdapter;
import com.sicco.erp.model.Dispatch;
import com.sicco.erp.model.Dispatch.OnLoadListener;
import com.sicco.erp.model.Status;
import com.sicco.erp.service.GetAllNotificationService;
import com.sicco.erp.util.Keyboard;
import com.sicco.erp.util.ViewDispatch;

public class DealtWithActivity extends Activity implements OnClickListener,
		OnItemClickListener {

	private LinearLayout searchView, connectError;
	private ImageView back, search, close, empty;
	private EditText editSearch;
	private TextView emptyView, something, something1;
	private ListView listDispatch;
	private ProgressBar loading;
	private Button retry;
	private ActionAdapter adapter;
	private ArrayList<Dispatch> arrDispatch;
	private Dispatch dispatch;
	private ViewDispatch viewDispatch;

	private AlertDialog alertDialog;
	private SpinnerStatusAdapter spinnerStatusAdapter;
	private Spinner spnFilter;
	private TextView title_actionbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_approval);

		OtherActivity.otherActivitySelected = false;

		init();

	}

	private void init() {
		something = (TextView) findViewById(R.id.something);
		something1 = (TextView) findViewById(R.id.something1);
		something.setVisibility(View.GONE);
		something1.setVisibility(View.GONE);
		searchView = (LinearLayout) findViewById(R.id.searchview);
		back = (ImageView) findViewById(R.id.back);
		search = (ImageView) findViewById(R.id.search);
		close = (ImageView) searchView.findViewById(R.id.close);
		empty = (ImageView) searchView.findViewById(R.id.empty);
		editSearch = (EditText) searchView.findViewById(R.id.edit_search);
		emptyView = (TextView) findViewById(R.id.empty_view);
		listDispatch = (ListView) findViewById(R.id.listDispatch);
		loading = (ProgressBar) findViewById(R.id.loading);
		retry = (Button) findViewById(R.id.retry);
		connectError = (LinearLayout) findViewById(R.id.connect_error);
		spnFilter = (Spinner) findViewById(R.id.spnFilter);
		title_actionbar = (TextView) findViewById(R.id.title_actionbar);
		title_actionbar.setText(getResources().getString(R.string.cv_xu_ly));
		spnFilter.setVisibility(View.GONE);
		// click
		back.setOnClickListener(this);
		search.setOnClickListener(this);
		close.setOnClickListener(this);
		empty.setOnClickListener(this);
		retry.setOnClickListener(this);
		listDispatch.setOnItemClickListener(this);

		// // set adapter
		// dispatch = new Dispatch(DealtWithActivity.this);
		// arrDispatch = dispatch.getData(DealtWithActivity.this, getResources()
		// .getString(R.string.api_get_dispatch_handle),
		// new OnLoadListener() {
		//
		// @Override
		// public void onStart() {
		// loading.setVisibility(View.VISIBLE);
		// connectError.setVisibility(View.GONE);
		// }
		//
		// @Override
		// public void onSuccess() {
		// loading.setVisibility(View.GONE);
		// adapter.notifyDataSetChanged();
		// if (adapter.getCount() <= 0) {
		// listDispatch.setEmptyView(emptyView);
		// }
		// }
		//
		// @Override
		// public void onFalse() {
		// loading.setVisibility(View.GONE);
		// connectError.setVisibility(View.VISIBLE);
		// }
		// }, 0);
		// adapter = new ActionAdapter(DealtWithActivity.this, arrDispatch, 0);
		// listDispatch.setAdapter(adapter);

		// setFilter
		// set spinner
		ArrayList<Status> listStatus = new ArrayList<Status>();
		listStatus.add(new Status(getResources().getString(R.string.all), Long
				.parseLong("-1")));
		listStatus.add(new Status(getResources()
				.getString(R.string.need_handle), Long.parseLong("2")));
		listStatus.add(new Status(getResources().getString(R.string.handling),
				Long.parseLong("3")));

		spinnerStatusAdapter = new SpinnerStatusAdapter(
				getApplicationContext(), listStatus);
		spnFilter.setAdapter(spinnerStatusAdapter);

		spnFilter.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Status status = (Status) parent.getAdapter().getItem(position);
				Log.d("NgaDV", "status.getKey(): " + status.getKey());

				if (!arrDispatch.isEmpty()) {
					adapter = new ActionAdapter(DealtWithActivity.this,
							dispatch.filterDispatch(status.getKey(),
									arrDispatch), 0);
					listDispatch.setAdapter(adapter);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.back:
			finish();
			break;
		case R.id.search:
			showSearchView();
			break;
		case R.id.close:
			closeSearchView();
			break;
		case R.id.empty:
			editSearch.setText("");
			break;
		case R.id.retry:
			adapter.setData(dispatch.getData(DealtWithActivity.this,
					getResources().getString(R.string.api_get_dispatch_handle),
					new OnLoadListener() {

						@Override
						public void onStart() {
							loading.setVisibility(View.VISIBLE);
							connectError.setVisibility(View.GONE);
						}

						@Override
						public void onSuccess() {
							loading.setVisibility(View.GONE);
							adapter.notifyDataSetChanged();
							if (adapter.getCount() <= 0) {
								listDispatch.setEmptyView(emptyView);
							}
						}

						@Override
						public void onFalse() {
							loading.setVisibility(View.GONE);
							connectError.setVisibility(View.VISIBLE);
						}
					}, 0));
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Dispatch dispatch = (Dispatch) arg0.getAdapter().getItem(arg2);
		viewDispatch = new ViewDispatch(DealtWithActivity.this,
				dispatch.getContent());
	}

	@Override
	public void onBackPressed() {
		if (searchView.getVisibility() == View.VISIBLE) {
			editSearch.setText("");
			searchView.setVisibility(View.GONE);
		} else {
			super.onBackPressed();
		}
	}

	private void showSearchView() {
		searchView.setVisibility(View.VISIBLE);
		searchView.requestFocus();
		Keyboard.showKeyboard(DealtWithActivity.this, editSearch);
		editSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				if (arg0.toString().trim().length() > 0) {
					empty.setVisibility(View.VISIBLE);
				} else {
					empty.setVisibility(View.GONE);
				}
				ArrayList<Dispatch> searchData = dispatch.search(arg0
						.toString().trim(), arrDispatch);
				adapter.setData(searchData);
				adapter.notifyDataSetChanged();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
	}

	private void closeSearchView() {
		Keyboard.hideKeyboard(DealtWithActivity.this, editSearch);
		searchView.setVisibility(View.GONE);
		editSearch.setText("");
	}

	// ToanNM
	@Override
	protected void onStart() {
		super.onStart();
		startGetAllNotificationService();
	}

	void startGetAllNotificationService() {
		Intent intent = new Intent(getApplicationContext(),
				GetAllNotificationService.class);
		intent.putExtra("ACTION", 0);
		getApplicationContext().startService(intent);
	}

	// End of ToanNM

	@Override
	protected void onResume() {

		HomeActivity.checkDate(DealtWithActivity.this);
		// set adapter
		dispatch = new Dispatch(DealtWithActivity.this);
		arrDispatch = dispatch.getData(DealtWithActivity.this, getResources()
				.getString(R.string.api_get_dispatch_handle),
				new OnLoadListener() {

					@Override
					public void onStart() {
						loading.setVisibility(View.VISIBLE);
						connectError.setVisibility(View.GONE);
					}

					@Override
					public void onSuccess() {
						loading.setVisibility(View.GONE);
						adapter.notifyDataSetChanged();
						if (adapter.getCount() <= 0) {
							listDispatch.setEmptyView(emptyView);
						}
					}

					@Override
					public void onFalse() {
						loading.setVisibility(View.GONE);
						connectError.setVisibility(View.VISIBLE);
					}
				}, 0);
		adapter = new ActionAdapter(DealtWithActivity.this, arrDispatch, 0);
		listDispatch.setAdapter(adapter);
		super.onResume();
	}
}