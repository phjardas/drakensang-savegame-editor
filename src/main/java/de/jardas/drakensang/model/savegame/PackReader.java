package de.jardas.drakensang.model.savegame;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class PackReader {
	private PackReader() {
		// utility class
	}

	public static PackData read(InputStream in) throws IOException {
		in.skip(4);

		final int version = in.read();
		final String[] data = loadData(in, version);

		return new PackData(version, data);
	}

	private static String[] loadData(InputStream in, final int version)
			throws IOException {
		switch (version) {
		case 2:
			return readVersion2(in);
		case 3:
			return readVersion3(in);
		default:
			throw new IOException("Unknown savegame version: " + version);
		}
	}

	private static String[] readVersion2(InputStream in) throws IOException {
		in.skip(4);
		final List<String> data = new ArrayList<String>();

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

	private static String[] readVersion3(InputStream in) throws IOException {
		in.skip(5);
		final List<String> data = new ArrayList<String>();

		while (true) {
			int length = in.read();

			if (length < 0) {
				break;
			}

			length--;

			in.skip(3);

			if (length > 0) {
				final byte[] buffer = new byte[length];
				in.read(buffer, 0, length);
				final String str = new String(buffer, "UTF-8");
				data.add(str);
			}
		}

		return data.toArray(new String[data.size()]);
	}

	public static class PackData {
		private final int version;
		private final String[] data;

		private PackData(int version, String[] data) {
			super();
			this.version = version;
			this.data = data;
		}

		public int getVersion() {
			return version;
		}

		public String[] getData() {
			return data;
		}

		@Override
		public String toString() {
			return getClass().getSimpleName() + "[version=" + version
					+ ", data=" + Arrays.toString(data) + "]";
		}
	}
}
