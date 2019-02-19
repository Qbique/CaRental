package carental;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class CarentalApplication extends Application {

    private Parent rootNode;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void init() {
        ConfigurableApplicationContext context = SpringApplication.run(CarentalApplication.class);

        //loading of FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main-dashboard.fxml"));

        //integration spring with JavaFX by passing spring context to FXML Loader
        loader.setControllerFactory(context::getBean);
        try {
            rootNode = loader.load();
        } catch (IllegalStateException | IOException e) {
            System.out.println("xml file is not present");
        }
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("CaRental");
        primaryStage.setScene(new Scene(rootNode));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}