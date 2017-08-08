package com.eason.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Creates and updated database on demand when opening it.
 * Helper class to create database the first time the provider is
 * initialized and upgrade it when a new version of the provider needs
 * an updated version of the database.
 *
 * This code get form "packages/providers/DownloadProvider"
 *
 * @author <a href="mailto:pujl1@lenovo.com">Eason Pu</a>
 * @date 10/15/14
 */
public final class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    /**
     * Database filename
     */
    private static final String DB_NAME = "downloads.db";
    /**
     * Current database version
     */
    private static final int DB_VERSION = 109;
    /**
     * Name of table in the database
     */
    private static final String DB_TABLE = "downloads";

    public DatabaseHelper(final Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * Creates database the first time we try to open it.
     */
    @Override
    public void onCreate(final SQLiteDatabase db) {
        Log.v(TAG, "populating new database");
        onUpgrade(db, 0, DB_VERSION);
    }

    /**
     * Updates the database format when a content provider is used
     * with a database that was created with a different format.
     * <p/>
     * Note: to support downgrades, creating a table should always drop it first if it already
     * exists.
     */
    @Override
    public void onUpgrade(final SQLiteDatabase db, int oldV, final int newV) {
        if (oldV == 31) {
            // 31 and 100 are identical, just in different code lines. Upgrading from 31 is the
            // same as upgrading from 100.
            oldV = 100;
        } else if (oldV < 100) {
            // no logic to upgrade from these older version, just recreate the DB
            Log.i(TAG, "Upgrading downloads database from version " + oldV
                + " to version " + newV + ", which will destroy all old data");
            oldV = 99;
        } else if (oldV > newV) {
            // user must have downgraded software; we have no way to know how to downgrade the
            // DB, so just recreate it
            Log.i(TAG, "Downgrading downloads database from version " + oldV
                + " (current version is " + newV + "), destroying all old data");
            oldV = 99;
        }

        for (int version = oldV + 1; version <= newV; version++) {
            upgradeTo(db, version);
        }
    }

    /**
     * Upgrade database from (version - 1) to version.
     */
    private void upgradeTo(SQLiteDatabase db, int version) {
        switch (version) {
            case 100:
                //createDownloadsTable(db);
                break;

            case 101:
                //createHeadersTable(db);
                break;

            case 102:
                /*addColumn(db, DB_TABLE, Downloads.Impl.COLUMN_IS_PUBLIC_API,
                    "INTEGER NOT NULL DEFAULT 0");
                addColumn(db, DB_TABLE, Downloads.Impl.COLUMN_ALLOW_ROAMING,
                    "INTEGER NOT NULL DEFAULT 0");
                addColumn(db, DB_TABLE, Downloads.Impl.COLUMN_ALLOWED_NETWORK_TYPES,
                    "INTEGER NOT NULL DEFAULT 0");*/
                break;

            case 103:
                /*addColumn(db, DB_TABLE, Downloads.Impl.COLUMN_IS_VISIBLE_IN_DOWNLOADS_UI,
                    "INTEGER NOT NULL DEFAULT 1");
                makeCacheDownloadsInvisible(db);*/
                break;

            case 104:
                /*addColumn(db, DB_TABLE, Downloads.Impl.COLUMN_BYPASS_RECOMMENDED_SIZE_LIMIT,
                    "INTEGER NOT NULL DEFAULT 0");*/
                break;

            case 105:
                /*fillNullValues(db);*/
                break;

            case 106:
                /*addColumn(db, DB_TABLE, Downloads.Impl.COLUMN_MEDIAPROVIDER_URI, "TEXT");
                addColumn(db, DB_TABLE, Downloads.Impl.COLUMN_DELETED,
                    "BOOLEAN NOT NULL DEFAULT 0");*/
                break;

            case 107:
                //addColumn(db, DB_TABLE, Downloads.Impl.COLUMN_ERROR_MSG, "TEXT");
                break;

            case 108:
                //addColumn(db, DB_TABLE, Downloads.Impl.COLUMN_ALLOW_METERED, "INTEGER NOT NULL DEFAULT 1");
                break;

            case 109:
                //addColumn(db, DB_TABLE, Downloads.Impl.COLUMN_ALLOW_WRITE, "BOOLEAN NOT NULL DEFAULT 0");
                break;

            default:
                throw new IllegalStateException("Don't know how to upgrade to " + version);
        }
    }
}
