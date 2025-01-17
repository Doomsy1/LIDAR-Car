interface Animal {
    keysPressed=new boolean[256];
    thread()

    run(){
        checks key inputs
        if there is a key pressed,

    call keyPressed(key key)}

    void keyPressed(key key);
}

class Dog implements Animal {
    @Override
    public void makeSound(String a) {
        System.out.println("Woof! " + a);
    }
}

class Cat implements Animal {
    @Override
    public void makeSound(String a) {
        System.out.println("Meow! " + a);
    }
}

public class Inter_Eg {
    public static void main(String[] args) {
        Animal dog = new Dog();
        Animal cat = new Cat();

        dog.makeSound("kevin");
        cat.makeSound("kevin");

        dog.printData();
        cat.printData();
    }
}