package com.jameschin.android.firstglobalcapital;

import java.text.NumberFormat;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TransactionAdapter extends ArrayAdapter<TransactionData> {
	private final Context activity;
	private final List<TransactionData> transactions;
	
	public TransactionAdapter(Context activity, List<TransactionData> objects) {
		super(activity, R.layout.transaction_list_item, objects);
		this.activity = activity;
		this.transactions = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        TransactionItemView tiView = null;
 
        if(rowView == null)
        {
            // Get a new instance of the row layout view
        	LayoutInflater inflater = ((Activity) activity).getLayoutInflater();
            rowView = inflater.inflate(R.layout.transaction_list_item, null);
 
            // Hold the view objects in an object, so they don't need to be re-fetched
            tiView = new TransactionItemView();
            tiView.description = (TextView) rowView.findViewById(R.id.transaction_description);
            tiView.id = (TextView) rowView.findViewById(R.id.transaction_id);
            tiView.amount = (TextView) rowView.findViewById(R.id.transaction_amount);
            tiView.type_image = (ImageView) rowView.findViewById(R.id.transaction_type_image);
 
            // Cache the view objects in the tag, so they can be re-accessed later
            rowView.setTag(tiView);
        } else {
            tiView = (TransactionItemView) rowView.getTag();
        }
 
        // Transfer the data from the data object to the view objects
        TransactionData currentTransaction = (TransactionData) transactions.get(position);
        tiView.description.setText(currentTransaction.getDescription());
        tiView.id.setText("  " + currentTransaction.getId());
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        tiView.amount.setText(formatter.format(currentTransaction.getAmount()));
        
        // Set image
        if (currentTransaction.getType().equals("Checking")) 
        	tiView.type_image.setImageResource(R.drawable.bank);
        else if (currentTransaction.getType().equals("Savings")) 
        	tiView.type_image.setImageResource(R.drawable.bank);
        else if (currentTransaction.getType().equals("Money_Market")) 
        	tiView.type_image.setImageResource(R.drawable.bank);
        else if (currentTransaction.getType().equals("Credit_Card")) 
        	tiView.type_image.setImageResource(R.drawable.credit_card);
        else if (currentTransaction.getType().equals("Recurring_Payment")) 
        	tiView.type_image.setImageResource(R.drawable.recurring);
        else if (currentTransaction.getType().equals("Scheduled_Payment")) 
        	tiView.type_image.setImageResource(R.drawable.scheduled);
        else if (currentTransaction.getType().equals("Check")) 
        	tiView.type_image.setImageResource(R.drawable.payment);
        else if (currentTransaction.getType().equals("Payment")) 
        	tiView.type_image.setImageResource(R.drawable.payment);
        else if (currentTransaction.getType().equals("Transfer")) 
        	tiView.type_image.setImageResource(R.drawable.transfer);
        else if (currentTransaction.getType().equals("Deposit")) 
        	tiView.type_image.setImageResource(R.drawable.money);
        else
        	tiView.type_image.setImageResource(R.drawable.ic_launcher);
 
        return rowView;
    }
 
    protected static class TransactionItemView {
    	protected ImageView type_image;
        protected TextView description;
        protected TextView id;
        protected TextView amount;
    }
}
