package fr.utc.miage.wallet;

import java.util.HashMap;
import java.util.Map;

class Wallet {
  private HashMap<Action, Integer> listAction;

  public Wallet() {
    this.listAction = new HashMap<>();
  }

  public Map<Action, Integer> getListAction() {
    return this.listAction;
  }

  public void addAction(Action action, int quantity) {
    if (quantity < 0) {
      throw new IllegalArgumentException("Quantity cannot be negative");
    }
    listAction.put(action, listAction.getOrDefault(action, 0) + quantity);
  }

  public double getTotalValue() {
    double total = 0.0;
    for (Map.Entry<Action, Integer> entry : listAction.entrySet()) {
      total += entry.getKey().getPrice() * entry.getValue();
    }
    return total;
  }

  @Override
  public String toString() {
    return "Wallet actions: " + listAction.toString();
  }
}