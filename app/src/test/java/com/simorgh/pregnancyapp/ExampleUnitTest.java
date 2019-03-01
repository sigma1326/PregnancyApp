package com.simorgh.pregnancyapp;

import org.junit.Test;

import io.reactivex.Observable;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
        Observable.just(1, 2, 3, 4, 5, 6)
                .filter(integer -> integer % 2 == 0)
                .subscribe(integer -> {
                    System.out.println(integer);

                });
    }
}