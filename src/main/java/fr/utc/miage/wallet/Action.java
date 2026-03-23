package fr.utc.miage.wallet;

public class Action {
  private String label;
  private int price;

  public Action(String label, int price) {
    this.label = label;
    this.price = price;
  }

  public String getLabel() {
    return label;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
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
      return label.equals(other.label) && price == other.price;
    }
  }

  @Override
  public int hashCode() {
    int result = label.hashCode();
    result = 31 * result + price;
    return result;
  }
}
