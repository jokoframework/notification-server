/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.db.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Vanessa
 */
@Entity
@Table
@JsonAutoDetect
public class Parametro  implements Serializable {
    private static final long serialVersionUID = 6163667597446187934L;

    @Id
    @GeneratedValue
    @Column(name = "parametro_id")
    private Long id;

    @Column(name = "nombre", unique = true)
    private String nombre;
    @Column(name = "valor")
    private String valor;
    @Column(name = "tipo_dato")
    private String tipoDato;

    public Parametro() {
        //Default constructor
    }

    public Parametro(String name, String valor, String tipoDato) {
        this.nombre = name;
        this.valor = valor;
        this.tipoDato = tipoDato;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(String tipoDato) {
        this.tipoDato = tipoDato;
    }

    @Override
    public String toString() {
        return "Parametro{" + "id=" + id + ", name=" + nombre + ", valor=" + valor + ", tipoDato=" + tipoDato + '}';
    }

    
}
