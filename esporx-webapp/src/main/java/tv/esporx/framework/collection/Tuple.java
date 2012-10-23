package tv.esporx.framework.collection;

import static com.google.common.collect.Iterables.find;

import java.util.List;

import com.google.common.base.Predicate;


public class Tuple<L, R> {
	
	private L left;
	private R right;
	
	public Tuple(L left, R right) {
		this.left = left;
		this.right = right;
	}

	public R getRight() {
		return right;
	}

	public L getLeft() {
		return left;
	}

	public static <L,R> R findFirstRight(final List<Tuple<L, R>> tuples, final L left) {
		return find(tuples, new Predicate<Tuple<L, R>>() {

			@Override
			public boolean apply(Tuple<L, R> tuple) {
				return tuple.getLeft().equals(left);
			}
		}).getRight();
	}

	
}
