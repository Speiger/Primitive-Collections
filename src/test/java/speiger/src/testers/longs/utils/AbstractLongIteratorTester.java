package speiger.src.testers.longs.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.Assert;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.IteratorFeature;

import junit.framework.AssertionFailedError;
import speiger.src.collections.longs.collections.LongBidirectionalIterator;
import speiger.src.collections.longs.collections.LongIterable;
import speiger.src.collections.longs.collections.LongIterator;
import speiger.src.collections.longs.lists.LongArrayList;
import speiger.src.collections.longs.lists.LongList;
import speiger.src.collections.longs.lists.LongListIterator;

@SuppressWarnings("javadoc")
public abstract class AbstractLongIteratorTester
{
	private Stimulus<LongIterator>[] stimuli;
	private final LongIterator elementsToInsert;
	private final Set<IteratorFeature> features;
	private final LongList expectedElements;
	private final int startIndex;
	private final KnownOrder knownOrder;

	private abstract static class PermittedMetaException extends RuntimeException {
		static final PermittedMetaException UOE_OR_ISE = new PermittedMetaException(
				"UnsupportedOperationException or IllegalStateException") {
			private static final long serialVersionUID = 1L;

			@Override
			boolean isPermitted(RuntimeException exception) {
				return exception instanceof UnsupportedOperationException || exception instanceof IllegalStateException;
			}
		};
		static final PermittedMetaException UOE = new PermittedMetaException("UnsupportedOperationException") {
			private static final long serialVersionUID = 1L;

			@Override
			boolean isPermitted(RuntimeException exception) {
				return exception instanceof UnsupportedOperationException;
			}
		};
		static final PermittedMetaException ISE = new PermittedMetaException("IllegalStateException") {
			private static final long serialVersionUID = 1L;

			@Override
			boolean isPermitted(RuntimeException exception) {
				return exception instanceof IllegalStateException;
			}
		};
		static final PermittedMetaException NSEE = new PermittedMetaException("NoSuchElementException") {
			private static final long serialVersionUID = 1L;

			@Override
			boolean isPermitted(RuntimeException exception) {
				return exception instanceof NoSuchElementException;
			}
		};

		private PermittedMetaException(String message) {
			super(message);
		}

		abstract boolean isPermitted(RuntimeException exception);

		void assertPermitted(RuntimeException exception) {
			if (!isPermitted(exception)) {
				String message = "Exception " + exception.getClass().getSimpleName() + " was thrown; expected "
						+ getMessage();
				AssertionFailedError assertionFailedError = new AssertionFailedError(String.valueOf(message));
				assertionFailedError.initCause(exception);
				throw assertionFailedError;
			}
		}

		private static final long serialVersionUID = 0;
	}

	private static final class UnknownElementException extends RuntimeException {
		private UnknownElementException(Collection<?> expected, Object actual) {
			super("Returned value '" + actual + "' not found. Remaining elements: " + expected);
		}

		private static final long serialVersionUID = 0;
	}

	protected final class MultiExceptionListIterator implements LongListIterator {
		final LongArrayList nextElements = new LongArrayList();
		final LongArrayList previousElements = new LongArrayList();
		LongArrayList stackWithLastReturnedElementAtTop = null;

		MultiExceptionListIterator(LongList expectedElements) {
			LongHelpers.addAll(nextElements, LongHelpers.reverse(expectedElements));
			for (int i = 0; i < startIndex; i++) {
				previousElements.push(nextElements.pop());
			}
		}

		@Override
		public void add(long e) {
			if (!features.contains(IteratorFeature.SUPPORTS_ADD)) {
				throw PermittedMetaException.UOE;
			}

			previousElements.push(e);
			stackWithLastReturnedElementAtTop = null;
		}

		@Override
		public boolean hasNext() {
			return !nextElements.isEmpty();
		}

		@Override
		public boolean hasPrevious() {
			return !previousElements.isEmpty();
		}

		@Override
		public long nextLong() {
			return transferElement(nextElements, previousElements);
		}

		@Override
		public int nextIndex() {
			return previousElements.size();
		}

		@Override
		public long previousLong() {
			return transferElement(previousElements, nextElements);
		}

		@Override
		public int previousIndex() {
			return nextIndex() - 1;
		}

		@Override
		public void remove() {
			throwIfInvalid(IteratorFeature.SUPPORTS_REMOVE);
			
			stackWithLastReturnedElementAtTop.pop();
			stackWithLastReturnedElementAtTop = null;
		}

		@Override
		public void set(long e) {
			throwIfInvalid(IteratorFeature.SUPPORTS_SET);

			stackWithLastReturnedElementAtTop.pop();
			stackWithLastReturnedElementAtTop.push(e);
		}

		void promoteToNext(long e) {
			if (nextElements.remLong(e)) {
				nextElements.push(e);
			} else {
				throw new UnknownElementException(nextElements, e);
			}
		}

		private long transferElement(LongArrayList source, LongArrayList destination) {
			if (source.isEmpty()) {
				throw PermittedMetaException.NSEE;
			}

			destination.push(source.pop());
			stackWithLastReturnedElementAtTop = destination;
			return destination.top();
		}

		private void throwIfInvalid(IteratorFeature methodFeature) {
			if (!features.contains(methodFeature)) {
				if (stackWithLastReturnedElementAtTop == null) {
					throw PermittedMetaException.UOE_OR_ISE;
				} else {
					throw PermittedMetaException.UOE;
				}
			} else if (stackWithLastReturnedElementAtTop == null) {
				throw PermittedMetaException.ISE;
			}
		}

		private LongList getElements() {
			LongList elements = new LongArrayList();
			LongHelpers.addAll(elements, previousElements);
			LongHelpers.addAll(elements, LongHelpers.reverse(nextElements));
			return elements;
		}
	}

	public enum KnownOrder {
		KNOWN_ORDER, UNKNOWN_ORDER
	}

	AbstractLongIteratorTester(int steps, LongIterable elementsToInsertIterable,
			Iterable<? extends IteratorFeature> features, LongIterable expectedElements, KnownOrder knownOrder, int startIndex) {
		stimuli = new Stimulus[steps];
		if (!elementsToInsertIterable.iterator().hasNext()) {
			throw new IllegalArgumentException();
		}
		elementsToInsert = LongHelpers.cycle(elementsToInsertIterable);
		this.features = Helpers.copyToSet(features);
		this.expectedElements = LongHelpers.copyToList(expectedElements);
		this.knownOrder = knownOrder;
		this.startIndex = startIndex;
	}

	protected abstract Iterable<? extends Stimulus<LongIterator>> getStimulusValues();

	protected abstract LongIterator newTargetIterator();
	protected void verify(LongList elements) {
	}

	/** Executes the test. */
	public final void test() {
		try {
			recurse(0);
		} catch (RuntimeException e) {
			throw new RuntimeException(Arrays.toString(stimuli), e);
		}
	}

	public void testForEachRemaining() {
		for (int i = 0; i < expectedElements.size() - 1; i++) {
			
			LongList targetElements = new LongArrayList();
			LongIterator iterator = newTargetIterator();
			for (int j = 0; j < i; j++) {
				targetElements.add(iterator.nextLong());
			}
			iterator.forEachRemaining(targetElements::add);
			if (knownOrder == KnownOrder.KNOWN_ORDER) {
				Assert.assertEquals(expectedElements, targetElements);
			} else {
				LongHelpers.assertEqualIgnoringOrder(expectedElements, targetElements);
			}
		}
	}

	private void recurse(int level) {
		if (level == stimuli.length) {
			// We've filled the array.
			compareResultsForThisListOfStimuli();
		} else {
			// Keep recursing to fill the array.
			for (Stimulus<LongIterator> stimulus : getStimulusValues()) {
				stimuli[level] = stimulus;
				recurse(level + 1);
			}
		}
	}

	private void compareResultsForThisListOfStimuli() {
		int removes = Collections.frequency(Arrays.asList(stimuli), remove);
		if ((!features.contains(IteratorFeature.SUPPORTS_REMOVE) && removes > 1) || (stimuli.length >= 5 && removes > 2)) {
			// removes are the most expensive thing to test, since they often
			// throw exceptions with stack
			// traces, so we test them a bit less aggressively
			return;
		}

		MultiExceptionListIterator reference = new MultiExceptionListIterator(expectedElements);
		LongIterator target = newTargetIterator();
		for (int i = 0; i < stimuli.length; i++) {
			try {
				stimuli[i].executeAndCompare(reference, target);
				verify(reference.getElements());
			} catch (AssertionFailedError cause) {
			    AssertionFailedError assertionFailedError = new AssertionFailedError(String.valueOf("failed with stimuli " + subListCopy(stimuli, i + 1)));
			    assertionFailedError.initCause(cause);
			    throw assertionFailedError;
			}
		}
	}

	private static List<Object> subListCopy(Object[] source, int size) {
		final Object[] copy = new Object[size];
		System.arraycopy(source, 0, copy, 0, size);
		return Arrays.asList(copy);
	}

	private interface IteratorOperation {
		long execute(LongIterator iterator);
	}

	/**
	 * Apply this method to both iterators and return normally only if both
	 * produce the same response.
	 *
	 * @see Stimulus#executeAndCompare(ListIterator, Iterator)
	 */
	private <E extends LongIterator> void internalExecuteAndCompare(E reference, E target, IteratorOperation method) {
		long referenceReturnValue = -1L;
		PermittedMetaException referenceException = null;
		long targetReturnValue = -1L;
		RuntimeException targetException = null;

		try {
			targetReturnValue = method.execute(target);
		} catch (RuntimeException e) {
			targetException = e;
		}

		try {
			if (method == NEXT_METHOD && targetException == null && knownOrder == KnownOrder.UNKNOWN_ORDER) {
				((MultiExceptionListIterator) reference).promoteToNext(targetReturnValue);
			}
			
			referenceReturnValue = method.execute(reference);
		} catch (PermittedMetaException e) {
			referenceException = e;
		} catch (UnknownElementException e) {
		    AssertionFailedError assertionFailedError = new AssertionFailedError(String.valueOf(e.getMessage()));
		    assertionFailedError.initCause(e);
		    throw assertionFailedError;
		}

		if (referenceException == null) {
			if (targetException != null) {
			    AssertionFailedError assertionFailedError = new AssertionFailedError("Target threw exception when reference did not");
			    assertionFailedError.initCause(targetException);
			    throw assertionFailedError;
			}
			Assert.assertEquals(referenceReturnValue, targetReturnValue);
			return;
		}

		if (targetException == null) {
			Assert.fail("Target failed to throw " + referenceException);
		}

		/*
		 * Reference iterator threw an exception, so we should expect an
		 * acceptable exception from the target.
		 */
		referenceException.assertPermitted(targetException);
	}

	private static final IteratorOperation REMOVE_METHOD = new IteratorOperation() {
		@Override
		public long execute(LongIterator iterator) {
			iterator.remove();
			return -1L;
		}
	};

	private static final IteratorOperation NEXT_METHOD = new IteratorOperation() {
		@Override
		public long execute(LongIterator iterator) {
			return iterator.nextLong();
		}
	};

	private static final IteratorOperation PREVIOUS_METHOD = new IteratorOperation() {
		@Override
		public long execute(LongIterator iterator) {
			return ((LongBidirectionalIterator) iterator).previousLong();
		}
	};

	private final IteratorOperation newAddMethod() {
		final long toInsert = elementsToInsert.nextLong();
		return new IteratorOperation() {
			@Override
			public long execute(LongIterator iterator) {
				@SuppressWarnings("unchecked")
				LongListIterator rawIterator = (LongListIterator) iterator;
				rawIterator.add(toInsert);
				return -1L;
			}
		};
	}

	private final IteratorOperation newSetMethod() {
		final long toInsert = elementsToInsert.nextLong();
		return new IteratorOperation() {
			@Override
			public long execute(LongIterator iterator) {
				@SuppressWarnings("unchecked")
				LongListIterator li = (LongListIterator) iterator;
				li.set(toInsert);
				return -1L;
			}
		};
	}

	abstract static class Stimulus<E extends LongIterator> {
		private final String toString;

		protected Stimulus(String toString) {
			this.toString = toString;
		}

		abstract void executeAndCompare(LongListIterator reference, E target);

		@Override
		public String toString() {
			return toString;
		}
	}

	Stimulus<LongIterator> hasNext = new Stimulus<LongIterator>("hasNext") {
		@Override
		void executeAndCompare(LongListIterator reference, LongIterator target) {
			Assert.assertEquals(reference.hasNext(), target.hasNext());
		}
	};
	Stimulus<LongIterator> next = new Stimulus<LongIterator>("next") {
		@Override
		void executeAndCompare(LongListIterator reference, LongIterator target) {
			internalExecuteAndCompare(reference, target, NEXT_METHOD);
		}
	};
	Stimulus<LongIterator> remove = new Stimulus<LongIterator>("remove") {
		@Override
		void executeAndCompare(LongListIterator reference, LongIterator target) {
			internalExecuteAndCompare(reference, target, REMOVE_METHOD);
		}
	};

	@SuppressWarnings("unchecked")
	List<Stimulus<LongIterator>> iteratorStimuli() {
		return Arrays.asList(hasNext, next, remove);
	}
	
	Stimulus<LongBidirectionalIterator> hasPrevious = new Stimulus<LongBidirectionalIterator>("hasPrevious") {
		@Override
		void executeAndCompare(LongListIterator reference, LongBidirectionalIterator target) {
			Assert.assertEquals(reference.hasPrevious(), target.hasPrevious());
		}
	};
	Stimulus<LongBidirectionalIterator> previous = new Stimulus<LongBidirectionalIterator>("previous") {
		@Override
		void executeAndCompare(LongListIterator reference, LongBidirectionalIterator target) {
			internalExecuteAndCompare(reference, target, PREVIOUS_METHOD);
		}
	};
	
	List<Stimulus<LongBidirectionalIterator>> biIteratorStimuli() {
		return Arrays.asList(hasPrevious, previous);
	}
	
	Stimulus<LongListIterator> nextIndex = new Stimulus<LongListIterator>("nextIndex") {
		@Override
		void executeAndCompare(LongListIterator reference, LongListIterator target) {
			Assert.assertEquals(reference.nextIndex(), target.nextIndex());
		}
	};
	Stimulus<LongListIterator> previousIndex = new Stimulus<LongListIterator>("previousIndex") {
		@Override
		void executeAndCompare(LongListIterator reference, LongListIterator target) {
			Assert.assertEquals(reference.previousIndex(), target.previousIndex());
		}
	};
	Stimulus<LongListIterator> add = new Stimulus<LongListIterator>("add") {
		@Override
		void executeAndCompare(LongListIterator reference, LongListIterator target) {
			internalExecuteAndCompare(reference, target, newAddMethod());
		}
	};
	Stimulus<LongListIterator> set = new Stimulus<LongListIterator>("set") {
		@Override
		void executeAndCompare(LongListIterator reference, LongListIterator target) {
			internalExecuteAndCompare(reference, target, newSetMethod());
		}
	};

	List<Stimulus<LongListIterator>> listIteratorStimuli() {
		return Arrays.asList(nextIndex, previousIndex, add, set);
	}
}