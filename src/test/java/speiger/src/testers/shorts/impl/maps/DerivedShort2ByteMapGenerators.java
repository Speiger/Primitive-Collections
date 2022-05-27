package speiger.src.testers.shorts.impl.maps;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;

import speiger.src.collections.shorts.functions.ShortComparator;
import speiger.src.collections.shorts.collections.ShortIterable;
import speiger.src.collections.shorts.lists.ShortArrayList;
import speiger.src.collections.shorts.lists.ShortList;
import speiger.src.collections.shorts.maps.abstracts.AbstractShort2ByteMap;
import speiger.src.collections.shorts.maps.interfaces.Short2ByteMap;
import speiger.src.collections.shorts.maps.interfaces.Short2ByteMap.Entry;
import speiger.src.collections.shorts.maps.interfaces.Short2ByteNavigableMap;
import speiger.src.collections.shorts.maps.interfaces.Short2ByteSortedMap;
import speiger.src.collections.shorts.sets.ShortSet;
import speiger.src.collections.shorts.sets.ShortSortedSet;
import speiger.src.collections.shorts.sets.ShortNavigableSet;
import speiger.src.testers.shorts.generators.TestShortSetGenerator;
import speiger.src.testers.shorts.generators.TestShortSortedSetGenerator;
import speiger.src.testers.shorts.generators.TestShortNavigableSetGenerator;
import speiger.src.testers.shorts.generators.maps.TestShort2ByteMapGenerator;
import speiger.src.testers.shorts.generators.maps.TestShort2ByteSortedMapGenerator;
import speiger.src.testers.shorts.utils.ShortSamples;
import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.collections.ByteIterable;
import speiger.src.collections.bytes.lists.ByteList;
import speiger.src.collections.bytes.functions.ByteComparator;
import speiger.src.testers.bytes.generators.TestByteCollectionGenerator;
import speiger.src.testers.bytes.utils.ByteSamples;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.ObjectIterators;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class DerivedShort2ByteMapGenerators {
	public static class NavigableMapGenerator extends SortedMapGenerator {
		public NavigableMapGenerator(TestShort2ByteSortedMapGenerator parent, Bound to, Bound from) {
			super(parent, to, from);
		}
		
	    @Override
	    Short2ByteNavigableMap createSubMap(Short2ByteSortedMap sortedMap, short firstExclusive, short lastExclusive) {
	    	Short2ByteNavigableMap map = (Short2ByteNavigableMap) sortedMap;
	      if (from == Bound.NO_BOUND && to == Bound.INCLUSIVE) {
	        return map.headMap(lastInclusive, true);
	      } else if (from == Bound.EXCLUSIVE && to == Bound.NO_BOUND) {
	        return map.tailMap(firstExclusive, false);
	      } else if (from == Bound.EXCLUSIVE && to == Bound.EXCLUSIVE) {
	        return map.subMap(firstExclusive, false, lastExclusive, false);
	      } else if (from == Bound.EXCLUSIVE && to == Bound.INCLUSIVE) {
	        return map.subMap(firstExclusive, false, lastInclusive, true);
	      } else if (from == Bound.INCLUSIVE && to == Bound.INCLUSIVE) {
	        return map.subMap(firstInclusive, true, lastInclusive, true);
	      } else {
	        return (Short2ByteNavigableMap) super.createSubMap(map, firstExclusive, lastExclusive);
	      }
	    }
	}
	
	public static class SortedMapGenerator extends MapGenerator implements TestShort2ByteSortedMapGenerator {
		TestShort2ByteSortedMapGenerator parent;
		Bound to;
		Bound from;
		short firstInclusive;
		short lastInclusive;
		Comparator<Entry> entryComparator;

		public SortedMapGenerator(TestShort2ByteSortedMapGenerator parent, Bound to, Bound from) {
			super(parent);
			this.parent = parent;
			this.to = to;
			this.from = from;
			Short2ByteSortedMap map = parent.create();
			entryComparator = DerivedShort2ByteMapGenerators.entryComparator(map.comparator());
			ObjectList<Entry> samples = parent.getSamples().asList();
			samples.sort(entryComparator);
			firstInclusive = samples.get(0).getShortKey();
			lastInclusive = samples.get(samples.size() - 1).getShortKey();
		}
		
		@Override
		public Short2ByteSortedMap create(Entry... elements) {
			ObjectList<Entry> entries = new ObjectArrayList<>();
			if (from != Bound.NO_BOUND) {
				entries.add(parent.belowSamplesLesser());
				entries.add(parent.belowSamplesGreater());
			}
			if (to != Bound.NO_BOUND) {
				entries.add(parent.aboveSamplesLesser());
				entries.add(parent.aboveSamplesGreater());
			}
			entries.addAll(elements);
			return createSubMap(parent.create(entries.toArray(Entry[]::new)), parent.belowSamplesGreater().getShortKey(), parent.aboveSamplesLesser().getShortKey());
		}
		
		Short2ByteSortedMap createSubMap(Short2ByteSortedMap map, short firstExclusive, short lastExclusive) {
			if (from == Bound.NO_BOUND && to == Bound.EXCLUSIVE) {
				return map.headMap(lastExclusive);
			} else if (from == Bound.INCLUSIVE && to == Bound.NO_BOUND) {
				return map.tailMap(firstInclusive);
			} else if (from == Bound.INCLUSIVE && to == Bound.EXCLUSIVE) {
				return map.subMap(firstInclusive, lastExclusive);
			} else {
				throw new IllegalArgumentException();
			}
		}

		@Override
		public Entry belowSamplesLesser() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Entry belowSamplesGreater() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Entry aboveSamplesLesser() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Entry aboveSamplesGreater() {
			throw new UnsupportedOperationException();
		}
	}
	
	public static class DescendingTestMapGenerator extends MapGenerator implements TestShort2ByteSortedMapGenerator
	{
		TestShort2ByteSortedMapGenerator parent;
		
		public DescendingTestMapGenerator(TestShort2ByteSortedMapGenerator parent) {
			super(parent);
			this.parent = parent;
		}

		@Override
		public Short2ByteNavigableMap create(Entry... elements) {
			return ((Short2ByteNavigableMap)parent.create(elements)).descendingMap();
		}
		
		@Override
		public Iterable<Map.Entry<Short, Byte>> order(List<Map.Entry<Short, Byte>> insertionOrder) {
			ObjectList<Map.Entry<Short, Byte>> values = ObjectIterators.pour(ObjectIterators.wrap(parent.order(insertionOrder).iterator()));
			ObjectLists.reverse(values);
			return values;
		}
		
		@Override
		public ObjectIterable<Entry> order(ObjectList<Entry> insertionOrder) {
			ObjectList<Entry> values = parent.order(insertionOrder).pourAsList();
			ObjectLists.reverse(values);
			return values;
		}
		
		@Override
		public Entry belowSamplesLesser() {
			return parent.aboveSamplesGreater();
		}

		@Override
		public Entry belowSamplesGreater() {
			return parent.aboveSamplesLesser();
		}

		@Override
		public Entry aboveSamplesLesser() {
			return parent.belowSamplesGreater();
		}

		@Override
		public Entry aboveSamplesGreater() {
			return parent.belowSamplesLesser();
		}
	}
	
	public static class MapGenerator implements TestShort2ByteMapGenerator
	{
		TestShort2ByteMapGenerator parent;
		
		public MapGenerator(TestShort2ByteMapGenerator parent) {
			this.parent = parent;
		}

		@Override
		public Iterable<Map.Entry<Short, Byte>> order(List<Map.Entry<Short, Byte>> insertionOrder) {
			return parent.order(insertionOrder);
		}
		
		@Override
		public ObjectSamples<Entry> getSamples() {
			return parent.getSamples();
		}
		
		@Override
		public ObjectIterable<Entry> order(ObjectList<Entry> insertionOrder) {
			return parent.order(insertionOrder);
		}
		
		@Override
		public Short2ByteMap create(Entry... elements) {
			return parent.create(elements);
		}
	}

	public static class MapEntrySetGenerator implements TestObjectSetGenerator<Short2ByteMap.Entry> {
		TestShort2ByteMapGenerator generator;

		public MapEntrySetGenerator(OneSizeTestContainerGenerator<Map<Short, Byte>, Map.Entry<Short, Byte>> inner) {
			generator = (TestShort2ByteMapGenerator) inner.getInnerGenerator();
		}

		@Override
		public ObjectSamples<Entry> getSamples() {
			return generator.getSamples();
		}

		@Override
		public ObjectIterable<Entry> order(ObjectList<Entry> insertionOrder) {
			return generator.order(insertionOrder);
		}

		@Override
		public Iterable<Entry> order(List<Entry> insertionOrder) {
			return generator.order(new ObjectArrayList<Entry>(insertionOrder));
		}

		@Override
		public Entry[] createArray(int length) {
			return new Entry[length];
		}

		@Override
		public ObjectSet<Entry> create(Object... elements) {
			return generator.create(elements).short2ByteEntrySet();
		}
	}

	public static TestShortSetGenerator keySetGenerator(OneSizeTestContainerGenerator<Map<Short, Byte>, Map.Entry<Short, Byte>> inner) {
		if (inner.getInnerGenerator() instanceof TestShort2ByteSortedMapGenerator) {
			ShortSet set = ((TestShort2ByteSortedMapGenerator) inner.getInnerGenerator()).create().keySet();
			if(set instanceof ShortNavigableSet) return new MapNavigableKeySetGenerator(inner);
			if(set instanceof ShortSortedSet) return new MapSortedKeySetGenerator(inner);
		}
		return new MapKeySetGenerator(inner);
	}
	
	public static class MapNavigableKeySetGenerator extends MapSortedKeySetGenerator implements TestShortNavigableSetGenerator {
		public MapNavigableKeySetGenerator(OneSizeTestContainerGenerator<Map<Short, Byte>, Map.Entry<Short, Byte>> inner) {
			super(inner);
		}
		
		@Override
		public ShortNavigableSet create(short... elements) {
			return (ShortNavigableSet) super.create(elements);
		}

		@Override
		public ShortNavigableSet create(Object... elements) {
			return (ShortNavigableSet) super.create(elements);
		}
	}
	
	public static class MapSortedKeySetGenerator extends MapKeySetGenerator implements TestShortSortedSetGenerator {
		TestShort2ByteSortedMapGenerator generator;

		public MapSortedKeySetGenerator(OneSizeTestContainerGenerator<Map<Short, Byte>, Map.Entry<Short, Byte>> inner) {
			super(inner);
			generator = (TestShort2ByteSortedMapGenerator) inner.getInnerGenerator();
		}

		@Override
		public ShortSortedSet create(short... elements) {
			return (ShortSortedSet) super.create(elements);
		}

		@Override
		public ShortSortedSet create(Object... elements) {
			return (ShortSortedSet) super.create(elements);
		}

		@Override
		public short belowSamplesLesser() {
			return generator.belowSamplesLesser().getShortKey();
		}

		@Override
		public short belowSamplesGreater() {
			return generator.belowSamplesGreater().getShortKey();
		}

		@Override
		public short aboveSamplesLesser() {
			return generator.aboveSamplesLesser().getShortKey();
		}

		@Override
		public short aboveSamplesGreater() {
			return generator.aboveSamplesGreater().getShortKey();
		}
	}

	public static class MapKeySetGenerator implements TestShortSetGenerator {
		TestShort2ByteMapGenerator generator;
		ShortSamples samples;

		public MapKeySetGenerator(OneSizeTestContainerGenerator<Map<Short, Byte>, Map.Entry<Short, Byte>> inner) {
			generator = (TestShort2ByteMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Short2ByteMap.Entry> samples = generator.getSamples();
			this.samples = new ShortSamples(samples.e0().getShortKey(), samples.e1().getShortKey(), samples.e2().getShortKey(), samples.e3().getShortKey(), samples.e4().getShortKey());
		}

		@Override
		public ShortSamples getSamples() {
			return samples;
		}

		@Override
		public ShortIterable order(ShortList insertionOrder) {
			byte value = generator.getSamples().e0().getByteValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (short key : insertionOrder) {
				entries.add(new AbstractShort2ByteMap.BasicEntry(key, value));
			}
			ShortList list = new ShortArrayList();
			for (Entry entry : generator.order(entries)) {
				list.add(entry.getShortKey());
			}
			return list;
		}

		@Override
		public Iterable<Short> order(List<Short> insertionOrder) {
			byte value = generator.getSamples().e0().getByteValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (short key : insertionOrder) {
				entries.add(new AbstractShort2ByteMap.BasicEntry(key, value));
			}
			ShortList list = new ShortArrayList();
			for (Entry entry : generator.order(entries)) {
				list.add(entry.getShortKey());
			}
			return list;
		}
		
		@Override
		public ShortSet create(short... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractShort2ByteMap.BasicEntry(elements[index++], entry.getByteValue());
			}
			return generator.create(result).keySet();
		}

		@Override
		public ShortSet create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractShort2ByteMap.BasicEntry((Short) elements[index++], entry.getValue());
			}
			return generator.create(result).keySet();
		}
	}

	public static class MapValueCollectionGenerator implements TestByteCollectionGenerator {
		TestShort2ByteMapGenerator generator;
		ByteSamples samples;

		public MapValueCollectionGenerator(OneSizeTestContainerGenerator<Map<Short, Byte>, Map.Entry<Short, Byte>> inner) {
			generator = (TestShort2ByteMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Short2ByteMap.Entry> samples = generator.getSamples();
			this.samples = new ByteSamples(samples.e0().getByteValue(), samples.e1().getByteValue(), samples.e2().getByteValue(), samples.e3().getByteValue(), samples.e4().getByteValue());
		}
		
		@Override
		public ByteSamples getSamples() {
			return samples;
		}

		@Override
		public ByteIterable order(ByteList insertionOrder) {
			ObjectList<Entry> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new ByteComparator() {
				@Override
				public int compare(byte key, byte value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(byte entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(list.get(i).getByteValue() == entry) return i;
					}
					throw new IllegalArgumentException("Short2ByteMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}

		@Override
		public Iterable<Byte> order(List<Byte> insertionOrder) {
			ObjectList<Entry> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new Comparator<Byte>() {
				@Override
				public int compare(Byte key, Byte value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(Byte entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(list.get(i).getByteValue() == entry.byteValue()) return i;
					}
					throw new IllegalArgumentException("Short2ByteMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}
		
		@Override
		public ByteCollection create(byte... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractShort2ByteMap.BasicEntry(entry.getShortKey(), elements[index++]);
			}
			return generator.create(result).values();
		}
		
		@Override
		public ByteCollection create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractShort2ByteMap.BasicEntry(entry.getKey(), (Byte)elements[index++]);
			}
			return generator.create(result).values();
		}
	}
	
	public static Comparator<Map.Entry<Short, Byte>> entryObjectComparator(Comparator<Short> keyComparator) {
		return new Comparator<Map.Entry<Short, Byte>>() {
			@Override
			public int compare(Map.Entry<Short, Byte> a, Map.Entry<Short, Byte> b) {
				if(keyComparator == null) {
					return Short.compare(a.getKey().shortValue(), b.getKey().shortValue());
				}
				return keyComparator.compare(a.getKey().shortValue(), b.getKey().shortValue());
			}
		};
	}
	
	public static Comparator<Entry> entryComparator(ShortComparator keyComparator) {
		return new Comparator<Entry>() {
			@Override
			public int compare(Entry a, Entry b) {
				if(keyComparator == null) {
					return Short.compare(a.getShortKey(), b.getShortKey());
				}
				return keyComparator.compare(a.getShortKey(), b.getShortKey());
			}
		};
	}
}