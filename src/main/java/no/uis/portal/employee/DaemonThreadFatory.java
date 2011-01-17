package no.uis.portal.employee;

import java.util.concurrent.ThreadFactory;

public class DaemonThreadFatory implements ThreadFactory {

  @Override
  public Thread newThread(Runnable r) {
    Thread t = new Thread(r);
    t.setDaemon(true);
    return t;
  }

}
