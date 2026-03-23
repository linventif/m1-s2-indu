package fr.utc.miage.wallet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Wallet {

  private String code;
  private ArrayList<Transaction> transactions;
  private Map<Action, Integer> listAction;

  public Wallet() {}

  public Wallet(final String code) {
    this.code = code;
    this.transactions = new ArrayList<Transaction>();
    this.listAction = new HashMap<Action, Integer>();
  }

  public String getCode() {
    return this.code;
  }

  public ArrayList<Transaction> getTransactions() {
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
    return "Wallet [getCode()=" + getCode() + ", getTransactions()=" + getTransactions() + ", getListAction()="
        + getListAction() + "]";
  }

}
