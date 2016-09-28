package com.kalendria.kalendria.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Validator {

	public final static boolean isValidEmail(final String email) {
		boolean valid = false;

		if (email != null) {
			valid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
		}
		return valid;
	}	 

	public static boolean isNetworkConnected(final Context context)
	{
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
	}
	
	
	
	
	public static boolean isEmailValid(final String emailAddress) {
		return Validator.isValidEmail(emailAddress);
	}
	
	
	
	
}