package fr.utc.miage.wallet;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class Utilisateur {
  private final String name;
  private final String firstName;
  private final Date birthday;
  private Wallet wallet;
  private Double cashAmout = 200.00;

  public Utilisateur(String name, String firstName, Date birthday) {
    this.name = name;
    this.firstName = firstName;

    LocalDate birthLocal = birthday.toLocalDate();
    int age = Period.between(birthLocal, LocalDate.now()).getYears();

    if (age < 18) {
      throw new IllegalArgumentException("L'utilisateur doit avoir au moins 18 ans.");
    }

    this.birthday = birthday;
    this.wallet = new Wallet();
  }

  public String getName() {
    return name;
  }

  public String getFirstName() {
    return firstName;
  }

  public Date getBirthday() {
    return birthday;
  }

  public Wallet getWallet() {
    return wallet;
  }

  public void setWallet(Wallet wallet) {
    this.wallet = wallet;
  }

  public Double getCashAmout(){
    return this.cashAmout;
  }

}
