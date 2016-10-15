/**
 * Copyright 2016 CVTE All Rights Reserved.
 */
package org.lowcarbon.soda.util;

import org.lowcarbon.soda.App;
import org.lowcarbon.soda.dao.DaoMaster;
import org.lowcarbon.soda.dao.KeyWord;
import org.lowcarbon.soda.dao.KeyWordDao;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: TODO
 * @author: laizhenqi
 * @date: 2016/10/8
 */
public class KeyWordDBUtil {
    private static final String TAG = KeyWordDBUtil.class.getSimpleName();
    private static final String DB_NAME = "keyword-db";
    private static KeyWordDBUtil sMessageDBUtil;
    private KeyWordDao mKeyWordDao;

    private KeyWordDBUtil() {
    }

    public static KeyWordDBUtil getInstance() {
        if (sMessageDBUtil == null) {
            sMessageDBUtil = new KeyWordDBUtil();
            sMessageDBUtil.mKeyWordDao = new DaoMaster(new DaoMaster.DevOpenHelper(
                    App.getInstance(), DB_NAME, null).getWritableDatabase()).newSession().getKeyWordDao();
        }
        return sMessageDBUtil;
    }

    public KeyWordDao getMessageDao() {
        return mKeyWordDao;
    }

    /**
     * 数据库插入搜索数据
     *
     * @param keywords 搜索词名称list
     */
    private void insertSearchKeywordsToDb(List<KeyWord> keywords) {
        for (KeyWord info : keywords) {
            mKeyWordDao.insertOrReplace(info);
        }
    }

    /**
     * 插入历史纪录到数据库
     *
     * @param info 搜索的单词
     */
    public void insertHistorySearchKeywordsToDb(KeyWord info) {
        // 数据库存在此历史时删除之
        clearSearchKeyWords(info.getKeyword());
        // 然后再插入
        mKeyWordDao.insertOrReplace(info);
    }

    /**
     * 根据搜索类型查找数据
     *
     * @param type 搜索类型(历史、热搜、联想词)
     * @return 搜索的结果集合
     */
    public List<String> queryKeywords(int type) {
        List<KeyWord> data = mKeyWordDao.queryBuilder().where(KeyWordDao.Properties.Type.eq(type))
                .orderDesc(type == KeyWord.TYPE_HOT ? KeyWordDao.Properties.Count : KeyWordDao.Properties.Time)
                .list();
        List<String> result = new ArrayList<>();
        for (KeyWord key : data) {
            result.add(key.getKeyword());
        }
        return result;
    }

    /**
     * 根据输入的词查找数据库匹配的数据
     * 用于输入单词的时候联想匹配单词
     * ①首先匹配历史，排序按搜索的时间
     * ②然后匹配联想词库，排序按搜索的次数，如果词库包含搜索历史，则不匹配因为第一步已经匹配
     *
     * @param word          输入单词
     * @return 搜索的结果集合
     */
    public List<KeyWord> queryMatchKeywords(String word) {
        List<KeyWord> data = new ArrayList<>();
        // 先查找历史，按时间排序
        List<KeyWord> history = mKeyWordDao.queryBuilder()
                .where(KeyWordDao.Properties.Keyword.like("%" + word + "%"), KeyWordDao.Properties.Type.eq(KeyWord.TYPE_HISTORY))
                .orderDesc(KeyWordDao.Properties.Time)
                .list();
        data.addAll(history);
        // 然后查找词库，按次数排序
        List<KeyWord> all = mKeyWordDao.queryBuilder()
                .where(KeyWordDao.Properties.Keyword.like("%" + word + "%"), KeyWordDao.Properties.Type.eq(KeyWord.TYPE_ALL))
                .list();
        data.addAll(all);
        return data;
    }


    /**
     * 数据库清除某个搜索历史
     */
    private void clearSearchKeyWords(String keyword) {
        mKeyWordDao.queryBuilder()
                .where(KeyWordDao.Properties.Keyword.eq(keyword), KeyWordDao.Properties.Type.eq(KeyWord.TYPE_HISTORY))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }

    /**
     * 数据库清空搜索历史以外的数据
     */
    private void clearTopAndAssociativeSearchKeyWords() {
        // 将不是历史的清空掉
        mKeyWordDao.queryBuilder()
                .where(KeyWordDao.Properties.Type.notEq(KeyWord.TYPE_HISTORY))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }

    /**
     * 数据库清空搜索历史的数据，用于一键情况历史纪录
     */
    public void clearHisotySearchKeyWords() {
        // 将历史清空掉
        mKeyWordDao.queryBuilder()
                .where(KeyWordDao.Properties.Type.eq(KeyWord.TYPE_HISTORY))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }
}
