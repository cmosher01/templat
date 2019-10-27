/*
 * Created on 2006-10-09
 */

package net.sourceforge.templat;

import java.util.Objects;

/**
 * Contains static utility methods intended to be called from templates.
 * @author Chris Mosher
 */
@SuppressWarnings("unused")
public class Util {
    /**
     * Checks to see if the given reference is <code>null</code>.
     * @param <T> data-type of object
     * @param object the reference to check
     * @return <code>true</code> if the reference is <code>null</code>.
     */
    public static <T> Boolean isNull(final T object) {
        return Objects.isNull(object);
    }
}
