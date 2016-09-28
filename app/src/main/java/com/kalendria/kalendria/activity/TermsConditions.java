package com.kalendria.kalendria.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;

import com.kalendria.kalendria.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by murugan on 2/19/2016.
 */
public class TermsConditions extends Activity {
    private WebView webView;
    Button terms_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.terms_conditions);
        terms_btn=(Button)findViewById(R.id.terms_btn);
        webView = (WebView) findViewById(R.id.terms_conditions);
        webView.loadData(readTextFromResource(R.raw.winqbiduseragreement), "text/html", "utf-8");
        terms_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });


    }
    private String readTextFromResource(int resourceID) {
        InputStream raw = getResources().openRawResource(resourceID);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int i;
        try {
            i = raw.read();

            while (i != -1)

            {

                stream.write(i);

                i = raw.read();

            }

            raw.close();
        } catch (IOException e)

        {
            e.printStackTrace();
        }
        return stream.toString();
        }
}
