package py.com.sodep.notificationserver.exceptions.handlers;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * User: duartm
 * Date: 18/09/13
 * Time: 04:47 PM
 */
@XmlRootElement
public class Error implements Serializable {

    private static final long serialVersionUID = -1004168309004710157L;

    private String codigo;
    private String mensaje;

    public Error(int codigo, String mensaje) {
        this(codigo + "", mensaje);
    }

    public Error(String codigo, String mensaje) {
        this.codigo = codigo;
        this.mensaje = mensaje;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public String toString() {
        return "Error{" +
                "codigo='" + codigo + '\'' +
                ", mensaje='" + mensaje + '\'' +
                '}';
    }
}
