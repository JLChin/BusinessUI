package com.jameschin.android.firstglobalcapital;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;

public class HomeFragment extends SherlockListFragment {
	String mTag;
	
	private List<String> depthStack; //this will hold menu traversal info
	private TextView home_text;
	private ImageView home_image;
	private Button home_topButton;
	private Spinner home_spinner;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		
		depthStack = new ArrayList<String>();
		home_text = (TextView) view.findViewById(R.id.home_text);
		home_image = (ImageView) view.findViewById(R.id.home_image);
		home_topButton = (Button) view.findViewById(R.id.home_top);
		home_spinner = (Spinner) view.findViewById(R.id.home_spinner);
		
		loadAccountsIntoList();
		addItemsOnSpinner();
		setupSpinnerListener();
		setupTopButtonListener();
		setupImageListener();
		
		return view;
	}

	public void setProviderId(String tag) {
		this.mTag = tag;
	}

	public void addItemsOnSpinner() {
		List<String> list = new ArrayList<String>();
		list.add(getString(R.string.home_spinner0)); //My Accounts
		list.add(getString(R.string.home_spinner1)); //Messages
		list.add(getString(R.string.home_spinner2)); //Alerts
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getSherlockActivity(), android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		home_spinner.setAdapter(dataAdapter);
	}
	
	public void setupSpinnerListener() {
		home_spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	}
	
	public class CustomOnItemSelectedListener implements OnItemSelectedListener {
		 
		public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
			if (pos == 0) {
				depthStack.clear(); //restart from root, empty stack
				loadAccountsIntoList();
				getListView().setVisibility(View.VISIBLE);
				home_text.setVisibility(View.GONE);
				home_image.setVisibility(View.GONE);
				home_topButton.setVisibility(View.GONE);
			}
			else if (pos == 1) {
				getListView().setVisibility(View.GONE);
				home_text.setVisibility(View.VISIBLE);
				home_image.setVisibility(View.GONE);
				home_topButton.setVisibility(View.GONE);
				home_text.setText("No new messages.");
			}
			else if (pos == 2) {
				getListView().setVisibility(View.GONE);
				home_text.setVisibility(View.VISIBLE);
				home_image.setVisibility(View.GONE);
				home_topButton.setVisibility(View.GONE);
				home_text.setText("No new alerts.");
			}
		}
		 
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		}	 
	}
	
	public void setupTopButtonListener() {
		home_topButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				depthStack.clear(); // restart from root, empty stack
				loadAccountsIntoList();
				getListView().setVisibility(View.VISIBLE);
				home_text.setVisibility(View.GONE);
				home_image.setVisibility(View.GONE);
				home_topButton.setVisibility(View.GONE);
			}
		});
	}
	
	public void setupImageListener() {
		home_image.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// if at image level, pop item from stack and retrieve account list
				if (depthStack.size() == 2) {
					depthStack.remove(1);
					loadTransactionsIntoList(depthStack.get(0)); // send description as load parameters
					
					getListView().setVisibility(View.VISIBLE);
					home_text.setVisibility(View.GONE);
					home_image.setVisibility(View.GONE);
					home_topButton.setVisibility(View.VISIBLE);
				}
			}
		});
	}
	
	@Override
	public void onListItemClick(ListView parent, View view, int position, long id) {
		
		//if at accounts level, show transactions
		if (depthStack.size() == 0) {
			//store selection
			depthStack.add(((TextView) view.findViewById(R.id.transaction_description)).getText().toString());
			
			loadTransactionsIntoList (((TextView) view.findViewById(R.id.transaction_description)).getText().toString());
			getListView().setVisibility(View.VISIBLE);
			home_text.setVisibility(View.GONE);
			home_image.setVisibility(View.GONE);
			home_topButton.setVisibility(View.VISIBLE);
		}
		
		//if at transactions level, show item (image)
		else if (depthStack.size() == 1) {
			//store selection
			depthStack.add(((TextView) view.findViewById(R.id.transaction_description)).getText().toString());
			
			home_image.setImageResource(R.drawable.sample_check);
			getListView().setVisibility(View.GONE);
			home_text.setVisibility(View.GONE);
			home_image.setVisibility(View.VISIBLE);
			home_topButton.setVisibility(View.VISIBLE);
		}
	}
	
	public void loadAccountsIntoList () {
		List<TransactionData> transactions = new ArrayList<TransactionData>();
		transactions.add(new TransactionData("Checking", "Interest Checking - 6523", "Available Balance", 1209.22));
		transactions.add(new TransactionData("Savings", "Regular Savings - 7689", "Available Balance", 3232.17));
		transactions.add(new TransactionData("Money_Market", "Money Market - 7122", "Available Balance", 2768.92));
		transactions.add(new TransactionData("Credit_Card", "FGC Corporate Card Business Visa - 2231", "Current Balance", 1221.96));

		setListAdapter(new TransactionAdapter(getSherlockActivity(), transactions));
	}
	
	public void loadTransactionsIntoList (String description) {
		List<TransactionData> transactions = new ArrayList<TransactionData>();
		transactions.add(new TransactionData("Check", "Check - 1015", "2/12/11", 612.00));
		transactions.add(new TransactionData("Transfer", "Online Transfer From SAV 4202", "2/12/11 Confirmation# 0568288504", 250.00));
		transactions.add(new TransactionData("Deposit", "Deposit 6523", "2/10/11", 1555.67));
		transactions.add(new TransactionData("Recurring_Payment", "Recurring Payment To Acct 2231", "2/10/11 Confirmation# 4122980317", 24.99));
		transactions.add(new TransactionData("Payment", "Online Payment - PACIFIC SVCS", "2/06/11 ID:CKF256247610", 32.20));
		transactions.add(new TransactionData("Check", "Check - 1014", "2/06/11", 122.30));
		transactions.add(new TransactionData("Scheduled_Payment", "Scheduled Payment To JCC#424", "2/05/11 Confirmation# 0223996132", 48.50));
		transactions.add(new TransactionData("Deposit", "FGC CARD CASHREWARD", "2/04/11 INDN:0000194452055000", 29.70));

		setListAdapter(new TransactionAdapter(getSherlockActivity(), transactions));
	}
}
