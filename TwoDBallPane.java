import javafx.animation.*;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import java.util.Random;

public class TwoDBallPane extends Pane {

   Random rand = new Random();
   
   private double[] dx = new double[3];
   private double[] dy = new double[3];
   
   private TextField tfMomentum = new TextField();
   private Circle[] circles = new Circle[3];
   
   private double momentum;
   
   private Timeline animation;
   
   public TwoDBallPane(){
   
      for (int i = 0; i < 3; i++){
         double radius = rand.nextInt(10)+10;
         double x = (450 / 4)*(i+1);
         double y = (450 / 4)*(i+1);
         circles[i] = new Circle(x, y, radius);
         int r = rand.nextInt(256);
         int b = rand.nextInt(256);
         int g = rand.nextInt(256);
         Color color = Color.rgb(r, g, b);
         circles[i].setFill(color);
         dx[i] = ((int)(rand.nextInt(2)-1))+1 * rand.nextDouble()*15;
         dy[i] = ((int)(rand.nextInt(2)-1))+1 * rand.nextDouble()*15;
      }
      
      for (int i = 0; i < 3; i++){
         getChildren().addAll(circles[i]);
      }
      getChildren().add(tfMomentum);
      animation = new Timeline(
         new KeyFrame(Duration.millis(50), e-> moveBalls()));
      animation.setCycleCount(Timeline.INDEFINITE);
      animation.play();
   }
   
   private void setMomentum(){
      double momentum = 0;
      for (int i = 0; i < 3; i++){
         momentum += circles[i].getRadius() * Math.sqrt((dx[i] * dx[i]) + (dy[i] * dy[i]));
      }
      tfMomentum.setText("" + momentum);
   }
   
   public void play(){
      animation.play();
   }
   
   public void pause(){
      animation.pause();
   }
   
   public DoubleProperty rateProperty(){
      return animation.rateProperty();
   }
   
   protected void moveBalls(){
      for (int i = 0; i < 3; i++){
         //Check edge collision:
         if (circles[i].getCenterX() <= circles[i].getRadius() || circles[i].getCenterX() >= getWidth() - circles[i].getRadius()){
            dx[i] *= -1;
         }
         if (circles[i].getCenterY() <= circles[i].getRadius() || circles[i].getCenterY() >= getHeight() - circles[i].getRadius()){
            dy[i] *= -1;
         }
         //Check object collision:
         for (int j = 0; j < 3; j++){
            if (i != j){
               double radius1 = circles[i].getRadius();
               double radius2 = circles[j].getRadius();
               if ((Math.abs(circles[j].getCenterX()-circles[i].getCenterX()) <= (radius1 + radius2)) && (Math.abs(circles[j].getCenterY()-circles[i].getCenterY()) <= radius1 + radius2)){
                  double oldDx1 = dx[i];
                  double oldDx2 = dx[j];
                  double oldDy1 = dy[i];
                  double oldDy2 = dy[j];
                  
                  dx[j] = ((2*radius1*oldDx1) + (radius2 * oldDx2) - (radius1 * oldDx2)) / (radius1 + radius2);
                  dx[i] = oldDx2 + dx[j] - oldDx1; 
                  dy[j] = ((2*radius1*oldDy1) + (radius2 * oldDy2) - (radius1 * oldDy2)) / (radius1 + radius2);
                  dy[i] = oldDy2 + dy[j] - oldDy1;
              
               }
            }  
         }
         circles[i].setCenterX(circles[i].getCenterX() + dx[i]);
         circles[i].setCenterY(circles[i].getCenterY() + dy[i]);
         setMomentum();
      }
      
   }
   
}