package org.smart4j.chapter1.threadlocal;

/**
 * Created by admin on 2018/9/5.
 */
public class ClientThread extends Thread {

  private Sequence sequence;

  public ClientThread(Sequence sequence) {
    this.sequence = sequence;
  }

  @Override
  public void run() {
    for (int i = 0; i < 3; i++) {
      System.out.println(Thread.currentThread().getName() + " => " + sequence.getNumber());
    }
  }
}
