package com.jesm3.newDualis.generatedDAO;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.jesm3.newDualis.generatedDAO.AbstractVorlesung;

import com.jesm3.newDualis.generatedDAO.AbstractVorlesungDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig abstractVorlesungDaoConfig;

    private final AbstractVorlesungDao abstractVorlesungDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        abstractVorlesungDaoConfig = daoConfigMap.get(AbstractVorlesungDao.class).clone();
        abstractVorlesungDaoConfig.initIdentityScope(type);

        abstractVorlesungDao = new AbstractVorlesungDao(abstractVorlesungDaoConfig, this);

        registerDao(AbstractVorlesung.class, abstractVorlesungDao);
    }
    
    public void clear() {
        abstractVorlesungDaoConfig.getIdentityScope().clear();
    }

    public AbstractVorlesungDao getAbstractVorlesungDao() {
        return abstractVorlesungDao;
    }

}