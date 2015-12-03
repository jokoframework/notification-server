/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.db.entities;

/**
 *
 * @author Vanessa
 */
import javax.ws.rs.FormParam;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

public class FileUploadForm {

    public FileUploadForm() {
    }

    @FormParam("uploadedFile")
    @PartType("application/octet-stream")
    private byte[] data;
    
    @FormParam("uploadedFile")
    @PartType("application/text")
    private String name;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "FileUploadForm{" + "data=" + data + ", name=" + name + '}';
    }

    
}
