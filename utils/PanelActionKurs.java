package org.apache.wicket.erp.utils;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.erp.MenuPanel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
public class PanelActionKurs extends MenuPanel{
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
	private Button listKursButton;
	private Button listKursPajakButton;
	public static int actionType=0;
	private static int prevActionType=0;
	private IActionHandler _iActionHandler;
	@SuppressWarnings("deprecation")
	public PanelActionKurs(String id,IActionHandler iActionHandler)
	{
		super(id);
		_iActionHandler=iActionHandler;
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
					bRet=_iActionHandler.insert();
				if(prevActionType==2)
					bRet=_iActionHandler.update();
				if(prevActionType==3)
					bRet=_iActionHandler.delete();
				if(bRet==1)
				{
					if(prevActionType==0)
						info("Insert data has already successfully.");
					if(prevActionType==2)
						info("Update data has already successfully.");
					if(prevActionType==3)
						info("Delete data has already successfully.");
					setActionEnabled(true);
				}
				else if(bRet==0)
				{
					if(prevActionType==0)
						error("Failed to insert data. Please try again!");
					if(prevActionType==2)
						error("Failed to update data. Please try again!");
					if(prevActionType==3)
						error("Failed to delete data. Please try again!");
					setActionEnabled(true);
				}
				else
				{
					//Duplicate..
					activateButton.setVisible(true);
				}
				
				setExecutionEnabled(false);
			}
		}.setDefaultFormProcessing(false);
		saveButton.setEnabled(actionType==1? true:false);
		add(saveButton);
		
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
				_iActionHandler.cancel(actionType==2 ||actionType==3);
				setActionEnabled(true);
			}
		}.setDefaultFormProcessing(false);
		cancelButton.setEnabled(actionType==1? true:false);
		add(cancelButton);
		
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
				_iActionHandler.update();
				_iActionHandler.activate();
			}
		}.setDefaultFormProcessing(false);
		activateButton.setVisible(false);
		activateButton.add(new SimpleAttributeModifier(
                "onclick", "if(!confirm('Do you really want to activate?')) return false;"));
		add(activateButton);
		
		listKursButton=new AjaxButton("kursButton")
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				actionType=prevActionType;
			}
		}.setDefaultFormProcessing(false);
		listKursButton.setEnabled((actionType==2||actionType==3)? true:false);
		add(listKursButton);
		
		listKursPajakButton=new AjaxButton("kurspajakButton")
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit()
			{
				// just set a new instance of the page
				
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				actionType=prevActionType;
				//_iActionHandler.showKursPajak(target);
			}
		}.setDefaultFormProcessing(false);
		listKursPajakButton.setEnabled((actionType==2||actionType==3)? true:false);
		add(listKursPajakButton);
		
	}
	
	private void setActionEnabled(boolean flag)
	{
		newButton.setEnabled(flag);
		editButton.setEnabled(flag);
		deleteButton.setEnabled(flag);
		listKursButton.setEnabled(flag);
		listKursPajakButton.setEnabled(flag);
	}
	
	private void setExecutionEnabled(boolean flag)
	{
		saveButton.setEnabled(flag);
		cancelButton.setEnabled(flag);
	}
}
