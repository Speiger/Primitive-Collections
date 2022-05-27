package speiger.src.testers.doubles.builder.maps;

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
import speiger.src.collections.doubles.maps.interfaces.Double2ByteMap;
import speiger.src.testers.bytes.builder.ByteCollectionTestSuiteBuilder;
import speiger.src.testers.bytes.generators.TestByteCollectionGenerator;
import speiger.src.testers.doubles.builder.DoubleSetTestSuiteBuilder;
import speiger.src.testers.doubles.generators.TestDoubleSetGenerator;
import speiger.src.testers.doubles.generators.maps.TestDouble2ByteMapGenerator;
import speiger.src.testers.doubles.impl.maps.DerivedDouble2ByteMapGenerators;
import speiger.src.testers.doubles.tests.maps.Double2ByteMapAddToTester;
import speiger.src.testers.doubles.tests.maps.Double2ByteMapClearTester;
import speiger.src.testers.doubles.tests.maps.Double2ByteMapComputeIfAbsentTester;
import speiger.src.testers.doubles.tests.maps.Double2ByteMapComputeIfPresentTester;
import speiger.src.testers.doubles.tests.maps.Double2ByteMapComputeTester;
import speiger.src.testers.doubles.tests.maps.Double2ByteMapContainsKeyTester;
import speiger.src.testers.doubles.tests.maps.Double2ByteMapContainsValueTester;
import speiger.src.testers.doubles.tests.maps.Double2ByteMapEntrySetTester;
import speiger.src.testers.doubles.tests.maps.Double2ByteMapEqualsTester;
import speiger.src.testers.doubles.tests.maps.Double2ByteMapForEachTester;
import speiger.src.testers.doubles.tests.maps.Double2ByteMapGetOrDefaultTester;
import speiger.src.testers.doubles.tests.maps.Double2ByteMapGetTester;
import speiger.src.testers.doubles.tests.maps.Double2ByteMapHashCodeTester;
import speiger.src.testers.doubles.tests.maps.Double2ByteMapIsEmptyTester;
import speiger.src.testers.doubles.tests.maps.Double2ByteMapMergeTester;
import speiger.src.testers.doubles.tests.maps.Double2ByteMapPutAllArrayTester;
import speiger.src.testers.doubles.tests.maps.Double2ByteMapPutAllTester;
import speiger.src.testers.doubles.tests.maps.Double2ByteMapPutIfAbsentTester;
import speiger.src.testers.doubles.tests.maps.Double2ByteMapPutTester;
import speiger.src.testers.doubles.tests.maps.Double2ByteMapRemoveEntryTester;
import speiger.src.testers.doubles.tests.maps.Double2ByteMapRemoveOrDefaultTester;
import speiger.src.testers.doubles.tests.maps.Double2ByteMapRemoveTester;
import speiger.src.testers.doubles.tests.maps.Double2ByteMapReplaceAllTester;
import speiger.src.testers.doubles.tests.maps.Double2ByteMapReplaceEntryTester;
import speiger.src.testers.doubles.tests.maps.Double2ByteMapReplaceTester;
import speiger.src.testers.doubles.tests.maps.Double2ByteMapSizeTester;
import speiger.src.testers.doubles.tests.maps.Double2ByteMapSupplyIfAbsentTester;
import speiger.src.testers.doubles.tests.maps.Double2ByteMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

@SuppressWarnings("javadoc")
public class Double2ByteMapTestSuiteBuilder extends MapTestSuiteBuilder<Double, Byte> {
	public static Double2ByteMapTestSuiteBuilder using(TestDouble2ByteMapGenerator generator) {
		return (Double2ByteMapTestSuiteBuilder) new Double2ByteMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Double2ByteMapClearTester.class);
		testers.add(Double2ByteMapComputeTester.class);
		testers.add(Double2ByteMapComputeIfAbsentTester.class);
		testers.add(Double2ByteMapComputeIfPresentTester.class);
		testers.add(Double2ByteMapSupplyIfAbsentTester.class);
		testers.add(Double2ByteMapContainsKeyTester.class);
		testers.add(Double2ByteMapContainsValueTester.class);
		testers.add(Double2ByteMapEntrySetTester.class);
		testers.add(Double2ByteMapEqualsTester.class);
		testers.add(Double2ByteMapForEachTester.class);
		testers.add(Double2ByteMapGetTester.class);
		testers.add(Double2ByteMapGetOrDefaultTester.class);
		testers.add(Double2ByteMapHashCodeTester.class);
		testers.add(Double2ByteMapIsEmptyTester.class);
		testers.add(Double2ByteMapMergeTester.class);
		testers.add(Double2ByteMapPutTester.class);
		testers.add(Double2ByteMapAddToTester.class);
		testers.add(Double2ByteMapPutAllTester.class);
		testers.add(Double2ByteMapPutAllArrayTester.class);
		testers.add(Double2ByteMapPutIfAbsentTester.class);
		testers.add(Double2ByteMapRemoveTester.class);
		testers.add(Double2ByteMapRemoveEntryTester.class);
		testers.add(Double2ByteMapRemoveOrDefaultTester.class);
		testers.add(Double2ByteMapReplaceTester.class);
		testers.add(Double2ByteMapReplaceAllTester.class);
		testers.add(Double2ByteMapReplaceEntryTester.class);
		testers.add(Double2ByteMapSizeTester.class);
		testers.add(Double2ByteMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Double, Byte>, Map.Entry<Double, Byte>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedDouble2ByteMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedDouble2ByteMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedDouble2ByteMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Double2ByteMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Double2ByteMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected DoubleSetTestSuiteBuilder createDerivedKeySetSuite(TestDoubleSetGenerator generator) {
		return DoubleSetTestSuiteBuilder.using(generator);
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
		TestUtils.getSurpession(suppressing, CollectionIteratorTester.class, "testIterator_unknownOrderRemoveSupported");
		TestUtils.getSurpession(suppressing, ObjectCollectionIteratorTester.class, "testIterator_unknownOrderRemoveSupported");
		return suppressing;
	}
}