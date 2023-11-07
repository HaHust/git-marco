package com.example.gitmacro.model;

import lombok.Data;
import lombok.Getter;

/**
 * Created by hs on 11/8/2023.
 */

@Data
public class MergeModel {
    private String currentbranch;
    private String destinationBranch;

    @Override
    public String toString() {
        return "MergeModel{" +
                "currentbranch='" + currentbranch + '\'' +
                ", destinationBranch='" + destinationBranch + '\'' +
                '}';
    }
}
