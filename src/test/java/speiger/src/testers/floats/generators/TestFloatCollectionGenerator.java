package speiger.src.testers.floats.generators;

import java.util.List;

import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestCollectionGenerator;

import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.collections.FloatIterable;
import speiger.src.collections.floats.lists.FloatList;
import speiger.src.testers.floats.utils.FloatSamples;

@SuppressWarnings("javadoc")
public interface TestFloatCollectionGenerator extends TestCollectionGenerator<Float>
{
	public FloatSamples getSamples();
	public FloatCollection create(float...elements);
	public FloatIterable order(FloatList insertionOrder);
	
	@Override
	public default SampleElements<Float> samples() {return getSamples().toSamples();}
	@Override
	public FloatCollection create(Object... elements);
	@Override
	public default Float[] createArray(int length) { return new Float[length]; }
	@Override
	public Iterable<Float> order(List<Float> insertionOrder);
}