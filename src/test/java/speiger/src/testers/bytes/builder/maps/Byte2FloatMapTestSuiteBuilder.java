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
import speiger.src.collections.bytes.maps.interfaces.Byte2FloatMap;
import speiger.src.testers.floats.builder.FloatCollectionTestSuiteBuilder;
import speiger.src.testers.floats.generators.TestFloatCollectionGenerator;
import speiger.src.testers.bytes.builder.ByteSetTestSuiteBuilder;
import speiger.src.testers.bytes.generators.TestByteSetGenerator;
import speiger.src.testers.bytes.generators.maps.TestByte2FloatMapGenerator;
import speiger.src.testers.bytes.impl.maps.DerivedByte2FloatMapGenerators;
import speiger.src.testers.bytes.tests.maps.Byte2FloatMapAddToTester;
import speiger.src.testers.bytes.tests.maps.Byte2FloatMapClearTester;
import speiger.src.testers.bytes.tests.maps.Byte2FloatMapComputeIfAbsentTester;
import speiger.src.testers.bytes.tests.maps.Byte2FloatMapComputeIfPresentTester;
import speiger.src.testers.bytes.tests.maps.Byte2FloatMapComputeTester;
import speiger.src.testers.bytes.tests.maps.Byte2FloatMapContainsKeyTester;
import speiger.src.testers.bytes.tests.maps.Byte2FloatMapContainsValueTester;
import speiger.src.testers.bytes.tests.maps.Byte2FloatMapEntrySetTester;
import speiger.src.testers.bytes.tests.maps.Byte2FloatMapEqualsTester;
import speiger.src.testers.bytes.tests.maps.Byte2FloatMapForEachTester;
import speiger.src.testers.bytes.tests.maps.Byte2FloatMapGetOrDefaultTester;
import speiger.src.testers.bytes.tests.maps.Byte2FloatMapGetTester;
import speiger.src.testers.bytes.tests.maps.Byte2FloatMapHashCodeTester;
import speiger.src.testers.bytes.tests.maps.Byte2FloatMapIsEmptyTester;
import speiger.src.testers.bytes.tests.maps.Byte2FloatMapMergeTester;
import speiger.src.testers.bytes.tests.maps.Byte2FloatMapPutAllArrayTester;
import speiger.src.testers.bytes.tests.maps.Byte2FloatMapPutAllTester;
import speiger.src.testers.bytes.tests.maps.Byte2FloatMapPutIfAbsentTester;
import speiger.src.testers.bytes.tests.maps.Byte2FloatMapPutTester;
import speiger.src.testers.bytes.tests.maps.Byte2FloatMapRemoveEntryTester;
import speiger.src.testers.bytes.tests.maps.Byte2FloatMapRemoveOrDefaultTester;
import speiger.src.testers.bytes.tests.maps.Byte2FloatMapRemoveTester;
import speiger.src.testers.bytes.tests.maps.Byte2FloatMapReplaceAllTester;
import speiger.src.testers.bytes.tests.maps.Byte2FloatMapReplaceEntryTester;
import speiger.src.testers.bytes.tests.maps.Byte2FloatMapReplaceTester;
import speiger.src.testers.bytes.tests.maps.Byte2FloatMapSizeTester;
import speiger.src.testers.bytes.tests.maps.Byte2FloatMapSupplyIfAbsentTester;
import speiger.src.testers.bytes.tests.maps.Byte2FloatMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

@SuppressWarnings("javadoc")
public class Byte2FloatMapTestSuiteBuilder extends MapTestSuiteBuilder<Byte, Float> {
	public static Byte2FloatMapTestSuiteBuilder using(TestByte2FloatMapGenerator generator) {
		return (Byte2FloatMapTestSuiteBuilder) new Byte2FloatMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Byte2FloatMapClearTester.class);
		testers.add(Byte2FloatMapComputeTester.class);
		testers.add(Byte2FloatMapComputeIfAbsentTester.class);
		testers.add(Byte2FloatMapComputeIfPresentTester.class);
		testers.add(Byte2FloatMapSupplyIfAbsentTester.class);
		testers.add(Byte2FloatMapContainsKeyTester.class);
		testers.add(Byte2FloatMapContainsValueTester.class);
		testers.add(Byte2FloatMapEntrySetTester.class);
		testers.add(Byte2FloatMapEqualsTester.class);
		testers.add(Byte2FloatMapForEachTester.class);
		testers.add(Byte2FloatMapGetTester.class);
		testers.add(Byte2FloatMapGetOrDefaultTester.class);
		testers.add(Byte2FloatMapHashCodeTester.class);
		testers.add(Byte2FloatMapIsEmptyTester.class);
		testers.add(Byte2FloatMapMergeTester.class);
		testers.add(Byte2FloatMapPutTester.class);
		testers.add(Byte2FloatMapAddToTester.class);
		testers.add(Byte2FloatMapPutAllTester.class);
		testers.add(Byte2FloatMapPutAllArrayTester.class);
		testers.add(Byte2FloatMapPutIfAbsentTester.class);
		testers.add(Byte2FloatMapRemoveTester.class);
		testers.add(Byte2FloatMapRemoveEntryTester.class);
		testers.add(Byte2FloatMapRemoveOrDefaultTester.class);
		testers.add(Byte2FloatMapReplaceTester.class);
		testers.add(Byte2FloatMapReplaceAllTester.class);
		testers.add(Byte2FloatMapReplaceEntryTester.class);
		testers.add(Byte2FloatMapSizeTester.class);
		testers.add(Byte2FloatMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Byte, Float>, Map.Entry<Byte, Float>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedByte2FloatMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedByte2FloatMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedByte2FloatMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Byte2FloatMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Byte2FloatMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected ByteSetTestSuiteBuilder createDerivedKeySetSuite(TestByteSetGenerator generator) {
		return ByteSetTestSuiteBuilder.using(generator);
	}
	
	protected FloatCollectionTestSuiteBuilder createDerivedValueCollectionSuite(TestFloatCollectionGenerator generator) {
		return FloatCollectionTestSuiteBuilder.using(generator);
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