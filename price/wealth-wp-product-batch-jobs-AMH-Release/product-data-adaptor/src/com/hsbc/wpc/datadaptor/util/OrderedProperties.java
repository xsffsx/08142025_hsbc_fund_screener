package com.dummy.wpc.datadaptor.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class OrderedProperties extends Properties {

	private static final long serialVersionUID = 4112578634029874840L;

	/**
	 * The default values for this Properties.
	 */
	protected OrderedProperties defaults;

	private static final int NONE = 0, SLASH = 1, UNICODE = 2, CONTINUE = 3, KEY_DONE = 4, IGNORE = 5;

	private final LinkedHashMap map;

	/**
	 * Constructs a new Properties object.
	 */
	public OrderedProperties() {
		super();
		map = new LinkedHashMap();
	}

	public OrderedProperties(final OrderedProperties properties) {
		defaults = properties;
		map = new LinkedHashMap();
	}

	@Override
    public String getProperty(final String name) {
		Object result = this.map.get(name);
		String property = result instanceof String ? (String) result : null;
		if (property == null && defaults != null) {
			property = defaults.getProperty(name);
		}
		return property;
	}

	public synchronized Set getKeys() {
		return this.map.keySet();
	}

	@Override
    public Enumeration<?> propertyNames() {
		if (defaults == null) {
			return keys();
		}

		Hashtable<Object, Object> set = new Hashtable<Object, Object>(defaults.size() + size());
		Enumeration<?> keys = defaults.propertyNames();
		while (keys.hasMoreElements()) {
			set.put(keys.nextElement(), set);
		}
		keys = keys();
		while (keys.hasMoreElements()) {
			set.put(keys.nextElement(), set);
		}
		return set.keys();
	}

	@Override
	public synchronized boolean isEmpty() {
		return this.map.size() == 0;
	}

	@Override
	public Set<java.util.Map.Entry<Object, Object>> entrySet() {
		// return super.entrySet();
		return this.map.keySet();
	}

	@Override
	public synchronized Enumeration<Object> keys() {
		return new PropEnumeration(this.map);
	}

	private static class PropEnumeration implements Enumeration {
		private final Map map;
		private final Iterator ite;

		PropEnumeration(final Map map) {
			this.map = map;
			ite = map.keySet().iterator();
		}

		@Override
        public boolean hasMoreElements() {
			// return false;
			return ite.hasNext();
		}

		@Override
        public Object nextElement() {
			// return null;
			return ite.next();
		}

	}

	@Override
    public String getProperty(final String name, final String defaultValue) {
		Object result = this.map.get(name);
		String property = result instanceof String ? (String) result : null;
		if (property == null && defaults != null) {
			property = defaults.getProperty(name);
		}
		if (property == null) {
			return defaultValue;
		}
		return property;
	}

	@Override
    public synchronized void load(final InputStream in) throws IOException {
		if (in == null) {
			throw new NullPointerException();
		}
		BufferedInputStream bis = new BufferedInputStream(in);
		bis.mark(Integer.MAX_VALUE);
		boolean isAscii = isAscii(bis);
		bis.reset();

		if (isAscii) {
			load(new InputStreamReader(bis, "ISO8859-1")); //$NON-NLS-1$
		} else {
			load(new InputStreamReader(bis)); 
		}

	}

	private boolean isAscii(final BufferedInputStream in) throws IOException {
		byte b;
		while ((b = (byte) in.read()) != -1) {
			if (b == 0x23 || b == 0x0a || b == 0x3d) {// ascii: newline/#/=
				return true;
			}
			if (b == 0x15) {// EBCDIC newline
				return false;
			}
		}
		return false;
	}

	@Override
    public synchronized void load(final Reader reader) throws IOException {
		int mode = NONE, unicode = 0, count = 0;
		char nextChar, buf[] = new char[40];
		int offset = 0, keyLength = -1;
		boolean firstChar = true;
		char[] inbuf = new char[256];
		int inbufCount = 0, inbufPos = 0;

		while (true) {
			if (inbufPos == inbufCount) {
				if ((inbufCount = reader.read(inbuf)) == -1) {
					break;
				}
				inbufPos = 0;
			}
			nextChar = inbuf[inbufPos++];

			if (offset == buf.length) {
				char[] newBuf = new char[buf.length * 2];
				System.arraycopy(buf, 0, newBuf, 0, offset);
				buf = newBuf;
			}
			if (mode == UNICODE) {
				int digit = Character.digit(nextChar, 16);
				if (digit >= 0) {
					unicode = (unicode << 4) + digit;
					if (++count < 4) {
						continue;
					}
				} else if (count <= 4) {
                    // luni.09=Invalid Unicode sequences: illegal character
                    throw new IllegalArgumentException("Invalid Unicode sequences: illegal character");
				}
				mode = NONE;
				buf[offset++] = (char) unicode;
				if (nextChar != '\n') {
					continue;
				}
			}
			if (mode == SLASH) {
				mode = NONE;
				switch (nextChar) {
				case '\r':
					mode = CONTINUE; // Look for a following \n
					continue;
				case '\n':
					mode = IGNORE; // Ignore whitespace on the next line
					continue;
				case 'b':
					nextChar = '\b';
					break;
				case 'f':
					nextChar = '\f';
					break;
				case 'n':
					nextChar = '\n';
					break;
				case 'r':
					nextChar = '\r';
					break;
				case 't':
					nextChar = '\t';
					break;
				case 'u':
					mode = UNICODE;
					unicode = count = 0;
					continue;
				}
			} else {
				switch (nextChar) {
				case '#':
				case '!':
					if (firstChar) {
						while (true) {
							if (inbufPos == inbufCount) {
								if ((inbufCount = reader.read(inbuf)) == -1) {
									inbufPos = -1;
									break;
								}
								inbufPos = 0;
							}
							nextChar = inbuf[inbufPos++]; // & 0xff
							// not
							// required
							if (nextChar == '\r' || nextChar == '\n') {
								break;
							}
						}
						continue;
					}
					break;
				case '\n':
					if (mode == CONTINUE) { // Part of a \r\n sequence
						mode = IGNORE; // Ignore whitespace on the next line
						continue;
					}
					// fall into the next case
				case '\r':
					mode = NONE;
					firstChar = true;
					if (offset > 0) {
						if (keyLength == -1) {
							keyLength = offset;
						}
						String temp = new String(buf, 0, offset);
						this.map.put(temp.substring(0, keyLength), temp.substring(keyLength));
					}
					keyLength = -1;
					offset = 0;
					continue;
				case '\\':
					if (mode == KEY_DONE) {
						keyLength = offset;
					}
					mode = SLASH;
					continue;
				case ':':
				case '=':
					if (keyLength == -1) { // if parsing the key
						mode = NONE;
						keyLength = offset;
						continue;
					}
					break;
				}
				if (Character.isWhitespace(nextChar)) {
					if (mode == CONTINUE) {
						mode = IGNORE;
					}
					// if key length == 0 or value length == 0
					if (offset == 0 || offset == keyLength || mode == IGNORE) {
						continue;
					}
					if (keyLength == -1) { // if parsing the key
						mode = KEY_DONE;
						continue;
					}
				}
				if (mode == IGNORE || mode == CONTINUE) {
					mode = NONE;
				}
			}
			firstChar = false;
			if (mode == KEY_DONE) {
				keyLength = offset;
				mode = NONE;
			}
			buf[offset++] = nextChar;
		}
		if (mode == UNICODE && count <= 4) {
            // luni.08=Invalid Unicode sequences: expected format \\uxxxx
            throw new IllegalArgumentException("Invalid Unicode sequences: expected format");
		}
		if (keyLength == -1 && offset > 0) {
			keyLength = offset;
		}
		if (keyLength >= 0) {
			String temp = new String(buf, 0, offset);
			String key = temp.substring(0, keyLength);
			String value = temp.substring(keyLength);
			if (mode == SLASH) {
				value += "\u0000";
			}
			this.map.put(key, value);
		}
	}

	@Override
    public Object setProperty(final String name, final String value) {
		return this.map.put(name, value);
	}

	private boolean isMapEmpty() {
		return this.map.size() == 0;
	}

	@Override
    public String toString() {
		if (isMapEmpty()) {
			return "{}";
		}

		StringBuilder buffer = new StringBuilder(size() * 28);
		buffer.append('{');
		Iterator it = this.map.keySet().iterator();
		while (it.hasNext()) {
			Object key = it.next();
			buffer.append(key);
			buffer.append('=');
			Object value = this.map.get(key);
			buffer.append(value);
			if (it.hasNext()) {
				buffer.append(", ");
			}
		}
		buffer.append('}');
		return buffer.toString();
	}
}
