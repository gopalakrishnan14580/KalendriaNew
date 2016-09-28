package com.kalendria.kalendria.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kalendria.kalendria.R;
import com.kalendria.kalendria.activity.Venue;
import com.kalendria.kalendria.adapter.CategoryAdapter;
import com.kalendria.kalendria.adapter.SubCategroryAdapter;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.model.FilerModel;
import com.kalendria.kalendria.model.SubCategoryChild;
import com.kalendria.kalendria.model.SubCategoryHeader;
import com.kalendria.kalendria.singleton.JsonResponce;
import com.kalendria.kalendria.utility.SafeParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mansoor on 03/03/16.
 */
public class SubCategoryFragment  extends Fragment {

    List<String> mjsonResponce;
    ListView listView;

    CategoryAdapter adapter1;
    private SharedPreferences sharedPref;
    ArrayList<Object> people = new ArrayList<>();
    TextView subcatagory_title;
    Button subcatagory_back_btn;

    AutoCompleteTextView textView;
    ImageButton homepage_search_img_btn; // Image Button for search view
    ArrayList<String> COUNTRIES = new ArrayList<>();   // This array list is used to save all the Names for filter
    ArrayList<FilerModel> filterModel = new ArrayList<FilerModel>(); // This array list is used to save all the Names

    public SubCategoryFragment() {
        // Required empty public constructor
        mjsonResponce = JsonResponce.getInstance().getParamList();

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View gridview = inflater.inflate(R.layout.subcategory_activity, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        sharedPref = getActivity().getSharedPreferences(Constant.MyPREFERENCES, 0);
        listView = (ListView) gridview.findViewById(R.id.subcatagory_list);
        subcatagory_title = (TextView) gridview.findViewById(R.id.subcatagory_title);
        subcatagory_back_btn = (Button) gridview.findViewById(R.id.subcatagory_back_btn);
        subcatagory_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        textView = (AutoCompleteTextView) gridview.findViewById(R.id.sub_category_autocompleted_txt);
        homepage_search_img_btn = (ImageButton) gridview.findViewById(R.id.sub_category_autocompleted_btn);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textView.setCursorVisible(true);

            }
        });

        subcatagory_title.setText(Constant.getCategoryName(getActivity()));
        Jsonparsing();


        homepage_search_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mfilterText = textView.getText().toString().trim();
                if (!mfilterText.isEmpty() || mfilterText != null) {
                    SharedPreferences.Editor editor1 = sharedPref.edit();
                    editor1.putString("HdeaderName", mfilterText);
                    editor1.commit();
                    for (int i = 0; i < filterModel.size(); i++) {
                        if (mfilterText.equalsIgnoreCase(filterModel.get(i).getName())) {
                            if (filterModel.get(i).getLevel().equalsIgnoreCase("sub_category 1")){

                                String sub_category_1_id= filterModel.get(i).getId();
                                String sub_category_1_parent_id= filterModel.get(i).getParent_id();

                                System.out.println("sub_category_1_id"+sub_category_1_id);
                                System.out.println("sub_category_1_parent_id"+sub_category_1_parent_id);
                                System.out.println("category_id"+Constant.getCategoryId(getActivity()));
                                System.out.println("type_id"+Constant.getTypeId(getActivity()));


                                Intent intent = new Intent(getActivity(), Venue.class);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("type_id", Constant.getTypeId(getActivity()));
                                editor.putString("category_id", Constant.getCategoryId(getActivity()));
                                editor.putString("HeaderId", sub_category_1_id);
                                editor.putString("sector", filterModel.get(i).sector);
                               // editor.putString("HdeaderName", "");
                                editor.putString("ChildId", "");
                                editor.commit();
                                startActivity(intent);

                            }else if(filterModel.get(i).getLevel().equalsIgnoreCase("sub_category 2"))
                            {

                                String sub_category_2_id= filterModel.get(i).getId();
                                String sub_category_2_parent_id= filterModel.get(i).getParent_id();
                                String sub_category_1_id= null;
                                String sub_category_1_parent_id= null;

                                for(int j=0;j<filterModel.size();j++) {
                                    if (filterModel.get(j).getId().equalsIgnoreCase(sub_category_2_parent_id)) {
                                        sub_category_1_id= filterModel.get(j).getId();
                                        sub_category_1_parent_id= filterModel.get(j).getParent_id();

                                    }
                                }

                                System.out.println("sub_category_2_id"+sub_category_2_id);
                                System.out.println("sub_category_2_parent_id"+sub_category_2_parent_id);
                                System.out.println("sub_category_1_id"+sub_category_1_id);


                                Intent intent = new Intent(getActivity(), Venue.class);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("type_id", Constant.getTypeId(getActivity()));
                                editor.putString("category_id", Constant.getCategoryId(getActivity()));
                                editor.putString("HeaderId", sub_category_1_id);
                                editor.putString("sector", filterModel.get(i).sector);
                                editor.putString("ChildId", sub_category_2_id);
                                editor.commit();
                                startActivity(intent);


                            }
                        }

                    }
                }else{
                    Toast.makeText(getActivity(), "Please Enter Your Search Option ", Toast.LENGTH_SHORT).show();
                }


            }
        });
        return gridview;
    }

    private void Jsonparsing(){

        String id= Constant.getCategoryId(getActivity());
        System.out.println("Category  getegory id-->"+id);

        try {
            // Parsing json object response response will be a json object
            String s= null;
            try {
                s = mjsonResponce.get(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // System.out.println("subcategories list-->"+s);
            if (s != null) {

                JSONObject jsonObject_category =new JSONObject(s);
                JSONArray jsonArray=jsonObject_category.getJSONArray("categorys");
               // System.out.println("subcategories list-->"+jsonArray);
                String subcatagory_id=null;
                String type=null;

                    for(int i=0;i<jsonArray.length();i++) {

                        JSONObject jsonObject=jsonArray.getJSONObject(i);

                        boolean isVisible = SafeParser.getBoolen(jsonObject, "isVisible", true);
                        boolean isSearch = SafeParser.getBoolen(jsonObject,"search",true);
                        if(/*isVisible==false ||*/ isSearch==false) {
                            Log.d("Subcategory", jsonObject.getString("name")+" Breaking..");
                            continue;
                        }
                        //======to get the fillter values start -======
                            FilerModel mfilerModel = new FilerModel();
                            mfilerModel.setParent_id(jsonObject.getString("parent"));
                            mfilerModel.setId(jsonObject.getString("id"));
                            mfilerModel.setName(jsonObject.getString("name"));
                            mfilerModel.setLevel(jsonObject.getString("level"));
                             mfilerModel.sector= SafeParser.getString(jsonObject,"sector","1");
                            filterModel.add(mfilerModel);
                            //======to get the fillter values end -======


                     type=jsonObject.getString("parent");
                    if(type.equalsIgnoreCase(id))
                    {
                       // System.out.println("Category parants name lsit-->"+" "+ jsonObject.getString("name"));
                       // System.out.println("Category parants id lsit-->"+" "+ jsonObject.getString("id"));
                        subcatagory_id=jsonObject.getString("id");
                        SubCategoryHeader subcategoryHeader=new SubCategoryHeader();
                        subcategoryHeader.setSubcategoryHeaderId(jsonObject.getString("id"));
                        subcategoryHeader.setSubcategoryHeaderName(jsonObject.getString("name"));

                        people.add(subcategoryHeader);
                        COUNTRIES.add(jsonObject.getString("name"));

                        for(int j=0;j<jsonArray.length();j++) {
                            JSONObject jsonObject1=jsonArray.getJSONObject(j);
                            String type1=jsonObject1.getString("parent");
                            if(type1.equalsIgnoreCase(subcatagory_id)) {
                                SubCategoryChild subcategory=new SubCategoryChild();
                                subcategory.setSubcategoryId(jsonObject1.getString("id"));
                                subcategory.setSubcategoryName(jsonObject1.getString("name"));
                                subcategory.sector= SafeParser.getString(jsonObject1,"sector","1");
                                subcategory.setParentId(jsonObject.getString("id"));
                                subcategory.setParentName(jsonObject.getString("name"));
                                boolean isVisible1 = SafeParser.getBoolen(jsonObject1, "isVisible", true);
                                boolean isSearch1 = SafeParser.getBoolen(jsonObject1,"search",true);
                                if( isSearch1==false) {
                                    Log.d("Subcategory", jsonObject.getString("name")+" Breaking..");
                                    continue;
                                }

                              //  System.out.println("subCategory parants name lsit-->"+" "+ jsonObject1.getString("name"));
                                people.add(subcategory);
                                COUNTRIES.add(jsonObject1.getString("name"));

                            }
                        }

                    }

                }
                //==== Filter Adapter start
                ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, COUNTRIES);
                textView.setAdapter(adapter);
                //==== Filter Adapter start end

                listView.setAdapter(new SubCategroryAdapter(getActivity(), people));

            }else{
                // if responce is null write your commants here
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
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
