package com.shaddyhollow.freedom.hostess;

import java.util.UUID;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.shaddyhollow.areaeditor.FlurryEvents;
import com.shaddyhollow.areaeditor.R;
import com.shaddyhollow.freedom.hostess.dialogs.TextInputDialog;
import com.shaddyhollow.freedom.hostess.dialogs.TextInputDialog.TextInputListener;
import com.shaddyhollow.quicktable.models.CarryOutVisit;
import com.shaddyhollow.util.DateUtils;

public class DiningCarryoutFragment extends Fragment {
	private CarryoutAdapter carryoutAdapter = null;
	@SuppressWarnings("unused")
	private Integer locationID;
	private Integer tenantID;
	Mode mode = null;

	public static DiningCarryoutFragment newInstance(Mode mode, CarryoutAdapter carryoutAdapter, Integer locationID, Integer tenantID) {
		DiningCarryoutFragment fragment = new DiningCarryoutFragment();
		fragment.mode = mode;
		fragment.carryoutAdapter = carryoutAdapter;
		fragment.locationID = locationID;
		fragment.tenantID = tenantID;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.carryout_detail, container, false);
		updateCarryoutView(view);
		return view;
	}

	private void updateCarryoutView(View view) {
		if(view==null) {
			view = getView();
		}
		if(view==null) {
			return;
		}

		TextView customerName = (TextView)view.findViewById(R.id.patron_name);
		TextView customerPhone = (TextView)view.findViewById(R.id.patron_phone);
		TextView timein = (TextView)view.findViewById(R.id.patron_timein);
		TextView patronstatus = (TextView)view.findViewById(R.id.patron_status);
		TextView orderstatus = (TextView)view.findViewById(R.id.order_status);
		TextView comments = (TextView)view.findViewById(R.id.patron_comments);
		TextView comments_label = (TextView)view.findViewById(R.id.label_patron_comments);
		Button pageButton = (Button)view.findViewById(R.id.btnpatron_page);
		Button textButton = (Button)view.findViewById(R.id.btnpatron_text);
		Button printButton = (Button)view.findViewById(R.id.btnpatron_print);
		Button removeButton = (Button)view.findViewById(R.id.btnpatron_remove);

		final CarryOutVisit visit = carryoutAdapter.getSelection();
		
		int childcount = ((ViewGroup)view).getChildCount();
		for(int i=0;i<childcount;i++) {
			View curView = ((ViewGroup)view).getChildAt(i);
			curView.setVisibility(visit==null ? View.INVISIBLE : View.VISIBLE);
		}
		
		if(visit==null) {
			return;
		}
		
		customerName.setText(visit.getName());
		customerPhone.setText(visit.getPhone_number());
		timein.setText(DateUtils.getTime(visit.getOrder_time()));
		orderstatus.setText(visit.getOrderStatus());
		patronstatus.setText(visit.getPatronStatus());
		
		if(visit.getSpecial_requests()!=null && visit.getSpecial_requests().length()>0) {
			comments.setText(visit.getSpecial_requests());
			comments.setVisibility(View.VISIBLE);
			comments_label.setVisibility(View.VISIBLE);
		} else {
			comments.setVisibility(View.GONE);
			comments_label.setVisibility(View.GONE);
		}
		
		// update page button
		if (visit.getPhone_number() != null && !visit.getPhone_number().isEmpty()) {
			pageButton.setAlpha(1.0f);
			pageButton.setEnabled(true);
			
			textButton.setAlpha(1.0f);
			textButton.setEnabled(true);
		} else {
			pageButton.setAlpha(0.5f);
			pageButton.setEnabled(false);

			textButton.setAlpha(0.5f);
			textButton.setEnabled(false);
		}

		printButton.setAlpha(1.0f);
		printButton.setEnabled(true);

		pageButton.setVisibility(View.VISIBLE);
		printButton.setVisibility(View.VISIBLE);
		removeButton.setVisibility(View.VISIBLE);

		printButton.setVisibility(View.VISIBLE);
		printButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				FlurryAgent.logEvent(FlurryEvents.CARRYOUT_PRINT.name());
				((CarryoutActivity)getActivity()).printCarryOutReceipt(visit);			
			}
		});

		pageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
	        	FlurryAgent.logEvent(FlurryEvents.PATRON_PAGE.name());
				((CarryoutActivity)getActivity()).pageCarryout(visit);			
			}
		});
		
		textButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				TextInputDialog dlg = TextInputDialog.newInstance("Send Text to " + visit.getName(), "", new TextInputListener() {
					@Override
					public void onValueSelected(String value) {
			        	FlurryAgent.logEvent(FlurryEvents.PATRON_TEXT.name());
						((CarryoutActivity)getActivity()).textCarryOut(visit, value);			
					}
				});
				dlg.show(getFragmentManager(), "comment");
			}
		});

		removeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
			    new AlertDialog.Builder(getActivity())
		        .setIcon(android.R.drawable.ic_dialog_alert)
		        .setTitle("Remove Order")
		        .setMessage("Are you sure you want to remove order for " + visit.getName() + "?")
		        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			        @Override
			        public void onClick(DialogInterface dialog, int which) {
			        	FlurryAgent.logEvent(FlurryEvents.CARRYOUT_REMOVE.name());
				     	((CarryoutActivity)getActivity()).deleteCarryoutVisit(visit);
						((CarryoutActivity)getActivity()).updateDetails(Mode.SECTION_LIST);
						DiningCarryoutFragment.this.carryoutAdapter.clearSelectionPosition();
						DiningCarryoutFragment.this.carryoutAdapter.notifyDataSetChanged();
			        }
			    })
			    .setNegativeButton("No", null)
			    .show();

			}
		});
		
	}
	
}
