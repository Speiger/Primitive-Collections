package speiger.src.testers.booleans.generators;

import java.util.List;

import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestCollectionGenerator;

import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.collections.BooleanIterable;
import speiger.src.collections.booleans.lists.BooleanList;
import speiger.src.testers.booleans.utils.BooleanSamples;

@SuppressWarnings("javadoc")
public interface TestBooleanCollectionGenerator extends TestCollectionGenerator<Boolean>
{
	public BooleanSamples getSamples();
	public BooleanCollection create(boolean...elements);
	public BooleanIterable order(BooleanList insertionOrder);
	
	@Override
	public default SampleElements<Boolean> samples() {return getSamples().toSamples();}
	@Override
	public BooleanCollection create(Object... elements);
	@Override
	public default Boolean[] createArray(int length) { return new Boolean[length]; }
	@Override
	public Iterable<Boolean> order(List<Boolean> insertionOrder);
}