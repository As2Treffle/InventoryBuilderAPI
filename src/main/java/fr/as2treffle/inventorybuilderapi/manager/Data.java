package fr.as2treffle.inventorybuilderapi.manager;

public class Data {

    private final String id;
    private final DataType type;
    private Object value;

    public Data(String id, DataType type, Object value) {
        this.id = id;
        this.type = type;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public DataType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
