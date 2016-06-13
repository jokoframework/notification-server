package py.com.sodep.notificationserver.business;


import py.com.sodep.notificationserver.config.GlobalCodes;
import py.com.sodep.notificationserver.db.dao.BaseDAO;
import py.com.sodep.notificationserver.db.dao.util.Filter;
import py.com.sodep.notificationserver.exceptions.handlers.BusinessException;

import java.util.List;

public class BaseBusiness<T> {

    protected BaseDAO<T> baseDAO;

    public T create(T entity) throws BusinessException {
        try {
            baseDAO.create(entity);
        } catch (Exception e) {
            throw new BusinessException(GlobalCodes.errors.DB_ERROR, e);
        }
        return entity;
    }

    public void save(T entity) throws BusinessException {
        try {
            baseDAO.save(entity);
        } catch (Exception e) {
            throw new BusinessException(GlobalCodes.errors.DB_ERROR, e);
        }
    }

    public T findById(long id) throws BusinessException {
        try {
            return baseDAO.findById(id);
        } catch (Exception e) {
            throw new BusinessException(GlobalCodes.errors.DB_ERROR, e);
        }
    }

    public boolean update(T entity) throws BusinessException {
        try {
            return baseDAO.update(entity);
        } catch (Exception e) {
            throw new BusinessException(GlobalCodes.errors.DB_ERROR, e);
        }
    }

    public boolean delete(T entity) throws BusinessException {
        try {
            return baseDAO.delete(entity);
        } catch (Exception e) {
            throw new BusinessException(GlobalCodes.errors.DB_ERROR, e);
        }
    }

    public List<T> getEntities(Integer page, Integer pageSize,
                               List<Filter> filters) throws BusinessException {
        try {
            return baseDAO.getEntities(page, pageSize, filters);
        } catch (Exception e) {
            throw new BusinessException(GlobalCodes.errors.DB_ERROR, e);
        }


    }

}
