package tv.esporx.collections.comparators;

import tv.esporx.domain.ConfigurableSlot;

import java.util.Comparator;

import static com.google.common.base.Preconditions.checkNotNull;

public class SlotByOrdinateComparator implements Comparator<ConfigurableSlot> {
    @Override
    public int compare(ConfigurableSlot left, ConfigurableSlot right) {
        checkNotNull(left);
        checkNotNull(right);

        if (left.getOrdinate() == right.getOrdinate()) {
            return 0;
        }
        else if (left.getOrdinate() > right.getOrdinate()) {
            return 1;
        }
        else {
            return -1;
        }

    }
}
