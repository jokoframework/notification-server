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
import py.com.sodep.notificationserver.db.dao.ApplicationDao;
import py.com.sodep.notificationserver.db.dao.ParametroDao;
import py.com.sodep.notificationserver.db.entities.Application;

/**
 *
 * @author Vanessa
 */
@RequestScoped
public class ApplicationBusiness {

    public void newApplication(Application a) throws Exception {
        System.out.println("Recibido " + a);
        ApplicationDao applicationDao = new ApplicationDao();
        ParametroDao paramDao = new ParametroDao();
        String base = paramDao.getByName("PATH_CERTIFICADOS").getValor();
        System.out.println("ALMACENANDO EN: " + base);
        try {
            byte[] desarrollo = Base64.decodeBase64(a.getCertificadoDev().getBytes());
            byte[] produccion = Base64.decodeBase64(a.getCertificadoProd().getBytes());
            System.out.println("Decoded value is " + new String(desarrollo));
            String fileNameDev = base + "/" + a.getName() + "-develop" + ".p12";
            String fileNameProd = base + "/" + a.getName() + "-production" + ".p12";
            try (FileOutputStream fos = new FileOutputStream(fileNameDev)) {
                fos.write(desarrollo);
                fos.close();
            } catch (IOException io) {
                throw new Exception("Error al guardar certificado de desarrollo: " + io.getMessage());
            }
            /*try (FileOutputStream fos = new FileOutputStream(fileNameProd)) {
                fos.write(produccion);
                fos.close();
            } catch (IOException io) {
                throw new Exception("Error al guardar certificado de produccion: " + io.getMessage());
            }*/
            a.setCertificadoDev(fileNameDev);
            a.setCertificadoDev(fileNameProd);
            applicationDao.save(a);
        } catch (Exception e) {
            throw new Exception("Error al crear aplicación, " + e.getMessage());
        }
    }

    public Response getApplication(String id) {
        System.out.println("Recibido " + id);
        ApplicationDao applicationDao = new ApplicationDao();
        Object a = applicationDao.findById(Long.valueOf(id), Application.class);
        System.out.println("Application encontrado:" + a);
        return Response.ok(a).build();
    }
}
