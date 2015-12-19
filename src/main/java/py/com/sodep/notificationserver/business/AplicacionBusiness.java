/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.business;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import javax.inject.Inject;
import org.apache.commons.codec.binary.Base64;
import py.com.sodep.notificationserver.db.dao.AplicacionDao;
import py.com.sodep.notificationserver.db.dao.ParametroDao;
import py.com.sodep.notificationserver.db.entities.Aplicacion;
import py.com.sodep.notificationserver.db.entities.AplicacionFile;

/**
 *
 * @author Vanessa
 */
public class AplicacionBusiness {

    @Inject
    AplicacionDao applicationDao;

    public Aplicacion createAplicacionJson(Aplicacion nuevo, Long id) throws Exception {
        System.out.println("Recibido " + nuevo);
        Aplicacion a;
        //AplicacionDao applicationDao = new AplicacionDao();
        ParametroDao paramDao = new ParametroDao();
        if (id != null) {
            a = applicationDao.findById(id, Aplicacion.class);
        } else {
            a = nuevo;
        }
        a.setNombre(nuevo.getNombre());
        a.setApiKeyDev(nuevo.getApiKeyDev());
        a.setApiKeyProd(nuevo.getApiKeyProd());
        a.setKeyFileDev(nuevo.getKeyFileDev());
        a.setKeyFileProd(nuevo.getKeyFileProd());

        String base = paramDao.getByName("PATH_CERTIFICADOS").getValor();
        System.out.println("ALMACENANDO EN: " + base);
        try {
            if (nuevo.getCertificadoDev() != null) {
                byte[] desarrollo = Base64.decodeBase64(nuevo.getCertificadoDev().getBytes());
                String fileNameDev = base + "/" + nuevo.getNombre() + "-develop" + ".p12";
                try (FileOutputStream fos = new FileOutputStream(fileNameDev)) {
                    fos.write(desarrollo);
                    fos.close();
                    a.setCertificadoDev(fileNameDev);
                    System.out.println("CERTIFICADO - DEV: " + a.getCertificadoDev());
                } catch (IOException io) {
                    throw new Exception("Error al guardar certificado de desarrollo: " + io.getMessage());
                }
            }
            if (nuevo.getCertificadoProd() != null) {
                String fileNameProd = base + "/" + nuevo.getNombre() + "-production" + ".p12";
                byte[] produccion = Base64.decodeBase64(nuevo.getCertificadoProd().getBytes());
                try (FileOutputStream fos = new FileOutputStream(fileNameProd)) {
                    fos.write(produccion);
                    fos.close();
                    a.setCertificadoProd(fileNameProd);
                    System.out.println("CERTIFICADO - PROD: " + a.getCertificadoProd());
                } catch (IOException io) {
                    throw new Exception("Error al guardar certificado de produccion: " + io.getMessage());
                }
            }
            if (a.getApiKeyDev() != null || a.getApiKeyProd() != null) {
                a.setPayloadSize(4096);
            }
            if (a.getCertificadoDev() != null || a.getCertificadoProd() != null) {
                a.setPayloadSize(2048);
            }

            System.out.println("ALMACENANDO: " + a);
            a = applicationDao.create(a);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error al crear aplicación, " + e.getMessage());
        }
        return a;
    }

    public Aplicacion newAplicacionFileUpload(AplicacionFile b, Long id) throws SQLException, Exception {
        Aplicacion a;
        //AplicacionDao applicationDao = new AplicacionDao();
        if (applicationDao != null) {
            System.out.println("**************** DAO NO ES NULL ****************");
        }
        ParametroDao paramDao = new ParametroDao();
        if (id != null) {
            a = applicationDao.findById(id, Aplicacion.class);
        } else {
            a = b.getAplicacion();
        }
        a.setApiKeyDev(b.getApiKeyDev());
        a.setApiKeyProd(b.getApiKeyProd());
        a.setKeyFileDev(b.getKeyFileDev());
        a.setKeyFileProd(b.getKeyFileProd());
        a.setNombre(b.getNombre());
        if (a.getApiKeyDev() != null || a.getApiKeyProd() != null) {
            a.setPayloadSize(4096);
        }
        if (a.getCertificadoDev() != null || a.getCertificadoProd() != null) {
            a.setPayloadSize(2048);
        }
        String base = paramDao.getByName("PATH_CERTIFICADOS").getValor();
        
        if (b.getCertificadoDevFile() != null) {
            String fileNameDev = base + "/" + b.getNombre() + "-develop" + ".p12";
            writeFile(fileNameDev, b.getCertificadoDevFile());
            a.setCertificadoDev(fileNameDev);
        }
        if (b.getCertificadoProdFile() != null) {
            String fileNameProd = base + "/" + b.getNombre() + "-production" + ".p12";
            writeFile(fileNameProd, b.getCertificadoProdFile());
            a.setCertificadoProd(fileNameProd);
        }
        if (a.getApiKeyDev() != null || a.getApiKeyProd() != null) {
            a.setPayloadSize(4096);
        }
        if (a.getCertificadoDev() != null || a.getCertificadoProd() != null) {
            a.setPayloadSize(2048);
        }
        System.out.println("CREANDO: " + a);
        applicationDao.create(a);

        return a;
    }

    public void writeFile(String fileName, byte[] data) throws Exception {
        if (data != null) {
            try (FileOutputStream fos = new FileOutputStream(fileName)) {
                fos.write(data);
                fos.close();
            } catch (IOException io) {
                throw new Exception("Error al guardar certificado: " + io.getMessage());
            }
        } else {
            throw new Exception("El archivo es null.");
        }
    }

    public Aplicacion getApplication(String id) throws Exception {
        System.out.println("Recibido " + id);
        //AplicacionDao applicationDao = new AplicacionDao();
        Object a = applicationDao.findById(Long.valueOf(id), Aplicacion.class);
        System.out.println("Application encontrado:" + a);
        return (Aplicacion) a;
    }

    public Aplicacion findAplicacion(String nombre) throws Exception {
        System.out.println("Buscando " + nombre);
        //AplicacionDao applicationDao = new AplicacionDao();
        Object a = applicationDao.getByName(nombre);
        System.out.println("Application encontrado:" + a);
        return (Aplicacion) a;
    }
}
