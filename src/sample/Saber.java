package sample;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Saber extends StackPane{



    private SaberType type;
    private double mouseX, mouseY;
    private double oldX, oldY;

    public SaberType getType() {
        return type;
    }

    public double getOldX() {
        return oldX;
    }

    public double getOldY() {
        return oldY;
    }


    Saber(SaberType type, int x, int y) {
        this.type = type;

        go(x, y);

        Circle circle = new Circle(25, 25, Main.CELL_SIZE * 0.3);

        if (type == SaberType.DARKGRAY) {
            circle.setFill(Color.DARKGRAY);
        } else if(type == SaberType.RED){
            circle.setFill(Color.RED);
        }

        circle.setTranslateX((Main.CELL_SIZE - Main.CELL_SIZE * 0.34 * 2) / 2);
        circle.setTranslateY((Main.CELL_SIZE - Main.CELL_SIZE * 0.34 * 2) / 2);

        getChildren().addAll(circle);

        setOnMousePressed(e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });

        setOnMouseDragged((MouseEvent e) -> relocate(e.getSceneX() - mouseX + oldX, e.getSceneY() - mouseY + oldY));
    }

    public void go(int x, int y) {
        oldX = x * Main.CELL_SIZE;
        oldY = y * Main.CELL_SIZE;
        relocate(oldX, oldY);
    }

    public void noMove() {
        relocate(oldX, oldY);
    }

}
