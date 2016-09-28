package com.kalendria.kalendria.utility;

import org.json.JSONObject;import java.lang.Exception;import java.lang.Integer;import java.lang.String;

/**
 * Created by sdinewimac on 5/18/16.
 */
public class SafeParser {

    public static String getString(JSONObject object,String key)
    {

        return getString(object,key,"");

    }

    public static String getString(JSONObject object,String key, String defValue)
    {
        try {


            if (object.has(key)) {
                String stVlaue = object.getString(key);
                return stVlaue==null || stVlaue.equalsIgnoreCase("null")?defValue:stVlaue;
            }
            else
                return defValue;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return defValue;
        }

    }

    public static JSONObject getObject(JSONObject object,String key)
    {
        try {


            if (object.has(key) && object.getJSONObject(key)!=null && object.getJSONObject(key) instanceof JSONObject) {
               return object.getJSONObject(key);
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();

        }

        return null;

    }


    public static boolean getBoolen(JSONObject object,String key, boolean defValue)
    {
        try {

            if (object.has(key))
            return  object.getBoolean(key);


        }
        catch (Exception ex)
        {
            ex.printStackTrace();

            String value = getString(object,key,"0");

            if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("True") || value.equalsIgnoreCase("yes") )
                return true;
            else   if(value.equalsIgnoreCase("false") || value.equalsIgnoreCase("False") || value.equalsIgnoreCase("no") )
                return false;

            try {
                return Integer.parseInt(value)>0 ? true:false;
            }
            catch (Exception exx)
            {
                exx.printStackTrace();
                return defValue;
            }

        }

        return defValue;

    }

    public static int getInt(JSONObject object,String key, int defValue)
    {
        try {

            if (object.has(key))
            return  object.getInt(key);


        }
        catch (Exception ex)
        {
            ex.printStackTrace();

            String value = getString(object,key,"0");


            try {
                return Integer.parseInt(value);
            }
            catch (Exception exx)
            {
                exx.printStackTrace();
                return defValue;
            }

        }
        return defValue;

    }


    public static double getDouble(JSONObject object,String key, double defValue)
    {
        try {

            if (object.has(key))
                return  object.getDouble(key);


        }
        catch (Exception ex)
        {
            ex.printStackTrace();

            String value = getString(object,key,"0");


            try {
                return Double.parseDouble(value);
            }
            catch (Exception exx)
            {
                exx.printStackTrace();
                return defValue;
            }

        }
        return defValue;

    }

}
