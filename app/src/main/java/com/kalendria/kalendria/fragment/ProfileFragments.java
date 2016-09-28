package com.kalendria.kalendria.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kalendria.kalendria.Croppings.BitmapUtil;
import com.kalendria.kalendria.Croppings.CropperImageActivity;
import com.kalendria.kalendria.Croppings.RoundedShapeBitmap;
import com.kalendria.kalendria.Croppings.Utils;
import com.kalendria.kalendria.R;
import com.kalendria.kalendria.activity.ResetPassword;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.model.RegisterSpinner;
import com.kalendria.kalendria.utility.CommonSingleton;
import com.kalendria.kalendria.utility.KalendriaAppController;
import com.kalendria.kalendria.utility.SafeParser;
import com.kalendria.kalendria.utility.Validator;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by murugan on 25/04/16.
 */
public class ProfileFragments extends Fragment {

    // LogCat tag
    private static final String TAG = ProfileFragments.class.getSimpleName();
    Button filter_btn;
    Bitmap bitmap;
    ImageView btnCapturePicture;
    private ProgressDialog pDialog;
    private ProgressDialog progress;
    AsyncTask<String, String, String> task;
    MultipartEntity entity;
    StringBuilder builder;

    String radiogroup_value;
    String spinner_selected_id,spinner_name,spinner_type,spinner_parent;
    List<String> cityTextArray;
    ArrayList<RegisterSpinner> cityModelArray =new ArrayList<RegisterSpinner>();
    public static String Tag = ProfileFragments.class.getSimpleName();
    RadioGroup radioGroup;
    EditText register_username_et,register_lastname_et,register_phone_et,register_address_et;
    TextView register_email_et;
    TextView txtCity;
    Button register_submit_btn,register_reset_password_btn;
    View rootView;
    Button btnsettings;

    boolean meditOption=true;

    String mUrlImage,mMediumImage,mThumbImage,mTypeImage,mIsDeletedImage,
            mCreatedAtImage,mUpdatedAtImage,mIdImage,mFileNameImage,mSizeImage,mProviderImage;

    Context mContext;

    public static final int FROM_LIBRARY = 1010;
    public static final int FROM_CAMERA = 1115;
    public static final int CROP_PIC = 2;

    int ACTION_REQUEST_CROP = 100;

    String profileImagePath;

    public ProfileFragments() {

        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mContext = container.getContext();

        rootView = inflater.inflate(R.layout.getprofile, container, false);


        btnsettings = (Button) rootView.findViewById(R.id.btnsettings);
        btnCapturePicture = (ImageView) rootView.findViewById(R.id.image_profile);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        radioGroup = (RadioGroup) rootView.findViewById(R.id.register_radiogroup);
        txtCity = (TextView) rootView.findViewById(R.id.textCity);
        register_submit_btn = (Button) rootView.findViewById(R.id.register_submit_btn);
        register_reset_password_btn = (Button) rootView.findViewById(R.id.register_reset_password_btn);

        register_username_et = (EditText) rootView.findViewById(R.id.register_username_et);
        register_lastname_et = (EditText) rootView.findViewById(R.id.register_lastname_et);
        register_email_et = (TextView) rootView.findViewById(R.id.register_email_et);
        register_address_et = (EditText) rootView.findViewById(R.id.register_address_et);

        register_username_et.setFocusable(false);
        register_username_et.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
        register_username_et.setClickable(false); // user navigates with wheel and selects widget


        register_lastname_et.setFocusable(false);
        register_lastname_et.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
        register_lastname_et.setClickable(false); // user navigates with wheel and selects widget

        register_phone_et = (EditText) rootView.findViewById(R.id.register_phone_et);
        register_phone_et.setFocusable(false);
        register_phone_et.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
        register_phone_et.setClickable(false); // user navigates with wheel and selects widget


        register_email_et.setFocusable(false);
        register_email_et.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
        register_email_et.setClickable(false); // user navigates with wheel and selects widget


        register_address_et.setFocusable(false);
        register_address_et.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
        register_address_et.setClickable(false); // user navigates with wheel and selects widget


        cityTextArray = CommonSingleton.getInstance().getCityList();
        cityModelArray = CommonSingleton.getInstance().getCityMode();
        onClickButton();
        get_radio_group_values();
        //Coded by Magesh, assuming onCreate View is not in Edit mode
        meditOption=false;
        changeEditMode(meditOption);

        if (KalendriaAppController.isNetworkConnected(getActivity())){
            getProfile();
        }else{
            Toast.makeText(getActivity(), "Please Check Your Internet", Toast.LENGTH_SHORT).show();
        }


        return rootView;


    }


    public void get_radio_group_values() {

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected


                RadioButton mbutton = (RadioButton) group.getChildAt(0);
                if (mbutton.isChecked()) {
                    radiogroup_value = "Male";
                } else
                    radiogroup_value = "Female";



            }

        });
    }

    //coded by Magesh
    public void changeEditMode(boolean isEditMode)
    {
        if(isEditMode){

            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                radioGroup.getChildAt(i).setEnabled(true);
            }
            btnCapturePicture.setEnabled(true);
            btnsettings.setBackgroundResource(R.drawable.delete2);
            register_submit_btn.setVisibility(View.VISIBLE);
            register_reset_password_btn.setVisibility(View.GONE);

            register_username_et.setFocusable(true);
            register_username_et.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
            register_username_et.setClickable(true); // user navigates with wheel and selects widget


            register_lastname_et.setFocusable(true);
            register_lastname_et.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
            register_lastname_et.setClickable(true); // user navigates with wheel and selects widget


            register_phone_et.setFocusable(true);
            register_phone_et.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
            register_phone_et.setClickable(true); // user navigates with wheel and selects widget


//            register_email_et.setFocusable(true);
//            register_email_et.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
//            register_email_et.setClickable(true); // user navigates with wheel and selects widget


            register_address_et.setFocusable(true);
            register_address_et.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
            register_address_et.setClickable(true); // user navigates with wheel and selects widget
        }else{

            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                radioGroup.getChildAt(i).setEnabled(false);
            }
            btnCapturePicture.setEnabled(false);
            register_submit_btn.setVisibility(View.GONE);
            register_reset_password_btn.setVisibility(View.VISIBLE);
            btnsettings.setBackgroundResource(R.drawable.edit_icon);
            register_username_et.setFocusable(false);
            register_username_et.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
            register_username_et.setClickable(false); // user navigates with wheel and selects widget


            register_lastname_et.setFocusable(false);
            register_lastname_et.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
            register_lastname_et.setClickable(false); // user navigates with wheel and selects widget


            register_phone_et.setFocusable(false);
            register_phone_et.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
            register_phone_et.setClickable(false); // user navigates with wheel and selects widget


            register_email_et.setFocusable(false);
            register_email_et.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
            register_email_et.setClickable(false); // user navigates with wheel and selects widget


            register_address_et.setFocusable(false);
            register_address_et.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
            register_address_et.setClickable(false); // user navigates with wheel and selects widget
        }

    }


    public  void onClickButton(){

        register_reset_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ResetPassword.class);
                startActivity(intent);
            }
        });

        btnsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(meditOption) {
                    meditOption = false;
                    /*code by gopal*/
                    register_username_et.setText(Constant.getFirstName());
                    register_lastname_et.setText(Constant.getLastName());
                    register_phone_et.setText(Constant.getPhone());
                    register_address_et.setText(Constant.getAdress());

                    final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    final boolean b;
                    b = imm.hideSoftInputFromWindow(getView().getWindowToken(), 0) ? true : false;
                }
                else
                {
                    meditOption=true;
                }
                changeEditMode(meditOption);

            }
        });
        btnCapturePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    selectImage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }); txtCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //coded by magesh: to avoid click event in non-edit mode
                if (!meditOption) return;

                if (KalendriaAppController.isNetworkConnected(getActivity())) {
                    getCityList();
                } else {
                    Toast.makeText(getActivity(), "Please check internet... ", Toast.LENGTH_SHORT).show();
                }

            }
        });

        register_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (meditOption) {
                        String username = register_username_et.getText().toString().trim();
                        String lastName = register_lastname_et.getText().toString().trim();
                        String city = txtCity.getText().toString().trim();
                        String phone = register_phone_et.getText().toString().trim();
                        String email = register_email_et.getText().toString().trim();
                        String address = register_address_et.getText().toString().trim();


                        if (TextUtils.isEmpty(username)) {
                            register_username_et.setError("Please enter Your First Name");
                            register_username_et.requestFocus();
                        } else if (TextUtils.isEmpty(lastName)) {
                            register_lastname_et.setError("Please enter Your Last Name");
                            register_lastname_et.requestFocus();
                        } else if (TextUtils.isEmpty(city)) {
                            txtCity.setError("Please enter Your city Name");
                            txtCity.requestFocus();
                        } else if (TextUtils.isEmpty(phone)||phone.length() < 9 ) {
                            register_phone_et.setError("Your phone number should have minimum 9 number");
                            register_phone_et.requestFocus();
                        } else if (TextUtils.isEmpty(email)) {
                            register_email_et.setError("Please enter a valid email id");
                            register_email_et.requestFocus();
                        } else if (!Validator.isEmailValid(email)) {
                            register_email_et.setError("Please enter a valid email id");
                            register_email_et.requestFocus();
                        }

                   /*  else if(TextUtils.isEmpty(address)){
                         register_address_et.setError("Please enter Your First Name");
                         register_address_et.requestFocus();
                     }*/

                        else {

                            if (KalendriaAppController.getInstance().isNetworkConnected(getActivity())) {
                                makeJsonObjectRequest(username, lastName, city, email, phone, address);
                            } else {
                                Toast.makeText(getActivity(), "Please Check your internet connection", Toast.LENGTH_LONG).show();
                            }

                        }
                    } else // Reset Password
                    {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
    }

    // =========select image view start===============//

    protected void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery","Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {

                    initCamera();

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    File file = new File(Environment.getExternalStorageDirectory() + File.separator + "img.jpg");
                    i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    startActivityForResult(i, FROM_LIBRARY);

                } else if (options[item].equals("Clear")) {

                    if (KalendriaAppController.getInstance().isNetworkConnected(getActivity())) {
                        makeJsonObjectRequest(Constant.getFirstName(), Constant.getLastName(), Constant.getCity(),
                                Constant.getEmail(), Constant.getPhone(), Constant.getAdress());
                    } else {
                        Toast.makeText(getActivity(), "Please Check your internet connection", Toast.LENGTH_LONG).show();
                    }
                    btnCapturePicture.setImageResource(R.drawable.profileimage);

                    dialog.dismiss();
                }
                else if (options[item].equals("Cancel")) {


                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    /*coded by gopal*/

    private void initCamera() {

        Utils.STORAGE availableStorage = Utils.getStorageWithFreeSpace(mContext);
        String rootPath = Utils.getRootPath(mContext, availableStorage);

        File folder = new File(rootPath);

        if (!folder.isDirectory()) {
            folder.mkdir();
        }

        File fileCamera = new File(Utils.getImagePath(mContext,availableStorage, true));
        profileImagePath = fileCamera.getPath();
        Log.d("log_tag", "profileImagePath: " + profileImagePath);

        if (!fileCamera.exists())
            try {
                fileCamera.createNewFile();
            } catch (IOException e) {
//                DebugReportOnLocat.e(e);
            }

        Uri mImageUri = Uri.fromFile(fileCamera);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        startActivityForResult(intent, FROM_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FROM_CAMERA
                && resultCode == Activity.RESULT_OK) {

            if (profileImagePath != null && !profileImagePath.equals("")) {
                startActivityForResult(new Intent(mContext,
                        CropperImageActivity.class).putExtra("Path",
                        profileImagePath), ACTION_REQUEST_CROP);

            }

        }

        if (requestCode == CROP_PIC && resultCode != 0) {
            // Create an instance of bundle and get the returned data
            Bundle extras = data.getExtras();
            // get the cropped bitmap from extras
            Bitmap thePic = extras.getParcelable("data");
            // set image bitmap to image view
            btnCapturePicture.setImageBitmap(thePic);

            btnCapturePicture.setImageBitmap(RoundedShapeBitmap.getRoundedShape(thePic, 120));

        }

        if (requestCode == FROM_LIBRARY && resultCode == Activity.RESULT_OK
                && null != data) {


            profileImagePath = getRealPathFromURI(data.getData());

            if (profileImagePath != null && !profileImagePath.equals("")) {
                startActivityForResult(new Intent(mContext,
                        CropperImageActivity.class).putExtra("Path",
                        profileImagePath), ACTION_REQUEST_CROP);

            }

        } else if (requestCode == ACTION_REQUEST_CROP
                && resultCode == Activity.RESULT_OK) {

            String filePath = data.getStringExtra("picture_path");
            System.out.println(" filePath>>>" + filePath);
            if (filePath != null && !filePath.equals("")) {

            } else {

                return;

            }

            bitmap = decodeFile(new File(filePath));

            try {

                int rotation = BitmapUtil.getExifOrientation(filePath);

                if (bitmap != null) {

                    Matrix matrix = new Matrix();
                    matrix.setRotate(rotation);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                            bitmap.getWidth(), bitmap.getHeight(),
                            matrix, true);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (bitmap != null) {
                btnCapturePicture.setImageBitmap(bitmap);

            }

            task = new GET_EXPECT2().execute();
        }

    }
    //------------------------------------------------------>

    //new

    final int IMAGE_MAX_SIZE = 512;

    private Bitmap decodeFile(File f) {

        Bitmap b = null;

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);

            BitmapFactory.decodeStream(fis, null, o);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {

            try {
                fis.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        int scale = 1;
        long file_length = f.length();

        if (f.length() > 8000) {

            scale = 5;

            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                scale = (int) Math.pow(
                        2,
                        (int) Math.ceil(Math.log(IMAGE_MAX_SIZE
                                / (double) Math.max(o.outHeight, o.outWidth))
                                / Math.log(0.5)));
                // scale = 8;
            }
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        try {
            fis = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        b = BitmapFactory.decodeStream(fis, null, o2);
        try {
            fis.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return b;
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = mContext.getContentResolver().query(contentURI, null, null,
                null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file
            // path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor
                    .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    //------------------------------------------------------->

    // end=======================================================//

    private class GET_EXPECT2 extends AsyncTask<String, String, String> {

        String sResponse = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            progress = new ProgressDialog(getActivity());
            progress.setMessage("Please wait!");
            progress.setCancelable(false);
            progress.show();

        }

        @SuppressLint("NewApi")
        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(String... params1) {


            try {

                builder = new StringBuilder();
                String url ="https://dev.api.kalendria.com/api/v1/media/upload";
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                HttpPost httpPost = new HttpPost(url);

                entity = new MultipartEntity();

                byte[]  photoimageByte = new byte[0];
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    photoimageByte = baos.toByteArray();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println("imageName-->"+profileImagePath);


                entity.addPart("file", new ByteArrayBody(photoimageByte, profileImagePath));
                httpPost.setEntity(entity);

                HttpResponse response = httpClient.execute(httpPost, localContext);
                sResponse = EntityUtils.getContentCharSet(response.getEntity());

                StatusLine statusLine = response.getStatusLine();
                System.out.println("statusLine...." + statusLine);
                int statusCode = statusLine.getStatusCode();

                if (statusCode == 200) {
                    HttpEntity entity1 = response.getEntity();
                    InputStream content = entity1.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    System.out.println("sResponse : " + builder);
                } else {
                    Log.e("class", "Failed to download file");
                    HttpEntity entity1 = response.getEntity();
                    InputStream content = entity1.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                }

            } catch (Exception e) {

                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
            return builder.toString();

        }


        @Override
        protected void onPostExecute(String result) {

            if (progress.isShowing()) {
                progress.dismiss();
            }
            System.out.println("---->result : " + builder.toString());
            try {
                JSONArray jsonArray=new JSONArray( builder.toString());
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject object=jsonArray.getJSONObject(i);

                    mUrlImage=object.getString("url");
                    mMediumImage=SafeParser.getString(object,"medium");
                    mThumbImage=SafeParser.getString(object,"thumb");
                    mTypeImage=object.getString("type");
                    mIsDeletedImage=object.getString("isDeleted");
                    mCreatedAtImage=object.getString("createdAt");
                    mUpdatedAtImage=object.getString("updatedAt");
                    mIdImage=object.getString("id");
                    mFileNameImage=object.getString("filename");
                    mSizeImage=object.getString("size");
                    mProviderImage=object.getString("provider");

                    //Constant.setProfileImage(mThumbImage);
                    //Constant.setProfileImageMedium(mMediumImage);

                    System.out.println("url-->"+object.getString("url"));
                    System.out.println("medium-->"+object.getString("medium"));
                    System.out.println("thumb-->"+object.getString("thumb"));
                    System.out.println("type-->"+object.getString("type"));
                    System.out.println("isDeleted-->"+object.getString("isDeleted"));
                    System.out.println("createdAt-->"+object.getString("createdAt"));
                    System.out.println("updatedAt-->"+object.getString("updatedAt"));
                    System.out.println("id-->"+object.getString("id"));
                    System.out.println("filename-->"+object.getString("filename"));
                    System.out.println("size-->"+object.getString("size"));
                    System.out.println("provider-->"+object.getString("provider"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



    }


    private void getProfile() {


        showpDialog();

        String url=Constant.GET_RROFILE+Constant.getUserId(getActivity());
        System.out.println("getprofile_url-->"+url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    // Parsing json object response response will be a json object
                    if (response != null) {
                        String first_name = response.getString("first_name");
                        String last_name = response.getString("last_name");
                        String email = response.getString("email");
                        String city = response.getString("city");
                        String gender = response.getString("gender");
                        String phone = response.getString("phone");
                        String address = response.getString("address");
                        String profile_image = response.getString("profile_image");
                        System.out.println("imagepfofile_muru" + profile_image);
                        String points = response.getString("points");
                        String wallets = response.getString("credit");

                        Constant.setCity(city);
                        Constant.setFirstName(first_name);
                        Constant.setLastName(last_name);
                        Constant.setEmail(email);

                        Constant.savedData(gender, "kGenderKey");
                        Constant.savedData(address, "kAddressKey");
                        Constant.savedData(phone, "kphoneKey");
                        Constant.savedData(points, "kLoyalityKey");
                        Constant.savedData(wallets, "kWalletsKey");

                        try {
                            JSONObject profile = SafeParser.getObject(response, "profile_image");
                            if (profile != null) {
                                if(profile.has("thumb")) {
                                    String profile_image_thump = profile.getString("thumb");
                                    Constant.setProfileImage(profile_image_thump);
                                }
                                if(profile.has("medium")) {
                                    String profile_image_medium = profile.getString("medium");

                                    Constant.setProfileImageMedium(profile_image_medium);
                                }
                            }
                        }
                        catch (Exception ex ){ex.printStackTrace();}

                        register_username_et.setText(first_name);
                        register_lastname_et.setText(last_name);
                        register_email_et.setText(email);
                        register_address_et.setText(address);
                        register_phone_et.setText(phone);

                        if(gender.equalsIgnoreCase("male")) {
                            radiogroup_value = "Male";
                            RadioButton female=(RadioButton) radioGroup.getChildAt(1);
                            female.setChecked(false);
                            RadioButton male=(RadioButton) radioGroup.getChildAt(0);
                            male.setChecked(true);

                        }
                        else if(gender.equalsIgnoreCase("female")) {
                            radiogroup_value = "Female";

                            RadioButton male=(RadioButton) radioGroup.getChildAt(0);
                            male.setChecked(false);
                            RadioButton female=(RadioButton) radioGroup.getChildAt(1);
                            female.setChecked(true);

                        }


                        for (RegisterSpinner spinner : cityModelArray)
                        {
                            if(spinner.getId().equalsIgnoreCase(city))
                            {
                                txtCity.setText(spinner.getName());
                                spinner_selected_id=spinner.getId();
                                spinner_name= spinner.getName();
                                spinner_type= spinner.getType();
                                spinner_parent= spinner.getParent();

                                break;
                            }

                        }

                        //register_spinner.setText(city);

                            if(profile_image!=null && !profile_image.equalsIgnoreCase("null")){
                                JSONObject object=new JSONObject(profile_image);
                                if(object.has("thumb")) {
                                    String imageUrl = object.getString("thumb");
                                    Picasso.with(getActivity())
                                            .load(imageUrl)
                                                    // .memoryPolicy(MemoryPolicy.NO_CACHE )
                                                    // .networkPolicy(NetworkPolicy.NO_CACHE)
                                                    //.resize(720, 350)
                                                    // .error(R.drawable.login)
                                            .placeholder(R.drawable.profileimage)
                                            .noFade()
                                                    // .fit().centerCrop()
                                            .into(btnCapturePicture);
                                }
                            }
                        }


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                String json;
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    try {
                        json = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        Log.e("Error login-->", json);


                        try {
                            // Parsing json object response response will be a json object
                            if (json != null) {

                                JSONObject jsonObject = new JSONObject(json);
                                String message = jsonObject.getString("message");

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    } catch (UnsupportedEncodingException e) {
                        Log.e("Error 111", e.getMessage());
                    }
                }
                hidepDialog();
            }

        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };


        // Adding request to request queue
        KalendriaAppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void makeJsonObjectRequest( final String username, final String lastName, final String city, final String email, final String phone, final String address) {

        JSONObject parentData = new JSONObject();
        JSONObject jsonObject = new JSONObject();

        try {


            parentData.put("phone",phone);
            parentData.put("type","customer");
            //parentData.put("total_bookings","0");
            parentData.put("city",spinner_selected_id);
            parentData.put("id",Constant.getUserId(getActivity()));
            parentData.put("email", email);
            parentData.put("first_name",username);
            parentData.put("last_name",lastName);
            parentData.put("address",address);
            parentData.put("gender",radiogroup_value);
            /*
            parentData.put("createdAt","");
            parentData.put("send_marketing_email",true);
            parentData.put("role","3");
            parentData.put("lastBookingAt","");

            parentData.put("points",0);
            parentData.put("credit",0.0);
            parentData.put("total_amount_spent",0.0);

            parentData.put("is_active",true);
            parentData.put("total_points_spent",0);

            parentData.put("isActivationEmailSent","true");
            parentData.put("lastReminderAt","");
            parentData.put("is_app_installed","");
            parentData.put("isDeleted","");
            parentData.put("isVerifiedEmailSent","");
            parentData.put("last_login","");
            parentData.put("updatedAt","");
            parentData.put("reminderCount",0);
            parentData.put("verifyToken","");
            parentData.put("like_count",0);*/


            /*code by gopal*/
            if (mUrlImage !=null) {
                jsonObject.put("url", mUrlImage);
                jsonObject.put("filename", mFileNameImage);
                jsonObject.put("size", mSizeImage);
                jsonObject.put("provider", mProviderImage);
                jsonObject.put("medium", mMediumImage);
                jsonObject.put("thumb", mThumbImage);
                jsonObject.put("type", mTypeImage);
                jsonObject.put("isDeleted", mIsDeletedImage);
                jsonObject.put("createdAt", mCreatedAtImage);
                jsonObject.put("updatedAt", mUpdatedAtImage);
                parentData.put("profile_image", jsonObject);
            }


            System.out.println("Register json object-->"+parentData);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        showpDialog();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,Constant.UPDATE_RROFILE+Constant.getUserId(getActivity()), parentData, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(Tag, response.toString());

                System.out.println("Profile Response : "+response.toString());

               // showSimplePopUp();

                Constant.setCity(spinner_selected_id);
                Constant.setFirstName(username);
                Constant.setLastName(lastName);
                Constant.setEmail(email);
                Constant.savedData(address, "kAddressKey");
                Constant.savedData(phone, "kphoneKey");
                meditOption=false;
                changeEditMode(meditOption);
                hidepDialog();
                showSimplePopUp();
                //getProfile();
            }


        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(Tag, "Error: " + error.getMessage());

                String json;
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    try {
                        json = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        Log.e("Error 111",json);
                        Toast.makeText(getActivity(), "A record with that `email` already exists ", Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e) {
                        Log.e("Error 111",e.getMessage());
                    }

                }
                hidepDialog();
            }


        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");

                return params;
            }
        };


        // Adding request to request queue
        KalendriaAppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void getCityList() {

        cityTextArray = CommonSingleton.getInstance().getCityList();
        cityModelArray = CommonSingleton.getInstance().getCityMode();

        try {
            final ArrayAdapter<String> spinner_countries = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_dropdown_item, cityTextArray);

            new AlertDialog.Builder(getActivity())
                    .setTitle("City")
                    .setAdapter(spinner_countries, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            txtCity.setText(cityTextArray.get(which).toString());
                            spinner_selected_id= cityModelArray.get(which).getId();
                            spinner_name= cityModelArray.get(which).getName();
                            spinner_type= cityModelArray.get(which).getType();
                            spinner_parent= cityModelArray.get(which).getParent();
                            //String imc_met = cityText.getSelectedItem().toString();

                            dialog.dismiss();
                        }
                    }).create().show();



        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void showSimplePopUp() {

        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(mContext);
        helpBuilder.setTitle("Kalendria");
        helpBuilder.setMessage("Your profile details has been updated successfully");
        helpBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        getProfile();
                    }
                });

        // Remember, create doesn't show the dialog
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
