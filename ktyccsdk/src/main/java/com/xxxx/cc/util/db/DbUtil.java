package com.xxxx.cc.util.db;

import android.content.Context;

import com.kty.cc.db.gen.ContentBeanDao;
import com.kty.cc.db.gen.FileDownloadVODao;
import com.kty.cc.db.gen.QueryCustomPersonBeanDao;
import com.xxxx.cc.global.Constans;
import com.xxxx.cc.model.ContentBean;
import com.xxxx.cc.model.FileDownloadVO;
import com.xxxx.cc.model.QueryCustomPersonBean;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;


/**
 * @author zhoufeng
 * @date 2019/8/5
 * @moduleName
 */
public class DbUtil {

    private static Context context;
    private static DbManager dbManager;

    public static void init(Context mContext) {
        context = mContext.getApplicationContext();
    }

    /**
     * 根据url查找本地音频路径
     *
     * @param url
     * @return
     */
    public static FileDownloadVO queryFileDownLoadPathByUrl(String url) {
        return getDbManager().getDaoSession().getFileDownloadVODao().
                queryBuilder().where(FileDownloadVODao.Properties.Url.eq(url))
                .build().unique();
    }

    /**
     * 根据localpath查找本地音频路径
     *
     * @param localPath
     * @return
     */
    public static FileDownloadVO queryFileDownLoadUrlByPath(String localPath) {
        return getDbManager().getDaoSession().getFileDownloadVODao().
                queryBuilder().where(FileDownloadVODao.Properties.AbsPath.eq(localPath))
                .build().unique();
    }

    /**
     * 保存file路径
     *
     * @param fileDownloadVO
     */
    public static void saveFileDownLoadPath(FileDownloadVO fileDownloadVO) {
        //判断是否存在有同一个url的数据
        FileDownloadVO cache = queryFileDownLoadPathByUrl(fileDownloadVO.getUrl());
        if (cache == null) {
            getDbManager().getDaoSession().getFileDownloadVODao().save(fileDownloadVO);
        } else {
            fileDownloadVO.setId(cache.getId());
            getDbManager().getDaoSession().getFileDownloadVODao().update(fileDownloadVO);
        }
    }


    public static void clearDb() {
        if (getDbManager() != null && getDbManager().getDaoSession() != null) {
            if (getDbManager().getDaoSession().getFileDownloadVODao() != null) {
                getDbManager().getDaoSession().getFileDownloadVODao().deleteAll();
            }
            if (getDbManager().getDaoSession().getContentBeanDao() != null) {
                getDbManager().getDaoSession().getContentBeanDao().deleteAll();
            }
            if (getDbManager().getDaoSession().getQueryCustomPersonBeanDao() != null) {
                getDbManager().getDaoSession().getQueryCustomPersonBeanDao().deleteAll();
            }
        }
    }

    public static void savePhoneRecordList(List<ContentBean> list) {
        getDbManager().getDaoSession().getContentBeanDao().insertOrReplaceInTx(list);
    }


    public static void savePhoneRecordListOrUpdate(List<ContentBean> list) {
        getDbManager().getDaoSession().getContentBeanDao().insertOrReplaceInTx(list);
    }

    public static void saveCustomPersonListOrUpdate(List<QueryCustomPersonBean> list) {
        getDbManager().getDaoSession().getQueryCustomPersonBeanDao().insertOrReplaceInTx(list);
    }


    public static void clearCustomPersonList() {
        if (getDbManager().getDaoSession().getQueryCustomPersonBeanDao() != null) {
            getDbManager().getDaoSession().getQueryCustomPersonBeanDao().deleteAll();
        }
    }

    public static List<ContentBean> queryPhoneRecordList(String userId, int page) {
        return getDbManager().getDaoSession().getContentBeanDao().queryBuilder()
                .where(ContentBeanDao.Properties.UserId.eq(userId))
                .orderDesc(ContentBeanDao.Properties.CreateTime)
                .offset(page * Constans.COMMON_PAGE_SIZE)
                .limit(Constans.COMMON_PAGE_SIZE)
                .list();
    }

    public static List<ContentBean> queryPhoneRecordList(String userId, String dnis, String direction, int page, int size) {
        return getDbManager().getDaoSession().getContentBeanDao().queryBuilder()
                .where(ContentBeanDao.Properties.UserId.eq(userId),
                        ContentBeanDao.Properties.Direction.eq(direction),
                        ContentBeanDao.Properties.Dnis.eq(dnis)
                )
                .orderDesc(ContentBeanDao.Properties.CreateTime)
                .offset(page * size)
                .limit(size)
                .list()
                ;
    }


    public static List<ContentBean> queryPhoneRecordListByHistory(String userId, String dnis, String direction) {
        return getDbManager().getDaoSession().getContentBeanDao().queryBuilder()
                .where(ContentBeanDao.Properties.UserId.eq(userId),
                        ContentBeanDao.Properties.Direction.eq(direction),
                        ContentBeanDao.Properties.Dnis.eq(dnis)
                )
                .orderDesc(ContentBeanDao.Properties.CreateTime)
                .offset(0)
                .limit(1000)
                .list()
                ;
    }

    public static List<ContentBean> queryPhoneRecordListByHistory(String userId) {
        return getDbManager().getDaoSession().getContentBeanDao().queryBuilder()
                .where(ContentBeanDao.Properties.UserId.eq(userId))
                .orderDesc(ContentBeanDao.Properties.CreateTime)
                .offset(0)
                .limit(Constans.COMMON_LOAD_PAGE_SIZE)
                .list()
                ;
    }

    public static List<QueryCustomPersonBean> queryCustomPersonBeanList(int page) {
        return getDbManager().getDaoSession().getQueryCustomPersonBeanDao().queryBuilder()
                .where(QueryCustomPersonBeanDao.Properties.Datastatus.eq(true))
                .orderAsc(QueryCustomPersonBeanDao.Properties.Letters)
                .offset(page * Constans.COMMON_PAGE_SIZE)
                .limit(Constans.COMMON_PAGE_SIZE)
                .list()
                ;
    }


    public static List<QueryCustomPersonBean> queryCustomPersonBeanAllList() {
        return getDbManager().getDaoSession().getQueryCustomPersonBeanDao().queryBuilder()
                .where(QueryCustomPersonBeanDao.Properties.Datastatus.eq(true))
                .orderAsc(QueryCustomPersonBeanDao.Properties.Letters)
//                .offset(0)
//                .limit(1000)
                .list()
                ;
    }


    public static QueryCustomPersonBean queryCustomPersonBeanById(String id) {
        return getDbManager().getDaoSession().getQueryCustomPersonBeanDao().queryBuilder()
                .where(QueryCustomPersonBeanDao.Properties.Datastatus.eq(true),
                        QueryCustomPersonBeanDao.Properties.Id.eq(id)
                )
//                .offset(0)
//                .limit(1000)
                .unique()
                ;
    }


    public static void updateQueryCustomPersonBeanById(QueryCustomPersonBean bean) {
        getDbManager().getDaoSession().getQueryCustomPersonBeanDao().update(bean);
    }

    public static List<QueryCustomPersonBean> queryCustomPersonBeanAllListByNameOrPhone(String str) {
        QueryBuilder qb = getDbManager().getDaoSession().getQueryCustomPersonBeanDao().queryBuilder();
        return qb.where(QueryCustomPersonBeanDao.Properties.Datastatus.eq(true),
                qb.or(QueryCustomPersonBeanDao.Properties.RealMobileNumber.like("%" + str + "%"),
                        QueryCustomPersonBeanDao.Properties.Name.like("%" + str + "%"),
                        QueryCustomPersonBeanDao.Properties.DisplayNameSpelling.like("%" + str.toUpperCase() + "%")
                )
        )
                .orderAsc(QueryCustomPersonBeanDao.Properties.Letters)
                .list()
                ;
    }

    private static DbManager getDbManager() {
        if (dbManager == null && context != null) {
            dbManager = DbManager.getInstance();
            dbManager.init(context);
        }
        return dbManager;
    }

}
