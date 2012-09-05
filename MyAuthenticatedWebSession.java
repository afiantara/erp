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

import org.apache.axis2.AxisFault;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.erp.utils.Service;
import org.apache.wicket.erp.utils.StringEncrypter;
import org.apache.wicket.erp.utils.UserInfo;
import org.apache.wicket.request.Request;

/**
 * Authenticated session subclass. Note that it is derived from AuthenticatedWebSession which is
 * defined in the auth-role module.
 * 
 * @author Jonathan Locke
 */
public class MyAuthenticatedWebSession extends AuthenticatedWebSession
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Construct.
	 * 
	 * @param request
	 *            The current request object
	 */
	public MyAuthenticatedWebSession(Request request)
	{
		super(request);
	}

	/**
	 * @see org.apache.wicket.authentication.AuthenticatedWebSession#authenticate(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean authenticate(final String username, final String password)
	{
		//final String WICKET = "wicket";
		// Check username and password
		//return WICKET.equals(username) && WICKET.equals(password);
		return doLogin(username,password);
	}

	private boolean doLogin(final String username,final String password)
    {
		Boolean ret=false;
    	try {
			Service _service=new Service(Service.ACCOUNTING_SERVICE_URL);
			sf.accounting.User user =new sf.accounting.User();
			user.setKstaff(username);
			String passwd;
			try {
		        String passPhrase   = "ekatamaputraperkasa";
		        // Create encrypter/decrypter class
		        StringEncrypter desEncrypter = new StringEncrypter(passPhrase);
				if(password.equals(""))
					passwd =    desEncrypter.encrypt("123");
				else
					passwd = desEncrypter.encrypt(password);
				
				user.setKpassword(passwd);
				Object[] params=new Object[]{user};
		    	Class[] retTypes =new Class[]{Boolean.class};
		    	Object[] response=_service.callServiceAccounting("login", params, retTypes);
				ret=(Boolean)response[0];
				if(ret)
				{
					//get user id from server.
					//--------------------------------------
					params=new Object[]{username};
					retTypes =new Class[]{sf.accounting.User.class};
			    	response=_service.callServiceAccounting("getUser", params, retTypes);
			    	user=(sf.accounting.User)response[0];
			    	UserInfo.USERID=username;
			    	UserInfo.USERNAME=user.getNstaff();
			    	UserInfo.PASSWORD = password;
			    	UserInfo.CODE_BRANCH=user.getKcabang();
			    	UserInfo.POSISI=user.getKjabatan();
			    	UserInfo.ACCESS_PRINT=user.getKprint().equals("Y")? true: false;
			    	UserInfo.ACCESS_PDF=user.getKconvert().equals("Y")? true: false;
			    	UserInfo.ACCESS_EXCEL=user.getKconvert().equals("Y")? true: false;
			    	UserInfo.ACCESS_HPP=user.getKhpp().equals("Y")? true: false;
			    	UserInfo.APPROVE_QUOTATION =user.getkApprove().equals("Y")? true: false;
			    	UserInfo.COMPANY = user.getKcompany(); //2011-09-16
			    	
			    	if(user.getKjabatan().equals("AD"))
			    		UserInfo.ACCESS_FILE=true;
			    	else
			    		UserInfo.ACCESS_FILE=false;
			    	//-----------------------------------------------------------------
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getLocalizedMessage());
			} 
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
    	return ret;
    }
	
	/**
	 * @see org.apache.wicket.authentication.AuthenticatedWebSession#getRoles()
	 */
	@Override
	public Roles getRoles()
	{
		if (isSignedIn())
		{
			// If the user is signed in, they have these roles
			return new Roles(Roles.ADMIN);
		}
		return null;
	}
}
