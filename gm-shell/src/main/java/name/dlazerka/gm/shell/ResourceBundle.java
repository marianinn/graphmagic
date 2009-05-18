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

package name.dlazerka.gm.shell;

import java.text.MessageFormat;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class ResourceBundle {
    private static final String MESSAGES_FILENAME = ResourceBundle.class.getPackage().getName() + ".messages";

    private static java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle(MESSAGES_FILENAME);

    public static String getString(String key, String... params) {
        String text = resourceBundle.getString(key);

        if (params != null) {
            MessageFormat mf = new MessageFormat(text);
            text = mf.format(params);
        }

        return text;
    }
}
