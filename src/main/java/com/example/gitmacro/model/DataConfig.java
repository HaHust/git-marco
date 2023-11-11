package com.example.gitmacro.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DataConfig {
    private boolean isUserNamePass;
    private String token;
    private String userName;
    private String password;
    private List<File> repos;
    private final String CONFIG_PATH = "config.properties";

    // Phương thức đọc thông tin từ tệp properties
    public static DataConfig loadConfig(String filePath) {
        Properties properties = new Properties();

        try {
            FileInputStream input = new FileInputStream(filePath);
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            try (FileOutputStream output = new FileOutputStream(filePath)) {
                properties.store(output, null);
            } catch (IOException e1) {
                e.printStackTrace();
            }
            return new DataConfig();
        }

        DataConfig config = new DataConfig();
        config.setUserNamePass(Boolean.parseBoolean(properties.getProperty("isUserNamePass", "false")));
        config.setToken(properties.getProperty("token", ""));
        config.setUserName(properties.getProperty("userName", ""));
        config.setPassword(properties.getProperty("password", ""));

        // Xử lý danh sách repos (đường dẫn đến các tệp)
        String reposString = properties.getProperty("repos", "");
        String[] reposArray = reposString.split(",");
        List<File> reposList = new ArrayList<>();
        for (String repoPath : reposArray) {
            File repoFile = new File(repoPath.trim());
            if (repoFile.exists()) {
                reposList.add(repoFile);
            }
        }
        config.setRepos(reposList);

        return config;
    }

    // Phương thức lưu thông tin vào tệp properties
    public void saveConfig(String filePath) {
        Properties properties = new Properties();
        properties.setProperty("isUserNamePass", String.valueOf(isUserNamePass));
        properties.setProperty("token", token);
        properties.setProperty("userName", userName);
        properties.setProperty("password", password);

        // Chuyển đổi danh sách repos thành một chuỗi ngăn cách bởi ","
        StringBuilder reposString = new StringBuilder();
        for (File repo : repos) {
            reposString.append(repo.getAbsolutePath()).append(",");
        }
        properties.setProperty("repos", reposString.toString());

        try (FileOutputStream output = new FileOutputStream(filePath)) {
            properties.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Các phương thức getter và setter khác
    // ...

    public boolean isUserNamePass() {
        return isUserNamePass;
    }

    public void setUserNamePass(boolean userNamePass) {
        isUserNamePass = userNamePass;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<File> getRepos() {
        return repos;
    }

    public void setRepos(List<File> repos) {
        this.repos = repos;
    }

    public void addRepo(File repos) {
        this.repos.add(repos);
    }
}
