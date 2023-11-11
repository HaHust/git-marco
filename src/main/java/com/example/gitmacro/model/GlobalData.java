package com.example.gitmacro.model;

import com.example.gitmacro.GitUtils;
import com.example.gitmacro.component.StoreComponent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import lombok.Data;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;

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
    private DataConfig dataConfig;

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

    public void render() {
        if (GlobalData.getInstance().getDataConfig() != null) {
            for (File repo : GlobalData.getInstance().getDataConfig().getRepos()) {
                createRepoPane(repo);
            }
        }

    }

    private void createRepoPane(File repo) {
        // Customize this method to add branches inside each repo pane
        addFolderToAccordion(repo.getName(), repo.getPath(), GitUtils.viewAllLocalBranches(repo));

    }

    private void addFolderToAccordion(String folderName, String folderPath, List<Ref> branches) {
        TitledPane newTitledPane = new TitledPane();
        newTitledPane.setText(folderName);

        VBox branchListContainer = new VBox();
        ListView<String> branchListView = new ListView<>();

        ObservableList<String> branchNames = FXCollections.observableArrayList();
        for (Ref branch : branches) {
            branchNames.add(branch.getName());
        }


        branchListView.setItems(branchNames);

        branchListView.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                String selectedBranch = branchListView.getSelectionModel().getSelectedItem();
                System.out.println("Chuyển đến nhánh: " + selectedBranch);
                switchToBranch(new File(folderPath), selectedBranch);
            }
        });

        // Sự kiện thay đổi chọn nhánh để bôi đậm
        branchListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                branchListView.setCellFactory(cell -> new ListCell<String>() {
                    @Override
                    protected void updateItem(String s, boolean b) {
                        super.updateItem(s, b);
                        // First, we are only going to update the cell if there is actually an item to display there.
                        if (s != null) {

                            // Set the text of the cell to the Person's name
                            setText(s);

                            // If the Person has the "important" flag, we can make the text bold here
                            if (s.equals(newSelection)) {
                                setStyle("-fx-font-weight: bold");
                            } else {
                                // Remove any styles from the cell, because this Person isn't important
                                setStyle(null);
                            }
                        } else {
                            // If there is no item to display in this cell, set the text to null
                            setText(null);
                        }
                    }
                });
                System.out.println("haha");
            }
        });

        branchListContainer.getChildren().add(branchListView);

        newTitledPane.setContent(branchListContainer);
        StoreComponent.getInstance().getAccordion().getPanes().add(newTitledPane);

    }

    private void switchToBranch(File gitDirectory, String branchName) {
        try {
            Git git = Git.open(gitDirectory);
            git.checkout().setName(branchName).call();
            git.close();
            System.out.println("Đã chuyển đến nhánh: " + branchName);

            // Bôi đậm nhánh hiện tại
            StoreComponent.getInstance().getAccordion().getPanes().forEach(titledPane -> {
                VBox branchListContainer = (VBox) titledPane.getContent();
                ListView<String> branchListView = (ListView<String>) branchListContainer.getChildren().get(0);

//                branchListView.setPseudoClass(CURRENT_BRANCH_PSEUDO_CLASS, branchListView.getItems().contains(branchName));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

