/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.erp;

import java.text.DateFormat;
import java.util.Date;

import org.apache.wicket.Application;
import org.apache.wicket.erp.utils.UserInfo;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;

/**
 * Navigation panel for the examples project.
 * 
 * @author Eelco Hillenius
 */
public final class ERPWebHeader extends Panel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static ERPWebHeader app;
	public static ERPWebHeader get()
	{
		return app;
	}
	/**
	 * Construct.
	 * 
	 * @param id
	 *            id of the component
	 * @param exampleTitle
	 *            title of the example
	 * @param page
	 *            The example page
	 */
	public ERPWebHeader(String id, final String exampleTitle, WebPage page)
	{
		super(id);
		
		add(new Label("username",UserInfo.USERNAME));
		add(new Label("version", new AbstractReadOnlyModel<String>()
		{

			private static final long serialVersionUID = 1L;

			@Override
			public String getObject()
			{

				Package p = Application.class.getPackage();

				String version = p.getSpecificationVersion();

				if (version == null || version.length() == 0)
				{
					return "Missing Version";
				}
				else
				{
					return version;
				}
			}
		}));
		DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
		String strDate =df.format(new Date());
		add(new Label("clockdate",strDate));
		
		app=this;
	}
	
	
}
