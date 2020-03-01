package sample;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.concurrent.ThreadLocalRandom;


public class Controller {
    @FXML
    private Button START_STOP_BTN;
    @FXML
    private Rectangle PLAYER;
    @FXML
    private Rectangle ENEMY1;
    private Enemy En1;
    @FXML
    private Rectangle ENEMY2;
    private Enemy En2;
    @FXML
    private Rectangle ENEMY3;
    private Enemy En3;
    @FXML
    private Rectangle ENEMY4;
    private Enemy En4;
    @FXML
    private Line UP_LINE;
    @FXML
    private Line LEFT_LINE;
    @FXML
    private Line RIGHT_LINE;
    @FXML
    private Line DOWN_LINE;

    @FXML
    private Text TIMER;
    private FloatProperty timeSeconds=new SimpleFloatProperty(0.f);
    private double GAME_AREA_WIDTH, GAME_AREA_HEIGHT;
    private Point2D GameAreaLocation;

    private boolean isPaused=false;
    private Timeline GameLoop;

    private EventHandler<MouseEvent> PlayerEvent= new EventHandler<MouseEvent>()
    {
        @Override
        public void handle(MouseEvent t) {
            PLAYER.setFill(Color.RED);
            PLAYER.setTranslateX(t.getSceneX()-PLAYER.getWidth()/2);
            PLAYER.setTranslateY(t.getSceneY()-PLAYER.getHeight()/2);
        }
    };
    private EventHandler<MouseEvent> PlayerPause= new EventHandler<MouseEvent>()
    {
        @Override
        public void handle(MouseEvent t) {

        }
    };
    @FXML
    private void CHANGE_PAUSE(ActionEvent event) {
        isPaused=!isPaused;
        if(isPaused)
        {
            PLAYER.setOnMouseDragged(PlayerPause);
        }
        else{
            PLAYER.setOnMouseDragged(PlayerEvent);
        }
    }

    @FXML
    public void initialize() {
        GAME_AREA_WIDTH=Math.abs(UP_LINE.getBoundsInParent().getMaxX()-UP_LINE.getBoundsInParent().getMinX());
        GAME_AREA_HEIGHT=Math.abs(RIGHT_LINE.getBoundsInParent().getMaxY()-RIGHT_LINE.getBoundsInParent().getMinY());
        GameAreaLocation=new Point2D(LEFT_LINE.getBoundsInParent().getMinX(),LEFT_LINE.getBoundsInParent().getMinY());
        PLAYER.setOnMouseDragged(PlayerEvent);
        En1=new Enemy(20.0,20.0,100.0,ENEMY1);
        En2=new Enemy(30.0,20.0,100.0,ENEMY2);
        En3=new Enemy(70.0,20.0,100.0,ENEMY3);
        En4=new Enemy(20.0,-20.0,100.0,ENEMY4);

        GameLoop = new Timeline();
        GameLoop.setCycleCount(Timeline.INDEFINITE);
        GameLoop.getKeyFrames().add(new KeyFrame(Duration.seconds(0.0001), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(!isPaused){Loop(0.0001f);}
            }
        }));
        GameLoop.playFromStart();

        TIMER.textProperty().bind(timeSeconds.asString());
    }

    private class Enemy{
        Rectangle EnemyRec;
        Point2D SpeedVector;
        Double SpeedValue;
        Enemy(Double SpeedVecX, Double SpeedVecY,Double Speed, Rectangle EnemyRectangle){
            EnemyRec=EnemyRectangle;
            SpeedVector=new Point2D(SpeedVecX,SpeedVecY);
            normalize();
            SpeedValue=Speed;
        }

        void checkOnBound(){
            double translateX =  EnemyRec.getTranslateX();
            double translateY = EnemyRec.getTranslateY();

            Double _SpeedVecX=SpeedVector.getX(), _SpeedVecY=SpeedVector.getY();
            boolean SpeedVecChanged=false;
            if(GameAreaLocation.getY()>=translateY){
                EnemyRec.setTranslateY(GameAreaLocation.getY()+1);
                _SpeedVecY= (double) ThreadLocalRandom.current().nextInt(0, 100 + 1);
                _SpeedVecX=(double) ThreadLocalRandom.current().nextInt(-100, 100 + 1);
                SpeedVecChanged=true;
            }
            else if( (GameAreaLocation.getY()+GAME_AREA_HEIGHT-EnemyRec.getHeight())<translateY){
                EnemyRec.setTranslateY(GameAreaLocation.getY()+GAME_AREA_HEIGHT-EnemyRec.getHeight()-1);
                _SpeedVecY= (double) ThreadLocalRandom.current().nextInt(-100, 0 + 1);
                _SpeedVecX=(double) ThreadLocalRandom.current().nextInt(-100, 100 + 1);
                SpeedVecChanged=true;
            }
            if(GameAreaLocation.getX()>=translateX){
                EnemyRec.setTranslateX(GameAreaLocation.getX()+1);
                _SpeedVecX= (double) ThreadLocalRandom.current().nextInt(0, 100 + 1);
                _SpeedVecY=(double) ThreadLocalRandom.current().nextInt(-100, 100 + 1);
                SpeedVecChanged=true;
            }
            else if( (GameAreaLocation.getX()+GAME_AREA_WIDTH-EnemyRec.getWidth())<translateX){
                EnemyRec.setTranslateX(GameAreaLocation.getX()+GAME_AREA_WIDTH-EnemyRec.getWidth()-1);
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
            EnemyRec.setTranslateX(EnemyRec.getTranslateX()+SpeedVector.getX()*SpeedValue*DeltaTime);
            EnemyRec.setTranslateY(EnemyRec.getTranslateY()+SpeedVector.getY()*SpeedValue*DeltaTime);
            checkOnBound();
        }
    }
    void checkPlayerOnBound(){
        double translateX = PLAYER.getTranslateX();
        double translateY = PLAYER.getTranslateY();
        if(GameAreaLocation.getY()>=translateY){
            PLAYER.setTranslateY(GameAreaLocation.getY()+1);
        }
        else if( (GameAreaLocation.getY()+GAME_AREA_HEIGHT-PLAYER.getHeight())<translateY){
            PLAYER.setTranslateY(GameAreaLocation.getY()+GAME_AREA_HEIGHT-PLAYER.getHeight()-1);
        }
        if(GameAreaLocation.getX()>=translateX){
            PLAYER.setTranslateX(GameAreaLocation.getX()+1);
        }
        else if( (GameAreaLocation.getX()+GAME_AREA_WIDTH-PLAYER.getWidth())<translateX){
            PLAYER.setTranslateX(GameAreaLocation.getX()+GAME_AREA_WIDTH-PLAYER.getWidth()-1);
        }
    }
    void Loop(float DeltaSeconds){
        checkPlayerOnBound();
        PLAYER.setFill(Color.color(Math.random(), Math.random(), Math.random()));
        En1.move(DeltaSeconds);
        En2.move(DeltaSeconds);
        En3.move(DeltaSeconds);
        En4.move(DeltaSeconds);
        timeSeconds.set(timeSeconds.get()+DeltaSeconds);
    }
}
