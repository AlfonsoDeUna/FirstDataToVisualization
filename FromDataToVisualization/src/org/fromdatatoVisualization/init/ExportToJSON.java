package org.fromdatatoVisualization.init;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * 
 * @author AUB
 *
 */
class Datos {
	
	String titulo;
	int freq;
	
	public Datos (String tit, int f) {
		titulo = tit;
		freq = f;
	}
}

class DatosSerializables implements JsonSerializer<Datos>
{
	List<Datos> datosGenerales = new ArrayList<Datos> ();
	
	public DatosSerializables() {		
	}	
	
	@Override
	public JsonElement serialize(Datos arg0, Type arg1,
			JsonSerializationContext arg2) {
		
		return new Gson().toJsonTree(arg0);
	}
}

public class ExportToJSON {

	
	public static void main(String[] strings) {
								
		/** In this example we use the class and serialize class*/
		Datos datos = new Datos ("ti-1", 1);
		Datos datos2 = new Datos ("ti-2", 2);
		
		List<Datos> datosGenerales = new ArrayList<Datos> ();
		datosGenerales.add (datos);
		datosGenerales.add(datos2);
		
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Datos.class, new DatosSerializables());
		Gson gson = builder.create();
		System.out.println(gson.toJson(datosGenerales));				
		
	}	
}
