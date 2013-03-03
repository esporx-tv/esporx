package tv.esporx.collections.functions;

import com.google.common.base.Function;
import tv.esporx.domain.front.JsonSlot;

public class ByIdConfigurableSlotIndexer implements Function<JsonSlot, Long> {
    @Override
    public Long apply(JsonSlot jsonSlot) {
        return Long.valueOf(jsonSlot.getId());
    }
}
