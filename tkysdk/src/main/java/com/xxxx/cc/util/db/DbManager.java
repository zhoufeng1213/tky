package com.xxxx.cc.util.db;

import android.content.Context;

import com.kty.cc.db.gen.DaoMaster;
import com.kty.cc.db.gen.DaoSession;


/**
 * @author zhoufeng
 * @date 2019/8/5
 * @moduleName
 */
public class DbManager {


    private static DaoMaster.DevOpenHelper mHelper;
    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;

    private volatile static DbManager dbManager = null;
    private Context mContext;

    public static DbManager getInstance() {
        DbManager instance = null;
        if (dbManager == null) {
            synchronized (DbManager.class) {
                if (instance == null) {
                    instance = new DbManager();
                    dbManager = instance;
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        this.mContext = context;
    }


    /**
     * 判断是否存在数据库，如果没有则创建数据库
     *
     * @return DaoMaster
     */
    public DaoMaster getDaoMaster() {
        if (mDaoMaster == null) {
            mHelper = new DaoMaster.DevOpenHelper(mContext.getApplicationContext(), "kty_sip", null);
            mDaoMaster = new DaoMaster(mHelper.getWritableDatabase());
        }
        return mDaoMaster;
    }

    /**
     * 完成对数据库的添加、删除、修改、查询的操作，仅仅是一个接口
     *
     * @return DaoSession
     */
    public DaoSession getDaoSession() {
        if (mDaoSession == null) {
            if (mDaoMaster == null) {
                mDaoMaster = getDaoMaster();
            }
            mDaoSession = mDaoMaster.newSession();
        }
        return mDaoSession;
    }

    /**
     * 关闭所有的操作,数据库开启的时候，使用完毕了必须要关闭
     */
    public void closeConnection() {
        closeHelper();
        closeDaoSession();
    }

    public void closeHelper() {
        if (mHelper != null) {
            mHelper.close();
            mHelper = null;
        }
    }

    public void closeDaoSession() {
        if (mDaoSession != null) {
            mDaoSession.clear();
            mDaoSession = null;
        }
    }


}
