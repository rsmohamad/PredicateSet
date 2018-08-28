import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.stream.IntStream;

public class PredicateSetTest {

    private static PredicateSet<Integer> set1, set2;

    @BeforeClass
    public static void setup() {
        // set1 = {0, 1, 2, 3, 4, 5, 6}
        set1 = PredicateSet.singleton(0)
                .add(1).add(2).add(3).add(4).add(5).add(6);

        // set2 = {3, 4, 5, 6, 7, 8, 9}
        set2 = PredicateSet.singleton(3)
                .add(4).add(5).add(6).add(7).add(8).add(9);
    }

    @AfterClass
    public static void teardown() {
        set1 = set2 = null;
    }

    @Test
    public void testAdd() {
        PredicateSet<Integer> set = PredicateSet.singleton(3);
        set = set.add(3);

        assertTrue(set.contains(3));
        assertFalse(set.contains(4));

        set = set.add(4);

        assertTrue(set.contains(3));
        assertTrue(set.contains(4));
        assertFalse(set.contains(-4));
    }

    @Test
    public void testRemove() {
        PredicateSet<Integer> set = PredicateSet.singleton(0);

        for (int i = 0; i < 2; i++) {
            set = set.add(0);

            assertTrue(set.contains(0));
            assertFalse(set.contains(1));

            set = set.remove(0).remove(0);

            assertFalse(set.contains(0));
            assertFalse(set.contains(1));

            set = set.add(0);

            assertTrue(set.contains(0));
            assertFalse(set.contains(1));
        }
    }

    @Test
    public void testUnion() {
        final PredicateSet<Integer> union1 = set1.union(set2);
        final PredicateSet<Integer> union2 = set2.union(set1);

        // union1 = union2 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}
        IntStream.rangeClosed(0, 9).forEach(i -> {
            assertTrue(union1.contains(i));
            assertTrue(union2.contains(i));
        });

        assertFalse(union1.contains(-1));
        assertFalse(union2.contains(-1));
        assertFalse(union1.contains(10));
        assertFalse(union2.contains(10));
    }

    @Test
    public void testIntersect() {
        final PredicateSet<Integer> intersect1 = set1.intersect(set2);
        final PredicateSet<Integer> intersect2 = set2.intersect(set1);

        // intersect1 = intersect2 = {3, 4, 5, 6}
        IntStream.rangeClosed(3, 6).forEach(i -> {
            assertTrue(intersect1.contains(i));
            assertTrue(intersect2.contains(i));
        });

        IntStream.rangeClosed(0, 2).forEach(i -> {
            assertFalse(intersect1.contains(i));
            assertFalse(intersect2.contains(i));
        });

        IntStream.rangeClosed(7, 9).forEach(i -> {
            assertFalse(intersect1.contains(i));
            assertFalse(intersect2.contains(i));
        });
    }

    @Test
    public void testDifference() {
        final PredicateSet<Integer> diff1 = set1.difference(set2);
        final PredicateSet<Integer> diff2 = set2.difference(set1);

        // diff1 = {0, 1, 2}
        // diff2 = {7, 8, 9}
        IntStream.rangeClosed(3, 6).forEach(i -> {
            assertFalse(diff1.contains(i));
            assertFalse(diff2.contains(i));
        });

        IntStream.rangeClosed(0, 2).forEach(i -> {
            assertTrue(diff1.contains(i));
            assertFalse(diff2.contains(i));
        });

        IntStream.rangeClosed(7, 9).forEach(i -> {
            assertFalse(diff1.contains(i));
            assertTrue(diff2.contains(i));
        });
    }

    @Test
    public void testComplement() {
        PredicateSet<Integer> complementSet1 = set1.complement();

        IntStream.rangeClosed(0, 6).forEach(i -> assertFalse(complementSet1.contains(i)));
        IntStream.range(-100, 0).forEach(i -> assertTrue(complementSet1.contains(i)));
        IntStream.range(7, 100).forEach(i -> assertTrue(complementSet1.contains(i)));
    }
}
