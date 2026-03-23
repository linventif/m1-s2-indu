package fr.utc.miage.wallet;

public class Action {
  private String label;
  private double price;

  public Action(String label, double price) {
    // add verif
    this.label = label;
    this.price = price;
  }

  public String getLabel() {
    return label;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public String toString() {
    return "Action: " + label + " (" + price + "€)";
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
