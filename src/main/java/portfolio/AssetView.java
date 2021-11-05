package portfolio;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.sql.SQLException;

public class AssetView {
    private InstrumentDatabase list;

    public AssetView(InstrumentDatabase list){
        this.list = list;
    }

    public Parent getView() throws SQLException{

        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(15,0,15,30));

        VBox tableLayout = new VBox();
        tableLayout.setAlignment(Pos.CENTER);
        tableLayout.setSpacing(10);

        TableView tableView = createTable();
        updateTable(tableView);

        Text totalAssetText = updateTotalAsset(tableView);
        totalAssetText.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 18));


        //creating right panel
        GridPane rightPanel = new GridPane();
        rightPanel.setPadding(new Insets(15,95,15,15));
        Button deleteButton = new Button("Delete Instrument");
        Text updateQty = new Text("New Quantity: ");
        TextField updateQtyField = new TextField();
        Button updateQtyButton = new Button("Update");
        Text updateUnitValue = new Text("New Unit Value: ");
        TextField updateUnitValueField = new TextField();
        Button updateUnitValueButton = new Button("Update");
        Text updateMessage = new Text(" ");
        updateMessage.setFill(Color.RED);


        tableView.setOnMouseClicked((event) -> {
            rightPanel.add(updateQty, 0,1);
            rightPanel.add(updateQtyField, 1,1);
            rightPanel.add(updateQtyButton, 2,1);
            rightPanel.add(updateUnitValue, 0,2);
            rightPanel.add(updateUnitValueField, 1,2);
            rightPanel.add(updateUnitValueButton, 2,2);
            rightPanel.add(updateMessage, 0,3);
            rightPanel.add(deleteButton, 0,4);

            layout.setRight(rightPanel);
            rightPanel.setVgap(10);
            rightPanel.setHgap(5);
        });

        //creating portfolio distribution chart for the first time
        layout.setBottom(createPieChart(list));

        updateQtyButton.setOnMouseClicked(event -> {
            Instrument currentInstrument = (Instrument) tableView.getSelectionModel().getSelectedItem();
            try {
                double newQty = Double.valueOf(updateQtyField.getText());
                list.updateQty(currentInstrument, newQty);
                updateTable(tableView);

                tableLayout.getChildren().remove(1);
                Text newTotalAsset = updateTotalAsset(tableView);
                newTotalAsset.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 18));
                tableLayout.getChildren().add(newTotalAsset);

                updateMessage.setText(" ");
                updateQtyField.clear();
                updateUnitValueField.clear();
                layout.getChildren().remove(rightPanel);
                layout.setRight(rightPanel);

                layout.setBottom(createPieChart(list));

            } catch (Exception e){
                updateMessage.setText("Number field!");
                updateQtyField.clear();
            }
        });

        updateUnitValueButton.setOnMouseClicked(event -> {
            Instrument currentInstrument = (Instrument) tableView.getSelectionModel().getSelectedItem();
            try {
                double newUnitValue = Double.valueOf(updateUnitValueField.getText());
                list.updateUnitValue(currentInstrument, newUnitValue);
                updateTable(tableView);

                tableLayout.getChildren().remove(1);
                Text newTotalAsset = updateTotalAsset(tableView);
                newTotalAsset.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 18));
                tableLayout.getChildren().add(newTotalAsset);

                updateMessage.setText(" ");
                updateQtyField.clear();
                updateUnitValueField.clear();

                //tried to update current piechart, but it does not show datas properly; so creating new piechart for each update.
                layout.setBottom(createPieChart(list));

            } catch (Exception e){
                updateMessage.setText("Number field!");
                updateUnitValueField.clear();
            }
        });



        deleteButton.setOnMouseClicked(event -> {
            try {
                Instrument currentInstrument = (Instrument) tableView.getSelectionModel().getSelectedItem();
                list.remove(currentInstrument);
                updateTable(tableView);

                tableLayout.getChildren().remove(1);
                Text newTotalAsset = updateTotalAsset(tableView);
                newTotalAsset.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 18));
                tableLayout.getChildren().add(newTotalAsset);

                layout.setBottom(createPieChart(list));

                rightPanel.getChildren().clear();
            }
            catch (Exception e){

                }
        });

        tableLayout.getChildren().addAll(tableView, totalAssetText);
        layout.setLeft(tableLayout);

        return layout;
    }

    private TableView createTable(){
        TableView tableView = new TableView();
        tableView.setMinHeight(550);

        TableColumn<Instrument, String> column1 = new TableColumn<>("Symbol");
        column1.setCellValueFactory(new PropertyValueFactory<>("symbol"));
        column1.setStyle( "-fx-alignment: CENTER;");
        column1.setMinWidth(150);

        TableColumn<Instrument, String> column2 = new TableColumn<>("Name");
        column2.setCellValueFactory(new PropertyValueFactory<>("name"));
        column2.setStyle( "-fx-alignment: CENTER;");
        column2.setMinWidth(200);

        TableColumn<Instrument, String> column3 = new TableColumn<>("Quantity");
        column3.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        column3.setStyle( "-fx-alignment: CENTER;");
        column3.setMinWidth(150);

        TableColumn<Instrument, String> column4 = new TableColumn<>("Unit value");
        column4.setCellValueFactory(new PropertyValueFactory<>("unitValue"));
        column4.setStyle( "-fx-alignment: CENTER;");
        column4.setMinWidth(150);

        TableColumn<Instrument, String> column5 = new TableColumn<>("Total value");
        column5.setCellValueFactory(new PropertyValueFactory<>("totalValue"));
        column5.setStyle( "-fx-alignment: CENTER;");
        column5.setMinWidth(150);

        tableView.getColumns().add(column1);
        tableView.getColumns().add(column2);
        tableView.getColumns().add(column3);
        tableView.getColumns().add(column4);
        tableView.getColumns().add(column5);

        return tableView;
    }

    private PieChart createPieChart(InstrumentDatabase list){
        try {
            PieChart newchart = new PieChart();
            newchart.setPrefSize(300, 300);
            newchart.setPadding(new Insets(0, 0, 0, 200));
            newchart.setTranslateX(300);
            newchart.setTranslateY(-300);
            if (list.getList().size() > 0) {
                newchart.setTitle("Portfolio Distribution");
            }
            list.getList().stream().forEach(instrument -> {
                newchart.getData().add(new PieChart.Data(instrument.getSymbol(), instrument.getTotalValue()));
            });
            return newchart;
        } catch (Exception e){
            return null;
        }
    }

    private void updateTable(TableView tableView){
        try {
            tableView.getItems().clear();
            for (Instrument instrument : list.getList()) {
                tableView.getItems().add(instrument);
            }
        } catch (Exception e) {

        }
    }


    private Text updateTotalAsset(TableView table){
        Text totalAssetText = new Text();
        double totalAsset = 0;
        try {
            for (Instrument instrument : list.getList()) {
                totalAsset += instrument.getTotalValue();
            }
            totalAssetText.setText("Total asset: " + totalAsset);
            return totalAssetText;

        } catch (Exception e) {
            return null;
        }
    }
}
