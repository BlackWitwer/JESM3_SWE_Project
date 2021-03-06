package com.jesm3.newDualis.generatedDAO;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.jesm3.newDualis.generatedDAO.AbstractMailContainer;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table ABSTRACT_MAIL_CONTAINER.
*/
public class AbstractMailContainerDao extends AbstractDao<AbstractMailContainer, Long> {

    public static final String TABLENAME = "ABSTRACT_MAIL_CONTAINER";

    /**
     * Properties of entity AbstractMailContainer.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property From = new Property(1, String.class, "from", false, "FROM");
        public final static Property FromComplete = new Property(2, String.class, "fromComplete", false, "FROM_COMPLETE");
        public final static Property To = new Property(3, String.class, "to", false, "TO");
        public final static Property Subject = new Property(4, String.class, "subject", false, "SUBJECT");
        public final static Property Text = new Property(5, String.class, "text", false, "TEXT");
        public final static Property Date = new Property(6, java.util.Date.class, "date", false, "DATE");
        public final static Property Attachment = new Property(7, Boolean.class, "attachment", false, "ATTACHMENT");
        public final static Property Seen = new Property(8, Boolean.class, "seen", false, "SEEN");
        public final static Property Html = new Property(9, Boolean.class, "html", false, "HTML");
        public final static Property MessageNumber = new Property(10, Integer.class, "messageNumber", false, "MESSAGE_NUMBER");
        public final static Property UId = new Property(11, Long.class, "uId", false, "U_ID");
    };


    public AbstractMailContainerDao(DaoConfig config) {
        super(config);
    }
    
    public AbstractMailContainerDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ABSTRACT_MAIL_CONTAINER' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'FROM' TEXT," + // 1: from
                "'FROM_COMPLETE' TEXT," + // 2: fromComplete
                "'TO' TEXT," + // 3: to
                "'SUBJECT' TEXT," + // 4: subject
                "'TEXT' TEXT," + // 5: text
                "'DATE' INTEGER," + // 6: date
                "'ATTACHMENT' INTEGER," + // 7: attachment
                "'SEEN' INTEGER," + // 8: seen
                "'HTML' INTEGER," + // 9: html
                "'MESSAGE_NUMBER' INTEGER," + // 10: messageNumber
                "'U_ID' INTEGER);"); // 11: uId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ABSTRACT_MAIL_CONTAINER'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, AbstractMailContainer entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String from = entity.getFrom();
        if (from != null) {
            stmt.bindString(2, from);
        }
 
        String fromComplete = entity.getFromComplete();
        if (fromComplete != null) {
            stmt.bindString(3, fromComplete);
        }
 
        String to = entity.getTo();
        if (to != null) {
            stmt.bindString(4, to);
        }
 
        String subject = entity.getSubject();
        if (subject != null) {
            stmt.bindString(5, subject);
        }
 
        String text = entity.getText();
        if (text != null) {
            stmt.bindString(6, text);
        }
 
        java.util.Date date = entity.getDate();
        if (date != null) {
            stmt.bindLong(7, date.getTime());
        }
 
        Boolean attachment = entity.getAttachment();
        if (attachment != null) {
            stmt.bindLong(8, attachment ? 1l: 0l);
        }
 
        Boolean seen = entity.getSeen();
        if (seen != null) {
            stmt.bindLong(9, seen ? 1l: 0l);
        }
 
        Boolean html = entity.getHtml();
        if (html != null) {
            stmt.bindLong(10, html ? 1l: 0l);
        }
 
        Integer messageNumber = entity.getMessageNumber();
        if (messageNumber != null) {
            stmt.bindLong(11, messageNumber);
        }
 
        Long uId = entity.getUId();
        if (uId != null) {
            stmt.bindLong(12, uId);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public AbstractMailContainer readEntity(Cursor cursor, int offset) {
        AbstractMailContainer entity = new AbstractMailContainer( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // from
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // fromComplete
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // to
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // subject
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // text
            cursor.isNull(offset + 6) ? null : new java.util.Date(cursor.getLong(offset + 6)), // date
            cursor.isNull(offset + 7) ? null : cursor.getShort(offset + 7) != 0, // attachment
            cursor.isNull(offset + 8) ? null : cursor.getShort(offset + 8) != 0, // seen
            cursor.isNull(offset + 9) ? null : cursor.getShort(offset + 9) != 0, // html
            cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10), // messageNumber
            cursor.isNull(offset + 11) ? null : cursor.getLong(offset + 11) // uId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, AbstractMailContainer entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setFrom(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setFromComplete(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setTo(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setSubject(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setText(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setDate(cursor.isNull(offset + 6) ? null : new java.util.Date(cursor.getLong(offset + 6)));
        entity.setAttachment(cursor.isNull(offset + 7) ? null : cursor.getShort(offset + 7) != 0);
        entity.setSeen(cursor.isNull(offset + 8) ? null : cursor.getShort(offset + 8) != 0);
        entity.setHtml(cursor.isNull(offset + 9) ? null : cursor.getShort(offset + 9) != 0);
        entity.setMessageNumber(cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10));
        entity.setUId(cursor.isNull(offset + 11) ? null : cursor.getLong(offset + 11));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(AbstractMailContainer entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(AbstractMailContainer entity) {
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
