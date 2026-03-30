package fr.utc.miage.wallet;

import java.sql.Date;

public class Utilisateur {
  private final String name;
  private final String firstName;
  private final Date birthday;
  private Wallet wallet;

  public static final Double INITIAL_CASH_AMOUNT = 200.00;
  private Double cashAmount;

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
    this.cashAmount += amount;
  }

  public void setWallet(final Wallet wallet) {
    this.wallet = wallet;
  }

  public void buyAction(final Action action, final Integer quantity) {
    if (this.iBelieveICanBuy(action, quantity)) {
      this.cashAmount -= action.getPrice() * quantity;
      this.getWallet().addAction(action, quantity);
    }

  }

  public boolean iBelieveICanBuy(Action action, Integer quantity) {
    if (action == null || quantity == null)
      throw new IllegalArgumentException("action ou quantity can not be null");
    return action.getPrice() * quantity <= this.cashAmount;
  }
  
  public void sellAction(final Action action, final Integer quantity) {
    if (action == null || quantity == null) {
      throw new IllegalArgumentException("action ou quantity can not be null");
    }
    if (quantity <= 0) {
      throw new IllegalArgumentException("quantity must be positive");
    }

    this.getWallet().removeAction(action, quantity);
    this.cashAmount += action.getPrice() * quantity;
  }
}
