package fr.utc.miage.wallet;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class Wallet {
  private final Map<Action, Integer> actions;

  public Wallet() {
    this.actions = new HashMap<>();
  }

  public Map<Action, Integer> getActions() {
    return this.actions;
  }

  public void addAction(final Action action, final int quantity) {
    if (quantity < 0) {
      throw new IllegalArgumentException("Quantity cannot be negative");
    }
    actions.put(action, actions.getOrDefault(action, 0) + quantity);
  }

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
