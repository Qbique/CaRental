package carental.controller.ui;

import carental.controller.RentalController;
import carental.controller.VehicleController;
import carental.model.Rental;
import carental.model.Vehicle;
import com.jfoenix.controls.JFXDatePicker;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Controller
public class MainDashboardController implements Initializable {

    private final VehicleController vehicleController;
    private final RentalController rentalController;
    @FXML
    private Label errorLabel;
    @FXML
    private JFXDatePicker startDate, endDate;
    @FXML
    private TableView<Vehicle> vehicleTableView;
    @FXML
    private TableColumn<Vehicle, Long> ascendingNumberColumn;
    @FXML
    private TableColumn<Vehicle, Integer> cubismColumn;
    @FXML
    private TableColumn<Vehicle, String> brandColumn, modelColumn;

    public MainDashboardController(VehicleController vehicleController, RentalController rentalController) {
        this.vehicleController = vehicleController;
        this.rentalController = rentalController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ascendingNumberColumn.setCellValueFactory(new PropertyValueFactory<>("A/N"));
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("Brand"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("Model"));
        cubismColumn.setCellValueFactory(new PropertyValueFactory<>("Cubism"));

        ascendingNumberColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Vehicle, Long>, ObservableValue<Long>>() {
            @Override
            public ObservableValue<Long> call(TableColumn.CellDataFeatures<Vehicle, Long> param) {
                return new ReadOnlyObjectWrapper(vehicleTableView.getItems().indexOf(param.getValue()) + 1);
            }
        });

        vehicleTableView
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    System.out.println("\nSelected Vehicle:");
                    System.out.println(newValue);
                });
    }

    public void bookVehicle() {

        Vehicle selectedVehicle = vehicleTableView.getSelectionModel().selectedItemProperty().get();
        if (selectedVehicle == null) {
            createAlert(AlertType.WARNING, "Warning Dialog", "No vehicle is selected", null).showAndWait();
        } else {
            Rental rental = new Rental(selectedVehicle, startDate.getValue(), endDate.getValue());

            Optional<ButtonType> optional = createAlert(AlertType.CONFIRMATION, "Information Dialog", "Confirm", "Are you sure").showAndWait();
            if (optional.get() == ButtonType.OK) {
                this.rentalController.addRental(rental);
                createAlert(AlertType.INFORMATION, "Information Dialog", "Successfully booked", null).showAndWait();
            }
        }
    }

    public void searchVehicleOnDates() {
        LocalDate now = LocalDate.now();
        if (startDate.getValue() == null || endDate.getValue() == null || startDate.getValue().isAfter(endDate.getValue()) || startDate.getValue().isBefore(now)) {
            errorLabel.setVisible(true);
        } else {
            errorLabel.setVisible(false);
            ObservableList<Vehicle> list = FXCollections.observableArrayList(this.vehicleController.getAllAvailable(startDate.getValue(), endDate.getValue()));
            vehicleTableView.setItems(list);

            List<Vehicle> allAvailable = this.vehicleController.getAllAvailable(startDate.getValue(), endDate.getValue());

            System.out.println("\nAll Available Vehicles from [" + startDate.getValue() + "] to [" + endDate.getValue() + "] are:\n");
            allAvailable.forEach(System.out::println);
        }
    }

    private Alert createAlert(AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType, content);
        alert.setTitle(title);
        alert.setHeaderText(header);
        return alert;
    }
}

