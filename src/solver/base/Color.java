package solver.base;

public enum Color {
    RED ("red"),
    ORANGE ("orange"),
    PINK("pink"),
    BROWN("brown"),
    VIOLET("violet"),
    YELLOW("yellow"),
    GRAY("gray"),
    DARK_GREEN("dark_green"),
    PARROT_GREEN("parrot_green"),
    LIGHT_GREEN("light_green"),
    LIGHT_BLUE("light_blue"),
    DARK_BLUE("dark_blue");

    private final String name;

    // private String name(){ return name; }

    Color(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}