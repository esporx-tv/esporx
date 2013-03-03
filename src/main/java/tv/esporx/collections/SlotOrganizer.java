package tv.esporx.collections;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;
import tv.esporx.collections.comparators.SlotByAbscissaComparator;
import tv.esporx.collections.comparators.SlotByOrdinateComparator;
import tv.esporx.collections.comparators.SlotByWidthComparator;
import tv.esporx.collections.predicates.SlotIsLaidOut;
import tv.esporx.collections.predicates.SlotWithOrdinatePredicate;
import tv.esporx.domain.ConfigurableSlot;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import static com.google.common.base.Objects.firstNonNull;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Ordering.from;

public class SlotOrganizer {

    private final List<ConfigurableSlot> slots;

    public SlotOrganizer(List<ConfigurableSlot> slots) {
        this.slots = slots;
        sortNonLaidOutByDescendingWidth();
    }

    /**
     * Lays out configurable slots, appending the non-positioned ones
     * after the positioned ones
     */
    public SlotOrganizer reorganize() {
        if (slots.size() > 0) {
            ConfigurableSlot maxSlot = null;
            int maxY = slots.get(slots.size()-1).getOrdinate();
            try {
                maxSlot = from(new SlotByAbscissaComparator()).max(Collections2.filter(slots, new SlotWithOrdinatePredicate(maxY)));
            } catch (NoSuchElementException e) {
            }

            for (ConfigurableSlot configurableSlot : slots) {
                if (!configurableSlot.isLaidOut()) {
                    maxSlot = firstNonNull(maxSlot, new ConfigurableSlot());
                    int nextX = 1 + (maxSlot.getAbscissa() * maxSlot.getWidth()) % 2;
                    configurableSlot.setAbscissa(nextX);
                    int nextY = nextX == 1 ? maxSlot.getOrdinate() + 1 : maxSlot.getOrdinate();
                    configurableSlot.setOrdinate(nextY);
                    maxSlot = configurableSlot;
                }
            }
        }
        return this;
    }

    /**
     * Sorts slot by ascending ordinate, and by ascending abscissa
     */
    public SlotOrganizer order() {
        return new SlotOrganizer(
            from(new SlotByOrdinateComparator())
                .compound(new SlotByAbscissaComparator())
                .sortedCopy(slots)
        );
    }

    public Collection<ConfigurableSlot> slots() {
        return slots;
    }

    private void sortNonLaidOutByDescendingWidth() {
        List<ConfigurableSlot> nonLaidOut = from(new SlotByWidthComparator()).reverse().sortedCopy(filter(slots, not(new SlotIsLaidOut())));
        slots.removeAll(newArrayList(nonLaidOut));
        slots.addAll(nonLaidOut);
    }
}
