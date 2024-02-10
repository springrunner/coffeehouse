package coffeehouse.libraries.base.lang;

/**
 * @author springrunner.kr@gmail.com
 */
public interface IterableItem<T> extends Iterable<T> {

    int size();

    boolean isEmpty();
}
