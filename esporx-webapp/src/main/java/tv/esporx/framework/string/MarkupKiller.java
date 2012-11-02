package tv.esporx.framework.string;

public class MarkupKiller {

	public String stripTags(final String input) {
		StringBuilder sb = new StringBuilder(input.length());
		boolean skipping = false;
		for (char c : input.toCharArray()) {
            skipping = skipOrAppend(sb, skipping, c);
        }
		return sb.toString();
	}

    private boolean skipOrAppend(StringBuilder sb, boolean skipping, char c) {
        if (skipping) {
            if (c == '>') {
                skipping = false;
            }
        } else {
            if (c == '<') {
                skipping = true;
            } else {
                sb.append(c);
            }
        }
        return skipping;
    }
}