package org.andengine.util.debuging;

import org.andengine.util.Constants;

import android.util.Log;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 13:29:16 - 08.03.2010
 */
public class Debuging {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static String sTag = Constants.DEBUGTAG;
	private static String sDebugUser = "";
	private static DebugLevel sDebugLevel = DebugLevel.VERBOSE;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public static String getTag() {
		return Debuging.sTag;
	}

	public static void setTag(final String pTag) {
		Debuging.sTag = pTag;
	}

	public static DebugLevel getDebugLevel() {
		return Debuging.sDebugLevel;
	}

	public static void setDebugLevel(final DebugLevel pDebugLevel) {
		if(pDebugLevel == null) {
			throw new IllegalArgumentException("pDebugLevel must not be null!");
		}
		Debuging.sDebugLevel = pDebugLevel;
	}

	public static void setDebugUser(final String pDebugUser) {
		if(pDebugUser == null) {
			throw new IllegalArgumentException("pDebugUser must not be null!");
		}
		Debuging.sDebugUser = pDebugUser;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public static void log(final DebugLevel pDebugLevel, final String pMessage) {
		switch(pDebugLevel) {
			case NONE:
				return;
			case VERBOSE:
				Debuging.v(pMessage);
				return;
			case INFO:
				Debuging.i(pMessage);
				return;
			case DEBUG:
				Debuging.d(pMessage);
				return;
			case WARNING:
				Debuging.w(pMessage);
				return;
			case ERROR:
				Debuging.e(pMessage);
				return;
		}
	}

	public static void log(final DebugLevel pDebugLevel, final String pMessage, final Throwable pThrowable) {
		switch(pDebugLevel) {
			case NONE:
				return;
			case VERBOSE:
				Debuging.v(pMessage, pThrowable);
				return;
			case INFO:
				Debuging.i(pMessage, pThrowable);
				return;
			case DEBUG:
				Debuging.d(pMessage, pThrowable);
				return;
			case WARNING:
				Debuging.w(pMessage, pThrowable);
				return;
			case ERROR:
				Debuging.e(pMessage, pThrowable);
				return;
		}
	}

	public static void log(final DebugLevel pDebugLevel, final String pTag, final String pMessage) {
		switch(pDebugLevel) {
			case NONE:
				return;
			case VERBOSE:
				Debuging.v(pTag, pMessage);
				return;
			case INFO:
				Debuging.i(pTag, pMessage);
				return;
			case DEBUG:
				Debuging.d(pTag, pMessage);
				return;
			case WARNING:
				Debuging.w(pTag, pMessage);
				return;
			case ERROR:
				Debuging.e(pTag, pMessage);
				return;
		}
	}

	public static void log(final DebugLevel pDebugLevel, final String pTag, final String pMessage, final Throwable pThrowable) {
		switch(pDebugLevel) {
			case NONE:
				return;
			case VERBOSE:
				Debuging.v(pTag, pMessage, pThrowable);
				return;
			case INFO:
				Debuging.i(pTag, pMessage, pThrowable);
				return;
			case DEBUG:
				Debuging.d(pTag, pMessage, pThrowable);
				return;
			case WARNING:
				Debuging.w(pTag, pMessage, pThrowable);
				return;
			case ERROR:
				Debuging.e(pTag, pMessage, pThrowable);
				return;
		}
	}

	public static void v(final String pMessage) {
		Debuging.v(Debuging.sTag, pMessage, null);
	}

	public static void v(final String pMessage, final Throwable pThrowable) {
		Debuging.v(Debuging.sTag, pMessage, pThrowable);
	}

	public static void v(final String pTag, final String pMessage) {
		Debuging.v(pTag, pMessage, null);
	}

	public static void v(final String pTag, final String pMessage, final Throwable pThrowable) {
		if(Debuging.sDebugLevel.isSameOrLessThan(DebugLevel.VERBOSE)) {
			if(pThrowable == null) {
				Log.v(pTag, pMessage);
			} else {
				Log.v(pTag, pMessage, pThrowable);
			}
		}
	}

	public static void vUser(final String pMessage, final String pDebugUser) {
		if(Debuging.sDebugUser.equals(pDebugUser)) {
			Debuging.v(pMessage);
		}
	}

	public static void vUser(final String pMessage, final Throwable pThrowable, final String pDebugUser) {
		if(Debuging.sDebugUser.equals(pDebugUser)) {
			Debuging.v(pMessage, pThrowable);
		}
	}

	public static void vUser(final String pTag, final String pMessage, final String pDebugUser) {
		if(Debuging.sDebugUser.equals(pDebugUser)) {
			Debuging.v(pTag, pMessage);
		}
	}

	public static void vUser(final String pTag, final String pMessage, final Throwable pThrowable, final String pDebugUser) {
		if(Debuging.sDebugUser.equals(pDebugUser)) {
			Debuging.v(pTag, pMessage, pThrowable);
		}
	}

	public static void d(final String pMessage) {
		Debuging.d(Debuging.sTag, pMessage, null);
	}

	public static void d(final String pMessage, final Throwable pThrowable) {
		Debuging.d(Debuging.sTag, pMessage, pThrowable);
	}

	public static void d(final String pTag, final String pMessage) {
		Debuging.d(pTag, pMessage, null);
	}

	public static void d(final String pTag, final String pMessage, final Throwable pThrowable) {
		if(Debuging.sDebugLevel.isSameOrLessThan(DebugLevel.DEBUG)) {
			if(pThrowable == null) {
				Log.d(pTag, pMessage);
			} else {
				Log.d(pTag, pMessage, pThrowable);
			}
		}
	}

	public static void dUser(final String pMessage, final String pDebugUser) {
		if(Debuging.sDebugUser.equals(pDebugUser)) {
			Debuging.d(pMessage);
		}
	}

	public static void dUser(final String pMessage, final Throwable pThrowable, final String pDebugUser) {
		if(Debuging.sDebugUser.equals(pDebugUser)) {
			Debuging.d(pMessage, pThrowable);
		}
	}

	public static void dUser(final String pTag, final String pMessage, final String pDebugUser) {
		if(Debuging.sDebugUser.equals(pDebugUser)) {
			Debuging.d(pTag, pMessage);
		}
	}

	public static void dUser(final String pTag, final String pMessage, final Throwable pThrowable, final String pDebugUser) {
		if(Debuging.sDebugUser.equals(pDebugUser)) {
			Debuging.d(pTag, pMessage, pThrowable);
		}
	}

	public static void i(final String pMessage) {
		Debuging.i(Debuging.sTag, pMessage, null);
	}

	public static void i(final String pMessage, final Throwable pThrowable) {
		Debuging.i(Debuging.sTag, pMessage, pThrowable);
	}

	public static void i(final String pTag, final String pMessage) {
		Debuging.i(pTag, pMessage, null);
	}

	public static void i(final String pTag, final String pMessage, final Throwable pThrowable) {
		if(Debuging.sDebugLevel.isSameOrLessThan(DebugLevel.INFO)) {
			if(pThrowable == null) {
				Log.i(pTag, pMessage);
			} else {
				Log.i(pTag, pMessage, pThrowable);
			}
		}
	}

	public static void iUser(final String pMessage, final String pDebugUser) {
		if(Debuging.sDebugUser.equals(pDebugUser)) {
			Debuging.i(pMessage);
		}
	}

	public static void iUser(final String pMessage, final Throwable pThrowable, final String pDebugUser) {
		if(Debuging.sDebugUser.equals(pDebugUser)) {
			Debuging.i(pMessage, pThrowable);
		}
	}

	public static void iUser(final String pTag, final String pMessage, final String pDebugUser) {
		if(Debuging.sDebugUser.equals(pDebugUser)) {
			Debuging.i(pTag, pMessage);
		}
	}

	public static void iUser(final String pTag, final String pMessage, final Throwable pThrowable, final String pDebugUser) {
		if(Debuging.sDebugUser.equals(pDebugUser)) {
			Debuging.i(pTag, pMessage, pThrowable);
		}
	}

	public static void w(final String pMessage) {
		Debuging.w(Debuging.sTag, pMessage, null);
	}

	public static void w(final Throwable pThrowable) {
		Debuging.w("", pThrowable);
	}

	public static void w(final String pMessage, final Throwable pThrowable) {
		Debuging.w(Debuging.sTag, pMessage, pThrowable);
	}

	public static void w(final String pTag, final String pMessage) {
		Debuging.w(pTag, pMessage, null);
	}

	public static void w(final String pTag, final String pMessage, final Throwable pThrowable) {
		if(Debuging.sDebugLevel.isSameOrLessThan(DebugLevel.WARNING)) {
			if(pThrowable == null) {
				Log.w(pTag, pMessage);
			} else {
				Log.w(pTag, pMessage, pThrowable);
			}
		}
	}

	public static void wUser(final String pMessage, final String pDebugUser) {
		if(Debuging.sDebugUser.equals(pDebugUser)) {
			Debuging.w(pMessage);
		}
	}

	public static void wUser(final Throwable pThrowable, final String pDebugUser) {
		if(Debuging.sDebugUser.equals(pDebugUser)) {
			Debuging.w(pThrowable);
		}
	}

	public static void wUser(final String pMessage, final Throwable pThrowable, final String pDebugUser) {
		if(Debuging.sDebugUser.equals(pDebugUser)) {
			Debuging.w(pMessage, pThrowable);
		}
	}

	public static void wUser(final String pTag, final String pMessage, final String pDebugUser) {
		if(Debuging.sDebugUser.equals(pDebugUser)) {
			Debuging.w(pTag, pMessage);
		}
	}

	public static void wUser(final String pTag, final String pMessage, final Throwable pThrowable, final String pDebugUser) {
		if(Debuging.sDebugUser.equals(pDebugUser)) {
			Debuging.w(pTag, pMessage, pThrowable);
		}
	}

	public static void e(final String pMessage) {
		Debuging.e(Debuging.sTag, pMessage, null);
	}

	public static void e(final Throwable pThrowable) {
		Debuging.e(Debuging.sTag, pThrowable);
	}

	public static void e(final String pMessage, final Throwable pThrowable) {
		Debuging.e(Debuging.sTag, pMessage, pThrowable);
	}

	public static void e(final String pTag, final String pMessage) {
		Debuging.e(pTag, pMessage, null);
	}

	public static void e(final String pTag, final String pMessage, final Throwable pThrowable) {
		if(Debuging.sDebugLevel.isSameOrLessThan(DebugLevel.ERROR)) {
			if(pThrowable == null) {
				Log.e(pTag, pMessage);
			} else {
				Log.e(pTag, pMessage, pThrowable);
			}
		}
	}

	public static void eUser(final String pMessage, final String pDebugUser) {
		if(Debuging.sDebugUser.equals(pDebugUser)) {
			Debuging.e(pMessage);
		}
	}

	public static void eUser(final Throwable pThrowable, final String pDebugUser) {
		if(Debuging.sDebugUser.equals(pDebugUser)) {
			Debuging.e(pThrowable);
		}
	}

	public static void eUser(final String pMessage, final Throwable pThrowable, final String pDebugUser) {
		if(Debuging.sDebugUser.equals(pDebugUser)) {
			Debuging.e(pMessage, pThrowable);
		}
	}

	public static void eUser(final String pTag, final String pMessage, final String pDebugUser) {
		if(Debuging.sDebugUser.equals(pDebugUser)) {
			Debuging.e(pTag, pMessage);
		}
	}

	public static void eUser(final String pTag, final String pMessage, final Throwable pThrowable, final String pDebugUser) {
		if(Debuging.sDebugUser.equals(pDebugUser)) {
			Debuging.e(pTag, pMessage, pThrowable);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static enum DebugLevel {
		NONE, ERROR, WARNING, INFO, DEBUG, VERBOSE;

		public static DebugLevel ALL = DebugLevel.VERBOSE;

		public boolean isSameOrLessThan(final DebugLevel pDebugLevel) {
			return this.compareTo(pDebugLevel) >= 0;
		}
	}
}
