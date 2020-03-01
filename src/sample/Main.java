package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class Main extends Application {
    private int GAME_AREA_WIDTH=500, GAME_AREA_HEIGHT=800;
    private Point2D GameAreaLocation=new Point2D(100,100);

    private BorderPane root = new BorderPane();
    private Sprite player=new Sprite(300,759,40,40,"player", Color.BLUE);
    private Enemy rec1=new Enemy(20.0,20.0,100.0,400,700,40,60,Color.BLACK);
    private Enemy rec2=new Enemy(0.0,30.0,100.0,400,700,40,30,Color.YELLOW);
    private Enemy rec3=new Enemy(10.0,40.0,100.0,300,500,10,60,Color.GREEN);
    private Enemy rec4=new Enemy(-20.0,-20.0,100.0,150,150,50,50,Color.BROWN);
    //private TextField TextBox=new TextField("TEdsgSDGDG");
    //private IntersectionScore TextBox=new IntersectionScore(GameAreaLocation.getX(),GameAreaLocation.getY(),0);
    private boolean isPaused=false;
    private EventHandler<MouseEvent> PlayerEvent= new EventHandler<MouseEvent>()
    {
        @Override
        public void handle(MouseEvent t) {
        player.setFill(Color.RED);
        player.setTranslateX(t.getSceneX()-player.getWidth()/2);
        player.setTranslateY(t.getSceneY()-player.getHeight()/2);
    }
    };

    private Line line1,line2,line3,line4;

    private Parent createContent(){
        root.setPrefSize(GAME_AREA_WIDTH+GameAreaLocation.getX()+100,GAME_AREA_HEIGHT+GameAreaLocation.getY()+100);

        line1 = new Line(GameAreaLocation.getX(),GameAreaLocation.getY(),GameAreaLocation.getX(),GameAreaLocation.getY()+GAME_AREA_HEIGHT);
        line2 = new Line(GameAreaLocation.getX(),GameAreaLocation.getY(),GameAreaLocation.getX()+GAME_AREA_WIDTH,GameAreaLocation.getY());
        line3 = new Line(GameAreaLocation.getX(),GameAreaLocation.getY()+GAME_AREA_HEIGHT,GameAreaLocation.getX()+GAME_AREA_WIDTH,GameAreaLocation.getY()+GAME_AREA_HEIGHT);
        line4 = new Line(GameAreaLocation.getX()+GAME_AREA_WIDTH,GameAreaLocation.getY(),GameAreaLocation.getX()+GAME_AREA_WIDTH,GameAreaLocation.getY()+GAME_AREA_HEIGHT);

        //TextBox.setTranslateX(300);TextBox.setTranslateX(759);

        root.getChildren().addAll(player,rec1,rec2,rec3,rec4,line1,line2,line3,line4);
        rec1.setTranslateZ(0);

        return root;
    }

    void checkPlayerOnBound(){
        double translateX = player.getTranslateX();
        double translateY = player.getTranslateY();
        if(GameAreaLocation.getY()>=translateY){
            player.setTranslateY(GameAreaLocation.getY()+1);
        }
        else if( (GameAreaLocation.getY()+GAME_AREA_HEIGHT-player.getHeight())<translateY){
            player.setTranslateY(GameAreaLocation.getY()+GAME_AREA_HEIGHT-player.getHeight()-1);
        }
        if(GameAreaLocation.getX()>=translateX){
            player.setTranslateX(GameAreaLocation.getX()+1);
        }
        else if( (GameAreaLocation.getX()+GAME_AREA_WIDTH-player.getWidth())<translateX){
            player.setTranslateX(GameAreaLocation.getX()+GAME_AREA_WIDTH-player.getWidth()-1);
        }
    }
    private void update(float DeltaTime){
        if(player.getBoundsInParent().intersects(rec1.getBoundsInParent())){
            player.setFill(Color.RED);
        }
        else {
            player.setFill(Color.BLUE);
        }
        checkPlayerOnBound();
        rec1.move(DeltaTime);
        rec2.move(DeltaTime);
        rec3.move(DeltaTime);
        rec4.move(DeltaTime);
    }
    @Override
    public void start(Stage primaryStage) throws Exception{
        /*
        // Create MenuBar
        MenuBar menuBar = new MenuBar();

        // Create menus
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        Menu projectMenu = new Menu("Project");
        Menu helpMenu = new Menu("Help");

        // Create MenuItems
        MenuItem newItem = new MenuItem("New");


        MenuItem openFileItem = new MenuItem("Open File");
        // SeparatorMenuItem.
        SeparatorMenuItem separator= new SeparatorMenuItem();
        MenuItem exitItem = new MenuItem("Exit");

        MenuItem copyItem = new MenuItem("Copy");
        MenuItem pasteItem = new MenuItem("Paste");

        // CheckMenuItem
        CheckMenuItem buildItem = new CheckMenuItem("Build Automatically");
        buildItem.setSelected(true);

        // RadioMenuItem
        RadioMenuItem updateItem1 = new RadioMenuItem("Auto Update");
        RadioMenuItem updateItem2 = new RadioMenuItem("Ask for Update");

        ToggleGroup group = new ToggleGroup();
        updateItem1.setToggleGroup(group);
        updateItem2.setToggleGroup(group);
        updateItem1.setSelected(true);



        // Add menuItems to the Menus
        fileMenu.getItems().addAll(newItem, openFileItem,separator, exitItem);
        editMenu.getItems().addAll(copyItem, pasteItem);
        projectMenu.getItems().add(buildItem);
        helpMenu.getItems().addAll(updateItem1,updateItem2);

        // Add Menus to the MenuBar
        menuBar.getMenus().addAll(fileMenu, editMenu, projectMenu, helpMenu);


        //editMenu.getItems().addAll(copyItem, pasteItem);
        //projectMenu.getItems().add(buildItem);
        //helpMenu.getItems().addAll(updateItem1,updateItem2);

        // Add Menus to the MenuBar
        //menuBar.getMenus().addAll(fileMenu, editMenu, projectMenu, helpMenu);
*/

        player.setOnMouseDragged(PlayerEvent);

        //root.setTop(menuBar);
        //Button restart_startButton = new Button("Restart/Start");

        //restart_startButton.setLayoutY(30);
        //restart_startButton.setTranslateX(100);
        //restart_startButton.setTranslateY(100);
        //root.getChildren().addAll(restart_startButton);

        AnimationTimer timer=new AnimationTimer() {
            private  long pastTick=System.nanoTime();;

            @Override
            public void handle(long now) {
                float delta = ( float ) ( ( float )( now - pastTick ) / 1e9 ) ;
                pastTick = now;
                update(delta);
            }

        };

        Scene scene = new Scene(createContent());
        timer.start();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static class Sprite extends javafx.scene.shape.Rectangle{
        boolean dead=false;
        final String type;
        Sprite(int x,int y, int w, int h, String type, javafx.scene.paint.Color color){
            super(w,h,color);
            this.type=type;

           setTranslateX(x);
           setTranslateY(y);
        }
        void moveLeft(){
            setTranslateX(getTranslateX()-5);
        }
        void moveRight(){
            setTranslateX(getTranslateX()+5);
        }
        void moveUp(){
            setTranslateY(getTranslateX()-5);
        }
        void moveDown(){
            setTranslateY(getTranslateX()+5);
        }
    }

    private class Enemy extends Rectangle{
        Point2D SpeedVector;
        Double SpeedValue;
        Enemy(Double SpeedVecX, Double SpeedVecY,Double Speed,int x,int y, int w, int h, Color color){
            super(w,h,color);
            setTranslateX(x);
            setTranslateY(y);
            SpeedVector=new Point2D(SpeedVecX,SpeedVecY);
            normalize();
            SpeedValue=Speed;
        }

        void checkOnBound(){
            double translateX = getTranslateX();
            double translateY = getTranslateY();

            Double _SpeedVecX=SpeedVector.getX(), _SpeedVecY=SpeedVector.getY();
            boolean SpeedVecChanged=false;
            if(GameAreaLocation.getY()>=translateY){
                setTranslateY(GameAreaLocation.getY()+1);
                _SpeedVecY= (double) ThreadLocalRandom.current().nextInt(0, 100 + 1);
                _SpeedVecX=(double) ThreadLocalRandom.current().nextInt(-100, 100 + 1);
                SpeedVecChanged=true;
            }
            else if( (GameAreaLocation.getY()+GAME_AREA_HEIGHT-getHeight())<translateY){
                setTranslateY(GameAreaLocation.getY()+GAME_AREA_HEIGHT-getHeight()-1);
                _SpeedVecY= (double) ThreadLocalRandom.current().nextInt(-100, 0 + 1);
                _SpeedVecX=(double) ThreadLocalRandom.current().nextInt(-100, 100 + 1);
                SpeedVecChanged=true;
            }
            if(GameAreaLocation.getX()>=translateX){
                setTranslateX(GameAreaLocation.getX()+1);
                _SpeedVecX= (double) ThreadLocalRandom.current().nextInt(0, 100 + 1);
                _SpeedVecY=(double) ThreadLocalRandom.current().nextInt(-100, 100 + 1);
                SpeedVecChanged=true;
            }
            else if( (GameAreaLocation.getX()+GAME_AREA_WIDTH-getWidth())<translateX){
                setTranslateX(GameAreaLocation.getX()+GAME_AREA_WIDTH-getWidth()-1);
                _SpeedVecX= (double) ThreadLocalRandom.current().nextInt(-100, 0 + 1);
                _SpeedVecY=(double) ThreadLocalRandom.current().nextInt(-100, 100 + 1);
                SpeedVecChanged=true;
            }
            if(SpeedVecChanged){
                SpeedVector=new Point2D(_SpeedVecX,_SpeedVecY);
                normalize();
            }
        }
        double length(){
            return Math.sqrt(SpeedVector.getX()*SpeedVector.getX()+SpeedVector.getY()*SpeedVector.getY());
        }
        void normalize(){
            double l=length();
            SpeedVector=new Point2D(SpeedVector.getX()/l,SpeedVector.getY()/l);
        }
        void move(float DeltaTime){
            setTranslateX(getTranslateX()+SpeedVector.getX()*SpeedValue*DeltaTime);
            setTranslateY(getTranslateY()+SpeedVector.getY()*SpeedValue*DeltaTime);
            checkOnBound();
        }
    }
    private class IntersectionScore extends TextField{
        IntersectionScore(Double x,Double y,Integer score){
            super();
            setTranslateX(x); setTranslateY(y);
            setScore(score);
        }
        void increment(){
            setScore(getScore()+1);
        }
        void setScore(int Score){
            setText(Integer.toString(Score));
        }
        int getScore(){
            return Integer.parseInt(getText());
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
