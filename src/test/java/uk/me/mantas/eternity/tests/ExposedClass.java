/**
 * Eternity Keeper, a Pillars of Eternity save game editor.
 * Copyright (C) 2015 Kim Mantas
 * <p>
 * Eternity Keeper is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * Eternity Keeper is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.me.mantas.eternity.tests;

// This class serves to expose encapsulated functionality of classes so they can be unit tested
// without compromising that encapsulation in non-test code.

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertNull;

public class ExposedClass {
	private final Class<?> cls;

	public ExposedClass (final Class<?> cls) {
		this.cls = cls;
	}

	public Object call (final String methodName, Object... args) {

		return call(null, methodName, args);
	}

	public Object call (final Object instance, final String methodName, Object... args) {
		Method method = null;
		try {
			method = cls.getDeclaredMethod(methodName);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			assertNull(e);
		}

		method.setAccessible(true);

		try {
			return method.invoke(instance, args);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			assertNull(e);
		}

		return null;
	}
}
