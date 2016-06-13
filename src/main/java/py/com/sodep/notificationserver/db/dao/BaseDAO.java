/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.db.dao;

import org.hibernate.*;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import py.com.sodep.notificationserver.config.HibernateSessionLocal;
import org.apache.log4j.Logger;
import py.com.sodep.notificationserver.db.dao.util.Filter;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @param <T>
 * @author Vanessa
 */
public class BaseDAO<T> {

    private static final Logger LOGGER = Logger.getLogger(BaseDAO.class);
    private static final Integer PAGE_SIZE = 20;
    protected Class<T> entityClass;


    public BaseDAO() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
    }

    public Session getSession() {
        return HibernateSessionLocal.getSessionFactory().getCurrentSession();
    }

    public T create(T entity) throws HibernateException {
        try {
            getSession().beginTransaction();
            getSession().saveOrUpdate(entity);
            getSession().getTransaction().commit();
        } catch (HibernateException e) {
            if (getSession().getTransaction() != null) {
                LOGGER.error("ROLLBACK: " + entity);
                getSession().getTransaction().rollback();
            }
            throw e;
        }
        return entity;
    }

    public void save(T entity) {
        try {
            getSession().beginTransaction();
            getSession().persist(entity);
            getSession().getTransaction().commit();
        } catch (HibernateException e) {
            if (getSession().getTransaction() != null) {
                LOGGER.error("ROLLBACK: " + entity);
                getSession().getTransaction().rollback();
            }
            throw e;
        }
    }

    public T findById(long id) {
        LOGGER.info("Buscando: " + entityClass);
        getSession().beginTransaction();
        T result = (T) getSession().get(entityClass, id);
        if (result != null) {
            Hibernate.initialize(result);
            getSession().getTransaction().commit();
            return result;
        } else {
            throw new ObjectNotFoundException(id, entityClass.getName());
        }
    }

    public boolean update(T entity) {
        Transaction tx = getSession().beginTransaction();
        try {
            if (entity == null) {
                return false;
            }
            getSession().evict(entity);
            getSession().update(entity);
            tx.commit();
            return true;
        } catch (Exception e) {
            LOGGER.error("ROLLBACK: " + entity);
            tx.rollback();
            throw e;
        }
    }

    public boolean delete(T entity) {
        try {
            if (entity == null) {
                return false;
            }
            getSession().delete(entity);
            getSession().getTransaction().commit();
            return true;
        } catch (HibernateException e) {
            LOGGER.error("ROLLBACK: " + entity);
            getSession().getTransaction().rollback();
            throw e;
        }
    }

    public List<T> getPaged(Integer page, Integer pageSize) {
        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }
        if (page == null || page <= 0) {
            page = 1;
        }
        try {
            getSession().beginTransaction();
            List<T> a = (List<T>) getSession()
                    .createCriteria(entityClass)
                    .setFirstResult((page - 1) * pageSize).setMaxResults(pageSize).list();
            getSession().getTransaction().commit();
            return a;
        } catch (HibernateException e) {
            if (getSession().getTransaction() != null) {
                LOGGER.error("Error al obtener la lista de entidades paginada");
                getSession().getTransaction().rollback();
            }
            throw e;
        }

    }

    public List<T> getEntities(Integer page, Integer pageSize, List<Filter> filters) throws Exception {
        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }

        try {
            List<String> addedJoins = new ArrayList<String>();
            getSession().beginTransaction();
            Criteria criteria = getSession()
                    .createCriteria(entityClass, entityClass.getSimpleName());

            for (Filter f : filters) {

                String currentAlias = entityClass.getSimpleName();
                String[] splitted = f.getPath().split("\\.");
                Class<?> currentClass = entityClass;

                for (int i = 0; i < splitted.length; i++) {
                    if (i == (splitted.length - 1)) {
                        //Llegamos al final del path, agregamos la condicion al criteria
                        addCondition(criteria, splitted[i], currentAlias, currentClass, f);
                    } else {
                        //Si no llegamos al final, tenemos que hacer un join
                        currentClass = doJoin(criteria, splitted[i], currentAlias, currentClass, addedJoins);
                    }
                    //Actualizamos el alias para la siguiente iteracion
                    currentAlias = currentAlias + "_" + splitted[i];
                }

            }

            if (page != null) {
                if (page <= 0) {
                    page = 1;
                }
                criteria.setFirstResult((page - 1) * pageSize).setMaxResults(pageSize);
            }
            List<T> entities = criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY).list();

            getSession().getTransaction().commit();
            return entities;
        }catch(Exception e){
            if (getSession().getTransaction() != null) {
                LOGGER.error("Error al obtener la lista de entidades");
                getSession().getTransaction().rollback();
            }
            throw e;
        }


    }

    private void addCondition(Criteria criteria, String path, String currentAlias,
                              Class<?> currentClass, Filter filter) throws Exception {
        Class<?> fieldType = currentClass.getDeclaredField(path).getType();
        String finalAlias = currentAlias + "." + path;
        if (filter.getEquals() != null) {
            criteria.add(Restrictions.eq(finalAlias, parse(filter.getEquals(), fieldType)));
        }
        if (filter.getGreaterThan() != null) {
            criteria.add(Restrictions.gt(finalAlias, parse(filter.getGreaterThan(), fieldType)));
        }
        if (filter.getLessThan() != null) {
            criteria.add(Restrictions.lt(finalAlias, parse(filter.getLessThan(), fieldType)));
        }
        if (filter.getSortAscending()) {
            criteria.addOrder(Order.asc(finalAlias));
        }
        if (filter.getSortDescending()) {
            criteria.addOrder(Order.desc(finalAlias));
        }

    }

    private Object parse(String s, Class<?> type) throws Exception {
        Object arg = s;
        if (type.equals(Integer.class)) {
            arg = Integer.valueOf(s);
        } else if (type.equals(Long.class)) {
            arg = Long.valueOf(s);
        } else if (type.equals(Double.class)) {
            arg = Double.valueOf(s);
        } else if (type.equals(Float.class)) {
            arg = Float.valueOf(s);
        } else if (type.equals(Boolean.class)) {
            arg = Boolean.valueOf(s);
        } else if (type.equals(Date.class)) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat(
                        "yyyy-MM-dd");
                arg = formatter.parse(s);
            } catch (Exception e) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                arg = df.parse(s);
            }

        }
        return arg;
    }

    private Class<?> doJoin(Criteria criteria, String path, String currentAlias,
                            Class<?> currentClass, List<String> addedJoins) throws Exception {

        //Obtenemos el class del relacionado
        Field f = currentClass.getDeclaredField(path);
        Class<?> fieldType = null;
        try {
            fieldType = (Class<?>) ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0];
        } catch (Exception e) {
            fieldType = f.getType();
        }
        //Si no hicimos el join anteriormente, lo hacemos ahora
        String newAlias = currentAlias + "_" + path;
        if (!addedJoins.contains(newAlias)) {
            criteria.createAlias(currentAlias + "." + path, newAlias);
            addedJoins.add(newAlias);
        } else {
            LOGGER.info("No se hara doble join de: " + currentAlias + "." + path);
        }
        return fieldType;

    }


}
