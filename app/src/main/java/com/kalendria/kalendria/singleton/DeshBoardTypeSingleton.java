package com.kalendria.kalendria.singleton;

import com.kalendria.kalendria.model.DeshBoard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by murugan on 07/03/16.
 */
public class DeshBoardTypeSingleton {

    private static DeshBoardTypeSingleton category_Singleton;

    public List<DeshBoard> Category_array_list;

    private DeshBoardTypeSingleton() {
        Category_array_list = new ArrayList<DeshBoard>();

    }
    public static DeshBoardTypeSingleton getInstance() {
        if (category_Singleton == null) {
            category_Singleton = new DeshBoardTypeSingleton();
        }

        return category_Singleton;
    }
    public List<DeshBoard> getParamList() {
        return Category_array_list;
    }

    public void setParamList(List<DeshBoard> paramList) {
        this.Category_array_list = paramList;
    }

}
