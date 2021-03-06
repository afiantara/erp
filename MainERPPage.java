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

import org.apache.wicket.erp.accounting.BranchPage;
import org.apache.wicket.examples.template.Banner;
import org.apache.wicket.examples.template.Banner1;
import org.apache.wicket.examples.template.Banner2;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;


/**
 * Everybody's favorite example!
 * 
 * @author Jonathan Locke
 */
public class MainERPPage extends Border
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** the current banner. */
	private Banner currentBanner;
	/**
	 * Constructor
	 */
	public MainERPPage(String id)
	{
		super(id);
		addToBorder(currentBanner = new Banner1("ad"));
		addToBorder(new Link("changeAdLink")
		{
			/**
			 * @see org.apache.wicket.markup.html.link.Link#onClick()
			 */
			@Override
			public void onClick()
			{
				if (currentBanner.getClass() == Banner1.class)
				{
					MainERPPage.this.replaceInBorder(currentBanner = new Banner2("ad"));
				}
				else
				{
					MainERPPage.this.replaceInBorder(currentBanner = new Banner1("ad"));
				}
			}
		});
		addToBorder(new BookmarkablePageLink<Void>("page1Link", Page1.class));
		addToBorder(new BookmarkablePageLink<Void>("page2Link", Page2.class));
		addToBorder(new BookmarkablePageLink<Void>("page3Link", Page2.class));
		addToBorder(new BookmarkablePageLink<Void>("page4Link", Page2.class));
		addToBorder(new BookmarkablePageLink<Void>("page5Link", Page2.class));
		addToBorder(new BookmarkablePageLink<Void>("page6Link", Page2.class));
		addToBorder(new BookmarkablePageLink<Void>("page7Link", Page2.class));
		addToBorder(new BookmarkablePageLink<Void>("page8Link", Page2.class));
		addToBorder(new BookmarkablePageLink<Void>("page9Link", Page2.class));
		addToBorder(new BookmarkablePageLink<Void>("pageBranchLink", BranchPage.class));
		
	}
}