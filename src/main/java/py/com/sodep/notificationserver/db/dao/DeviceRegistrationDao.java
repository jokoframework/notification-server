/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.db.dao;

import java.util.List;
import org.hibernate.criterion.Restrictions;
import py.com.sodep.notificationserver.db.entities.Aplicacion;
import py.com.sodep.notificationserver.db.entities.DeviceRegistration;

/**
 *
 * @author Vanessa
 */
public class DeviceRegistrationDao extends BaseDAO<DeviceRegistration, Long> {

    public List<DeviceRegistration> getPendientes(Aplicacion aplicacion) {
        getSession().beginTransaction();
        List<DeviceRegistration> a = getSession()
                .createCriteria(DeviceRegistration.class)
                .add(Restrictions.like("estado", "NUEVO"))
                .add(Restrictions.eq("aplicacion", aplicacion)).list();
        getSession().getTransaction().commit();
        return a;
    }

    public void setEstado(String estado, List<DeviceRegistration> list) {
        
        for (DeviceRegistration d : list) {
            try {
                d.setEstado("CONSULTADO");
                create(d);
            } catch (Exception e) {
                log.error(e);
            }
        }
        
    }
}
