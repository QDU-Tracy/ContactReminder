package com.example.test99;

/**
 * Created by TracyM on 2016/9/10.
 */

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private Button button;
    RecordResolver recordResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResoveRecord();
            }
        });
    }

    public void ResoveRecord() {
        ContentResolver contentResolver = this.getContentResolver();
        Cursor cursor = null;
        //Runtime Permission Request(for 6.0)
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CALL_LOG}
                ,REQUEST_CODE_ASK_PERMISSIONS);
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_CALL_LOG);
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
                //读取是从新日期到旧日期进行读取
                RecordEntity record = new RecordEntity();
                record.name = cursor.getString(cursor
                        .getColumnIndex(CallLog.Calls.CACHED_NAME));
                record.number = cursor.getString(cursor
                        .getColumnIndex(CallLog.Calls.NUMBER));
                //Type:1:拨出2.接入3.未接
                record.type = cursor.getInt(cursor
                        .getColumnIndex(CallLog.Calls.TYPE));
                record.date = cursor.getLong(cursor
                        .getColumnIndex(CallLog.Calls.DATE));
                SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date date = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE))));
                String time = sfd.format(date);//格式化的效果:例如2010-01-08 09:10:11
                Log.d("Date", time);

                record.duration = cursor.getLong(cursor
                        .getColumnIndex(CallLog.Calls.DURATION));
                record._new = cursor.getInt(cursor
                        .getColumnIndex(CallLog.Calls.NEW));
                Log.d("log", record.toString());
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
    //请求权限申请的结果
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode,grantResults);
    }
    //处理权限被拒绝或授权后的动作
    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ResoveRecord();
                Log.d("Debug", "Permission Granted");
            } else {
                Log.d("Debug", "Permission Denied");
            }
        }
    }

}
