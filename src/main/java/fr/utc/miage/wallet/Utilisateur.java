package fr.utc.miage.wallet;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Utilisateur {
  private final String name;
  private final String firstName;
  private final Date birthday;
  private Wallet wallet;

  public static final Double INITIAL_CASH_AMOUNT = 200.00;
  private Double cashAmount;
  private List<String> historiMouvementSold = new ArrayList<>(); //liste pour enregister les mouvements du sold
  

  public Utilisateur(final String firstName, final String name, final Date birthday) {
    this.firstName = firstName;
    this.name = name;
    this.birthday = birthday;
    this.wallet = new Wallet();
    this.cashAmount = INITIAL_CASH_AMOUNT;
  }

  public String getName() {
    return this.name;
  }

  public String getFirstName() {
    return this.firstName;
  }

  public Date getBirthday() {
    return birthday;
  }

  public Wallet getWallet() {
    return wallet;
  }

  public Double getCashAmount() {
    return cashAmount;
  }

  public void addCashAmount(Double amount) {
    if (amount <= 0) {
      throw new IllegalArgumentException("Amount must be positive");
    }
    historiMouvementSold.add("Ajout d'argent :" + amount);
    this.cashAmount += amount;
  }

  public void setWallet(final Wallet wallet) {
    this.wallet = wallet;
  }

  public void setActionList(final List<Action> actionList) {
    this.actionList = actionList;
  }

  public void buyAction(final Action action, final Integer quantity) {
    if (action == null || quantity == null)
      throw new IllegalArgumentException("action ou quantity can not be null");
    if (action.getPrice() * quantity <= this.cashAmout) {
      this.cashAmout -= action.getPrice() * quantity;
      this.getWallet().addAction(action, quantity);
      historiMouvementSold.add("Action acheté :" + action.toString() + this.cashAmount);
    }

  }
}
