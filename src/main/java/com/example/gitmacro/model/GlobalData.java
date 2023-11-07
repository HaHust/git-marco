package com.example.gitmacro.model;

import lombok.Data;

import java.io.File;
import java.util.List;

/**
 * Created by hs on 11/8/2023.
 */

@Data
public class GlobalData {
    private static GlobalData instance;

    private List<String> branches ;
    private MergeModel mergeModel;
    private File file;

    private GlobalData() {
        // Private constructor to prevent instantiation
        if (mergeModel == null) mergeModel = new MergeModel();
    }

    public static GlobalData getInstance() {
        if (instance == null) {
            instance = new GlobalData();
        }
        return instance;
    }

    public List<String> getBranches() {
        return branches;
    }

    public void setBranches(List<String> value) {
        this.branches = value;
    }

    @Override
    public String toString() {
        return "GlobalData{" +
                "mergeModel=" + mergeModel.toString() +
                '}';
    }
}

