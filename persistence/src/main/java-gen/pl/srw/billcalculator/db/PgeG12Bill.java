package pl.srw.billcalculator.db;

import pl.srw.billcalculator.db.dao.DaoSession;
import de.greenrobot.dao.DaoException;

import pl.srw.billcalculator.db.dao.PgeG12BillDao;
import pl.srw.billcalculator.db.dao.PgePricesDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table PGE_G12_BILL.
 */
public class PgeG12Bill implements Bill {

    private Long id;
    private Integer readingDayFrom;
    private Integer readingDayTo;
    private Integer readingNightFrom;
    private Integer readingNightTo;
    private java.util.Date dateFrom;
    private java.util.Date dateTo;
    private Double amountToPay;
    private Long pricesId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient PgeG12BillDao myDao;

    private PgePrices pgePrices;
    private Long pgePrices__resolvedKey;


    public PgeG12Bill() {
    }

    public PgeG12Bill(Long id) {
        this.id = id;
    }

    public PgeG12Bill(Long id, Integer readingDayFrom, Integer readingDayTo, Integer readingNightFrom, Integer readingNightTo, java.util.Date dateFrom, java.util.Date dateTo, Double amountToPay, Long pricesId) {
        this.id = id;
        this.readingDayFrom = readingDayFrom;
        this.readingDayTo = readingDayTo;
        this.readingNightFrom = readingNightFrom;
        this.readingNightTo = readingNightTo;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.amountToPay = amountToPay;
        this.pricesId = pricesId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPgeG12BillDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getReadingDayFrom() {
        return readingDayFrom;
    }

    public void setReadingDayFrom(Integer readingDayFrom) {
        this.readingDayFrom = readingDayFrom;
    }

    public Integer getReadingDayTo() {
        return readingDayTo;
    }

    public void setReadingDayTo(Integer readingDayTo) {
        this.readingDayTo = readingDayTo;
    }

    public Integer getReadingNightFrom() {
        return readingNightFrom;
    }

    public void setReadingNightFrom(Integer readingNightFrom) {
        this.readingNightFrom = readingNightFrom;
    }

    public Integer getReadingNightTo() {
        return readingNightTo;
    }

    public void setReadingNightTo(Integer readingNightTo) {
        this.readingNightTo = readingNightTo;
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
