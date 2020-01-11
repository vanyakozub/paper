import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Game extends Application {
    static int speed = 2;
    static int width = 20;
    static int height = 20;
    static int cornersize = 25;
    static Corner[][] field = new Corner[width][height];
    public enum Dir {
        left, right, up, down
    }
    static List<Corner> tail = new ArrayList<Corner>();
    static List<Corner> enemyTail = new ArrayList<Corner>();
    static Dir direction = Dir.left;
    static boolean gameOver = false;
    public static Player player= new Player();

    public void begin(String[] args) {
        this.launch(args);
    }

    public void firstInit(){
        for(int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                field[i][j] = new Corner(i, j, 0);
            }
        }
        field[0][0].val = 3;
        field[0][1].val = 3;
        field[1][0].val = 3;
        field[1][1].val = 3;
        field[width-1][height - 1].val = 1;
        field[width-1][height - 2].val = 1;
        field[width - 2][height - 1].val = 1;
        field[(width - 2)][(height ) - 2].val = 1;

    }

    public void start(Stage primaryStage) {
        try {

            player.start();

            //newFood();
            firstInit();
            VBox root = new VBox();
            Canvas c = new Canvas(width * cornersize, height * cornersize);
            GraphicsContext gc = c.getGraphicsContext2D();
            root.getChildren().add(c);

            new AnimationTimer() {
                long lastTick = 0;

                public void handle(long now) {
                    if (lastTick == 0) {
                        lastTick = now;
                        tick(gc);
                        return;
                    }

                    if (now - lastTick > 1000000000 / speed) {
                        lastTick = now;
                        tick(gc);
                    }
                }

            }.start();
            if(player.getIsGreen()) {
                direction = Dir.left;
            }
            else {
                direction = Dir.right;
            }
            Scene scene = new Scene(root, width * cornersize, height * cornersize);

            // control
            scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
                if (key.getCode() == KeyCode.W) {
                    direction = Dir.up;
                }
                if (key.getCode() == KeyCode.A) {
                    direction = Dir.left;
                }
                if (key.getCode() == KeyCode.S) {
                    direction = Dir.down;
                }
                if (key.getCode() == KeyCode.D) {
                    direction = Dir.right;
                }

            });

            // add start tail parts
            if(player.getIsGreen()) {
                tail.add(new Corner(width - 1, height - 1, player.getTail()));
                enemyTail.add(new Corner(0,0, player.getEnemyTail()));
            }
            else {
                tail.add(new Corner(0,0,player.getTail()));
                enemyTail.add(new Corner(width - 1, height - 1, player.getEnemyTail()));
            }


            primaryStage.setScene(scene);
            primaryStage.setTitle("Paper IO"+ "isGreen" + "=" + player.getIsGreen());
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void tick(GraphicsContext gc) {
        if (gameOver) {
            gc.setFill(Color.RED);
            gc.setFont(new Font("", 50));
            gc.fillText("GAME OVER", 100, 250);
            return;
        }
        /*if(field[tail.get(tail.size() - 1).y][tail.get(tail.size() - 1).x].val ==  1) {
            for (int i = tail.size() - 1; i >= 1; i--) {
                tail.get(i).x = tail.get(i - 1).x;
                tail.get(i).y = tail.get(i - 1).y;
            }
        }*/
        Corner tmp;

        switch (direction) {
            case up:
                tmp = new Corner(tail.get(0).x, tail.get(0).y, player.getHead());
                tmp.y--;
                tail.add(0,tmp);
                if (tail.get(0).y < 0) {
                    gameOver = true;
                }
                tail.get(1).val = player.getTail();
                break;
            case down:
                tmp = new Corner(tail.get(0).x, tail.get(0).y, player.getHead());
                tmp.y++;
                tail.add(0, tmp);
                if (tail.get(0).y > height - 1) {
                    gameOver = true;
                }
                tail.get(1).val = player.getTail();
                break;
            case left:
                tmp = new Corner(tail.get(0).x, tail.get(0).y, player.getHead());
                tmp.x--;
                tail.add(0,tmp);
                if (tail.get(0).x < 0) {
                    gameOver = true;
                }
                tail.get(1).val = player.getTail();
                break;
            case right:
                tmp = new Corner(tail.get(0).x, tail.get(0).y, player.getHead());
                tmp.x++;
                tail.add(0, tmp);
                if (tail.get(0).x > width - 1) {
                    gameOver = true;
                }
                tail.get(1).val = player.getTail();
                break;

        }
        player.sendChanges(tail.get(0).toString());

        // self destroy
        for (int i = 1; i < tail.size(); i++) {
            if (tail.get(0).x == tail.get(i).x && tail.get(0).y == tail.get(i).y) {
                gameOver = true;
            }
        }


        // fill
        // background
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, width * cornersize, height * cornersize);
        String[] answer;
        String delimeter = ";";
        System.out.println(player.fromServer);
        answer = player.fromServer.split(delimeter);
        if(answer.length == 3) {
            enemyTail.add(0, new Corner(Integer.parseInt(answer[0]), Integer.parseInt(answer[1]), Integer.parseInt(answer[2])));
            enemyTail.get(1).val = player.getEnemyTail();
        }
        // tail
        for (Corner c : tail) {
            if(field[c.x][c.y].val != player.getArea()) {
                field[c.x][c.y].val = c.val;
            }
            /*gc.setFill(Color.LIGHTGREEN);
            gc.fillRect(c.x * cornersize, c.y * cornersize, cornersize - 1, cornersize - 1);
            gc.setFill(Color.LIGHTGREEN);
            gc.fillRect(c.x * cornersize, c.y * cornersize, cornersize - 2, cornersize - 2);*/
            if(tail.size()>2 && field[tail.get(0).x][tail.get(0).y].val==player.getArea()) {
                if (player.getIsGreen()) {
                    greenFilling();
                }
                else {
                    redFilling();
                }
            }
        }
        for (Corner c :enemyTail) {
            if(field[c.x][c.y].val != player.getEnemyArea()) {
                field[c.x][c.y].val = c.val;
            }
            if(enemyTail.size()>2 && field[enemyTail.get(0).x][enemyTail.get(0).y].val==player.getEnemyArea()) {
                if (player.getIsGreen()) {
                    //greenFilling();
                }
                else {
                    //redFilling();
                }
            }
        }

        //fillField();

        for(Corner[] corn : field) {
            for(Corner c: corn){
                switch (c.val) {
                    case 0:
                        break;
                    case 1:
                        gc.setFill(Color.GREEN);
                        gc.fillRect(c.x * cornersize, c.y * cornersize, cornersize - 1, cornersize - 1);
                        break;
                    case 2:
                        gc.setFill(Color.LIGHTGREEN);
                        gc.fillRect(c.x * cornersize, c.y * cornersize, cornersize - 1, cornersize - 1);
                        break;
                    case 3:
                        gc.setFill(Color.RED);
                        gc.fillRect(c.x * cornersize, c.y * cornersize, cornersize - 1, cornersize - 1);
                        break;
                    case 4:
                        gc.setFill(Color.LIGHTCORAL);
                        gc.fillRect(c.x * cornersize, c.y * cornersize, cornersize - 1, cornersize - 1);
                        break;
                    case 5:
                        gc.setFill(Color.YELLOW);
                        gc.fillRect(c.x * cornersize, c.y * cornersize, cornersize - 1, cornersize - 1);
                        break;
                    case 6:
                        gc.setFill(Color.ORANGE);
                        gc.fillRect(c.x * cornersize, c.y * cornersize, cornersize - 1, cornersize - 1);
                        break;
                }
            }
        }
        if(player.getIsGreen()) {
            gc.setFill(Color.YELLOW);
        }
        else {
            gc.setFill(Color.ORANGE);
        }
        gc.fillRect(tail.get(0).x * cornersize, tail.get(0).y*cornersize, cornersize -1, cornersize -1);

    }

    public static void greenFilling() {
        for(Corner c: tail) {
            boolean space = false;
            for(int k = c.y + 1; k < height; k++) {
                if(field[c.x][k].val == player.getTail() ||field[c.x][k].val ==player.getArea())
                    space = true;

            }
            for(int i = c.y + 1; space && i < height; i++) {

                if(field[c.x][i].val == 0) {
                    field[c.x][i].val = 1;
                }
                else if(field[c.x][i].val ==1 || field[c.x][i].val == 2) {
                    break;
                }
            }
        }
        Corner tmp = new Corner(tail.get(0).x, tail.get(0).y, tail.get(0).val);
        for (Corner c: tail) {
            field[c.x][c.y].val = 1;
        }
        tail.clear();
        tail.add(tmp);
    }
    public static void redFilling() {
        for(Corner c: tail) {
            boolean space = false;
            for(int k = c.y +1; k < height; k++) {
                if(field[c.x][k].val == player.getTail() ||field[c.x][k].val ==player.getArea())
                    space = true;

            }
            for(int i = c.y +1; space && i < height; i++) {

                if(field[c.x][i].val == 0) {
                    field[c.x][i].val = 3;
                }
                else if(field[c.x][i].val ==3 || field[c.x][i].val == 4) {
                    break;
                }
            }
        }
        Corner tmp = new Corner(tail.get(0).x, tail.get(0).y, tail.get(0).val);
        for (Corner c: tail) {
            field[c.x][c.y].val = 3;
        }
        tail.clear();
        tail.add(tmp);
    }
}
