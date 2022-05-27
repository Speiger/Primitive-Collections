package speiger.src.testers.longs.builder.maps;

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
import speiger.src.collections.longs.maps.interfaces.Long2ByteMap;
import speiger.src.testers.bytes.builder.ByteCollectionTestSuiteBuilder;
import speiger.src.testers.bytes.generators.TestByteCollectionGenerator;
import speiger.src.testers.longs.builder.LongSetTestSuiteBuilder;
import speiger.src.testers.longs.generators.TestLongSetGenerator;
import speiger.src.testers.longs.generators.maps.TestLong2ByteMapGenerator;
import speiger.src.testers.longs.impl.maps.DerivedLong2ByteMapGenerators;
import speiger.src.testers.longs.tests.maps.Long2ByteMapAddToTester;
import speiger.src.testers.longs.tests.maps.Long2ByteMapClearTester;
import speiger.src.testers.longs.tests.maps.Long2ByteMapComputeIfAbsentTester;
import speiger.src.testers.longs.tests.maps.Long2ByteMapComputeIfPresentTester;
import speiger.src.testers.longs.tests.maps.Long2ByteMapComputeTester;
import speiger.src.testers.longs.tests.maps.Long2ByteMapContainsKeyTester;
import speiger.src.testers.longs.tests.maps.Long2ByteMapContainsValueTester;
import speiger.src.testers.longs.tests.maps.Long2ByteMapEntrySetTester;
import speiger.src.testers.longs.tests.maps.Long2ByteMapEqualsTester;
import speiger.src.testers.longs.tests.maps.Long2ByteMapForEachTester;
import speiger.src.testers.longs.tests.maps.Long2ByteMapGetOrDefaultTester;
import speiger.src.testers.longs.tests.maps.Long2ByteMapGetTester;
import speiger.src.testers.longs.tests.maps.Long2ByteMapHashCodeTester;
import speiger.src.testers.longs.tests.maps.Long2ByteMapIsEmptyTester;
import speiger.src.testers.longs.tests.maps.Long2ByteMapMergeTester;
import speiger.src.testers.longs.tests.maps.Long2ByteMapPutAllArrayTester;
import speiger.src.testers.longs.tests.maps.Long2ByteMapPutAllTester;
import speiger.src.testers.longs.tests.maps.Long2ByteMapPutIfAbsentTester;
import speiger.src.testers.longs.tests.maps.Long2ByteMapPutTester;
import speiger.src.testers.longs.tests.maps.Long2ByteMapRemoveEntryTester;
import speiger.src.testers.longs.tests.maps.Long2ByteMapRemoveOrDefaultTester;
import speiger.src.testers.longs.tests.maps.Long2ByteMapRemoveTester;
import speiger.src.testers.longs.tests.maps.Long2ByteMapReplaceAllTester;
import speiger.src.testers.longs.tests.maps.Long2ByteMapReplaceEntryTester;
import speiger.src.testers.longs.tests.maps.Long2ByteMapReplaceTester;
import speiger.src.testers.longs.tests.maps.Long2ByteMapSizeTester;
import speiger.src.testers.longs.tests.maps.Long2ByteMapSupplyIfAbsentTester;
import speiger.src.testers.longs.tests.maps.Long2ByteMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Long2ByteMapTestSuiteBuilder extends MapTestSuiteBuilder<Long, Byte> {
	public static Long2ByteMapTestSuiteBuilder using(TestLong2ByteMapGenerator generator) {
		return (Long2ByteMapTestSuiteBuilder) new Long2ByteMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Long2ByteMapClearTester.class);
		testers.add(Long2ByteMapComputeTester.class);
		testers.add(Long2ByteMapComputeIfAbsentTester.class);
		testers.add(Long2ByteMapComputeIfPresentTester.class);
		testers.add(Long2ByteMapSupplyIfAbsentTester.class);
		testers.add(Long2ByteMapContainsKeyTester.class);
		testers.add(Long2ByteMapContainsValueTester.class);
		testers.add(Long2ByteMapEntrySetTester.class);
		testers.add(Long2ByteMapEqualsTester.class);
		testers.add(Long2ByteMapForEachTester.class);
		testers.add(Long2ByteMapGetTester.class);
		testers.add(Long2ByteMapGetOrDefaultTester.class);
		testers.add(Long2ByteMapHashCodeTester.class);
		testers.add(Long2ByteMapIsEmptyTester.class);
		testers.add(Long2ByteMapMergeTester.class);
		testers.add(Long2ByteMapPutTester.class);
		testers.add(Long2ByteMapAddToTester.class);
		testers.add(Long2ByteMapPutAllTester.class);
		testers.add(Long2ByteMapPutAllArrayTester.class);
		testers.add(Long2ByteMapPutIfAbsentTester.class);
		testers.add(Long2ByteMapRemoveTester.class);
		testers.add(Long2ByteMapRemoveEntryTester.class);
		testers.add(Long2ByteMapRemoveOrDefaultTester.class);
		testers.add(Long2ByteMapReplaceTester.class);
		testers.add(Long2ByteMapReplaceAllTester.class);
		testers.add(Long2ByteMapReplaceEntryTester.class);
		testers.add(Long2ByteMapSizeTester.class);
		testers.add(Long2ByteMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Long, Byte>, Map.Entry<Long, Byte>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedLong2ByteMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedLong2ByteMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedLong2ByteMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Long2ByteMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Long2ByteMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected LongSetTestSuiteBuilder createDerivedKeySetSuite(TestLongSetGenerator generator) {
		return LongSetTestSuiteBuilder.using(generator);
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