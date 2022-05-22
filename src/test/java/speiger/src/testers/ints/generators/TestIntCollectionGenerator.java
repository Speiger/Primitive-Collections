package speiger.src.testers.ints.generators;

import java.util.List;

import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestCollectionGenerator;

import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.collections.IntIterable;
import speiger.src.collections.ints.lists.IntList;
import speiger.src.testers.ints.utils.IntSamples;

public interface TestIntCollectionGenerator extends TestCollectionGenerator<Integer>
{
	public IntSamples getSamples();
	public IntCollection create(int...elements);
	public IntIterable order(IntList insertionOrder);
	
	@Override
	public default SampleElements<Integer> samples() {return getSamples().toSamples();}
	@Override
	public IntCollection create(Object... elements);
	@Override
	public default Integer[] createArray(int length) { return new Integer[length]; }
	@Override
	public Iterable<Integer> order(List<Integer> insertionOrder);
}