package io.netstrap.common.tool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 将一串数据按照gzip方式压缩和解压缩
 * @author minghu.zhang
 */
public class GzipTool {

	/**
	 * 压缩
 	 */
	public static byte[] compress(byte[] data) throws IOException {
		if (data == null || data.length == 0) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		gzip.write(data);
		gzip.close();
		return out.toByteArray();
	}

	/**
	 * 压缩
	 */
	public static byte[] compress(String data) throws IOException {
		if (data == null || data.length() == 0) {
			return null;
		}
		return compress(data.getBytes("utf-8"));
	}

	/**
	 * 解压缩
 	 */
	public static byte[] uncompress(byte[] data) throws IOException {
		if (data == null || data.length == 0) {
			return data;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		GZIPInputStream unzip = new GZIPInputStream(in);
		byte[] buffer = new byte[256];
		int n;
		while ((n = unzip.read(buffer)) >= 0) {
			out.write(buffer, 0, n);
		}
		unzip.close();
		in.close();
		return out.toByteArray();
	}

	/**
	 * 解压缩
	 */
	public static String uncompress(String str) throws IOException {
		if (str == null || str.length() == 0) {
			return str;
		}
		byte[] data = uncompress(str.getBytes("utf-8"));
		return new String(data);
	}
}
