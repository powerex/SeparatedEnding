package ua.azbest.comunication;

public enum Color {
    WHITE("White"), BLACK("Black");

    private String name;

    Color(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
