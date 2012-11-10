package tv.esporx.collections.predicates;

import com.google.common.base.Predicate;
import tv.esporx.domain.Occurrence;

/**
 * Checks that an occurrence is either the provided occurrence or
 * an echo of it.
 */
public class IsDerivedOccurrenceFilter implements Predicate<Occurrence> {

    private final Occurrence origin;

    public IsDerivedOccurrenceFilter(Occurrence origin) {
        this.origin = origin;
    }

    @Override
    public boolean apply(Occurrence input) {
        Occurrence actualOrigin = input.getOrigin();
        return origin.equals(input) || actualOrigin != null && actualOrigin.equals(origin);
    }
}
