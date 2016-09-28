package com.kalendria.kalendria.utility;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.kalendria.kalendria.R;
import com.kalendria.kalendria.activity.Login;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.model.AddToCardVenueModel;
import com.kalendria.kalendria.model.FilerModel;
import com.kalendria.kalendria.model.KAGift;
import com.kalendria.kalendria.singleton.AddToCardSingleTone;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class KalendriaAppController extends MultiDexApplication  {

	public static final String TAG = KalendriaAppController.class.getSimpleName();

	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;

	//Signing Options
	public  GoogleSignInOptions gso;

	public  JSONObject dictGift;
	public  JSONObject dictPromo;
	//google api client
	private GoogleApiClient mGoogleApiClient;


	private static KalendriaAppController mInstance;

	public AddToCardVenueModel selectedAddToCardVenueModel;
	public KAGift selectedGiftVoucher;

	/*
	public String selectedCity;
	public String selectedZipcode;
	public String selectedAddress;
	public String selectedPhone;
	*/
	public  double latitude=25.2048,longitude=55.2708;
	private LocationManager locationManager;
	private String provider;
	public String HTMLString;
	public Location mLastLocation ;

	public ArrayList<FilerModel> mFilterModel = new ArrayList<>();

	@Override
	public void onCreate() {
		super.onCreate();


		//Set value for Live or Development
		Constant.setHOSTURL(true);

		// configure Flurry
		FlurryAgent.setLogEnabled(false);

		// init Flurry
		FlurryAgent.init(this, Constant.Flurry_Api);


		MultiDex.install(this);
		mInstance = this;
		FacebookSdk.sdkInitialize(getApplicationContext());
		AppEventsLogger.activateApp(this);

		//Initializing google signin option
		gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestEmail()
				.build();
		getGoogleClient();

	}
	public static int getResColor(int color)
	{
		return mInstance.getResources().getColor(color);
	}

	public static synchronized KalendriaAppController getInstance() {
		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public ImageLoader getImageLoader() {
		getRequestQueue();	
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(this.mRequestQueue,new LruBitmapCache());
		}
		return this.mImageLoader;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

	public static String getErrorMessage()
	{
		return KalendriaAppController.getInstance().getResources().getString(R.string.request_failed);

	}
	public static boolean isNetworkConnected(final Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
	}

	public static void hideKeyboar(Activity activity)
	{
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); // hide
	}
	public static void hideSoftKeyboard(Activity activity) {
		try {
			InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
			if(inputMethodManager!=null)
				inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);

		}
		catch (Exception ex){ex.printStackTrace();}

	}
	public void showLogoutAlertPopup(final Context context) {



		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Kalendria");
		builder.setMessage(
				"Are you sure you want to logout?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(
									@SuppressWarnings("unused") final DialogInterface dialog, final int id) {

								Constant.setProfileImage("");
								Constant.setProfileImageMedium("");

								SharedPreferences sharedPref; //Initialize the SharedPreference class
								SharedPreferences.Editor editor;
								sharedPref = KalendriaAppController.this.getSharedPreferences(Constant.MyPREFERENCES, 0); //we can use same Reference key or dynamic key
								editor = sharedPref.edit();



								editor.clear();
								editor.commit();
								try {

									signOut();
									/*
									if (mGoogleApiClient != null) {
										//Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
										mGoogleApiClient.disconnect();
										mGoogleApiClient.clearDefaultAccountAndReconnect();
									}
									*/
								} catch (Exception ex) {
									ex.printStackTrace();
								}
								//Remove cart list
								AddToCardSingleTone.getInstance().addToCardArrayList.clear();
								Intent intent = new Intent(context, Login.class);
								//	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
								startActivity(intent);
								dialog.cancel();


								}
							}

							)
									.

							setNegativeButton("No",new DialogInterface.OnClickListener() {
								public void onClick ( final DialogInterface dialog,
								@SuppressWarnings("unused") final int id){

									//getActivity().finish();
									dialog.cancel();
								}
							}

							);
							AlertDialog alert = builder.create();
		alert.show();

						}

	private void signOut() {
		Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
				new ResultCallback<Status>() {
					@Override
					public void onResult(Status status) {
						// [START_EXCLUDE]
						//updateUI(false);
						// [END_EXCLUDE]
					}
				});
	}
	public static void hideStatusBar(boolean isHide,Activity activity) {
		final View decorView = activity.getWindow().getDecorView();

		final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
				| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

		if (isHide) {
			int flagsMore = flags | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
			decorView.setSystemUiVisibility(flagsMore);
		} else {
			decorView.setSystemUiVisibility(flags);
		}

		decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {

			@Override
			public void onSystemUiVisibilityChange(int visibility) {
				if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
					//  decorView.setSystemUiVisibility(flags);
				}
			}
		});
	}

	public GoogleApiClient getGoogleClient()
	{


		if(mGoogleApiClient==null) {
			mGoogleApiClient = new GoogleApiClient.Builder(this)
					.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
					//.addApi(Plus.API)
					.addApi(LocationServices.API)
					.addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
						@Override
						public void onConnected(@Nullable Bundle bundle) {
							fetchAddressButtonHandler();
						}

						@Override
						public void onConnectionSuspended(int i) {
							KalendriaAppController.getInstance().mGoogleApiClient.connect();

						}
					})
					.addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
						@Override
						public void onConnectionFailed(ConnectionResult connectionResult) {
							Log.d("Location error", "Location error " + connectionResult.getErrorCode());

						}
					})
					.build();
			mGoogleApiClient.connect();
		}
		return mGoogleApiClient;
	}

	public void initGPS(Activity activity)
	{
		if (checkAndRequestPermissions(activity)) {
			if (!hasGPSDevice(this)) {
				Toast.makeText(this,"Gps not Supported",Toast.LENGTH_SHORT).show();
				return;
			}
			final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

			if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(this)) {
				// Toast.makeText(mContext,"Gps not enabled",Toast.LENGTH_SHORT).show();
				enableGoogleGPS(activity);
			} else {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						fetchAddressButtonHandler();
					}
				}, 800);
			}
		}

	}

	private boolean hasGPSDevice(Context context) {
		final LocationManager mgr = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		if (mgr == null)
			return false;
		final List<String> providers = mgr.getAllProviders();
		if (providers == null)
			return false;
		return providers.contains(LocationManager.GPS_PROVIDER);
	}


	final static int REQUEST_LOCATION = 199;
	public void enableGoogleGPS(final Activity activity)
	{
		KalendriaAppController app = KalendriaAppController.getInstance();

		GoogleApiClient mGoogleApiClient = app.getGoogleClient();

		LocationRequest locationRequest = LocationRequest.create();
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		locationRequest.setInterval(30 * 1000);
		locationRequest.setFastestInterval(5 * 1000);
		LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
				.addLocationRequest(locationRequest);

		builder.setAlwaysShow(true);

		PendingResult<LocationSettingsResult> result =
				LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
		result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
			@Override
			public void onResult(LocationSettingsResult result) {
				final Status status = result.getStatus();
				switch (status.getStatusCode()) {
					case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
						try {
							// Show the dialog by calling startResolutionForResult(),
							// and check the result in onActivityResult().
							status.startResolutionForResult(activity, REQUEST_LOCATION);
						} catch (IntentSender.SendIntentException e) {
							// Ignore the error.
						}
						break;
				}
			}
		});
	}

	public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

	private boolean checkAndRequestPermissions(Activity activity) {

		int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
		int writePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

		List<String> listPermissionsNeeded = new ArrayList<>();

		if (locationPermission != PackageManager.PERMISSION_GRANTED) {
			listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
		}

		if (writePermission != PackageManager.PERMISSION_GRANTED) {
			listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
		}

		if (!listPermissionsNeeded.isEmpty()) {
			ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
			return false;
		}

		return true;
	}

	boolean mAddressRequested=false;
	public void fetchAddressButtonHandler() {

		KalendriaAppController app = KalendriaAppController.getInstance();
		GoogleApiClient mGoogleApiClient = app.getGoogleClient();


		boolean b = mGoogleApiClient.isConnected();
		if (mGoogleApiClient.isConnected()/* && mLastLocation != null*/) {
			if (app.mLastLocation != null) {
				// startIntentService();

				if(mLastLocation!=null) {
					latitude = app.mLastLocation.getLatitude();
					longitude = app.mLastLocation.getLongitude();
				}
				Log.d("GPS VALUE","LAT :"+app.latitude);
				Log.d("GPS VALUE","LAT :"+app.longitude);
				getCity();

				mAddressRequested = true;
			} else {
				try {
					app.mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
				}
				catch (SecurityException ex){ex.printStackTrace();}

				if(mLastLocation!=null) {
					latitude = app.mLastLocation.getLatitude();
					longitude = app.mLastLocation.getLongitude();
					Log.d("GPS VALUE", "LAT :" + app.latitude);
					Log.d("GPS VALUE", "LAT :" + app.longitude);
				}
				getCity();
				// startIntentService();
				mAddressRequested = true;
			}

		}
	}


	public  void getCity()
	{
		Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
		StringBuilder builder = new StringBuilder();
		try {
			List<Address> address = geoCoder.getFromLocation(getLatitude(), getLongitude(), 1);
			int maxLines = address.get(0).getMaxAddressLineIndex();
			String addressStr ="";
			for (int i=0; i<maxLines; i++) {
				addressStr = address.get(0).getAdminArea();
				if(addressStr==null)
					addressStr =address.get(0).getSubAdminArea();
				if(addressStr==null)
					addressStr =address.get(0).getLocality();

			}
			Constant.setUserGPSCity(addressStr);

			Log.d("Final Address","AD :"+addressStr);

		} catch (IOException e) {
			// Handle IOException
		} catch (NullPointerException e) {
			// Handle NullPointerException
		}
	}

	/**
	 * Function to get latitude
	 * */
	public double getLatitude() {
		if (mLastLocation != null) {
			latitude = mLastLocation.getLatitude();
		}

		// return latitude
		return latitude;
	}

	/**
	 * Function to get longitude
	 * */
	public double getLongitude() {
		if (mLastLocation != null) {
			longitude = mLastLocation.getLongitude();
		}

		// return longitude
		return longitude;
	}






}