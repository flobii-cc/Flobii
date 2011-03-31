package com.gwittit.client.facebook;

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.  
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayNumber;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwittit.client.facebook.entities.ActionLink;
import com.gwittit.client.facebook.entities.Album;
import com.gwittit.client.facebook.entities.Attachment;
import com.gwittit.client.facebook.entities.Comment;
import com.gwittit.client.facebook.entities.Cookie;
import com.gwittit.client.facebook.entities.ErrorResponse;
import com.gwittit.client.facebook.entities.EventInfo;
import com.gwittit.client.facebook.entities.EventMembers;
import com.gwittit.client.facebook.entities.FriendInfo;
import com.gwittit.client.facebook.entities.FriendList;
import com.gwittit.client.facebook.entities.Group;
import com.gwittit.client.facebook.entities.GroupMembers;
import com.gwittit.client.facebook.entities.MailboxFolder;
import com.gwittit.client.facebook.entities.MessageThread;
import com.gwittit.client.facebook.entities.Note;
import com.gwittit.client.facebook.entities.Notification;
import com.gwittit.client.facebook.entities.NotificationRequest;
import com.gwittit.client.facebook.entities.Photo;
import com.gwittit.client.facebook.entities.Session;
import com.gwittit.client.facebook.entities.SessionRecord;
import com.gwittit.client.facebook.entities.Stream;
import com.gwittit.client.facebook.entities.StreamFilter;
import com.gwittit.client.facebook.entities.User;
import com.gwittit.client.facebook.entities.UserInfo;
import com.gwittit.client.facebook.entities.UserStandardInfo;

/**
 * This is the main object for using the Facebook REST API in GWT.
 * 
 * @see <a
 *      href="http://wiki.developers.facebook.com/index.php/JS_API_T_FB.ApiClient">FB.ApiClient</a>
 * @see <a href="http://wiki.developers.facebook.com/index.php/API">Rest API</a>
 * 
 * @author olamar72
 */


public class FacebookApi{

    // ---------------- Public Methods ---------------------
    /**
     * Creates a new api
     */
    protected FacebookApi() {
        // loadApi();
    }

    /**
     * Check if session is valid
     * 
     * @param sessionRecord
     *            to check
     * @return true if session is valid
     */
    public static native boolean sessionIsExpired(SessionRecord sessionRecord) /*-{
        if ( sessionRecord == null ) {
            return true;
        } else {
            return $wnd.FB.ApiClient.sessionIsExpired (sessionRecord );    
        }
    }-*/;

    /**
     * Return if the session is valid
     * 
     * @return true if session is valid
     */
    public boolean isSessionValid() {
        // return getLoggedInUser () != null;
        SessionRecord sr = getSessionRecord ();
        if (sr == null) {
            return false;
        }
        return !sessionIsExpired ( sr );
    }

    /**
     * Get api key
     */
    public native String getApiKey() /*-{
        return  $wnd.FB.Facebook.apiClient.get_apiKey();
    }-*/;

    /**
     * Get session record
     */
    public native SessionRecord getSessionRecord() /*-{
        try{
            return $wnd.FB.Facebook.apiClient.get_session();
        } catch ( ex ) {
            alert ( "Debug: Exception while loading FB.apiClient " + ex );
        }
    }-*/;

    /**
     * Get uid of current logged in user
     * 
     * @return uid of user
     */
    public Long getLoggedInUser() {

        SessionRecord sr = getSessionRecord ();
        if (sr != null) {
            return sr.getUid ();
        }
        return null;
    }

    /**
     * This method adds a comment to an xid on behalf of a user. This
     * essentially works like stream.addComment and allows addition of comments
     * to an application's fb:comment and Comments Boxes.
     * <p/>
     * Desktop applications must pass a valid session key, and only the user
     * associated with that session key can add comments.
     * <p/>
     * In order for your application to publish a feed story associated with a
     * comment, that user must grant your application the publish_stream
     * extended permission.
     * 
     * @param xid
     *            string The xid of a particular Comments Box or fb:comments.
     * @param text
     *            string The comment/text to be added, as inputted by a user.
     *            optional
     * @param uid
     *            int The user ID to add a comment on behalf of. This defaults
     *            to the session user and must only be the session user if using
     *            a session secret (example: Desktop and JSCL apps).
     * @param title
     *            string The title associated with the item the user is
     *            commenting on. This is required if publishing a feed story as
     *            it provides the text of the permalink to give context to the
     *            user's comment.
     * @param url
     *            string The url associated with the item the user is commenting
     *            on. This is required if publishing a feed story as it is the
     *            permalink associated with the comment.
     * @param publish_to_stream
     *            bool Whether a feed story should be published about this
     *            comment. This defaults to false and can only be 'true' if the
     *            user has granted the publish_stream extended permission.
     * 
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/Comments.add">Comments.add</a>
     * 
     */
    public void commentsAdd(Comment comment, AsyncCallback<JavaScriptObject> callback) {
        callMethod ( "comments.add", comment, callback );
    }

    /**
     * Returns all comments for a given XID posted through fb:comments or the
     * Comments Box (which is created with the fb:comments (XFBML) tag). This
     * method is a wrapper for the FQL query on the comment FQL table.
     * <p/>
     * You can specify only one XID with this call. If you want to retrieve
     * comments for multiple XIDs, run fql.query against the comment FQL table.
     * <p/>
     * Note: Currently there is a bug in the facebook api, causing
     * <code>comments.get</code>; to result with unknown method error.
     * 
     * @param xid
     *            int The comment xid that you want to retrieve. For a Comments
     *            Box, you can determine the xid on the admin panel or in the
     *            application settings editor in the Facebook Developer
     *            application.
     * @param callback
     *            result
     * 
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/Comments.get">Facebook
     *      Comments.get</a>
     */
    public void commentsGet(String xid, final AsyncCallback<List<Comment>> callback) {
        // Facebook Bug
        // JavaScriptObject p = getAllParams ( CommentsGetParams.values (),
        // params );
        // callMethodRetList ( "comments.get", p, Comment.class, callback );

        // JSONObject p = getDefaultParams ();
        String fql = "select xid, text,fromid,time,id,username,reply_xid from comment where xid ='" + xid + "'";

        // Call Facebook Method
        fqlQuery ( fql,

        new Callback<JavaScriptObject> ( callback ) {
            public void onSuccess(JavaScriptObject result) {
                callback.onSuccess ( cast ( Comment.class, result ) );
            }
        } );
    }

    /**
     * This method removes a comment from an xid on behalf of a user (or not).
     * <p/>
     * Desktop applications must pass a valid session key, and only comments
     * made by the user can be removed by that user. When using the app secret,
     * an application may remove any of its comments. required
     * <p/>
     * 
     * @param xid
     *            string The xid of a particular Comments Box or fb:comments.
     * @param commentId
     *            string The comment_id, as returned by Comments.add or
     *            Comments.get, to be removed.
     * 
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/Comments.remove">Comments.remove</a>
     */
    public void commentsRemove(String xid, String commentId, AsyncCallback<JavaScriptObject> callback) {
        Json j = new Json ();
        j.put ( "xid", xid ).put ( "comment_id", commentId );

        callMethod ( "comments.remove", j.getJavaScriptObject (), callback );
    }

    /**
     * This method returns the number of friends of the current user who have
     * accounts on your site, but have not yet connected their accounts. Also
     * see fb:unconnected-friends-count. Note that this number is determined
     * using the information passed via connect.registerUsers. If you have not
     * previously called that function, this method will always return 0.
     * <p/>
     * You can use the response from this call to determine whether or not to
     * display a link allowing the user to invite their friends to connect as
     * well.
     * <p/>
     * 
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/JS_API_M_FB.ApiClient.Connect_getUnconnectedFriendsCount">Connect_getUnconnectedFriendsCount</a>
     */
    public void connectGetUnconnectedFriendsCount(AsyncCallback<Integer> callback) {
        JavaScriptObject p = getDefaultParams ().getJavaScriptObject ();
        callMethodRetInteger ( "connect.getUnconnectedFriendsCount", p, callback );
    }

    // public void connectRegisterUsers(Map<String, String> params,
    // AsyncCallback<JavaScriptObject> callback) {
    //
    // }

    // public void connectUnregisterUsers(Map<String, String> params,
    // AsyncCallback<JavaScriptObject> callback) {
    //
    // }

    /**
     * This method returns all cookies for a given user and application.
     * <p/>
     * Cookies only apply to Web applications; they do not apply to desktop
     * applications.
     * <p/>
     * 
     * @param uid
     *            int The user from whom to get the cookies. optional
     * @param name
     *            string The name of the cookie. If not specified, all the
     *            cookies for the given user get returned.
     * @param callback
     *            result
     * 
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/Data.getCookies">Data.getCookies</a>
     */
    public void dataGetCookies(String name, AsyncCallback<List<Cookie>> callback) {
        Json j = new Json ().put ( "name", name );
        callMethodRetList ( "data.getCookies", j.getJavaScriptObject (), Cookie.class, callback );
    }

    /**
     * Beta in Facebook.
     * 
     * This method sets a cookie for a given user and application.
     * 
     * You can set cookies for Web applications only; you cannot set cookies for
     * desktop applications.
     * 
     * required
     * 
     * @param uid
     *            int The user for whom this cookie needs to be set.
     * @param name
     *            string Name of the cookie.
     * @param value
     *            string Value of the cookie.
     * @param expires
     *            int Time stamp when the cookie should expire. If not
     *            specified, the cookie expires after 24 hours. (The time stamp
     *            can be longer than 24 hours and currently has no limit)
     * @param path
     *            string Path relative to the application's callback URL, with
     *            which the cookie should be associated. (Default value is /.)
     * 
     * @param callback
     *            result
     * 
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/Data.setCookie">Data.setCookie</a>
     */
    public void dataSetCookie(Cookie c, AsyncCallback<Boolean> callback) {
        callMethodRetBoolean ( "data.setCookie", c, callback );
    }

    /**
     * Cancels an event. The application must be an admin of the event. An
     * application is the admin of an event if the application created the event
     * on behalf of a user (with that user's active session) or if it is the
     * creator of the event itself (that is, the event was created without an
     * active user session).
     * <p/>
     * This method does not require a session key. However if you call this
     * method without an active user session, then your application can cancel
     * an event only if it is the event creator.
     * 
     * @param eid
     *            event id
     * @param cancelMessage
     *            The message sent explaining why the event was canceled. You
     *            can pass an empty string if you don't want to provide an
     *            explanation.
     * @param callback
     *            true if event was successfully cancelled
     */
    public void eventsCancel(Long eid, String cancelMessage, AsyncCallback<Boolean> callback) {
        Json j = new Json ().put ( "eid", eid ).put ( "cancel_message", cancelMessage );
        callMethodRetBoolean ( "events.cancel", j.getJavaScriptObject (), callback );
    }

    /**
     * Creates an event on behalf of the user if the application has an active
     * session key for that user; otherwise it creates an event on behalf of the
     * application. Applications can create events for a user if the user grants
     * the application the create_event extended permission.
     * <p/>
     * If you are creating an event on behalf of a user, then your application
     * is an admin for the event, while the user is the creator.
     * <p/>
     * You can upload an image and associate it with the event by forming the
     * request as a MIME multi-part message. See photos.upload for details on
     * the message format to use and the supported image types. You can replace
     * or delete images in an event using events.edit.
     * <p/>
     * This method does not require a session key. However if you call this
     * method without an active user session, then your application is both the
     * creator and admin for the event.
     * 
     * @param eventInfo
     *            information about the event
     * @param callback
     *            response to user
     * 
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/Events.create">
     *      events.create </a>
     * 
     */
    public void eventsCreate(EventInfo eventInfo, AsyncCallback<JavaScriptObject> callback) {
        Json j = new Json ().put ( "event_info", new JSONObject ( eventInfo ).toString () );
        callMethod ( "events.create", j.getJavaScriptObject (), callback );
    }

    /**
     * Edits the details of an existing event. The application must be an admin
     * of the event. An application is the admin of an event if the application
     * created the event on behalf of a user (with that user's active session)
     * or if it is the creator of the event itself (that is, the event was
     * created without an active user session).
     * <p/>
     * This method does not require a session key. However if you call this
     * method without an active user session, then your application can edit an
     * event only if it is the event creator.
     * 
     * @param eid
     *            eventid
     * @param eventInfo
     *            updated info
     * @param callback
     *            boolean if succeeded
     * 
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/Events.edit">Event.edit</a>
     */
    public void eventsEdit(Long eid, EventInfo event, AsyncCallback<Boolean> callback) {
        Json j = new Json ().put ( "eid", eid ).put ( "event_info", new JSONObject ( event ).toString () );
        callMethodRetBoolean ( "events.edit", j.getJavaScriptObject (), callback );
    }

    /**
     * 
     * Returns all visible events according to the filters specified. You can
     * use this method to find all events for a user, or to query a specific set
     * of events by a list of event IDs (eids).
     * <p/>
     * If both the uid and eids parameters are provided, the method returns all
     * events in the set of eids that are associated with the user. If no eids
     * parameter are specified, the method returns all events associated with
     * the specified user.
     * <p/>
     * If the uid parameter is omitted, the method returns all events associated
     * with the provided eids, regardless of any user relationship.
     * <p/>
     * The uid can be replaced by gid in order to get events hosted by a group
     * instead of by an individual user.
     * <p/>
     * If both parameters are omitted, the method returns all events associated
     * with the session user.
     * <p/>
     * The start_time and end_time parameters specify a (possibly open-ended)
     * window in which all events returned overlap. Note that if start_time is
     * greater than or equal to end_time, an empty top-level element is
     * returned.
     * <p/>
     * This method no longer requires a session key. However if you call this
     * method without an active user session, you can only get the events for
     * which your application was the creator; you can see only those event
     * attendees who authorized your application. Applications can create events
     * for users if the users grant the application the create_event extended
     * permission.
     * 
     * </pre>
     * 
     * @param uid
     *            int Filter by events associated with a user with this uid.
     * @param eids
     *            array Filter by this list of event IDs. This is a
     *            comma-separated list of event IDs.
     * @param start_time
     *            int Filter with this UTC as lower bound. A missing or zero
     *            parameter indicates no lower bound.
     * @param end_time
     *            int Filter with this UTC as upper bound. A missing or zero
     *            parameter indicates no upper bound.
     * @param rsvp_status
     *            string Filter by this RSVP status. The RSVP status should be
     *            one of the following strings:
     * 
     *            attending unsure declined not_replied
     * 
     * @param params
     *            map
     * 
     * @param callback
     *            result
     * 
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/JS_API_M_FB.ApiClient.Events_get">ApiClient.Events_Get</a>
     */
    public void eventsGet(EventInfo eventFilter, AsyncCallback<List<EventInfo>> callback) {
        callMethodRetList ( "events.get", eventFilter, EventInfo.class, callback );
    }

    /**
     * Returns membership list data associated with an event.
     * <p/>
     * This method no longer requires a session key. However if you call this
     * method without an active user session, you can only get the events for
     * which your application was the creator; you can see only those event
     * attendees who have authorized your application. Applications can create
     * events for users if the users grant the application the create_event
     * extended permission.
     * <p/>
     * 
     * @param eid
     *            id of event
     * @param params
     *            map
     * @param callback
     *            result
     * 
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/JS_API_M_FB.ApiClient.Events_getMembers">Events_getMembers</a>
     */
    public void eventsGetMembers(Long eid, AsyncCallback<EventMembers> callback) {
        Json j = new Json ().put ( "eid", eid );
        callMethodRetObject ( "events.getMembers", j.getJavaScriptObject (), EventMembers.class, callback );
    }

    /**
     * Valid values for param rsvp_status <code>events.rsvp</code>
     */
    public static enum RsvpStatus {
        attending, unsure, declined
    }

    /**
     * Sets a user's RSVP status for an event. An application can set a user's
     * RSVP status only if the following are all true:
     * <ul>
     * <li>The application is an admin for the event.
     * <li>The application has an active session for the user.
     * <li>The active user has granted the application the rsvp_event extended
     * permission.
     * </ul>
     * 
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/Events.rsvp">Events.rsvp</a>
     */
    public void eventsRsvp(Long eventId, RsvpStatus status, AsyncCallback<Boolean> callback) {
        Json j = new Json ();
        j.put ( "eid", eventId ).put ( "rsvp_status", status.toString () );
        callMethodRetBoolean ( "events.rsvp", j.getJavaScriptObject (), callback );
    }

    /**
     * TODO: Implement
     */
    public void fbmlDeleteCustomTags(Map<String, String> params, AsyncCallback<JavaScriptObject> callback) {
        // TODO Auto-generated method stub

    }

    /**
     * TODO: Implement
     */
    public void fbmlGetCustomTags(Map<String, String> params, AsyncCallback<JavaScriptObject> callback) {
        // TODO Auto-generated method stub

    }

    /**
     * TODO: Implement
     */
    public void fbmlRefreshImgSrc(Map<String, String> params, AsyncCallback<JavaScriptObject> callback) {
        // TODO Auto-generated method stub

    }

    /**
     * TODO: Implement
     */
    public void fbmlRefreshRefUrl(Map<String, String> params, AsyncCallback<JavaScriptObject> callback) {
        // TODO Auto-generated method stub

    }

    /**
     * TODO: Implement
     */
    public void fbmlRegisterCustomTags(Map<String, String> params, AsyncCallback<JavaScriptObject> callback) {
        // TODO Auto-generated method stub

    }

    /**
     * TODO: Implement
     */
    public void fbmlSetRefHandle(Map<String, String> params, AsyncCallback<JavaScriptObject> callback) {
        // TODO Auto-generated method stub

    }

    /**
     * Valid params for method <code>feed.publishUserAction</code>
     */

    /**
     * Publishes a story on behalf of the user owning the session, using the
     * specified template bundle. This method can publish one line stories to
     * the user's Wall only (or a short story to the user's Wall if the user
     * explicitly selects the short story size in the Feed dialog, then checks
     * the Always do this check box before publishing).
     * <p/>
     * For a description of the parameters (other than sequencer), see
     * feed.publishUserAction, as they are the same. For information on forming
     * the template_data object, see Template Data.
     * 
     * @param params
     *            map
     * 
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/JS_API_M_FB.ApiClient.Feed_publishUserAction">
     *      Feed_publishUserAction</a>
     * 
     */
    public void feedPublishUserAction( /* UserAction object */AsyncCallback<JavaScriptObject> callback) {
        Window.alert ( "Not implemented" );
    }

    /**
     * Returns whether or not two specified users are friends with each other.
     * The first array specifies one half of each pair, the second array the
     * other half; therefore, they must be of equal size.
     * 
     * @param uids1
     *            array A list of user IDs matched with uids2. This is a
     *            comma-separated list of user IDs.
     * @param uids2
     *            array A list of user IDs matched with uids1. This is a
     *            comma-separated list of user IDs.
     * 
     * @see <a
     *      hreF="http://wiki.developers.facebook.com/index.php/JS_API_M_FB.ApiClient.Friends_areFriends">Friends_areFriends</a>
     */
    public void friendsAreFriends(List<Long> uids1, List<Long> uids2, AsyncCallback<List<FriendInfo>> callback) {

        if (uids1.size () != uids2.size ()) {
            throw new IllegalArgumentException ( "uids1 and uids2 size must be equal" );
        }
        Json j = new Json ();
        j.put ( "uids1", uids1 ).put ( "uids2", uids2 );

        callMethodRetList ( "friends.areFriends", j.getJavaScriptObject (), FriendInfo.class, callback );
    }

    /**
     * See #friends_get(Map, AsyncCallback)
     */
    public void friendsGet(final AsyncCallback<List<Long>> callback) {
        friendsGet ( null, callback );
    }

    /**
     * Returns the Facebook user IDs of the current user's Facebook friends. The
     * current user is determined from the session_key parameter. The values
     * returned from this call are not storable.
     * <p/>
     * You can call this method without a session key to return a list of
     * friends of a user on your application's canvas page. The user must have
     * authorized your application in order to make this call without a session
     * key. This is similar to how Facebook passes the UIDs of friends of a user
     * on your application's canvas page.
     * 
     * @param flid
     *            int Returns the friends in a friend list.
     * 
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/JS_API_M_FB.ApiClient.Friends_get">Friends_get</a>
     */
    public void friendsGet(Integer flid, final AsyncCallback<List<Long>> callback) {
        Json j = new Json ().put ( "flid", flid );
        friendsGetGeneric ( "friends.get", j.getJavaScriptObject (), callback );
    }

    /**
     * A slightly different version of friends.get returning name and uid. See
     * #friends_get(AsyncCallback)
     * 
     * @param callback
     *            list of users.
     */
    public void friendsGetExtended(final AsyncCallback<List<User>> callback) {
        JSONObject p = getDefaultParams ();

        String fql = "SELECT uid, name FROM user WHERE uid IN (SELECT uid2 FROM friend WHERE uid1=" + getLoggedInUser () + ") ";
        p.put ( "query", new JSONString ( fql ) );
        callMethodRetList ( "fql.query", p.getJavaScriptObject (), User.class, callback );
    }

    /**
     * Returns the user IDs of the current user's Facebook friends who have
     * authorized the specific calling application or who have already connected
     * their Facebook accounts via Facebook Connect. The current user is
     * determined from the session_key parameter. The values returned from this
     * call are not storable.
     * 
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/JS_API_M_FB.ApiClient.Friends_getAppUsers">Friends_getAppUsers</a>
     */
    public void friendsGetAppUsers(AsyncCallback<List<Long>> callback) {
        JSONObject p = getDefaultParams ();
        friendsGetGeneric ( "friends.getAppUsers", p.getJavaScriptObject (), callback );
    }

    /**
     * Returns the names and identifiers of any friend lists that the user has
     * created. The current user is determined from the session_key parameter.
     * <p>
     * The values returned from this call are storable. You can store the ID of
     * a friend list that the user has elected for use in some feature of your
     * application, but you should verify the ID periodically, as users may
     * delete or modify lists at any time. Friend lists are private on Facebook,
     * so you cannot republish this information to anyone other than the logged
     * in user. Members of lists may be obtained using friends.get with an flid
     * parameter.
     * 
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/JS_API_M_FB.ApiClient.Friends_getLists">Friends_getLists</a>
     */
    public void friendsGetLists(final AsyncCallback<List<FriendList>> callback) {
        JSONObject p = getDefaultParams ();
        callMethodRetList ( "friends.getLists", p.getJavaScriptObject (), FriendList.class, callback );
    }

    /**
     * Returns the Facebook user IDs of the mutual friends between the source
     * user and target user. For the source user, you can either specify the
     * source's user ID (the source_id) or use the session key of the logged-in
     * user, but not specify both.
     * <p/>
     * The source user must have authorized your application.
     * <p/>
     * You cannot store the IDs that get returned from this call.
     * <p/>
     * Privacy applies to the results of this method: If the source user chooses
     * to not show friends on his or her public profile, then no mutual friends
     * get returned. If a mutual friend chooses to be hidden from search
     * results, then that user's UID does not get returned from this call.
     * 
     * @param target_uid
     *            int The user ID of one of the target user whose mutual friends
     *            you want to retrieve. optional
     * 
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/Friends.getMutualFriends">Friends_getMutualFriends</a>
     */
    public void friendsGetMutualFriends(Long targetUid, AsyncCallback<List<Long>> callback) {
        Json j = new Json ().put ( "target_uid", targetUid );
        friendsGetGeneric ( "friends.getMutualFriends", j.getJavaScriptObject (), callback );
    }

    /*
     * Method that parses long's from the response.
     */
    private void friendsGetGeneric(String method, JavaScriptObject params, final AsyncCallback<List<Long>> callback) {

        AsyncCallback<JavaScriptObject> ac = new AsyncCallback<JavaScriptObject> () {
            public void onFailure(Throwable caught) {
                callback.onFailure ( caught );
            }

            public void onSuccess(JavaScriptObject jso) {
                if ("{}".equals ( new JSONObject ( jso ).toString () )) {
                    callback.onSuccess ( Collections.EMPTY_LIST );
                } else {
                    JsArrayNumber jsArray = jso.cast ();
                    callback.onSuccess ( Util.convertNumberArray ( jsArray ) );
                }
            }
        };
        callMethod ( method, params, ac );
    }

    /**
     * Evaluates an FQL (Facebook Query Language) query.
     * 
     * Warning: If you use JSON as the output format, you may run into problems
     * when selecting multiple fields with the same name or with selecting
     * multiple "anonymous" fields (for example, SELECT 1+2, 3+4 ...).
     * 
     * @param query
     *            The query to perform, as described in the FQL documentation.
     *            See http://wiki.developers.facebook.com/index.php/FQL FQL
     *            Documentation
     * 
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/JS_API_M_FB.ApiClient.Fql_query">Fql_query</a>
     */
    public void fqlQuery(String query, AsyncCallback<JavaScriptObject> callback) {
        Json j = new Json ().put ( "query", query );
        callMethod ( "fql.query", j.getJavaScriptObject (), callback );
    }

    /**
     * Returns all visible groups according to the filters specified. You can
     * use this method to return all groups associated with a user, or query a
     * specific set of groups by a list of GIDs.
     * <p/>
     * If both the uid and gids parameters are provided, the method returns all
     * groups in the set of gids with which the user is associated. If the gids
     * parameter is omitted, the method returns all groups associated with the
     * provided user.
     * <p/>
     * However, if the uid parameter is omitted, the method returns all groups
     * associated with the provided gids, regardless of any user relationship.
     * <p/>
     * If both parameters are omitted, the method returns all groups of the
     * session user.
     * 
     * @param gids
     *            to filter by
     * 
     * @param callback
     *            result
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/JS_API_M_FB.ApiClient.Groups_get">Groups_get</a>
     */
    public void groupsGet(List<Long> gids, AsyncCallback<List<Group>> callback) {
        Json j = new Json ().put ( "gids", gids );
        callMethodRetList ( "groups.get", j.getJavaScriptObject (), Group.class, callback );
    }

    /**
     * Returns membership list data associated with a group.
     */
    public void groupsGetMembers(Long gid, AsyncCallback<GroupMembers> callback) {

        if (gid == null) {
            throw new IllegalArgumentException ( "gid cannot be null" );
        }
        Json j = new Json ().put ( "gid", gid );
        callMethodRetObject ( "groups.getMembers", j.getJavaScriptObject (), GroupMembers.class, callback );
    }

    /**
     * TODO: Implement
     */
    public void intlGetTranslations(Map<String, String> params, AsyncCallback<JavaScriptObject> callback) {
    }

    /**
     * TODO: Implement
     */
    public void intlUploadNativeStrings(Map<String, String> params, AsyncCallback<JavaScriptObject> callback) {
    }

    /**
     * TODO: Implement
     */
    public void linksGet(Map<String, String> params, AsyncCallback<JavaScriptObject> callback) {
    }

    /**
     * TODO: Implement
     */
    public void linksPost(Map<String, String> params, AsyncCallback<JavaScriptObject> callback) {
    }

    /**
     * TODO: Implement
     */
    public void liveMessageSend(Map<String, String> params, AsyncCallback<JavaScriptObject> callback) {
    }

    /**
     * Get current users mailboxes
     */
    public void messageGetMailBoxFolders(AsyncCallback<List<MailboxFolder>> callback) {
        Json j = new Json ();
        String fql = "SELECT folder_id, name, unread_count FROM mailbox_folder WHERE 1";
        j.put ( "query", fql );
        callMethodRetList ( "fql.query", j.getJavaScriptObject (), MailboxFolder.class, callback );
    }

    /**
     * Returns all of a user's messages and threads from the Inbox. The user
     * needs to grant the calling application the read_mailbox extended
     * permission.
     * 
     * <p/>
     * 
     * This method is a wrapper around the thread and message FQL tables; you
     * can achieve more fine-grained control by using those two FQL tables in
     * conjunction with the fql.multiquery API call.
     * 
     * Applications must pass a valid session key or a user ID.@
     * 
     * @param folderId
     *            The ID of the folder you want to return. The ID can be one of:
     *            0 (for Inbox), 1 (for Outbox), or 4 (for Updates).
     * @param includeRead
     *            Indicates whether to include notifications that have already
     *            been read. By default, notifications a user has read are not
     *            included.
     * @param limit
     *            Indicates the number of threads to return.
     * @param offset
     *            Indicates how many threads to skip from the most recent
     *            thread.
     * 
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/Message.getThreadsInFolder">Message.getThreadsInFolder</a>
     */
    public void messageGetThreadsInFolder(Integer folderId,
                                          Boolean includeRead,
                                          Integer limit,
                                          Integer offset,
                                          AsyncCallback<List<MessageThread>> callback) {
        Json j = new Json ().put ( "folder_id", folderId ).put ( "include_read", includeRead );
        j.put ( "limit", limit );
        j.put ( "offset", offset );

        callMethodRetList ( "message.getThreadsInFolder", j.getJavaScriptObject (), MessageThread.class, callback );
    }

    /**
     * Lets a user write a Facebook note through your application. Before a user
     * can write a note through your application, the user must grant your
     * application the create_note extended permission.
     * 
     * @param note
     *            to be created
     */
    public void notesCreate(Note note, AsyncCallback<Long> callback) {
        callMethodRetLong ( "notes.create", note, callback );
    }

    /**
     * Lets a user delete a Facebook note that was written through your
     * application. Before a user can delete the note, the user must grant your
     * application the create_note extended permission.
     */
    public void notesDelete(Long noteId, AsyncCallback<Boolean> callback) {
        JavaScriptObject p = new Json ().put ( "note_id", noteId ).getJavaScriptObject ();
        callMethodRetBoolean ( "notes.delete", p, callback );
    }

    /**
     * TODO: Implement
     */
    public void notesEdit(Map<String, String> params, AsyncCallback<JavaScriptObject> callback) {
    }

    /**
     * Returns a list of all of the visible notes written by the specified user.
     * If the user is logged out, only publicly viewable notes get returned.
     * 
     * For desktop applications, this call works only for the logged-in user,
     * since that's the only session you have. If you want data for other users,
     * make an FQL query (fql.query) on the note (FQL) table.
     * 
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/Notes.get">Notes.get</a>
     */
    public void notesGet(Long uid, AsyncCallback<List<Note>> callback) {
        String fql = "SELECT note_id,title,content,created_time,updated_time FROM note WHERE uid=" + uid;
        Json j = new Json ().put ( "query", fql );
        callMethodRetList ( "fql.query", j.getJavaScriptObject (), Note.class, callback );
    }

    /**
     * This method returns the same set of subelements, whether or not there are
     * outstanding notifications in any area. Note that if the unread subelement
     * value is 0 for any of the pokes or shares elements, the most_recent
     * element is also 0. Otherwise, the most_recent element contains an
     * identifier for the most recent notification of the enclosing type.
     * <p/>
     * If you are building an application that notifies users of new
     * messages/pokes/shares, we encourage you to use the following logic when
     * deciding whether to show a notification:
     * 
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/JS_API_M_FB.ApiClient.Notifications_get">Notifications_get</a>
     */
    @Deprecated
    public void notificationsGet(final AsyncCallback<List<NotificationRequest>> callback) {
        JSONObject p = getDefaultParams ();
        final NotificationRequest.NotificationType[] types = NotificationRequest.NotificationType.values ();

        final AsyncCallback<JavaScriptObject> internCallback = new AsyncCallback<JavaScriptObject> () {
            public void onFailure(Throwable caught) {
                callback.onFailure ( caught );
            }

            public void onSuccess(JavaScriptObject jso) {
                List<NotificationRequest> resultList = new ArrayList<NotificationRequest> ();
                JSONObject result = new JSONObject ( jso );
                for (NotificationRequest.NotificationType t : types) {
                    if (result.isObject ().get ( t.toString () ) != null) {
                        resultList.add ( new NotificationRequest ( t.toString (), result.isObject ().get ( t.toString () ) ) );
                    }
                }
                callback.onSuccess ( resultList );
            }
        };
        callMethod ( "notifications.get", p.getJavaScriptObject (), internCallback );
    }

    /**
     * This method gets all the current session user's notifications, as well as
     * data for the applications that generated those notifications. It is a
     * wrapper around the notification and application FQL tables; you can
     * achieve more fine-grained control by using those two FQL tables in
     * conjunction with the fql.multiquery API call.
     * 
     * @param start_time
     *            time Indicates the earliest time to return a notification.
     *            This equates to the updated_time field in the notification FQL
     *            table. If not specified, this call returns all available
     *            notifications.
     * @param include_read
     *            bool Indicates whether to include notifications that have
     *            already been read. By default, notifications a user has read
     *            are not included.
     */
    @Deprecated
    public void notificationsGetList(Long startTime, Boolean includeRead, final AsyncCallback<List<Notification>> callback) {

        Json j = new Json ().put ( "start_time", startTime ).put ( "include_read", includeRead );
        AsyncCallback<JavaScriptObject> internCallback = new AsyncCallback<JavaScriptObject> () {

            public void onFailure(Throwable caught) {
                callback.onFailure ( caught );
            }

            public void onSuccess(JavaScriptObject jso) {
                List<Notification> resultList = new ArrayList<Notification> ();
                JSONObject result = new JSONObject ( jso );
                JSONValue v = result.isObject ().get ( "notifications" );
                JSONArray a = v.isArray ();

                for (int i = 0; a != null && i < a.size (); i++) {
                    resultList.add ( new Notification ( a.get ( i ).isObject () ) );
                }
                callback.onSuccess ( resultList );
            }

        };
        callMethod ( "notifications.getList", j.getJavaScriptObject (), internCallback );
    }

    /**
     * Wraps the same method that takes a list of notification ids as parameter
     * 
     * @see #notificationsMarkRead(List, AsyncCallback)
     */
    @Deprecated
    public void notificationsMarkRead(Long notificationId, final AsyncCallback<Boolean> callback) {
        List<Long> ids = new ArrayList<Long> ();
        ids.add ( notificationId );
        notificationsMarkRead ( ids, callback );
    }

    /**
     * 
     * This method marks one or more notifications as read. You return the
     * notifications by calling notifications.getList or querying the
     * notification FQL table.
     * 
     * Applications must pass a valid session key, and can only mark the
     * notifications of the current session user.
     * 
     * @param notification_ids
     *            array The IDs of the notifications to mark as read, as
     *            retrieved via the notification FQL table or the
     *            notifications.getList API method. This is a comma-separated
     *            list.
     * 
     *            See
     *            http://wiki.developers.facebook.com/index.php/Notifications
     *            .markRead
     */
    @Deprecated
    public void notificationsMarkRead(List<Long> notificationIds, final AsyncCallback<Boolean> callback) {
        Json j = new Json ().put ( "notification_ids", notificationIds );
        callMethodRetBoolean ( "notifications.markRead", j.getJavaScriptObject (), callback );
    }

    /**
     * Wraps the same method but less parameters. 
     * 
     * @see #notificationsSend(List, String, NotificationType, AsyncCallback)
     */
    @Deprecated
    public void notificationsSend(Long uid, String notification, AsyncCallback<JavaScriptObject> callback) {
        List<Long> uids = new ArrayList<Long> ();
        uids.add ( uid );
        notificationsSend ( uids, notification, NotificationType.user_to_user, callback );
    }

    /**
     * Valid notificationTypes
     */
    @Deprecated
    public static enum NotificationType {
        user_to_user, app_to_user
    }

    /**
     * Sends a notification to a set of users. Notifications are items sent by
     * an application to a user's notifications page in response to some sort of
     * user activity within an application. You can also send messages to the
     * logged-in user's notifications (located on the right hand side of the
     * chat bar), as well as on their notifications page.
     * <p/>
     * Your application can send a number of notifications to a user in a day
     * based on a number of metrics (or buckets). To get this number, use
     * admin.getAllocation or check the Allocations tab on the Insights
     * dashboard for your application in the Facebook Developer application. If
     * the number of recipients exceeds the allocation limit, then the
     * notification gets sent to the first n recipients specified in to_ids (see
     * the Parameters table below), where n is the allocation limit.
     * <p/>
     * Notifications sent to the notifications page for non-application users
     * are subject to spam control. Read more information about how spamminess
     * is measured. Additionally, any notification that you send on behalf of a
     * user appears with that user's notifications as a "sent notification."
     * <p/>
     * 
     * @param toIds
     *            Comma-separated list of recipient IDs. These must be either
     *            friends of the logged-in user or people who have added your
     *            application. To send a notification to the current logged-in
     *            user without a name prepended to the message, set to_ids to
     *            the empty string. You should include no more than 50 user IDs
     *            the array, otherwise you run the risk of your call timing out
     *            during processing.
     * @param notification
     *            The content of the notification. The notification uses a
     *            stripped down version of FBML and HTML, allowing only text and
     *            links (see the list of allowed tags). The notification can
     *            contain up to 2,000 characters.
     * @param type
     *            string Specify whether the notification is a user_to_user one
     *            or an app_to_user. (Default value is user_to_user.)
     * 
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/JS_API_M_FB.ApiClient.Notifications_send">Notifications_send</a>
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/Notifications.send">Notifications.send</a>
     */
    @Deprecated
    public void notificationsSend(List<Long> toIds, String notification, NotificationType type, AsyncCallback<JavaScriptObject> callback) {
        Json j = new Json ().put ( "to_ids", toIds );
        j.put ( "notification", notification );
        j.put ( "type", type.toString () );
        callMethod ( "notifications.send", j.getJavaScriptObject (), callback );

    }
    
    public void notificationsSendEmail(Map<String, String> params, AsyncCallback<JavaScriptObject> callback) {
        // TODO Auto-generated method stub

    }

    public void pagesGetInfo(Map<String, String> params, AsyncCallback<JavaScriptObject> callback) {
        // TODO Auto-generated method stub

    }

    public void pagesIsAdmin(Map<String, String> params, AsyncCallback<JavaScriptObject> callback) {
        // TODO Auto-generated method stub

    }

    public void pagesIsAppAdded(Map<String, String> params, AsyncCallback<JavaScriptObject> callback) {
        // TODO Auto-generated method stub

    }

    public void pagesIsFan(Map<String, String> params, AsyncCallback<JavaScriptObject> callback) {
        // TODO Auto-generated method stub

    }

    public void photosAddTag(Map<String, String> params, AsyncCallback<JavaScriptObject> callback) {
        // TODO Auto-generated method stub

    }

    /**
     * Creates and returns a new album owned by the specified user or the
     * current session user. See photo uploads for a description of the upload
     * workflow. The only storable values returned from this call are aid and
     * owner. No relationships between them are storable.
     * <p/>
     * For Web applications, you must pass either the ID of the user on whose
     * behalf you're making this call or the session key for that user, but not
     * both. If you don't specify a user with the uid parameter, then that user
     * whose session it is will be the target of the call.
     * <p/>
     * However, if your application is a desktop application, you must pass a
     * valid session key for security reasons. Do not pass a uid parameter.
     * 
     * @param name
     *            string The album name.
     * @param location
     *            string The album location.
     * @param description
     *            string The album description.
     * @param visible
     *            string Visibility of the album. One of friends,
     *            friends-of-friends, networks, everyone.
     * 
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/JS_API_M_FB.ApiClient.Photos_createAlbum">Photos_createAlbum</a>
     */
    public void photosCreateAlbum(Album album, final AsyncCallback<Album> callback) {
        callMethodRetObject ( "photos.createAlbum", album, Album.class, callback );
    }

    /**
     * Get current users albums See #photos_getAlbums(Map, AsyncCallback)
     */
    public void photosGetAlbums(final AsyncCallback<List<Album>> callback) {
        photosGetAlbums ( null, null, callback );
    }

    /**
     * Returns metadata about all of the photo albums uploaded by the specified
     * user.
     * <p/>
     * This method returns information from all visible albums satisfying the
     * filters specified. The method can be used to return all photo albums
     * created by a user, query a specific set of albums by a list of aids, or
     * filter on any combination of these two.
     * <p/>
     * This call does return a user's profile picture album. However, you cannot
     * upload photos to this album using photos.upload. You can determine
     * whether an album is the profile album by comparing the album cover pid
     * with the user's profile picture pid. If they are the same pid, then
     * that's the profile picture album. Also, see the Notes below for another
     * way of returning the profile picture album.
     * <p/>
     * You cannot store the values returned from this call.
     * 
     * @param uid
     *            Return albums created by this user. You must specify either
     *            uid or aids. The uid parameter has no default value.
     * @param aids
     *            Return albums with aids in this list. This is a
     *            comma-separated list of aids. You must specify either uid or
     *            aids. The aids parameter has no default value.
     * 
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/JS_API_M_FB.ApiClient.Photos_getAlbums">Photos_getAlbums</a>
     */
    public void photosGetAlbums(Long uid, List<Long> aids, final AsyncCallback<List<Album>> callback) {
        Json j = new Json ().put ( "uid", uid ).put ( "aids", aids );
        callMethodRetList ( "photos.getAlbums", j.getJavaScriptObject (), Album.class, callback );
    }

    /**
     * Returns all visible photos of subject.
     * 
     * @see #photosGet(Long, Long, List, AsyncCallback)
     */
    public void photosGet(Long subjId, final AsyncCallback<List<Photo>> callback) {
        photosGet ( subjId, null, null, callback );
    }

    /**
     * Returns all visible photos according to the filters specified. You can
     * use this method to find all photos that are:
     * <p/>
     * Tagged with the specified subject (passing the user's uid as the subj_id)
     * Contained within the album specified by aid Included in the list of
     * photos specified by pids Any combination of these three criteria
     * 
     * @param subjId
     *            int Filter by photos tagged with this user. You must specify
     *            at least one of subj_id, aid or pids. The subj_id parameter
     *            has no default value, but if you pass one, it must be the
     *            user's user ID.
     * @param aid
     *            string Filter by photos in this album. You must specify at
     *            least one of subj_id, aid or pids. The aid parameter has no
     *            default value. The aid cannot be longer than 50 characters.
     * @param pids
     *            array Filter by photos in this list. This is a comma-separated
     *            list of pids. You must specify at least one of subj_id, aid or
     *            pids. The pids parameter has no default value.
     * 
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/JS_API_M_FB.ApiClient.Photos_get">Photos_get</a>
     */
    public void photosGet(Long subjId, Long aid, List<Long> pids, final AsyncCallback<List<Photo>> callback) {
        Json j = new Json ().put ( "subj_id", subjId ).put ( "aid", aid ).put ( "pids", pids );
        callMethodRetList ( "photos.get", j.getJavaScriptObject (), Photo.class, callback );
    }

    public void photosGetTags(Map<String, String> params, AsyncCallback<JavaScriptObject> callback) {
        Window.alert ( "not implemented" );
    }

    /**
     * Uploads a photo owned by the specified user or the current session user
     * and returns the new photo. See photo uploads for a description of the
     * upload workflow. The only storable values returned from this call are
     * pid, aid, and owner. All applications can upload photos with a "pending"
     * state, which means that the photos must be approved by the user before
     * they are visible on the site. Photos uploaded by applications with the
     * photo_upload extended permission are visible immediately.
     * 
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/Photos.upload">
     *      Photos.upload </a>
     * @param aid
     *            The album ID of the destination album. The aid cannot be
     *            longer than 50 characters.
     * @param caption
     *            The caption of the photo.
     * @param callback
     *            with photo created
     */
    public void photosUpload(String aid, String caption, AsyncCallback<Photo> callback) {
        Json j = new Json ();
        j.put ( "data", "asldkjasdlfkjasdflkjasd f sadlkjasdlfkj " );

        callMethodRetObject ( "photos.upload", j.getJavaScriptObject (), Photo.class, callback );
    }

    /*
     * public void profile_getFBML(Map<String, String> params,
     * AsyncCallback<JavaScriptObject> callback) { Window.alert (
     * "not implemented" ); }
     * 
     * public void profile_getInfo(Map<String, String> params,
     * AsyncCallback<JavaScriptObject> callback) { Window.alert (
     * "not implemented" ); }
     * 
     * public void profile_getInfoOptions(Map<String, String> params,
     * AsyncCallback<JavaScriptObject> callback) { Window.alert (
     * "not implemented" ); }
     * 
     * public void profile_setFBML(Map<String, String> params,
     * AsyncCallback<JavaScriptObject> callback) { Window.alert (
     * "not implemented" ); }
     * 
     * public void profile_setInfo(Map<String, String> params,
     * AsyncCallback<JavaScriptObject> callback) { Window.alert (
     * "not implemented" ); }
     * 
     * public void profile_setInfoOptions(Map<String, String> params,
     * AsyncCallback<JavaScriptObject> callback) { Window.alert (
     * "not implemented" ); }
     */

    /**
     * Valid permissions
     */
    public enum Permission {
        email, read_stream, publish_stream, offline_access, status_update, photo_upload, create_event, rsvp_event, sms, video_upload, create_note, share_item, read_mailbox
    };

    /**
     * <p/>
     * Checks whether the user has opted in to an extended application
     * permission.
     * <p/>
     * For non-desktop applications, you may pass the ID of the user on whose
     * behalf you're making this call. If you don't specify a user with the uid
     * parameter but you do specify a session_key, then that user whose session
     * it is will be the target of the call.
     * <p/>
     * However, if your application is a desktop application, you must pass a
     * valid session key for security reasons. Passing a uid parameter will
     * result in an error.
     * 
     * @param ext_perm
     *            string String identifier for the extended permission that is
     *            being checked for. Must be one of email, read_stream,
     *            publish_stream, offline_access, status_update, photo_upload,
     *            create_event, rsvp_event, sms, video_upload, create_note,
     *            share_item. optional
     * @param session_key
     *            string The session key of the user whose permissions you are
     *            checking. Note: A session key is always required for desktop
     *            applications. It is required for Web applications only when
     *            the uid is not specified.
     * @param uid
     *            int The user ID of the user whose permissions you are
     *            checking. If this parameter is not specified, then it defaults
     *            to the session user. Note: This parameter applies only to Web
     *            applications and is required by them only if the session_key
     *            is not specified. Facebook ignores this parameter if it is
     *            passed by a desktop application.
     * 
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/JS_API_M_FB.ApiClient.Users_hasAppPermission">Users_hasAppPermission</a>
     */
    public void usersHasAppPermission(Permission permission, final AsyncCallback<Boolean> callback) {
        GWT.log ( "users_hasAppPermission: " + permission.toString (), null );

        Json j = new Json ().put ( "ext_perm", permission.toString () );

        Callback<JavaScriptObject> nativeCallback = new Callback<JavaScriptObject> ( callback ) {
            public void onSuccess(JavaScriptObject jso) {
                callback.onSuccess ( "1".equals ( jso.toString () ) );
            }
        };
        callMethod ( "users.hasAppPermission", j.getJavaScriptObject (), nativeCallback );
    }

    /**
     * This method is used to determine whether a user has enabled SMS for the
     * application. Requires an active session, and is recommended only as a
     * basis for design in terms of disabling certain elements or options that
     * are conditional on this capability.
     * 
     * @param uid
     *            of user, defaults to current user
     * @param callback
     *            returns 0 on success, or an error code
     * 
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/Sms.canSend">Sms.canSend
     *      </a>
     */
    public void smsCanSend(Long uid, AsyncCallback<Integer> callback) {
        Json j = new Json ().put ( "uid", uid );
        callMethodRetInteger ( "sms.canSend", j.getJavaScriptObject (), callback );
    }

    public void smsSend(Map<String, String> params, AsyncCallback<JavaScriptObject> callback) {
        Window.alert ( "not implemented" );
    }

    /**
     * This method returns a list of Stream objects that contains the stream
     * from the perspective of a specific viewer -- a user or a Facebook Page.
     * 
     * The hashmap takes the following arguments:
     * 
     * @param viewer_id
     *            int The user ID for whom you are fetching stream data. You can
     *            pass 0 for this parameter to retrieve publicly viewable
     *            information. However, desktop applications must always specify
     *            a viewer as well as a session key. (Default value is the
     *            current session user.)
     * @param source_ids
     *            array An array containing all the stream data for the user
     *            profiles and Pages connected to the viewer_id. You can filter
     *            the stream to include posts by the IDs you specify here.
     *            (Default value is all connections of the viewer.)
     * @param start_time
     *            time The earliest time (in Unix time) for which to retrieve
     *            posts from the stream. The start_time uses the updated_time
     *            field in the stream (FQL) table as the baseline for
     *            determining the earliest time for which to get the stream.
     *            (Default value is 1 day ago.)
     * @param end_time
     *            time The latest time (in Unix time) for which to retrieve
     *            posts from the stream. The end_time uses the updated_time
     *            field in the stream (FQL) table as the baseline for
     *            determining the latest time for which to get the stream.
     *            (Default value is now.)
     * @param limit
     *            int A 32-bit int representing the total number of posts to
     *            return. (Default value is 30 posts.)
     * @param filter_key
     *            string A filter associated with the user. Filters get returned
     *            by stream.getFilters or the stream_filter FQL table. To filter
     *            for stream posts from your application, look for a filter with
     *            a filter_key set to app_YOUR_APPLICATION_ID.
     * @param metadata
     *            array A JSON-encoded array in which you can specify one or
     *            more of 'albums', 'profiles', and 'photo_tags' to request the
     *            user's aid, id (user ID or Page ID), and pid (respectively)
     *            when you call stream.get. All three parameters are optional.
     *            (Default value is false for all three keys.)
     * 
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/JS_API_M_FB.ApiClient.stream_get">stream_get</a>
     */
    public void streamGet(Long viewerId,
                          List<Long> sourceIds,
                          Long startTime,
                          Long endTime,
                          Integer limit,
                          String filterKey,
                          List<String> metadata,
                          final AsyncCallback<Stream> callback) {

        Json j = new Json ();
        j.put ( "viewer_id", viewerId ).
          put ( "source_ids", sourceIds ).
          put ( "start_time", startTime ).
          put ( "end_time", endTime ).
          put ( "filter_key", filterKey ).
          puts ( "metadata", metadata );
        
        callMethodRetObject ( "stream.get", j.getJavaScriptObject (), Stream.class, callback );
        
    }

    /**
     * Wraps the more complex method with less parameters.
     * 
     * @see #streamGet(Long, List, Long, Long, Integer, String, List,
     *      AsyncCallback)
     */
    public void streamGet(final AsyncCallback<Stream> callback) {
        
        callMethodRetObject ( "stream.get", getDefaultParams ().getJavaScriptObject (), Stream.class, callback );
    }

    /**
     * Wraps the more complex method. Filter stream by filter key.
     * 
     * @see #streamGetFilters(AsyncCallback)
     * @param filterKey
     *            to filter by
     * @param callback
     */
    public void streamGet(String filterKey, final AsyncCallback<Stream> callback) {
        streamGet ( null, null, null, null, null, filterKey, null, callback );
    }

    /**
     * Updates current user's status.
     * 
     * @see #statusSet(Long, String, AsyncCallback)
     */
    public void statusSet(String status, AsyncCallback<JavaScriptObject> callback) {
        statusSet ( null, status, callback );
    }

    /**
     * Updates a user's Facebook status through your application. Before your
     * application can set a user's status, the user must grant it the
     * status_update extended permission. This call is a streamlined version of
     * users.setStatus, as it takes fewer arguments.
     * <p/>
     * For Web applications, you must pass either the ID of the user on whose
     * behalf you're making this call or the session key for that user, but not
     * both. If you don't specify a user with the uid parameter, then that user
     * whose session it is will be the target of the call. To set the status of
     * a facebook Page, pass the uid of the Page.
     * <p/>
     * However, if your application is a desktop application, you must pass a
     * valid session key for security reasons. Do not pass a uid parameter.
     * 
     * @param status
     *            string The status message to set. Note: The maximum message
     *            length is 255 characters; messages longer than that limit will
     *            be truncated and appended with "...".
     * 
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/JS_API_M_FB.ApiClient.Users_setStatus">Users_setStatus</a>
     */
    public void statusSet(Long uid, String status, AsyncCallback<JavaScriptObject> callback) {
        Json j = new Json ().put ( "uid", uid ).put ( "status", status );
        callMethod ( "users.setStatus", j.getJavaScriptObject (), callback );
    }

    /**
     * Returns the user's current and most recent statuses.
     * 
     * For desktop applications, this call works only for the logged-in user,
     * since that's the only session you have. If you want data for other users,
     * make an FQL query (fql.query) on the status (FQL) table.
     * 
     *required
     * 
     * @param uid
     *            int The user ID of the user whose status messages you want to
     *            retrieve.
     * @param limit
     *            NOT SUPPORTED int The number of status messages you want to
     *            return. (Default value is 100.)
     * 
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/Status.get">Status.get</a>
     */
    public void statusGet(Long uid, Integer limit, AsyncCallback<JavaScriptObject> callback) {
        if (uid == null) {
            throw new IllegalArgumentException ( "status_get called without uid" );
        }

        /**
         * Cant get this to work, its in beta so wont use much time on it
         * JSONObject params = getDefaultParams (); params.put( "uid", new
         * JSONString ( uid ) ) ; callMethod ( "status.get",
         * params.getJavaScriptObject(), callback );
         */
        fqlQuery ( "SELECT message FROM status WHERE uid=" + uid + " LIMIT 1", callback );
    }

    /**
     * This method adds a comment to a post that was already published to a
     * user's Wall.
     * <p/>
     * Privacy rules apply to the posts to which the user can add comments; the
     * user must be able to see the post in order for your application to allow
     * the user add a comment to it.
     * <p/>
     * Desktop applications must pass a valid session key, and only the user
     * associated with that session key can add comments. Other applications can
     * allows users to add comments to any posts the user can see, provided you
     * have a valid post_id.
     * <p/>
     * In order for your application to allow a user to add a comment, that user
     * must grant your application the publish_stream extended permission.
     * 
     * @param post_id
     *            string The ID for the post to which you're adding the comment.
     * @param comment
     *            string The text of the comment. This is a plain text parameter
     *            only; you cannot format the comment with HTML or FBML.
     * 
     * @param params
     *            map
     * @param callback
     *            result
     * 
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/Stream.addComment">Stream.addComment</a>
     */
    public void streamAddComment(String postId, String comment, AsyncCallback<JavaScriptObject> callback) {
        Json j = new Json ().put ( "post_id", postId ).put ( "comment", comment );
        callMethod ( "stream.addComment", j.getJavaScriptObject (), callback );
    }

    /**
     * This method lets a user add a like to any post the user can see. A user
     * can like each post only once.
     * <p/>
     * Desktop applications must pass a valid session key, and only the user
     * associated with that session key can like the post. Otherwise, the
     * specified user can like the post.
     * <p/>
     * In order for your user to like a post, the user must grant your
     * application the publish_stream extended permission.
     * 
     * 
     * @param uid
     *            string The user ID of the user who likes the post. If this
     *            parameter is not specified, then it defaults to the session
     *            user. Note: This parameter applies only to Web applications
     *            and is required by them only if the session_key is not
     *            specified. Facebook ignores this parameter if it is passed by
     *            a desktop application.
     * 
     * @param post_id
     *            string The ID of the post. &lt;/p&gt; (non-Javadoc)
     * 
     * @params params map
     * @params callback result
     * 
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/Stream.addLike">Stream.addLike</a>
     */
    public void streamAddLike(String postId, AsyncCallback<JavaScriptObject> callback) {
        Json j = new Json ().put ( "post_id", postId );
        callMethod ( "stream.addLike", j.getJavaScriptObject (), callback );
    }

    /**
     * This method removes a like a user added to a post.
     * <p/>
     * Desktop applications must pass a valid session key, and can remove likes
     * made by the user associated with that session key only. Other
     * applications can remove any likes, provided you have a valid post_id.
     * <p/>
     * In order to remove a Like from a post, the user must grant your
     * application the publish_stream extended permission.
     * 
     * @param post_id
     *            string The ID of the post.
     * 
     * @param callback
     *            result
     * 
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/Stream.removeLike">Stream.removeLike</a>
     */
    public void streamRemoveLike(String postId, AsyncCallback<JavaScriptObject> callback) {
        Json j = new Json ().put ( "post_id", postId );
        callMethod ( "stream.removeLike", j.getJavaScriptObject (), callback );
    }

    /**
     * This method returns all comments associated with a post in a user's
     * stream. This method returns comments only if the user who owns the post
     * (that is, the user published the post to his or her profile) has
     * authorized your application.
     * <p/>
     * This method is a wrapper for comment FQL table, indexed by post_id rather
     * than xid. param post_id string The ID for the post for which you're
     * retrieving the comments.
     * 
     * @param params
     *            map
     * @param callback
     *            result
     * 
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/Stream.getComments">Stream.getComments</a>
     */
    public void streamGetComments(String postId, final AsyncCallback<List<Comment>> callback) {
        Json j = new Json ().put ( "post_id", postId );
        callMethodRetList ( "stream.getComments", j.getJavaScriptObject (), Comment.class, callback );
    }

    /**
     * This method returns any filters associated with a user's home page
     * stream. You can use these filters to narrow down a stream you want to
     * return with stream.get or when you query the stream FQL table.
     * 
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/JS_API_M_FB.ApiClient.stream_getFilters">stream.getFilters</a>
     */
    public void streamGetFilters(final AsyncCallback<List<StreamFilter>> callback) {
        JavaScriptObject p = getDefaultParams ().getJavaScriptObject ();
        callMethodRetList ( "stream.getFilters", p, StreamFilter.class, callback );
    }

    /**
     * This method publishes a post into the stream on the Wall of a user or a
     * Facebook Page, group or event connected to the user. By default, this
     * call publishes to the current session user's Wall, but if you specify a
     * user ID, Facebook Page ID, group ID, or event ID as the target_id, then
     * the post appears on the Wall of the target, and not the user posting the
     * item.
     * 
     * The post also appears in the streams (News Feeds) of any user connected
     * to the target of the post.
     * 
     * This method works in two ways. You can use it to publish:
     * 
     * As a Feed form, which is the default behavior for this method. Keep the
     * auto_publish parameter set to the default, false, so the Feed form
     * appears. You do not need the publish_stream permission, nor does the user
     * have to be connected to your site, to publish in this manner. If the user
     * isn't logged in to Facebook when you make this call, a login dialog
     * appears, followed by a dialog with the post data. Your users can add
     * their own message to the post.
     * 
     * Directly to a user's or Page's stream, without prompting the user. Before
     * your application can publish directly to the stream, the user or Page
     * must grant your application the publish_stream extended permission. If
     * the user previously granted your application the permission to publish
     * short stories into the News Feed automatically, then you don't need to
     * prompt for this permission in order to call this method. Make sure you
     * set the auto_publish parameter to true.
     * 
     * This method takes similar parameters to stream.publish. The main
     * difference between calling this function and calling stream.publish is
     * that if the user hasn't granted your application the publish_stream
     * extended permission, or if the auto_publish parameter is set to false
     * (the default), a Feed form appears, asking the user to confirm the post
     * before publishing.
     * 
     * To provide rich content like MP3 audio, Flash, or an image, you can
     * supply a predefined object called attachment. Facebook formats the
     * attachment into the post. The attachment is described in Attachment
     * (Streams).
     * 
     * Its recommended to use the FacebookConnect method. It will automatically
     * prompt the user for permission.
     * 
     * @see {@link FacebookConnect#streamPublish(String, Attachment, List, String, String, Boolean, String, AsyncCallback)}
     * @param userMessage
     *            The message the user enters for the post at the time of
     *            publication. Although this can be used to set a default for
     *            the stream publish form user message text field, this is
     *            against policy. user_message should only be set to a
     *            user-entered message (for example, a comment or text the user
     *            entered into an earlier form with the understanding that it
     *            would be published here)
     * 
     * @param attachment
     *            A dictionary object containing the text of the post, relevant
     *            links, a media type (image, video, mp3, flash), as well as any
     *            other key/value pairs you may want to add. See Attachment
     *            (Streams) for more details. Note: If you want to use this call
     *            to update a user's status, don't pass an attachment; the
     *            content of the user_message parameter will become the user's
     *            new status and will appear at the top of the user's profile.
     * @param actionLinks
     *            A dictionary of action link objects, containing the link text
     *            and a hyperlink.
     * @param userMessagePrompt
     *            Text you provide the user as a prompt to specify a
     *            user_message. This appears above the box where the user enters
     *            a custom message. For example, "What's on your mind?"
     * @param autoPublish
     *            Indicates whether to automatically publish to the user's
     *            stream without displaying a Feed form to the user. If the user
     *            has granted your application the publish_stream extended
     *            permission and this parameter is set to true, the post is
     *            published automatically. (Default value is false.)
     * @param actorId
     *            Allows the logged in user to publish on a Facebook Page's
     *            behalf if the user is an admin of the Page. If specified,
     *            actor_id indicates the ID of the Page that will publish the
     *            post. If the user publishes the post, the post will appear on
     *            the Page's Wall as if the Page has posted it. (Default value
     *            is null.)
     * 
     * @param showDialog
     *            true to show dialog to the user.
     * @param callback
     */
    public void streamPublish(String userMessage,
                              Attachment attachment,
                              List<ActionLink> actionLinks,
                              String targetId,
                              String userMessagePrompt,
                              Boolean autoPublish,
                              String actorId,
                              boolean showDialog,
                              AsyncCallback<JavaScriptObject> callback) {

        if (showDialog) {
            FacebookConnect.streamPublish ( userMessage, attachment, actionLinks, targetId, userMessagePrompt, autoPublish, actorId, callback );
        } else {
            Json j = new Json ();
            j.put ( "user_message", userMessage );
            j.put ( "attachment", attachment );
            j.putlist ( "action_links", actionLinks );
            j.put ( "user_message_prompt", userMessagePrompt );
            j.put ( "auto_publish", autoPublish );
            j.put ( "actor_id", actorId );
            j.put ( "target_id", targetId );
            callMethod ( "stream.publish", j.getJavaScriptObject (), callback );
        }
    }

    /**
     * This method removes a post from a user's Wall. The post also gets removed
     * from the user's and the user's friends' News Feeds.
     * <p/>
     * Your application may only remove posts that were created through it.
     * <p/>
     * Desktop applications must pass a valid session key, and can remove posts
     * only from the user associated with that session key. Other applications
     * can delete any post that they published, provided you have a valid
     * post_id. Web applications must pass either a valid session key or a user
     * ID.
     * <p/>
     * In order to remove a post from a user's Wall, the user must grant your
     * application the publish_stream extended permission.
     * 
     * Parameters Required Name Type Description optional
     * 
     * @param post_id
     *            string The ID for the post you want to remove.
     * 
     * 
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/Stream.remove">Stream.remove</a>
     */
    public void streamRemove(String postId, AsyncCallback<JavaScriptObject> callback) {
        Json j = new Json ().put ( "post_id", postId );
        callMethod ( "stream.remove", j.getJavaScriptObject (), callback );
    }

    /**
     * This method removes a comment from a post.
     * <p/>
     * Privacy rules apply to the posts from which the user can delete comments;
     * if the post is on the user's Wall, any comment can be deleted. If the
     * post is on a friend's Wall, only comments made by the user can be
     * deleted.
     * <p/>
     * Desktop applications must pass a valid session key, and can remove
     * comments made by the user associated with that session key only. Other
     * applications can delete any comments, provided you have a valid post_id.
     * <p/>
     * In order to remove a comment, the user must grant your application the
     * publish_stream extended permission.
     * 
     * @param comment
     *            string The ID for the comment you want to remove.
     * 
     * @see <a
     *      href="http://wiki.developers.facebook.com/index.php/Stream.removeComment">Stream.removeComment</a>
     */
    public void streamRemoveComment(final String commentId, final AsyncCallback<JavaScriptObject> callback) {
        Json j = new Json ().put ( "comment_id", commentId );
        callMethod ( "stream.removeComment", j.getJavaScriptObject (), callback );
    }

    /**
     * Returns a wide array of user-specific information for each user
     * identifier passed, limited by the view of the current user. The current
     * user is determined from the session_key parameter. The only storable
     * values returned from this call are those under the affiliations element,
     * the notes_count value, the proxied_email address, and the contents of the
     * profile_update_time element.
     * <p/>
     * Use this call to get user data that you intend to display to other users
     * (of your application, for example). If you need some basic information
     * about a user for analytics purposes, call users.getStandardInfo instead.
     * <p/>
     * This call no longer requires a session key. However, if you call this
     * method without a session key, you can only get the following information:
     * 
     * uid first_name last_name name locale current_location affiliations
     * (regional type only) pic_square profile_url sex
     * <p/>
     * You can call this method as soon as a user interacts with your
     * application, before she has authorized your application to access her
     * information. If you do so, you can get the same information as you can
     * without a session (see above). User Privacy and Visible Data
     * 
     * <p/>
     * Important: Depending upon the user's privacy settings (including whether
     * the user has decided to opt out of Platform completely), you may not see
     * certain user data. For any user submitted to this method, the following
     * user fields are visible to an application only if that user has
     * authorized that application:
     * 
     * meeting_for meeting_sex religion significant_other_id
     * 
     * <p/>
     * In addition, the visibility of all fields, with the exception of
     * affiliations, first_name, last_name, name, and uid may be restricted by
     * the user's Facebook privacy settings in relation to the calling user (the
     * user associated with the current session).
     * 
     * <p/>
     * If a field is not visible for either of the above reasons, then that
     * field's corresponding element will be empty with a nil attribute set, in
     * the following manner: <significant_other_id xsi:nil="true"/>
     * <p/>
     * Finally, a user profile query will not return any data if the user has
     * turned off access to Facebook Platform.
     * 
     * @params uids List of user IDs.
     * @param fields List of desired fields in return.
     * @param callback
     */
    public void usersGetInfo(List<Long> uids, List<String> fields, AsyncCallback<List<UserInfo>> callback) {
        
        Json j = new Json ();
        j.putAsCommaSep (  "uids",uids );
        j.putAsCommaSep ( "fields", fields );
        callMethodRetList ( "users.getInfo", j.getJavaScriptObject (), UserInfo.class, callback );
    }

    /**
     * Gets the user ID (uid) associated with the current session. This value
     * should be stored for the duration of the session, to avoid unnecessary
     * subsequent calls to this method.
     * 
     * @param params
     *            map
     * @param callback
     *            result
     */
    public void usersGetLoggedInUser(AsyncCallback<Long> callback) {
        JavaScriptObject p = getDefaultParams ().getJavaScriptObject ();
        callMethodRetLong ( "users.getLoggedInUser", p, callback );
    }

    /**
     * Returns an array of user-specific information...
     * 
     * @see #usersGetStandardInfo(List, AsyncCallback)
     * 
     * @param callback
     *            result
     */
    public void usersGetStandardInfo(AsyncCallback<UserStandardInfo> callback) {
        usersGetStandardInfo ( null, callback );
    }

    /**
     * Returns an array of user-specific information for use by the application
     * itself. Make this call on behalf of your application when you need
     * analytic information only. Don't display this information to any users.
     * If you need to display information to other users, call users.getInfo.
     * 
     * @{link 
     *        http://wiki.developers.facebook.com/index.php/Users.getStandardInfo
     *        }
     * 
     * @param params
     *            userFilter to limit fields returned. Default is all.
     * @param callback
     *            result
     */
    public void usersGetStandardInfo(List<String> filter, AsyncCallback<UserStandardInfo> callback) {
        Json j = new Json ().puts ( "filter", filter );
        callMethodRetObject ( "users.getStandardInfo", j.getJavaScriptObject (), UserStandardInfo.class, callback );
    }

    /*
     * public void usersIsAppUser(Map<String, String> params,
     * AsyncCallback<JavaScriptObject> callback) { Window.alert (
     * "not implemented" ); }
     * 
     * public void usersIsVerified(Map<String, String> params,
     * AsyncCallback<JavaScriptObject> callback) { Window.alert (
     * "not implemented" ); }
     * 
     * public void usersSetStatus(Map<String, String> params,
     * AsyncCallback<JavaScriptObject> callback) { Window.alert (
     * "not implemented" ); }
     * 
     * public void videoGetUploadLimits(Map<String, String> params,
     * AsyncCallback<JavaScriptObject> callback) { Window.alert (
     * "not implemented" ); }
     * 
     * public void videoUpload(Map<String, String> params,
     * AsyncCallback<JavaScriptObject> callback) { Ft (
     * "not implemented" ); }
     */
    /*
     * Another wrapper. Use this to get all parameters in one line.
     */
    private <T extends Enum<T>> JavaScriptObject getAllParams(final Enum<T>[] enumValues, final Map<Enum<T>, String> params) {

        JSONObject allParams = getDefaultParams ();
        copyAllParams ( allParams, enumValues, params );
        return allParams.getJavaScriptObject ();
    }

    /*
     * Get jsonobject with api_key parameter
     */
    private JSONObject getDefaultParams() {
        JSONObject obj = new JSONObject ();
        return obj;
    }

    /*
     * Copy a list of enum values to a json object
     */
    private <T extends Enum<T>> void copyAllParams(final JSONObject jo, final Enum<T>[] enumValues, final Map<Enum<T>, String> params) {

        if (params == null) {
            return;
        }
        for (Enum<?> e : enumValues) {
            String propValue = params.get ( e );

            if (propValue != null) {
                jo.put ( e.toString (), new JSONString ( propValue ) );
            }
        }
    }

    /*
     * Call method and parse response to Integer
     */
    private void callMethodRetInteger(final String method, final JavaScriptObject params, final AsyncCallback<Integer> callback) {

        callMethod ( method, params, new Callback<JavaScriptObject> ( callback ) {
            public void onSuccess(JavaScriptObject jso) {
                callback.onSuccess ( new Integer ( jso.toString () ) );
            }
        } );
    }

    /*
     * Call method and parse response to Integer
     */
    private void callMethodRetLong(final String method, final JavaScriptObject params, final AsyncCallback<Long> callback) {

        callMethod ( method, params, new Callback<JavaScriptObject> ( callback ) {
            public void onSuccess(JavaScriptObject jso) {
                callback.onSuccess ( new Long ( jso.toString () ) );
            }
        } );
    }

    /*
     * Call method and cast javascriptobject to a extending javascriptobject.
     */
    private <T extends JavaScriptObject> void callMethodRetObject(final String method,
                                                                  final JavaScriptObject params,
                                                                  final Class<T> entity,
                                                                  final AsyncCallback<T> callback) {

        callMethod ( method, params, new Callback<JavaScriptObject> ( callback ) {
            public void onSuccess(JavaScriptObject jso) {
                T entity = (T) jso.cast ();
                callback.onSuccess ( entity );
            }
        } );
    }

    /*
     * Method for calling method wich returns a boolean in the form "1" or "0",
     * or "true" or "false"
     */
    private void callMethodRetBoolean(final String method, final JavaScriptObject params, final AsyncCallback<Boolean> callback) {

        callMethod ( method, params, new Callback<JavaScriptObject> ( callback ) {

            public void onSuccess(JavaScriptObject response) {

                // Hackarond facebook bug, data.setCookie returns an empty
                // object, should return 0 or 1.
                if (new JSONObject ( response ).toString ().equals ( "{}" )) {
                    callback.onSuccess ( true );
                    return;
                }
                callback.onSuccess ( ("1".equals ( response.toString () ) || "true".equals ( response.toString () )) );
            }
        } );
    }

    /*
     * Convenient method and cast response to a list
     */
    @SuppressWarnings("unchecked")
    public void callMethodRetList(final String method, final JavaScriptObject params, final Class entity, final AsyncCallback callback) {

        callMethod ( method, params, new Callback<JavaScriptObject> ( callback ) {
            public void onSuccess(JavaScriptObject jso) {
                try {

                    if ("{}".equals ( new JSONObject ( jso ).toString ().trim () )) {
                        callback.onSuccess ( new ArrayList () );
                    } else {
                        callback.onSuccess ( cast ( entity, jso ) );
                    }
                } catch (Exception e) {
                }
            }
        } );
    }

    // Convenient method for casting a javascriptobject to a list.
    private <T extends JavaScriptObject> List<T> cast(Class<T> entity, JavaScriptObject jso) {
        if (jso == null) {
            return new ArrayList<T> ();
        }
        List<T> result = new ArrayList<T> ();

        JsArray<T> array = jso.cast ();

        for (int i = 0; i < array.length (); i++) {
            result.add ( array.get ( i ) );
        }
        return result;
    }

    /*
     * Call Facebook method and execute callback method. This methods needs to
     * check the response from facebook. Sometimes facebook returns object,
     * sometimes primitivies. If a primitive is returned, wrap it in a new
     * object so it can be converted to JavaScriptObject.Any object that extends
     * JavaScriptObject can be passed directly to this function.
     */
    private native void callMethod(final String method, final JavaScriptObject params, final AsyncCallback<JavaScriptObject> callback) /*-{
        var app=this;
        $wnd.FB_RequireFeatures(["Api"], function(){			
        	$wnd.FB.Facebook.apiClient.callMethod( method, params, 
        		function(result, exception){
        		    var jso=null;        		    
                    
        		    if ( result == undefined || result == null ) {
        		        var tmp=null;
        		        if ( typeof ( exception ) == 'object' ) {
        		            tmp=exception;
        		         } else {
                             tmp = { "message": exception };
        		         } 
                        app.@com.gwittit.client.facebook.FacebookApi::callbackError(Lcom/google/gwt/user/client/rpc/AsyncCallback;Lcom/google/gwt/core/client/JavaScriptObject;)(callback,tmp);
        		    } else {
            		    if ( typeof ( result ) == 'object' ) {
            		        jso = result;
            		    } else if ( typeof ( result ) == 'string' ) {
            		        jso = new String ( result );
            		    } else if ( typeof ( result ) == 'number' ) {
            		        jso = new Number ( result );
            		    } 
                        app.@com.gwittit.client.facebook.FacebookApi::callbackSuccess(Lcom/google/gwt/user/client/rpc/AsyncCallback;Lcom/google/gwt/core/client/JavaScriptObject;)(callback,jso);
        		    }
        		}
        	);
        });
    }-*/;

    /*
     * Callback
     */
    protected void callbackError(AsyncCallback<JSONValue> callback, JavaScriptObject jso) {
        ErrorResponse er = jso.cast ();
        callback.onFailure ( new FacebookException ( er ) );
    }

    /*
     * Called when method succeeded.
     */
    protected void callbackSuccess(AsyncCallback<JavaScriptObject> callback, JavaScriptObject obj) {
        GWT.log ( "FacebookApi: callbackSuccess " + new JSONObject ( obj ), null );
        callback.onSuccess ( obj );
    }


}
