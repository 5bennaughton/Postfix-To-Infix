import javax.swing.JOptionPane;

public class Main {
    public Main() {
    }

    public static void main(String[] args) {
        boolean running = true;
        InfixConverter testOne = new InfixConverter();

        while(running) {
            String input = JOptionPane.showInputDialog("Enter an infix expression");
            running = !testOne.validateExpression(input);
            if (!running) {
                testOne.processExpression(input);
            }
        }

    }
}
