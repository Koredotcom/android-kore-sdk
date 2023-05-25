package com.kore.ai.widgetsdk.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class CursorQueryWrapper {

    private final String tag;
    private QueryInterface queryInterface;

    public static class CursorIteratonInterrupt extends Exception {
        private static final long serialVersionUID = -1124533346986767621L;
    }

    public static abstract class CursorResultIterator {
        public void iterate(Cursor cursor) throws Exception {
        }

        boolean shouldIterate() {
            return true;
        }

        void prepareForIterations(Cursor cursor) {
        }
    }

    public interface QueryInterface {
        Cursor query(Context context, Uri uri, String[] projection,
                     String selection, String[] selectionArgs, String sortOrder);
    }

    public static class ContextQuery implements QueryInterface {

        @Override
        public Cursor query(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs,
                            String sortOrder) {
            return context.getContentResolver()
                    .query(uri, projection, selection, selectionArgs, sortOrder);
        }

    }

    CursorQueryWrapper(final String tag) {
        this(tag, null);
    }

    private CursorQueryWrapper(final String tag, QueryInterface queryInterface) {
        this.tag = tag;
        this.queryInterface = new ContextQuery();
    }

    public boolean query(Context context, Uri uri, String[] projection,
                         String selection, String[] selectionArgs, String sortOrder, CursorResultIterator iterator) {
        Cursor cursor = null;
        Log.d(tag, "Starting query");
        try {
            StringBuilder bld = new StringBuilder(
                    "Running query");
            bld.append("\nUri: ").append(uri.toString());
            bld.append("\nProjection: ").append(projection == null ? "null" : TextUtils.join(", ", projection));
            bld.append("\nSelection: ").append(selection);
            bld.append("\nSelectionArgs: ").append("null");
            bld.append("\nSortOrder: ").append(sortOrder);
            Log.d(tag, bld.toString());
            cursor = queryInterface.query(context, uri, projection, selection, selectionArgs, sortOrder);
            if (cursor == null) {
                Log.e(tag, "Cannot process the query. The cursor is null as a result of some error.");
                return false;
            }
            Log.d(tag, "Cursor's count is " + cursor.getCount());
            iterator.prepareForIterations(cursor);
            while (cursor.moveToNext()) {
                iterator.iterate(cursor);
            }
        } catch (Exception e) {
            if (!(e instanceof CursorIteratonInterrupt)) {
                Log.e(tag,
                        "Cannot process the query.", e);
                return false;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.d(tag, "The query is ended successfully");
        return true;
    }

}