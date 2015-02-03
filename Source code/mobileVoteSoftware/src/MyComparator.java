import java.util.Comparator;
import java.util.Map;


public class MyComparator implements Comparator<Object> {

	@Override
	public int compare(Object obj1, Object obj2) {
		
		int result=0;
		
		@SuppressWarnings("unchecked")
		Map.Entry<Integer, Integer> e1 = (Map.Entry<Integer, Integer>)obj1 ;
		@SuppressWarnings("unchecked")
		Map.Entry<Integer, Integer> e2 = (Map.Entry<Integer, Integer>)obj2 ;//Sort based on values.

		Integer value1 = (Integer)e1.getValue();
		Integer value2 = (Integer)e2.getValue();

		if(value1.compareTo(value2)==0){

			Integer key1=(Integer)e1.getKey();
			Integer key2=(Integer)e2.getKey();

			//Sort String in an alphabetical order
			result=key1.compareTo(key2);

		} else{
			//Sort values in a descending order
			result=value2.compareTo( value1 );
		}

		return result;
	}

}
