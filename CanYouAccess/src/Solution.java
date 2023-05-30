import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.regex.*;
import java.security.*;

public class Solution {
    public static void main(String[] args) throws Exception {
        DoNotTerminate.forbidExit();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int num = Integer.parseInt(br.readLine().trim());
            Object o;

            // İç içe geçmiş sınıfın oluşturulması
            Inner.Private innerPrivate = new Inner().new Private();
            // Oluşturulan iç içe geçmiş sınıfın çıktısı alınması
            o = innerPrivate;
            System.out.println(num + " is " + innerPrivate.powerof2(num));

            // reflection (yansıma) ile Outer sınıfının erişiminin sağlanması
            Class<?> clazz = Class.forName("Solution$Inner");
            Constructor<?> constructor = clazz.getDeclaredConstructor(Solution.class);
            constructor.setAccessible(true);
            Inner inner = (Inner) constructor.newInstance(new Solution());
            // Outer sınıfının çıktısının alınması
            o = inner;
            System.out.println(inner.getClass().getDeclaredMethods()[0].invoke(inner, num));

            // DoNotTerminate sınıfının çıktısının alınması
            o.getClass().getDeclaredField("flag").set(o, false);
        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (NoSuchElementException e) {
            System.out.println("NoSuchElementException: " + e.getMessage());
        } catch (InvocationTargetException e) {
            System.out.println("InvocationTargetException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Other Exception: " + e.getMessage());
        }
    }

    static class Inner {
        private class Private {
            private String powerof2(int num) {
                return ((num & num - 1) == 0) ? "power of 2" : "not a power of 2";
            }
        }
    }
}

class DoNotTerminate { // Bu sınıfın düzenlenmesi yasaktır
    public static class ForbiddenExitException extends SecurityException {
        private static final long serialVersionUID = 1L;
    }

    public static void forbidExit() {
        final SecurityManager securityManager = new SecurityManager() {
            @Override
            public void checkPermission(Permission permission) {
                if (permission.getName().contains("exitVM")) {
                    throw new ForbiddenExitException();
                }
            }
        };
        System.setSecurityManager(securityManager);
    }
}