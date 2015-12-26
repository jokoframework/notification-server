/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.business;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static javapns.Push.payload;
import javapns.devices.Device;
import javax.inject.Inject;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import py.com.sodep.notificationserver.db.dao.AplicacionDao;
import py.com.sodep.notificationserver.db.dao.DeviceRegistrationDao;
import py.com.sodep.notificationserver.db.dao.ParametroDao;
import py.com.sodep.notificationserver.db.entities.Aplicacion;
import py.com.sodep.notificationserver.db.entities.AplicacionFile;
import py.com.sodep.notificationserver.db.entities.DeviceRegistration;
import py.com.sodep.notificationserver.facade.ApnsFacade;

/**
 *
 * @author Vanessa
 */
public class AplicacionBusiness {

    @Inject
    AplicacionDao applicationDao;

    @Inject
    DeviceRegistrationDao deviceDao;

    @Inject
    ApnsFacade facade;

    @Inject
    Logger log;

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
        a.setEstadoAndroid(nuevo.getEstadoAndroid() == null ? "HABILITADA" : nuevo.getEstadoAndroid());
        a.setEstadoIos(nuevo.getEstadoIos() == null ? "HABILITADA" : nuevo.getEstadoIos());
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
        a.setEstadoAndroid(b.getEstadoAndroid() == null ? "HABILITADA" : b.getEstadoAndroid());
        a.setEstadoIos(b.getEstadoIos() == null ? "HABILITADA" : b.getEstadoIos());

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

    public Aplicacion getApplication(Long id) throws Exception {
        System.out.println("Recibido " + id);
        //AplicacionDao applicationDao = new AplicacionDao();
        Object a = applicationDao.findById(id, Aplicacion.class);
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

    public Aplicacion habilitarAplicacionAndroid(Long id) throws Exception {
        Aplicacion a = getApplication(id);
        a.setEstadoAndroid("HABILITADA");
        a.setError(null);
        applicationDao.create(a);
        return a;
    }

    public Aplicacion habilitarAplicacionIos(Long id) throws Exception {
        Aplicacion a = getApplication(id);
        a.setEstadoIos("HABILITADA");
        a.setError(null);
        applicationDao.create(a);
        return a;
    }

    public Aplicacion eliminarAplicacion(Long id) throws Exception {
        Aplicacion a = getApplication(id);
        applicationDao.delete(a);
        return a;
    }

    public List<DeviceRegistration> getListaRegIdInvalido(Long id) throws Exception {
        Aplicacion a = getApplication(id);
        List<DeviceRegistration> nuevos = deviceDao.getPendientes(a);
        deviceDao.setEstado("CONSULTADO", nuevos);
        return nuevos;
    }

    public List<DeviceRegistration> getListaIosRegIdInvalido(Long id) throws Exception {
        Aplicacion a = getApplication(id);
        File certificado = new File(a.getCertificadoProd());

        List<Device> lista = facade.getInactiveDevices(certificado, a.getKeyFileProd(), true);
        List<DeviceRegistration> nuevos = new ArrayList<>();
        for (Device d : lista) {
            try {
                nuevos.add(new DeviceRegistration(d.getToken(), null, "NUEVO", "Inactivo", a, "ELIMINAR"));
            } catch (Exception e) {
                log.error("Error al almacenar dispositivo inactivo IOS: " + e);
            }
        }
        return nuevos;
    }

}
