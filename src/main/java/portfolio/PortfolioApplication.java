package portfolio;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.sql.SQLException;

/**
 *
 * @author Burak
 */
public class PortfolioApplication extends Application {
    private InstrumentDatabase list;


    public void init() throws Exception{
        String databasePath = "jdbc:h2:./todo-database";
        list = new InstrumentDatabase(databasePath);
    }

    public void start(Stage window) throws SQLException {
        AddView addView = new AddView(list);
        AssetView assetView = new AssetView(list);

        BorderPane layout = new BorderPane();



        HBox menu = new HBox();
        Button menuAddButton = new Button("Add Instrument");
        Button menuListButton = new Button("List Instruments");
        menu.getChildren().addAll(menuListButton, menuAddButton);
        menu.setSpacing(10);

        layout.setTop(menu);
        layout.setCenter(assetView.getView());

        layout.setMargin(menu, new Insets(30,12,12,60));

        menuAddButton.setOnMouseClicked((event) -> {
            layout.setCenter(addView.getView());
        });

        menuListButton.setOnMouseClicked((event)  -> {
            try {
                layout.setCenter(assetView.getView());
            } catch (Exception e){

            }
        });

        Scene scene = new Scene(layout,1366,700);

        window.setScene(scene);
        window.show();
    }


    public static void main(String[] args) {
        launch(PortfolioApplication.class);
    }
}