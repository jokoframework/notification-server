/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.db.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Arrays;
import javax.persistence.Transient;
import javax.ws.rs.FormParam;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

/**
 *
 * @author canetev
 */
public class AplicacionFile extends Aplicacion {

    @Transient
    @FormParam("certificadoDevFile")
    @PartType("application/octet-stream")
    private byte[] certificadoDevFile;

    @Transient
    @FormParam("certificadoProdFile")
    @PartType("application/octet-stream")
    private byte[] certificadoProdFile;

    @JsonIgnore(true)
    public byte[] getCertificadoDevFile() {
        return certificadoDevFile;
    }

    public void setCertificadoDevFile(byte[] certificadoDevFile) {
        this.certificadoDevFile = certificadoDevFile;
    }

    @JsonIgnore(true)
    public byte[] getCertificadoProdFile() {
        return certificadoProdFile;
    }

    public void setCertificadoProdFile(byte[] certificadoProdFile) {
        this.certificadoProdFile = certificadoProdFile;
    }

    public Aplicacion getAplicacion() {
        Aplicacion a = new Aplicacion();
        a.setId(this.getId());
        a.setApiKeyDev(this.getApiKeyDev());
        a.setApiKeyProd(this.getApiKeyProd());
        a.setCertificadoDev(this.getCertificadoDev());
        a.setCertificadoProd(this.getCertificadoProd());
        a.setNombre(this.getNombre());
        a.setKeyFileDev(this.getKeyFileDev());
        a.setKeyFileProd(this.getKeyFileProd());
        return a;
    }

    @Override
    public String toString() {
        return super.toString() + "AplicacionFile{" + "certificadoDevFile=" + Arrays.toString(certificadoDevFile) + ", certificadoProdFile=" + certificadoProdFile + '}';
    }

}
