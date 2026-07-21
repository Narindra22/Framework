package util;

import java.lang.reflect.Method;

public class Util {

    public static boolean haveParameter(Method methode, Class<?> param) {
        for (Class<?> typeParametre : methode.getParameterTypes()) {
            if (typeParametre.equals(param)) {
                return true;
            }
        }

        return false;
    }
}
