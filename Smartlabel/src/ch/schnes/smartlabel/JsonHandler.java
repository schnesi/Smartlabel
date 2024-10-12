package ch.schnes.smartlabel;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class to serialize Maps and deserialize to Maps.
 * Note: For multidimensional JSON objects, the inner objects must be deserialized separately.
 * 
 */
public class JsonHandler {
	private static final ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * Convert Map to String.
	 * @param object
	 * @return String
	 * @throws JsonProcessingException
	 */
	public static String serialize(Map<String, Object> object) throws JsonProcessingException {
		return mapper.writeValueAsString(object);
	}
	
	/**
	 * Convert String to Map.
	 * @param jsonString
	 * @return Map
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	public static Map<String, Object> deserialize(String jsonString) throws JsonMappingException, JsonProcessingException {
		return mapper.readValue(jsonString, Map.class);
	}
}
