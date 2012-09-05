package org.apache.wicket.erp.confirm;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.erp.ERPWebPage;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;


/**
 * Homepage
 */
public class HomePage extends ERPWebPage {

    public HomePage(final PageParameters parameters) {

        // The label that shows the result from the ModalWindow
        final Label resultLabel = new Label("resultlabel", new Model(""));
        resultLabel.setOutputMarkupId(true);
        add(resultLabel);



        // The ModalWindow, showing some choices for the user to select.
        final ModalWindow selectModalWindow = new SelectModalWindow("modalwindow"){

            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void onSelect(AjaxRequestTarget target, String selection) {
                // Handle Select action
                resultLabel.setDefaultModelObject(selection);
                target.addComponent(resultLabel);
                close(target);
            }

            public void onCancel(AjaxRequestTarget target) {
                // Handle Cancel action
                resultLabel.setDefaultModelObject("ModalWindow cancelled.");
                target.addComponent(resultLabel);
                close(target);
            }

        };
        add(selectModalWindow);



        // The AjaxLink to open the modal window
        add(new AjaxLink("linkToModalWindow"){
            public void onClick(AjaxRequestTarget target) {
                selectModalWindow.show(target);
            }
        });
    }
}