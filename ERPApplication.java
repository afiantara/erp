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

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.util.file.Folder;

/**
 * Application class for hello world example.
 * 
 * @author Jonathan Locke
 */
public class ERPApplication extends AuthenticatedWebApplication
{
	private Folder uploadFolder = null;
	public static String pageTitle="";
	public static final List<Locale> LOCALES = Arrays.asList(Locale.ENGLISH,
			new Locale("nl", "NL"), Locale.GERMAN, Locale.SIMPLIFIED_CHINESE, Locale.JAPANESE,
			new Locale("pt", "BR"), new Locale("fa", "IR"), new Locale("da", "DK"),
			new Locale("th", "TH"), new Locale("ru"), new Locale("ko", "KR"));
	
	static
	{
		System.setProperty("java.awt.headless", "true");
	}
	/**
	 * Constructor.
	 */
	public ERPApplication()
	{
	}
	
	@Override
	public Session newSession(Request request, Response response)
	{
		return new MyAuthenticatedWebSession(request);
	}
	
	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends Page> getHomePage()
	{
		if(MyAuthenticatedWebSession.get().isSignedIn())
			return Home.class;
		else
			return LoginPage.class;
	}

	@Override
	protected Class<? extends WebPage> getSignInPageClass() {
		// TODO Auto-generated method stub
		return MySignInPage.class;
	}

	@Override
	protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
		// TODO Auto-generated method stub
		return MyAuthenticatedWebSession.class;
	}
	/**
	 * @see org.apache.wicket.authentication.AuthenticatedWebApplication#init()
	 */
	@Override
	protected void init()
	{
		super.init();
		getDebugSettings().setDevelopmentUtilitiesEnabled(true);
		//getDebugSettings().setAjaxDebugModeEnabled(true);
		getResourceSettings().setThrowExceptionOnMissingResource(false);
		uploadFolder = new Folder(System.getProperty("java.io.tmpdir"), "wicket-uploads");
		// Ensure folder exists
		uploadFolder.mkdirs();
	}
	
	/**
	 * @return the folder for uploads
	 */
	public Folder getUploadFolder()
	{
		return uploadFolder;
	}
}
