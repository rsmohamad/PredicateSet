import java.util.function.Predicate;

public class PredicateSet<T> {
    final private Predicate<T> setDefinition;

    private PredicateSet(Predicate<T> f) {
        setDefinition = f;
    }

    public static<T> PredicateSet<T> singleton(T a) {
        return new PredicateSet<T>(a::equals);
    }

    public PredicateSet<T> union(PredicateSet<T> that) {
        return new PredicateSet<T>(setDefinition.or(that.setDefinition));
    }

    public PredicateSet<T> intersect(PredicateSet<T> that) {
        return new PredicateSet<T>(setDefinition.and(that.setDefinition));
    }

    public PredicateSet<T> difference(PredicateSet<T> that) {
        return intersect(that.complement());
    }

    public PredicateSet<T> complement() {
        return new PredicateSet<T>(setDefinition.negate());
    }

    public PredicateSet<T> add(T a) {
        return union(PredicateSet.singleton(a));
    }

    public PredicateSet<T> remove(T a) {
        return difference(PredicateSet.singleton(a));
    }

    public boolean contains(T a) {
        return setDefinition.test(a);
    }
}
