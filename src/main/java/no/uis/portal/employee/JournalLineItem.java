package no.uis.portal.employee;

public class JournalLineItem {

  private String title;
  private int amount;

  public JournalLineItem() {
  }
  public JournalLineItem(String title, int amount) {
    this.title = title;
    this.amount = amount;
  }
  
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public int getAmount() {
    return amount;
  }
  public void setAmount(int amount) {
    this.amount = amount;
  }
  
  public void increment() {
    amount ++;
  }
  
}
