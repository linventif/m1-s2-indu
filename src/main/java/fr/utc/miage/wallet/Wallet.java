package fr.utc.miage.wallet;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Représente un portefeuille contenant des actions et leur quantité.
 * <p>
 * Le portefeuille permet d'ajouter ou de retirer des actions, d'accéder au
 * contenu courant et de calculer sa valeur totale en fonction des prix courants
 * des actions qu'il contient.
 */
class Wallet {
  private final Map<Action, Integer> actions;

  /** Crée un portefeuille vide. */
  public Wallet() {
    this.actions = new HashMap<>();
  }

  /**
   * Retourne les actions actuellement détenues dans le portefeuille.
   *
   * @return une association entre chaque action et la quantité détenue
   */
  public Map<Action, Integer> getActions() {
    return this.actions;
  }

  /**
   * Ajoute une quantité d'une action au portefeuille.
   *
   * @param action l'action à ajouter
   * @param quantity la quantité à ajouter
   * @throws IllegalArgumentException si la quantité est strictement négative
   */
  public void addAction(final Action action, final int quantity) {
    if (quantity < 0) {
      throw new IllegalArgumentException("Quantity cannot be negative");
    }
    actions.put(action, actions.getOrDefault(action, 0) + quantity);
  }

  /**
   * Retire une quantité d'une action déjà détenue.
   *
   * @param action l'action à retirer
   * @param quantity la quantité à retirer
   * @throws IllegalArgumentException si l'action est {@code null}, si la
   *         quantité n'est pas strictement positive ou si le portefeuille ne
   *         contient pas suffisamment de titres
   */
  public void removeAction(final Action action, final int quantity) {
    if (action == null) {
      throw new IllegalArgumentException("Action cannot be null");
    }
    if (quantity <= 0) {
      throw new IllegalArgumentException("Quantity must be positive");
    }

    Integer ownedQuantity = actions.get(action);
    if (ownedQuantity == null || ownedQuantity < quantity) {
      throw new IllegalArgumentException("Not enough actions in wallet");
    }

    actions.put(action, ownedQuantity - quantity);
  }

  /**
   * Calcule la valeur totale du portefeuille en utilisant le prix courant de
   * chaque action.
   *
   * @return la valeur totale du portefeuille
   */
  public double getTotalValue() {
    double total = 0.0;
    for (Map.Entry<Action, Integer> entry : actions.entrySet()) {
      total += entry.getKey().getPrice() * entry.getValue();
    }
    return total;
  }

  @Override
  public String toString() {
    return "Wallet actions: " + actions;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Wallet other)) {
      return false;
    }
    return Objects.equals(actions, other.actions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(actions);
  }
}
