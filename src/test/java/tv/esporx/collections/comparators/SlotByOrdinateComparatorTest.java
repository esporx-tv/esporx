package tv.esporx.collections.comparators;

import org.junit.Test;
import tv.esporx.domain.ConfigurableSlot;

import static org.fest.assertions.Assertions.assertThat;

public class SlotByOrdinateComparatorTest {

    @Test
    public void should_consider_same_ordinate_slot_equal() {
        ConfigurableSlot left = new ConfigurableSlot();
        left.setOrdinate(1);
        ConfigurableSlot right = new ConfigurableSlot();
        right.setOrdinate(1);
        assertThat(new SlotByOrdinateComparator().compare(left, right)).isEqualTo(0);
    }

    @Test
    public void should_consider_left_smaller_slot_than_right() {
        ConfigurableSlot left = new ConfigurableSlot();
        left.setOrdinate(-2);
        ConfigurableSlot right = new ConfigurableSlot();
        right.setOrdinate(1);
        assertThat(new SlotByOrdinateComparator().compare(left, right)).isNegative();
    }

    @Test(expected = NullPointerException.class)
    public void should_throw_error_with_left_null() {
        new SlotByOrdinateComparator().compare(null, new ConfigurableSlot());
    }

    @Test(expected = NullPointerException.class)
    public void should_throw_error_with_right_null() {
        new SlotByOrdinateComparator().compare(new ConfigurableSlot(), null);
    }
}
