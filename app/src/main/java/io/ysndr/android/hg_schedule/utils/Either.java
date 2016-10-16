package io.ysndr.android.hg_schedule.utils;

import rx.functions.Action1;

/**
 * Created by yannik on 10/16/16.
 */
public class Either<A, B> {

    private A left;
    private B right;

    private Either(A left, B right) {
        this.left = left;
        this.right = right;
    }

    public static <A, B> Either<A, B> left(A value) {
        return new Either<>(value, null);
    }

    public static <A, B> Either<A, B> right(B value) {
        return new Either<>(null, value);
    }

    public void fold(Action1<A> funcL, Action1<B> funcR) {
        if (right == null) funcL.call(left);
        else funcR.call(right);
    }
}