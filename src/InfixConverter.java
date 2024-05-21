import javax.swing.*;
        import java.util.regex.Matcher;
        import java.util.regex.Pattern;

public class InfixConverter {
    // Regular expression to identify invalid characters in the input
    Pattern invalidCharactersPattern = Pattern.compile("[^0-9+\\-*/^()]");
    Matcher invalidMatcher;
    private final ArrayStack operatorStack = new ArrayStack(); // Stack for holding operators during conversion
    private final ArrayStack evaluationStack = new ArrayStack(); // Stack for holding values during postfix evaluation

    public InfixConverter() {
    }

    // Main method to process the expression: convert and evaluate
    public void processExpression(String expression) {
        String postfix = infixToPostfix(expression);
        displayResults(expression, postfix, evaluatePostfix(postfix));
    }

    // Converts infix expression to postfix notation
    public String infixToPostfix(String expression) {
        StringBuilder postfixResult = new StringBuilder();
        Pattern operatorPattern = Pattern.compile("[+\\-*/^()]");
        Pattern operandPattern = Pattern.compile("[0-9]");
        Matcher operatorMatcher;
        Matcher operandMatcher;
        char currentChar;

        // Validate expression before conversion
        if (!validateExpression(expression)) {
            return "";
        }

        // Loop through the expression to convert it
        for (int i = 0; i < expression.length(); i++) {
            currentChar = expression.charAt(i);
            operatorMatcher = operatorPattern.matcher(Character.toString(currentChar));
            operandMatcher = operandPattern.matcher(Character.toString(currentChar));

            // Append digits directly to the postfix result
            if (operandMatcher.find()) {
                postfixResult.append(currentChar);
            }
            // Push opening parenthesis onto the stack
            else if (currentChar == '(') {
                operatorStack.push(currentChar);
            }
            // Pop operators until an opening parenthesis is found
            else if (currentChar == ')') {
                while (!operatorStack.isEmpty() && (char) operatorStack.top() != '(') {
                    postfixResult.append(operatorStack.pop());
                }
                operatorStack.pop(); // Remove the opening parenthesis
            }
            // Manage operator precedence and stack operations
            else if (operatorMatcher.find()) {
                while (!operatorStack.isEmpty() && precedenceLevel(currentChar) <= precedenceLevel((char) operatorStack.top())) {
                    postfixResult.append(operatorStack.pop());
                }
                operatorStack.push(currentChar);
            }
        }

        // Append any remaining operators in the stack to the postfix result
        while (!operatorStack.isEmpty()) {
            postfixResult.append(operatorStack.pop());
        }

        return postfixResult.toString();
    }

    // Evaluates a postfix expression
    public double evaluatePostfix(String postfix) {
        char currentChar;

        for (int i = 0; i < postfix.length(); i++) {
            currentChar = postfix.charAt(i);

            if (Character.isDigit(currentChar)) {
                // Push digit values onto the stack
                evaluationStack.push((double) (currentChar - '0'));
            } else {
                // Perform operations using the top two values on the stack
                double rightOperand = (double) evaluationStack.pop();
                double leftOperand = (double) evaluationStack.pop();

                switch (currentChar) {
                    case '^' -> evaluationStack.push(Math.pow(leftOperand, rightOperand));
                    case '*' -> evaluationStack.push(leftOperand * rightOperand);
                    case '/' -> evaluationStack.push(leftOperand / rightOperand);
                    case '+' -> evaluationStack.push(leftOperand + rightOperand);
                    case '-' -> evaluationStack.push(leftOperand - rightOperand);
                }
            }
        }

        return (double) evaluationStack.pop();
    }

    // Validates the input expression for correctness
    public boolean validateExpression(String expression) {
        invalidMatcher = invalidCharactersPattern.matcher(expression);

        if (invalidMatcher.find()) {
            JOptionPane.showMessageDialog(null, "Only digits and operators (+, -, *, /, ^, (, )) are allowed.");
            return false;
        } else if (expression.length() > 20 || expression.length() < 3) {
            JOptionPane.showMessageDialog(null, "Expression length must be between 3 and 20 characters.");
            return false;
        } else {
            return true;
        }
    }

    // Determines the precedence level of operators
    public int precedenceLevel(char operator) {
        return switch (operator) {
            case '^' -> 3;
            case '*', '/' -> 2;
            case '+', '-' -> 1;
            default -> 0;
        };
    }

    // Displays the conversion and evaluation results
    public void displayResults(String original, String converted, double result) {
        if ("".equals(converted)) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid expression.");
        } else {
            JOptionPane.showMessageDialog(null, "Original: " + original + "\nConverted: " + converted + "\nResult: " + result);
        }
    }
}
