package tv.esporx.framework;

import com.google.common.base.Preconditions;

public class StringGeneratorUtils {

	public static String generateString(final int length) {
		Preconditions.checkArgument(length > 0);
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < length; i++) {
			stringBuilder.append("a");
		}
		return stringBuilder.toString();
	}
}
