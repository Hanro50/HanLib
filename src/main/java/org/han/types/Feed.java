package org.han.types;

@FunctionalInterface
public interface Feed<in, out> {
	public out get(in input);
}
