package application;

public class CostFunction {

    private String name;
    private double t1;
    private double t2;
    private double t3;
    private double t4;
    private double t5;
    private double t6;

    public CostFunction(String name, double t1, double t2, double t3, double t4, double t5, double t6) {
        this.name = name;
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
        this.t4 = t4;
        this.t5 = t5;
        this.t6 = t6;
    }

    public CostFunction() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getT1() {
        return t1;
    }

    public void setT1(double t1) {
        this.t1 = t1;
    }

    public double getT2() {
        return t2;
    }

    public void setT2(double t2) {
        this.t2 = t2;
    }

    public double getT3() {
        return t3;
    }

    public void setT3(double t3) {
        this.t3 = t3;
    }

    public double getT4() {
        return t4;
    }

    public void setT4(double t4) {
        this.t4 = t4;
    }

    public double getT5() {
        return t5;
    }

    public void setT5(double t5) {
        this.t5 = t5;
    }

    public double getT6() {
        return t6;
    }

    public void setT6(double t6) {
        this.t6 = t6;
    }

    @Override
    public String toString() {
        return "CostFunction{" +
                "name='" + name + '\'' +
                ", t1=" + t1 +
                ", t2=" + t2 +
                ", t3=" + t3 +
                ", t4=" + t4 +
                ", t5=" + t5 +
                ", t6=" + t6 +
                '}';
    }
}
