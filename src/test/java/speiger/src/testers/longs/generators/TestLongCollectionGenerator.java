package speiger.src.testers.longs.generators;

import java.util.List;

import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestCollectionGenerator;

import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.longs.collections.LongIterable;
import speiger.src.collections.longs.lists.LongList;
import speiger.src.testers.longs.utils.LongSamples;

public interface TestLongCollectionGenerator extends TestCollectionGenerator<Long>
{
	public LongSamples getSamples();
	public LongCollection create(long...elements);
	public LongIterable order(LongList insertionOrder);
	
	@Override
	public default SampleElements<Long> samples() {return getSamples().toSamples();}
	@Override
	public LongCollection create(Object... elements);
	@Override
	public default Long[] createArray(int length) { return new Long[length]; }
	@Override
	public Iterable<Long> order(List<Long> insertionOrder);
}