/**
 * Created by vawanir on 5/9/2017.
 */
class A {
        public static void method1() {
            method2();
            B obj = new B(42.0);
            obj.method4();
        }
        public static void method2() {
            method3(42);
            method3("Hello", 42);
        }
        public static void method3(int x) {
        }
        public static void method3(String x, int y) {}

    }
         class B {
            public B(double x) {
// Implied call to Object's constructor
                method5();
            }
            public void method4() {
                method5();
                method6();
            }
            public void method5() {}

        public void method6() {}
    }

