package com.example.gitmacro.controller;


import com.example.gitmacro.GitUtils;
import com.example.gitmacro.model.GlobalData;
import com.example.gitmacro.HelloApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeController {
    @FXML
    private Button btnChooseRepo;
    @FXML
    private Button btnSource;
    @FXML
    private Button btnDes;
    @FXML
    private Button btnMerge;
    @FXML
    private Label labelMerge;
    @FXML
    private Accordion accordion;

    private final Map<String, TitledPane> folderMap = new HashMap<>();

    @FXML
    protected void onBtnChooseRepo(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Chọn thư mục");

        File selectedDirectory = directoryChooser.showDialog(null);

        if (selectedDirectory != null) {
            if (isGitRepository(selectedDirectory)) {
                System.out.println("Thư mục đã chọn là một kho lưu trữ Git.");
                // Kiểm tra xem đường dẫn đã tồn tại trong Accordion chưa
                if (!folderMap.containsKey(selectedDirectory.getAbsolutePath())) {
                    GlobalData.getInstance().setFile(selectedDirectory);
                    GlobalData.getInstance().getDataConfig().addRepo(selectedDirectory);
                    List<Ref> branches = GitUtils.viewAllLocalBranches(selectedDirectory);
                    addFolderToAccordion(selectedDirectory.getName(), selectedDirectory.getPath(), branches);
                } else {
                    System.out.println("Thư mục đã tồn tại trong Accordion.");
                }
            } else {
                System.out.println("Thư mục đã chọn không phải là một kho lưu trữ Git.");
            }
        } else {
            System.out.println("Không có thư mục nào được chọn.");
        }
    }

    @FXML
    protected void onBtnSource(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("merge-view.fxml"));
            Parent root = loader.load();

            MergeController mergeController = loader.getController();
            mergeController.initializeSource();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Branch List");

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onBtnDes(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("merge-view.fxml"));
            Parent root = loader.load();

            MergeController mergeController = loader.getController();
            mergeController.initializeDes();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Branch List");

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onBtnMerge(ActionEvent event) throws IOException, GitAPIException {
        Git git = Git.open(GlobalData.getInstance().getFile());

        Repository repo = new FileRepositoryBuilder()
                .setGitDir(GlobalData.getInstance().getFile())
                .readEnvironment()
                .findGitDir()
                .build();

        String currentbranch = GlobalData.getInstance().getMergeModel().getCurrentbranch();
        String destinationBranch = GlobalData.getInstance().getMergeModel().getDestinationBranch();
        PullCommand source = git.pull().setRemote("origin").setRemoteBranchName(currentbranch);
        PullCommand des = git.pull().setRemote("origin").setRemoteBranchName(destinationBranch);
        try {
            // Cung cấp thông tin đăng nhập
            UsernamePasswordCredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider("HaHust", "Haqh1234");
            source.setCredentialsProvider(credentialsProvider);
            des.setCredentialsProvider(credentialsProvider);
            // Stage all files
            git.add().addFilepattern(".").call();

            // Stash changes
            git.stashCreate().call();
            git.checkout().setName(currentbranch).call();

            // Pull changes from branch 'a'
            git.pull().setCredentialsProvider(new UsernamePasswordCredentialsProvider("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", "")).call();

            // Switch to branch 'a'

            // Unstash changes
            git.stashApply().call();
            git.commit()
                    .setCommitter("HaHust", "vanha.hust@gmail.com")
                    .setMessage("Your commit message")
                    .call();
            // Push changes to branch 'a'
            git.push().setCredentialsProvider(new UsernamePasswordCredentialsProvider("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", "")).call();

            // Switch to branch 'b'
            git.checkout().setName(destinationBranch).call();
            git.pull().setCredentialsProvider(new UsernamePasswordCredentialsProvider("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", "")).call();

            // Merge changes from branch 'a' to branch 'b'
            MergeResult o = git.merge().include(git.getRepository().resolve(currentbranch)).call();
            if (o.getConflicts() != null && !o.getConflicts().isEmpty()) {
                // Handle conflicts
                labelMerge.setText("Conflicts occurred. Rolling back to the previous state.");

                ProcessBuilder pb =  new ProcessBuilder("git", "merge", "--abort");
                pb.directory(repo.getDirectory());
                Process pr = pb.start();
                pr.waitFor();

            } else

                labelMerge.setText("OKOKOK");
                git.push().setCredentialsProvider(new UsernamePasswordCredentialsProvider("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", "")).call();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(GlobalData.getInstance().getMergeModel().toString());
    }

    private boolean isGitRepository(File directory) {
        try {
            Repository repo = new FileRepositoryBuilder()
                    .setGitDir(new File(directory, ".git"))
                    .readEnvironment()
                    .findGitDir()
                    .build();

            return repo.getObjectDatabase().exists();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
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
        accordion.getPanes().add(newTitledPane);

        folderMap.put(folderPath, newTitledPane);
    }

    private void switchToBranch(File gitDirectory, String branchName) {
        try {
            Git git = Git.open(gitDirectory);
            git.checkout().setName(branchName).call();
            git.close();
            System.out.println("Đã chuyển đến nhánh: " + branchName);

            // Bôi đậm nhánh hiện tại
            accordion.getPanes().forEach(titledPane -> {
                VBox branchListContainer = (VBox) titledPane.getContent();
                ListView<String> branchListView = (ListView<String>) branchListContainer.getChildren().get(0);

//                branchListView.setPseudoClass(CURRENT_BRANCH_PSEUDO_CLASS, branchListView.getItems().contains(branchName));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
