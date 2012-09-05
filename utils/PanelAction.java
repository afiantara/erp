package org.apache.wicket.erp.utils;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.erp.MenuPanel;
import org.apache.wicket.markup.html.form.Button;
public class PanelAction extends MenuPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Button saveButton;
	private Button cancelButton;
	private Button newButton;
	private Button editButton;
	private Button deleteButton;
	private Button activateButton;
	public static int actionType=0;
	private static int prevActionType=0;
	private IActionHandler _iActionHandler;
	private int idx=0;
	@SuppressWarnings("deprecation")
	public PanelAction(String id,int idx,IActionHandlerIndex iActionHandler)
	{
		super(id);
		_iActionHandler=(IActionHandlerIndex)iActionHandler;
		this.idx=idx;
		init();
	}
	
	public PanelAction(String id,boolean statusdisable,IActionHandler iActionHandler)
	{
		super(id);
		_iActionHandler=(IActionHandler)iActionHandler;
		init();
		if(statusdisable)
		{
			newButton.setEnabled(!statusdisable);
			editButton.setEnabled(!statusdisable);
			deleteButton.setEnabled(!statusdisable);
			saveButton.setEnabled(!statusdisable);
			cancelButton.setEnabled(!statusdisable);
		}
	}
	
	public PanelAction(String id,IActionHandler iActionHandler)
	{
		super(id);
		_iActionHandler=(IActionHandler)iActionHandler;
		init();
	}
	private void init()
	{
		newButton=new Button("newButton")
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit()
			{
				prevActionType=0;
				actionType=1;
				if(_iActionHandler instanceof IActionHandlerIndex)
					((IActionHandlerIndex)_iActionHandler).newAction(idx);
				else
					_iActionHandler.newAction();
				setActionEnabled(false);
				setExecutionEnabled(true);
			}
			
			
		}.setDefaultFormProcessing(false);
		newButton.setEnabled((actionType==0|| actionType==2 || actionType==3) ? true:false);
		add(newButton);
		
		editButton=new Button("editButton"){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit()
			{
				prevActionType=2;
				actionType=1;
				if(_iActionHandler instanceof IActionHandlerIndex)
					((IActionHandlerIndex)_iActionHandler).editAction(idx);
				else
					_iActionHandler.editAction();
				
				setActionEnabled(false);
				setExecutionEnabled(true);
			}
		}.setDefaultFormProcessing(false);
		editButton.setEnabled((actionType==2||actionType==3)? true:false);
		add(editButton);
		deleteButton=new Button("deleteButton"){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit()
			{
				prevActionType=3;
				actionType=1;
				if(_iActionHandler instanceof IActionHandlerIndex)
					((IActionHandlerIndex)_iActionHandler).deleteAction(idx);
				else
					_iActionHandler.deleteAction();
				setActionEnabled(false);
				setExecutionEnabled(true);
			}
		}.setDefaultFormProcessing(false);
		
		deleteButton.add(new SimpleAttributeModifier(
                "onclick", "if(!confirm('Do you really want to perform this action?')) return false;"));
		deleteButton.setEnabled((actionType==2||actionType==3)? true:false);
		add(deleteButton);
		
		saveButton=new Button("saveButton"){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit()
			{
				// just set a new instance of the page
				//update data into calling service..
				int bRet=0;
				if(prevActionType==0)
				{
					if(_iActionHandler instanceof IActionHandlerIndex)
						bRet=((IActionHandlerIndex)_iActionHandler).insert(idx);
					else
						bRet=_iActionHandler.insert();
						
				}
				if(prevActionType==2)
				{
					if(_iActionHandler instanceof IActionHandlerIndex)
						bRet=((IActionHandlerIndex)_iActionHandler).update(idx);
					else
						bRet=_iActionHandler.update();
				}
				if(prevActionType==3)
				{
					if(_iActionHandler instanceof IActionHandlerIndex)
						bRet=((IActionHandlerIndex)_iActionHandler).delete(idx);
					else
						bRet=_iActionHandler.delete();
				}
				
				if(bRet==1)
				{
					if(prevActionType==0)
						info("Insert data has already successfully.");
					if(prevActionType==2)
						info("Update data has already successfully.");
					if(prevActionType==3)
						info("Delete data has already successfully.");
					setActionEnabled(true);
					setExecutionEnabled(false);
				}
				else if(bRet==0)
				{
					if(prevActionType==0)
						error("Failed to insert data. Please try again!");
					if(prevActionType==2)
						error("Failed to update data. Please try again!");
					if(prevActionType==3)
						error("Failed to delete data. Please try again!");
					setActionEnabled(false);
					setExecutionEnabled(true);
				}
				else
				{
					setActionEnabled(false);
					//Duplicate..
					activateButton.setVisible(true);
					cancelButton.setEnabled(true);
					saveButton.setEnabled(false);
				}
			}
		}.setDefaultFormProcessing(false);
		saveButton.setEnabled(actionType==1? true:false);
		add(saveButton);
		
		
		
		activateButton=new Button("activateButton")
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit()
			{
				// just set a new instance of the page
				actionType=prevActionType;
				if(_iActionHandler instanceof IActionHandlerIndex)
					((IActionHandlerIndex)_iActionHandler).update(idx);
				else
					_iActionHandler.update();
				
			}
		}.setDefaultFormProcessing(false);
		activateButton.setVisible(false);
		activateButton.add(new SimpleAttributeModifier(
                "onclick", "if(!confirm('Do you really want to activate?')) return false;"));
		add(activateButton);
		
		cancelButton=new Button("cancelButton")
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit()
			{
				// just set a new instance of the page
				actionType=prevActionType;
				if(_iActionHandler instanceof IActionHandlerIndex)
					((IActionHandlerIndex)_iActionHandler).cancel(idx,actionType==2 ||actionType==3);
					
				else
					_iActionHandler.cancel(actionType==2 ||actionType==3);
				setActionEnabled(true);
			}
		}.setDefaultFormProcessing(false);
		cancelButton.setEnabled(actionType==1 || activateButton.isVisible()? true:false);
		add(cancelButton);
		
	}
	
	private void setActionEnabled(boolean flag)
	{
		newButton.setEnabled(flag);
		editButton.setEnabled(flag);
		deleteButton.setEnabled(flag);
	}
	
	private void setExecutionEnabled(boolean flag)
	{
		saveButton.setEnabled(flag);
		cancelButton.setEnabled(flag);
	}
}
