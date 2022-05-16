package speiger.src.collections.objects.lists;

import java.util.ListIterator;

import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;

/**
 * A Type Specific ListIterator that reduces boxing/unboxing
 * @param <T> the type of elements maintained by this Collection
 */
public interface ObjectListIterator<T> extends ListIterator<T>, ObjectBidirectionalIterator<T>
{
}