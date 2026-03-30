package fr.utc.miage.wallet;

import java.sql.Date;

public class Utilisateur {
  private final String name;
  private final String firstName;
  private final Date birthday;
  private Wallet wallet;
  private Double cashAmout = 200.00;

  public Utilisateur(final String firstName, final String name, final Date birthday) {
    this.firstName = firstName;
    this.name = name;
    this.birthday = birthday;
    this.wallet = new Wallet();
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

  public Double getCashAmout() {
    return cashAmout;
  }

  public void setWallet(final Wallet wallet) {
    this.wallet = wallet;
  }
}
