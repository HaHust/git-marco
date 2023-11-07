package com.example.gitmacro.controller;

import com.example.gitmacro.model.GlobalData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import java.util.List;

/**
 * Created by hs on 11/8/2023.
 */


public class MergeController {
    @FXML
    private ListView<String> branchListView;

    public void initializeSource() {
        // Initialize the ListView with dummy data
        List<String> branches1 = GlobalData.getInstance().getBranches();
        ObservableList<String> branches = FXCollections.observableArrayList(branches1);
        branchListView.setItems(branches);

        // Add double-click event handler
        branchListView.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                // Lưu phần tử được chọn
                String selectedBranch = branchListView.getSelectionModel().getSelectedItem();
                GlobalData.getInstance().getMergeModel().setCurrentbranch(selectedBranch);
                System.out.println(selectedBranch);
                // Thực hiện các bước lưu và đóng view Merge ở đây

                // Ví dụ: Đóng view Merge
                closeMergeView();
            }
        });
    }
    public void initializeDes() {
        // Initialize the ListView with dummy data
        List<String> branches1 = GlobalData.getInstance().getBranches();
        ObservableList<String> branches = FXCollections.observableArrayList(branches1);
        branchListView.setItems(branches);

        // Add double-click event handler
        branchListView.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                // Lưu phần tử được chọn
                String selectedBranch = branchListView.getSelectionModel().getSelectedItem();
                GlobalData.getInstance().getMergeModel().setDestinationBranch(selectedBranch);
                System.out.println(selectedBranch);
                // Thực hiện các bước lưu và đóng view Merge ở đây

                // Ví dụ: Đóng view Merge
                closeMergeView();
            }
        });
    }

    private void closeMergeView() {
        // Thực hiện các bước cần thiết để đóng view Merge ở đây
        // Ví dụ: sử dụng Stage để đóng cửa sổ
        Stage stage = (Stage) branchListView.getScene().getWindow();
        stage.close();
    }

}
