package tv.esporx.collections.comparators;

import tv.esporx.domain.Occurrence;

import java.util.Comparator;

public class OccurrenceByStartDateComparator implements	Comparator<Occurrence> {
	
	@Override
    public int compare(Occurrence occ1, Occurrence occ2) {
        return occ1.getStartDateTime().compareTo(occ2.getStartDateTime());
    }

}
