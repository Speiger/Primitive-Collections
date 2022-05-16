package speiger.src.collections.chars.sets;

import java.util.Set;

import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.collections.chars.collections.CharSplititerator;
import speiger.src.collections.chars.utils.CharSets;
import speiger.src.collections.chars.utils.CharSplititerators;


/**
 * A Type Specific Set class to reduce boxing/unboxing
 */
public interface CharSet extends Set<Character>, CharCollection
{	
	@Override
	public CharIterator iterator();
	
	@Override
	public CharSet copy();
	
	/**
	 * A Type Specific remove function to reduce boxing/unboxing
	 * @param o the element that should be removed
	 * @return true if the element was removed
	 */
	public boolean remove(char o);
	
	@Override
	public default boolean remChar(char o) {
		return remove(o);
	}
	
	@Override
	@Deprecated
	public default boolean add(Character e) {
		return CharCollection.super.add(e);
	}
	
	@Override
	@Deprecated
	public default boolean contains(Object o) {
		return CharCollection.super.contains(o);
	}
	
	@Override
	@Deprecated
	public default boolean remove(Object o) {
		return CharCollection.super.remove(o);
	}
	
	/**
	 * Creates a Wrapped Set that is Synchronized
	 * @return a new Set that is synchronized
	 * @see CharSets#synchronize
	 */
	public default CharSet synchronize() { return CharSets.synchronize(this); }
	
	/**
	 * Creates a Wrapped Set that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Set Wrapper that is synchronized
	 * @see CharSets#synchronize
	 */
	public default CharSet synchronize(Object mutex) { return CharSets.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Set that is unmodifiable
	 * @return a new Set Wrapper that is unmodifiable
	 * @see CharSets#unmodifiable
	 */
	public default CharSet unmodifiable() { return CharSets.unmodifiable(this); }
	
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default CharSplititerator spliterator() { return CharSplititerators.createSplititerator(this, 0); }
}