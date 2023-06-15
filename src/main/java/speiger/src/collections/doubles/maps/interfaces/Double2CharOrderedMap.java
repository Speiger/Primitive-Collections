package speiger.src.collections.doubles.maps.interfaces;

import speiger.src.collections.doubles.utils.maps.Double2CharMaps;
import speiger.src.collections.doubles.sets.DoubleOrderedSet;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
/**
 * A Special Map Interface giving Access to some really usefull functions
 * The Idea behind this interface is to allow access to functions that give control to the Order of elements.
 * Since Linked implementations as examples can be reordered outside of the Insertion Order.
 * This interface provides basic access to such functions while also providing some Sorted/NaivgableMap implementations that still fit into here.
 * 
 */
public interface Double2CharOrderedMap extends Double2CharMap
{
	/**
	 * A customized put method that allows you to insert into the first index.
	 * @param key the key that should be inserted
	 * @param value the value that should be inserted
	 * @return the previous present or default return value
	 * @see java.util.Map#put(Object, Object)
	 */
	public char putAndMoveToFirst(double key, char value);
	
	/**
	 * A customized put method that allows you to insert into the last index. (This may be nessesary depending on the implementation)
	 * @param key the key that should be inserted
	 * @param value the value that should be inserted
	 * @return the previous present or default return value
	 * @see java.util.Map#put(Object, Object)
	 */
	public char putAndMoveToLast(double key, char value);
	
	/**
	 * A specific move method to move a given key/value to the first index.
	 * @param key that should be moved to the first index
	 * @return true if the value was moved.
	 * @note returns false if the value was not present in the first place
	 */
	public boolean moveToFirst(double key);
	/**
	 * A specific move method to move a given key/value to the last index.
	 * @param key that should be moved to the first last
	 * @return true if the value was moved.
	 * @note returns false if the value was not present in the first place
	 */
	public boolean moveToLast(double key);
	
	/**
	 * A Specific get method that allows to move teh given key/value int the first index.
	 * @param key that is searched for
	 * @return the given value for the requested key or default return value
	 */
	public char getAndMoveToFirst(double key);
	/**
	 * A Specific get method that allows to move teh given key/value int the last index.
	 * @param key that is searched for
	 * @return the given value for the requested key or default return value
	 */
	public char getAndMoveToLast(double key);
	
	/**
	 * A method to get the first Key of a Map.
	 * @return the first key in the map
	 */
	public double firstDoubleKey();
	/**
	 * A method to get and remove the first Key of a Map.
	 * @return the first key in the map
	 */
	public double pollFirstDoubleKey();
	/**
	 * A method to get the last Key of a Map.
	 * @return the last key in the map
	 */
	public double lastDoubleKey();
	/**
	 * A method to get and remove the last Key of a Map.
	 * @return the last key in the map
	 */
	public double pollLastDoubleKey();
	
	/**
	 * A method to get the first Value of a Map.
	 * @return the first key in the map
	 */
	public char firstCharValue();
	/**
	 * A method to get the last Value of a Map.
	 * @return the last key in the map
	 */
	public char lastCharValue();
	
	@Override
	public Double2CharOrderedMap copy();
	@Override
	public DoubleOrderedSet keySet();
	@Override
	public ObjectOrderedSet<Double2CharMap.Entry> double2CharEntrySet();
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @return a new SortedMap that is synchronized
	 * @see Double2CharMaps#synchronize
	 */
	@Override
	public default Double2CharOrderedMap synchronize() { return Double2CharMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new SortedMap Wrapper that is synchronized
	 * @see Double2CharMaps#synchronize
	 */
	@Override
	public default Double2CharOrderedMap synchronize(Object mutex) { return Double2CharMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped SortedMap that is unmodifiable
	 * @return a new SortedMap Wrapper that is unmodifiable
	 * @see Double2CharMaps#unmodifiable
	 */
	@Override
	public default Double2CharOrderedMap unmodifiable() { return Double2CharMaps.unmodifiable(this); }
	
	/**
	 * Fast Ordered Entry Set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	interface FastOrderedSet extends Double2CharMap.FastEntrySet, ObjectOrderedSet<Double2CharMap.Entry> {
		@Override
		public ObjectBidirectionalIterator<Double2CharMap.Entry> fastIterator();
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @param fromElement that is going to be started from.
		 * @return a improved iterator that starts from the desired element
		 */
		public ObjectBidirectionalIterator<Double2CharMap.Entry> fastIterator(double fromElement);
	}
}