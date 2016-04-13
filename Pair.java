/**
 * Simple definition of Pair class
 * @param <A> first element of pair
 * @param <B> second element of pair
 */
public class Pair<A,B> {
	public A first;
	public B second;
	public Pair(A a, B b)
	{
		first = a;
		second = b;
	}
	public String ToString()
	{
		return "(" + first.toString() + " , " + second.toString() + ")";
	}
	
}