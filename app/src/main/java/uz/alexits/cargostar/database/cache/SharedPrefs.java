package uz.alexits.cargostar.database.cache;

/*
WingsPushSDK for Android
created by Sergey Kim on 2.12.2019
Copyright © 2019 Wings Solutions. All rights reserved
*/

import android.content.Context;
import android.content.SharedPreferences;

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

    public static SharedPrefs getInstance(final Context context)
    {
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

    public String getString(final String key)
    {
        return this.prefs.getString(key, null);
    }

    public int getInt(final String key) {
        return this.prefs.getInt(key, -1);
    }

    public boolean getBoolean(final String key)
    {
        return this.prefs.getBoolean(key, false);
    }

    public long getLong(final String key) {
        return this.prefs.getLong(key, -1);
    }

    public static final String LOGIN = "login";
    public static final String PASSWORD_HASH = "password_hash";
    public static final String ID = "id";
    public static final String BRANCH_ID = "branch_id";
    public static final String TOKEN = "token";
    public static final String KEEP_LOGGED = "keep_logged";
}
