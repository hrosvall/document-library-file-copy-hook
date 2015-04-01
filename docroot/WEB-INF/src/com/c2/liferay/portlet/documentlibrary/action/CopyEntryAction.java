package com.c2.liferay.portlet.documentlibrary.action;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.WindowState;
import javax.servlet.http.HttpServletResponse;

import com.liferay.portal.DuplicateLockException;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.servlet.ServletResponseConstants;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.struts.BaseStrutsPortletAction;
import com.liferay.portal.kernel.struts.StrutsPortletAction;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.asset.AssetCategoryException;
import com.liferay.portlet.asset.AssetTagException;
import com.liferay.portlet.documentlibrary.DuplicateFileException;
import com.liferay.portlet.documentlibrary.DuplicateFolderNameException;
import com.liferay.portlet.documentlibrary.NoSuchFileEntryException;
import com.liferay.portlet.documentlibrary.NoSuchFolderException;
import com.liferay.portlet.documentlibrary.SourceFileNameException;


public class CopyEntryAction extends BaseStrutsPortletAction {

	@Override
    public void processAction(StrutsPortletAction originalStrutsPortletAction,
            PortletConfig portletConfig, ActionRequest actionRequest,
            ActionResponse actionResponse) throws Exception {
        /**
         * This is the custom process action
         * Once you try to login this method will be invoked
         * We can write our own logic here
         * Invoke the original struts action at the end
         */
         
        String cmd = ParamUtil.getString(actionRequest, Constants.CMD);
        System.out.println(cmd);
        
        try {
			if (cmd.equals(Constants.COPY)) {
				copyEntries(actionRequest);
			}
			WindowState windowState = actionRequest.getWindowState();

			if (!windowState.equals(LiferayWindowState.POP_UP)) {
				//actionResponse.sendRedirect(actionRequest, actionResponse);
			}
			else {
				String redirect = PortalUtil.escapeRedirect(
					ParamUtil.getString(actionRequest, "redirect"));

				if (Validator.isNotNull(redirect)) {
					actionResponse.sendRedirect(redirect);
				}
			}
        }
        catch (Exception e) {
			if (e instanceof DuplicateLockException ||
				e instanceof NoSuchFileEntryException ||
				e instanceof NoSuchFolderException ||
				e instanceof PrincipalException) {

				if (e instanceof DuplicateLockException) {
					DuplicateLockException dle = (DuplicateLockException)e;

					SessionErrors.add(
						actionRequest, dle.getClass(), dle.getLock());
				}
				else {
					SessionErrors.add(actionRequest, e.getClass());
				}

				//setForward(actionRequest, "portlet.document_library.error");
			}
			else if (e instanceof DuplicateFileException ||
					 e instanceof DuplicateFolderNameException ||
					 e instanceof SourceFileNameException) {

				if (e instanceof DuplicateFileException) {
					HttpServletResponse response =
						PortalUtil.getHttpServletResponse(actionResponse);

					response.setStatus(
						ServletResponseConstants.SC_DUPLICATE_FILE_EXCEPTION);
				}

				SessionErrors.add(actionRequest, e.getClass());
			}
			else if (e instanceof AssetCategoryException ||
					 e instanceof AssetTagException) {

				SessionErrors.add(actionRequest, e.getClass(), e);
			}
			else {
				throw e;
			}
		}
		
        super.processAction(
                originalStrutsPortletAction, portletConfig, actionRequest,
                actionResponse);
    }

	protected void copyEntries(ActionRequest actionRequest) throws Exception {

		System.out.println("copyEntries method reached.");

	}

	@Override
    public String render(
            StrutsPortletAction originalStrutsPortletAction,
            PortletConfig portletConfig, RenderRequest renderRequest,
            RenderResponse renderResponse)
        throws Exception {
         
        /**
         * Our own render method
         * This method is for rendering the view
         * At the end call the original struts action
         */
        
        System.out.println("render method reached.");
        //return super.render(
        //    null, portletConfig, renderRequest, renderResponse);
        

        return "/portlet/document_library/copy_entry.jsp";
    }
    
	@Override
	public void serveResource(
			StrutsPortletAction originalStrutsPortletAction,
			PortletConfig portletConfig, ResourceRequest resourceRequest,
			ResourceResponse resourceResponse)
		throws Exception {

		System.out.println("serveResource method reached.");
		
		super.serveResource(
			originalStrutsPortletAction, portletConfig, resourceRequest,
			resourceResponse);
	}
  
}
