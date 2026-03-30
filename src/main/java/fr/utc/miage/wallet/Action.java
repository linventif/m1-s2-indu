package fr.utc.miage.wallet;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Représente une action financière manipulée par l'application.
 * <p>
 * Une action peut être simple, c'est-à-dire portée uniquement par son propre
 * prix, ou composée, auquel cas elle décrit une composition d'autres actions.
 * Chaque action est également enregistrée dans un registre statique permettant
 * de la retrouver par libellé.
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
   * Retourne une représentation textuelle simple de l'action.
   *
   * @return une chaîne décrivant le libellé et le prix de l'action
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
   * Retourne l'historique des prix sous forme textuelle.
   *
   * @return une chaîne multi-ligne décrivant l'historique, ou {@code null} si
   *         aucun historique n'est disponible
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
