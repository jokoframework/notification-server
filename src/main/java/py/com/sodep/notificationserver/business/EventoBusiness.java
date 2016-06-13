package py.com.sodep.notificationserver.business;


import org.apache.log4j.Logger;
import py.com.sodep.notificationserver.config.GlobalCodes;
import py.com.sodep.notificationserver.db.dao.EventoDao;
import py.com.sodep.notificationserver.db.entities.Evento;
import py.com.sodep.notificationserver.exceptions.handlers.BusinessException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class EventoBusiness extends BaseBusiness<Evento> {

    private static final Logger LOGGER = Logger.getLogger(EventoBusiness.class);

    @Inject
    EventoDao eventoDao;

    @PostConstruct
    private void init() {
        this.baseDAO = eventoDao;
    }

    public Evento cancelarEvento(Long id) throws BusinessException {
        try {
            Evento evento = this.findById(id);
            evento.setEstadoAndroid(GlobalCodes.SUSPENDIDO);
            return this.create(evento);
        } catch (Exception e) {
            throw new BusinessException(GlobalCodes.errors.UNKNOWN_ERROR, e);
        }


    }
}
