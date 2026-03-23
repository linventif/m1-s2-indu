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

  @Override
  public String toString() {
    return "Wallet{" +
        "listAction=" + listAction +
        '}';
  }
}