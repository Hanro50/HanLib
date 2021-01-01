package za.net.hanro50.types;

@FunctionalInterface
public interface Feed<in, out> {
	public out get(in input);
}
