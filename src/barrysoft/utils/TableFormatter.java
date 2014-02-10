package barrysoft.utils;

import java.util.Vector;

import barrysoft.common.Printer;
import barrysoft.utils.StringUtils;

public class TableFormatter {
	
	private String[]	headers;
	private String[][]	values;
	private char		separator;
	private int			indentation;
	private Printer		printer;
	
	public void print()
	{
		if (getPrinter() == null)
		{
			throw new IllegalStateException("You must specify a printer " +
					"with the setPrinter "+
					"method before calling the print method.");
		}
		
		String[] rows = renderTable();
		
		for (String row : rows)
			getPrinter().print(row, false);
	}
	
	public String[] renderTable()
	{
		Vector<String> rows = new Vector<String>();
		
		int maxColumns = getMaxColumns();
		
		if (maxColumns > 0)
		{
			int[] columnWidths = getColumnWidths(maxColumns);
			String[] formats = getFormatStrings(columnWidths, getIndentation());
			
			rows.add(assembleRow(getHeaders(), formats));
			rows.add(assembleSeparator(columnWidths, formats));
			
			for (int i = 0; i < getValues().length; i++)
				rows.add(assembleRow(getValues()[i], formats));
		}
	  
	  	return (String[]) rows.toArray(new String[rows.size()]);
	}
	
	protected int getMaxColumns()
	{
		if (getValues() == null)
			throw new IllegalStateException("Values must be set before renderTable is called.");
		
		int maxColumns = 0;
		for (int i = 0; i < getValues().length; i++)
			maxColumns = Math.max(getValues()[i].length, maxColumns);
		
		return maxColumns;
	}
	
	protected int[] getColumnWidths(int maxColumns)
	{
		if (getValues() == null)
			throw new IllegalStateException("Values must be set before renderTable is called.");

		// Find the maximum length of a string in each column
		int[] lengths = new int[maxColumns];
		  
		for (int i=0; i < headers.length; i++)
			lengths[i] = Math.max(headers[i].length(), lengths[i]);
		  
		for (int i = 0; i < getValues().length; i++)
			for (int j = 0; j < getValues()[i].length; j++)
				lengths[j] = Math.max(getValues()[i][j].length(), lengths[j]);
		
		return lengths;
	}
	
	protected String[] getFormatStrings(int[] columnWidths, int indent)
	{
		String sindent = StringUtils.repeatString("\t", indent);
		  
		// Generate a format string for each column
		String[] formats = new String[columnWidths.length];
		for (int i = 0; i < columnWidths.length; i++) {
			
			formats[i] = "";
			
			if (i == 0)
				formats[i] += sindent;
			
			formats[i] += "%1$-" + columnWidths[i] + "s";
			
			if (i == columnWidths.length-1)
				formats[i] += "\n";
			else
				formats[i] += " ";
		}
		
		return formats;
	}
	
	protected String assembleRow(String[] values, String[] formats)
	{
		String row = "";
		
		for (int i=0; i < values.length; i++)
			row += String.format(formats[i], values[i]);
		
		return row;
	}
	
	protected String assembleSeparator(int[] columnWidths, String[] formats)
	{
		String row = "";
		
		for (int i=0; i < columnWidths.length; i++)
		{
			row += String.format(formats[i], 
					StringUtils.repeatString(Character.toString(getSeparator()),
							columnWidths[i]));
		}
	
		return row;
	}
	
	public String[] getHeaders()
	{
		return headers;
	}
	
	public void setHeaders(String[] headers)
	{
		this.headers = headers;
	}
	
	public String[][] getValues()
	{
		return values;
	}
	
	public void setValues(Vector<String[]> values)
	{
		this.values = values.toArray(new String[values.size()][]);
	}
	
	public void setValues(String[][] values)
	{
		this.values = values;
	}
	
	public char getSeparator()
	{
		return separator;
	}
	
	public void setSeparator(char separator)
	{
		this.separator = separator;
	}

	public int getIndentation()
	{
		return indentation;
	}

	public void setIndentation(int indentation)
	{
		this.indentation = indentation;
	}

	public Printer getPrinter()
	{
		return printer;
	}

	public void setPrinter(Printer printer)
	{
		this.printer = printer;
	}
	
	

}
