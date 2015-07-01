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

import com.google.common.collect.Maps;
import uk.me.mantas.eternity.EKUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertNull;

public class ExposedClass {
	private final Class<?> cls;
	private final Object instance;

	public ExposedClass (final Class<?> cls) {
		this.cls = cls;
		this.instance = null;
	}

	public ExposedClass (final Object instance) {
		this.cls = instance.getClass();
		this.instance = instance;
	}

	private Class[] extractClasses (final Object[] args) {
		return Arrays.stream(args).map(arg -> {
			if (arg.getClass().getSimpleName().contains("Mockito")) {
				return arg.getClass().getSuperclass();
			} else {
				return toPrimitiveClass(arg.getClass());
			}
		}).toArray(Class[]::new);
	}

	private Class toPrimitiveClass (final Class cls) {
		final String name = cls.getSimpleName();
		switch (name) {
			case "Boolean": return boolean.class;
			case "Integer": return int.class;
			case "Byte": return byte.class;
			case "Character": return char.class;
			case "Float": return float.class;
			case "Double": return double.class;
			case "Short": return short.class;
		}

		return cls;
	}

	public Object call (final String methodName, final Object... args) {
		final Class[] argsClasses = extractClasses(args);
		final Map<Object, Class> argMap =
			IntStream.range(0, args.length)
				.mapToObj(i -> Maps.immutableEntry(args[i], argsClasses[i]))
				.collect(Collectors.toMap(
					Entry::getKey
					, Entry::getValue
					, EKUtils.throwingMerger()
					// Need to use LinkedHashMap to maintain insertion order.
					, LinkedHashMap::new));

		return call(methodName, argMap);
	}

	public Object call (final String methodName, final Map<Object, Class> args) {
		Method method = null;
		final Class[] argsClasses =
			args.entrySet().stream().map(Entry::getValue).toArray(Class[]::new);

		try {
			method = cls.getDeclaredMethod(methodName, argsClasses);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			assertNull(e);
		}

		method.setAccessible(true);
		final Object[] argsInstances =
			args.entrySet().stream().map(Entry::getKey).toArray(Object[]::new);

		try {
			return method.invoke(instance, argsInstances);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			assertNull(e);
		}

		return null;
	}

	public void set (final String fieldName, final Object value) {
		Field field = null;
		Field modifiersField = null;

		try {
			field = cls.getDeclaredField(fieldName);
			modifiersField = Field.class.getDeclaredField("modifiers");
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			assertNull(e);
		}

		field.setAccessible(true);
		modifiersField.setAccessible(true);

		try {
			modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
			field.set(instance, value);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			assertNull(e);
		}
	}
}
