import java.util.StringJoiner;

public class People {
    public String role = "masses";
    private double income = 100;

    public People() {
    }

    public People(String role) {
        this.role = role;
    }

    private People(double income) {
        this.income = income;
    }

    public void info() {
        System.out.println("info  ...");
    }

    public String info2(String args0) {
        System.out.println("info2 is " + args0);
        return args0;
    }

    private double info3(double args0) {
        System.out.println("info3 is " + args0);
        return args0;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", People.class.getSimpleName() + "[", "]")
                .add("role='" + role + "'")
                .add("income=" + income)
                .toString();
    }
}