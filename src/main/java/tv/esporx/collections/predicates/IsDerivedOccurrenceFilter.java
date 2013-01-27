package tv.esporx.collections.predicates;

import com.google.common.base.Predicate;
import tv.esporx.domain.Occurrence;

import static com.google.common.base.Objects.firstNonNull;
import static com.google.common.base.Preconditions.checkNotNull;

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
        Long expectedId = origin.getId();
        if (expectedId == null) {
            return origin.equals(input) || origin.equals(actualOrigin);
        }
        else {
            return expectedId.equals(input.getId()) || (actualOrigin != null && expectedId.equals(expectedId));
        }
    }
}
