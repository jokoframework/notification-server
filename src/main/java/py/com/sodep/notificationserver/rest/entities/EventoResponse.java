/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.rest.entities;

import py.com.sodep.notificationserver.db.entities.Evento;

/**
 *
 * @author Vanessa
 */
public class EventoResponse {

    private Long id;
    private String estadoAndroid;
    private String estadoIos;

    public EventoResponse() {
    }

    public EventoResponse(Evento e) {
        this.id = e.getId();
        this.estadoAndroid = e.getEstadoAndroid();
        this.estadoIos = e.getEstadoIos();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEstadoAndroid() {
        return estadoAndroid;
    }

    public void setEstadoAndroid(String estadoAndroid) {
        this.estadoAndroid = estadoAndroid;
    }

    public String getEstadoIos() {
        return estadoIos;
    }

    public void setEstadoIos(String estadoIos) {
        this.estadoIos = estadoIos;
    }

    

}
