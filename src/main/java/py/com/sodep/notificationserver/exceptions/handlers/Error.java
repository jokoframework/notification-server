package py.com.sodep.notificationserver.exceptions.handlers;

import java.io.Serializable;

public class Error implements Serializable {

    private final String codigo;
    private final String mensaje;

    public Error(int codigo, String mensaje) {
        this(Integer.toString(codigo), mensaje);
    }

    public Error(String codigo, String mensaje) {
        this.codigo = codigo;
        this.mensaje = mensaje;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

    @Override
    public String toString() {
        return "Error{"
                + "codigo='" + codigo + '\''
                + ", mensaje='" + mensaje + '\''
                + '}';
    }
}
