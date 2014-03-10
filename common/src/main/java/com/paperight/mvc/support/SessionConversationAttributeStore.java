package com.paperight.mvc.support;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.SessionAttributeStore;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

/**
 * This class handles how session scoped model attributes are stored and
 * retrieved from the HttpSession.  This implementation uses a timestamp
 * to distinguish multiple command objects of the same type.  This is needed
 * for users editing the same entity on multiple tabs of a browser.
 * @author MJones
 * @version Sep 2, 2010
 *
 */
public class SessionConversationAttributeStore implements SessionAttributeStore, InitializingBean {
	
	@Autowired 
	private RequestMappingHandlerAdapter requestMappingHandlerAdapter;
    
	private Logger _logger = LoggerFactory.getLogger(SessionConversationAttributeStore.class);
    
    private int numConversationsToKeep = 10;
    
    /* (non-Javadoc)
     * @see org.springframework.web.bind.support.SessionAttributeStore#storeAttribute(org.springframework.web.context.request.WebRequest, java.lang.String, java.lang.Object)
     */
    public void storeAttribute(WebRequest request, String attributeName, Object attributeValue) {
        Assert.notNull(request, "WebRequest must not be null");
        Assert.notNull(attributeName, "Attribute name must not be null");
        Assert.notNull(attributeValue, "Attribute value must not be null");
        
        // look for a conversation id as a request parameter
        String cId = getConversationIdFromRequest(request, attributeName);
        
        // create a new conversation id if it does not exist.
        if (cId == null || cId.trim().length() == 0) {
            cId = String.valueOf(System.currentTimeMillis());

            // set a request attribute so that the view can use it to pass along the 
            // conversation id.
            request.setAttribute(attributeName + "_cId", cId, WebRequest.SCOPE_REQUEST);
        }
        
        // calculate the session lookup key.
        String sessionLookupKey = calculateSessionLookupKey(attributeName, cId);
        
        _logger.debug("storeAttribute - storing bean reference for (" + sessionLookupKey + ").");
        
        // set the attribute value in the session.
        request.setAttribute(sessionLookupKey, attributeValue, WebRequest.SCOPE_SESSION);

        // handles adding to the queue and pruning old conversations if needed.
        handleQueueActions(request, attributeName, cId);
    }

    /* (non-Javadoc)
     * @see org.springframework.web.bind.support.SessionAttributeStore#retrieveAttribute(org.springframework.web.context.request.WebRequest, java.lang.String)
     */
    public Object retrieveAttribute(WebRequest request, String attributeName) {
        Assert.notNull(request, "WebRequest must not be null");
        Assert.notNull(attributeName, "Attribute name must not be null");
        
        // calculate what the session attribute name should be.
        String storeAttributeName = calculateAttributeNameInSession(request, attributeName);
        
        _logger.debug("retrieveAttribute - retrieving bean reference for (" + storeAttributeName + ").");
        
        // return the requested command object based upon the attribute
        return request.getAttribute(storeAttributeName, WebRequest.SCOPE_SESSION);
    }

    /* (non-Javadoc)
     * @see org.springframework.web.bind.support.SessionAttributeStore#cleanupAttribute(org.springframework.web.context.request.WebRequest, java.lang.String)
     */
    public void cleanupAttribute(WebRequest request, String attributeName) {
        Assert.notNull(request, "WebRequest must not be null");
        Assert.notNull(attributeName, "Attribute name must not be null");
        
        if (_logger.isDebugEnabled()) {
            String storeAttributeName = calculateAttributeNameInSession(request, attributeName);
            _logger.debug("cleanupAttribute - removing bean reference for (" + storeAttributeName + ").");
        }
        
        // remove the entity from the session and from the queue
        removeEntityFromSession(request, attributeName, 
            getConversationIdFromRequest(request, attributeName));
        
        dumpConversationQueuesToLog(request, attributeName);
    }
    
    /**
     * calculates the attributeName to be looked up in the session.
     * @param request
     * @param attributeName
     * @return
     */
    private String calculateAttributeNameInSession(WebRequest request, String attributeName) {
        // look for a conversation id.
        String cId = getConversationIdFromRequest(request, attributeName);
        
        if (cId != null && cId.trim().length() > 0) {
            attributeName = calculateSessionLookupKey(attributeName, cId);
        }
        
        return attributeName;
    }
    
    /**
     * convience method to calculate the session lookup attribute name
     * @param attributeName
     * @param cId
     * @return
     */
    private String calculateSessionLookupKey(String attributeName, String cId) {
        return attributeName + "_" + cId;
    }
    
    /**
     * gets the conversations holder or creates one if it does not exist.
     * @param request
     * @param attributeName
     * @return
     */
    @SuppressWarnings("unchecked")
    private Map<String, Queue<String>> getConversationsMap(WebRequest request) {
        
        // get a reference to the conversation queue holder.
        Map<String, Queue<String>> conversationQueueMap = 
            (Map<String, Queue<String>>)request.getAttribute( 
                "_sessionConversations", WebRequest.SCOPE_SESSION);
        
        // create the map if it does not exist.
        if (conversationQueueMap == null) {
            conversationQueueMap = new HashMap<String, Queue<String>>();

            // store the map on the session.
            request.setAttribute("_sessionConversations", 
                conversationQueueMap, WebRequest.SCOPE_SESSION);
        }
        
        return conversationQueueMap;
    }
    
    /**
     * @param conversationQueueMap
     * @param attributeName
     * @return
     */
    private void handleQueueActions(WebRequest request,
        String attributeName, String conversationId) {
        
        if (getNumConversationsToKeep() > 0) { 
        
            // get a reference to the conversation queue map
            Map<String, Queue<String>> conversationQueueMap = getConversationsMap(request);
            
            // get a reference to the conversation queue for the given attribute name
            Queue<String> queue = conversationQueueMap.get(attributeName);
            
            // create queue if necessary.
            if (queue == null) {
                // create new queue if needed.
                queue = new LinkedList<String>();
                conversationQueueMap.put(attributeName, queue);
            }

            // add the conversation id to the queue if it needs it.
            if (!queue.contains(conversationId)) {
                // add the cId to the queue.
                queue.add(conversationId);
                
                // since a new conversation id was added to the queue, we need to 
                // check to see if the queue needs to be pruned.
                pruneQueueIfNeeded(request, queue, attributeName);
            }
            
            // dump to log what is in the queues
            dumpConversationQueuesToLog(request, attributeName);
        }
    }
    
    /**
     * @param request
     * @param queue
     * @param attributeName
     */
    private void pruneQueueIfNeeded(WebRequest request, Queue<String> queue, String attributeName) {
        // now check to see if we have hit the limit of conversations for the
        // command name.
        if (queue.size() > getNumConversationsToKeep()) {
            
            if (_logger.isDebugEnabled()) {
                for (Object str : queue.toArray()) {
                    _logger.debug("pruneQueueIfNeeded - (" + attributeName + 
                        ") queue entry (" + str + " " + new java.util.Date(Long.parseLong((String)str)));
                }
            }
            
            // grab the next item to be removed.
            String conversationId = queue.peek();
            
            if (conversationId != null) {
            
                _logger.debug("pruneQueueIfNeeded - (" + attributeName + 
                    ") removed (" + conversationId + " " + new java.util.Date(
                        Long.parseLong(conversationId)));
    
                // remove the reference object from the session.
                removeEntityFromSession(request, attributeName, conversationId);
            }
        }
    }
    
    /**
     * @param request
     * @param attributeName
     * @param fullAttributeName
     */
    private void removeEntityFromSession(WebRequest request, String attributeName, 
        String conversationId) {
        
        // calculate the full session store attribute name.
        String fullAttributeName = calculateSessionLookupKey(attributeName, conversationId);
        
        // remove the attribute from the session.
        request.removeAttribute(fullAttributeName, WebRequest.SCOPE_SESSION);
        
        // remove the conversation from the queue
        if (getNumConversationsToKeep() > 0) {
            
            // get reference to the 
            Map<String, Queue<String>> conversationQueueHolder = 
                getConversationsMap(request);
    
            // get conversation queue for the given attribute name
            Queue<String> queue = conversationQueueHolder.get(attributeName);
            
            // create queue if necessary.
            if (queue != null) {
                if (conversationId != null && conversationId.trim().length() > 0) {
                    queue.remove(conversationId);
                }
            }
        }
    }

    /**
     * Utility method to display what is currently in the queue.
     * @param request
     * @param attributeName
     */
    private void dumpConversationQueuesToLog(WebRequest request, String attributeName) {

        if (_logger.isDebugEnabled()) {
            
            // get the conversation queue map
            Map<String, Queue<String>> conversationQueueMap = 
                getConversationsMap(request);
            
            // iterate over the map
            for (String key : conversationQueueMap.keySet()) {
                LinkedList<String> ll = (LinkedList<String>)conversationQueueMap.get(key);
                _logger.debug("dumpConversationQueuesLog - queue(" + key + ") - " + ll);
            }
        }
    }

    /**
     * @param request
     * @param attributeName
     * @return
     */
    private String getConversationIdFromRequest(WebRequest request, String attributeName) {
        return request.getParameter(attributeName + "_cId");        
    }

	public int getNumConversationsToKeep() {
		return numConversationsToKeep;
	}

	public void setNumConversationsToKeep(int numConversationsToKeep) {
		this.numConversationsToKeep = numConversationsToKeep;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		requestMappingHandlerAdapter.setSessionAttributeStore(this);
	}
    
}
