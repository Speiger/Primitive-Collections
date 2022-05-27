package speiger.src.testers.shorts.utils;

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
import speiger.src.collections.shorts.collections.ShortBidirectionalIterator;
import speiger.src.collections.shorts.collections.ShortIterable;
import speiger.src.collections.shorts.collections.ShortIterator;
import speiger.src.collections.shorts.lists.ShortArrayList;
import speiger.src.collections.shorts.lists.ShortList;
import speiger.src.collections.shorts.lists.ShortListIterator;

public abstract class AbstractShortIteratorTester{
	private Stimulus<ShortIterator>[] stimuli;
	private final ShortIterator elementsToInsert;
	private final Set<IteratorFeature> features;
	private final ShortList expectedElements;
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

	protected final class MultiExceptionListIterator implements ShortListIterator {
		final ShortArrayList nextElements = new ShortArrayList();
		final ShortArrayList previousElements = new ShortArrayList();
		ShortArrayList stackWithLastReturnedElementAtTop = null;

		MultiExceptionListIterator(ShortList expectedElements) {
			ShortHelpers.addAll(nextElements, ShortHelpers.reverse(expectedElements));
			for (int i = 0; i < startIndex; i++) {
				previousElements.push(nextElements.pop());
			}
		}

		@Override
		public void add(short e) {
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
		public short nextShort() {
			return transferElement(nextElements, previousElements);
		}

		@Override
		public int nextIndex() {
			return previousElements.size();
		}

		@Override
		public short previousShort() {
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
		public void set(short e) {
			throwIfInvalid(IteratorFeature.SUPPORTS_SET);

			stackWithLastReturnedElementAtTop.pop();
			stackWithLastReturnedElementAtTop.push(e);
		}

		void promoteToNext(short e) {
			if (nextElements.remShort(e)) {
				nextElements.push(e);
			} else {
				throw new UnknownElementException(nextElements, e);
			}
		}

		private short transferElement(ShortArrayList source, ShortArrayList destination) {
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

		private ShortList getElements() {
			ShortList elements = new ShortArrayList();
			ShortHelpers.addAll(elements, previousElements);
			ShortHelpers.addAll(elements, ShortHelpers.reverse(nextElements));
			return elements;
		}
	}

	public enum KnownOrder {
		KNOWN_ORDER, UNKNOWN_ORDER
	}

	AbstractShortIteratorTester(int steps, ShortIterable elementsToInsertIterable,
			Iterable<? extends IteratorFeature> features, ShortIterable expectedElements, KnownOrder knownOrder, int startIndex) {
		stimuli = new Stimulus[steps];
		if (!elementsToInsertIterable.iterator().hasNext()) {
			throw new IllegalArgumentException();
		}
		elementsToInsert = ShortHelpers.cycle(elementsToInsertIterable);
		this.features = Helpers.copyToSet(features);
		this.expectedElements = ShortHelpers.copyToList(expectedElements);
		this.knownOrder = knownOrder;
		this.startIndex = startIndex;
	}

	protected abstract Iterable<? extends Stimulus<ShortIterator>> getStimulusValues();

	protected abstract ShortIterator newTargetIterator();
	protected void verify(ShortList elements) {
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
			
			ShortList targetElements = new ShortArrayList();
			ShortIterator iterator = newTargetIterator();
			for (int j = 0; j < i; j++) {
				targetElements.add(iterator.nextShort());
			}
			iterator.forEachRemaining(targetElements::add);
			if (knownOrder == KnownOrder.KNOWN_ORDER) {
				Assert.assertEquals(expectedElements, targetElements);
			} else {
				ShortHelpers.assertEqualIgnoringOrder(expectedElements, targetElements);
			}
		}
	}

	private void recurse(int level) {
		if (level == stimuli.length) {
			// We've filled the array.
			compareResultsForThisListOfStimuli();
		} else {
			// Keep recursing to fill the array.
			for (Stimulus<ShortIterator> stimulus : getStimulusValues()) {
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
		ShortIterator target = newTargetIterator();
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
		short execute(ShortIterator iterator);
	}

	/**
	 * Apply this method to both iterators and return normally only if both
	 * produce the same response.
	 *
	 * @see Stimulus#executeAndCompare(ListIterator, Iterator)
	 */
	private <E extends ShortIterator> void internalExecuteAndCompare(E reference, E target, IteratorOperation method) {
		short referenceReturnValue = (short)-1;
		PermittedMetaException referenceException = null;
		short targetReturnValue = (short)-1;
		RuntimeException targetException = null;

		try {
			targetReturnValue = method.execute(target);
		} catch (RuntimeException e) {
			targetException = e;
		}

		try {
			if (method == nextShort_METHOD && targetException == null && knownOrder == KnownOrder.UNKNOWN_ORDER) {
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

	private static final IteratorOperation removeShort_METHOD = new IteratorOperation() {
		@Override
		public short execute(ShortIterator iterator) {
			iterator.remove();
			return (short)-1;
		}
	};

	private static final IteratorOperation nextShort_METHOD = new IteratorOperation() {
		@Override
		public short execute(ShortIterator iterator) {
			return iterator.nextShort();
		}
	};

	private static final IteratorOperation previousShort_METHOD = new IteratorOperation() {
		@Override
		public short execute(ShortIterator iterator) {
			return ((ShortBidirectionalIterator) iterator).previousShort();
		}
	};

	private final IteratorOperation newAddMethod() {
		final short toInsert = elementsToInsert.nextShort();
		return new IteratorOperation() {
			@Override
			public short execute(ShortIterator iterator) {
				@SuppressWarnings("unchecked")
				ShortListIterator rawIterator = (ShortListIterator) iterator;
				rawIterator.add(toInsert);
				return (short)-1;
			}
		};
	}

	private final IteratorOperation newSetMethod() {
		final short toInsert = elementsToInsert.nextShort();
		return new IteratorOperation() {
			@Override
			public short execute(ShortIterator iterator) {
				@SuppressWarnings("unchecked")
				ShortListIterator li = (ShortListIterator) iterator;
				li.set(toInsert);
				return (short)-1;
			}
		};
	}

	abstract static class Stimulus<E extends ShortIterator> {
		private final String toString;

		protected Stimulus(String toString) {
			this.toString = toString;
		}

		abstract void executeAndCompare(ShortListIterator reference, E target);

		@Override
		public String toString() {
			return toString;
		}
	}

	Stimulus<ShortIterator> hasNext = new Stimulus<ShortIterator>("hasNext") {
		@Override
		void executeAndCompare(ShortListIterator reference, ShortIterator target) {
			Assert.assertEquals(reference.hasNext(), target.hasNext());
		}
	};
	Stimulus<ShortIterator> next = new Stimulus<ShortIterator>("next") {
		@Override
		void executeAndCompare(ShortListIterator reference, ShortIterator target) {
			internalExecuteAndCompare(reference, target, nextShort_METHOD);
		}
	};
	Stimulus<ShortIterator> remove = new Stimulus<ShortIterator>("remove") {
		@Override
		void executeAndCompare(ShortListIterator reference, ShortIterator target) {
			internalExecuteAndCompare(reference, target, removeShort_METHOD);
		}
	};

	@SuppressWarnings("unchecked")
	List<Stimulus<ShortIterator>> iteratorStimuli() {
		return Arrays.asList(hasNext, next, remove);
	}
	
	Stimulus<ShortBidirectionalIterator> hasPrevious = new Stimulus<ShortBidirectionalIterator>("hasPrevious") {
		@Override
		void executeAndCompare(ShortListIterator reference, ShortBidirectionalIterator target) {
			Assert.assertEquals(reference.hasPrevious(), target.hasPrevious());
		}
	};
	Stimulus<ShortBidirectionalIterator> previous = new Stimulus<ShortBidirectionalIterator>("previous") {
		@Override
		void executeAndCompare(ShortListIterator reference, ShortBidirectionalIterator target) {
			internalExecuteAndCompare(reference, target, previousShort_METHOD);
		}
	};
	
	List<Stimulus<ShortBidirectionalIterator>> biIteratorStimuli() {
		return Arrays.asList(hasPrevious, previous);
	}
	
	Stimulus<ShortListIterator> nextIndex = new Stimulus<ShortListIterator>("nextIndex") {
		@Override
		void executeAndCompare(ShortListIterator reference, ShortListIterator target) {
			Assert.assertEquals(reference.nextIndex(), target.nextIndex());
		}
	};
	Stimulus<ShortListIterator> previousIndex = new Stimulus<ShortListIterator>("previousIndex") {
		@Override
		void executeAndCompare(ShortListIterator reference, ShortListIterator target) {
			Assert.assertEquals(reference.previousIndex(), target.previousIndex());
		}
	};
	Stimulus<ShortListIterator> add = new Stimulus<ShortListIterator>("add") {
		@Override
		void executeAndCompare(ShortListIterator reference, ShortListIterator target) {
			internalExecuteAndCompare(reference, target, newAddMethod());
		}
	};
	Stimulus<ShortListIterator> set = new Stimulus<ShortListIterator>("set") {
		@Override
		void executeAndCompare(ShortListIterator reference, ShortListIterator target) {
			internalExecuteAndCompare(reference, target, newSetMethod());
		}
	};

	List<Stimulus<ShortListIterator>> listIteratorStimuli() {
		return Arrays.asList(nextIndex, previousIndex, add, set);
	}
}