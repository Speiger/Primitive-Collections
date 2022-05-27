package speiger.src.testers.chars.utils;

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
import speiger.src.collections.chars.collections.CharBidirectionalIterator;
import speiger.src.collections.chars.collections.CharIterable;
import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.collections.chars.lists.CharArrayList;
import speiger.src.collections.chars.lists.CharList;
import speiger.src.collections.chars.lists.CharListIterator;

@SuppressWarnings("javadoc")
public abstract class AbstractCharIteratorTester
{
	private Stimulus<CharIterator>[] stimuli;
	private final CharIterator elementsToInsert;
	private final Set<IteratorFeature> features;
	private final CharList expectedElements;
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

	protected final class MultiExceptionListIterator implements CharListIterator {
		final CharArrayList nextElements = new CharArrayList();
		final CharArrayList previousElements = new CharArrayList();
		CharArrayList stackWithLastReturnedElementAtTop = null;

		MultiExceptionListIterator(CharList expectedElements) {
			CharHelpers.addAll(nextElements, CharHelpers.reverse(expectedElements));
			for (int i = 0; i < startIndex; i++) {
				previousElements.push(nextElements.pop());
			}
		}

		@Override
		public void add(char e) {
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
		public char nextChar() {
			return transferElement(nextElements, previousElements);
		}

		@Override
		public int nextIndex() {
			return previousElements.size();
		}

		@Override
		public char previousChar() {
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
		public void set(char e) {
			throwIfInvalid(IteratorFeature.SUPPORTS_SET);

			stackWithLastReturnedElementAtTop.pop();
			stackWithLastReturnedElementAtTop.push(e);
		}

		void promoteToNext(char e) {
			if (nextElements.remChar(e)) {
				nextElements.push(e);
			} else {
				throw new UnknownElementException(nextElements, e);
			}
		}

		private char transferElement(CharArrayList source, CharArrayList destination) {
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

		private CharList getElements() {
			CharList elements = new CharArrayList();
			CharHelpers.addAll(elements, previousElements);
			CharHelpers.addAll(elements, CharHelpers.reverse(nextElements));
			return elements;
		}
	}

	public enum KnownOrder {
		KNOWN_ORDER, UNKNOWN_ORDER
	}

	AbstractCharIteratorTester(int steps, CharIterable elementsToInsertIterable,
			Iterable<? extends IteratorFeature> features, CharIterable expectedElements, KnownOrder knownOrder, int startIndex) {
		stimuli = new Stimulus[steps];
		if (!elementsToInsertIterable.iterator().hasNext()) {
			throw new IllegalArgumentException();
		}
		elementsToInsert = CharHelpers.cycle(elementsToInsertIterable);
		this.features = Helpers.copyToSet(features);
		this.expectedElements = CharHelpers.copyToList(expectedElements);
		this.knownOrder = knownOrder;
		this.startIndex = startIndex;
	}

	protected abstract Iterable<? extends Stimulus<CharIterator>> getStimulusValues();

	protected abstract CharIterator newTargetIterator();
	protected void verify(CharList elements) {
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
			
			CharList targetElements = new CharArrayList();
			CharIterator iterator = newTargetIterator();
			for (int j = 0; j < i; j++) {
				targetElements.add(iterator.nextChar());
			}
			iterator.forEachRemaining(targetElements::add);
			if (knownOrder == KnownOrder.KNOWN_ORDER) {
				Assert.assertEquals(expectedElements, targetElements);
			} else {
				CharHelpers.assertEqualIgnoringOrder(expectedElements, targetElements);
			}
		}
	}

	private void recurse(int level) {
		if (level == stimuli.length) {
			// We've filled the array.
			compareResultsForThisListOfStimuli();
		} else {
			// Keep recursing to fill the array.
			for (Stimulus<CharIterator> stimulus : getStimulusValues()) {
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
		CharIterator target = newTargetIterator();
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
		char execute(CharIterator iterator);
	}

	/**
	 * Apply this method to both iterators and return normally only if both
	 * produce the same response.
	 *
	 * @see Stimulus#executeAndCompare(ListIterator, Iterator)
	 */
	private <E extends CharIterator> void internalExecuteAndCompare(E reference, E target, IteratorOperation method) {
		char referenceReturnValue = (char)-1;
		PermittedMetaException referenceException = null;
		char targetReturnValue = (char)-1;
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
		public char execute(CharIterator iterator) {
			iterator.remove();
			return (char)-1;
		}
	};

	private static final IteratorOperation NEXT_METHOD = new IteratorOperation() {
		@Override
		public char execute(CharIterator iterator) {
			return iterator.nextChar();
		}
	};

	private static final IteratorOperation PREVIOUS_METHOD = new IteratorOperation() {
		@Override
		public char execute(CharIterator iterator) {
			return ((CharBidirectionalIterator) iterator).previousChar();
		}
	};

	private final IteratorOperation newAddMethod() {
		final char toInsert = elementsToInsert.nextChar();
		return new IteratorOperation() {
			@Override
			public char execute(CharIterator iterator) {
				@SuppressWarnings("unchecked")
				CharListIterator rawIterator = (CharListIterator) iterator;
				rawIterator.add(toInsert);
				return (char)-1;
			}
		};
	}

	private final IteratorOperation newSetMethod() {
		final char toInsert = elementsToInsert.nextChar();
		return new IteratorOperation() {
			@Override
			public char execute(CharIterator iterator) {
				@SuppressWarnings("unchecked")
				CharListIterator li = (CharListIterator) iterator;
				li.set(toInsert);
				return (char)-1;
			}
		};
	}

	abstract static class Stimulus<E extends CharIterator> {
		private final String toString;

		protected Stimulus(String toString) {
			this.toString = toString;
		}

		abstract void executeAndCompare(CharListIterator reference, E target);

		@Override
		public String toString() {
			return toString;
		}
	}

	Stimulus<CharIterator> hasNext = new Stimulus<CharIterator>("hasNext") {
		@Override
		void executeAndCompare(CharListIterator reference, CharIterator target) {
			Assert.assertEquals(reference.hasNext(), target.hasNext());
		}
	};
	Stimulus<CharIterator> next = new Stimulus<CharIterator>("next") {
		@Override
		void executeAndCompare(CharListIterator reference, CharIterator target) {
			internalExecuteAndCompare(reference, target, NEXT_METHOD);
		}
	};
	Stimulus<CharIterator> remove = new Stimulus<CharIterator>("remove") {
		@Override
		void executeAndCompare(CharListIterator reference, CharIterator target) {
			internalExecuteAndCompare(reference, target, REMOVE_METHOD);
		}
	};

	@SuppressWarnings("unchecked")
	List<Stimulus<CharIterator>> iteratorStimuli() {
		return Arrays.asList(hasNext, next, remove);
	}
	
	Stimulus<CharBidirectionalIterator> hasPrevious = new Stimulus<CharBidirectionalIterator>("hasPrevious") {
		@Override
		void executeAndCompare(CharListIterator reference, CharBidirectionalIterator target) {
			Assert.assertEquals(reference.hasPrevious(), target.hasPrevious());
		}
	};
	Stimulus<CharBidirectionalIterator> previous = new Stimulus<CharBidirectionalIterator>("previous") {
		@Override
		void executeAndCompare(CharListIterator reference, CharBidirectionalIterator target) {
			internalExecuteAndCompare(reference, target, PREVIOUS_METHOD);
		}
	};
	
	List<Stimulus<CharBidirectionalIterator>> biIteratorStimuli() {
		return Arrays.asList(hasPrevious, previous);
	}
	
	Stimulus<CharListIterator> nextIndex = new Stimulus<CharListIterator>("nextIndex") {
		@Override
		void executeAndCompare(CharListIterator reference, CharListIterator target) {
			Assert.assertEquals(reference.nextIndex(), target.nextIndex());
		}
	};
	Stimulus<CharListIterator> previousIndex = new Stimulus<CharListIterator>("previousIndex") {
		@Override
		void executeAndCompare(CharListIterator reference, CharListIterator target) {
			Assert.assertEquals(reference.previousIndex(), target.previousIndex());
		}
	};
	Stimulus<CharListIterator> add = new Stimulus<CharListIterator>("add") {
		@Override
		void executeAndCompare(CharListIterator reference, CharListIterator target) {
			internalExecuteAndCompare(reference, target, newAddMethod());
		}
	};
	Stimulus<CharListIterator> set = new Stimulus<CharListIterator>("set") {
		@Override
		void executeAndCompare(CharListIterator reference, CharListIterator target) {
			internalExecuteAndCompare(reference, target, newSetMethod());
		}
	};

	List<Stimulus<CharListIterator>> listIteratorStimuli() {
		return Arrays.asList(nextIndex, previousIndex, add, set);
	}
}