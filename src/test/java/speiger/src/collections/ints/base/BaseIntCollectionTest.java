package speiger.src.collections.ints.base;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Test;

import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.utils.IntArrays;
import speiger.src.collections.ints.utils.IntCollections;
import speiger.src.collections.ints.utils.IntCollections.SynchronizedCollection;
import speiger.src.collections.ints.utils.IntCollections.UnmodifiableCollection;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.tests.CollectionTest;

@SuppressWarnings("javadoc")
public abstract class BaseIntCollectionTest extends BaseIntIterableTest
{
	protected static final int[] ADD_ARRAY = new int[]{3212, -12, 423, -182, -4912};
	protected static final int[] BULK_ADD_ARRAY = IntStream.range(200, 500).toArray();
	protected static final int[] CONTAINS_ARRAY = new int[]{23, 45, 63, 89, 32};
	
	@Override
	protected abstract IntCollection create(int[] data);
	
	protected EnumSet<CollectionTest> getValidCollectionTests() { return EnumSet.allOf(CollectionTest.class); }
	
	@Test
	public void testAdd() {
		if(!getValidCollectionTests().contains(CollectionTest.ADD)) return;
		IntCollection collection = create(EMPTY_ARRAY);
		Assert.assertTrue(collection.isEmpty());
		collection.add(2012);
		Assert.assertFalse(collection.isEmpty());
		Assert.assertTrue(collection.contains(2012));
	}
	
	@Test
	public void testAddAll() {
		if(!getValidCollectionTests().contains(CollectionTest.ADD_ALL)) return;
		IntCollection collection = create(TEST_ARRAY);
		Assert.assertEquals(TEST_ARRAY.length, collection.size());
		collection.addAll(create(ADD_ARRAY));
		Assert.assertEquals(TEST_ARRAY.length + ADD_ARRAY.length, collection.size());
		collection = create(TEST_ARRAY);
		Assert.assertEquals(TEST_ARRAY.length, collection.size());
		collection.addAll(create(BULK_ADD_ARRAY));
		Assert.assertEquals(TEST_ARRAY.length + BULK_ADD_ARRAY.length, collection.size());
		// Testing if adding via non Type Specific work like they should
		List<Integer> wrapper = new ObjectArrayList<>(create(ADD_ARRAY));
		Assert.assertNotNull(create(ADD_ARRAY).addAll(wrapper));
	}
	
	@Test
	@SuppressWarnings("deprecation")
	public void testContains() {
		if(!getValidCollectionTests().contains(CollectionTest.CONTAINS)) return;
		IntCollection collection = create(TEST_ARRAY);
		Assert.assertTrue(collection.contains(50));
		Assert.assertFalse(collection.contains(5120));
		Assert.assertTrue(collection.contains(Integer.valueOf(50)));
	}
	
	@Test
	public void testContainsAll() {
		if(!getValidCollectionTests().contains(CollectionTest.CONTAINS_ALL)) return;
		IntCollection collection = create(TEST_ARRAY);
		Assert.assertTrue(collection.containsAll(create(CONTAINS_ARRAY)));
		Assert.assertFalse(collection.containsAll(create(ADD_ARRAY)));
		Assert.assertTrue(collection.containsAll(Arrays.asList(IntArrays.wrap(CONTAINS_ARRAY))));
	}
	
	@Test
	@SuppressWarnings("deprecation")
	public void testContainsAny() {
		if(!getValidCollectionTests().contains(CollectionTest.CONTAINS_ANY)) return;
		IntCollection collection = create(TEST_ARRAY);
		Assert.assertTrue(collection.containsAny(create(CONTAINS_ARRAY)));
		Assert.assertFalse(collection.containsAny(create(ADD_ARRAY)));
		Assert.assertTrue(collection.containsAny(Arrays.asList(IntArrays.wrap(CONTAINS_ARRAY))));
	}
	
	@Test
	@SuppressWarnings("deprecation")
	public void testRemove() {
		if(!getValidCollectionTests().contains(CollectionTest.REMOVE)) return;
		IntCollection collection = create(TEST_ARRAY);
		Assert.assertTrue(collection.remInt(50));
		Assert.assertFalse(collection.remInt(50));
		Assert.assertTrue(collection.remove(Integer.valueOf(75)));
	}
	
	@Test
	@SuppressWarnings("deprecation")
	public void testRemoveIf() {
		if(!getValidCollectionTests().contains(CollectionTest.REMOVE_IF)) return;
		IntCollection collection = create(TEST_ARRAY);
		Assert.assertTrue(collection.remIf(T -> T != 25));
		Assert.assertFalse(collection.remIf(T -> T != 25));
		Assert.assertEquals(1, collection.size());
		collection = create(TEST_ARRAY);
		Assert.assertTrue(collection.removeIf(T -> T.intValue() != 25));
		Assert.assertFalse(collection.removeIf(T -> T.intValue() != 25));
		Assert.assertEquals(1, collection.size());
	}
	
	@Test
	public void testRemoveAll() {
		if(!getValidCollectionTests().contains(CollectionTest.REMOVE_ALL)) return;
		IntCollection collection = create(TEST_ARRAY);
		Assert.assertTrue(collection.removeAll(create(CONTAINS_ARRAY)));
		Assert.assertFalse(collection.removeAll(create(CONTAINS_ARRAY)));
		collection = create(TEST_ARRAY);
		Assert.assertFalse(collection.removeAll(Arrays.asList(IntArrays.wrap(ADD_ARRAY))));
		Assert.assertTrue(collection.removeAll(Arrays.asList(IntArrays.wrap(CONTAINS_ARRAY))));
	}
	
	@Test
	public void testRemoveAllListener() {
		if(!getValidCollectionTests().contains(CollectionTest.REMOVE_ALL_LISTENER)) return;
		IntCollection collection = create(TEST_ARRAY);
		Assert.assertTrue(collection.removeAll(create(CONTAINS_ARRAY), T -> {}));
		Assert.assertFalse(collection.removeAll(create(CONTAINS_ARRAY), T -> Assert.assertTrue(false)));
	}
	
	@Test
	public void testRetainAll() {
		if(!getValidCollectionTests().contains(CollectionTest.RETAIN_ALL)) return;
		IntCollection collection = create(TEST_ARRAY);
		IntCollection retained = create(CONTAINS_ARRAY);
		Assert.assertTrue(collection.retainAll(retained));
		Assert.assertFalse(collection.retainAll(retained));
		Assert.assertEquals(CONTAINS_ARRAY.length, collection.size());
		int[] retainedArray = retained.toIntArray();
		int[] collectionArray = collection.toIntArray();
		IntArrays.stableSort(retainedArray);
		IntArrays.stableSort(collectionArray);
		Assert.assertArrayEquals(retainedArray, collectionArray);
		collection.retainAll(IntCollections.EMPTY);
		Assert.assertTrue(collection.isEmpty());
	}
	
	@Test
	public void testRetainAllListener() {
		if(!getValidCollectionTests().contains(CollectionTest.RETAIN_ALL_LISTENER)) return;
		IntCollection collection = create(TEST_ARRAY);
		IntCollection retained = create(CONTAINS_ARRAY);
		Assert.assertTrue(collection.retainAll(retained, T -> {}));
		Assert.assertFalse(collection.retainAll(retained, T -> Assert.assertTrue(false)));
		Assert.assertEquals(CONTAINS_ARRAY.length, collection.size());
		int[] retainedArray = retained.toIntArray();
		int[] collectionArray = collection.toIntArray();
		IntArrays.stableSort(retainedArray);
		IntArrays.stableSort(collectionArray);
		Assert.assertArrayEquals(retainedArray, collectionArray);
		collection.retainAll(IntCollections.EMPTY, T -> {});
		Assert.assertTrue(collection.isEmpty());
	}
	
	@Test
	public void testToArray() {
		if(!getValidCollectionTests().contains(CollectionTest.TO_ARRAY)) return;
		IntCollection collection = create(TEST_ARRAY);
		int[] array = collection.toIntArray();
		IntArrays.stableSort(array);
		Assert.assertArrayEquals(array, TEST_ARRAY);
		int[] other = collection.toIntArray(new int[collection.size()]);
		IntArrays.stableSort(other);
		Assert.assertArrayEquals(other, TEST_ARRAY);
		other = IntArrays.unwrap(collection.toArray(new Integer[collection.size()]));
		IntArrays.stableSort(other);		
		Assert.assertArrayEquals(other, TEST_ARRAY);
	}
	
	@Test
	public void testClear() {
		if(!getValidCollectionTests().contains(CollectionTest.CLEAR)) return;
		IntCollection collection = create(TEST_ARRAY);
		Assert.assertFalse(collection.isEmpty());
		collection.clear();
		Assert.assertTrue(collection.isEmpty());
	}
	
	@Test
	public void testWrapper() {
		if(!getValidCollectionTests().contains(CollectionTest.WRAPPER)) return;
		IntCollection collection = create(TEST_ARRAY);
		collection = IntCollections.synchronize(collection);
		Assert.assertTrue(collection instanceof SynchronizedCollection);
		collection = IntCollections.unmodifiable(collection);
		Assert.assertTrue(collection instanceof UnmodifiableCollection);
	}
	
	@Test
	public void testToString() {
		if(!getValidCollectionTests().contains(CollectionTest.TO_STRING)) return;
		String base = Arrays.toString(BULK_ADD_ARRAY);
		IntCollection collection = create(BULK_ADD_ARRAY);
		Assert.assertEquals(base, collection.toString());
		Assert.assertEquals(base, IntCollections.synchronize(collection).toString());
		Assert.assertEquals(base, IntCollections.unmodifiable(collection).toString());
	}
	
	@Test
	public void testCopy() {
		if(!getValidCollectionTests().contains(CollectionTest.COPY)) return;
		IntCollection collection = create(BULK_ADD_ARRAY);
		IntCollection copy = collection.copy();
		Assert.assertFalse(collection == copy);
		Assert.assertEquals(collection, copy);
	}
}