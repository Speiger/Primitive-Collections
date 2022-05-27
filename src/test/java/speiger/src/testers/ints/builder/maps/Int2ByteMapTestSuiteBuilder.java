package speiger.src.testers.ints.builder.maps;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.FeatureSpecificTestSuiteBuilder;
import com.google.common.collect.testing.MapTestSuiteBuilder;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.Feature;
import com.google.common.collect.testing.features.MapFeature;
import com.google.common.collect.testing.testers.CollectionIteratorTester;

import junit.framework.TestSuite;
import speiger.src.collections.ints.maps.interfaces.Int2ByteMap;
import speiger.src.testers.bytes.builder.ByteCollectionTestSuiteBuilder;
import speiger.src.testers.bytes.generators.TestByteCollectionGenerator;
import speiger.src.testers.ints.builder.IntSetTestSuiteBuilder;
import speiger.src.testers.ints.generators.TestIntSetGenerator;
import speiger.src.testers.ints.generators.maps.TestInt2ByteMapGenerator;
import speiger.src.testers.ints.impl.maps.DerivedInt2ByteMapGenerators;
import speiger.src.testers.ints.tests.maps.Int2ByteMapAddToTester;
import speiger.src.testers.ints.tests.maps.Int2ByteMapClearTester;
import speiger.src.testers.ints.tests.maps.Int2ByteMapComputeIfAbsentTester;
import speiger.src.testers.ints.tests.maps.Int2ByteMapComputeIfPresentTester;
import speiger.src.testers.ints.tests.maps.Int2ByteMapComputeTester;
import speiger.src.testers.ints.tests.maps.Int2ByteMapContainsKeyTester;
import speiger.src.testers.ints.tests.maps.Int2ByteMapContainsValueTester;
import speiger.src.testers.ints.tests.maps.Int2ByteMapEntrySetTester;
import speiger.src.testers.ints.tests.maps.Int2ByteMapEqualsTester;
import speiger.src.testers.ints.tests.maps.Int2ByteMapForEachTester;
import speiger.src.testers.ints.tests.maps.Int2ByteMapGetOrDefaultTester;
import speiger.src.testers.ints.tests.maps.Int2ByteMapGetTester;
import speiger.src.testers.ints.tests.maps.Int2ByteMapHashCodeTester;
import speiger.src.testers.ints.tests.maps.Int2ByteMapIsEmptyTester;
import speiger.src.testers.ints.tests.maps.Int2ByteMapMergeTester;
import speiger.src.testers.ints.tests.maps.Int2ByteMapPutAllArrayTester;
import speiger.src.testers.ints.tests.maps.Int2ByteMapPutAllTester;
import speiger.src.testers.ints.tests.maps.Int2ByteMapPutIfAbsentTester;
import speiger.src.testers.ints.tests.maps.Int2ByteMapPutTester;
import speiger.src.testers.ints.tests.maps.Int2ByteMapRemoveEntryTester;
import speiger.src.testers.ints.tests.maps.Int2ByteMapRemoveOrDefaultTester;
import speiger.src.testers.ints.tests.maps.Int2ByteMapRemoveTester;
import speiger.src.testers.ints.tests.maps.Int2ByteMapReplaceAllTester;
import speiger.src.testers.ints.tests.maps.Int2ByteMapReplaceEntryTester;
import speiger.src.testers.ints.tests.maps.Int2ByteMapReplaceTester;
import speiger.src.testers.ints.tests.maps.Int2ByteMapSizeTester;
import speiger.src.testers.ints.tests.maps.Int2ByteMapSupplyIfAbsentTester;
import speiger.src.testers.ints.tests.maps.Int2ByteMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

@SuppressWarnings("javadoc")
public class Int2ByteMapTestSuiteBuilder extends MapTestSuiteBuilder<Integer, Byte> {
	public static Int2ByteMapTestSuiteBuilder using(TestInt2ByteMapGenerator generator) {
		return (Int2ByteMapTestSuiteBuilder) new Int2ByteMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Int2ByteMapClearTester.class);
		testers.add(Int2ByteMapComputeTester.class);
		testers.add(Int2ByteMapComputeIfAbsentTester.class);
		testers.add(Int2ByteMapComputeIfPresentTester.class);
		testers.add(Int2ByteMapSupplyIfAbsentTester.class);
		testers.add(Int2ByteMapContainsKeyTester.class);
		testers.add(Int2ByteMapContainsValueTester.class);
		testers.add(Int2ByteMapEntrySetTester.class);
		testers.add(Int2ByteMapEqualsTester.class);
		testers.add(Int2ByteMapForEachTester.class);
		testers.add(Int2ByteMapGetTester.class);
		testers.add(Int2ByteMapGetOrDefaultTester.class);
		testers.add(Int2ByteMapHashCodeTester.class);
		testers.add(Int2ByteMapIsEmptyTester.class);
		testers.add(Int2ByteMapMergeTester.class);
		testers.add(Int2ByteMapPutTester.class);
		testers.add(Int2ByteMapAddToTester.class);
		testers.add(Int2ByteMapPutAllTester.class);
		testers.add(Int2ByteMapPutAllArrayTester.class);
		testers.add(Int2ByteMapPutIfAbsentTester.class);
		testers.add(Int2ByteMapRemoveTester.class);
		testers.add(Int2ByteMapRemoveEntryTester.class);
		testers.add(Int2ByteMapRemoveOrDefaultTester.class);
		testers.add(Int2ByteMapReplaceTester.class);
		testers.add(Int2ByteMapReplaceAllTester.class);
		testers.add(Int2ByteMapReplaceEntryTester.class);
		testers.add(Int2ByteMapSizeTester.class);
		testers.add(Int2ByteMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Integer, Byte>, Map.Entry<Integer, Byte>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedInt2ByteMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedInt2ByteMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedInt2ByteMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Int2ByteMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Int2ByteMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected IntSetTestSuiteBuilder createDerivedKeySetSuite(TestIntSetGenerator generator) {
		return IntSetTestSuiteBuilder.using(generator);
	}
	
	protected ByteCollectionTestSuiteBuilder createDerivedValueCollectionSuite(TestByteCollectionGenerator generator) {
		return ByteCollectionTestSuiteBuilder.using(generator);
	}
	
	private static Set<Feature<?>> computeEntrySetFeatures(Set<Feature<?>> mapFeatures) {
		Set<Feature<?>> entrySetFeatures = MapTestSuiteBuilder.computeCommonDerivedCollectionFeatures(mapFeatures);
		if (mapFeatures.contains(MapFeature.ALLOWS_NULL_ENTRY_QUERIES)) {
			entrySetFeatures.add(CollectionFeature.ALLOWS_NULL_QUERIES);
		}
		entrySetFeatures.remove(SpecialFeature.COPYING);
		return entrySetFeatures;
	}

	private static Set<Feature<?>> computeKeySetFeatures(Set<Feature<?>> mapFeatures) {
		Set<Feature<?>> keySetFeatures = MapTestSuiteBuilder.computeCommonDerivedCollectionFeatures(mapFeatures);
		keySetFeatures.add(CollectionFeature.SUBSET_VIEW);
		if (mapFeatures.contains(MapFeature.ALLOWS_NULL_KEYS)) {
			keySetFeatures.add(CollectionFeature.ALLOWS_NULL_VALUES);
		} else if (mapFeatures.contains(MapFeature.ALLOWS_NULL_KEY_QUERIES)) {
			keySetFeatures.add(CollectionFeature.ALLOWS_NULL_QUERIES);
		}
		keySetFeatures.remove(SpecialFeature.COPYING);
		return keySetFeatures;
	}

	private static Set<Feature<?>> computeValuesCollectionFeatures(Set<Feature<?>> mapFeatures) {
		Set<Feature<?>> valuesCollectionFeatures = MapTestSuiteBuilder.computeCommonDerivedCollectionFeatures(mapFeatures);
		if (mapFeatures.contains(MapFeature.ALLOWS_NULL_VALUE_QUERIES)) {
			valuesCollectionFeatures.add(CollectionFeature.ALLOWS_NULL_QUERIES);
		}
		if (mapFeatures.contains(MapFeature.ALLOWS_NULL_VALUES)) {
			valuesCollectionFeatures.add(CollectionFeature.ALLOWS_NULL_VALUES);
		}
		valuesCollectionFeatures.remove(SpecialFeature.COPYING);
		return valuesCollectionFeatures;
	}
	
	private static Set<Method> getEntrySetSuppressing(Set<Method> suppressing) {
		TestUtils.getSurpession(suppressing, CollectionIteratorTester.class, "testIterator_removeAffectsBackingCollection");
		TestUtils.getSurpession(suppressing, ObjectCollectionIteratorTester.class, "testIterator_removeAffectsBackingCollection");
		TestUtils.getSurpession(suppressing, ObjectCollectionRemoveAllTester.class, "testRemoveAll_someFetchRemovedElements");
		TestUtils.getSurpession(suppressing, ObjectCollectionRetainAllTester.class, "testRetainAllExtra_disjointPreviouslyNonEmpty", "testRetainAllExtra_containsDuplicatesSizeSeveral", "testRetainAllExtra_subset", "testRetainAllExtra_partialOverlap");
		return suppressing;
	}
}