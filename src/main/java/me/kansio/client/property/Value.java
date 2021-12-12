package me.kansio.client.property;

public abstract class Value<Type> {

    private final String name;

    private final Object owner;
    protected Value<?> parent;
    private Type value;

    public Value(String name, Object owner, Type value) {
        this.name = name;
        this.owner = owner;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getOwner() {
        return owner;
    }

    public Type getValue() {
        return value;
    }

    public void setValueAutoSave(Type value) {
        this.value = value;
        //Sulfur.getInstance().getFileManager().getObjects().forEach(MFile::save);
    }


    public void setValue(Type value) {
        this.value = value;
    }

    public Value getParent() {
        return parent;
    }

    public String getValueAsString() {
        return String.valueOf(value);
    }

}