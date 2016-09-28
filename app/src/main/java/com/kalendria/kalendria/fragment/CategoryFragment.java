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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kalendria.kalendria.R;
import com.kalendria.kalendria.activity.SubCategory;
import com.kalendria.kalendria.activity.Venue;
import com.kalendria.kalendria.activity.VenueItem;
import com.kalendria.kalendria.adapter.CategoryAdapter;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.model.Category;
import com.kalendria.kalendria.model.DeshBoard;
import com.kalendria.kalendria.model.FilerModel;
import com.kalendria.kalendria.singleton.DeshBoardTypeSingleton;
import com.kalendria.kalendria.singleton.JsonResponce;
import com.kalendria.kalendria.utility.SafeParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by murugan on 02/03/16.
 */
public class CategoryFragment extends Fragment {

    List<String> mjsonResonceSingletone;
    List<DeshBoard> mdeshboard_type_singletone;
    GridView gridView;

    CategoryAdapter mcategoryAdapter;
    private SharedPreferences msharedPref;
    TextView tvcatagory_header;
    Button btnsettings,btnnotification;
    ArrayList<Category> categoryPage_models_listlist =new ArrayList<Category>();

    AutoCompleteTextView textView;
    ImageButton homepage_search_img_btn; // Image Button for search view
    ArrayList<String> COUNTRIES = new ArrayList<>();   // This array list is used to save all the Names for filter
    ArrayList<FilerModel> filterModel = new ArrayList<FilerModel>(); // This array list is used to save all the Names
    private SharedPreferences sharedPref;
    private String currentSector;
    public CategoryFragment() {
        // Required empty public constructor
        mjsonResonceSingletone = JsonResponce.getInstance().getParamList();
        mdeshboard_type_singletone = DeshBoardTypeSingleton.getInstance().getParamList();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View gridview = inflater.inflate(R.layout.category_activity, container, false);
        msharedPref = getActivity().getSharedPreferences(Constant.MyPREFERENCES, 0);
         gridView = (GridView) gridview.findViewById(R.id.gridview);
        tvcatagory_header=(TextView)gridview.findViewById(R.id.tvcatagory_header);
        tvcatagory_header.setText(Constant.getTypeName(getActivity()));
        btnsettings=(Button)gridview.findViewById(R.id.btnsettings);
        btnnotification=(Button)gridview.findViewById(R.id.btnnotification);

        sharedPref = getActivity().getSharedPreferences(Constant.MyPREFERENCES, 0);
        textView = (AutoCompleteTextView) gridview.findViewById(R.id.category_autocompleted_txt);
        homepage_search_img_btn = (ImageButton) gridview.findViewById(R.id.category_autocompleted_btn);

        btnnotification.setVisibility(View.GONE);
        Jsonparsing() ;

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                Intent intent = new Intent(getActivity(), SubCategory.class);
                SharedPreferences.Editor editor = msharedPref.edit();
                editor.putString("sector", currentSector);
                editor.putString("category_id", categoryPage_models_listlist.get(position).getCategoryId());
                editor.putString("categoryName", categoryPage_models_listlist.get(position).getCategoryName());
                editor.commit();
                startActivity(intent);
            }
        });
        updateArrows();
       /* homepage_search_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id= Constant.getTypeId(getActivity());
                String mfilterText = textView.getText().toString().trim();
                if (!mfilterText.isEmpty() || mfilterText != null) {
                    SharedPreferences.Editor editor1 = sharedPref.edit();
                    editor1.putString("HdeaderName", mfilterText);
                    editor1.commit();
                    for (int i = 0; i < categoryPage_models_listlist.size(); i++) {
                        if (mfilterText.equalsIgnoreCase(categoryPage_models_listlist.get(i).getCategoryName())) {
                                Intent intent = new Intent(getActivity(), Venue.class);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("type_id",id );
                                editor.putString("category_id", categoryPage_models_listlist.get(i).getCategoryId());
                                editor.putString("HeaderId", "");
                                editor.putString("ChildId", "");
                               // editor.putString("HdeaderName", "");
                                editor.commit();
                                startActivity(intent);

                        }
                    }
                }


            }
        });*/


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
                            if(filterModel.get(i).isSearch=false )
                            {
                                Log.d("Breaking..",filterModel.get(i).getName());
                                continue;
                            }
                           // Toast.makeText(getActivity(), "i am equal ", Toast.LENGTH_SHORT).show();
                            if (filterModel.get(i).getLevel().equalsIgnoreCase("type")) {
                                Intent intent = new Intent(getActivity(), Venue.class);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("type_id", filterModel.get(i).getId());
                                editor.putString("HdeaderName", filterModel.get(i).getName());
                                editor.putString("sector", filterModel.get(i).sector);
                                editor.commit();
                                startActivity(intent);
                            } else if (filterModel.get(i).getLevel().equalsIgnoreCase("category")) {

                                Intent intent = new Intent(getActivity(), Venue.class);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("type_id", filterModel.get(i).getParent_id());
                                editor.putString("category_id", filterModel.get(i).getId());
                                editor.putString("sector", filterModel.get(i).sector);
                                // editor.putString("HdeaderName", filterModel.get(i).getName());
                                editor.commit();
                                startActivity(intent);
                            } else if (filterModel.get(i).getLevel().equalsIgnoreCase("sub_category 1")) {

                                String sub_category_1_id = filterModel.get(i).getId();
                                String sub_category_1_parent_id = filterModel.get(i).getParent_id();
                                String category_id = null;
                                String category_parent_id = null;
                                String type_id = null;
                                String type_parent_id = null;
                                for (int j = 0; j < filterModel.size(); j++) {
                                    if (filterModel.get(j).getId().equalsIgnoreCase(sub_category_1_parent_id)) {
                                        category_id = filterModel.get(j).getId();
                                        category_parent_id = filterModel.get(j).getParent_id();
                                        for (int k = 0; k < filterModel.size(); k++) {
                                            if (filterModel.get(k).getId().equalsIgnoreCase(category_parent_id)) {
                                                type_id = filterModel.get(k).getId();
                                                type_parent_id = filterModel.get(k).getParent_id();
                                            }
                                        }

                                    }
                                }

                               /* System.out.println("sub_category_1_id" + sub_category_1_id);
                                System.out.println("sub_category_1_parent_id" + sub_category_1_parent_id);
                                System.out.println("category_id" + category_id);
                                System.out.println("category_parent_id" + category_parent_id);
                                System.out.println("type_id" + type_id);
                                System.out.println("type_parent_id" + type_parent_id);*/

                                Intent intent = new Intent(getActivity(), Venue.class);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("type_id", type_id);
                                editor.putString("category_id", category_id);
                                editor.putString("HeaderId", sub_category_1_id);
                                editor.putString("sector", filterModel.get(i).sector);
                                //editor.putString("HdeaderName", "");
                                editor.commit();
                                startActivity(intent);

                            } else if (filterModel.get(i).getLevel().equalsIgnoreCase("sub_category 2")) {

                                String sub_category_2_id = filterModel.get(i).getId();
                                String sub_category_2_parent_id = filterModel.get(i).getParent_id();
                                String sub_category_1_id = null;
                                String sub_category_1_parent_id = null;
                                String category_id = null;
                                String category_parent_id = null;
                                String type_id = null;
                                String type_parent_id = null;
                                String sector ="1";
                                for (int j = 0; j < filterModel.size(); j++) {
                                    if (filterModel.get(j).getId().equalsIgnoreCase(sub_category_2_parent_id)) {
                                        sub_category_1_id = filterModel.get(j).getId();
                                        sub_category_1_parent_id = filterModel.get(j).getParent_id();
                                        for (int k = 0; k < filterModel.size(); k++) {
                                            if (filterModel.get(k).getId().equalsIgnoreCase(sub_category_1_parent_id)) {
                                                category_id = filterModel.get(k).getId();
                                                category_parent_id = filterModel.get(k).getParent_id();
                                                for (int n = 0; n < filterModel.size(); n++) {
                                                    if (filterModel.get(n).getId().equalsIgnoreCase(category_parent_id)) {
                                                        type_id = filterModel.get(n).getId();
                                                        type_parent_id = filterModel.get(n).getParent_id();
                                                        sector= filterModel.get(n).sector;
                                                    }
                                                }

                                            }
                                        }

                                    }
                                }

                               /* System.out.println("sub_category_2_id" + sub_category_2_id);
                                System.out.println("sub_category_2_parent_id" + sub_category_2_parent_id);

                                System.out.println("sub_category_1_id" + sub_category_1_id);
                                System.out.println("sub_category_1_parent_id" + sub_category_1_parent_id);
                                System.out.println("category_id" + category_id);
                                System.out.println("category_parent_id" + category_parent_id);
                                System.out.println("type_id" + type_id);
                                System.out.println("type_parent_id" + type_parent_id);*/

                                Intent intent = new Intent(getActivity(), Venue.class);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("type_id", type_id);
                                editor.putString("category_id", category_id);
                                editor.putString("HeaderId", sub_category_1_id);
                                editor.putString("ChildId", sub_category_2_id);
                                editor.putString("sector", sector);
                                //editor.putString("HdeaderName", "");
                                editor.commit();
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(getActivity(), VenueItem.class);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("venueID", filterModel.get(i).getId());
                                editor.putString("sector", filterModel.get(i).sector);
                                editor.commit();
                                startActivity(intent);
                            }

                        }
                    }
                }


            }
        });

        btnsettings.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Jsonparsing_right_arrow_clink();

            }
        });

        btnnotification.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Jsonparsing_lift_arrow_clink();

            }
        });
        return gridview;
    }

    private void Jsonparsing(){

        String id= Constant.getTypeId(getActivity());
        System.out.println("Category  postion id-->"+id);
        currentSector = Constant.getSector(getActivity());
        try {
            // Parsing json object response response will be a json object
            String s= null;
            String parentId=null;
            String subcatagory_id=null;
            try {
                s = mjsonResonceSingletone.get(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("categories list-->"+s);
            if (s != null) {

                JSONObject jsonObject_category =new JSONObject(s);
                JSONArray jsonArray=jsonObject_category.getJSONArray("categorys");

                for(int i=0;i<jsonArray.length();i++) {

                    JSONObject jsonObject5=jsonArray.getJSONObject(i);
                    parentId=jsonObject5.getString("parent");
                    //System.out.println("parent"+jsonObject.getString("parent"));
                    boolean isVisible = SafeParser.getBoolen(jsonObject5, "isVisible", true);
                    boolean isSearch = SafeParser.getBoolen(jsonObject5,"search",true);
                    if( isSearch==false) {
                        Log.d("filter", jsonObject5.getString("name")+" Breaking..");
                        continue;
                    }

                    FilerModel mfilerModel = new FilerModel();
                    mfilerModel.setParent_id(jsonObject5.getString("parent"));
                    mfilerModel.setId(jsonObject5.getString("id"));
                    mfilerModel.setName(jsonObject5.getString("name"));
                    mfilerModel.setLevel(jsonObject5.getString("level"));
                    mfilerModel.sector= SafeParser.getString(jsonObject5,"sector","1");
                    mfilerModel.isVisible=isVisible;
                    mfilerModel.isSearch=isSearch;

                    filterModel.add(mfilerModel);

                    if(parentId.equalsIgnoreCase(id))
                    {
                        Category categoryPage_model=new Category();
                        categoryPage_model.setCategoryId(jsonObject5.getString("id"));
                        categoryPage_model.setCategoryName(jsonObject5.getString("name"));
                        categoryPage_model.sector= SafeParser.getString(jsonObject5,"sector","1");
                        System.out.println("Category parants name lsit-->"+" "+ jsonObject5.getString("name"));
                        String nameValue = jsonObject5.getString("media");
                        System.out.println("Category media lsit-->"+"  "+ nameValue);

                        if(nameValue !="null" ) {
                            System.out.println("i am in side");
                            JSONObject jsonObject2=new JSONObject(nameValue);
                            categoryPage_model.setCategoryImages(jsonObject2.getString("medium"));
                            System.out.println("Category media url list"+ jsonObject2.getString("medium"));

                        }

                        COUNTRIES.add(jsonObject5.getString("name"));
                        categoryPage_models_listlist.add(categoryPage_model);

                    }

                }
                System.out.println("size of type array"+ categoryPage_models_listlist.size());
               /* for(Category homePage_model: categoryPage_models_listlist) {
                    System.out.println("iiiiiiii"+homePage_model.getCategoryName());
                }*/



                for(int i=0;i<jsonArray.length();i++) {

                    JSONObject jsonObject=jsonArray.getJSONObject(i);

                    String  prantIdType=jsonObject.getString("parent");

                    if(prantIdType.equalsIgnoreCase(id)) {

                     String   prantIdCategories=jsonObject.getString("id");
                       // COUNTRIES.add(jsonObject.getString("name"));
                        boolean isVisible1 = SafeParser.getBoolen(jsonObject, "isVisible", true);
                        boolean isSearch1 = SafeParser.getBoolen(jsonObject, "search", true);
                        if( isSearch1==false) {
                            Log.d("Subcategory", jsonObject.getString("name") + " Breaking..");
                            continue;
                        }
                        for(int j=0;j<jsonArray.length();j++) {

                            JSONObject jsonObject1=jsonArray.getJSONObject(j);


                            String parentIdSubCategories=jsonObject1.getString("parent");

                            boolean isVisible2 = SafeParser.getBoolen(jsonObject1, "isVisible", true);
                            boolean isSearch2 = SafeParser.getBoolen(jsonObject1, "search", true);
                            if( isSearch2==false) {
                                Log.d("Subcategory", jsonObject.getString("name") + " Breaking..");
                                continue;
                            }

                            if(parentIdSubCategories.equalsIgnoreCase(prantIdCategories)) {

                                COUNTRIES.add(jsonObject1.getString("name"));

                              String  parentIdSubCategoriesChild=jsonObject1.getString("id");

                                for(int k=0;k<jsonArray.length();k++)
                                {
                                    JSONObject jsonObject2=jsonArray.getJSONObject(k);
                                    String subCategory2=jsonObject2.getString("parent");
                                    if(parentIdSubCategoriesChild.equalsIgnoreCase(subCategory2)) {

                                        boolean isVisible3 = SafeParser.getBoolen(jsonObject2, "isVisible", true);
                                        boolean isSearch3 = SafeParser.getBoolen(jsonObject2, "search", true);
                                        if( isSearch3==false) {
                                            Log.d("Subcategory", jsonObject.getString("name") + " Breaking..");
                                            continue;
                                        }
                                        COUNTRIES.add(jsonObject2.getString("name"));


                                    }
                                }
                            }
                        }

                    }
                }


                //==== Filter Adapter start
                ArrayAdapter adapter = new ArrayAdapter(getActivity(),R.layout.autocomplete_layout, COUNTRIES);
                textView.setAdapter(adapter);
                //==== Filter Adapter start end
                mcategoryAdapter = new CategoryAdapter(categoryPage_models_listlist,getActivity());
                gridView.setAdapter(mcategoryAdapter);



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

    private void Jsonparsing_right_arrow_clink(){
        int counter = 0;
        counter = Integer.parseInt(Constant.homepage_list_postion(getActivity()));
        counter ++;
        System.out.println("count -->"+counter);


        int  key= mdeshboard_type_singletone.size();
        //coded by Magesh
        if(key<=counter+1  ) {
            btnsettings.setVisibility(View.GONE);
        }
        //code end
        System.out.println("array size ==>"+key);
        btnnotification.setVisibility(View.VISIBLE);
        if(key>counter && counter>0 )
        {

            categoryPage_models_listlist.clear();
            COUNTRIES.clear();
            filterModel.clear();
            SharedPreferences.Editor editor = msharedPref.edit();
            editor.putString("position_id", String.valueOf(counter));

            DeshBoard deshBoard = mdeshboard_type_singletone.get(counter);
            tvcatagory_header.setText(deshBoard.getTypeName());

            String idnew= deshBoard.getTypeId();
            editor.putString("type_id", idnew);
            editor.commit();
            currentSector = deshBoard.sector;

            try {

                // Parsing json object response response will be a json object
                String s= mjsonResonceSingletone.get(0);
                System.out.println("categories list-->"+s);
                if (s != null) {

                    JSONObject jsonObject_category =new JSONObject(s);
                    JSONArray jsonArray=jsonObject_category.getJSONArray("categorys");

                    for(int i=0;i<jsonArray.length();i++) {

                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String type=jsonObject.getString("parent");
                        //System.out.println("parent"+jsonObject.getString("parent"));
                        boolean isVisible = SafeParser.getBoolen(jsonObject, "isVisible", true);
                        boolean isSearch = SafeParser.getBoolen(jsonObject, "search", true);
                        if( isSearch==false) {
                            Log.d("Subcategory", jsonObject.getString("name") + " Breaking..");
                            continue;
                        }
                        FilerModel mfilerModel = new FilerModel();
                        mfilerModel.setParent_id(jsonObject.getString("parent"));
                        mfilerModel.setId(jsonObject.getString("id"));
                        mfilerModel.setName(jsonObject.getString("name"));
                        mfilerModel.setLevel(jsonObject.getString("level"));
                        /*
                        if(jsonObject.get("sector")==null || (jsonObject.get("sector") instanceof String) && ((String) jsonObject.get("sector")).equalsIgnoreCase("null") )
                            mfilerModel.sector=parentSeector;
                        else
                        mfilerModel.sector= SafeParser.getString(jsonObject,"sector","1");
                        */
                        mfilerModel.sector= SafeParser.getString(jsonObject,"sector","1");

                        mfilerModel.isVisible=isVisible;
                        mfilerModel.isSearch=isSearch;
                        filterModel.add(mfilerModel);

                        if(type.equalsIgnoreCase(idnew))
                        {

                            Category categoryPage_model=new Category();
                            categoryPage_model.setCategoryId(jsonObject.getString("id"));
                            categoryPage_model.setCategoryName(jsonObject.getString("name"));
                            System.out.println("Category parants name lsit-->"+" "+ jsonObject.getString("name"));
                            String nameValue = jsonObject.getString("media");
                            System.out.println("Category media lsit-->"+"  "+ nameValue);
                            categoryPage_model.sector= SafeParser.getString(jsonObject,"sector","1");
                            if(nameValue !="null" ){
                                System.out.println("i am in side");
                                JSONObject jsonObject1=new JSONObject(nameValue);
                                categoryPage_model.setCategoryImages(jsonObject1.getString("medium"));
                                System.out.println("Category media url list" + jsonObject1.getString("medium"));

                            }

                            COUNTRIES.add(jsonObject.getString("name"));
                            categoryPage_models_listlist.add(categoryPage_model);

                        }

                    }
                    System.out.println("size of type array"+ categoryPage_models_listlist.size());
                   /* for(Category homePage_model1: categoryPage_models_listlist) {
                        System.out.println("iiiiiiii"+homePage_model1.getCategoryName());
                    }*/

                    for(int i=0;i<jsonArray.length();i++) {

                        JSONObject jsonObject=jsonArray.getJSONObject(i);

                        boolean isVisible3 = SafeParser.getBoolen(jsonObject, "isVisible", true);
                        boolean isSearch3 = SafeParser.getBoolen(jsonObject, "search", true);
                        if( isSearch3==false) {
                            Log.d("Subcategory", jsonObject.getString("name") + " Breaking..");
                            continue;
                        }

                        String  prantIdType=jsonObject.getString("parent");

                        if(prantIdType.equalsIgnoreCase(idnew)) {

                            String   prantIdCategories=jsonObject.getString("id");
                            // COUNTRIES.add(jsonObject.getString("name"));

                            for(int j=0;j<jsonArray.length();j++) {

                                JSONObject jsonObject1=jsonArray.getJSONObject(j);

                                String parentIdSubCategories=jsonObject1.getString("parent");

                                if(parentIdSubCategories.equalsIgnoreCase(prantIdCategories)) {

                                    boolean isVisible4 = SafeParser.getBoolen(jsonObject1, "isVisible", true);
                                    boolean isSearch4 = SafeParser.getBoolen(jsonObject1, "search", true);
                                    if( isSearch4==false) {
                                        Log.d("Subcategory", jsonObject.getString("name") + " Breaking..");
                                        continue;
                                    }
                                    COUNTRIES.add(jsonObject1.getString("name"));

                                    String  parentIdSubCategoriesChild=jsonObject1.getString("id");

                                    for(int k=0;k<jsonArray.length();k++)
                                    {
                                        JSONObject jsonObject2=jsonArray.getJSONObject(k);
                                        String subCategory2=jsonObject2.getString("parent");
                                        if(parentIdSubCategoriesChild.equalsIgnoreCase(subCategory2)) {
                                            boolean isVisible5 = SafeParser.getBoolen(jsonObject2, "isVisible", true);
                                            boolean isSearch5 = SafeParser.getBoolen(jsonObject2, "search", true);
                                            if( isSearch5==false) {
                                                Log.d("Subcategory", jsonObject.getString("name") + " Breaking..");
                                                continue;
                                            }

                                            COUNTRIES.add(jsonObject2.getString("name"));


                                        }
                                    }
                                }
                            }

                        }
                    }
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, COUNTRIES);
                    textView.setAdapter(adapter);
                    mcategoryAdapter = new CategoryAdapter(categoryPage_models_listlist,getActivity());
                    gridView.setAdapter(mcategoryAdapter);



                }else{
                    // if responce is null write your commants here
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }else{
            btnsettings.setVisibility(View.GONE);
        }

    }

    private void updateArrows()
    {
        int counter = 0;
        counter = Integer.parseInt(Constant.homepage_list_postion(getActivity()));

        System.out.println("count -->"+counter);

        //coded by Magesh, we have set visibility based on current Index
        if(counter==0)
        {
            btnnotification.setVisibility(View.GONE);
            btnsettings.setVisibility(View.VISIBLE);
            return;
        }
        btnnotification.setVisibility(View.VISIBLE);
        int  key= mdeshboard_type_singletone.size();
        //coded by Magesh
        if(key<=counter+1  ) {
            btnsettings.setVisibility(View.GONE);
        }
    }
    private void Jsonparsing_lift_arrow_clink(){

        int counter = 0;
        counter = Integer.parseInt(Constant.homepage_list_postion(getActivity()));
        counter --;
        System.out.println("count -->"+counter);

        //coded by Magesh, we have set visibility based on current Index
        if((counter-1)<0)
        {
            btnnotification.setVisibility(View.GONE);
        }
// end
        btnsettings.setVisibility(View.VISIBLE);
        int  key= mdeshboard_type_singletone.size();
        System.out.println("array size ==>"+key);

        if(key>counter && counter>=0 )
        {
            categoryPage_models_listlist.clear();
            COUNTRIES.clear();
            filterModel.clear();
            SharedPreferences.Editor editor = msharedPref.edit();
            editor.putString("position_id", String.valueOf(counter));

            DeshBoard deshBoard = mdeshboard_type_singletone.get(counter);
            currentSector = deshBoard.sector;
            tvcatagory_header.setText(deshBoard.getTypeName());
            String idnew= deshBoard.getTypeId();
            editor.putString("type_id", idnew);
            editor.commit();


            try {

                // Parsing json object response response will be a json object
                String s= mjsonResonceSingletone.get(0);
                System.out.println("categories list-->"+s);
                if (s != null) {

                    JSONObject jsonObject_category =new JSONObject(s);
                    JSONArray jsonArray=jsonObject_category.getJSONArray("categorys");

                    for(int i=0;i<jsonArray.length();i++) {

                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String type=jsonObject.getString("parent");
                        //System.out.println("parent"+jsonObject.getString("parent"));
                        boolean isVisible2 = SafeParser.getBoolen(jsonObject, "isVisible", true);
                        boolean isSearch2 = SafeParser.getBoolen(jsonObject, "search", true);
                        if( isSearch2==false) {
                            Log.d("Subcategory", jsonObject.getString("name") + " Breaking..");
                            continue;
                        }
                        FilerModel mfilerModel = new FilerModel();
                        mfilerModel.setParent_id(jsonObject.getString("parent"));
                        mfilerModel.sector= SafeParser.getString(jsonObject,"sector","1");
                        mfilerModel.setId(jsonObject.getString("id"));
                        mfilerModel.setName(jsonObject.getString("name"));
                        mfilerModel.setLevel(jsonObject.getString("level"));
                        mfilerModel.sector= SafeParser.getString(jsonObject,"sector","1");
                        mfilerModel.isVisible=isVisible2;
                        mfilerModel.isSearch=isSearch2;
                        filterModel.add(mfilerModel);

                        if(type.equalsIgnoreCase(idnew))
                        {
                            Category categoryPage_model=new Category();
                            categoryPage_model.setCategoryId(jsonObject.getString("id"));
                            categoryPage_model.setCategoryName(jsonObject.getString("name"));
                            categoryPage_model.sector= SafeParser.getString(jsonObject,"sector","1");
                            System.out.println("Category parants name lsit-->"+" "+ jsonObject.getString("name"));
                            String nameValue = jsonObject.getString("media");
                            System.out.println("Category media lsit-->"+"  "+ nameValue);

                            if(nameValue !="null" ){
                                System.out.println("i am in side");
                                JSONObject jsonObject1=new JSONObject(nameValue);
                                categoryPage_model.setCategoryImages(jsonObject1.getString("medium"));
                                System.out.println("Category media url list"+ jsonObject1.getString("medium"));

                            }
                            COUNTRIES.add(jsonObject.getString("name"));
                            categoryPage_models_listlist.add(categoryPage_model);

                        }

                    }
                   /* System.out.println("size of type array"+ categoryPage_models_listlist.size());
                    for(Category homePage_model1: categoryPage_models_listlist) {
                        System.out.println("iiiiiiii"+homePage_model1.getCategoryName());
                    }*/

                    for(int i=0;i<jsonArray.length();i++) {

                        JSONObject jsonObject=jsonArray.getJSONObject(i);

                        String  prantIdType=jsonObject.getString("parent");

                        if(prantIdType.equalsIgnoreCase(idnew)) {

                            String   prantIdCategories=jsonObject.getString("id");
                            // COUNTRIES.add(jsonObject.getString("name"));

                            for(int j=0;j<jsonArray.length();j++) {

                                JSONObject jsonObject1=jsonArray.getJSONObject(j);

                                String parentIdSubCategories=jsonObject1.getString("parent");

                                if(parentIdSubCategories.equalsIgnoreCase(prantIdCategories)) {

                                    COUNTRIES.add(jsonObject1.getString("name"));

                                    String  parentIdSubCategoriesChild=jsonObject1.getString("id");

                                    for(int k=0;k<jsonArray.length();k++)
                                    {
                                        JSONObject jsonObject2=jsonArray.getJSONObject(k);
                                        String subCategory2=jsonObject2.getString("parent");
                                        if(parentIdSubCategoriesChild.equalsIgnoreCase(subCategory2)) {

                                            COUNTRIES.add(jsonObject2.getString("name"));


                                        }
                                    }
                                }
                            }

                        }
                    }
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, COUNTRIES);
                    textView.setAdapter(adapter);
                    mcategoryAdapter = new CategoryAdapter(categoryPage_models_listlist,getActivity());
                    gridView.setAdapter(mcategoryAdapter);



                }else{
                    // if responce is null write your commants here
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }else{
            btnnotification.setVisibility(View.GONE);
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
