package com.jesm3.newDualis.generatedDAO;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.jesm3.newDualis.generatedDAO.AbstractVorlesung;
import com.jesm3.newDualis.generatedDAO.AbstractMailContainer;
import com.jesm3.newDualis.generatedDAO.AbstractNote;

import com.jesm3.newDualis.generatedDAO.AbstractVorlesungDao;
import com.jesm3.newDualis.generatedDAO.AbstractMailContainerDao;
import com.jesm3.newDualis.generatedDAO.AbstractNoteDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig abstractVorlesungDaoConfig;
    private final DaoConfig abstractMailContainerDaoConfig;
    private final DaoConfig abstractNoteDaoConfig;

    private final AbstractVorlesungDao abstractVorlesungDao;
    private final AbstractMailContainerDao abstractMailContainerDao;
    private final AbstractNoteDao abstractNoteDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        abstractVorlesungDaoConfig = daoConfigMap.get(AbstractVorlesungDao.class).clone();
        abstractVorlesungDaoConfig.initIdentityScope(type);

        abstractMailContainerDaoConfig = daoConfigMap.get(AbstractMailContainerDao.class).clone();
        abstractMailContainerDaoConfig.initIdentityScope(type);

        abstractNoteDaoConfig = daoConfigMap.get(AbstractNoteDao.class).clone();
        abstractNoteDaoConfig.initIdentityScope(type);

        abstractVorlesungDao = new AbstractVorlesungDao(abstractVorlesungDaoConfig, this);
        abstractMailContainerDao = new AbstractMailContainerDao(abstractMailContainerDaoConfig, this);
        abstractNoteDao = new AbstractNoteDao(abstractNoteDaoConfig, this);

        registerDao(AbstractVorlesung.class, abstractVorlesungDao);
        registerDao(AbstractMailContainer.class, abstractMailContainerDao);
        registerDao(AbstractNote.class, abstractNoteDao);
    }
    
    public void clear() {
        abstractVorlesungDaoConfig.getIdentityScope().clear();
        abstractMailContainerDaoConfig.getIdentityScope().clear();
        abstractNoteDaoConfig.getIdentityScope().clear();
    }

    public AbstractVorlesungDao getAbstractVorlesungDao() {
        return abstractVorlesungDao;
    }

    public AbstractMailContainerDao getAbstractMailContainerDao() {
        return abstractMailContainerDao;
    }

    public AbstractNoteDao getAbstractNoteDao() {
        return abstractNoteDao;
    }

}
