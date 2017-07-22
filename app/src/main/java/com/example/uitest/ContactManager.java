package com.example.uitest.autoaddcontact;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Administrator on 17-6-21.
 */

public class ContactManager {
    private String TAG = "AddContact";
    private ContentValues values;
    private final String NAME_KEY = ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME;
    private final String PHONE_KEY = ContactsContract.CommonDataKinds.Phone.NUMBER;
    private final String MIMETYPE = ContactsContract.Data.MIMETYPE;
    private final String RAW_CONTACT_ID = ContactsContract.Data.RAW_CONTACT_ID;
    private final Uri DATA_URI = ContactsContract.Data.CONTENT_URI;
    private final String NAME_TYPR = ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE;
    private final String NUMBER_TYPR = ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE;
    private ArrayList<Long> id_list = new ArrayList<>();
    private int count = 0;  //初始联系人数目
    private ContentResolver mResolver;
    private  Random random_id = new Random();

    public ContactManager(Context context){

        this.mResolver = context.getContentResolver();
        values = new ContentValues();
        count = mResolver.query(ContactsContract.RawContacts.CONTENT_URI,
                new String[]{ContactsContract.RawContacts._ID},null,null,null).getCount();
    }

    /**
     * 将联系人写入数据库
     * @param contact 联系人数据
     * @return 返回添加的数据的ID
     */
    public long add(Contact contact){

        Uri rawcontactsrui = mResolver.insert(ContactsContract.RawContacts.CONTENT_URI,values);
        long id = ContentUris.parseId(rawcontactsrui);
        id_list.add(id);
        //写入号码
        values.put(RAW_CONTACT_ID,id);
        values.put(MIMETYPE,NUMBER_TYPR);
        values.put(PHONE_KEY,contact.getPhone_number());
        mResolver.insert(DATA_URI,values);
        values.clear();
        //写入姓名
        values.put(RAW_CONTACT_ID,id);
        values.put(MIMETYPE, NAME_TYPR);
        values.put(NAME_KEY,contact.getName());
        mResolver.insert(DATA_URI,values);
        values.clear();

        return id;
    }

    /**
     * 随机删除一个已添加的联系人
     * @return  删除成功返回true
     */
    public boolean delete(){
        if(id_list.size()==0){
            return false;
        }

        int index = random_id.nextInt(id_list.size());
        int delete_result = mResolver.delete(ContactsContract.RawContacts.CONTENT_URI,
                ContactsContract.RawContacts._ID+"=?",new String[]{String.valueOf(id_list.get(index))});

        if(delete_result>-1){
            id_list.remove(index);
            Log.e(TAG,"删除成功");
            return true;
        }
        return false;
    }
    public boolean checkContact(){
        if(id_list.size()==0){
            return false;
        }
        Cursor cursor = mResolver.query(ContactsContract.RawContacts.CONTENT_URI,new String[]{ContactsContract.RawContacts._ID},null,null,null);

        if(cursor.getCount()==(id_list.size()+count)){
            Log.e(TAG,"检查数目正确");
            cursor.close();
            return true;
        }
        return false;
    }
}
