package uz.alexits.cargostar.database.cache;

/*
WingsPushSDK for Android
created by Sergey Kim on 2.12.2019
Copyright © 2019 Wings Solutions. All rights reserved
*/

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Класс для работы с sharedPreferences
 * Хранит clientId, deviceId, token пользователя
 * локально на устройстве
 */
public class SharedPrefs {
    private static SharedPrefs INSTANCE;
    private final SharedPreferences prefs;
    private final static String PREFS_FILENAME = "HIDDEN_PREFS";

    private SharedPrefs(final Context context) {
        this.prefs = context.getSharedPreferences(context.getPackageName() + PREFS_FILENAME, Context.MODE_PRIVATE);
    }

    public static SharedPrefs getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (SharedPrefs.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SharedPrefs(context);
                }
            }
        }
        return INSTANCE;
    }

    public void putBoolean(final String key, final boolean value) {
        this.prefs.edit().putBoolean(key, value).apply();
    }

    public void putString(final String key, final String value) {
        this.prefs.edit().putString(key, value).apply();
    }

    public void putInt(final String key, final int value) {
        this.prefs.edit().putInt(key, value).apply();
    }

    public void putLong(final String key, final long value) {
        this.prefs.edit().putLong(key, value).apply();
    }

    public String getString(final String key) {
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        final Callable<String> callable = () -> this.prefs.getString(key, null);
        final Future<String> value = executorService.submit(callable);
        executorService.shutdown();
        try {
            return value.get();
        }
        catch (ExecutionException | InterruptedException e) {
            Log.e(TAG, "getString(): ", e);
            return null;
        }
    }

    public int getInt(final String key) {
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        final Callable<Integer> callable = () -> this.prefs.getInt(key, 0);
        final Future<Integer> value = executorService.submit(callable);
        executorService.shutdown();
        try {
            return value.get();
        }
        catch (ExecutionException | InterruptedException e) {
            Log.e(TAG, "getInt(): ", e);
            return 0;
        }
    }

    public boolean getBoolean(final String key) {
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        final Callable<Boolean> callable = () -> this.prefs.getBoolean(key, true);
        final Future<Boolean> value = executorService.submit(callable);
        executorService.shutdown();
        try {
            return value.get();
        }
        catch (ExecutionException | InterruptedException e) {
            Log.e(TAG, "getBoolean(): ", e);
            return false;
        }
    }

    public long getLong(final String key) {
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        final Callable<Long> callable = () -> this.prefs.getLong(key, 0L);
        final Future<Long> value = executorService.submit(callable);
        executorService.shutdown();
        try {
            return value.get();
        }
        catch (ExecutionException | InterruptedException e) {
            Log.e(TAG, "getLong(): ", e);
            return 0L;
        }
    }

    public static final String ID = "id";
    public static final String BRANCH_ID = "branch_id";
    public static final String KEEP_LOGGED = "keep_logged";
    public static final String IS_LOGGED = "is_logged";
    private static final String TAG = SharedPrefs.class.toString();
}
