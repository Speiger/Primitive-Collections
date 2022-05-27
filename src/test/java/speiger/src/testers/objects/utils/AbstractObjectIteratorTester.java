package speiger.src.testers.objects.utils;

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
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;

@SuppressWarnings("javadoc")
public abstract class AbstractObjectIteratorTester<T>
{
	private Stimulus<ObjectIterator<?>>[] stimuli;
	private final ObjectIterator<T> elementsToInsert;
	private final Set<IteratorFeature> features;
	private final ObjectList<T> expectedElements;
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

	protected final class MultiExceptionListIterator implements ObjectListIterator<T> {
		final ObjectArrayList<T> nextElements = new ObjectArrayList<>();
		final ObjectArrayList<T> previousElements = new ObjectArrayList<>();
		ObjectArrayList<T> stackWithLastReturnedElementAtTop = null;

		MultiExceptionListIterator(ObjectList<T> expectedElements) {
			ObjectHelpers.addAll(nextElements, ObjectHelpers.reverse(expectedElements));
			for (int i = 0; i < startIndex; i++) {
				previousElements.push(nextElements.pop());
			}
		}

		@Override
		public void add(T e) {
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
		public T next() {
			return transferElement(nextElements, previousElements);
		}

		@Override
		public int nextIndex() {
			return previousElements.size();
		}

		@Override
		public T previous() {
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
		public void set(T e) {
			throwIfInvalid(IteratorFeature.SUPPORTS_SET);

			stackWithLastReturnedElementAtTop.pop();
			stackWithLastReturnedElementAtTop.push(e);
		}

		void promoteToNext(T e) {
			if (nextElements.remove(e)) {
				nextElements.push(e);
			} else {
				throw new UnknownElementException(nextElements, e);
			}
		}

		private T transferElement(ObjectArrayList<T> source, ObjectArrayList<T> destination) {
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

		private ObjectList<T> getElements() {
			ObjectList<T> elements = new ObjectArrayList<>();
			ObjectHelpers.addAll(elements, previousElements);
			ObjectHelpers.addAll(elements, ObjectHelpers.reverse(nextElements));
			return elements;
		}
	}

	public enum KnownOrder {
		KNOWN_ORDER, UNKNOWN_ORDER
	}

	AbstractObjectIteratorTester(int steps, ObjectIterable<T> elementsToInsertIterable,
			Iterable<? extends IteratorFeature> features, ObjectIterable<T> expectedElements, KnownOrder knownOrder, int startIndex) {
		stimuli = new Stimulus[steps];
		if (!elementsToInsertIterable.iterator().hasNext()) {
			throw new IllegalArgumentException();
		}
		elementsToInsert = ObjectHelpers.cycle(elementsToInsertIterable);
		this.features = Helpers.copyToSet(features);
		this.expectedElements = ObjectHelpers.copyToList(expectedElements);
		this.knownOrder = knownOrder;
		this.startIndex = startIndex;
	}

	protected abstract Iterable<? extends Stimulus<ObjectIterator<?>>> getStimulusValues();

	protected abstract ObjectIterator<T> newTargetIterator();
	protected void verify(ObjectList<T> elements) {
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
			
			ObjectList<T> targetElements = new ObjectArrayList<>();
			ObjectIterator<T> iterator = newTargetIterator();
			for (int j = 0; j < i; j++) {
				targetElements.add(iterator.next());
			}
			iterator.forEachRemaining(targetElements::add);
			if (knownOrder == KnownOrder.KNOWN_ORDER) {
				Assert.assertEquals(expectedElements, targetElements);
			} else {
				ObjectHelpers.assertEqualIgnoringOrder(expectedElements, targetElements);
			}
		}
	}

	private void recurse(int level) {
		if (level == stimuli.length) {
			// We've filled the array.
			compareResultsForThisListOfStimuli();
		} else {
			// Keep recursing to fill the array.
			for (Stimulus<ObjectIterator<?>> stimulus : getStimulusValues()) {
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
		ObjectIterator<T> target = newTargetIterator();
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
		Object execute(ObjectIterator<?> iterator);
	}

	/**
	 * Apply this method to both iterators and return normally only if both
	 * produce the same response.
	 *
	 * @see Stimulus#executeAndCompare(ListIterator, Iterator)
	 */
	private <E extends ObjectIterator<?>> void internalExecuteAndCompare(E reference, E target, IteratorOperation method) {
		Object referenceReturnValue = null;
		PermittedMetaException referenceException = null;
		Object targetReturnValue = null;
		RuntimeException targetException = null;

		try {
			targetReturnValue = method.execute(target);
		} catch (RuntimeException e) {
			targetException = e;
		}

		try {
			if (method == NEXT_METHOD && targetException == null && knownOrder == KnownOrder.UNKNOWN_ORDER) {
				((MultiExceptionListIterator) reference).promoteToNext((T)targetReturnValue);
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
		public Object execute(ObjectIterator<?> iterator) {
			iterator.remove();
			return null;
		}
	};

	private static final IteratorOperation NEXT_METHOD = new IteratorOperation() {
		@Override
		public Object execute(ObjectIterator<?> iterator) {
			return iterator.next();
		}
	};

	private static final IteratorOperation PREVIOUS_METHOD = new IteratorOperation() {
		@Override
		public Object execute(ObjectIterator<?> iterator) {
			return ((ObjectBidirectionalIterator<?>) iterator).previous();
		}
	};

	private final IteratorOperation newAddMethod() {
		final Object toInsert = elementsToInsert.next();
		return new IteratorOperation() {
			@Override
			public Object execute(ObjectIterator<?> iterator) {
				@SuppressWarnings("unchecked")
				ObjectListIterator<Object> rawIterator = (ObjectListIterator<Object>) iterator;
				rawIterator.add(toInsert);
				return null;
			}
		};
	}

	private final IteratorOperation newSetMethod() {
		final Object toInsert = elementsToInsert.next();
		return new IteratorOperation() {
			@Override
			public Object execute(ObjectIterator<?> iterator) {
				@SuppressWarnings("unchecked")
				ObjectListIterator<Object> li = (ObjectListIterator<Object>) iterator;
				li.set(toInsert);
				return null;
			}
		};
	}

	abstract static class Stimulus<E extends ObjectIterator<?>> {
		private final String toString;

		protected Stimulus(String toString) {
			this.toString = toString;
		}

		abstract void executeAndCompare(ObjectListIterator<?> reference, E target);

		@Override
		public String toString() {
			return toString;
		}
	}

	Stimulus<ObjectIterator<?>> hasNext = new Stimulus<ObjectIterator<?>>("hasNext") {
		@Override
		void executeAndCompare(ObjectListIterator<?> reference, ObjectIterator<?> target) {
			Assert.assertEquals(reference.hasNext(), target.hasNext());
		}
	};
	Stimulus<ObjectIterator<?>> next = new Stimulus<ObjectIterator<?>>("next") {
		@Override
		void executeAndCompare(ObjectListIterator<?> reference, ObjectIterator<?> target) {
			internalExecuteAndCompare(reference, target, NEXT_METHOD);
		}
	};
	Stimulus<ObjectIterator<?>> remove = new Stimulus<ObjectIterator<?>>("remove") {
		@Override
		void executeAndCompare(ObjectListIterator<?> reference, ObjectIterator<?> target) {
			internalExecuteAndCompare(reference, target, REMOVE_METHOD);
		}
	};

	@SuppressWarnings("unchecked")
	List<Stimulus<ObjectIterator<?>>> iteratorStimuli() {
		return Arrays.asList(hasNext, next, remove);
	}
	
	Stimulus<ObjectBidirectionalIterator<?>> hasPrevious = new Stimulus<ObjectBidirectionalIterator<?>>("hasPrevious") {
		@Override
		void executeAndCompare(ObjectListIterator<?> reference, ObjectBidirectionalIterator<?> target) {
			Assert.assertEquals(reference.hasPrevious(), target.hasPrevious());
		}
	};
	Stimulus<ObjectBidirectionalIterator<?>> previous = new Stimulus<ObjectBidirectionalIterator<?>>("previous") {
		@Override
		void executeAndCompare(ObjectListIterator<?> reference, ObjectBidirectionalIterator<?> target) {
			internalExecuteAndCompare(reference, target, PREVIOUS_METHOD);
		}
	};
	
	List<Stimulus<ObjectBidirectionalIterator<?>>> biIteratorStimuli() {
		return Arrays.asList(hasPrevious, previous);
	}
	
	Stimulus<ObjectListIterator<?>> nextIndex = new Stimulus<ObjectListIterator<?>>("nextIndex") {
		@Override
		void executeAndCompare(ObjectListIterator<?> reference, ObjectListIterator<?> target) {
			Assert.assertEquals(reference.nextIndex(), target.nextIndex());
		}
	};
	Stimulus<ObjectListIterator<?>> previousIndex = new Stimulus<ObjectListIterator<?>>("previousIndex") {
		@Override
		void executeAndCompare(ObjectListIterator<?> reference, ObjectListIterator<?> target) {
			Assert.assertEquals(reference.previousIndex(), target.previousIndex());
		}
	};
	Stimulus<ObjectListIterator<?>> add = new Stimulus<ObjectListIterator<?>>("add") {
		@Override
		void executeAndCompare(ObjectListIterator<?> reference, ObjectListIterator<?> target) {
			internalExecuteAndCompare(reference, target, newAddMethod());
		}
	};
	Stimulus<ObjectListIterator<?>> set = new Stimulus<ObjectListIterator<?>>("set") {
		@Override
		void executeAndCompare(ObjectListIterator<?> reference, ObjectListIterator<?> target) {
			internalExecuteAndCompare(reference, target, newSetMethod());
		}
	};

	List<Stimulus<ObjectListIterator<?>>> listIteratorStimuli() {
		return Arrays.asList(nextIndex, previousIndex, add, set);
	}
}