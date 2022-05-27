package speiger.src.testers.bytes.builder.maps;

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
import speiger.src.collections.bytes.maps.interfaces.Byte2LongMap;
import speiger.src.testers.longs.builder.LongCollectionTestSuiteBuilder;
import speiger.src.testers.longs.generators.TestLongCollectionGenerator;
import speiger.src.testers.bytes.builder.ByteSetTestSuiteBuilder;
import speiger.src.testers.bytes.generators.TestByteSetGenerator;
import speiger.src.testers.bytes.generators.maps.TestByte2LongMapGenerator;
import speiger.src.testers.bytes.impl.maps.DerivedByte2LongMapGenerators;
import speiger.src.testers.bytes.tests.maps.Byte2LongMapAddToTester;
import speiger.src.testers.bytes.tests.maps.Byte2LongMapClearTester;
import speiger.src.testers.bytes.tests.maps.Byte2LongMapComputeIfAbsentTester;
import speiger.src.testers.bytes.tests.maps.Byte2LongMapComputeIfPresentTester;
import speiger.src.testers.bytes.tests.maps.Byte2LongMapComputeTester;
import speiger.src.testers.bytes.tests.maps.Byte2LongMapContainsKeyTester;
import speiger.src.testers.bytes.tests.maps.Byte2LongMapContainsValueTester;
import speiger.src.testers.bytes.tests.maps.Byte2LongMapEntrySetTester;
import speiger.src.testers.bytes.tests.maps.Byte2LongMapEqualsTester;
import speiger.src.testers.bytes.tests.maps.Byte2LongMapForEachTester;
import speiger.src.testers.bytes.tests.maps.Byte2LongMapGetOrDefaultTester;
import speiger.src.testers.bytes.tests.maps.Byte2LongMapGetTester;
import speiger.src.testers.bytes.tests.maps.Byte2LongMapHashCodeTester;
import speiger.src.testers.bytes.tests.maps.Byte2LongMapIsEmptyTester;
import speiger.src.testers.bytes.tests.maps.Byte2LongMapMergeTester;
import speiger.src.testers.bytes.tests.maps.Byte2LongMapPutAllArrayTester;
import speiger.src.testers.bytes.tests.maps.Byte2LongMapPutAllTester;
import speiger.src.testers.bytes.tests.maps.Byte2LongMapPutIfAbsentTester;
import speiger.src.testers.bytes.tests.maps.Byte2LongMapPutTester;
import speiger.src.testers.bytes.tests.maps.Byte2LongMapRemoveEntryTester;
import speiger.src.testers.bytes.tests.maps.Byte2LongMapRemoveOrDefaultTester;
import speiger.src.testers.bytes.tests.maps.Byte2LongMapRemoveTester;
import speiger.src.testers.bytes.tests.maps.Byte2LongMapReplaceAllTester;
import speiger.src.testers.bytes.tests.maps.Byte2LongMapReplaceEntryTester;
import speiger.src.testers.bytes.tests.maps.Byte2LongMapReplaceTester;
import speiger.src.testers.bytes.tests.maps.Byte2LongMapSizeTester;
import speiger.src.testers.bytes.tests.maps.Byte2LongMapSupplyIfAbsentTester;
import speiger.src.testers.bytes.tests.maps.Byte2LongMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

@SuppressWarnings("javadoc")
public class Byte2LongMapTestSuiteBuilder extends MapTestSuiteBuilder<Byte, Long> {
	public static Byte2LongMapTestSuiteBuilder using(TestByte2LongMapGenerator generator) {
		return (Byte2LongMapTestSuiteBuilder) new Byte2LongMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Byte2LongMapClearTester.class);
		testers.add(Byte2LongMapComputeTester.class);
		testers.add(Byte2LongMapComputeIfAbsentTester.class);
		testers.add(Byte2LongMapComputeIfPresentTester.class);
		testers.add(Byte2LongMapSupplyIfAbsentTester.class);
		testers.add(Byte2LongMapContainsKeyTester.class);
		testers.add(Byte2LongMapContainsValueTester.class);
		testers.add(Byte2LongMapEntrySetTester.class);
		testers.add(Byte2LongMapEqualsTester.class);
		testers.add(Byte2LongMapForEachTester.class);
		testers.add(Byte2LongMapGetTester.class);
		testers.add(Byte2LongMapGetOrDefaultTester.class);
		testers.add(Byte2LongMapHashCodeTester.class);
		testers.add(Byte2LongMapIsEmptyTester.class);
		testers.add(Byte2LongMapMergeTester.class);
		testers.add(Byte2LongMapPutTester.class);
		testers.add(Byte2LongMapAddToTester.class);
		testers.add(Byte2LongMapPutAllTester.class);
		testers.add(Byte2LongMapPutAllArrayTester.class);
		testers.add(Byte2LongMapPutIfAbsentTester.class);
		testers.add(Byte2LongMapRemoveTester.class);
		testers.add(Byte2LongMapRemoveEntryTester.class);
		testers.add(Byte2LongMapRemoveOrDefaultTester.class);
		testers.add(Byte2LongMapReplaceTester.class);
		testers.add(Byte2LongMapReplaceAllTester.class);
		testers.add(Byte2LongMapReplaceEntryTester.class);
		testers.add(Byte2LongMapSizeTester.class);
		testers.add(Byte2LongMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Byte, Long>, Map.Entry<Byte, Long>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedByte2LongMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedByte2LongMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedByte2LongMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Byte2LongMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Byte2LongMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected ByteSetTestSuiteBuilder createDerivedKeySetSuite(TestByteSetGenerator generator) {
		return ByteSetTestSuiteBuilder.using(generator);
	}
	
	protected LongCollectionTestSuiteBuilder createDerivedValueCollectionSuite(TestLongCollectionGenerator generator) {
		return LongCollectionTestSuiteBuilder.using(generator);
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