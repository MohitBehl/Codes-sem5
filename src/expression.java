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
		System.out.println(operands + " " + operators);
		System.out.println(solveHelper(operands, operators));
	}

	static void solveHelper1(ArrayList<String> operands, ArrayList<Character> operators) throws Exception {

		Stack<Integer> obj = new Stack<>();
	}

	static double solveHelper(ArrayList<String> operands, ArrayList<Character> operators) throws Exception {
		if (operators.size() != operands.size() - 1)
			throw new Exception("invalid expression");
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
