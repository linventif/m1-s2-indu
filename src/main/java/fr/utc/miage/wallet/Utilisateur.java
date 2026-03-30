package fr.utc.miage.wallet;

import java.time.LocalDate;
import java.time.Period;
import java.sql.Date;
import java.util.List;

public class Utilisateur {
  private final String name;
  private final String firstName;
  private final Date birthday;
  private Wallet wallet;
  private Double cashAmout = 200.00;

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

  public List<Action> getActionList() {
    return actionList;
  }

  public void setWallet(Wallet wallet) {
    this.wallet = wallet;
  }

  public void setActionList(List<Action> actionList) {
    this.actionList = actionList;
  }

}
