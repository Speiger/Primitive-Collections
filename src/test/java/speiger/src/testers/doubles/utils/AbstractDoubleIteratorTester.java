package speiger.src.testers.doubles.utils;

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
import speiger.src.collections.doubles.collections.DoubleBidirectionalIterator;
import speiger.src.collections.doubles.collections.DoubleIterable;
import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.lists.DoubleArrayList;
import speiger.src.collections.doubles.lists.DoubleList;
import speiger.src.collections.doubles.lists.DoubleListIterator;

@SuppressWarnings("javadoc")
public abstract class AbstractDoubleIteratorTester
{
	private Stimulus<DoubleIterator>[] stimuli;
	private final DoubleIterator elementsToInsert;
	private final Set<IteratorFeature> features;
	private final DoubleList expectedElements;
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

	protected final class MultiExceptionListIterator implements DoubleListIterator {
		final DoubleArrayList nextElements = new DoubleArrayList();
		final DoubleArrayList previousElements = new DoubleArrayList();
		DoubleArrayList stackWithLastReturnedElementAtTop = null;

		MultiExceptionListIterator(DoubleList expectedElements) {
			DoubleHelpers.addAll(nextElements, DoubleHelpers.reverse(expectedElements));
			for (int i = 0; i < startIndex; i++) {
				previousElements.push(nextElements.pop());
			}
		}

		@Override
		public void add(double e) {
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
		public double nextDouble() {
			return transferElement(nextElements, previousElements);
		}

		@Override
		public int nextIndex() {
			return previousElements.size();
		}

		@Override
		public double previousDouble() {
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
		public void set(double e) {
			throwIfInvalid(IteratorFeature.SUPPORTS_SET);

			stackWithLastReturnedElementAtTop.pop();
			stackWithLastReturnedElementAtTop.push(e);
		}

		void promoteToNext(double e) {
			if (nextElements.remDouble(e)) {
				nextElements.push(e);
			} else {
				throw new UnknownElementException(nextElements, e);
			}
		}

		private double transferElement(DoubleArrayList source, DoubleArrayList destination) {
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

		private DoubleList getElements() {
			DoubleList elements = new DoubleArrayList();
			DoubleHelpers.addAll(elements, previousElements);
			DoubleHelpers.addAll(elements, DoubleHelpers.reverse(nextElements));
			return elements;
		}
	}

	public enum KnownOrder {
		KNOWN_ORDER, UNKNOWN_ORDER
	}

	AbstractDoubleIteratorTester(int steps, DoubleIterable elementsToInsertIterable,
			Iterable<? extends IteratorFeature> features, DoubleIterable expectedElements, KnownOrder knownOrder, int startIndex) {
		stimuli = new Stimulus[steps];
		if (!elementsToInsertIterable.iterator().hasNext()) {
			throw new IllegalArgumentException();
		}
		elementsToInsert = DoubleHelpers.cycle(elementsToInsertIterable);
		this.features = Helpers.copyToSet(features);
		this.expectedElements = DoubleHelpers.copyToList(expectedElements);
		this.knownOrder = knownOrder;
		this.startIndex = startIndex;
	}

	protected abstract Iterable<? extends Stimulus<DoubleIterator>> getStimulusValues();

	protected abstract DoubleIterator newTargetIterator();
	protected void verify(DoubleList elements) {
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
			
			DoubleList targetElements = new DoubleArrayList();
			DoubleIterator iterator = newTargetIterator();
			for (int j = 0; j < i; j++) {
				targetElements.add(iterator.nextDouble());
			}
			iterator.forEachRemaining(targetElements::add);
			if (knownOrder == KnownOrder.KNOWN_ORDER) {
				Assert.assertEquals(expectedElements, targetElements);
			} else {
				DoubleHelpers.assertEqualIgnoringOrder(expectedElements, targetElements);
			}
		}
	}

	private void recurse(int level) {
		if (level == stimuli.length) {
			// We've filled the array.
			compareResultsForThisListOfStimuli();
		} else {
			// Keep recursing to fill the array.
			for (Stimulus<DoubleIterator> stimulus : getStimulusValues()) {
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
		DoubleIterator target = newTargetIterator();
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
		double execute(DoubleIterator iterator);
	}

	/**
	 * Apply this method to both iterators and return normally only if both
	 * produce the same response.
	 *
	 * @see Stimulus#executeAndCompare(ListIterator, Iterator)
	 */
	private <E extends DoubleIterator> void internalExecuteAndCompare(E reference, E target, IteratorOperation method) {
		double referenceReturnValue = -1D;
		PermittedMetaException referenceException = null;
		double targetReturnValue = -1D;
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
			Assert.assertEquals(Double.doubleToLongBits(referenceReturnValue), Double.doubleToLongBits(targetReturnValue));
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
		public double execute(DoubleIterator iterator) {
			iterator.remove();
			return -1D;
		}
	};

	private static final IteratorOperation NEXT_METHOD = new IteratorOperation() {
		@Override
		public double execute(DoubleIterator iterator) {
			return iterator.nextDouble();
		}
	};

	private static final IteratorOperation PREVIOUS_METHOD = new IteratorOperation() {
		@Override
		public double execute(DoubleIterator iterator) {
			return ((DoubleBidirectionalIterator) iterator).previousDouble();
		}
	};

	private final IteratorOperation newAddMethod() {
		final double toInsert = elementsToInsert.nextDouble();
		return new IteratorOperation() {
			@Override
			public double execute(DoubleIterator iterator) {
				@SuppressWarnings("unchecked")
				DoubleListIterator rawIterator = (DoubleListIterator) iterator;
				rawIterator.add(toInsert);
				return -1D;
			}
		};
	}

	private final IteratorOperation newSetMethod() {
		final double toInsert = elementsToInsert.nextDouble();
		return new IteratorOperation() {
			@Override
			public double execute(DoubleIterator iterator) {
				@SuppressWarnings("unchecked")
				DoubleListIterator li = (DoubleListIterator) iterator;
				li.set(toInsert);
				return -1D;
			}
		};
	}

	abstract static class Stimulus<E extends DoubleIterator> {
		private final String toString;

		protected Stimulus(String toString) {
			this.toString = toString;
		}

		abstract void executeAndCompare(DoubleListIterator reference, E target);

		@Override
		public String toString() {
			return toString;
		}
	}

	Stimulus<DoubleIterator> hasNext = new Stimulus<DoubleIterator>("hasNext") {
		@Override
		void executeAndCompare(DoubleListIterator reference, DoubleIterator target) {
			Assert.assertEquals(reference.hasNext(), target.hasNext());
		}
	};
	Stimulus<DoubleIterator> next = new Stimulus<DoubleIterator>("next") {
		@Override
		void executeAndCompare(DoubleListIterator reference, DoubleIterator target) {
			internalExecuteAndCompare(reference, target, NEXT_METHOD);
		}
	};
	Stimulus<DoubleIterator> remove = new Stimulus<DoubleIterator>("remove") {
		@Override
		void executeAndCompare(DoubleListIterator reference, DoubleIterator target) {
			internalExecuteAndCompare(reference, target, REMOVE_METHOD);
		}
	};

	@SuppressWarnings("unchecked")
	List<Stimulus<DoubleIterator>> iteratorStimuli() {
		return Arrays.asList(hasNext, next, remove);
	}
	
	Stimulus<DoubleBidirectionalIterator> hasPrevious = new Stimulus<DoubleBidirectionalIterator>("hasPrevious") {
		@Override
		void executeAndCompare(DoubleListIterator reference, DoubleBidirectionalIterator target) {
			Assert.assertEquals(reference.hasPrevious(), target.hasPrevious());
		}
	};
	Stimulus<DoubleBidirectionalIterator> previous = new Stimulus<DoubleBidirectionalIterator>("previous") {
		@Override
		void executeAndCompare(DoubleListIterator reference, DoubleBidirectionalIterator target) {
			internalExecuteAndCompare(reference, target, PREVIOUS_METHOD);
		}
	};
	
	List<Stimulus<DoubleBidirectionalIterator>> biIteratorStimuli() {
		return Arrays.asList(hasPrevious, previous);
	}
	
	Stimulus<DoubleListIterator> nextIndex = new Stimulus<DoubleListIterator>("nextIndex") {
		@Override
		void executeAndCompare(DoubleListIterator reference, DoubleListIterator target) {
			Assert.assertEquals(reference.nextIndex(), target.nextIndex());
		}
	};
	Stimulus<DoubleListIterator> previousIndex = new Stimulus<DoubleListIterator>("previousIndex") {
		@Override
		void executeAndCompare(DoubleListIterator reference, DoubleListIterator target) {
			Assert.assertEquals(reference.previousIndex(), target.previousIndex());
		}
	};
	Stimulus<DoubleListIterator> add = new Stimulus<DoubleListIterator>("add") {
		@Override
		void executeAndCompare(DoubleListIterator reference, DoubleListIterator target) {
			internalExecuteAndCompare(reference, target, newAddMethod());
		}
	};
	Stimulus<DoubleListIterator> set = new Stimulus<DoubleListIterator>("set") {
		@Override
		void executeAndCompare(DoubleListIterator reference, DoubleListIterator target) {
			internalExecuteAndCompare(reference, target, newSetMethod());
		}
	};

	List<Stimulus<DoubleListIterator>> listIteratorStimuli() {
		return Arrays.asList(nextIndex, previousIndex, add, set);
	}
}