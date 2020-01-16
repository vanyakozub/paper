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
    static boolean isWin = false;
    static String win = new String("win");
    public static Player player= new Player();
    static String ready = new String ("1");
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
            player.sendChanges(ready);
            while (Integer.parseInt(player.fromServer) <1){
                System.out.println(player.fromServer);
                player.fromServer = player.getFromServer();
            }

            player.sendChanges(ready);
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
        if(player.fromServer.equals("win")) {
            isWin = true;
            gameOver = true;
        }
        if (gameOver) {
            if(!isWin) {
                gc.setFill(Color.RED);
                gc.setFont(new Font("", 50));
                gc.fillText("GAME OVER", 100, 250);
                return;
            }
            else {
                gc.setFill(Color.PALEGREEN);
                gc.setFont(new Font("", 50));
                gc.fillText("YOU WIN", 150, 250);
                return;
            }
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
                    player.sendChanges(win);
                    gameOver = true;
                }
                tail.get(1).val = player.getTail();
                break;
            case down:
                tmp = new Corner(tail.get(0).x, tail.get(0).y, player.getHead());
                tmp.y++;
                tail.add(0, tmp);
                if (tail.get(0).y > height - 1) {
                    player.sendChanges(win);
                    gameOver = true;
                }
                tail.get(1).val = player.getTail();
                break;
            case left:
                tmp = new Corner(tail.get(0).x, tail.get(0).y, player.getHead());
                tmp.x--;
                tail.add(0,tmp);
                if (tail.get(0).x < 0) {
                    player.sendChanges(win);
                    gameOver = true;
                }
                tail.get(1).val = player.getTail();
                break;
            case right:
                tmp = new Corner(tail.get(0).x, tail.get(0).y, player.getHead());
                tmp.x++;
                tail.add(0, tmp);
                if (tail.get(0).x > width - 1) {
                    player.sendChanges(win);
                    gameOver = true;
                }
                tail.get(1).val = player.getTail();
                break;

        }
        player.sendChanges(tail.get(0).toString());

        // self destroy
        for (int i = 1; i < tail.size(); i++) {
            if (tail.get(0).x == tail.get(i).x && tail.get(0).y == tail.get(i).y) {
                player.sendChanges(win);
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
        Corner enemyHead = enemyTail.get(0);
        for (Corner c : tail) {
            if (c.equalsC(enemyHead)){
                player.sendChanges(win);
                gameOver = true;
            }
        }
        // tail
        for (Corner c : tail) {
            if(field[c.x][c.y].val != player.getArea()) {
                field[c.x][c.y].val = c.val;
            }
            tail.get(0).val = player.getTail();
            /*gc.setFill(Color.LIGHTGREEN);
            gc.fillRect(c.x * cornersize, c.y * cornersize, cornersize - 1, cornersize - 1);
            gc.setFill(Color.LIGHTGREEN);
            gc.fillRect(c.x * cornersize, c.y * cornersize, cornersize - 2, cornersize - 2);*/
            if(tail.size()>2 && field[tail.get(0).x][tail.get(0).y].val==player.getArea()) {
                if (player.getIsGreen()) {
                    //greenFilling();
                    OlyasFillingRed(2);
                }
                else {
                    //redFilling();
                    OlyasFillingRed(4);
                }
            }
        }
        for (Corner c :enemyTail) {
            if(field[c.x][c.y].val != player.getEnemyArea()) {
                field[c.x][c.y].val = c.val;
            }
            if(enemyTail.size()>2 && field[enemyTail.get(0).x][enemyTail.get(0).y].val==player.getEnemyArea()) {
                if (player.getIsGreen()) {
                    //enemyRedFilling();
                    OlyasFillingRed(4);
                }
                else {
                    //enemyGreenFilling();
                    OlyasFillingRed(2);

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
        if(player.getIsGreen()) {
            gc.setFill(Color.ORANGE);
        }
        else {
            gc.setFill(Color.YELLOW);
        }
        gc.fillRect(enemyTail.get(0).x*cornersize, enemyTail.get(0).y*cornersize, cornersize-1,cornersize-1);
    }

    public static void OlyasFillingRed(int color) {
        int headC=-1;
        int areaC = -1;
        Corner t_t = new Corner(-1,-1,-1);
        if(color == 4) {
            headC = 6;
            areaC = 3;
        }
        else {
            headC = 5;
            areaC = 1;
        }
        if(player.getIsGreen()){
            if(headC == 5){
                field[tail.get(0).y][tail.get(0).y].val = 2;
            }
            else field[enemyTail.get(0).x][enemyTail.get(0).y].val = 4;
        }
        else {
            if (headC == 5) {
                field[enemyTail.get(0).x][enemyTail.get(0).y].val = 2;
            } else {
                field[tail.get(0).y][tail.get(0).y].val = 4;
            }
        }
        for(int j = 0; j < width; j++) {
            for (int i = 0; i < height; i++){
                if(field[i][j].val == headC ){
                    field[i][j].val = color;
                }
                if(field[i][j].val == color ||field[i][j].val ==areaC ) {
                    if (t_t.x < 0) {
                        t_t.x = field[i][j].x;
                        t_t.y = field[i][j].y;
                        t_t.val = field[i][j].val;
                    }
                    else {
                        Corner tmp = field[i][j];
                        if(field[i][j].val == areaC && t_t.val == field[i][j].val){
                            t_t.x = field[i][j].x;
                            t_t.y = field[i][j].y;
                            continue;
                        }
                        if(tmp.x - t_t.x < 2){
                            t_t.x = -1;
                            t_t.y = -1;
                        }
                        else {
                            for(int k = t_t.x; k < tmp.x; k++) {
                                field[k][j].val = color;
                            }
                            t_t.x = -1;
                            t_t.y = -1;
                        }
                    }
                }

            }
            t_t.x = -1;
            t_t.y = -1;
        }

        t_t.x = -1;
        t_t.y = -1;
        for(int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                if(field[j][i].val == color) {
                    if (t_t.x < 0) {
                        t_t.x = field[j][i].x;
                        t_t.y = field[j][i].y;
                    }
                    else {
                        Corner tmp = field[j][i];
                        if(tmp.y - t_t.y < 2){
                            t_t.x = -1;
                            t_t.y = -1;
                        }
                        else {
                            for(int k = t_t.y; k < tmp.y; k++) {
                                field[j][k].val = color;
                            }
                            t_t.x = -1;
                            t_t.y = -1;
                        }
                    }
                }
            }
            t_t.x = -1;
            t_t.y = -1;
        }
        if(color == 2){
            for(int i = 0; i < width; i++) {
                for(int j = 0; j < height; j++) {
                    if(field[i][j].val == color) {
                        field[i][j].val = 1;
                    }
                }
            }
            if(player.getIsGreen()){
                Corner head = new Corner(tail.get(0).x,tail.get(0).y,headC);
                tail.clear();
                tail.add(0,head);
            }
            else {
                Corner head = new Corner(enemyTail.get(0).x,enemyTail.get(0).y,headC);
                enemyTail.clear();
                enemyTail.add(0, head);
            }


        }
        if(color == 4) {
            for(int i = 0; i < width; i++) {
                for(int j = 0; j < height; j++) {
                    if(field[i][j].val == color) {
                        field[i][j].val = 3;
                    }
                }
            }
            if(player.getIsGreen()){
                Corner head = new Corner(enemyTail.get(0).x,enemyTail.get(0).y,headC);
                enemyTail.clear();
                enemyTail.add(0, head);
            }
            else {
                Corner head = new Corner(tail.get(0).x,tail.get(0).y,headC);
                tail.clear();
                tail.add(0,head);
            }
        }
    }
    public int getSpeed (){
        return speed;
    }
    public int getRedHead(){

        return field[0][0].val;
    }
}