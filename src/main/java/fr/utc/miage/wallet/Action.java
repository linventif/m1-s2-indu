package fr.utc.miage.wallet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Comparator;

/**
 * Représente une action financière manipulée par l'application.
 * <p>
 * Une action peut être simple ou composée, posséder une catégorie, un prix
 * courant, une composition éventuelle et un historique de prix. Les actions
 * sont également enregistrées dans un registre statique permettant de les
 * retrouver par leur libellé.
 */
public class Action {

  /**
   * Décrit la nature d'une action.
   */
  enum TypeAction {
    /** Action indépendante, sans composition détaillée. */
    SIMPLE,
    /** Action composée d'autres actions pondérées. */
    COMPOSEE
  }

  private static final Map<String, Action> ACTIONS = new HashMap<>();

  private final String label;
  private double price;
  private final TypeAction type;
  private ActionCategory category;
  private Map<String, Float> composition;
  private Map<Date, Double> historicalPrices;
  private static final int CHART_HEIGHT = 10;

  /**
   * Crée une action simple dans la catégorie {@link ActionCategory#OTHER}.
   *
   * @param label le libellé unique de l'action
   * @param price le prix courant de l'action
   */
  public Action(final String label, final Double price) {
    this(label, price, ActionCategory.OTHER);
  }

  /**
   * Crée une action simple avec une catégorie explicite.
   *
   * @param label le libellé unique de l'action
   * @param price le prix courant de l'action
   * @param category la catégorie de l'action
   * @throws IllegalArgumentException si le libellé est vide ou nul, ou si le
   *         prix est nul ou négatif
   */
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

  /**
   * Crée une action composée à partir d'une composition de sous-actions.
   *
   * @param label le libellé unique de l'action
   * @param price le prix courant de l'action
   * @param composition la composition de l'action, exprimée sous forme de
   *        pondérations par libellé
   * @throws IllegalArgumentException si le libellé est vide ou nul, ou si le
   *         prix est nul ou négatif
   */
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

  /**
   * Crée une action simple à partir d'un prix entier.
   *
   * @param label le libellé unique de l'action
   * @param price le prix courant de l'action
   */
  public Action(final String label, final int price) {
    this(label, Double.valueOf(price));
  }

  /**
   * Valide les paramètres communs à tous les constructeurs.
   *
   * @param label le libellé à valider
   * @param price le prix à valider
   * @throws IllegalArgumentException si le libellé est vide ou nul, ou si le
   *         prix est nul ou négatif
   */
  private static void validate(final String label, final Double price) {
    if (label == null || label.isEmpty()) {
      throw new IllegalArgumentException("Label cannot be null or empty");
    }
    if (price == null || price < 0) {
      throw new IllegalArgumentException("Price cannot be negative");
    }
  }

  /**
   * Recherche une action déjà enregistrée à partir de son libellé.
   *
   * @param label le libellé recherché
   * @return l'action correspondante ou {@code null} si elle n'existe pas
   */
  public static Action getActionByLabel(final String label) {
    return ACTIONS.get(label);
  }

  /**
   * Retourne le libellé de l'action.
   *
   * @return le libellé de l'action
   */
  public String getLabel() {
    return label;
  }

  /**
   * Retourne la catégorie de l'action.
   *
   * @return la catégorie courante
   */
  public ActionCategory getCategory() {
    return category;
  }

  /**
   * Modifie la catégorie de l'action.
   *
   * @param category la nouvelle catégorie
   */
  public void setCategory(final ActionCategory category) {
    this.category = category;
  }

  /**
   * Retourne le prix courant de l'action.
   *
   * @return le prix courant
   */
  public double getPrice() {
    return price;
  }

  /**
   * Affiche une analyse tabulaire de l'historique des prix de l'action ainsi
   * qu'un résumé statistique simple.
   * <p>
   * La sortie est écrite sur la sortie standard et inclut la date, la valeur et
   * l'évolution entre deux mesures successives.
   */
  public void getActionAnalyse() {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    final String SEPARATEUR = "+------------+---------+------------+";
    List<Map.Entry<Date, Double>> sortedEntries = getSortedHistoricalEntries();

    System.out.println(SEPARATEUR);
    System.out.println("| Date       | Valeur  | Evolution  |");
    System.out.println(SEPARATEUR);

    Double previousValue = null;

    for (Map.Entry<Date, Double> entry : sortedEntries) {
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

    System.out.println(SEPARATEUR);

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

  /**
   * Retourne une courbe ASCII représentant l'évolution du prix dans le temps.
   * <p>
   * Les valeurs sont triées par date avant le tracé. Si aucun historique n'est
   * disponible, la méthode retourne {@code null}.
   *
   * @return une représentation textuelle de la courbe, ou {@code null} si
   *         aucun historique n'est enregistré
   */
  public String getHistoricalPriceCurveString() {
    List<Map.Entry<Date, Double>> sortedEntries = getSortedHistoricalEntries();
    if (sortedEntries.isEmpty()) {
      return null;
    }

    int width = sortedEntries.size() * 3 - 2;
    char[][] grid = createEmptyGrid(CHART_HEIGHT, width);
    double min = sortedEntries.stream().mapToDouble(Map.Entry::getValue).min().orElse(price);
    double max = sortedEntries.stream().mapToDouble(Map.Entry::getValue).max().orElse(price);

    int[] xPoints = new int[sortedEntries.size()];
    int[] yPoints = new int[sortedEntries.size()];

    for (int i = 0; i < sortedEntries.size(); i++) {
      xPoints[i] = i * 3;
      yPoints[i] = mapValueToRow(sortedEntries.get(i).getValue(), min, max, CHART_HEIGHT);
      grid[yPoints[i]][xPoints[i]] = '*';
    }

    for (int i = 0; i < sortedEntries.size() - 1; i++) {
      drawSegment(grid, xPoints[i], yPoints[i], xPoints[i + 1], yPoints[i + 1]);
      grid[yPoints[i]][xPoints[i]] = '*';
      grid[yPoints[i + 1]][xPoints[i + 1]] = '*';
    }

    return buildChartString(grid, sortedEntries, min, max);
  }

  /**
   * Retourne le prix historique enregistré pour une date donnée.
   *
   * @param date la date recherchée
   * @return le prix enregistré à cette date
   * @throws IllegalArgumentException si aucun prix n'est disponible pour cette
   *         date
   */
  public Double getHistoricalPrice(final Date date) {
    if (this.historicalPrices.containsKey(date)) {
      return this.historicalPrices.get(date);
    } else {
      throw new IllegalArgumentException();
    }
  }

  /**
   * Modifie le prix courant de l'action.
   *
   * @param price le nouveau prix
   * @throws IllegalArgumentException si le prix est négatif
   */
  public void setPrice(final double price) {
    if (price < 0) {
      throw new IllegalArgumentException("Price cannot be negative");
    }
    this.price = price;
  }

  /**
   * Retourne le type de l'action.
   *
   * @return le type de l'action
   */
  public TypeAction getType() {
    return type;
  }

  /**
   * Retourne la composition d'une action composée.
   *
   * @return la composition courante, ou {@code null} pour une action simple
   */
  public Map<String, Float> getComposition() {
    return composition;
  }

  /**
   * Définit ou remplace la composition de l'action.
   *
   * @param composition la nouvelle composition
   */
  public void setComposition(final Map<String, Float> composition) {
    this.composition = composition;
  }

  /**
   * Supprime l'action du registre statique des actions connues.
   */
  public void delete() {
    ACTIONS.remove(label);
  }

  /**
   * Retourne l'historique des prix connus.
   *
   * @return l'historique des prix, indexé par date
   */
  public Map<Date, Double> getHistoricalPrices() {
    return historicalPrices;
  }

  /**
   * Ajoute un prix historique pour une date donnée.
   *
   * @param date la date associée au prix
   * @param price le prix historique à enregistrer
   * @throws IllegalArgumentException si le prix est négatif ou si la date est
   *         nulle
   */
  public void addHistoricalPrice(final Date date, final double price) {
    if (price < 0) {
      throw new IllegalArgumentException("Price cannot be negative");
    }
    if (date == null) {
      throw new IllegalArgumentException("Date cannot be null");
    }
    this.historicalPrices.put(date, price);
  }

  /**
   * Importe un historique de prix depuis un fichier CSV.
   * <p>
   * Le fichier doit contenir un en-tête exact {@code date,price}, puis une
   * ligne par mesure avec une date au format {@code yyyy-MM-dd} et un prix.
   *
   * @param csvFilePath le chemin du fichier CSV à lire
   * @throws IllegalArgumentException si l'en-tête du fichier est invalide
   * @throws IllegalStateException si une erreur de lecture survient
   */
  public void importHistoricalPrices(final String csvFilePath) {
    try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
      String line;
      String headerLine = br.readLine();
      if (headerLine == null || !headerLine.trim().equals("date,price")) {
        throw new IllegalArgumentException("Invalid CSV header");
      }
      while ((line = br.readLine()) != null) {
        String[] values = line.split(",");
        if (values.length == 2) {
          processHistoricalPriceLine(values);
        }
      }
    } catch (IOException e) {
      throw new IllegalStateException("Error reading CSV file: " + e.getMessage(), e);
    }
  }

  /**
   * Traite une ligne de fichier CSV représentant une date et un prix.
   *
   * @param values les deux colonnes lues dans le CSV
   */
  private void processHistoricalPriceLine(final String[] values) {
    try {
      Date date = Date.valueOf(values[0].trim());
      double priceValue = Double.parseDouble(values[1].trim());
      addHistoricalPrice(date, priceValue);
    } catch (IllegalArgumentException e) {
      System.out.println("Skipping invalid line in CSV: " + String.join(",", values) + " - " + e.getMessage());
    }
  }

  /**
   * Retourne le prix historique à une date donnée, ou le prix courant si aucun
   * historique n'est disponible pour cette date.
   *
   * @param date la date recherchée
   * @return le prix à la date demandée, ou le prix courant par défaut
   */
  public double getPriceAtDate(final Date date) {
    if (historicalPrices != null) {
      Double historicalPrice = historicalPrices.get(date);
      if (historicalPrice != null) {
        return historicalPrice;
      }
    }
    return price;
  }

  /**
   * Retourne une représentation textuelle simple de l'action.
   *
   * @return une chaîne décrivant le libellé et le prix courant
   */
  @Override
  public String toString() {
    return "Action: " + label + " (" + price + "€)";
  }

  /**
   * Retourne une représentation textuelle incluant la composition.
   *
   * @return une chaîne décrivant le libellé, le prix et la composition
   */
  public String toStringC() {
    return "Action: " + label + " (" + price + "€)" + " Composition " + composition;
  }

  /**
   * Retourne une représentation textuelle de l'historique des prix.
   *
   * @return une chaîne décrivant l'historique des prix, ou {@code null} si
   *         aucun historique n'est enregistré
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

  private List<Map.Entry<Date, Double>> getSortedHistoricalEntries() {
    List<Map.Entry<Date, Double>> sortedEntries = new ArrayList<>(historicalPrices.entrySet());
    sortedEntries.sort(Comparator.comparing(Map.Entry::getKey));
    return sortedEntries;
  }

  private static char[][] createEmptyGrid(final int height, final int width) {
    char[][] grid = new char[height][width];
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        grid[row][col] = ' ';
      }
    }
    return grid;
  }

  private static int mapValueToRow(final double value, final double min, final double max, final int height) {
    if (Double.compare(min, max) == 0) {
      return height / 2;
    }
    double normalized = (value - min) / (max - min);
    return height - 1 - (int) Math.round(normalized * (height - 1));
  }

  private static void drawSegment(
      final char[][] grid, final int x1, final int y1, final int x2, final int y2) {
    int steps = Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1));
    for (int step = 1; step < steps; step++) {
      int x = x1 + (int) Math.round((x2 - x1) * (step / (double) steps));
      int y = y1 + (int) Math.round((y2 - y1) * (step / (double) steps));
      if (grid[y][x] == '*') {
        continue;
      }
      grid[y][x] = getSegmentCharacter(x1, y1, x2, y2);
    }
  }

  private static char getSegmentCharacter(final int x1, final int y1, final int x2, final int y2) {
    if (y1 == y2) {
      return '-';
    }
    if (x1 == x2) {
      return '|';
    }
    return y2 < y1 ? '/' : '\\';
  }

  private static String buildChartString(
      final char[][] grid,
      final List<Map.Entry<Date, Double>> sortedEntries,
      final double min,
      final double max) {
    StringBuilder sb = new StringBuilder();
    sb.append("Courbe d'evolution du prix").append("\n");
    for (int row = 0; row < grid.length; row++) {
      double axisValue = max - ((max - min) * row / (grid.length - 1.0));
      sb.append(String.format("%8.2f | ", axisValue));
      sb.append(grid[row]);
      sb.append("\n");
    }
    sb.append("         +");
    for (int col = 0; col < grid[0].length + 2; col++) {
      sb.append('-');
    }
    sb.append("\n");
    sb.append("Dates    : ");
    for (Map.Entry<Date, Double> entry : sortedEntries) {
      sb.append(new SimpleDateFormat("dd/MM").format(entry.getKey())).append("   ");
    }
    return sb.toString();
  }

  /**
   * Calcule le code de hachage de l'action à partir de ses attributs
   * significatifs.
   *
   * @return le code de hachage de l'action
   */
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

  /**
   * Compare cette action à un autre objet.
   *
   * @param obj l'objet à comparer
   * @return {@code true} si les deux objets représentent la même action,
   *         {@code false} sinon
   */
  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Action other = (Action) obj;
    if (label == null) {
      if (other.label != null) {
        return false;
      }
    } else if (!label.equals(other.label)) {
      return false;
    }
    if (Double.doubleToLongBits(price) != Double.doubleToLongBits(other.price)) {
      return false;
    }
    if (type != other.type) {
      return false;
    }
    if (category != other.category) {
      return false;
    }
    if (composition == null) {
      if (other.composition != null) {
        return false;
      }
    } else if (!composition.equals(other.composition)) {
      return false;
    }
    return true;
  }

  /**
   * Retourne toutes les actions enregistrées appartenant à une catégorie
   * donnée.
   *
   * @param category la catégorie recherchée
   * @return les actions appartenant à cette catégorie
   */
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
