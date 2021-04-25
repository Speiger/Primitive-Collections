package speiger.src.collections.ints.base;

import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Test;

import speiger.src.collections.ints.utils.IIntArray;

@SuppressWarnings("javadoc")
public interface IIntArrayTest
{
	static final int[] TEST_ARRAY = IntStream.range(0, 100).toArray();
	public IIntArray create(int[] data);
	
	@Test
	public default void testEnsureCapacity()
	{
		IIntArray array = create(TEST_ARRAY);
		array.ensureCapacity(2500);
		Assert.assertTrue(array.elements().length >= 2500);
	}
	
	@Test
	public default void testElements()
	{
		IIntArray array = create(TEST_ARRAY);
		Assert.assertArrayEquals(TEST_ARRAY, array.elements());
		array.elements(T -> Assert.assertArrayEquals(TEST_ARRAY, T));
	}
	
	@Test
	public default void testTrim()
	{
		IIntArray array = create(TEST_ARRAY);
		array.ensureCapacity(2500);
		Assert.assertNotEquals(TEST_ARRAY.length, array.elements().length);
		array.trim();
		Assert.assertEquals(TEST_ARRAY.length, array.elements().length);		
	}
}
