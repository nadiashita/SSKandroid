package corelibrary;

import org.mariuszgromada.math.mxparser.*;

public class LogicEvaluator
{ /// <summary>
 /** Evaluates a dynamically built logical expression 
  Implement try catch when calling this function : if an exception is thrown, the logical expression is not valid
  
  @param logicalExpression True AND False OR True
  @return 
 */
	/*
	public static boolean EvaluateLogicalExpression(String logicalExpression)
	{
		System.Data.DataTable table = new System.Data.DataTable();
		table.Columns.Add("", Boolean.class);
		table.Columns[0].Expression = logicalExpression;

		System.Data.DataRow r = table.NewRow();
		table.Rows.Add(r);
		boolean result = (boolean)r[0];
		return result;
	}*/

	public static boolean EvaluateLogicalExpression(String logicalExpression)
	{
		Expression e = new Expression(logicalExpression);
		mXparser.consolePrintln("Res: " + e.getExpressionString() + " = " + e.calculate());

		boolean result = e.calculate() >= 1.0 ? true : false;
		return result;
	}
}