package rea.ui;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import rea.SceneLayout;
import rea.components.GameMap;
import rea.components.Place;

public class GamePanel extends VerticalLayout {

    private Div background;
    private Character character;
    private SceneLayout scene;
    private Image backgroundImage;

    public GamePanel(GameMap gameMap) {
        setWidthFull();
        setHeightFull();
        setPadding(false);
        setSpacing(false);

        background = new Div();
        background.setWidthFull();
        background.setHeight("600px");

        Place startPlace = gameMap.getStartPlace();
        if (startPlace != null && startPlace.getVisual() != null) {
            String imagePath = startPlace.getVisual().getPathname();
            background.getStyle().set("background-image", "url('" + imagePath + "')");
            background.getStyle().set("background-size", "cover");
        }

        add(background);
    }

    public void setBackground(String imagePath, int width, int height) {
        background.setWidth(width + "px");
        background.setHeight(height + "px");
        background.getStyle().set("background-image", "url('" + imagePath + "')");
        background.getStyle().set("background-size", "cover");
    }

}
