package speiger.src.testers.doubles.utils;

import com.google.common.collect.testing.SampleElements;

import speiger.src.collections.doubles.collections.DoubleIterable;
import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.lists.DoubleArrayList;
import speiger.src.collections.doubles.lists.DoubleList;

public class DoubleSamples implements DoubleIterable
{
	private final double e0;
	private final double e1;
	private final double e2;
	private final double e3;
	private final double e4;
	
	public DoubleSamples(SampleElements<Double> samples)
	{
		this(samples.e0(), samples.e1(), samples.e2(), samples.e3(), samples.e4());
	}
	
	public DoubleSamples(double e0, double e1, double e2, double e3, double e4)
	{
		this.e0 = e0;
		this.e1 = e1;
		this.e2 = e2;
		this.e3 = e3;
		this.e4 = e4;
	}
	
	public SampleElements<Double> toSamples()
	{
		return new SampleElements<>(e0(), e1(), e2(), e3(), e4());
	}
	
	@Override
	public DoubleIterator iterator()
	{
		return asList().iterator();
	}
	
	public DoubleList asList()
	{
		return DoubleArrayList.wrap(e0(), e1(), e2(), e3(), e4());
	}
	
	public double e0()
	{
		return e0;
	}
	
	public double e1()
	{
		return e1;
	}
	
	public double e2()
	{
		return e2;
	}
	
	public double e3()
	{
		return e3;
	}
	
	public double e4()
	{
		return e4;
	}
}