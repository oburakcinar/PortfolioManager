package portfolio;

public class Instrument {
    private String symbol;
    private String name;
    private double quantity;
    private double unitValue;


    public Instrument(String symbol, String name, double quantity, double unitValue) {
        this.symbol = symbol;
        this.name = name;
        this.quantity = quantity;
        this.unitValue = unitValue;


    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getUnitValue() {
        return unitValue;
    }

    public double getTotalValue() {
        return unitValue * quantity;
    }

    public void setQuantity(double qty){
        quantity = qty;
    }

    public void setUnitValue(double unitValue){
        this.unitValue = unitValue;
    }






}

