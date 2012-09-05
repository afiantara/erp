package org.apache.wicket.erp;

import java.text.DateFormat;
import java.util.Date;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.AbstractReadOnlyModel;


/**
 * A simple component that displays current time
 * 
 * @author Igor Vaynberg (ivaynberg)
 */
public class MyLabel extends Label
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param id
	 *            Component id
	 * @param tz
	 *            String
	 */
	public MyLabel(String id, String tz)
	{
		super(id, new TitleModel(tz));

	}

	/**
	 * A model that returns current time in the specified timezone via a formatted string
	 * 
	 * @author Igor Vaynberg (ivaynberg)
	 */
	private static class TitleModel extends AbstractReadOnlyModel<String>
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private final DateFormat df;

		/**
		 * @param tz
		 */
		public TitleModel(String tz)
		{
			df = DateFormat.getTimeInstance( DateFormat.SHORT);
		}

		/**
		 * @see org.apache.wicket.model.AbstractReadOnlyModel#getObject()
		 */
		@Override
		public String getObject()
		{
			return df.format(new Date());
		}
	}
}