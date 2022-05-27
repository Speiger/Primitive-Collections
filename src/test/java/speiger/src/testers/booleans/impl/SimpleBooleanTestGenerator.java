package speiger.src.testers.booleans.impl;

import java.util.List;
import java.util.function.Function;

import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.collections.BooleanIterable;
import speiger.src.collections.booleans.lists.BooleanList;
import speiger.src.testers.booleans.generators.TestBooleanCollectionGenerator;
import speiger.src.testers.booleans.generators.TestBooleanListGenerator;
import speiger.src.testers.booleans.utils.BooleanSamples;

@SuppressWarnings("javadoc")
public class SimpleBooleanTestGenerator<E extends BooleanCollection> {
	Function<boolean[], E> mapper;
	public SimpleBooleanTestGenerator(Function<boolean[], E> mapper) {
		this.mapper = mapper;
	}
	
	public BooleanSamples getSamples() {
		return new BooleanSamples(true, false, true, false, true);
	}
	
	public E create(boolean... elements) {
		return mapper.apply(elements);
	}
	
	public E create(Object... elements) {
		boolean[] array = new boolean[elements.length];
		int i = 0;
		for (Object e : elements) {
			array[i++] = ((Boolean)e).booleanValue();
		}
		return mapper.apply(array);
	}
	
	public BooleanIterable order(BooleanList insertionOrder) {
		return insertionOrder;
	}
	
	public Iterable<Boolean> order(List<Boolean> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Collections extends SimpleBooleanTestGenerator<BooleanCollection> implements TestBooleanCollectionGenerator
	{
		public Collections(Function<boolean[], BooleanCollection> mapper) {
			super(mapper);
		}
	}
	
	public static class Lists extends SimpleBooleanTestGenerator<BooleanList> implements TestBooleanListGenerator
	{
		public Lists(Function<boolean[], BooleanList> mapper) {
			super(mapper);
		}
	}
	
}