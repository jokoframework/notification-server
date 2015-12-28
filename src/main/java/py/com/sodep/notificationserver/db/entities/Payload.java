package py.com.sodep.notificationserver.db.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class Payload implements Serializable {

    private static final long serialVersionUID = 6055960223584022815L;
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    private String clave;
    private String valor;
    
    @ManyToOne()
    @JoinColumn(name = "evento_id")
    @JsonIgnore
    private Evento evento;

    public Payload() {
        //Default constructor
    }

    public Payload(String clave, String valor) {
        this.clave = clave;
        this.valor = valor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    @Override
    public String toString() {
        return "Payload{" + "id=" + id + ", clave=" + clave + ", valor=" + valor + '}';
    }

}
