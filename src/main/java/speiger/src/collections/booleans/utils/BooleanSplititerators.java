package speiger.src.collections.booleans.utils;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.function.Consumer;

import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.collections.BooleanIterator;
import speiger.src.collections.booleans.collections.BooleanSplititerator;
import speiger.src.collections.booleans.functions.BooleanConsumer;
import speiger.src.collections.utils.SanityChecks;

/**
 * Helper class that provides SplitIterators for normal and stream usage
 */
public class BooleanSplititerators
{
	/**
	* Creates a Type Specific SplitIterator to reduce boxing/unboxing
	* @param array that should be wrapped into a split iterator
	* @param characteristics characteristics properties of this spliterator's  source or elements.
	* @return a Type Specific SplitIterator
	*/
	public static BooleanSplititerator createArraySplititerator(boolean[] array, int characteristics) { return createArraySplititerator(array, 0, array.length, characteristics);}
	/**
	* Creates a Type Specific SplitIterator to reduce boxing/unboxing
	* @param array that should be wrapped into a split iterator
	* @param size the maximum index within the array
	* @param characteristics characteristics properties of this spliterator's  source or elements.
	* @return a Type Specific SplitIterator
	* @throws IllegalStateException if the size is outside of the array size
	*/
	public static BooleanSplititerator createArraySplititerator(boolean[] array, int size, int characteristics) { return createArraySplititerator(array, 0, size, characteristics);}
	/**
	* Creates a Type Specific SplitIterator to reduce boxing/unboxing
	* @param array that should be wrapped into a split iterator
	* @param offset the starting index of the array
	* @param size the maximum index within the array
	* @param characteristics characteristics properties of this spliterator's  source or elements.
	* @return a Type Specific SplitIterator
	* @throws IllegalStateException the offset and size are outside of the arrays range
	*/
	public static BooleanSplititerator createArraySplititerator(boolean[] array, int offset, int size, int characteristics) {
		SanityChecks.checkArrayCapacity(array.length, offset, size);
		return new TypeArraySplitIterator(array, offset, size, characteristics);
	}
	
	/**
	* Creates a Type Specific SplitIterator to reduce boxing/unboxing
	* @param collection the collection that should be wrapped in a split iterator
	* @param characteristics characteristics properties of this spliterator's  source or elements.
	* @return a Type Specific SplitIterator
	*/
	public static BooleanSplititerator createSplititerator(BooleanCollection collection, int characteristics) {
		return new TypeIteratorSpliterator(collection, characteristics);
	}
	
	/**
	* Creates a Type Specific SplitIterator to reduce boxing/unboxing
	* @param iterator the Iterator that should be wrapped in a split iterator
	* @param characteristics characteristics properties of this spliterator's source or elements.
	* @return a Type Specific SplitIterator
	*/
	public static BooleanSplititerator createUnknownSplititerator(BooleanIterator iterator, int characteristics) {
		return new TypeIteratorSpliterator(iterator, characteristics);
	}
	
	/**
	* Creates a Type Specific SplitIterator to reduce boxing/unboxing
	* @param iterator the collection that should be wrapped in a split iterator
	* @param size the amount of elements in the iterator
	* @param characteristics characteristics properties of this spliterator's  source or elements.
	* @return a Type Specific SplitIterator
	*/
	public static BooleanSplititerator createSizedSplititerator(BooleanIterator iterator, long size, int characteristics) {
		return new TypeIteratorSpliterator(iterator, size, characteristics);
	}
	
	static class TypeIteratorSpliterator implements BooleanSplititerator {
		static final int BATCH_UNIT = 1 << 10;
		static final int MAX_BATCH = 1 << 25;
		private final BooleanCollection collection;
		private BooleanIterator it;
		private final int characteristics;
		private long est;
		private int batch;
	
		TypeIteratorSpliterator(BooleanCollection collection, int characteristics) {
			this.collection = collection;
			it = null;
			this.characteristics = (characteristics & Spliterator.CONCURRENT) == 0
								? characteristics | Spliterator.SIZED | Spliterator.SUBSIZED
								: characteristics;
		}
	
		TypeIteratorSpliterator(BooleanIterator iterator, long size, int characteristics) {
			collection = null;
			it = iterator;
			est = size;
			this.characteristics = (characteristics & Spliterator.CONCURRENT) == 0
								? characteristics | Spliterator.SIZED | Spliterator.SUBSIZED
								: characteristics;
		}
		
		TypeIteratorSpliterator(BooleanIterator iterator, int characteristics) {
			collection = null;
			it = iterator;
			est = Long.MAX_VALUE;
			this.characteristics = characteristics & ~(Spliterator.SIZED | Spliterator.SUBSIZED);
		}
		
		private BooleanIterator iterator()
		{
			if (it == null) {
				it = collection.iterator();
				est = collection.size();
			}
			return it;
		}
		
		@Override
		public BooleanSplititerator trySplit() {
			BooleanIterator i = iterator();
			if (est > 1 && i.hasNext()) {
				int n = Math.min(batch + BATCH_UNIT, Math.min((int)est, MAX_BATCH));
				boolean[] a = new boolean[n];
				int j = 0;
				do { a[j] = i.nextBoolean(); } while (++j < n && i.hasNext());
				batch = j;
				if (est != Long.MAX_VALUE)
					est -= j;
				return new TypeArraySplitIterator(a, 0, j, characteristics);
			}
			return null;
		}
		
		@Override
		public void forEachRemaining(BooleanConsumer action) {
			if (action == null) throw new NullPointerException();
			iterator().forEachRemaining(action);
		}
		
		@Override
		public boolean tryAdvance(BooleanConsumer action) {
			if (action == null) throw new NullPointerException();
			BooleanIterator iter = iterator();
			if (iter.hasNext()) {
				action.accept(iter.nextBoolean());
				return true;
			}
			return false;
		}
		
		@Override
		public boolean tryAdvance(Consumer<? super Boolean> action) {
			if (action == null) throw new NullPointerException();
			BooleanIterator iter = iterator();
			if (iter.hasNext()) {
				action.accept(Boolean.valueOf(iter.nextBoolean()));
				return true;
			}
			return false;
		}
		
		@Override
		public long estimateSize() {
			iterator();
			return est;
		}
	
		@Override
		public int characteristics() { return characteristics; }
		
		@Override
		public Comparator<? super Boolean> getComparator() {
			if (hasCharacteristics(4)) //Sorted
				return null;
			throw new IllegalStateException();
		}
	
		@Override
		public boolean nextBoolean() { return iterator().nextBoolean(); }
	
		@Override
		public boolean hasNext() { return iterator().hasNext(); }
	}
	
	static final class TypeArraySplitIterator implements BooleanSplititerator {
		private final boolean[] array;
		private int index;
		private final int fence; 
		private final int characteristics;
		
		public TypeArraySplitIterator(boolean[] array, int origin, int fence, int additionalCharacteristics) {
			this.array = array;
			index = origin;
			this.fence = fence;
			characteristics = additionalCharacteristics | Spliterator.SIZED | Spliterator.SUBSIZED;
		}
		
		@Override
		public BooleanSplititerator trySplit() {
			int lo = index, mid = (lo + fence) >>> 1;
			return (lo >= mid) ? null : new TypeArraySplitIterator(array, lo, index = mid, characteristics);
		}
		
		@Override
		public void forEachRemaining(BooleanConsumer action) {
			if (action == null) throw new NullPointerException();
			boolean[] a; int i, hi;
			if ((a = array).length >= (hi = fence) && (i = index) >= 0 && i < (index = hi)) {
				do { action.accept(a[i]); } while (++i < hi);
			}
		}
		
		@Override
		public boolean tryAdvance(BooleanConsumer action) {
			if (action == null) throw new NullPointerException();
			if (index >= 0 && index < fence) {
				action.accept(array[index++]);
				return true;
			}
			return false;
		}
		
		@Override
		public boolean tryAdvance(Consumer<? super Boolean> action) {
			if (action == null) throw new NullPointerException();
			if (index >= 0 && index < fence) {
				action.accept(Boolean.valueOf(array[index++]));
				return true;
			}
			return false;
		}
		
		@Override
		public long estimateSize() { return fence - index; }
		@Override
		public int characteristics() { return characteristics; }
		@Override
		public Comparator<? super Boolean> getComparator() {
			if (hasCharacteristics(4)) //Sorted
				return null;
			throw new IllegalStateException();
		}
		
		@Override
		public boolean nextBoolean() {
			if(!hasNext()) throw new NoSuchElementException();
			return array[index++];
		}
		
		@Override
		public boolean hasNext() { return index < fence; }
	}
	
}