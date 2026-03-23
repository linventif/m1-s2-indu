package fr.utc.miage.wallet;

import java.time.LocalDate;
import java.time.Period;
import java.sql.Date;
import java.util.List;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Utilisateur {
  private final String name;
  private final String firstName;
  private final Date birthday;
  private Wallet wallet;
  private List<Action> actionList;

  public Utilisateur(String name, String firstName, Date birthday, Wallet wallet, List<Action> actionList) {
    this.name = name;
    this.firstName = firstName;

    LocalDate birthLocal = birthday.toLocalDate();
    int age = Period.between(birthLocal, LocalDate.now()).getYears();

    if (age < 18) {
      throw new IllegalArgumentException("L'utilisateur doit avoir au moins 18 ans.");
    }

    this.birthday = birthday;
    this.wallet = wallet;
    this.actionList = actionList;
  }

  public Utilisateur(String name, String firstName, Date birthday, Wallet wallet, List<Action> actionList) {
    this.name = name;
    this.firstName = firstName;

    LocalDate birthLocal = birthday.toLocalDate();
    int age = Period.between(birthLocal, LocalDate.now()).getYears();

    if (age < 18) {
      throw new IllegalArgumentException("L'utilisateur doit avoir au moins 18 ans.");
    }

    this.birthday = birthday;
    this.wallet = wallet;
    this.actionList = actionList;
  }

  public String getName() {
    return name;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getBirthday() {
    return birthday;
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
