package portfolio;


import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 *
 * @author Burak
 */
public class AddView {
    private InstrumentDatabase list;

    public AddView(InstrumentDatabase list) {
        this.list = list;
    }

    public Parent getView(){
        GridPane layout = new GridPane();


        Text symbolName = new Text("Symbol: ");
        TextField symbolField = new TextField();
        Text name = new Text("Name: ");
        TextField nameField = new TextField();
        Text qty = new Text("Quantity: ");
        TextField qtyField = new TextField();
        Text unitPrice = new Text("Unit price: ");
        TextField priceField = new TextField();
        Button addButton = new Button("Add");
        Text message = new Text("");


        layout.add(symbolName, 0, 0);
        layout.add(symbolField, 1, 0);
        layout.add(name, 0, 1);
        layout.add(nameField, 1, 1);
        layout.add(qty, 0, 2);
        layout.add(qtyField, 1, 2);
        layout.add(unitPrice, 0, 3);
        layout.add(priceField, 1, 3);
        layout.add(addButton, 1,4);
        layout.add(message, 1,5);

        symbolField.setMaxWidth(150);
        nameField.setMaxWidth(150);
        qtyField.setMaxWidth(150);
        priceField.setMaxWidth(150);

        layout.setHgap(6);
        layout.setVgap(15);
        layout.setPadding(new Insets(30,0,0,100));

        addButton.setOnMouseClicked((event) -> {
            if(!symbolField.getText().equals("") && !nameField.getText().equals("") && !qtyField.getText().equals("") && !priceField.getText().equals("")){
                try {
                    double qtyToAdd = Double.valueOf(qtyField.getText());
                    double priceToAdd = Double.valueOf(priceField.getText());
                    String symbolToAdd = symbolField.getText().trim();
                    String nameToAdd = nameField.getText().trim();
                    boolean bool = true;
                    for (Instrument inst : list.getList()){
                        if(inst.getSymbol().equals(symbolToAdd) && inst.getName().equals(nameToAdd)) {
                            bool = false;
                            message.setText("Instrument already exists");
                            message.setFill(Color.RED);
                        }
                    }
                    if(bool) {
                        list.add(symbolToAdd, nameToAdd, qtyToAdd, priceToAdd);
                        symbolField.clear();
                        nameField.clear();
                        qtyField.clear();
                        priceField.clear();
                        message.setText("Instrument successfully added");
                        message.setFill(Color.GREEN);
                    }
                } catch (Exception e){
                    message.setText("Quantity and price fields should be numbers!");
                    message.setFill(Color.RED);
                }

            } else {
                message.setText("Please fill all the fields!");
                message.setFill(Color.RED);
            }

        });


        return layout;
    }


}