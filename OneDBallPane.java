import javafx.animation.*;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import java.util.Random;

public class OneDBallPane extends Pane {
   Random rand = new Random();
   
   public double[] dx = new double[2];
   
   public Circle[] circles = new Circle[2];
   
   private Timeline animation;
   
   public OneDBallPane(){
      setPrefSize(450, 450);
      for (int i = 0; i < 2; i++){
         double radius = rand.nextInt(10)+10;
         double x = (450 / 3)*(i+1);
         double y = 450/2;
         circles[i] = new Circle(x, y, radius);
         int r = rand.nextInt(256);
         int b = rand.nextInt(256);
         int g = rand.nextInt(256);
         Color color = Color.rgb(r, g, b);
         circles[i].setFill(color);
         dx[i] = ((int)(rand.nextInt(2)-1))+1 * rand.nextDouble()*15;
      }
      
      for (int i = 0; i < 2; i++){
         getChildren().addAll(circles[i]);
      }
      animation = new Timeline(
         new KeyFrame(Duration.millis(50), e-> moveBalls()));
      animation.setCycleCount(Timeline.INDEFINITE);
      animation.play();
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
         //Check edge collision:
         if (circles[0].getCenterX() < circles[0].getRadius() || circles[0].getCenterX() > getWidth() - circles[0].getRadius()){
            dx[0] *= -1;
         }
         if (circles[1].getCenterX() < circles[1].getRadius() || circles[1].getCenterX() > getWidth() - circles[1].getRadius()){
            dx[1] *= -1;
         }
         
         //Check object collision:
         double radius1 = circles[0].getRadius();
         double radius2 = circles[1].getRadius();
         if (Math.abs(circles[1].getCenterX()-circles[0].getCenterX()) <= radius1 + radius2){
            double oldDx1 = dx[0];
            double oldDx2 = dx[1];
            
            dx[1] = ((2*radius1*oldDx1) + (radius2 * oldDx2) - (radius1 * oldDx2)) / (radius1 + radius2);
            dx[0] = oldDx2 + dx[1] - oldDx1; 
              
         }
         for(int i = 0; i < 2; i++){
            circles[i].setCenterX(circles[i].getCenterX() + dx[i]);
         } 
   }
}