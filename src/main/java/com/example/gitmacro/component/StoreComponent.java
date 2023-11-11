package com.example.gitmacro.component;

import javafx.scene.Scene;
import javafx.scene.control.Accordion;

/**
 * Created by hs on 11/12/2023.
 */


public class StoreComponent {
    private static StoreComponent instance;

    private Accordion accordion;

    private Scene scene;

    public static StoreComponent getInstance() {
        if (instance == null) {
            instance = new StoreComponent();
        }
        return instance;
    }

    public void startComponent() {
        accordion = (Accordion) scene.lookup("#" + "accordion");
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Accordion getAccordion() {
        return accordion;
    }

    public void setAccordion(Accordion accordion) {
        this.accordion = accordion;
    }
}
