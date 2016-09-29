package com.example.test99;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TracyM on 2016/9/10.
 */
public class RecordResolver {
    MainActivity mainActivity;

    public void ResoveRecord() {
        ContentResolver contentResolver = mainActivity.getContentResolver();
        Cursor cursor = null;
        try {
            if (ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            cursor = contentResolver.query(
                    // CallLog.Calls.CONTENT_URI, Columns, null,
                    // null,CallLog.Calls.DATE+" desc");
                    CallLog.Calls.CONTENT_URI, null, null, null,
                    CallLog.Calls.DATE + " desc");
            if (cursor == null)
                return ;
            List<RecordEntity> mRecordList = new ArrayList<RecordEntity>();
            while (cursor.moveToNext()) {
                RecordEntity record = new RecordEntity();
                record.name = cursor.getString(cursor
                        .getColumnIndex(CallLog.Calls.CACHED_NAME));
                record.number = cursor.getString(cursor
                        .getColumnIndex(CallLog.Calls.NUMBER));
                record.type = cursor.getInt(cursor
                        .getColumnIndex(CallLog.Calls.TYPE));
                record.date = cursor.getLong(cursor
                        .getColumnIndex(CallLog.Calls.DATE));

                record.duration = cursor.getLong(cursor
                        .getColumnIndex(CallLog.Calls.DURATION));
                record._new = cursor.getInt(cursor
                        .getColumnIndex(CallLog.Calls.NEW));
                Log.e("TAG", record.toString());
//						int photoIdIndex = cursor.getColumnIndex(CACHED_PHOTO_ID);
//						if (photoIdIndex >= 0) {
//							record.cachePhotoId = cursor.getLong(photoIdIndex);
//						}
                mRecordList.add(record);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
