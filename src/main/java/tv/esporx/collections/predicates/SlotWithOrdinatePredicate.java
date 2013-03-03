package tv.esporx.collections.predicates;

import com.google.common.base.Predicate;
import tv.esporx.domain.ConfigurableSlot;

import javax.annotation.Nullable;

public class SlotWithOrdinatePredicate implements Predicate<ConfigurableSlot> {

    private final int ordinate;

    public SlotWithOrdinatePredicate(int ordinate) {
        this.ordinate = ordinate;
    }
    @Override
    public boolean apply(ConfigurableSlot configurableSlot) {
        return configurableSlot.getOrdinate() == this.ordinate;
    }
}
