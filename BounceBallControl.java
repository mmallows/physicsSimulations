import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;

public class BounceBallControl extends Application{
   public void start(Stage primaryStage){
      TabPane tPane = new TabPane();
      Tab t2dBallBounce = new Tab("2-D Elastic Collisions");
      Tab t1dBallBounce = new Tab("1-D Elastic Collisions");
      Tab t2dProjectilePlane = new Tab("Projectile Problem");
      tPane.getTabs().addAll(t1dBallBounce, t2dBallBounce, t2dProjectilePlane);
      
      //2-D Elastic Collisions:
      TwoDBallPane twoDballPane = new TwoDBallPane();
      t2dBallBounce.setContent(twoDballPane);
      
      //1-D Elastic Collisoins:
      OneDBallPane oneDballPane = new OneDBallPane();
      t1dBallBounce.setContent(oneDballPane);
      
      //2-D Projectile Problem:
      TwoDProjectilePlane twoDProjectilePlane = new TwoDProjectilePlane();
      t2dProjectilePlane.setContent(twoDProjectilePlane);
      
      //Scene setting:
      Scene scene = new Scene(tPane, 450, 450);
      primaryStage.setTitle("Physics Simulations:");
      primaryStage.setScene(scene);
      primaryStage.show();
   
   }
   
}