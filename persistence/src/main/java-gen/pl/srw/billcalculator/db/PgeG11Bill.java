package pl.srw.billcalculator.db;

import pl.srw.billcalculator.db.dao.DaoSession;
import de.greenrobot.dao.DaoException;

import pl.srw.billcalculator.db.dao.PgeG11BillDao;
import pl.srw.billcalculator.db.dao.PgePricesDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table PGE_G11_BILL.
 */
public class PgeG11Bill implements Bill {

    private Long id;
    private Integer readingFrom;
    private Integer readingTo;
    private java.util.Date dateFrom;
    private java.util.Date dateTo;
    private Double amountToPay;
    private Long pricesId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient PgeG11BillDao myDao;

    private PgePrices pgePrices;
    private Long pgePrices__resolvedKey;


    public PgeG11Bill() {
    }

    public PgeG11Bill(Long id) {
        this.id = id;
    }

    public PgeG11Bill(Long id, Integer readingFrom, Integer readingTo, java.util.Date dateFrom, java.util.Date dateTo, Double amountToPay, Long pricesId) {
        this.id = id;
        this.readingFrom = readingFrom;
        this.readingTo = readingTo;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.amountToPay = amountToPay;
        this.pricesId = pricesId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPgeG11BillDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getReadingFrom() {
        return readingFrom;
    }

    public void setReadingFrom(Integer readingFrom) {
        this.readingFrom = readingFrom;
    }

    public Integer getReadingTo() {
        return readingTo;
    }

    public void setReadingTo(Integer readingTo) {
        this.readingTo = readingTo;
    }

    public java.util.Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(java.util.Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public java.util.Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(java.util.Date dateTo) {
        this.dateTo = dateTo;
    }

    public Double getAmountToPay() {
        return amountToPay;
    }

    public void setAmountToPay(Double amountToPay) {
        this.amountToPay = amountToPay;
    }

    public Long getPricesId() {
        return pricesId;
    }

    public void setPricesId(Long pricesId) {
        this.pricesId = pricesId;
    }

    /** To-one relationship, resolved on first access. */
    public PgePrices getPgePrices() {
        Long __key = this.pricesId;
        if (pgePrices__resolvedKey == null || !pgePrices__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PgePricesDao targetDao = daoSession.getPgePricesDao();
            PgePrices pgePricesNew = targetDao.load(__key);
            synchronized (this) {
                pgePrices = pgePricesNew;
            	pgePrices__resolvedKey = __key;
            }
        }
        return pgePrices;
    }

    public void setPgePrices(PgePrices pgePrices) {
        synchronized (this) {
            this.pgePrices = pgePrices;
            pricesId = pgePrices == null ? null : pgePrices.getId();
            pgePrices__resolvedKey = pricesId;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}
