/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.db.dao;

import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import py.com.sodep.notificationserver.db.entities.Aplicacion;
import py.com.sodep.notificationserver.db.entities.DeviceRegistration;
import py.com.sodep.notificationserver.exceptions.handlers.GlobalCodes;

/**
 *
 * @author Vanessa
 */
public class DeviceRegistrationDao extends BaseDAO<DeviceRegistration> {

    private static final Logger LOGGER = Logger.getLogger(DeviceRegistrationDao.class);

    public List<DeviceRegistration> getPendientesAndroid(Aplicacion aplicacion) {
        getSession().beginTransaction();
        List<DeviceRegistration> a = getSession()
                .createCriteria(DeviceRegistration.class)
                .add(Restrictions.like("estado", GlobalCodes.NUEVO))
                .add(Restrictions.like("tipo", GlobalCodes.ANDROID))
                .add(Restrictions.eq("aplicacion", aplicacion)).list();
        getSession().getTransaction().commit();
        return a;
    }
    
    public List<DeviceRegistration> getPendientesIos(Aplicacion aplicacion) {
        getSession().beginTransaction();
        List<DeviceRegistration> a = getSession()
                .createCriteria(DeviceRegistration.class)
                .add(Restrictions.like("estado", GlobalCodes.NUEVO))
                .add(Restrictions.like("tipo", GlobalCodes.IOS))
                .add(Restrictions.eq("aplicacion", aplicacion)).list();
        getSession().getTransaction().commit();
        return a;
    }

    public void setEstado(String estado, List<DeviceRegistration> list) {

        for (DeviceRegistration d : list) {
            try {
                d.setEstado(estado);
                create(d);
            } catch (Exception e) {
                LOGGER.error(e);
            }
        }

    }
}
