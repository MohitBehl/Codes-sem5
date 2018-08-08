import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class expression {

	public static void main(String[] args) {
		Scanner scn = new Scanner(System.in);
		String expression = scn.nextLine();
		scn.close();
		try {
			solve(expression);
		} catch (Exception e) {
			System.out.println("error:" + e);
		}
	}

	static void solve(String expression) throws Exception {
		ArrayList<String> operands = new ArrayList<>();
		ArrayList<Character> operators = new ArrayList<>();
		parseExpression(expression, operands, operators);
		if (operators.size() != operands.size() - 1)
			throw new Exception("invalid expression");
		System.out.println(operands + " " + operators);
		System.out.print("without DMAS:");
		System.out.printf("%.2f", solveHelper(operands, operators));
		System.out.println();
		System.out.print("With DMAS:");
		System.out.printf("%.2f", solveHelper1(operands, operators));
		System.out.println();
	}

	static int precedence(char ch) {
		int i = 0;
		switch (ch) {
		case '-':
			i = 1;
			break;
		case '+':
			i = 2;
			break;
		case '*':
			i = 3;
			break;
		case '/':
			i = 4;
			break;
		default:
			return -1;
		}
		// System.out.println("precedence of " + ch + " :" + i);
		return i;
	}

	static ArrayList<String> postfixCreator(ArrayList<String> operands, ArrayList<Character> operators) {
		Stack<Character> obj = new Stack<>();
		ArrayList<String> postfixExpression = new ArrayList<>();
		postfixExpression.add(operands.get(0));
		int operandIndex = 1;
		int operatorIndex = 0;
		boolean isOperator = true;
		while (operandIndex < operands.size()) {
			if (!isOperator) {
				postfixExpression.add(operands.get(operandIndex));
				operandIndex++;
				isOperator = !isOperator;
			} else if (isOperator) {
				if (obj.isEmpty())
					obj.push(operators.get(operatorIndex));
				else {
					char ch = operators.get(operatorIndex);
					while (!obj.isEmpty() && precedence(ch) <= precedence(obj.peek())) {
						postfixExpression.add(obj.pop() + "");
					}
					obj.push(ch);
				}
				operatorIndex++;
				isOperator = !isOperator;
			}
		}
		while (!obj.isEmpty())
			postfixExpression.add(obj.pop() + "");
		return postfixExpression;
	}

	static double solveHelper1(ArrayList<String> operands, ArrayList<Character> operators) throws Exception {
		ArrayList<String> postExpression = postfixCreator(operands, operators);
		System.out.println(postExpression);
		Stack<Double> stack = new Stack<>();
		int i = 0;
		while (i < postExpression.size()) {
			String str = postExpression.get(i);
			if (str.equals("*") || str.equals("+") || str.equals("-") || str.equals("/")) {
				Double op2 = stack.pop();
				Double op1 = stack.pop();
				switch (str) {
				case "+":
					stack.push(op1 + op2);
					break;
				case "-":
					stack.push(op1 - op2);
					break;
				case "*":
					stack.push(op1 * op2);
					break;
				case "/": {
					if (op2 == 0.0)
						throw new Exception("divide by zero exception");
					stack.push(op1 / op2);
				}
					break;
				}
			} else {
				stack.push(Double.parseDouble(str));
			}
			i++;
		}
		return stack.peek();
	}

	static double solveHelper(ArrayList<String> operands, ArrayList<Character> operators) {
		double result = Double.parseDouble(operands.get(0));
		int i = 1;
		int j = 0;
		while (i != operands.size()) {
			double oper = Double.parseDouble(operands.get(i));
			char operator = operators.get(j);
			if (operator == '+')
				result = result + oper;
			else if (operator == '-')
				result = result - (oper);
			else if (operator == '*')
				result = result * oper;
			else if (operator == '/')
				result = result / oper;
			// result = Precision.round(result,2);
			i++;
			j++;
		}
		return result;
	}

	static void parseExpression(String expression, ArrayList<String> operands, ArrayList<Character> operators)
			throws Exception {
		int i = 0;
		int length = expression.length();
		boolean chance = true;
		while (i < length) {
			char ch = expression.charAt(i);
			if (chance && ((ch >= '0' && ch <= '9') || ch == '.' || ch == '+' || ch == '-')) {
				String num = "";
				if (ch == '+' || ch == '-') {
					if (i < length - 1) {
						char nextCh = expression.charAt(i + 1);
						if (nextCh >= '0' && nextCh <= '9') {
							num += ch;
							ch = nextCh;
							i++;
						}
					} else
						throw new Exception("invalid expression format");
				}
				while (i < length && (ch >= '0' && ch <= '9' || ch == '.')) {
					num += ch;
					i++;
					if (i < length)
						ch = expression.charAt(i);
				}
				operands.add(num);
				i--;
				chance = !chance;
			} else if (!chance && (ch == '+' || ch == '*' || ch == '-' || ch == '/')) {
				operators.add(ch);
				chance = !chance;
			} else if (ch == ' ') {
				i++;
				continue;
			} else
				throw new Exception("invalid expression format");
			i++;
		}
	}
}
