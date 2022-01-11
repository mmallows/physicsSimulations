import javafx.animation.*;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;
import java.lang.Math;
import java.util.Random;
import javafx.scene.control.*;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class TwoDProjectilePlane extends Pane {
   
   private Random rand = new Random();
   
   //TextFields:
   private TextField tfV0 = new TextField("Initial Velocity (m/s)");
   private TextField tfH0 = new TextField("Initial Height (m)");
   private TextField tfThetaD = new TextField("Angle (deg.)");
   private TextField tfThetaR = new TextField("Angle (rads)");
   
   private HBox timeStuff = new HBox(10);
   private Button btHeight = new Button("Height at: ");
   private TextField tfTime = new TextField("Time (s)");
   private TextArea tHeight = new TextArea("height at given time:");
   
   private TextField tfG = new TextField("-9.80665 m/s/s");
   private Button btDraw = new Button("Draw");
   private Button btClear = new Button("Clear");
   
   private VBox vBox = new VBox(15);
  
   //Initial values of velocity & position:
   private double g = -9.80665;
   private double v0 = 0;
   private double h0 = 0;
   private double thetaD = 0;
   private double thetaR = Math.toRadians(thetaD);
   
   private double v0y = v0 * Math.sin(thetaR);
   private double v0x = v0 * Math.cos(thetaR);
   
   //Universal values that vary:
   private double t = 0;
   private double h = 0;
   private double v = 0;
   
   private double highZ;
   private double lowZ;
   
   //Visual objects:
   private double startX = 500/6;
   private double startY = 5 * 500 / 6;
   private Circle cannonBall = new Circle(startX, startY, 7);
   
   private HBox hBox = new HBox(20);
   private Pane graph = new Pane();
   
   
   //Line Chart:
   private NumberAxis xAxis = new NumberAxis();
   private NumberAxis yAxis = new NumberAxis();
   //creating the chart
   private LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);
           
   //defining a series
   XYChart.Series series = new XYChart.Series();
   
   //series.getData().add(new XYChart.Data(1, 23));
   
   //Constructor:
   public TwoDProjectilePlane(){
      setPrefSize(500, 500);
      tHeight.setPrefRowCount(3);
      int r = rand.nextInt(256);
      int b = rand.nextInt(256);
      int g = rand.nextInt(256);
      Color color = Color.rgb(r, g, b);
      cannonBall.setFill(color);
      timeStuff.getChildren().addAll(btHeight, tfTime, tHeight);
      vBox.getChildren().addAll(tfV0, tfH0, tfThetaD, tfThetaR, timeStuff, tfG, btDraw, btClear);
      graph.getChildren().addAll(lineChart);
      lineChart.getData().add(series);
      hBox.getChildren().addAll(graph, vBox);
      getChildren().addAll(hBox);
      
      //Set On Action:
      btDraw.setOnAction(e -> {
         draw();
      });
      btClear.setOnAction(e -> {
         clear();
      });
      btHeight.setOnAction(e -> {
         String str = getH(Double.parseDouble(tfTime.getText())) + " meters\nPeak: " + getPeak() + "\nHits ground: t = " + highZ + " ; " + (v0x * highZ) + " meters from start";
         tHeight.setText(str);
      });
      
   }
   protected void updateCannonBall(){
      cannonBall.setCenterX(500/6);
      cannonBall.setCenterY(500/6);
   }
      
   //Functions:
   //Return height at given time:
   public double getH(double time){
      return (((g / 2) * time * time) + (v0y * time) + h0);
   }
   //Return inst. vertical velocity at given time:
   public double getVy(double time){
      return ((g * time) + v0y);
   }
   //Return est. half of total time:
   public double getPeak(){
      return -(v0y) / g;
   }
   
   public void draw(){
      setG();
      setThetaR();
      setThetaD();
      setH0();
      setV0();
      getZeroes();
      
      
      double interval = highZ - lowZ;
      interval = interval / 400;
      for(int i = 0; i <= 400; i++){
         double x = lowZ + (i*interval);
         series.getData().add(new XYChart.Data(x, getH(x)));
      }
   }
   
   public void clear(){
      series.getData().clear();
      //draw();
   }
   
   public void setV0(){
      if (tfV0.getText() == ""){
         v0 = 0;
      }
      else{
         try{
            v0 = Double.parseDouble(tfV0.getText());
            v0x = v0 * Math.cos(thetaR);
            v0y = v0 * Math.sin(thetaR);
         } catch (Exception ex){
            System.out.println("Invalid input: v0");
         }
      }
   }
   
   public void setH0(){
      if (tfH0.getText() == ""){
         h0 = 0;
      }
      else{
         try{
            h0 = Double.parseDouble(tfH0.getText());
         } catch (Exception ex){
            System.out.println("Invalid input: h0\nPlease enter number");
         }
      }
   }
   
   public void setThetaD(){
      if (tfThetaD.getText() == ""){
         //Do nothing since setThetaR() will be called anyway
      }
      else{
         try{
            thetaD = Double.parseDouble(tfThetaD.getText());
            thetaR = Math.toRadians(thetaD);
            tfThetaR.setText(thetaR + "");
         } catch (Exception ex){
            System.out.println("Invalid input: ThetaD\nPlease enter number");
         }
      }
   }
   
   public void setThetaR(){
      if (tfThetaD.getText() == ""){
         if(tfThetaR.getText() == ""){
            thetaD = 0;
            thetaR = 0;
         }else{
            setThetaD();
         }
      }
      else{
         try{
            thetaR = Double.parseDouble(tfThetaR.getText());
            thetaD = Math.toDegrees(thetaR);
            tfThetaD.setText(thetaD + "");
         } catch (Exception ex){
            System.out.println("Invalid input: ThetaR\nPlease enter number");
         }
      }
   }
   
   public void setG(){
      if (tfG.getText() == ""){
         g = -9.80665;
      }
      else{
         try{
            g = Double.parseDouble(tfG.getText());
         } catch (Exception ex){
            System.out.println("Invalid input: g\nPlease enter number");
         }
      }
   }
   
   public void getZeroes(){
      double a = g/2;
      double b = v0y;
      double c = h0;
      highZ = (-b - Math.sqrt((b*b) - (4*a*c))) / (2 * a);
      lowZ = (-b + Math.sqrt((b*b) - (4*a*c))) / (2 * a);
   }
   
}