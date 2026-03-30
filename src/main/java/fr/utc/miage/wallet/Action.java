package fr.utc.miage.wallet;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
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

  public Map<Date, Double> getHistoricalPrices() {
    return historicalPrices;
  }


  /**
   * Adds a historical price for a specific date.
   * 
   * @param date
   * @param price
   * @throws IllegalArgumentException if the price is negative or if the date is null
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

  /**
  * Imports historical prices from a CSV file.
  *
  * @param csvFilePath the path to the CSV file containing historical prices.
  * The CSV file should have two columns: "date" and "price", first line is a header.
  * The "date" column should be in the format "yyyy-MM-dd", and the "price" column should contain the corresponding price for that date.
  * @throws IOException if an I/O error occurs while reading the CSV file
  * @throws IllegalArgumentException if the CSV file is not properly formatted or if any of the data is invalid
  */
  public void importHistoricalPrices(String csvFilePath) {
    try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
        String line;
        br.readLine(); // Skip header line
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            if (values.length == 2) {
                try {
                    Date date = Date.valueOf(values[0].trim());
                    double priceValue = Double.parseDouble(values[1].trim());
                    addHistoricalPrice(date, priceValue);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
  } 

  /**
  * Get the price of the action at a specific date.
  *
  * @param date the date for which to get the price
  * @return the price of the action at the given date or the default price if no historical price is found for that date
  */
  public double getPriceAtDate(Date date) {
    if (historicalPrices != null) {
      Double historicalPrice = historicalPrices.get(date);
      if (historicalPrice != null) {
        return historicalPrice;
      }
      }
    return price;
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
  * @return a string containing the historical prices or null if there are no historical prices
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
