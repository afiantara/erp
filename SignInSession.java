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
 * Session class for signin example. Holds and authenticates users.
 * 
 * @author Jonathan Locke
 */
public final class SignInSession extends AuthenticatedWebSession
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Trivial user representation */
	private String user;

	/**
	 * Constructor
	 * 
	 * @param request
	 */
	protected SignInSession(Request request)
	{
		super(request);
	}

	/**
	 * Checks the given username and password, returning a User object if if the username and
	 * password identify a valid user.
	 * 
	 * @param username
	 *            The username
	 * @param password
	 *            The password
	 * @return True if the user was authenticated
	 */
	@Override
	public final boolean authenticate(final String username, final String password)
	{
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
				
			} 
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			//QMessageBox.critical(this, "Invalid Login",e.getMessage());
		}
    	return ret;
    }
	/**
	 * @return User
	 */
	public String getUser()
	{
		return user;
	}

	/**
	 * @param user
	 *            New user
	 */
	public void setUser(final String user)
	{
		this.user = user;
	}

	/**
	 * @see org.apache.wicket.authentication.AuthenticatedWebSession#getRoles()
	 */
	@Override
	public Roles getRoles()
	{
		return null;
	}
}
