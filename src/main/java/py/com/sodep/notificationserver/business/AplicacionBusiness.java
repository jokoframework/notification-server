package py.com.sodep.notificationserver.business;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javapns.devices.Device;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import py.com.sodep.notificationserver.db.dao.AplicacionDao;
import py.com.sodep.notificationserver.db.dao.DeviceRegistrationDao;
import py.com.sodep.notificationserver.db.dao.ParametroDao;
import py.com.sodep.notificationserver.db.entities.Aplicacion;
import py.com.sodep.notificationserver.db.entities.AplicacionFile;
import py.com.sodep.notificationserver.db.entities.DeviceRegistration;
import py.com.sodep.notificationserver.exceptions.handlers.BusinessException;
import py.com.sodep.notificationserver.config.GlobalCodes;
import py.com.sodep.notificationserver.facade.ApnsFacade;

/**
 *
 * @author Vanessa
 */
public class AplicacionBusiness extends BaseBusiness<Aplicacion> {

    private static final Logger LOGGER = Logger.getLogger(AplicacionBusiness.class);

    @Inject
    AplicacionDao applicationDao;

    @Inject
    ParametroDao paramDao;

    @Inject
    DeviceRegistrationDao deviceDao;

    @Inject
    ApnsFacade facade;

    @PostConstruct
    private void init(){
        this.baseDAO = applicationDao;
    }

    public Aplicacion createAplicacionJson(Aplicacion nuevo, Long id) throws BusinessException {
        Aplicacion a = null;
        try {
            if (id != null) {
                a = applicationDao.findById(id);
            } else {
                a = nuevo;
            }

            a.setNombre(nuevo.getNombre());
            a.setApiKeyDev(nuevo.getApiKeyDev());
            a.setApiKeyProd(nuevo.getApiKeyProd());
            a.setKeyFileDev(nuevo.getKeyFileDev());
            a.setKeyFileProd(nuevo.getKeyFileProd());
            a.setEstadoAndroid(nuevo.getEstadoAndroid() == null ? GlobalCodes.HABILITADA : nuevo.getEstadoAndroid());
            a.setEstadoIos(nuevo.getEstadoIos() == null ? GlobalCodes.HABILITADA : nuevo.getEstadoIos());
            String base = paramDao.getByName("PATH_CERTIFICADOS").getValor();
            if (nuevo.getCertificadoDev() != null) {
                a.setCertificadoDev(writeFile(base, nuevo.getNombre(), nuevo.getCertificadoDev(), "develop"));
            }
            if (nuevo.getCertificadoProd() != null) {
                a.setCertificadoProd(writeFile(base, nuevo.getNombre(), nuevo.getCertificadoProd(), "production"));
            }
            if (a.isIos()) {
                a.setPayloadSize(2048);
            } else {
                a.setPayloadSize(4096);
            }
            a = applicationDao.create(a);
        } catch (HibernateException | SQLException ex) {
            throw new BusinessException(GlobalCodes.errors.DB_ERROR, ex);
        }
        return a;
    }

    public String writeFile(String base, String nombre, String certificado, String type) throws BusinessException {
        String fileName = base + "/" + nombre + "-" + type + ".p12";
        try {
            byte[] file = Base64.decodeBase64(certificado.getBytes());
            FileOutputStream fos = new FileOutputStream(fileName);
            fos.write(file);
            fos.close();
        } catch (FileNotFoundException e) {
            throw new BusinessException(GlobalCodes.errors.APP_PATH_NOT_FOUND, e);
        } catch (IOException ex) {
            throw new BusinessException(GlobalCodes.errors.APP_PATH_ERROR, ex);
        }
        return fileName;
    }

    public Aplicacion newAplicacionFileUpload(AplicacionFile b, Long id) throws BusinessException {

        try {
            Aplicacion a;
            if (id != null) {
                a = applicationDao.findById(id);
            } else {
                a = b.getAplicacion();
            }
            a.setApiKeyDev(b.getApiKeyDev());
            a.setApiKeyProd(b.getApiKeyProd());
            a.setKeyFileDev(b.getKeyFileDev());
            a.setKeyFileProd(b.getKeyFileProd());
            a.setNombre(b.getNombre());
            a.setEstadoAndroid(b.getEstadoAndroid() == null ? GlobalCodes.HABILITADA : b.getEstadoAndroid());
            a.setEstadoIos(b.getEstadoIos() == null ? GlobalCodes.HABILITADA : b.getEstadoIos());


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
            if (a.isIos()) {
                a.setPayloadSize(2048);
            } else {
                a.setPayloadSize(4096);
            }
            applicationDao.create(a);

            return a;
        } catch (HibernateException | SQLException ex) {
            throw new BusinessException(GlobalCodes.errors.DB_ERROR, ex);
        }
    }

    public void writeFile(String fileName, byte[] data) throws BusinessException {
        if (data != null) {
            try {
                FileOutputStream fos = new FileOutputStream(fileName);
                fos.write(data);
                fos.close();
            } catch (IOException io) {
                LOGGER.error("Error al crear archivo: ", io);
                throw new BusinessException(GlobalCodes.errors.APP_PATH_ERROR, "Error al guardar certificado: " + io.getMessage());
            }
        } else {
            throw new BusinessException(GlobalCodes.errors.APP_EMPTY_FILE, "El archivo es null.");
        }
    }

    public Aplicacion getApplication(Long id) throws BusinessException {
        Object a = applicationDao.findById(id);
        return (Aplicacion) a;
    }

    public Aplicacion findAplicacion(String nombre) {
        Object a = applicationDao.getByName(nombre);
        return (Aplicacion) a;
    }

    public Aplicacion habilitarAplicacionAndroid(Long id) throws BusinessException {
        try {
            Aplicacion a = getApplication(id);
            a.setEstadoAndroid(GlobalCodes.HABILITADA);
            a.setError(null);
            applicationDao.create(a);
            return a;
        } catch (HibernateException | SQLException ex) {
            LOGGER.error(ex);
        }
        return null;
    }

    public Aplicacion habilitarAplicacionIos(Long id) throws BusinessException {
        try {
            Aplicacion a = getApplication(id);
            a.setEstadoIos(GlobalCodes.HABILITADA);
            a.setError(null);
            applicationDao.create(a);
            return a;
        } catch (HibernateException | SQLException ex) {
            throw new BusinessException(GlobalCodes.errors.DB_ERROR, ex);
        }
    }

    public Aplicacion eliminarAplicacion(Long id) throws BusinessException {
        try {
            Aplicacion a = getApplication(id);
            applicationDao.delete(a);
            return a;
        } catch (HibernateException ex) {
            throw new BusinessException(GlobalCodes.errors.DB_ERROR, ex);
        }
    }

    public List<DeviceRegistration> getListaRegIdInvalido(Long id) throws BusinessException {
        Aplicacion a = getApplication(id);
        List<DeviceRegistration> nuevos = deviceDao.getPendientesAndroid(a);
        deviceDao.setEstado(GlobalCodes.CONSULTADO, nuevos);
        return nuevos;
    }

    public List<DeviceRegistration> getListaIosRegIdInvalido(Long id) throws BusinessException {
        Aplicacion a = getApplication(id);
        File certificado = new File(a.getCertificadoProd());

        List<Device> lista = facade.getInactiveDevices(certificado, a.getKeyFileProd(), true);
        List<DeviceRegistration> nuevos = new ArrayList<>();
        for (Device d : lista) {
            try {
                nuevos.add(new DeviceRegistration(d.getToken(), null, GlobalCodes.NUEVO, "Inactivo", a, GlobalCodes.ELIMINAR, GlobalCodes.IOS));
            } catch (Exception e) {
                LOGGER.error("Error al almacenar dispositivo inactivo IOS: " + e);
            }
        }
        return nuevos;
    }

}
