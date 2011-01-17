package no.uis.portal.employee;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import javax.faces.context.FacesContext;
import javax.portlet.PortletRequest;

import org.apache.log4j.Logger;

import com.icesoft.faces.async.render.SessionRenderer;
import com.icesoft.faces.context.DisposableBean;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.util.bridges.jsf.common.FacesMessageUtil;
import com.liferay.util.bridges.jsf.common.JSFPortletUtil;

public class DataBean implements DisposableBean {

  private static final Logger log = Logger.getLogger(DataBean.class);
 
  private String imagePath;

  private ReentrantLock updateLock = new ReentrantLock();
  
  private static final String PUSH_GROUP_NAME = DataBean.class.getName();

  private static final ThreadFactory DAEMON_THREAD_FACTORY = ThreadFactoryStateHolder.threadFactory;

  private ScheduledExecutorService taskExecutor;

  private int counter = 1;  

  public int getCounter(){
	  return counter;
  }
  public void setCounter(int innCounter ){
	  counter = innCounter;
  }
  public void incCounter() {
	  counter++;
	  if (updateLock.tryLock()) {
		  try {
			  if (taskExecutor == null) {
				  taskExecutor = Executors.newSingleThreadScheduledExecutor(DAEMON_THREAD_FACTORY);	          
				  taskExecutor.scheduleAtFixedRate(new CounterUpdater(), 1L, 1L, TimeUnit.SECONDS);
			  }
		  } finally {
			  updateLock.unlock();
		  }
	  }
  } 
  
  public DataBean() {
    SessionRenderer.addCurrentSession(PUSH_GROUP_NAME);
   }

  public String getThemeImagePath() {
    if (imagePath == null) {
      PortletRequest request = JSFPortletUtil.getPortletRequest(FacesContext.getCurrentInstance());
      ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(WebKeys.THEME_DISPLAY);
      imagePath = themeDisplay.getPathThemeImages(); 
    }
    return imagePath;
  }

  private static String getText(FacesContext facesContext, String textKey) {
    Locale locale = null;
    
    if (facesContext != null) {
      try {
        locale = facesContext.getViewRoot().getLocale();
      } catch (Exception e) {
        log.warn(e);
      }
    } 
    if (locale == null) {
      locale = LocaleUtil.getDefault();
    }
    ResourceBundle res = ResourceBundle.getBundle("Language", locale);
    String txt = res.getString(textKey);
    return txt;
  }
  
  private static String getPreference(String prefKey, String defaultValue) {
   
    try {
      String pref = PropsUtil.get(prefKey);
      if (pref != null) {
        return pref;
      }
    } catch(Exception e) {
      log.error("DataBean.getPreference", e);
    }
    return defaultValue;
  }
  
  private void logUpdateError(Exception e, String messageKey) {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    FacesMessageUtil.error(facesContext, getText(facesContext, messageKey), e.getLocalizedMessage());
    log.error(e);
  }

  @Override
  public void dispose() {
    ExecutorService exec = taskExecutor;
    if (exec != null) {
      taskExecutor.shutdownNow();
    }
  }
  
  private class CounterUpdater implements Runnable {

	  public CounterUpdater() {

	  }

	  @Override
	  public void run() {
		  try {	
			  incCounter();
			  SessionRenderer.render(PUSH_GROUP_NAME);
		  } catch(Exception e) {
			  log.error(e);
		  }
	  }
  }

  static ThreadFactory createThreadFactory() {
    return new DaemonThreadFatory();
  }
  
  private static class ThreadFactoryStateHolder {
    private static ThreadFactory threadFactory = createThreadFactory();
  }
}

