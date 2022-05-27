package speiger.src.testers.bytes.impl.maps;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;

import speiger.src.collections.bytes.functions.ByteComparator;
import speiger.src.collections.bytes.collections.ByteIterable;
import speiger.src.collections.bytes.lists.ByteArrayList;
import speiger.src.collections.bytes.lists.ByteList;
import speiger.src.collections.bytes.maps.abstracts.AbstractByte2ShortMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ShortMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ShortMap.Entry;
import speiger.src.collections.bytes.maps.interfaces.Byte2ShortNavigableMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ShortSortedMap;
import speiger.src.collections.bytes.sets.ByteSet;
import speiger.src.collections.bytes.sets.ByteSortedSet;
import speiger.src.collections.bytes.sets.ByteNavigableSet;
import speiger.src.testers.bytes.generators.TestByteSetGenerator;
import speiger.src.testers.bytes.generators.TestByteSortedSetGenerator;
import speiger.src.testers.bytes.generators.TestByteNavigableSetGenerator;
import speiger.src.testers.bytes.generators.maps.TestByte2ShortMapGenerator;
import speiger.src.testers.bytes.generators.maps.TestByte2ShortSortedMapGenerator;
import speiger.src.testers.bytes.utils.ByteSamples;
import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.collections.ShortIterable;
import speiger.src.collections.shorts.lists.ShortList;
import speiger.src.collections.shorts.functions.ShortComparator;
import speiger.src.testers.shorts.generators.TestShortCollectionGenerator;
import speiger.src.testers.shorts.utils.ShortSamples;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.ObjectIterators;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class DerivedByte2ShortMapGenerators {
	public static class NavigableMapGenerator extends SortedMapGenerator {
		public NavigableMapGenerator(TestByte2ShortSortedMapGenerator parent, Bound to, Bound from) {
			super(parent, to, from);
		}
		
	    @Override
	    Byte2ShortNavigableMap createSubMap(Byte2ShortSortedMap sortedMap, byte firstExclusive, byte lastExclusive) {
	    	Byte2ShortNavigableMap map = (Byte2ShortNavigableMap) sortedMap;
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
	        return (Byte2ShortNavigableMap) super.createSubMap(map, firstExclusive, lastExclusive);
	      }
	    }
	}
	
	public static class SortedMapGenerator extends MapGenerator implements TestByte2ShortSortedMapGenerator {
		TestByte2ShortSortedMapGenerator parent;
		Bound to;
		Bound from;
		byte firstInclusive;
		byte lastInclusive;
		Comparator<Entry> entryComparator;

		public SortedMapGenerator(TestByte2ShortSortedMapGenerator parent, Bound to, Bound from) {
			super(parent);
			this.parent = parent;
			this.to = to;
			this.from = from;
			Byte2ShortSortedMap map = parent.create();
			entryComparator = DerivedByte2ShortMapGenerators.entryComparator(map.comparator());
			ObjectList<Entry> samples = parent.getSamples().asList();
			samples.sort(entryComparator);
			firstInclusive = samples.get(0).getByteKey();
			lastInclusive = samples.get(samples.size() - 1).getByteKey();
		}
		
		@Override
		public Byte2ShortSortedMap create(Entry... elements) {
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
			return createSubMap(parent.create(entries.toArray(Entry[]::new)), parent.belowSamplesGreater().getByteKey(), parent.aboveSamplesLesser().getByteKey());
		}
		
		Byte2ShortSortedMap createSubMap(Byte2ShortSortedMap map, byte firstExclusive, byte lastExclusive) {
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
	
	public static class DescendingTestMapGenerator extends MapGenerator implements TestByte2ShortSortedMapGenerator
	{
		TestByte2ShortSortedMapGenerator parent;
		
		public DescendingTestMapGenerator(TestByte2ShortSortedMapGenerator parent) {
			super(parent);
			this.parent = parent;
		}

		@Override
		public Byte2ShortNavigableMap create(Entry... elements) {
			return ((Byte2ShortNavigableMap)parent.create(elements)).descendingMap();
		}
		
		@Override
		public Iterable<Map.Entry<Byte, Short>> order(List<Map.Entry<Byte, Short>> insertionOrder) {
			ObjectList<Map.Entry<Byte, Short>> values = ObjectIterators.pour(ObjectIterators.wrap(parent.order(insertionOrder).iterator()));
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
	
	public static class MapGenerator implements TestByte2ShortMapGenerator
	{
		TestByte2ShortMapGenerator parent;
		
		public MapGenerator(TestByte2ShortMapGenerator parent) {
			this.parent = parent;
		}

		@Override
		public Iterable<Map.Entry<Byte, Short>> order(List<Map.Entry<Byte, Short>> insertionOrder) {
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
		public Byte2ShortMap create(Entry... elements) {
			return parent.create(elements);
		}
	}

	public static class MapEntrySetGenerator implements TestObjectSetGenerator<Byte2ShortMap.Entry> {
		TestByte2ShortMapGenerator generator;

		public MapEntrySetGenerator(OneSizeTestContainerGenerator<Map<Byte, Short>, Map.Entry<Byte, Short>> inner) {
			generator = (TestByte2ShortMapGenerator) inner.getInnerGenerator();
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
			return generator.create(elements).byte2ShortEntrySet();
		}
	}

	public static TestByteSetGenerator keySetGenerator(OneSizeTestContainerGenerator<Map<Byte, Short>, Map.Entry<Byte, Short>> inner) {
		if (inner.getInnerGenerator() instanceof TestByte2ShortSortedMapGenerator) {
			ByteSet set = ((TestByte2ShortSortedMapGenerator) inner.getInnerGenerator()).create().keySet();
			if(set instanceof ByteNavigableSet) return new MapNavigableKeySetGenerator(inner);
			if(set instanceof ByteSortedSet) return new MapSortedKeySetGenerator(inner);
		}
		return new MapKeySetGenerator(inner);
	}
	
	public static class MapNavigableKeySetGenerator extends MapSortedKeySetGenerator implements TestByteNavigableSetGenerator {
		public MapNavigableKeySetGenerator(OneSizeTestContainerGenerator<Map<Byte, Short>, Map.Entry<Byte, Short>> inner) {
			super(inner);
		}
		
		@Override
		public ByteNavigableSet create(byte... elements) {
			return (ByteNavigableSet) super.create(elements);
		}

		@Override
		public ByteNavigableSet create(Object... elements) {
			return (ByteNavigableSet) super.create(elements);
		}
	}
	
	public static class MapSortedKeySetGenerator extends MapKeySetGenerator implements TestByteSortedSetGenerator {
		TestByte2ShortSortedMapGenerator generator;

		public MapSortedKeySetGenerator(OneSizeTestContainerGenerator<Map<Byte, Short>, Map.Entry<Byte, Short>> inner) {
			super(inner);
			generator = (TestByte2ShortSortedMapGenerator) inner.getInnerGenerator();
		}

		@Override
		public ByteSortedSet create(byte... elements) {
			return (ByteSortedSet) super.create(elements);
		}

		@Override
		public ByteSortedSet create(Object... elements) {
			return (ByteSortedSet) super.create(elements);
		}

		@Override
		public byte belowSamplesLesser() {
			return generator.belowSamplesLesser().getByteKey();
		}

		@Override
		public byte belowSamplesGreater() {
			return generator.belowSamplesGreater().getByteKey();
		}

		@Override
		public byte aboveSamplesLesser() {
			return generator.aboveSamplesLesser().getByteKey();
		}

		@Override
		public byte aboveSamplesGreater() {
			return generator.aboveSamplesGreater().getByteKey();
		}
	}

	public static class MapKeySetGenerator implements TestByteSetGenerator {
		TestByte2ShortMapGenerator generator;
		ByteSamples samples;

		public MapKeySetGenerator(OneSizeTestContainerGenerator<Map<Byte, Short>, Map.Entry<Byte, Short>> inner) {
			generator = (TestByte2ShortMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Byte2ShortMap.Entry> samples = generator.getSamples();
			this.samples = new ByteSamples(samples.e0().getByteKey(), samples.e1().getByteKey(), samples.e2().getByteKey(), samples.e3().getByteKey(), samples.e4().getByteKey());
		}

		@Override
		public ByteSamples getSamples() {
			return samples;
		}

		@Override
		public ByteIterable order(ByteList insertionOrder) {
			short value = generator.getSamples().e0().getShortValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (byte key : insertionOrder) {
				entries.add(new AbstractByte2ShortMap.BasicEntry(key, value));
			}
			ByteList list = new ByteArrayList();
			for (Entry entry : generator.order(entries)) {
				list.add(entry.getByteKey());
			}
			return list;
		}

		@Override
		public Iterable<Byte> order(List<Byte> insertionOrder) {
			short value = generator.getSamples().e0().getShortValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (byte key : insertionOrder) {
				entries.add(new AbstractByte2ShortMap.BasicEntry(key, value));
			}
			ByteList list = new ByteArrayList();
			for (Entry entry : generator.order(entries)) {
				list.add(entry.getByteKey());
			}
			return list;
		}
		
		@Override
		public ByteSet create(byte... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractByte2ShortMap.BasicEntry(elements[index++], entry.getShortValue());
			}
			return generator.create(result).keySet();
		}

		@Override
		public ByteSet create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractByte2ShortMap.BasicEntry((Byte) elements[index++], entry.getValue());
			}
			return generator.create(result).keySet();
		}
	}

	public static class MapValueCollectionGenerator implements TestShortCollectionGenerator {
		TestByte2ShortMapGenerator generator;
		ShortSamples samples;

		public MapValueCollectionGenerator(OneSizeTestContainerGenerator<Map<Byte, Short>, Map.Entry<Byte, Short>> inner) {
			generator = (TestByte2ShortMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Byte2ShortMap.Entry> samples = generator.getSamples();
			this.samples = new ShortSamples(samples.e0().getShortValue(), samples.e1().getShortValue(), samples.e2().getShortValue(), samples.e3().getShortValue(), samples.e4().getShortValue());
		}
		
		@Override
		public ShortSamples getSamples() {
			return samples;
		}

		@Override
		public ShortIterable order(ShortList insertionOrder) {
			ObjectList<Entry> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new ShortComparator() {
				@Override
				public int compare(short key, short value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(short entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(list.get(i).getShortValue() == entry) return i;
					}
					throw new IllegalArgumentException("Byte2ShortMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}

		@Override
		public Iterable<Short> order(List<Short> insertionOrder) {
			ObjectList<Entry> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new Comparator<Short>() {
				@Override
				public int compare(Short key, Short value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(Short entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(list.get(i).getShortValue() == entry.shortValue()) return i;
					}
					throw new IllegalArgumentException("Byte2ShortMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}
		
		@Override
		public ShortCollection create(short... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractByte2ShortMap.BasicEntry(entry.getByteKey(), elements[index++]);
			}
			return generator.create(result).values();
		}
		
		@Override
		public ShortCollection create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractByte2ShortMap.BasicEntry(entry.getKey(), (Short)elements[index++]);
			}
			return generator.create(result).values();
		}
	}
	
	public static Comparator<Map.Entry<Byte, Short>> entryObjectComparator(Comparator<Byte> keyComparator) {
		return new Comparator<Map.Entry<Byte, Short>>() {
			@Override
			public int compare(Map.Entry<Byte, Short> a, Map.Entry<Byte, Short> b) {
				if(keyComparator == null) {
					return Byte.compare(a.getKey().byteValue(), b.getKey().byteValue());
				}
				return keyComparator.compare(a.getKey().byteValue(), b.getKey().byteValue());
			}
		};
	}
	
	public static Comparator<Entry> entryComparator(ByteComparator keyComparator) {
		return new Comparator<Entry>() {
			@Override
			public int compare(Entry a, Entry b) {
				if(keyComparator == null) {
					return Byte.compare(a.getByteKey(), b.getByteKey());
				}
				return keyComparator.compare(a.getByteKey(), b.getByteKey());
			}
		};
	}
}