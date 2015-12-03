/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.business;

import java.io.FileOutputStream;
import java.io.IOException;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Response;
import org.apache.commons.codec.binary.Base64;
import py.com.sodep.notificationserver.db.dao.AplicacionDao;
import py.com.sodep.notificationserver.db.dao.ParametroDao;
import py.com.sodep.notificationserver.db.entities.Aplicacion;

/**
 *
 * @author Vanessa
 */
@RequestScoped
public class AplicacionBusiness {

    public void newApplication(Aplicacion a) throws Exception {
        System.out.println("Recibido " + a);
        AplicacionDao applicationDao = new AplicacionDao();
        ParametroDao paramDao = new ParametroDao();
        String base = paramDao.getByName("PATH_CERTIFICADOS").getValor();
        System.out.println("ALMACENANDO EN: " + base);
        try {
            byte[] desarrollo = Base64.decodeBase64(a.getCertificadoDev().getBytes());
            byte[] produccion = Base64.decodeBase64(a.getCertificadoProd().getBytes());
            String fileNameDev = base + "/" + a.getNombre() + "-develop" + ".p12";
            String fileNameProd = base + "/" + a.getNombre() + "-production" + ".p12";
            try (FileOutputStream fos = new FileOutputStream(fileNameDev)) {
                fos.write(desarrollo);
                fos.close();
            } catch (IOException io) {
                throw new Exception("Error al guardar certificado de desarrollo: " + io.getMessage());
            }
            try (FileOutputStream fos = new FileOutputStream(fileNameProd)) {
                fos.write(produccion);
                fos.close();
            } catch (IOException io) {
                throw new Exception("Error al guardar certificado de produccion: " + io.getMessage());
            }
            a.setCertificadoDev(fileNameDev);
            a.setCertificadoProd(fileNameProd);
            System.out.println(a);
            applicationDao.save(a);
        } catch (Exception e) {
            throw new Exception("Error al crear aplicación, " + e.getMessage());
        }
    }

    public Aplicacion getApplication(String id) throws Exception {
        System.out.println("Recibido " + id);
        AplicacionDao applicationDao = new AplicacionDao();
        Object a = applicationDao.findById(Long.valueOf(id), Aplicacion.class);
        System.out.println("Application encontrado:" + a);
        return (Aplicacion) a;
    }
}
