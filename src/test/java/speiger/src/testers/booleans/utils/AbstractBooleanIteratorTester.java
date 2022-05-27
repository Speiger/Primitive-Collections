package speiger.src.testers.booleans.utils;

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
import speiger.src.collections.booleans.collections.BooleanBidirectionalIterator;
import speiger.src.collections.booleans.collections.BooleanIterable;
import speiger.src.collections.booleans.collections.BooleanIterator;
import speiger.src.collections.booleans.lists.BooleanArrayList;
import speiger.src.collections.booleans.lists.BooleanList;
import speiger.src.collections.booleans.lists.BooleanListIterator;

@SuppressWarnings("javadoc")
public abstract class AbstractBooleanIteratorTester
{
	private Stimulus<BooleanIterator>[] stimuli;
	private final BooleanIterator elementsToInsert;
	private final Set<IteratorFeature> features;
	private final BooleanList expectedElements;
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

	protected final class MultiExceptionListIterator implements BooleanListIterator {
		final BooleanArrayList nextElements = new BooleanArrayList();
		final BooleanArrayList previousElements = new BooleanArrayList();
		BooleanArrayList stackWithLastReturnedElementAtTop = null;

		MultiExceptionListIterator(BooleanList expectedElements) {
			BooleanHelpers.addAll(nextElements, BooleanHelpers.reverse(expectedElements));
			for (int i = 0; i < startIndex; i++) {
				previousElements.push(nextElements.pop());
			}
		}

		@Override
		public void add(boolean e) {
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
		public boolean nextBoolean() {
			return transferElement(nextElements, previousElements);
		}

		@Override
		public int nextIndex() {
			return previousElements.size();
		}

		@Override
		public boolean previousBoolean() {
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
		public void set(boolean e) {
			throwIfInvalid(IteratorFeature.SUPPORTS_SET);

			stackWithLastReturnedElementAtTop.pop();
			stackWithLastReturnedElementAtTop.push(e);
		}

		void promoteToNext(boolean e) {
			if (nextElements.remBoolean(e)) {
				nextElements.push(e);
			} else {
				throw new UnknownElementException(nextElements, e);
			}
		}

		private boolean transferElement(BooleanArrayList source, BooleanArrayList destination) {
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

		private BooleanList getElements() {
			BooleanList elements = new BooleanArrayList();
			BooleanHelpers.addAll(elements, previousElements);
			BooleanHelpers.addAll(elements, BooleanHelpers.reverse(nextElements));
			return elements;
		}
	}

	public enum KnownOrder {
		KNOWN_ORDER, UNKNOWN_ORDER
	}

	AbstractBooleanIteratorTester(int steps, BooleanIterable elementsToInsertIterable,
			Iterable<? extends IteratorFeature> features, BooleanIterable expectedElements, KnownOrder knownOrder, int startIndex) {
		stimuli = new Stimulus[steps];
		if (!elementsToInsertIterable.iterator().hasNext()) {
			throw new IllegalArgumentException();
		}
		elementsToInsert = BooleanHelpers.cycle(elementsToInsertIterable);
		this.features = Helpers.copyToSet(features);
		this.expectedElements = BooleanHelpers.copyToList(expectedElements);
		this.knownOrder = knownOrder;
		this.startIndex = startIndex;
	}

	protected abstract Iterable<? extends Stimulus<BooleanIterator>> getStimulusValues();

	protected abstract BooleanIterator newTargetIterator();
	protected void verify(BooleanList elements) {
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
			
			BooleanList targetElements = new BooleanArrayList();
			BooleanIterator iterator = newTargetIterator();
			for (int j = 0; j < i; j++) {
				targetElements.add(iterator.nextBoolean());
			}
			iterator.forEachRemaining(targetElements::add);
			if (knownOrder == KnownOrder.KNOWN_ORDER) {
				Assert.assertEquals(expectedElements, targetElements);
			} else {
				BooleanHelpers.assertEqualIgnoringOrder(expectedElements, targetElements);
			}
		}
	}

	private void recurse(int level) {
		if (level == stimuli.length) {
			// We've filled the array.
			compareResultsForThisListOfStimuli();
		} else {
			// Keep recursing to fill the array.
			for (Stimulus<BooleanIterator> stimulus : getStimulusValues()) {
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
		BooleanIterator target = newTargetIterator();
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
		boolean execute(BooleanIterator iterator);
	}

	/**
	 * Apply this method to both iterators and return normally only if both
	 * produce the same response.
	 *
	 * @see Stimulus#executeAndCompare(ListIterator, Iterator)
	 */
	private <E extends BooleanIterator> void internalExecuteAndCompare(E reference, E target, IteratorOperation method) {
		boolean referenceReturnValue = false;
		PermittedMetaException referenceException = null;
		boolean targetReturnValue = false;
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
		public boolean execute(BooleanIterator iterator) {
			iterator.remove();
			return false;
		}
	};

	private static final IteratorOperation NEXT_METHOD = new IteratorOperation() {
		@Override
		public boolean execute(BooleanIterator iterator) {
			return iterator.nextBoolean();
		}
	};

	private static final IteratorOperation PREVIOUS_METHOD = new IteratorOperation() {
		@Override
		public boolean execute(BooleanIterator iterator) {
			return ((BooleanBidirectionalIterator) iterator).previousBoolean();
		}
	};

	private final IteratorOperation newAddMethod() {
		final boolean toInsert = elementsToInsert.nextBoolean();
		return new IteratorOperation() {
			@Override
			public boolean execute(BooleanIterator iterator) {
				@SuppressWarnings("unchecked")
				BooleanListIterator rawIterator = (BooleanListIterator) iterator;
				rawIterator.add(toInsert);
				return false;
			}
		};
	}

	private final IteratorOperation newSetMethod() {
		final boolean toInsert = elementsToInsert.nextBoolean();
		return new IteratorOperation() {
			@Override
			public boolean execute(BooleanIterator iterator) {
				@SuppressWarnings("unchecked")
				BooleanListIterator li = (BooleanListIterator) iterator;
				li.set(toInsert);
				return false;
			}
		};
	}

	abstract static class Stimulus<E extends BooleanIterator> {
		private final String toString;

		protected Stimulus(String toString) {
			this.toString = toString;
		}

		abstract void executeAndCompare(BooleanListIterator reference, E target);

		@Override
		public String toString() {
			return toString;
		}
	}

	Stimulus<BooleanIterator> hasNext = new Stimulus<BooleanIterator>("hasNext") {
		@Override
		void executeAndCompare(BooleanListIterator reference, BooleanIterator target) {
			Assert.assertEquals(reference.hasNext(), target.hasNext());
		}
	};
	Stimulus<BooleanIterator> next = new Stimulus<BooleanIterator>("next") {
		@Override
		void executeAndCompare(BooleanListIterator reference, BooleanIterator target) {
			internalExecuteAndCompare(reference, target, NEXT_METHOD);
		}
	};
	Stimulus<BooleanIterator> remove = new Stimulus<BooleanIterator>("remove") {
		@Override
		void executeAndCompare(BooleanListIterator reference, BooleanIterator target) {
			internalExecuteAndCompare(reference, target, REMOVE_METHOD);
		}
	};

	@SuppressWarnings("unchecked")
	List<Stimulus<BooleanIterator>> iteratorStimuli() {
		return Arrays.asList(hasNext, next, remove);
	}
	
	Stimulus<BooleanBidirectionalIterator> hasPrevious = new Stimulus<BooleanBidirectionalIterator>("hasPrevious") {
		@Override
		void executeAndCompare(BooleanListIterator reference, BooleanBidirectionalIterator target) {
			Assert.assertEquals(reference.hasPrevious(), target.hasPrevious());
		}
	};
	Stimulus<BooleanBidirectionalIterator> previous = new Stimulus<BooleanBidirectionalIterator>("previous") {
		@Override
		void executeAndCompare(BooleanListIterator reference, BooleanBidirectionalIterator target) {
			internalExecuteAndCompare(reference, target, PREVIOUS_METHOD);
		}
	};
	
	List<Stimulus<BooleanBidirectionalIterator>> biIteratorStimuli() {
		return Arrays.asList(hasPrevious, previous);
	}
	
	Stimulus<BooleanListIterator> nextIndex = new Stimulus<BooleanListIterator>("nextIndex") {
		@Override
		void executeAndCompare(BooleanListIterator reference, BooleanListIterator target) {
			Assert.assertEquals(reference.nextIndex(), target.nextIndex());
		}
	};
	Stimulus<BooleanListIterator> previousIndex = new Stimulus<BooleanListIterator>("previousIndex") {
		@Override
		void executeAndCompare(BooleanListIterator reference, BooleanListIterator target) {
			Assert.assertEquals(reference.previousIndex(), target.previousIndex());
		}
	};
	Stimulus<BooleanListIterator> add = new Stimulus<BooleanListIterator>("add") {
		@Override
		void executeAndCompare(BooleanListIterator reference, BooleanListIterator target) {
			internalExecuteAndCompare(reference, target, newAddMethod());
		}
	};
	Stimulus<BooleanListIterator> set = new Stimulus<BooleanListIterator>("set") {
		@Override
		void executeAndCompare(BooleanListIterator reference, BooleanListIterator target) {
			internalExecuteAndCompare(reference, target, newSetMethod());
		}
	};

	List<Stimulus<BooleanListIterator>> listIteratorStimuli() {
		return Arrays.asList(nextIndex, previousIndex, add, set);
	}
}