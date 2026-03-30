package fr.utc.miage.wallet;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Action {

  enum TypeAction {
    SIMPLE,
    COMPOSEE
  }

  private static final Map<String, Action> ACTIONS = new HashMap<>();

  private final String label;
  private double price;
  private final TypeAction type;
  private ActionCategory category;
  private Map<String, Float> composition;

  public Action(final String label, final Double price) {
    this(label, price, ActionCategory.OTHER);
  }

  // Creation d'une action simple
  public Action(final String label, final Double price, final ActionCategory category) {
    validate(label, price);
    this.label = label;
    this.price = price;
    this.type = TypeAction.SIMPLE;
    this.category = category;
    this.composition = null;
    ACTIONS.put(label, this);
  }

  // Creation d'une action composee
  public Action(final String label, final Double price, final Map<String, Float> composition) {
    validate(label, price);
    this.label = label;
    this.price = price;
    this.type = TypeAction.COMPOSEE;
    this.category = ActionCategory.OTHER;
    this.composition = composition;
    ACTIONS.put(label, this);
  }

  public Action(final String label, final int price) {
    this(label, Double.valueOf(price));
  }

  private static void validate(final String label, final Double price) {
    if (label == null || label.isEmpty()) {
      throw new IllegalArgumentException("Label cannot be null or empty");
    }
    if (price == null || price < 0) {
      throw new IllegalArgumentException("Price cannot be negative");
    }
  }

  public static Action getActionByLabel(final String label) {
    return ACTIONS.get(label);
  }

  public String getLabel() {
    return label;
  }

  public ActionCategory getCategory() {
    return category;
  }

  public void setCategory(final ActionCategory category) {
    this.category = category;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(final double price) {
    if (price < 0) {
      throw new IllegalArgumentException("Price cannot be negative");
    }
    this.price = price;
  }

  public TypeAction getType() {
    return type;
  }

  public Map<String, Float> getComposition() {
    return composition;
  }

  public void setComposition(final Map<String, Float> composition) {
    this.composition = composition;
  }

  public void delete() {
    ACTIONS.remove(label);
  }

  @Override
  public String toString() {
    return "Action: " + label + " (" + price + "€)";
  }

  public String toStringC() {
    return "Action: " + label + " (" + price + "€)" + " Composition " + composition;
  }

  @Override
  public int hashCode() {
    return Objects.hash(label, price, type, category, composition);
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Action other)) {
      return false;
    }
    return Double.doubleToLongBits(price) == Double.doubleToLongBits(other.price)
        && Objects.equals(label, other.label)
        && type == other.type
        && category == other.category
        && Objects.equals(composition, other.composition);
  }

  public static Map<String, Action> getActionsByCategory(final ActionCategory category) {
    Map<String, Action> result = new HashMap<>();
    for (Map.Entry<String, Action> entry : ACTIONS.entrySet()) {
      if (entry.getValue().getCategory() == category) {
        result.put(entry.getKey(), entry.getValue());
      }
    }
    return result;
  }
}
