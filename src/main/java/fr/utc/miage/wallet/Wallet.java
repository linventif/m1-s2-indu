package fr.utc.miage.wallet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Wallet {
  private ArrayList<Transaction> transactions;
  private Map<Action, Integer> listAction;

  public Wallet() {
    this.transactions = new ArrayList<>();
    this.listAction = new HashMap<>();
  }

  public List<Transaction> getTransactions() {
    return this.transactions;
  }

  public Map<Action, Integer> getListAction() {
    return this.listAction;
  }

  public void addTransaction(Transaction transaction) {
    Action action = transaction.getAction();
    this.transactions.add(transaction);

    if (!this.listAction.containsKey(action))
      this.listAction.put(action, transaction.getQuantity());
    else
      this.listAction.put(action, this.listAction.get(action) + transaction.getQuantity());
  }

  @Override
  public String toString() {
    return "Wallet [getTransactions()=" + getTransactions() + ", getListAction()="
        + getListAction() + "]";
  }

}
