/*
 * GraphMagic, package for scientists working in graph theory.
 * Copyright (C) 2009 Dzmitry Lazerka dlazerka@dlazerka.name
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Author: Dzmitry Lazerka dlazerka@dlazerka.name
 */

package name.dlazerka.gm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class Mark {
	private List<String> markList = new ArrayList<String>();
	private Map<String, String> markMap = new HashMap<String, String>();

	@Override
	public String toString() {
		return markList.toString() + markMap.toString();
	}

	public void setAt(int i, String s) {
		while (markList.size() <= i) {
			markList.add("?");
		}
		markList.set(i, s);
	}

	public void put(String key, String value) {
		markMap.put(key, value);
	}

	public List<String> getMarkList() {
		return markList;
	}

	public Map<String, String> getMarkMap() {
		return markMap;
	}

	public String get(int i) {
		if (markList.size() <= i) {
			return null;
		}
		return markList.get(0);
	}

	public String get(String key) {
		return markMap.get(key);
	}
}
