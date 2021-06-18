import java.util.StringJoiner;

public class User extends People implements Behavior ,Cloneable{
    public String name = "Tom";
    public int age = 18;
    private String gender = "male";

    public User() {
    }

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    private User(String gender) {
        this.gender = gender;
    }

    public void show() {
        System.out.println("hello ...");
    }

    public String show1(String args0) {
        System.out.println("show1 is " + args0);
        return args0;
    }

    private int show2(int args0) {
        System.out.println("show2 is " + args0);
        return args0;
    }

    @Override
    public void eat() {
        System.out.println("eat ...");
    }

    @Override
    public void drink() {
        System.out.println("drink ...");
    }

    public static void sleep(String args0,int args1){
        System.out.println(args0+" "+args1+" sleep ...");
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                .add("role='" + role + "'")
                .add("name='" + name + "'")
                .add("age=" + age)
                .add("gender='" + gender + "'")
                .toString();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
