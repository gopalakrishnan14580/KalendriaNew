package com.kalendria.kalendria.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kalendria.kalendria.R;
import com.kalendria.kalendria.activity.Venue;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.model.SubCategoryChild;
import com.kalendria.kalendria.model.SubCategoryHeader;

import java.util.ArrayList;

public class SubCategroryAdapter extends BaseAdapter {
    private ArrayList<Object> personArray;
    private LayoutInflater inflater;
    private static final int TYPE_PERSON = 0;
    private static final int TYPE_DIVIDER = 1;
    TextView title,name;
    Context mContext;

    private SharedPreferences sharedPref;

    public SubCategroryAdapter(Context context, ArrayList<Object> personArray) {
        this.personArray = personArray;
        mContext=context;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return personArray.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return personArray.get(position);
    }

    @Override
    public int getViewTypeCount() {
        // TYPE_PERSON and TYPE_DIVIDER
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof SubCategoryChild) {
            return TYPE_PERSON;
        }

        return TYPE_DIVIDER;
    }

    @Override
    public boolean isEnabled(int position) {
        return (getItemViewType(position) == TYPE_PERSON);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case TYPE_PERSON:
                    convertView = inflater.inflate(R.layout.sub_row_item, parent, false);
                    break;
                case TYPE_DIVIDER:
                    convertView = inflater.inflate(R.layout.row_header, parent, false);
                    break;
            }
        }

        switch (type) {
            case TYPE_PERSON:
                final SubCategoryChild person = (SubCategoryChild)getItem(position);
                name = (TextView)convertView.findViewById(R.id.nameLabel);

                name.setText(person.getSubcategoryName());
                name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*System.out.println("uuuuuuuuu-->"+person.getSubcategoryName());
                        System.out.println("uuuuuuuuu-->"+person.getSubcategoryId());
                        System.out.println("uuuuuuuuu-->"+person.getParentId());
                        System.out.println("uuuuuuuuu-->"+person.getParentName());
*/
                        Intent intent = new Intent(mContext, Venue.class);
                            sharedPref = mContext.getSharedPreferences(Constant.MyPREFERENCES, 0);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("HeaderId", person.getParentId());
                            editor.putString("HdeaderName", person.getParentName());
                            editor.putString("ChildId", person.getSubcategoryId());
                           // editor.putString("sector", person.sector);
                            editor.commit();
                        mContext.startActivity(intent);
                    }
                });


                break;
            case TYPE_DIVIDER:
                title = (TextView)convertView.findViewById(R.id.headerTitle);
              //  String titleString = (String)getItem(position);
                SubCategoryHeader header = (SubCategoryHeader)getItem(position);
                title.setText(header.getSubcategoryHeaderName());

                break;
        }

        return convertView;
    }
}