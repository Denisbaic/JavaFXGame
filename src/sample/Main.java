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



public class Main extends Application {
    private BorderPane root = new BorderPane();
    private Sprite player=new Sprite(300,759,40,40,"player", Color.BLUE);
    private Sprite rec1=new Sprite(400,700,40,60,"Rec",Color.BLACK);

    private Line line1,line2,line3,line4;
    private int GAME_AREA_WIDTH=500, GAME_AREA_HEIGHT=800;
    private Point2D GameAreaLocation=new Point2D(100,100);
    private Parent createContent(){
        root.setPrefSize(GAME_AREA_WIDTH+GameAreaLocation.getX()+100,GAME_AREA_HEIGHT+GameAreaLocation.getY()+100);

        line1 = new Line(GameAreaLocation.getX(),GameAreaLocation.getY(),GameAreaLocation.getX(),GameAreaLocation.getY()+GAME_AREA_HEIGHT);
        line2 = new Line(GameAreaLocation.getX(),GameAreaLocation.getY(),GameAreaLocation.getX()+GAME_AREA_WIDTH,GameAreaLocation.getY());
        line3 = new Line(GameAreaLocation.getX(),GameAreaLocation.getY()+GAME_AREA_HEIGHT,GameAreaLocation.getX()+GAME_AREA_WIDTH,GameAreaLocation.getY()+GAME_AREA_HEIGHT);
        line4 = new Line(GameAreaLocation.getX()+GAME_AREA_WIDTH,GameAreaLocation.getY(),GameAreaLocation.getX()+GAME_AREA_WIDTH,GameAreaLocation.getY()+GAME_AREA_HEIGHT);

        root.getChildren().addAll(player,rec1,line1,line2,line3,line4);
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
    private void update(){

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

        player.setOnMouseDragged(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent t) {
                player.setFill(Color.RED);
                player.setTranslateX(t.getSceneX()-player.getWidth()/2);
                player.setTranslateY(t.getSceneY()-player.getHeight()/2);
            }
        });

        //root.setTop(menuBar);
        //Button restart_startButton = new Button("Restart/Start");

        //restart_startButton.setLayoutY(30);
        //restart_startButton.setTranslateX(100);
        //restart_startButton.setTranslateY(100);
        //root.getChildren().addAll(restart_startButton);

        AnimationTimer timer=new AnimationTimer() {
            @Override
            public void handle(long l) {
                if(player.getBoundsInParent().intersects(rec1.getBoundsInParent())){
                    player.setFill(Color.RED);
                }
                else {
                    player.setFill(Color.BLUE);
                }
                checkPlayerOnBound();
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

    private static class Enemy extends Rectangle{
        
    }
    public static void main(String[] args) {
        launch(args);
    }
}
