package speiger.src.testers.doubles.generators;

import java.util.List;

import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestCollectionGenerator;

import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.collections.DoubleIterable;
import speiger.src.collections.doubles.lists.DoubleList;
import speiger.src.testers.doubles.utils.DoubleSamples;

public interface TestDoubleCollectionGenerator extends TestCollectionGenerator<Double>
{
	public DoubleSamples getSamples();
	public DoubleCollection create(double...elements);
	public DoubleIterable order(DoubleList insertionOrder);
	
	@Override
	public default SampleElements<Double> samples() {return getSamples().toSamples();}
	@Override
	public DoubleCollection create(Object... elements);
	@Override
	public default Double[] createArray(int length) { return new Double[length]; }
	@Override
	public Iterable<Double> order(List<Double> insertionOrder);
}