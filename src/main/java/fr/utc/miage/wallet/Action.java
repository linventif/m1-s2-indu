package fr.utc.miage.wallet;

import java.util.HashMap;

public class Action {
  private String label;
  private double price;
  private static HashMap<String, Action> actions = new HashMap<>();

  public Action(String label, double price) {
    if (label == null || label.isEmpty()) {
      throw new IllegalArgumentException("Label cannot be null or empty");
    } else if (price < 0) {
      throw new IllegalArgumentException("Price cannot be negative");
    }
    this.label = label;
    this.price = price;
    actions.put(label, this);
  }

  public static Action getActionByLabel(String label) {
    return actions.get(label);
  }

  public String getLabel() {
    return label;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    if (price < 0) {
      throw new IllegalArgumentException("Price cannot be negative");
    }
    this.price = price;
  }

  public String toString() {
    return "Action: " + label + " (" + price + "€)";
  }

  public void delete() {
    actions.remove(label);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj == null || getClass() != obj.getClass()) {
      return false;
    } else {
      Action other = (Action) obj;
      return label.equals(other.label) && Double.compare(price, other.price) == 0;
    }
  }

  @Override
  public int hashCode() {
    int result = label.hashCode();
    result = 31 * result + Double.hashCode(price);
    return result;
  }
}
