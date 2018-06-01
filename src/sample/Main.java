package sample;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;



public class Main extends Application {

    public static final int CELL_SIZE = 50;
    private static final int WIDTH = 8;
    private static final int HEIGHT = 8;

    private Square[][] board = new Square[WIDTH][HEIGHT];

    private Group squareGroup = new Group();
    private Group saberGroup = new Group();

    private int xod = 0;
    private boolean count = true;

    private int countred = 0;
    private int countwhite = 0;


    private Stage primaryStage;

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(WIDTH * CELL_SIZE + 125, HEIGHT * CELL_SIZE);
        root.getChildren().addAll(squareGroup, saberGroup);

        javafx.scene.control.Button btn = new Button("Передать ход");
        btn.setStyle("-fx-font: 14 Arial; -fx-base: #000000;");
        btn.relocate(410 , 190);
        btn.setOnAction(event -> {
            count = !count;
            if (xod == 0 && !count){
                xod++;
                count = true;
            } else if (xod == 1 && !count) {
                xod--;
                count = true;
            }
        });

        root.getChildren().add(btn);

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Square square = new Square((x + y) % 2 == 0, x, y);
                board[x][y] = square;

                squareGroup.getChildren().add(square);

                Saber saber = null;

                if (y < 3 && x < 3) {
                    saber = makeCheker(SaberType.RED, x, y);
                }

                if (y >= 5 && x >= 5) {
                    saber = makeCheker(SaberType.DARKGRAY, x, y);
                }

                if (saber != null) {
                    square.setSaber(saber);
                    saberGroup.getChildren().add(saber);
                }
            }
        }

        return root;
    }

    private MoveResult tryMove(Saber saber, int newX, int newY) {
        int x0 = toBoard(saber.getOldX());
        int y0 = toBoard(saber.getOldY());

        if (board[newX][newY].hasSaber() || (newY - y0 != 0 && newX - x0 != 0) ||
                Math.abs(newY - y0) > 2 || Math.abs(newX - x0) > 2)  {
            return new MoveResult(MoveType.NO);
        }

        boolean d1 = (Math.abs(x0 - newX) == 2 || Math.abs(y0 - newY) == 2 ) && newX > x0 && newY == y0
                && !board[newX-1][newY].hasSaber();

        boolean d2 = (Math.abs(x0 - newX) == 2 || Math.abs(y0 - newY) == 2 ) && newX < x0 && newY == y0
                && !board[newX+1][newY].hasSaber();

        boolean d3 = (Math.abs(x0 - newX) == 2 || Math.abs(y0 - newY) == 2 ) && newX == x0 && newY < y0
                && !board[newX][newY+1].hasSaber();

        boolean d4 = (Math.abs(x0 - newX) == 2 || Math.abs(y0 - newY) == 2 ) && newX == x0 && newY > y0
                && !board[newX][newY-1].hasSaber();

        if ((Math.abs(newY - y0) == 2 || Math.abs(newX - x0) == 2) && (d1 || d2 || d3 || d4)) {
            return new MoveResult(MoveType.NO);
        }

        if (saber.getType() == SaberType.RED && xod == 1){
            return new MoveResult(MoveType.NO);
        }
        if (saber.getType() == SaberType.DARKGRAY && xod == 0){
            return new MoveResult(MoveType.NO);
        }


        return new MoveResult(MoveType.YES);
    }

    private int toBoard(double pixel) {
        return (int)(pixel + CELL_SIZE / 2) / CELL_SIZE;
    }
    private Saber makeCheker(SaberType type, int x, int y) {
        Saber saber = new Saber(type, x, y);


        saber.setOnMouseReleased(e -> {
            int newX = toBoard(saber.getLayoutX());
            int newY = toBoard(saber.getLayoutY());

            MoveResult result;

            if (newX < 0 || newY < 0 || newX >= WIDTH || newY >= HEIGHT) {
                result = new MoveResult(MoveType.NO);
            } else {
                result = tryMove(saber, newX, newY);
            }

            int x0 = toBoard(saber.getOldX());
            int y0 = toBoard(saber.getOldY());


            switch (result.getType()) {
                case NO:
                    saber.noMove();
                    break;
                case YES:

                    boolean a1 = (Math.abs(x0 - newX) == 2 || Math.abs(y0 - newY) == 2 )&& newX + 2 < 8
                            && board[newX+1][newY].hasSaber() && !board[newX + 2][newY].hasSaber() && (newX + 2 != x0 && newY != y0);

                    boolean a2 = (Math.abs(x0 - newX) == 2 || Math.abs(y0 - newY) == 2 )&& newX - 2 >= 0
                            && board[newX-1][newY].hasSaber() && !board[newX - 2][newY].hasSaber() && (newX - 2 != x0 && newY != y0);

                    boolean a3 = (Math.abs(x0 - newX) == 2 || Math.abs(y0 - newY) == 2 )&& newY + 2 < 8
                            && board[newX][newY+1].hasSaber() && !board[newX][newY+2].hasSaber() && (newX  != x0 && newY + 2 != y0);

                    boolean a4 = (Math.abs(x0 - newX) == 2 || Math.abs(y0 - newY) == 2 )&& newY - 2 >= 0
                            && board[newX][newY-1].hasSaber() && !board[newX][newY-2].hasSaber()&& (newX  != x0 && newY - 2 != y0);

                    if (!a1 && !a2 && !a3 && !a4 && saber.getType() == SaberType.RED){
                        xod++;
                    }else if ((!a1 && !a2 && !a3 && !a4) && saber.getType() == SaberType.DARKGRAY){
                        xod--;
                    }

                    saber.go(newX, newY);
                    board[x0][y0].setSaber(null);
                    board[newX][newY].setSaber(saber);

                    for (int x5 = 0 ; x5 < 4 ; x5++){
                        for(int y5 = 0; y5 < 4; y5++){
                            if (board[x5][y5].hasSaber() && board[x5][y5].getSaber().getType() == SaberType.DARKGRAY){
                                countred++;
                            }
                        }
                    }

                    for (int x6 = 5 ; x6 < 8 ; x6++){
                        for(int y6 = 5; y6 < 8; y6++){
                            if (board[x6][y6].hasSaber() && board[x6][y6].getSaber().getType() == SaberType.RED){
                                countwhite++;
                            }
                        }
                    }

                    if (countred == 9 || countwhite == 9){
                        FlowPane root = new FlowPane();
                        root.setPrefSize(400, 400);
                        Label l;
                        if (countred == 9) {
                            l = new Label("Победили Белые!");
                        } else {
                            l = new Label("Победили Красные!");
                        }
                        root.getChildren().addAll(l);
                        root.setAlignment(Pos.CENTER);
                        primaryStage.setScene(new Scene(root));
                    } else {
                        countred = 0;
                        countwhite = 0;
                    }

                    break;
            }
        });

        return saber;
    }


    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Corners by Aida inc.");
        Scene scene = new Scene(createContent());
        primaryStage.setScene(scene);

        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}