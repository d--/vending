package kata.vending;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Jacoco erroneously counts auto-generated enum bytecode as needing covered.
 * This exists only so we don't have to always check why coverage wasn't 100%.
 */
public class EnumsTest {
    /**
     * A list of enum classes to exercise.
     */
    private List<Class<? extends Enum<?>>> enums;

    /**
     * Set up the list of enums.
     */
    @Before
    public final void listEnums() {
        enums = new ArrayList<>();
        enums.add(Currency.class);
        enums.add(Product.class);
        enums.add(Coin.Size.class);
        enums.add(Coin.Weight.class);
    }

    /**
     * Simplest possible exercising "test" for our enums.
     *
     * @throws NoSuchMethodException     yep
     * @throws InvocationTargetException yep
     * @throws IllegalAccessException    yep
     */
    @Test
    public final void coverEnums() throws NoSuchMethodException,
            InvocationTargetException,
            IllegalAccessException {
        for (Class<? extends Enum> e : enums) {
            Method valuesMethod = e.getMethod("values");
            Object[] values = (Object[]) valuesMethod.invoke(null);
            Method valueOfMethod = e.getMethod("valueOf", String.class);
            valueOfMethod.invoke(null, ((Enum) values[0]).name());
        }
    }
}
