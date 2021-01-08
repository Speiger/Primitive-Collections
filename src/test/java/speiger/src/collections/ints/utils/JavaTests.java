package speiger.src.collections.ints.utils;

import java.util.Arrays;
import java.util.TreeSet;
import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Test;

public class JavaTests
{
	protected static final Integer[] CONTAINS_ARRAY = new Integer[]{23, 45, 63, 89, 32};
	protected static final Integer[] TEST_ARRAY = IntStream.range(0, 100).mapToObj(Integer::valueOf).toArray(Integer[]::new);
	
	private TreeSet<Integer> create(Integer[] array)
	{
		TreeSet<Integer> tree = new TreeSet<Integer>();
		tree.addAll(Arrays.asList(array));
		return tree;
	}
	
	@Test
	public void simpleTest()
	{
		TreeSet<Integer> collection = create(TEST_ARRAY);
		Assert.assertTrue(collection.removeAll(create(CONTAINS_ARRAY)));
		Assert.assertFalse(collection.removeAll(create(CONTAINS_ARRAY)));
	}
}
