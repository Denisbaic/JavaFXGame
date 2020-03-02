package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
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
    private CheckBox ENEMY1_ENABLE;
    @FXML
    private Rectangle ENEMY2;
    private Enemy En2;
    @FXML
    private CheckBox ENEMY2_ENABLE;
    @FXML
    private Rectangle ENEMY3;
    private Enemy En3;
    @FXML
    private CheckBox ENEMY3_ENABLE;
    @FXML
    private Rectangle ENEMY4;
    private Enemy En4;
    @FXML
    private CheckBox ENEMY4_ENABLE;
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
    @FXML
    private Text INTERSECTIONS;
    private IntegerProperty intersectionCount=new SimpleIntegerProperty(0);

    @FXML
    private CheckBox CONST_SPEED;
    @FXML
    private Text SPEED_TEXT;
    @FXML
    private Slider SPEED_VALUE;

    private double GAME_AREA_WIDTH, GAME_AREA_HEIGHT;
    private Point2D GameAreaLocation;

    private boolean isPaused=true;
    private boolean isPlayerIntersect=false;
    private Timeline GameLoop;

    private EventHandler<MouseEvent> PlayerEvent= new EventHandler<MouseEvent>()
    {
        @Override
        public void handle(MouseEvent t) {
            PLAYER.setTranslateX(t.getSceneX()-PLAYER.getWidth()/2);
            PLAYER.setTranslateY(t.getSceneY()-PLAYER.getHeight()/2);
        }
    };
    private EventHandler<MouseEvent> PlayerPause= new EventHandler<MouseEvent>()
    {
        @Override
        public void handle(MouseEvent t) {}
    };
    @FXML
    private void CHANGE_PAUSE(ActionEvent event) {
        if(ENEMY1_ENABLE.isSelected() || ENEMY2_ENABLE.isSelected() ||ENEMY3_ENABLE.isSelected() ||ENEMY4_ENABLE.isSelected()){
            isPaused=!isPaused;
            if(isPaused)
            {
                PLAYER.setOnMouseDragged(PlayerPause);
            }
            else{
                intersectionCount.set(0);
                timeSeconds.set(0.f);
                PLAYER.setOnMouseDragged(PlayerEvent);
            }
        }
    }
    @FXML
    private void SELECTED1(ActionEvent event){
        En1.setEnabled(ENEMY1_ENABLE.isSelected());
    }
    @FXML
    private void SELECTED2(ActionEvent event){
        En2.setEnabled(ENEMY2_ENABLE.isSelected());
    }
    @FXML
    private void SELECTED3(ActionEvent event){
        En3.setEnabled(ENEMY3_ENABLE.isSelected());
    }
    @FXML
    private void SELECTED4(ActionEvent event){
        En4.setEnabled(ENEMY4_ENABLE.isSelected());
    }
    @FXML
    public void initialize() {
        GAME_AREA_WIDTH=Math.abs(UP_LINE.getBoundsInParent().getMaxX()-UP_LINE.getBoundsInParent().getMinX());
        GAME_AREA_HEIGHT=Math.abs(RIGHT_LINE.getBoundsInParent().getMaxY()-RIGHT_LINE.getBoundsInParent().getMinY());
        GameAreaLocation=new Point2D(LEFT_LINE.getBoundsInParent().getMinX(),LEFT_LINE.getBoundsInParent().getMinY());
        PLAYER.setOnMouseDragged(PlayerPause);
        En1=new Enemy(20.0,20.0,100.0, ENEMY1, ENEMY1.getBoundsInParent().getMinX(), ENEMY1.getBoundsInParent().getMinY(),false);
        En2=new Enemy(30.0,20.0,100.0, ENEMY2, ENEMY2.getBoundsInParent().getMinX(), ENEMY2.getBoundsInParent().getMinY(),false);
        En3=new Enemy(70.0,20.0,100.0, ENEMY3, ENEMY3.getBoundsInParent().getMinX(), ENEMY3.getBoundsInParent().getMinY(),false);
        En4=new Enemy(20.0,-20.0,100.0,ENEMY4, ENEMY4.getBoundsInParent().getMinX(), ENEMY4.getBoundsInParent().getMinY(),false);
        SPEED_TEXT.textProperty().bind(Bindings.format("%.2f",SPEED_VALUE.valueProperty()));

        GameLoop = new Timeline();
        GameLoop.setCycleCount(Timeline.INDEFINITE);
        GameLoop.getKeyFrames().add(new KeyFrame(Duration.seconds(0.0001), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                CheckBoxRules();
                if(!isPaused){Loop(0.0001f);}
            }
        }));
        GameLoop.playFromStart();

        TIMER.textProperty().bind(timeSeconds.asString());
        INTERSECTIONS.textProperty().bind(intersectionCount.asString());
    }
    void CheckBoxRules(){
        if(ENEMY1_ENABLE.isSelected())
        {
            if(!(ENEMY2_ENABLE.isSelected() || ENEMY3_ENABLE.isSelected() || ENEMY4_ENABLE.isSelected())){
                ENEMY1_ENABLE.setDisable(true);
            }
            else
                ENEMY1_ENABLE.setDisable(false);
        }
        if(ENEMY2_ENABLE.isSelected())
        {
            if(!(ENEMY1_ENABLE.isSelected() || ENEMY3_ENABLE.isSelected() || ENEMY4_ENABLE.isSelected())){
                ENEMY2_ENABLE.setDisable(true);
            }
            else
                ENEMY2_ENABLE.setDisable(false);
        }
        if(ENEMY3_ENABLE.isSelected())
        {
            if(!(ENEMY1_ENABLE.isSelected() || ENEMY2_ENABLE.isSelected() || ENEMY4_ENABLE.isSelected())){
                ENEMY3_ENABLE.setDisable(true);
            }
            else
                ENEMY3_ENABLE.setDisable(false);
        }
        if(ENEMY4_ENABLE.isSelected())
        {
            if(!(ENEMY1_ENABLE.isSelected() || ENEMY2_ENABLE.isSelected() || ENEMY3_ENABLE.isSelected())){
                ENEMY4_ENABLE.setDisable(true);
            }
            else
                ENEMY4_ENABLE.setDisable(false);
        }
    }
    void increaseSpeed(double Add){
        SPEED_VALUE.setValue(SPEED_VALUE.getValue()+Add);
    }
    void setSpeedForEnemy(double Speed){
        En1.SpeedValue=Speed;
        En2.SpeedValue=Speed;
        En3.SpeedValue=Speed;
        En4.SpeedValue=Speed;
    }
    private class Enemy{
        Rectangle EnemyRec;
        Point2D SpeedVector;
        Double SpeedValue;
        boolean isEnabled;
        Point2D DefaultLocation;
        Enemy(Double SpeedVecX, Double SpeedVecY,Double Speed, Rectangle EnemyRectangle,Double DefaultLocX,Double DefaultLocY, boolean isEnable){
            EnemyRec=EnemyRectangle;
            SpeedVector=new Point2D(SpeedVecX,SpeedVecY);
            DefaultLocation=new Point2D(DefaultLocX,DefaultLocY);
            normalize();
            SpeedValue=Speed;
            isEnabled=isEnable;
        }

        void setEnabled(boolean isEnable){
            if(isEnable){
                do{
                    Double XCoord=ThreadLocalRandom.current().nextDouble(GameAreaLocation.getX(), GameAreaLocation.getX()+GAME_AREA_WIDTH + 1);
                    Double YCoord=ThreadLocalRandom.current().nextDouble(GameAreaLocation.getY(), GameAreaLocation.getY()+GAME_AREA_HEIGHT + 1);
                    EnemyRec.setTranslateY(YCoord);
                    EnemyRec.setTranslateX(XCoord);
                }while(EnemyRec.getBoundsInParent().intersects(PLAYER.getBoundsInParent()));
                SpeedVector=new Point2D(ThreadLocalRandom.current().nextDouble(-100, 100 + 1),ThreadLocalRandom.current().nextDouble(-100,100 + 1));
                normalize();
                checkOnBound();
            }
            else
            {
                EnemyRec.setTranslateY(DefaultLocation.getY());
                EnemyRec.setTranslateX(DefaultLocation.getX());
            }
            isEnabled=isEnable;
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
            else if((GameAreaLocation.getY()+GAME_AREA_HEIGHT-EnemyRec.getHeight())<translateY){
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
            if(isEnabled){
                EnemyRec.setTranslateX(EnemyRec.getTranslateX()+SpeedVector.getX()*SpeedValue*DeltaTime);
                EnemyRec.setTranslateY(EnemyRec.getTranslateY()+SpeedVector.getY()*SpeedValue*DeltaTime);
                checkOnBound();
            }
        }
    }
    void Restart(){
        En1.setEnabled(true);
        En2.setEnabled(true);
        En3.setEnabled(true);
        En4.setEnabled(true);
    }
    void checkPlayerOnBound(){
        double translateX = PLAYER.getTranslateX();
        double translateY = PLAYER.getTranslateY();
        if(GameAreaLocation.getY()>=translateY){
            PLAYER.setTranslateY(GameAreaLocation.getY()+1);
        }
        else if( (GameAreaLocation.getY()+GAME_AREA_HEIGHT-PLAYER.getHeight())<translateY){
            PLAYER.setTranslateY(GameAreaLocation.getY()+GAME_AREA_HEIGHT-PLAYER.getHeight());
        }
        if(GameAreaLocation.getX()>=translateX){
            PLAYER.setTranslateX(GameAreaLocation.getX()+1);
        }
        else if( (GameAreaLocation.getX()+GAME_AREA_WIDTH-PLAYER.getWidth())<translateX){
            PLAYER.setTranslateX(GameAreaLocation.getX()+GAME_AREA_WIDTH-PLAYER.getWidth());
        }
    }
    boolean checkPlayerOnIntersection(){
        Bounds PlayerBounds=PLAYER.getBoundsInParent();
        Bounds En1Bounds=ENEMY1.getBoundsInParent();
        Bounds En2Bounds=ENEMY2.getBoundsInParent();
        Bounds En3Bounds=ENEMY3.getBoundsInParent();
        Bounds En4Bounds=ENEMY4.getBoundsInParent();
        if((PlayerBounds.intersects(En1Bounds) || PlayerBounds.intersects(En2Bounds) || PlayerBounds.intersects(En3Bounds) || PlayerBounds.intersects(En4Bounds))){
            if(!isPlayerIntersect)
            {
                intersectionCount.set(intersectionCount.get()+1);
                isPlayerIntersect=true;
            }
        }
        else{
            isPlayerIntersect=false;
        }
        return isPlayerIntersect;
    }
    void Loop(float DeltaSeconds){
        checkPlayerOnBound();
        En1.move(DeltaSeconds);
        En2.move(DeltaSeconds);
        En3.move(DeltaSeconds);
        En4.move(DeltaSeconds);

        if(checkPlayerOnIntersection()){
            En1.isEnabled=false;
            En2.isEnabled=false;
            En3.isEnabled=false;
            En4.isEnabled=false;
        }
        else {
            if(!CONST_SPEED.isSelected()){
                increaseSpeed(DeltaSeconds);
            }
            setSpeedForEnemy(Double.parseDouble(SPEED_TEXT.getText().replace(',','.')));
            En1.isEnabled=ENEMY1_ENABLE.isSelected();
            En2.isEnabled=ENEMY2_ENABLE.isSelected();
            En3.isEnabled=ENEMY3_ENABLE.isSelected();
            En4.isEnabled=ENEMY4_ENABLE.isSelected();
            timeSeconds.set(timeSeconds.get()+DeltaSeconds);
        }
    }
}