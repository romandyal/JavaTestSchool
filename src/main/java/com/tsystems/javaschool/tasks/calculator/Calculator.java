package com.tsystems.javaschool.tasks.calculator;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.LinkedList;
import java.util.Locale;

public class Calculator {

    /**
     * Evaluate statement represented as string.
     *
     * @param statement mathematical statement containing digits, '.' (dot) as decimal mark,
     *           parentheses, operations signs '+', '-', '*', '/'<br>
     *           Example: <code>(1 + 38) * 4.5 - 1 / 2.</code>
     * @return string value containing result of evaluation or null if statement is invalid
     */
    public String evaluate(String statement) {
        if(statement == null || statement.equals("")) return null;
        statement = statement.replaceAll(" ", "");

        LinkedList<Double> signsQueue = new LinkedList<Double>();
        LinkedList<Operations> opQueue = new LinkedList<Operations>();
        boolean openBracketFlag = false;
        for (int i = 0; i < statement.length(); i++) {
            char c = statement.charAt(i);

            if (c == Operations.OPEN_BR.getOper()) {
                openBracketFlag = true;
                opQueue.add(Operations.OPEN_BR);
                continue;
            } else if (c == Operations.CLOSE_BR.getOper()) { //If the bracket is closed, look for the open parenthesis among the operations
                try {
                    while (opQueue.getLast() != Operations.OPEN_BR) {
                        if (!calcOperations(signsQueue, opQueue.removeLast())) { //we perform operations in brackets
                            return null;
                        }
                    }
                    opQueue.removeLast(); //remove the bracket from the list of operations
                } catch (Exception e) {
                    return null;
                }
            } else {
                Operations symbol = isOperation(c);
                if (symbol != null && !((symbol == Operations.MIN || symbol == Operations.PLUS) && openBracketFlag)) {
                    //if symbol - operator
                    try {
                        while (!opQueue.isEmpty() && priority(opQueue.getLast()) >= priority(symbol)) { //работаем с приоритетами
                            if (!calcOperations(signsQueue, opQueue.removeLast())) {
                                return null;
                            }
                        }
                        opQueue.add(symbol);//add new operation
                    } catch (Exception e) {
                        return null;
                    }
                } else {  //if symbol is digit
                    String operand = "";
                    if (statement.charAt(i) == Operations.MIN.getOper() || statement.charAt(i) == Operations.PLUS.getOper()) {
                        operand += statement.charAt(i++);
                    }
                    while (i < statement.length() && (Character.isDigit(statement.charAt(i)) || statement.charAt(i) == '.')) {
                        operand += statement.charAt(i++);
                    }
                    --i;
                    try {
                        signsQueue.add(Double.parseDouble(operand));//add operand
                    } catch (Exception e) {  //unknown symbol
                        return null;
                    }
                }
            }
            openBracketFlag = false;
        }
        try {
            while (!opQueue.isEmpty()) { //perform operations until the last
                if (!calcOperations(signsQueue, opQueue.removeLast())) {
                    return null;
                }
            }
        } catch (Exception e) {
            return null;
        }

        DecimalFormat df = new DecimalFormat("#.####", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        return df.format(signsQueue.get(0));
    }

    private boolean calcOperations(LinkedList<Double> signsQueue, Operations operations) {
        double r = signsQueue.removeLast(); //first operand
        double l = signsQueue.removeLast(); //second operand
        Double res = null;
        try {
            switch (operations.getOper()) {
                case '+':
                    res = r + l;
                    break;
                case '-':
                    res = l - r;
                    break;
                case '*':
                    res = l * r;
                    break;
                case '/':
                    if(r == 0) return false;
                    res = l / r;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        signsQueue.add(res);
        return res != null;
    }

    public static void main(String[] args) {
        Calculator c = new Calculator();
        System.out.println(c.evaluate("(1+38)*4-5")); // Result: 151
        System.out.println(c.evaluate("7*6/2+8")); // Result: 29
        System.out.println(c.evaluate("-12)1//(")); // Result: null
    }

    public enum Operations {
        PLUS('+'),
        MIN('-'),
        DIV('/'),
        MUL('*'),
        OPEN_BR('('),
        CLOSE_BR(')');

        private Character oper;
        private String str;

        Operations(char oper) {
            this.oper = oper;
        }

        public Character getOper() {
            return oper;
        }

    }

    public Operations isOperation(char c) {
        if (c == Operations.PLUS.getOper()) {
            return Operations.PLUS;
        } else if (c == Operations.MIN.getOper()) {
            return Operations.MIN;
        } else if (c == Operations.MUL.getOper()) {
            return Operations.MUL;
        } else if (c == Operations.DIV.getOper()) {
            return Operations.DIV;
        }
        return null;
    }

    public static int priority(Operations op) {
        switch (op) {
            case PLUS:
            case MIN:
                return 1;
            case MUL:
            case DIV:
                return 2;
        }
        return -1;
    }


}
