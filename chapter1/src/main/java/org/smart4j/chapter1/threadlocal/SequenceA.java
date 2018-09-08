package org.smart4j.chapter1.threadlocal;

/**
 * Created by admin on 2018/9/5.
 */
public class SequenceA implements Sequence {

  private static int number = 0;

  @Override
  public int getNumber() {
    return ++number;
  }

  public static void main(String[] args) {
    Sequence sequence = new SequenceA();

    ClientThread thread1 = new ClientThread(sequence);
    ClientThread thread2 = new ClientThread(sequence);
    ClientThread thread3 = new ClientThread(sequence);
    thread1.start();
    thread2.start();
    thread3.start();
  }
}
