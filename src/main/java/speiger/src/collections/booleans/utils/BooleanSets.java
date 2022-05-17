package speiger.src.collections.booleans.utils;

import java.util.NoSuchElementException;
import speiger.src.collections.booleans.collections.BooleanIterator;
import speiger.src.collections.booleans.sets.AbstractBooleanSet;
import speiger.src.collections.booleans.sets.BooleanSet;
import speiger.src.collections.booleans.utils.BooleanCollections.EmptyCollection;

/**
 * A Helper class for sets
 */
public class BooleanSets
{
	/**
	 * Empty Set Variable
	 */
	private static final BooleanSet EMPTY = new EmptySet();
	
	/**
	 * EmptySet getter
	 * @return a EmptySet
	 */
	public static BooleanSet empty() {
		return EMPTY;
	}
	
	/**
	 * Creates a Singleton set of a given element
	 * @param element the element that should be converted into a singleton set
	 * @return a singletonset of the given element
	 */
	public static BooleanSet singleton(boolean element) {
		return new SingletonSet(element);
	}
	
	private static class SingletonSet extends AbstractBooleanSet
	{
		boolean element;
		
		SingletonSet(boolean element) {
			this.element = element;
		}
		
		@Override
		public boolean remove(boolean o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean add(boolean o) { throw new UnsupportedOperationException(); }
		@Override
		public BooleanIterator iterator()
		{
			return new BooleanIterator() {
				boolean next = true;
				@Override
				public boolean hasNext() { return next; }
				@Override
				public boolean nextBoolean() {
					if(!hasNext()) throw new NoSuchElementException();
					next = false;
					return element;
				}
			};
		}
		@Override
		public int size() { return 1; }
		
		@Override
		public SingletonSet copy() { return new SingletonSet(element); }
	}
	
	private static class EmptySet extends EmptyCollection implements BooleanSet
	{
		@Override
		public boolean remove(boolean o) { throw new UnsupportedOperationException(); }
		@Override
		public EmptySet copy() { return this; }
	}
	
}