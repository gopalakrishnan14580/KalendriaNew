package com.kalendria.kalendria.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kalendria.kalendria.R;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.model.AddToCardVenueModel;
import com.kalendria.kalendria.model.DeshBoard;
import com.kalendria.kalendria.singleton.AddToCardSingleTone;

import org.json.JSONObject;

import java.util.List;


@SuppressLint("CommitPrefEdits")
public class Splash extends Activity {

    //----------Declaration of variables-------------------------//

    public static String Tag = Splash.class.getSimpleName();
    private static int SPLASH_TIME_OUT = 3000;
    LinearLayout internetConnection_root_linear_layout;
    ImageView login_parant_root;
    Button retryBtn;
    TextView responseText;
    private ProgressBar activityCircle;// Initializing ProgressBar
    private SharedPreferences sharedPref;
    JSONObject gcm_device_id = null;
    private ProgressDialog pDialog;
    String code;
    ImageView imageView;
    Handler   handler;
    int count=0;
    List<AddToCardVenueModel> addToCardSingletone;

    int iamge[]=new int[]{R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3,
            R.drawable.image4,
            R.drawable.image5,
            R.drawable.image6,
            R.drawable.image7,
            R.drawable.image8,
            R.drawable.image9,
            R.drawable.image10,
            R.drawable.image11,
            R.drawable.image12,
            R.drawable.image13,
            R.drawable.image14,
            R.drawable.image15,
            R.drawable.image16,
            R.drawable.image17,
            R.drawable.image18,
            R.drawable.image19,
            R.drawable.image20,
            R.drawable.image21,
            R.drawable.image22,
            R.drawable.image23,
            R.drawable.image24,
            R.drawable.image25,
            R.drawable.image26,
            R.drawable.image27,
            R.drawable.image28,
            R.drawable.image29,
            R.drawable.image30,
            R.drawable.image31,
            R.drawable.image32,
            R.drawable.image33,
            R.drawable.image34,
            R.drawable.image35,
            R.drawable.image36,
            R.drawable.image37,
            R.drawable.image38,
            R.drawable.image39,
            R.drawable.image40,
            R.drawable.image41,
            R.drawable.image42,
            R.drawable.image43,
            R.drawable.image44,
            R.drawable.image45,
            R.drawable.image46,
            R.drawable.image47,
            R.drawable.image48,
            R.drawable.image49,
            R.drawable.image50,
            R.drawable.image51
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        //Coded by Magesh
                Constant.initPreference();
        addToCardSingletone = AddToCardSingleTone.getInstance().getParamList();/*This line is used add to card venue name and servie list */
        addToCardSingletone.clear();

        imageView=(ImageView)findViewById(R.id.login_parant_root);

           handler = new Handler();
        final Runnable r = new Runnable() {


            public void run() {
               // tv.append("Hello World");
               /* for(int i=0;i<iamge.length;i++){

                }*/

                if(count<iamge.length){
                    imageView.setImageResource(iamge[count]);
                    count++;

                   // System.out.println("count-->"+count);
                    if(count>=51){
                        System.out.println("i am completed -->"+count);
                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {


                                if(!Constant.getUserId(getApplicationContext()).isEmpty()){
                                    Intent newIntent = new Intent(getApplicationContext(), DashBoard.class);
                                    newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(newIntent);
                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                                }else{
                                    Intent newIntent = new Intent(getApplicationContext(), Login.class);
                                    newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(newIntent);
                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                                }


                            }
                        }, SPLASH_TIME_OUT);

                    }
                }

                handler.postDelayed(this, 15);


            }
        };

        handler.postDelayed(r, 15);

    }



}


