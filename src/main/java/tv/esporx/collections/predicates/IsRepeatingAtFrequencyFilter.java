package tv.esporx.collections.predicates;

import com.google.common.base.Predicate;
import tv.esporx.domain.Occurrence;

import static tv.esporx.domain.FrequencyType.FrequencyTypes;


public class IsRepeatingAtFrequencyFilter implements Predicate<Occurrence> {

    private final FrequencyTypes frequency;

    public IsRepeatingAtFrequencyFilter(FrequencyTypes frequency) {
        this.frequency = frequency;
    }

    @Override
    public boolean apply(Occurrence input) {
        return frequency.name().equals(input.getFrequencyType().getValue());
    }
}
