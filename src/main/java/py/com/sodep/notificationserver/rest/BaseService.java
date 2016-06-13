package py.com.sodep.notificationserver.rest;

import py.com.sodep.notificationserver.business.BaseBusiness;
import py.com.sodep.notificationserver.db.dao.util.Filter;
import py.com.sodep.notificationserver.exceptions.handlers.BusinessException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BaseService {

    private final String PAGE = "page";
    private final String PAGE_SIZE = "pageSize";
    private final String CREATED_AT_FIELD = "createdAt";
    private final String START_AFTER = "startAfter";
    private final String END_BEFORE = "endBefore";
    private final String SORT_ASCENDING = "sortAscending";
    private final String SORT_DESCENDING = "sortDescending";


    protected BaseBusiness<?> baseBusiness;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getApplicationById(@PathParam("id") Long id) throws BusinessException {

        Object o = baseBusiness.findById(id);
        return Response.ok(o).build();

    }

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findEvento(@Context UriInfo uriInfo) throws Exception {

        Response.ResponseBuilder builder = null;
        Integer page = null;
        Integer pageSize = null;
        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
        Iterator<String> iterator = queryParams.keySet().iterator();
        List<Filter> filters = new ArrayList<Filter>();
        while (iterator.hasNext()){
            String key = iterator.next();
            List<String> values = (ArrayList<String>) queryParams.get(key);
            for(String value : values){
                Filter f = new Filter();
                switch (key){
                    case START_AFTER:
                        f.setPath(CREATED_AT_FIELD);
                        f.setGreaterThan(value);
                        break;
                    case END_BEFORE:
                        f.setPath(CREATED_AT_FIELD);
                        f.setLessThan(value);
                        break;
                    case SORT_ASCENDING:
                        f.setPath(value);
                        f.setSortAscending(true);
                        break;
                    case SORT_DESCENDING:
                        f.setPath(value);
                        f.setSortDescending(true);
                        break;
                    case PAGE:
                        page = Integer.valueOf(value);
                        continue;
                    case PAGE_SIZE:
                        pageSize = Integer.valueOf(value);
                        continue;
                    default:
                        f.setPath(key);
                        f.setEquals(value);
                        break;
                }
                filters.add(f);
            }
        }

        List<?> objects = baseBusiness.getEntities(page, pageSize, filters);
        return Response.ok(objects).build();

    }

}
