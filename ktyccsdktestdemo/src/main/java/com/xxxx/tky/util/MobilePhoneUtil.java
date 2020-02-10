package com.xxxx.tky.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.xxxx.tky.model.ContactUserName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhoufeng
 * @date 2019/10/14
 * @moduleName
 */
public class MobilePhoneUtil {
    private final static String ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
    // 号码
    private final static String NUM = ContactsContract.CommonDataKinds.Phone.NUMBER;
    // 联系人姓名
    private final static String NAME = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;

    //联系人提供者的uri
    private static Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

    //获取所有联系人
    public static List<ContactUserName> getMobilePhoneList(Context context) {
        List<ContactUserName> phoneDtos = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        if (cr != null) {
            Cursor cursor = cr.query(phoneUri, new String[]{ID, NUM, NAME}, null, null, null);
            while (cursor != null && cursor.moveToNext()) {
                String phoneNum = cursor.getString(cursor.getColumnIndex(NUM));
                phoneNum = TextUtil.getTextRemoveSpace(phoneNum);
                ContactUserName phoneDto = new ContactUserName(
                        cursor.getString(cursor.getColumnIndex(ID)),
                        cursor.getString(cursor.getColumnIndex(NAME)), phoneNum,
                        TextUtil.getNameFirstChar(cursor.getString(cursor.getColumnIndex(NAME))),
                        TextUtil.getNameToPinyin(cursor.getString(cursor.getColumnIndex(NAME)))
                        );
                phoneDtos.add(phoneDto);
            }
        }
        return phoneDtos;
    }
}
