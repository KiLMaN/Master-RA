package log;

import android.widget.TextView;

public class Log {
	public static TextView log = null;
	
	public static void d(String text){
		if(log != null)
			log.append(getCallingMethod() + text + "\n");
	}
	public static void e(String text){
		if(log != null)
			log.append(getCallingMethod() + text + "\n");
	}
	public static void i(String text){
		if(log != null)
			log.append(getCallingMethod() + text + "\n");
	}
	
	public static void addLog(String text){
		if(log != null)
			log.append(getCallingMethod() + text + "\n");
	}

    private static String getCallingMethod() {
        return trace(Thread.currentThread().getStackTrace(), 4);
    }
 
    private static String trace(StackTraceElement[] e, int level) {
        if (e != null && e.length >= level) {
            StackTraceElement s = e[level];
            if (s != null) {
                return s.getClassName() + "::" + s.getMethodName() + "() -> ";
            }
        }
        return null;
    }
}
