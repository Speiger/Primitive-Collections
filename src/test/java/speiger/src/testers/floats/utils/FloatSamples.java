package speiger.src.testers.floats.utils;

import com.google.common.collect.testing.SampleElements;

import speiger.src.collections.floats.collections.FloatIterable;
import speiger.src.collections.floats.collections.FloatIterator;
import speiger.src.collections.floats.lists.FloatArrayList;
import speiger.src.collections.floats.lists.FloatList;

@SuppressWarnings("javadoc")
public class FloatSamples implements FloatIterable
{
	private final float e0;
	private final float e1;
	private final float e2;
	private final float e3;
	private final float e4;
	
	public FloatSamples(SampleElements<Float> samples)
	{
		this(samples.e0(), samples.e1(), samples.e2(), samples.e3(), samples.e4());
	}
	
	public FloatSamples(float e0, float e1, float e2, float e3, float e4)
	{
		this.e0 = e0;
		this.e1 = e1;
		this.e2 = e2;
		this.e3 = e3;
		this.e4 = e4;
	}
	
	public SampleElements<Float> toSamples()
	{
		return new SampleElements<>(e0(), e1(), e2(), e3(), e4());
	}
	
	@Override
	public FloatIterator iterator()
	{
		return asList().iterator();
	}
	
	public FloatList asList()
	{
		return FloatArrayList.wrap(e0(), e1(), e2(), e3(), e4());
	}
	
	public float e0()
	{
		return e0;
	}
	
	public float e1()
	{
		return e1;
	}
	
	public float e2()
	{
		return e2;
	}
	
	public float e3()
	{
		return e3;
	}
	
	public float e4()
	{
		return e4;
	}
}