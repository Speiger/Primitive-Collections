package speiger.src.collections.doubles.utils;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterator.OfDouble;
import java.util.function.Consumer;

import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.collections.DoubleSplititerator;
import speiger.src.collections.doubles.functions.DoubleConsumer;
import speiger.src.collections.utils.SanityChecks;

/**
 * Helper class that provides SplitIterators for normal and stream usage
 */
public class DoubleSplititerators
{
	/**
	 * Creates A stream compatible split iterator without copying the original array or boxing
	 * @param array that should be wrapped into a split iterator
	 * @param characteristics characteristics properties of this spliterator's  source or elements.
	 * @return a split iterator of a Stream compatible type
	 */
	public static OfDouble createArrayJavaSplititerator(double[] array, int characteristics) { return createArrayJavaSplititerator(array, 0, array.length, characteristics);}
	/**
	 * Creates A stream compatible split iterator without copying the original array or boxing
	 * @param array that should be wrapped into a split iterator
	 * @param size the maximum index within the array
	 * @param characteristics characteristics properties of this spliterator's  source or elements.
	 * @return a split iterator of a Stream compatible type
	 * @throws IllegalStateException if the size is outside of the array size
	 */
	public static OfDouble createArrayJavaSplititerator(double[] array, int size, int characteristics) { return createArrayJavaSplititerator(array, 0, size, characteristics);}
	/**
	 * Creates A stream compatible split iterator without copying the original array or boxing
	 * @param array that should be wrapped into a split iterator
	 * @param offset the starting index of the array
	 * @param size the maximum index within the array
	 * @param characteristics characteristics properties of this spliterator's  source or elements.
	 * @return a split iterator of a Stream compatible type
	 * @throws IllegalStateException the offset and size are outside of the arrays range
	 */
	public static OfDouble createArrayJavaSplititerator(double[] array, int offset, int size, int characteristics) {
		SanityChecks.checkArrayCapacity(array.length, offset, size);
		return new ArraySplitIterator(array, offset, size, characteristics);
	}
	
	/**
	 * Creates a stream compatible split iterator without copying it or boxing it
	 * @param collection the collection that should be wrapped in a split iterator
	 * @param characteristics characteristics properties of this spliterator's  source or elements.
	 * @return a split iterator of a Stream compatible type
	 */
	public static OfDouble createJavaSplititerator(DoubleCollection collection, int characteristics) {
		return new IteratorSpliterator(collection, characteristics);
	}
	
	/**
	 * Creates a stream compatible split iterator without copying it or boxing it
	 * @param iterator the Iterator that should be wrapped in a split iterator
	 * @param characteristics characteristics properties of this spliterator's source or elements.
	 * @return a split iterator of a Stream compatible type
	 */
	public static OfDouble createUnknownJavaSplititerator(DoubleIterator iterator, int characteristics) {
		return new IteratorSpliterator(iterator, characteristics);
	}
	
	/**
	 * Creates a stream compatible split iterator without copying it or boxing it
	 * @param iterator the collection that should be wrapped in a split iterator
	 * @param size the amount of elements in the iterator
	 * @param characteristics characteristics properties of this spliterator's  source or elements.
	 * @return a split iterator of a Stream compatible type
	 */
	public static OfDouble createSizedJavaSplititerator(DoubleIterator iterator, long size, int characteristics) {
		return new IteratorSpliterator(iterator, size, characteristics);
	}
	
	/**
	* Creates a Type Specific SplitIterator to reduce boxing/unboxing
	* @param array that should be wrapped into a split iterator
	* @param characteristics characteristics properties of this spliterator's  source or elements.
	* @return a Type Specific SplitIterator
	*/
	public static DoubleSplititerator createArraySplititerator(double[] array, int characteristics) { return createArraySplititerator(array, 0, array.length, characteristics);}
	/**
	* Creates a Type Specific SplitIterator to reduce boxing/unboxing
	* @param array that should be wrapped into a split iterator
	* @param size the maximum index within the array
	* @param characteristics characteristics properties of this spliterator's  source or elements.
	* @return a Type Specific SplitIterator
	* @throws IllegalStateException if the size is outside of the array size
	*/
	public static DoubleSplititerator createArraySplititerator(double[] array, int size, int characteristics) { return createArraySplititerator(array, 0, size, characteristics);}
	/**
	* Creates a Type Specific SplitIterator to reduce boxing/unboxing
	* @param array that should be wrapped into a split iterator
	* @param offset the starting index of the array
	* @param size the maximum index within the array
	* @param characteristics characteristics properties of this spliterator's  source or elements.
	* @return a Type Specific SplitIterator
	* @throws IllegalStateException the offset and size are outside of the arrays range
	*/
	public static DoubleSplititerator createArraySplititerator(double[] array, int offset, int size, int characteristics) {
		SanityChecks.checkArrayCapacity(array.length, offset, size);
		return new TypeArraySplitIterator(array, offset, size, characteristics);
	}
	
	/**
	* Creates a Type Specific SplitIterator to reduce boxing/unboxing
	* @param collection the collection that should be wrapped in a split iterator
	* @param characteristics characteristics properties of this spliterator's  source or elements.
	* @return a Type Specific SplitIterator
	*/
	public static DoubleSplititerator createSplititerator(DoubleCollection collection, int characteristics) {
		return new TypeIteratorSpliterator(collection, characteristics);
	}
	
	/**
	* Creates a Type Specific SplitIterator to reduce boxing/unboxing
	* @param iterator the Iterator that should be wrapped in a split iterator
	* @param characteristics characteristics properties of this spliterator's source or elements.
	* @return a Type Specific SplitIterator
	*/
	public static DoubleSplititerator createUnknownSplititerator(DoubleIterator iterator, int characteristics) {
		return new TypeIteratorSpliterator(iterator, characteristics);
	}
	
	/**
	* Creates a Type Specific SplitIterator to reduce boxing/unboxing
	* @param iterator the collection that should be wrapped in a split iterator
	* @param size the amount of elements in the iterator
	* @param characteristics characteristics properties of this spliterator's  source or elements.
	* @return a Type Specific SplitIterator
	*/
	public static DoubleSplititerator createSizedSplititerator(DoubleIterator iterator, long size, int characteristics) {
		return new TypeIteratorSpliterator(iterator, size, characteristics);
	}
	
	static class TypeIteratorSpliterator implements DoubleSplititerator {
		static final int BATCH_UNIT = 1 << 10;
		static final int MAX_BATCH = 1 << 25;
		private final DoubleCollection collection;
		private DoubleIterator it;
		private final int characteristics;
		private long est;
		private int batch;
	
		TypeIteratorSpliterator(DoubleCollection collection, int characteristics) {
			this.collection = collection;
			it = null;
			this.characteristics = (characteristics & Spliterator.CONCURRENT) == 0
								? characteristics | Spliterator.SIZED | Spliterator.SUBSIZED
								: characteristics;
		}
	
		TypeIteratorSpliterator(DoubleIterator iterator, long size, int characteristics) {
			collection = null;
			it = iterator;
			est = size;
			this.characteristics = (characteristics & Spliterator.CONCURRENT) == 0
								? characteristics | Spliterator.SIZED | Spliterator.SUBSIZED
								: characteristics;
		}
		
		TypeIteratorSpliterator(DoubleIterator iterator, int characteristics) {
			collection = null;
			it = iterator;
			est = Long.MAX_VALUE;
			this.characteristics = characteristics & ~(Spliterator.SIZED | Spliterator.SUBSIZED);
		}
		
		private DoubleIterator iterator()
		{
			if (it == null) {
				it = collection.iterator();
				est = collection.size();
			}
			return it;
		}
		
		@Override
		public DoubleSplititerator trySplit() {
			DoubleIterator i = iterator();
			if (est > 1 && i.hasNext()) {
				int n = Math.min(batch + BATCH_UNIT, Math.min((int)est, MAX_BATCH));
				double[] a = new double[n];
				int j = 0;
				do { a[j] = i.nextDouble(); } while (++j < n && i.hasNext());
				batch = j;
				if (est != Long.MAX_VALUE)
					est -= j;
				return new TypeArraySplitIterator(a, 0, j, characteristics);
			}
			return null;
		}
		
		@Override
		public void forEachRemaining(DoubleConsumer action) {
			if (action == null) throw new NullPointerException();
			iterator().forEachRemaining(action);
		}
		
		@Override
		public boolean tryAdvance(DoubleConsumer action) {
			if (action == null) throw new NullPointerException();
			DoubleIterator iter = iterator();
			if (iter.hasNext()) {
				action.accept(iter.nextDouble());
				return true;
			}
			return false;
		}
		
		@Override
		public boolean tryAdvance(Consumer<? super Double> action) {
			if (action == null) throw new NullPointerException();
			DoubleIterator iter = iterator();
			if (iter.hasNext()) {
				action.accept(Double.valueOf(iter.nextDouble()));
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
		public Comparator<? super Double> getComparator() {
			if (hasCharacteristics(4)) //Sorted
				return null;
			throw new IllegalStateException();
		}
	
		@Override
		public double nextDouble() { return iterator().nextDouble(); }
	
		@Override
		public boolean hasNext() { return iterator().hasNext(); }
	}
	
	static final class TypeArraySplitIterator implements DoubleSplititerator {
		private final double[] array;
		private int index;
		private final int fence; 
		private final int characteristics;
		
		public TypeArraySplitIterator(double[] array, int origin, int fence, int additionalCharacteristics) {
			this.array = array;
			index = origin;
			this.fence = fence;
			characteristics = additionalCharacteristics | Spliterator.SIZED | Spliterator.SUBSIZED;
		}
		
		@Override
		public DoubleSplititerator trySplit() {
			int lo = index, mid = (lo + fence) >>> 1;
			return (lo >= mid) ? null : new TypeArraySplitIterator(array, lo, index = mid, characteristics);
		}
		
		@Override
		public void forEachRemaining(DoubleConsumer action) {
			if (action == null) throw new NullPointerException();
			double[] a; int i, hi;
			if ((a = array).length >= (hi = fence) && (i = index) >= 0 && i < (index = hi)) {
				do { action.accept(a[i]); } while (++i < hi);
			}
		}
		
		@Override
		public boolean tryAdvance(DoubleConsumer action) {
			if (action == null) throw new NullPointerException();
			if (index >= 0 && index < fence) {
				action.accept(array[index++]);
				return true;
			}
			return false;
		}
		
		@Override
		public boolean tryAdvance(Consumer<? super Double> action) {
			if (action == null) throw new NullPointerException();
			if (index >= 0 && index < fence) {
				action.accept(Double.valueOf(array[index++]));
				return true;
			}
			return false;
		}
		
		@Override
		public long estimateSize() { return fence - index; }
		@Override
		public int characteristics() { return characteristics; }
		@Override
		public Comparator<? super Double> getComparator() {
			if (hasCharacteristics(4)) //Sorted
				return null;
			throw new IllegalStateException();
		}
		
		@Override
		public double nextDouble() {
			if(!hasNext()) throw new NoSuchElementException();
			return array[index++];
		}
		
		@Override
		public boolean hasNext() { return index < fence; }
	}
	
	static class IteratorSpliterator implements OfDouble {
		static final int BATCH_UNIT = 1 << 10;
		static final int MAX_BATCH = 1 << 25;
		private final DoubleCollection collection;
		private DoubleIterator it;
		private final int characteristics;
		private long est;
		private int batch;

		IteratorSpliterator(DoubleCollection collection, int characteristics) {
			this.collection = collection;
			it = null;
			this.characteristics = (characteristics & Spliterator.CONCURRENT) == 0
								   ? characteristics | Spliterator.SIZED | Spliterator.SUBSIZED
								   : characteristics;
		}

		IteratorSpliterator(DoubleIterator iterator, long size, int characteristics) {
			collection = null;
			it = iterator;
			est = size;
			this.characteristics = (characteristics & Spliterator.CONCURRENT) == 0
								   ? characteristics | Spliterator.SIZED | Spliterator.SUBSIZED
								   : characteristics;
		}
		
		IteratorSpliterator(DoubleIterator iterator, int characteristics) {
			collection = null;
			it = iterator;
			est = Long.MAX_VALUE;
			this.characteristics = characteristics & ~(Spliterator.SIZED | Spliterator.SUBSIZED);
		}
		
		private DoubleIterator iterator()
		{
			if (it == null) {
				it = collection.iterator();
				est = collection.size();
			}
			return it;
		}
		
		@Override
		public OfDouble trySplit() {
			DoubleIterator i = iterator();
			if (est > 1 && i.hasNext()) {
				int n = Math.min(batch + BATCH_UNIT, Math.min((int)est, MAX_BATCH));
				double[] a = new double[n];
				int j = 0;
				do { a[j] = i.nextDouble(); } while (++j < n && i.hasNext());
				batch = j;
				if (est != Long.MAX_VALUE)
					est -= j;
				return new ArraySplitIterator(a, 0, j, characteristics);
			}
			return null;
		}
		
		@Override
		public void forEachRemaining(java.util.function.DoubleConsumer action) {
			if (action == null) throw new NullPointerException();
			iterator().forEachRemaining(T -> action.accept(T));
		}

		@Override
		public boolean tryAdvance(java.util.function.DoubleConsumer action) {
			if (action == null) throw new NullPointerException();
			DoubleIterator iter = iterator();
			if (iter.hasNext()) {
				action.accept(iter.nextDouble());
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
		public Comparator<? super Double> getComparator() {
			if (hasCharacteristics(4)) //Sorted
				return null;
			throw new IllegalStateException();
		}
	}
	
	static final class ArraySplitIterator implements OfDouble {
		private final double[] array;
		private int index;
		private final int fence; 
		private final int characteristics;
		
		public ArraySplitIterator(double[] array, int origin, int fence, int additionalCharacteristics) {
			this.array = array;
			index = origin;
			this.fence = fence;
			characteristics = additionalCharacteristics | Spliterator.SIZED | Spliterator.SUBSIZED;
		}
		
		@Override
		public OfDouble trySplit() {
			int lo = index, mid = (lo + fence) >>> 1;
			return (lo >= mid) ? null : new ArraySplitIterator(array, lo, index = mid, characteristics);
		}
		
		@Override
		public void forEachRemaining(java.util.function.DoubleConsumer action) {
			if (action == null) throw new NullPointerException();
			double[] a; int i, hi;
			if ((a = array).length >= (hi = fence) && (i = index) >= 0 && i < (index = hi)) {
				do { action.accept(a[i]); } while (++i < hi);
			}
		}

		@Override
		public boolean tryAdvance(java.util.function.DoubleConsumer action) {
			if (action == null) throw new NullPointerException();
			if (index >= 0 && index < fence) {
				action.accept(array[index++]);
				return true;
			}
			return false;
		}
		
		@Override
		public long estimateSize() { return fence - index; }
		@Override
		public int characteristics() { return characteristics; }
		@Override
		public Comparator<? super Double> getComparator() {
			if (hasCharacteristics(4)) //Sorted
				return null;
			throw new IllegalStateException();
		}
	}
}