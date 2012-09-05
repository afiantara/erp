package org.apache.wicket.erp.confirm;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

public abstract class SelectContentPanel extends Panel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SelectContentPanel(String id) {
        super(id);

        // Create the form, to use later for the buttons
        Form form = new Form("form");
        add(form);



        // Add some example 'selection' methods, to show as example
        // You can also use a full fledged form, I left that as an
        // exercise for the reader :-)
        add(new AjaxLink("selectionLink"){
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void onClick(AjaxRequestTarget target) {
                onSelect(target, new String("Selected something using the link."));
            }
        });

        form.add(new AjaxButton("selectionButton"){
        	@Override
            protected void onSubmit(AjaxRequestTarget target, Form form) {
                onSelect(target, new String("Selected something using the button."));
            }

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				
			}
        });



        // Add a cancel / close button.
        form.add(new AjaxButton("close") {

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				onCancel(target);
			}
        });

    }

    abstract void onCancel(AjaxRequestTarget target);

    abstract void onSelect(AjaxRequestTarget target, String selection);

}
