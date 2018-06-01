package sample;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Square extends Rectangle {

    private Saber saber;

    public boolean hasSaber() {
        return saber != null;
    }

    public Saber getSaber() {
        return saber;
    }

    public void setSaber(Saber saber) {
        this.saber = saber;
    }

    Square(boolean white, int x, int y) {
        setWidth(Main.CELL_SIZE);
        setHeight(Main.CELL_SIZE);

        relocate(x * Main.CELL_SIZE, y * Main.CELL_SIZE);

        if (white){
            setFill(Color.WHITE);
        } else {
            setFill(Color.BLACK);
        }
    }
}