package com.example.gitmacro;

import com.example.gitmacro.model.GlobalData;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hs on 11/12/2023.
 */


public class GitUtils {
    public static List<Ref> viewAllLocalBranches(File gitDirectory) {
        List<Ref> branches = null;
        try {
            Git git = Git.open(gitDirectory);
            branches = git.branchList().call();
            GlobalData.getInstance().setBranches(branches.parallelStream().map(Ref::getName).collect(Collectors.toList()));
            git.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return branches;
    }
}
