package speiger.src.testers.shorts.generators;

import java.util.List;

import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestCollectionGenerator;

import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.collections.ShortIterable;
import speiger.src.collections.shorts.lists.ShortList;
import speiger.src.testers.shorts.utils.ShortSamples;

@SuppressWarnings("javadoc")
public interface TestShortCollectionGenerator extends TestCollectionGenerator<Short>
{
	public ShortSamples getSamples();
	public ShortCollection create(short...elements);
	public ShortIterable order(ShortList insertionOrder);
	
	@Override
	public default SampleElements<Short> samples() {return getSamples().toSamples();}
	@Override
	public ShortCollection create(Object... elements);
	@Override
	public default Short[] createArray(int length) { return new Short[length]; }
	@Override
	public Iterable<Short> order(List<Short> insertionOrder);
}