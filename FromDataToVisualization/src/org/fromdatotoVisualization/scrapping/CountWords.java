package org.fromdatotoVisualization.scrapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Analitics 
 * @author briofons
 *
 */
public class CountWords {
	
	Map <String, Integer> _array_dimensional = null;
	
	public CountWords () {
		_array_dimensional = new HashMap<String, Integer>();
	}
	
	/**
	 * find words and count the items in the map.
	 */
	public void count (String stemmWord) {
		
		Integer count = _array_dimensional.get(stemmWord);
		
		if (count == null) 
			_array_dimensional.put(stemmWord, 1);
		else 
			_array_dimensional.put(stemmWord, ++count);
	}
	
	/**
	 * return results
	 */
	
	public Map returnTenWordsOnHeap() {	
		
		HashMap<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		
		List<String> mapKeys = new ArrayList<String> (_array_dimensional.keySet());
		List<Integer> mapValues = new ArrayList<Integer> (_array_dimensional.values());
		
		Collections.sort(mapValues);
		
		Iterator<Integer> it =mapValues.iterator();
		while (it.hasNext()) {
			Integer value = it.next();
			Iterator<String> itKey = mapKeys.iterator();
			
			while (itKey.hasNext()) {
				String key = itKey.next();
				
				String comp1 = _array_dimensional.get(key).toString();
				String comp2 = value.toString();
				
				if (comp1.equals(comp2)) {
					_array_dimensional.remove(key);
					mapKeys.remove(key);
					
					sortedMap.put(key,value);
					break;
				}
			}
		}
		
		return sortedMap;
		
	}
	
}
