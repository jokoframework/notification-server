package py.com.sodep.notificationserver.rest;

import org.apache.log4j.Logger;
import py.com.sodep.notificationserver.business.EventoBusiness;
import py.com.sodep.notificationserver.db.dao.util.Filter;
import py.com.sodep.notificationserver.db.entities.Aplicacion;
import py.com.sodep.notificationserver.db.entities.Evento;
import py.com.sodep.notificationserver.exceptions.handlers.BusinessException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Path("/evento")
@RequestScoped
public class EventoService extends BaseService {

    private static final Logger LOGGER = Logger.getLogger(AplicacionService.class);

    @Inject
    EventoBusiness eventoBusiness;

    @PostConstruct
    private void init(){
        this.baseBusiness = eventoBusiness;
    }

    @POST
    @Path("/{id}/cancelar/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response cancelarEvento(@PathParam(value = "id") Long id) throws BusinessException {
        Evento cancelado = eventoBusiness.cancelarEvento(id);
        return Response.ok(cancelado).build();
    }


//    @GET
//    @Path("/{id}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getApplicationById(@PathParam("id") Long id) throws BusinessException {
//        LOGGER.info("Evento/id " + id);
//        Evento e = eventoBusiness.findById(id);
//        return Response.ok(e).build();
//
//    }
//
//    @GET
//    @Path("")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response findEvento(@Context UriInfo uriInfo) throws Exception {
//
//        Response.ResponseBuilder builder = null;
//        Integer page = null;
//        Integer pageSize = null;
//        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
//        Iterator<String> iterator = queryParams.keySet().iterator();
//        List<Filter> filters = new ArrayList<Filter>();
//        while (iterator.hasNext()){
//            String key = iterator.next();
//            List<String> values = (ArrayList<String>) queryParams.get(key);
//            for(String value : values){
//                Filter f = new Filter();
//                switch (key){
//                    case "startAfter":
//                        f.setPath("createdAt");
//                        f.setGreaterThan(value);
//                        break;
//                    case "endBefore":
//                        f.setPath("createdAt");
//                        f.setLessThan(value);
//                        break;
//                    case "sortAscending":
//                        f.setPath(value);
//                        f.setSortAscending(true);
//                        break;
//                    case "sortDescending":
//                        f.setPath(value);
//                        f.setSortDescending(true);
//                        break;
//                    case "page":
//                        page = Integer.valueOf(value);
//                        continue;
//                    case "pageSize":
//                        pageSize = Integer.valueOf(value);
//                        continue;
//                    default:
//                        f.setPath(key);
//                        f.setEquals(value);
//                        break;
//                }
//                filters.add(f);
//            }
//        }
//
//        List<Evento> eventos = eventoBusiness.getEntities(page, pageSize, filters);
//        return Response.ok(eventos).build();
//
//    }

}
