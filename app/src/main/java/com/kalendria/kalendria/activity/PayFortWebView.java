package com.kalendria.kalendria.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.kalendria.kalendria.R;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.singleton.AddToCardSingleTone;
import com.kalendria.kalendria.utility.CommonSingleton;
import com.kalendria.kalendria.utility.KalendriaAppController;

import java.util.LinkedHashMap;

/**
 * Created by mansoor on 11/03/16.
 */
public class PayFortWebView extends AppCompatActivity {


    private WebView myWebView;
    boolean isOrder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payportactivity);


        Intent intent = getIntent();
        isOrder = intent.getExtras().getBoolean("isOrderKey",false);


        myWebView = (WebView) findViewById(R.id.myWebView);


        WebSettings webSettings = myWebView.getSettings();
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAppCacheEnabled(false);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setLoadWithOverviewMode(true);

        // Use WideViewport and Zoom out if there is no viewport defined
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        /*
        // Enable pinch to zoom without the zoom buttons
        webSettings.setBuiltInZoomControls(true);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            // Hide the zoom controls for HONEYCOMB+
            webSettings.setDisplayZoomControls(false);
        }*/

        myWebView.setWebViewClient(new MyBrowser());

        // Enable remote debugging via chrome://inspect
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }


        myWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        //myWebView.loadUrl(url);

        Button back = (Button) findViewById(R.id.register_back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PayFortWebView.this.finish();

            }
        });

        myWebView.setWebChromeClient(new WebChromeClient() {

            public void onConsoleMessage(String message, int lineNumber, String sourceID) {
                Log.d("KAL PAY PORT", message + " -- From line "
                        + lineNumber + " of "
                        + sourceID);
            }

        });
        myWebView.loadData(KalendriaAppController.getInstance().HTMLString, "text/html", "UTF-8");


    }


    private void showGiftDetails() {
        Toast.makeText(PayFortWebView.this, "Gift purchased successfully", Toast.LENGTH_SHORT).show();
        CommonSingleton.getInstance().getUserProfileInformation();
        showGiftPage();
        // appDelegate.getUserInformation()
//        appDelegate.showOrderPage()
    }

    private void showOrders() {

        //Remove last booked service from cart
        // appDelegate.getUserInformation()

        //appDelegate.arrCartList.removeObject(self.dictBusiness)
        //appDelegate.saveCartList()
        // appDelegate.checkSeviceCount()
        CommonSingleton.getInstance().getUserProfileInformation();
        AddToCardSingleTone singleTone = AddToCardSingleTone.getInstance();
        singleTone.addToCardArrayList.remove(KalendriaAppController.getInstance(). selectedAddToCardVenueModel);
        Toast.makeText(PayFortWebView.this, "Appointment booked successfully", Toast.LENGTH_SHORT).show();
        //Show Order Details page

        showOrderPage();


    }

    private void showGiftPage() {
        Intent newIntent = new Intent(this, GiftListView.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(newIntent);
    }

    private void showOrderPage() {
        Intent newIntent = new Intent(this, MyOrderActivity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(newIntent);

    }

    private void showHomePage() {
        Intent newIntent = new Intent(this, DashBoard.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(newIntent);
    }


    private class MyBrowser extends WebViewClient {



        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            String loadURLString = url;
            Log.d("PAY FORT", url);

            System.out.println("PAY FORT URL ------------------------> "+url);
            System.out.println("PAY FORT URL123 ------------------------> "+loadURLString);



            if (loadURLString.contains("cancelOperation") || loadURLString.contains("AbnormalPage") )
            {
                myWebView.loadUrl("http://www.google.com");

                System.out.println("Operation cancelled-------------------->");
                Toast.makeText(PayFortWebView.this,"Operation cancelled",Toast.LENGTH_LONG);
                PayFortWebView.this.finish();
               // showHomePage();
                return false;
            }
            else  if ( loadURLString.contains("AbnormalPage") ) {
                myWebView.loadUrl("http://www.google.com");
                System.out.println("MMMMMMMMMMMM-------------------->");
                Toast.makeText(PayFortWebView.this, "Something went worng, Try agin later!", Toast.LENGTH_LONG);
                PayFortWebView.this.finish();
                return false;
            }
            if (loadURLString.equalsIgnoreCase(Constant.HOSTDOMAIN)) {

                myWebView.loadUrl("http://www.google.com");
                System.out.println("AAAAAAAAAAA-------------------->");
                showHomePage();
                return false;
            }

            if (isOrder == true) {

                String[] arrCompns = loadURLString.split("\\?");//!.componentsSeparatedByString("?")
                String parmeterString = arrCompns[arrCompns.length - 1];

                LinkedHashMap<String, String> dictQueryString = new LinkedHashMap<>();

                String[] arrURLComponents = parmeterString.split("&");

                for (String keyValePair : arrURLComponents) {

                    String arrPairComp[] = keyValePair.split("=");
                    String valueDict = arrPairComp[arrCompns.length - 1];
                    String keyDict = arrPairComp[0];
                     dictQueryString.put(keyDict, valueDict);
                }



                String status = dictQueryString.get("status");

                if (status != null) {

                    if (status.equalsIgnoreCase("success")) {

                        myWebView.loadUrl("http://www.google.com");
                        showOrders();
                        return false;

                        //return true;
                    }

                }
            } else {

                String[] arrCompns = loadURLString.split("/");
                String giftString = arrCompns[arrCompns.length - 1];

                if (giftString.equalsIgnoreCase("gift")) {

                    myWebView.loadUrl("about:blank");
                    showGiftDetails();
                    return false;

                }


            }


            view.loadUrl(url);
            return true;

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyWebView();
    }

    public void destroyWebView() {



        if(myWebView != null) {
            /*
            myWebView.clearHistory();
            myWebView.clearCache(true);
            myWebView.setWebViewClient(null);
            myWebView.loadUrl("http://www.google.com");
            myWebView.freeMemory();
            myWebView.pauseTimers();
            */
            myWebView = null;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }


}
