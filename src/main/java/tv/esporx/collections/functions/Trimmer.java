package tv.esporx.collections.functions;

import com.google.common.base.Function;

public class Trimmer implements Function<String, String> {
    @Override
    public String apply(String input) {
        return input.trim();
    }
}
