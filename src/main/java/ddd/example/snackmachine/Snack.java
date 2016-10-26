package ddd.example.snackmachine;

public class Snack {

	public static final Snack None = new Snack("None");
	public static final Snack Chocolate = new Snack("Chocolate");
	public static final Snack Soda = new Snack("Soda");
	public static final Snack Gum = new Snack("Gum");

	public String name;

	public Snack(String name) {
		this.name = name;
	}

}
