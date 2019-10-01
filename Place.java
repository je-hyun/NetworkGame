import java.util.ArrayList;

public class Place {
  private String name;
  private String description;
  private ArrayList<Place> neighbors;
  public String getName() {
    return name;
  }
  public static void connect(Place place1, Place place2) {
    place1.addNeighbor(place2);
    place2.addNeighbor(place1);
  }
  public static void connectOneWay(Place place1, Place place2) {
    place1.addNeighbor(place2);
  }
  public Place(String name, String description) {
    this.neighbors = new ArrayList<Place>();
    this.name = name;
    this.description = description;
  }
  public void addNeighbor(Place place) {
    neighbors.add(place);
  }
  public ArrayList<Place> getNeighbors() {
    return this.neighbors;
  }
  @Override
  public String toString() {
    String result = this.name + " -- \"" + this.description + "\" - Your options: ";
    for (Place pl : this.neighbors) {
      result += "| " + pl.getName() + " |";
    }
    return result;
  }
}
