package fr.utc.miage.wallet;

import java.util.HashMap;
import java.util.Map;

class Wallet {
  private HashMap<Action, Integer> actions;

  public Wallet() {
    this.actions = new HashMap<>();
  }

  public Map<Action, Integer> getActions() {
    return this.actions;
  }

  public void addAction(Action action, int quantity) {
    if (quantity < 0) {
      throw new IllegalArgumentException("Quantity cannot be negative");
    }
    actions.put(action, actions.getOrDefault(action, 0) + quantity);
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
    return "Wallet actions: " + actions.toString();
  }
}