package tv.esporx.collections.comparators;

import tv.esporx.domain.ConfigurableSlot;

import java.util.Comparator;

import static com.google.common.base.Preconditions.checkNotNull;

public class SlotByAbscissaComparator implements Comparator<ConfigurableSlot> {
    @Override
    public int compare(ConfigurableSlot left, ConfigurableSlot right) {
        checkNotNull(left);
        checkNotNull(right);

        if (left.getAbscissa() == right.getAbscissa()) {
            return 0;
        }
        else if (left.getAbscissa() > right.getAbscissa()) {
            return 1;
        }
        else {
            return -1;
        }

    }
}
