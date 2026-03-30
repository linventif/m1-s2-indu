package fr.utc.miage.wallet;

import java.util.Map;

public class Action {

  enum TypeAction {
    SIMPLE,
    COMPOSEE
  }

  private String label;
  private double price;
  private TypeAction type;
  private Map<String, Float> composition;

  public Action(final String label, final Double price) {
    if (label == null || label.isEmpty()) {
      throw new IllegalArgumentException("Label cannot be null or empty");
    } else if (price < 0) {
      throw new IllegalArgumentException("Price cannot be negative");
    }
    this.label = label;
    this.price = price;
    this.type = TypeAction.SIMPLE;
    this.composition = null;
  }

  public Action(final String label, final Double price, final Map<String, Float> composition) {
    if (label == null || label.isEmpty()) {
      throw new IllegalArgumentException("Label cannot be null or empty");
    } else if (price < 0) {
      throw new IllegalArgumentException("Price cannot be negative");
    }
    this.label = label;
    this.price = price;
    this.type = TypeAction.COMPOSEE;
    this.composition = composition;
  }

  public Action(String label2, int price2) {
    // TODO Auto-generated constructor stub
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

  public TypeAction getType() {
    return type;
  }

  public void setType(TypeAction type) {
    this.type = type;
  }

  public Map<String, Float> getComposition() {
    return composition;
  }

  public void setComposition(Map<String, Float> composition) {
    this.composition = composition;
  }

  public void delete() {
    this.delete();
  }

  public String toString() {
    return "Action: " + label + " (" + price + "€)";
  }

  public String toStringC() {
    return "Action: " + label + " (" + price + "€)" + " Composition " + composition;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((label == null) ? 0 : label.hashCode());
    long temp;
    temp = Double.doubleToLongBits(price);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    result = prime * result + ((composition == null) ? 0 : composition.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Action other = (Action) obj;
    if (label == null) {
      if (other.label != null)
        return false;
    } else if (!label.equals(other.label))
      return false;
    if (Double.doubleToLongBits(price) != Double.doubleToLongBits(other.price))
      return false;
    if (type != other.type)
      return false;
    if (composition == null) {
      if (other.composition != null)
        return false;
    } else if (!composition.equals(other.composition))
      return false;
    return true;
  }

}
