package tv.esporx.collections.predicates;

import com.google.common.base.Predicate;

public class IsValidHashtag implements Predicate<String> {
    @Override
    public boolean apply(String s) {
        String trimmed = s.trim();
        boolean hashtagStartsWith = trimmed.startsWith("#");
        boolean hashtagLengthIsLessThan20 = trimmed.length() <= 20;
        boolean hashtagDoesntContainSpaces = !trimmed.contains(" ");

        return hashtagStartsWith && hashtagLengthIsLessThan20 && hashtagDoesntContainSpaces;
    }
}
