
import java.util.Iterator;
import java.util.LinkedList;

public class AssociationList<K,V> implements Map<K,V>{
	private LinkedList <Pair> pairsList;

	public class Pair<K,V>{
		private K key;
		private V value;
		public Pair(K key, V value){
			this.key = key;
			this.value = value;
		}
		public K getKey(){
			return this.key;
		}
		public V getValue(){
			return this.value;
		}
	}

	public AssociationList(){
		pairsList = new LinkedList();
	}
	
	public void add(K k, V v){
		pairsList.add(new Pair(k,v));
	}

	@Override
	public void put(K k, V v) {
		if(pairsList.isEmpty()){
			pairsList.add(new Pair(k,v));
		}
		else{
			Iterator <Pair> pairIt = pairsList.iterator();
			while(pairIt.hasNext()){
				if(pairIt.next().getKey() == k){
					pairIt.next().value = v;
				}
			}
		}
	}

	@Override
	public V remove(Object k) {
		Iterator <Pair> pairIt = pairsList.iterator();
		while(pairIt.hasNext()){
			Pair curr = pairIt.next();
			if(curr.getKey() == k){
				Object returned = curr.getValue();
				pairsList.remove(curr);
				return (V) returned;
			}
		}
		throw new IllegalArgumentException();
	}


	@Override
	public int size() {
		return pairsList.size();
	}

	@Override
	public boolean containsKey(Object k) {
		Iterator <Pair> pairIt = pairsList.iterator();
		while(pairIt.hasNext()){
			Pair curr = pairIt.next();
			if(curr.getValue() == k){
				return true;
			}
		}
		return false;
	}
	
	
	/*public V getValueByKey(Object k) {
		Iterator <Pair> pairIt = pairsList.iterator();
		while(pairIt.hasNext()){
			Pair curr = pairIt.next();
			if(curr.getKey() == k){
				return curr.getValue();
			}
		}
		return null;
	}*/
	

	@Override
	public Object get(Object k) {
		Iterator <Pair> pairIt = pairsList.iterator();
		while(pairIt.hasNext()){
			Pair curr = pairIt.next();
			if(curr.getValue() == k){
				return curr;
			}
		}
		throw new IllegalArgumentException();
	}

	@Override
	public LinkedList <K> keys() {
		LinkedList ret = new LinkedList();
		Iterator <Pair> pairIt = pairsList.iterator();
		while(pairIt.hasNext()){
			Pair curr = pairIt.next();
			ret.add(curr.getKey());
		}
		return ret;
	}

	@Override
	public LinkedList <V>values() {
		LinkedList ret = new LinkedList();
		Iterator <Pair> pairIt = pairsList.iterator();
		while(pairIt.hasNext()){
			Pair curr = pairIt.next();
			ret.add(curr.getValue());
		}
		return ret;
	}

}