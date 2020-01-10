import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import static java.lang.Math.abs;

public class Main  {
    // variable
    /*static int speed = 2;
    static int width = 20;
    static int height = 20;
    static int cornersize = 25;
    static Corner[][] field = new Corner[width][height];

    static List<Corner> snake = new ArrayList<Corner>();
    static Dir direction = Dir.left;
    static boolean gameOver = false;
    static Random rand = new Random();

    public enum Dir {
        left, right, up, down
    }*/

    /*public static class Corner {
        int x;
        int y;
        int val;

        public Corner(int x, int y, int val) {
            this.x = x;
            this.y = y;
            this.val = val;
        }

    }*/

    /*public void start(Stage primaryStage) {
        try {
            //newFood();

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

            // add start snake parts
            snake.add(new Corner(width - 1, height - 1, 2));


            primaryStage.setScene(scene);
            primaryStage.setTitle("Paper IO");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // tick
    public static void tick(GraphicsContext gc) {
        if (gameOver) {
            gc.setFill(Color.RED);
            gc.setFont(new Font("", 50));
            gc.fillText("GAME OVER", 100, 250);
            return;
        }
        if(field[snake.get(snake.size() - 1).y][snake.get(snake.size() - 1).x].val ==  1) {
            for (int i = snake.size() - 1; i >= 1; i--) {
                snake.get(i).x = snake.get(i - 1).x;
                snake.get(i).y = snake.get(i - 1).y;
            }
        }
        Corner tmp;

        switch (direction) {
            case up:
                tmp = new Corner(snake.get(0).x, snake.get(0).y, 5);
                tmp.y--;
                snake.add(0,tmp);
                if (snake.get(0).y < 0) {
                    gameOver = true;
                }
                snake.get(1).val = 2;
                break;
            case down:
                tmp = new Corner(snake.get(0).x, snake.get(0).y, 5);
                tmp.y++;
                snake.add(0, tmp);
                if (snake.get(0).y > height - 1) {
                    gameOver = true;
                }
                snake.get(1).val = 2;
                break;
            case left:
                tmp = new Corner(snake.get(0).x, snake.get(0).y, 5);
                tmp.x--;
                snake.add(0,tmp);
                if (snake.get(0).x < 0) {
                    gameOver = true;
                }
                snake.get(1).val = 2;
                break;
            case right:
                tmp = new Corner(snake.get(0).x, snake.get(0).y, 5);
                tmp.x++;
                snake.add(0, tmp);
                if (snake.get(0).x > width - 1) {
                    gameOver = true;
                }
                snake.get(1).val = 2;
                break;

        }

        // self destroy
        for (int i = 1; i < snake.size(); i++) {
            if (snake.get(0).x == snake.get(i).x && snake.get(0).y == snake.get(i).y) {
                gameOver = true;
            }
        }


        // fill
        // background
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, width * cornersize, height * cornersize);

        // snake
        for (Corner c : snake) {
            if(field[c.x][c.y].val != 1) {
                field[c.x][c.y].val = c.val;
            }
            /*gc.setFill(Color.LIGHTGREEN);
            gc.fillRect(c.x * cornersize, c.y * cornersize, cornersize - 1, cornersize - 1);
            gc.setFill(Color.LIGHTGREEN);
            gc.fillRect(c.x * cornersize, c.y * cornersize, cornersize - 2, cornersize - 2);*/
      /*      if(snake.size()>2 && field[snake.get(0).x][snake.get(0).y].val==1) {
                anotherFilling();
                //fillField();
            }
        }
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
                }
            }
        }
        gc.setFill(Color.YELLOW);
        gc.fillRect(snake.get(0).x * cornersize, snake.get(0).y*cornersize, cornersize -1, cornersize -1);

    }
    public static void anotherFilling() {
        for(Corner c: snake) {
            for(int i = c.y + 1; i < height; i++) {
                if(field[c.x][i].val == 0) {
                    field[c.x][i].val = 1;
                }
                else if(field[c.x][i].val ==1 || field[c.x][i].val == 2) {
                    break;
                }
            }
        }
        Corner tmp = new Corner(snake.get(0).x, snake.get(0).y, snake.get(0).val);
        for (Corner c: snake) {
            field[c.x][c.y].val = 1;
        }
        snake.clear();
        snake.add(tmp);
    }
    public static void fillField() {
        boolean flag = false;
        //boolean fill = false;
        int remain = 0;
        //int k = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if ((field[i][j].val == 2 || field[i][j].val == 5) && flag == false) {
                    flag = true;
                    continue;
                }
                if (field[i][j].val == 0 && flag) {
                    remain = height - i % height;
            /*for(int j = field[i].y; j < width; j++) {
                if(field[i/width + j].val == 2)
                    fill = true;
            }
            if (fill)*/
       /*             field[i][j].val = 1;
                    continue;
                }
                if (i % height == 0) {
                    flag = false;
                }
                if (remain == 0 && flag) {
                    flag = false;
                }
                if ((field[i][j].val == 2 || field[i][j].val == 5) && flag) {
                    flag = false;
                    continue;
                }
            }
        }
        Corner tmp = new Corner(snake.get(0).x, snake.get(0).y, snake.get(0).val);
        for (Corner c: snake) {
            field[c.x][c.y].val = 1;
        }
        snake.clear();
        snake.add(tmp);
    }*/


    public static void main(String[] args) {


        Game game = new Game();
        game.begin(args);
    }
}
