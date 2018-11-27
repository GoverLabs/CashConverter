package activity;

import android.content.Context;

public class ContextSingleton {
	private static ContextSingleton instance;

	public static void init(Context context) {
		if (instance == null) {
			instance = new ContextSingleton(context);
		}
	}

	public static ContextSingleton getInstance() {
		return instance;
	}

	private final Context context;

	private ContextSingleton(Context context) {
		this.context = context;
	}

	public Context getContext() {
		return context;
	}
}
