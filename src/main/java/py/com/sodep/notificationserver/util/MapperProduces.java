package py.com.sodep.notificationserver.util;

import javax.enterprise.inject.Produces;

import com.fasterxml.jackson.databind.ObjectMapper;



public class MapperProduces {
	
	ObjectMapper mapper;
	
	@Produces
	public ObjectMapper get(){
		mapper = new ObjectMapper();
		return mapper;
	}
}
