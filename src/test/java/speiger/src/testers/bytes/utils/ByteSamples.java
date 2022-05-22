package speiger.src.testers.bytes.utils;

import com.google.common.collect.testing.SampleElements;

import speiger.src.collections.bytes.collections.ByteIterable;
import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.bytes.lists.ByteArrayList;
import speiger.src.collections.bytes.lists.ByteList;

public class ByteSamples implements ByteIterable
{
	private final byte e0;
	private final byte e1;
	private final byte e2;
	private final byte e3;
	private final byte e4;
	
	public ByteSamples(SampleElements<Byte> samples)
	{
		this(samples.e0(), samples.e1(), samples.e2(), samples.e3(), samples.e4());
	}
	
	public ByteSamples(byte e0, byte e1, byte e2, byte e3, byte e4)
	{
		this.e0 = e0;
		this.e1 = e1;
		this.e2 = e2;
		this.e3 = e3;
		this.e4 = e4;
	}
	
	public SampleElements<Byte> toSamples()
	{
		return new SampleElements<>(e0(), e1(), e2(), e3(), e4());
	}
	
	@Override
	public ByteIterator iterator()
	{
		return asList().iterator();
	}
	
	public ByteList asList()
	{
		return ByteArrayList.wrap(e0(), e1(), e2(), e3(), e4());
	}
	
	public byte e0()
	{
		return e0;
	}
	
	public byte e1()
	{
		return e1;
	}
	
	public byte e2()
	{
		return e2;
	}
	
	public byte e3()
	{
		return e3;
	}
	
	public byte e4()
	{
		return e4;
	}
}