package de.jardas.drakensang.model.savegame;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public final class PackReader {
	private PackReader() {
		// utility class
	}

	public static String[] read(InputStream in) throws IOException {
		final List<String> data = new ArrayList<String>();
		in.skip(9);

		while (true) {
			int length = in.read();

			if (length < 0) {
				break;
			}
			
			length--;

			in.skip(3);

			final byte[] buffer = new byte[length];
			in.read(buffer, 0, length);
			final String str = new String(buffer, "UTF-8");
			data.add(str);
		}

		return data.toArray(new String[data.size()]);
	}
}
