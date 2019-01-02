package webapp.utils.Ext;


import java.util.function.Predicate;

/**
 * author: Yukai
 *Description : Predicate class with two generic types, for the better using XXXDDD
 * **/

public interface PredicateX<A,B> extends Predicate<A> {

    boolean test(A a ,B b);

}
