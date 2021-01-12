package com.xxxx.cc.parse;

import android.content.Context;
import android.text.TextUtils;

import com.xxxx.cc.global.Constans;
import com.xxxx.cc.util.SharedPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CustomItemParse {

    /**
     * [
     * {
     * "callTaskField":false,
     * "createdtime":"2019-07-23 22:50:02",
     * "createrid":"83d4c439977142d5ac9622e30095f458",
     * "creatername":"skyy",
     * "customInput":false,
     * "id":"8062de7fbfb341f487daaa68ce8e399f",
     * "moudle":"contacts",
     * "name":"222",
     * "number":0,
     * "orgid":"b3c7d259aee544f59e7c1a3315051da2",
     * "parentId":"8ab8481c61624d5591c934e2c88950b9",
     * "requireWrite":false,
     * "sequence":0,
     * "showInPage":true,
     * "showInServiceSummary":false,
     * "showInWorkorder":false,
     * "supportEdit":false,
     * "supportExport":false,
     * "supportImport":false,
     * "supportSearch":0,
     * "updatetime":"2019-07-23 22:50:02",
     * "updateuser":"83d4c439977142d5ac9622e30095f458"
     * },
     * {
     * "callTaskField":false,
     * "createdtime":"2019-07-23 22:49:57",
     * "createrid":"83d4c439977142d5ac9622e30095f458",
     * "creatername":"skyy",
     * "customInput":false,
     * "id":"fe3301f0fbc5478a9fa84efb8ff1c5a0",
     * "moudle":"contacts",
     * "name":"111111",
     * "number":0,
     * "orgid":"b3c7d259aee544f59e7c1a3315051da2",
     * "parentId":"8ab8481c61624d5591c934e2c88950b9",
     * "requireWrite":false,
     * "sequence":0,
     * "showInPage":true,
     * "showInServiceSummary":false,
     * "showInWorkorder":false,
     * "supportEdit":false,
     * "supportExport":false,
     * "supportImport":false,
     * "supportSearch":0,
     * "updatetime":"2019-07-23 22:49:57",
     * "updateuser":"83d4c439977142d5ac9622e30095f458"
     * }
     * ]
     */

    public static JSONArray parseItem(Context context, String key) {
        String json = SharedPreferencesUtil.getValue(context, Constans.SP_DEFINED_ITEM_KEY);
        if (TextUtils.isEmpty(json) || TextUtils.isEmpty(key)) {
            return null;
        }

        if (!json.contains(key)) {
            return null;
        }

        try {
            JSONObject jsonObject = new JSONObject(json).optJSONObject("data");
            JSONArray items = jsonObject.optJSONArray(key);
            return items;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
