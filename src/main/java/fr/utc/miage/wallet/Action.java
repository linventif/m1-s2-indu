package fr.utc.miage.wallet;

import java.util.HashMap;
public class Action {
  private String label;
  private double price;
  private static HashMap<String, Action> actions = new HashMap<>();
  private ActionCategory category;

  public Action(String label, double price, ActionCategory category) {
    if (label == null || label.isEmpty()) {
      throw new IllegalArgumentException("Label cannot be null or empty");
    } else if (price < 0) {
      throw new IllegalArgumentException("Price cannot be negative");
    }
    this.label = label;
    this.price = price;
    this.category = category;
    actions.put(label, this);
  }

  public Action(String label, double price) {
    if (label == null || label.isEmpty()) {
      throw new IllegalArgumentException("Label cannot be null or empty");
    } else if (price < 0) {
      throw new IllegalArgumentException("Price cannot be negative");
    }
    this.label = label;
    this.price = price;
    this.category = ActionCategory.OTHER;
    actions.put(label, this);
  }

  public static Action getActionByLabel(String label) {
    return actions.get(label);
  }

  public static HashMap<String, Action> getActions() {
    return actions;
  }

  public static HashMap<String, Action> getActionsByCategory(ActionCategory category) {
    HashMap<String, Action> result = new HashMap<>();
    for (Action action : actions.values()) {
      if (action.getCategory().equals(category.toString())) {
        result.put(action.getLabel(), action);
      }
    }
    return result;
  }

  public String getLabel() {
    return label;
  }

  public String getCategory() {
    return category.toString();
  }

  public void setCategory(ActionCategory category) {
    this.category = category;
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
    return "Action: " + label + " (" + price + "€), Category: " + category;
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
