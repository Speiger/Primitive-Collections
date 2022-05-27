package speiger.src.testers.shorts.builder.maps;

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
import speiger.src.collections.shorts.maps.interfaces.Short2ByteMap;
import speiger.src.testers.bytes.builder.ByteCollectionTestSuiteBuilder;
import speiger.src.testers.bytes.generators.TestByteCollectionGenerator;
import speiger.src.testers.shorts.builder.ShortSetTestSuiteBuilder;
import speiger.src.testers.shorts.generators.TestShortSetGenerator;
import speiger.src.testers.shorts.generators.maps.TestShort2ByteMapGenerator;
import speiger.src.testers.shorts.impl.maps.DerivedShort2ByteMapGenerators;
import speiger.src.testers.shorts.tests.maps.Short2ByteMapAddToTester;
import speiger.src.testers.shorts.tests.maps.Short2ByteMapClearTester;
import speiger.src.testers.shorts.tests.maps.Short2ByteMapComputeIfAbsentTester;
import speiger.src.testers.shorts.tests.maps.Short2ByteMapComputeIfPresentTester;
import speiger.src.testers.shorts.tests.maps.Short2ByteMapComputeTester;
import speiger.src.testers.shorts.tests.maps.Short2ByteMapContainsKeyTester;
import speiger.src.testers.shorts.tests.maps.Short2ByteMapContainsValueTester;
import speiger.src.testers.shorts.tests.maps.Short2ByteMapEntrySetTester;
import speiger.src.testers.shorts.tests.maps.Short2ByteMapEqualsTester;
import speiger.src.testers.shorts.tests.maps.Short2ByteMapForEachTester;
import speiger.src.testers.shorts.tests.maps.Short2ByteMapGetOrDefaultTester;
import speiger.src.testers.shorts.tests.maps.Short2ByteMapGetTester;
import speiger.src.testers.shorts.tests.maps.Short2ByteMapHashCodeTester;
import speiger.src.testers.shorts.tests.maps.Short2ByteMapIsEmptyTester;
import speiger.src.testers.shorts.tests.maps.Short2ByteMapMergeTester;
import speiger.src.testers.shorts.tests.maps.Short2ByteMapPutAllArrayTester;
import speiger.src.testers.shorts.tests.maps.Short2ByteMapPutAllTester;
import speiger.src.testers.shorts.tests.maps.Short2ByteMapPutIfAbsentTester;
import speiger.src.testers.shorts.tests.maps.Short2ByteMapPutTester;
import speiger.src.testers.shorts.tests.maps.Short2ByteMapRemoveEntryTester;
import speiger.src.testers.shorts.tests.maps.Short2ByteMapRemoveOrDefaultTester;
import speiger.src.testers.shorts.tests.maps.Short2ByteMapRemoveTester;
import speiger.src.testers.shorts.tests.maps.Short2ByteMapReplaceAllTester;
import speiger.src.testers.shorts.tests.maps.Short2ByteMapReplaceEntryTester;
import speiger.src.testers.shorts.tests.maps.Short2ByteMapReplaceTester;
import speiger.src.testers.shorts.tests.maps.Short2ByteMapSizeTester;
import speiger.src.testers.shorts.tests.maps.Short2ByteMapSupplyIfAbsentTester;
import speiger.src.testers.shorts.tests.maps.Short2ByteMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Short2ByteMapTestSuiteBuilder extends MapTestSuiteBuilder<Short, Byte> {
	public static Short2ByteMapTestSuiteBuilder using(TestShort2ByteMapGenerator generator) {
		return (Short2ByteMapTestSuiteBuilder) new Short2ByteMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Short2ByteMapClearTester.class);
		testers.add(Short2ByteMapComputeTester.class);
		testers.add(Short2ByteMapComputeIfAbsentTester.class);
		testers.add(Short2ByteMapComputeIfPresentTester.class);
		testers.add(Short2ByteMapSupplyIfAbsentTester.class);
		testers.add(Short2ByteMapContainsKeyTester.class);
		testers.add(Short2ByteMapContainsValueTester.class);
		testers.add(Short2ByteMapEntrySetTester.class);
		testers.add(Short2ByteMapEqualsTester.class);
		testers.add(Short2ByteMapForEachTester.class);
		testers.add(Short2ByteMapGetTester.class);
		testers.add(Short2ByteMapGetOrDefaultTester.class);
		testers.add(Short2ByteMapHashCodeTester.class);
		testers.add(Short2ByteMapIsEmptyTester.class);
		testers.add(Short2ByteMapMergeTester.class);
		testers.add(Short2ByteMapPutTester.class);
		testers.add(Short2ByteMapAddToTester.class);
		testers.add(Short2ByteMapPutAllTester.class);
		testers.add(Short2ByteMapPutAllArrayTester.class);
		testers.add(Short2ByteMapPutIfAbsentTester.class);
		testers.add(Short2ByteMapRemoveTester.class);
		testers.add(Short2ByteMapRemoveEntryTester.class);
		testers.add(Short2ByteMapRemoveOrDefaultTester.class);
		testers.add(Short2ByteMapReplaceTester.class);
		testers.add(Short2ByteMapReplaceAllTester.class);
		testers.add(Short2ByteMapReplaceEntryTester.class);
		testers.add(Short2ByteMapSizeTester.class);
		testers.add(Short2ByteMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Short, Byte>, Map.Entry<Short, Byte>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedShort2ByteMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedShort2ByteMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedShort2ByteMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Short2ByteMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Short2ByteMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected ShortSetTestSuiteBuilder createDerivedKeySetSuite(TestShortSetGenerator generator) {
		return ShortSetTestSuiteBuilder.using(generator);
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