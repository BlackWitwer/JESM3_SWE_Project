package com.jesm3.newDualis.generatedDAO;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.jesm3.newDualis.generatedDAO.AbstractNote;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table ABSTRACT_NOTE.
*/
public class AbstractNoteDao extends AbstractDao<AbstractNote, Long> {

    public static final String TABLENAME = "ABSTRACT_NOTE";

    /**
     * Properties of entity AbstractNote.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Titel = new Property(1, String.class, "titel", false, "TITEL");
        public final static Property Note = new Property(2, String.class, "note", false, "NOTE");
        public final static Property Credits = new Property(3, String.class, "credits", false, "CREDITS");
    };


    public AbstractNoteDao(DaoConfig config) {
        super(config);
    }
    
    public AbstractNoteDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ABSTRACT_NOTE' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'TITEL' TEXT," + // 1: titel
                "'NOTE' TEXT," + // 2: note
                "'CREDITS' TEXT);"); // 3: credits
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ABSTRACT_NOTE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, AbstractNote entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String titel = entity.getTitel();
        if (titel != null) {
            stmt.bindString(2, titel);
        }
 
        String note = entity.getNote();
        if (note != null) {
            stmt.bindString(3, note);
        }
 
        String credits = entity.getCredits();
        if (credits != null) {
            stmt.bindString(4, credits);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public AbstractNote readEntity(Cursor cursor, int offset) {
        AbstractNote entity = new AbstractNote( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // titel
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // note
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // credits
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, AbstractNote entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTitel(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setNote(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setCredits(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(AbstractNote entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(AbstractNote entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
