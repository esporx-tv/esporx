package tv.esporx.collections.predicates;

import com.google.common.base.Predicate;
import tv.esporx.domain.ConfigurableSlot;

public class SlotIsLaidOut implements Predicate<ConfigurableSlot> {
    @Override
    public boolean apply(ConfigurableSlot input) {
        return input.isLaidOut();
    }
}
