package speiger.src.testers.floats.builder.maps;

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
import speiger.src.collections.floats.maps.interfaces.Float2ByteMap;
import speiger.src.testers.bytes.builder.ByteCollectionTestSuiteBuilder;
import speiger.src.testers.bytes.generators.TestByteCollectionGenerator;
import speiger.src.testers.floats.builder.FloatSetTestSuiteBuilder;
import speiger.src.testers.floats.generators.TestFloatSetGenerator;
import speiger.src.testers.floats.generators.maps.TestFloat2ByteMapGenerator;
import speiger.src.testers.floats.impl.maps.DerivedFloat2ByteMapGenerators;
import speiger.src.testers.floats.tests.maps.Float2ByteMapAddToTester;
import speiger.src.testers.floats.tests.maps.Float2ByteMapClearTester;
import speiger.src.testers.floats.tests.maps.Float2ByteMapComputeIfAbsentTester;
import speiger.src.testers.floats.tests.maps.Float2ByteMapComputeIfPresentTester;
import speiger.src.testers.floats.tests.maps.Float2ByteMapComputeTester;
import speiger.src.testers.floats.tests.maps.Float2ByteMapContainsKeyTester;
import speiger.src.testers.floats.tests.maps.Float2ByteMapContainsValueTester;
import speiger.src.testers.floats.tests.maps.Float2ByteMapEntrySetTester;
import speiger.src.testers.floats.tests.maps.Float2ByteMapEqualsTester;
import speiger.src.testers.floats.tests.maps.Float2ByteMapForEachTester;
import speiger.src.testers.floats.tests.maps.Float2ByteMapGetOrDefaultTester;
import speiger.src.testers.floats.tests.maps.Float2ByteMapGetTester;
import speiger.src.testers.floats.tests.maps.Float2ByteMapHashCodeTester;
import speiger.src.testers.floats.tests.maps.Float2ByteMapIsEmptyTester;
import speiger.src.testers.floats.tests.maps.Float2ByteMapMergeTester;
import speiger.src.testers.floats.tests.maps.Float2ByteMapPutAllArrayTester;
import speiger.src.testers.floats.tests.maps.Float2ByteMapPutAllTester;
import speiger.src.testers.floats.tests.maps.Float2ByteMapPutIfAbsentTester;
import speiger.src.testers.floats.tests.maps.Float2ByteMapPutTester;
import speiger.src.testers.floats.tests.maps.Float2ByteMapRemoveEntryTester;
import speiger.src.testers.floats.tests.maps.Float2ByteMapRemoveOrDefaultTester;
import speiger.src.testers.floats.tests.maps.Float2ByteMapRemoveTester;
import speiger.src.testers.floats.tests.maps.Float2ByteMapReplaceAllTester;
import speiger.src.testers.floats.tests.maps.Float2ByteMapReplaceEntryTester;
import speiger.src.testers.floats.tests.maps.Float2ByteMapReplaceTester;
import speiger.src.testers.floats.tests.maps.Float2ByteMapSizeTester;
import speiger.src.testers.floats.tests.maps.Float2ByteMapSupplyIfAbsentTester;
import speiger.src.testers.floats.tests.maps.Float2ByteMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Float2ByteMapTestSuiteBuilder extends MapTestSuiteBuilder<Float, Byte> {
	public static Float2ByteMapTestSuiteBuilder using(TestFloat2ByteMapGenerator generator) {
		return (Float2ByteMapTestSuiteBuilder) new Float2ByteMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Float2ByteMapClearTester.class);
		testers.add(Float2ByteMapComputeTester.class);
		testers.add(Float2ByteMapComputeIfAbsentTester.class);
		testers.add(Float2ByteMapComputeIfPresentTester.class);
		testers.add(Float2ByteMapSupplyIfAbsentTester.class);
		testers.add(Float2ByteMapContainsKeyTester.class);
		testers.add(Float2ByteMapContainsValueTester.class);
		testers.add(Float2ByteMapEntrySetTester.class);
		testers.add(Float2ByteMapEqualsTester.class);
		testers.add(Float2ByteMapForEachTester.class);
		testers.add(Float2ByteMapGetTester.class);
		testers.add(Float2ByteMapGetOrDefaultTester.class);
		testers.add(Float2ByteMapHashCodeTester.class);
		testers.add(Float2ByteMapIsEmptyTester.class);
		testers.add(Float2ByteMapMergeTester.class);
		testers.add(Float2ByteMapPutTester.class);
		testers.add(Float2ByteMapAddToTester.class);
		testers.add(Float2ByteMapPutAllTester.class);
		testers.add(Float2ByteMapPutAllArrayTester.class);
		testers.add(Float2ByteMapPutIfAbsentTester.class);
		testers.add(Float2ByteMapRemoveTester.class);
		testers.add(Float2ByteMapRemoveEntryTester.class);
		testers.add(Float2ByteMapRemoveOrDefaultTester.class);
		testers.add(Float2ByteMapReplaceTester.class);
		testers.add(Float2ByteMapReplaceAllTester.class);
		testers.add(Float2ByteMapReplaceEntryTester.class);
		testers.add(Float2ByteMapSizeTester.class);
		testers.add(Float2ByteMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Float, Byte>, Map.Entry<Float, Byte>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedFloat2ByteMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedFloat2ByteMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedFloat2ByteMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Float2ByteMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Float2ByteMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected FloatSetTestSuiteBuilder createDerivedKeySetSuite(TestFloatSetGenerator generator) {
		return FloatSetTestSuiteBuilder.using(generator);
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