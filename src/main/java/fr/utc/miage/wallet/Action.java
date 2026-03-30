package fr.utc.miage.wallet;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.Map;

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

  private Map<Date, Double> historicalPrices;

  public Action(final String label, final Double price) {
    this(label, price, ActionCategory.OTHER);
  }

  public Action(final String label, final Double price, final ActionCategory category) {
    validate(label, price);
    this.label = label;
    this.price = price;
    this.type = TypeAction.SIMPLE;
    this.category = category;
    this.composition = null;
    this.historicalPrices = new HashMap<>();
    ACTIONS.put(label, this);
  }

  public Action(final String label, final Double price, final Map<String, Float> composition) {
    validate(label, price);
    this.label = label;
    this.price = price;
    this.type = TypeAction.COMPOSEE;
    this.category = ActionCategory.OTHER;
    this.composition = composition;
    this.historicalPrices = new HashMap<>();
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

  public void getActionAnalyse() {
    // Trier par date pour une analyse plus lisible
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    System.out.println("+------------+---------+------------+");
    System.out.println("| Date       | Valeur  | Evolution  |");
    System.out.println("+------------+---------+------------+");

    Double previousValue = null;

    for (Map.Entry<Date, Double> entry : this.historicalPrices.entrySet()) {
      Double currentValue = entry.getValue();

      String evolution;
      if (previousValue == null) {
        evolution = "N/A";
      } else {
        double percentChange = ((currentValue - previousValue) / previousValue) * 100;
        evolution = String.format("%.2f%%", percentChange);
      }

      System.out.printf("| %-10s | %-7.2f | %-10s |\n",
          sdf.format(entry.getKey()),
          currentValue,
          evolution);

      previousValue = currentValue;
    }

    System.out.println("+------------+---------+------------+");

    DoubleSummaryStatistics stats = this.historicalPrices.values()
        .stream()
        .mapToDouble(Double::doubleValue)
        .summaryStatistics();

    System.out.println("Nombre de valeurs : " + stats.getCount());
    System.out.println("Minimum           : " + stats.getMin());
    System.out.println("Maximum           : " + stats.getMax());
    System.out.println("Moyenne           : " + stats.getAverage());
    System.out.println("Somme             : " + stats.getSum());
  }

  public Double getHistoricalPrice(final Date date) {
    if (this.historicalPrices.containsKey(date))
      return this.historicalPrices.get(date);
    else
      throw new IllegalArgumentException();
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

  public Map<Date, Double> getHistoricalPrices() {
    return historicalPrices;
  }

  /**
   * Adds a historical price for a specific date.
   * 
   * @param date
   * @param price
   * @throws IllegalArgumentException if the price is negative or if the date is
   *                                  null
   */
  public void addHistoricalPrice(Date date, double price) {
    if (price < 0) {
      throw new IllegalArgumentException("Price cannot be negative");
    }
    if (date == null) {
      throw new IllegalArgumentException("Date cannot be null");
    }
    this.historicalPrices.put(date, price);
  }

  @Override
  public String toString() {
    return "Action: " + label + " (" + price + "€)";
  }

  public String toStringC() {
    return "Action: " + label + " (" + price + "€)" + " Composition " + composition;
  }

  /**
   * Returns a string representation of the historical prices.
   *
   * @return a string containing the historical prices or null if there are no
   *         historical prices
   */
  public String getHistoricalPricesString() {
    StringBuilder sb = new StringBuilder();
    if (historicalPrices.isEmpty()) {
      return null;
    }
    sb.append("Action: ").append(label).append("\n").append(" Historical Prices: ");
    for (Map.Entry<Date, Double> entry : historicalPrices.entrySet()) {
      sb.append("\n").append(entry.getKey()).append(": ").append(entry.getValue()).append("€");
    }
    return sb.toString();

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
    result = prime * result + ((category == null) ? 0 : category.hashCode());
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
    if (category != other.category)
      return false;
    if (composition == null) {
      if (other.composition != null)
        return false;
    } else if (!composition.equals(other.composition))
      return false;
    return true;
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
