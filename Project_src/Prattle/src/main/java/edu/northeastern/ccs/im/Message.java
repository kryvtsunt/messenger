package edu.northeastern.ccs.im;


/**
 * Each instance of this class represents a single transmission by our IM
 * clients.
 * <p>
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-sa/4.0/. It is based on work
 * originally written by Matthew Hertz and has been adapted for use in a class
 * assignment at Northeastern University.
 *
 * @version 1.3
 */
public class Message {
    /**
     * List of the different possible message types.
     */
    protected enum MessageType {
        /**
         * Message sent by the user attempting to login using a specified username.
         */
        HELLO("HLO"),

        HELP("HLP"),
        /**
         * Message sent by the server acknowledging a successful log in.
         */
        ACKNOWLEDGE("ACK"),
        /**
         * Message sent by the server rejecting a login attempt.
         */
        NO_ACKNOWLEDGE("NAK"),
        /**
         * Message sent by the user to start the logging out process and sent by the
         * server once the logout process completes.
         */
        QUIT("BYE"),
        /**
         * Message whose contents is broadcast to all connected users.
         */
        BROADCAST("BCT"),

        /**
         * Message whose contents is directed to specific user.
         */
        DIRECT("DIR"),

        /**
         * Message for groups
         */
        GROUP("GRP"),

        /**
         * Message for retreival operations
         */
        RETRIEVE("RET"),

        /**
         * Message for updating a user or a group
         */
        UPDATE("UPD"),

        /**
         * Message to delete a user or a group
         */
        DELETE("DEL"),

        /**
         * Message from a user to join a group
         */
        JOIN("JIN"),

        /**
         * Message from the user to leave a group
         */
        LEAVE("LVE"),
        /**
         * Wiretap request
         */
        WIRETAPU("WTU"),
        WIRETAPG("WTG"),
        /**
         * Wiretap approve
         */
        APPROVE("APR"),

        REJECT("RJT"),

        SIGNIN("SIN"),
        SIGNUP("SUP"),
        /**
         * Set the role
         */
        ROLE("RLE"),
        /**
         * Message from user to recall last message
         */
        RECALL("RCL"),

        PCONTROL("PCL"),

        LOGGER("LOG");


        /**
         * Store the short name of this message type.
         */
        private String tla;

        /**
         * Define the message type and specify its short name.
         *
         * @param abbrev Short name of this message type, as a String.
         */
        private MessageType(String abbrev) {
            tla = abbrev;
        }

        /**
         * Return a representation of this Message as a String.
         *
         * @return Three letter abbreviation for this type of message.
         */
        @Override
        public String toString() {
            return tla;
        }
    }

    /**
     * The string sent when a field is null.
     */
    private static final String NULL_OUTPUT = "--";


    /**
     * The handle of the message.
     */
    private MessageType msgType;

    /**
     * The first argument used in the message. This will be the sender's identi fier.
     */
    private String msgSender;

    /**
     * The second argument used in the message. This will be the receiver's identifier.
     */
    private String msgReceiver;

    /**
     * The second argument used in the message.
     */
    private String msgText;

    /**
     * Create a new message that contains actual IM text. The type of distribution
     * is defined by the handle and we must also set the name of the message sender,
     * message recipient, and the text to send.
     *
     * @param handle  Handle for the type of message being created.
     * @param srcName Name of the individual sending this message
     * @param text    Text of the instant message
     */
    private Message(MessageType handle, String srcName, String dstName, String text) {
        msgType = handle;
        // Save the properly formatted identifier for the user sending the
        // message.
        msgSender = srcName;

        msgReceiver = dstName;
        // Save the text of the message.
        msgText = text;
    }

    /**
     * Create simple command type message that does not include any data.
     *
     * @param handle Handle for the type of message being created.
     */
    private Message(MessageType handle) {
        this(handle, null, null, null);
    }


    /**
     * Create a new message that contains a command sent the server that requires a
     * single argument. This message contains the given handle and the single
     * argument.
     *
     * @param handle  Handle for the type of message being created.
     * @param srcName Argument for the message; at present this is the name used to
     *                log-in to the IM server.
     */
    private Message(MessageType handle, String srcName) {
        this(handle, srcName, null, null);
    }

    /**
     * Create a new message to continue the logout process.
     *
     * @return Instance of Message that specifies the process is logging out.
     */
    public static Message makeQuitMessage(String myName) {
        return new Message(MessageType.QUIT, myName, null, null);
    }

    /**
     * Create a new message broadcasting an announcement to the world.
     *
     * @param myName Name of the sender of this very important missive.
     * @param text   Text of the message that will be sent to all users
     * @return Instance of Message that transmits text to all logged in users.
     */
    public static Message makeBroadcastMessage(String myName, String text) {
        return new Message(MessageType.BROADCAST, myName, null, text);
    }

    public static Message makeSigninMessage(String myName, String text) {
        return new Message(MessageType.SIGNIN, myName, null, text);
    }

    public static Message makeSignupMessage(String myName, String text) {
        return new Message(MessageType.SIGNUP, myName, null, text);
    }

    public static Message makeLoggerMessage(String myName) {
        return new Message(MessageType.LOGGER, myName, null, null);
    }

    public static Message makePControlMessage(String myName, String directTo) {
        return new Message(MessageType.PCONTROL, myName, directTo, null);
    }

    public static Message makeHelpMessage(String myName) {
        return new Message(MessageType.HELP, myName, null, null);
    }

    /**
     * Create a new message to continue the recall process.
     *
     * @return Instance of Message that specifies the process is of recalling a message.
     */
    public static Message makeRecallMessage(String myName, String text) {
        return new Message(MessageType.RECALL, myName, null, text);
    }

    /**
     * Create a new message directed to a certain user.
     *
     * @param myName   Name of the sender of this very important missive.
     * @param directTo Name of the destination user
     * @param text     Text of the message that will be sent to all users
     * @return Instance of Message that transmits text to all logged in users.
     */
    public static Message makeDirectMessage(String myName, String directTo, String text) {
        return new Message(MessageType.DIRECT, myName, directTo, text);
    }

    /**
     * create a messgae when the user role is set
     * @param myName Name of the sender
     * @param directTo Name of the destination user
     * @param text Text of the message being sent
     * @return Instance of the message sent that is transmitted to the user whose role is being set
     */
    public static Message makeSetRoleMessage(String myName, String directTo, String text) {
        return new Message(MessageType.ROLE, myName, directTo, text);
    }

    /**
     * Create a wiretap message
     * @param myName Name of the sender
     * @param directTo Name of the destination user
     * @param text Text of the message being sent
     * @return Instance of the message sent to the agency
     */
    public static Message makeWiretapUserMessage(String myName, String directTo, String text) {
        return new Message(MessageType.WIRETAPU, myName, directTo, text);
    }


    /**
     * Create a wiretap message
     * @param myName Name of the sender
     * @param directTo Name of the destination group
     * @param text Text of the message being sent
     * @return Instance of the message sent to the agency
     */
    public static Message makeWiretapGroupMessage(String myName, String directTo, String text) {
        return new Message(MessageType.WIRETAPG, myName, directTo, text);
    }

    /**
     * Create a wiretap approval message
     * @param myName Name of the sender
     * @param directTo Name of the destination user
     * @param text Text of the message being sent
     * @return Instance of the message sent to the agency who is granted the permission to wiretap users
     */
    public static Message makeWiretapApproveMessage(String myName, String directTo, String text) {
        return new Message(MessageType.APPROVE, myName, directTo, text);
    }


    /**
     * Create a wiretap rejection message
     * @param myName Name of the sender
     * @param directTo Name of the destination user
     * @param text Text of the message being sent
     * @return Instance of the message sent to the agency who is denied the permission to wiretap users
     */
    public static Message makeWiretapRejectMessage(String myName, String directTo, String text) {
        return new Message(MessageType.REJECT, myName, directTo, text);
    }


    /**
     * Create a new group message
     *
     * @param myName   Name of the sender of this very important missive.
     * @param directTo Name of the destination user
     * @param text     Text of the message that will be sent to all users in a group
     * @return Instance of Message that transmits text to all logged in users.
     */
    public static Message makeGroupMessage(String myName, String directTo, String text) {
        return new Message(MessageType.GROUP, myName, directTo, text);
    }


    /**
     * Message while performing retrieval operations
     *
     * @param myName Name of the sender of this very important missive.
     * @param text   Text of the message that will be sent to that particular user
     * @return Instance of Message that transmits to the user who requested a retrieval operation
     */
    public static Message makeRetrieveMessage(String myName, String text) {
        return new Message(MessageType.RETRIEVE, myName, null, text);
    }

    /**
     * Create a new message to delete current user.
     *
     * @param myName Name of the sender of this very important missive.
     * @param text   Text of the message that will be sent to that particular user
     * @return Instance of Message that transmits text to user who requested a deletion.
     */
    public static Message makeDeleteMessage(String myName, String text) {
        return new Message(MessageType.DELETE, myName, null, text);
    }

    /**
     * Create a message to join a group.
     *
     * @param myName Name of the sender of this very important missive.
     * @param text   Group to join
     * @return Instance of Message that transmits text to user who joined a group.
     */
    public static Message makeJoinMessage(String myName, String text) {
        return new Message(MessageType.JOIN, myName, null, text);
    }

    /**
     * Create a message to leave a group.
     *
     * @param myName Name of the sender of this very important missive.
     * @param text   Group to leave
     * @return Instance of Message that transmits text to user who left
     */
    public static Message makeLeaveMessage(String myName, String text) {
        return new Message(MessageType.LEAVE, myName, null, text);
    }


    /**
     * Create a message to update
     *
     * @param myName Name of the sender of this very important missive.
     * @param text   Update message
     * @return Instance of Message that transmits text to user who updated their details.
     */
    public static Message makeUpdateMessage(String myName, String text) {
        return new Message(MessageType.UPDATE, myName, null, text);
    }

    /**
     * Create a new message stating the name with which the user would like to
     * login.
     *
     * @param text Name the user wishes to use as their screen name.
     * @return Instance of Message that can be sent to the server to try and login.
     */
    protected static Message makeHelloMessage(String text) {
        return new Message(MessageType.HELLO, null, null, text);
    }

    /**
     * Given a handle, name and text, return the appropriate message instance or an
     * instance from a subclass of message.
     *
     * @param handle  Handle of the message to be generated.
     * @param srcName Name of the originator of the message (may be null)
     * @param text    Text sent in this message (may be null)
     * @return Instance of Message (or its subclasses) representing the handle,
     * name, & text.
     */
    @SuppressWarnings("all")
    protected static Message makeMessage(String handle, String srcName, String dstName, String text) {
        Message result = null;
        if (handle.equals(MessageType.QUIT.toString())) result = makeQuitMessage(srcName);
        else if (handle.equals(MessageType.HELLO.toString())) result = makeLoginMessage(srcName);
        else if (handle.equals(MessageType.BROADCAST.toString())) result = makeBroadcastMessage(srcName, text);
        else if (handle.equals(MessageType.ACKNOWLEDGE.toString())) result = makeAcknowledgeMessage(srcName);
        else if (handle.equals(MessageType.NO_ACKNOWLEDGE.toString())) result = makeNoAcknowledgeMessage();
        else if (handle.equals(MessageType.DIRECT.toString())) result = makeDirectMessage(srcName, dstName, text);
        else if (handle.equals(MessageType.GROUP.toString())) result = makeGroupMessage(srcName, dstName, text);
        else if (handle.equals(MessageType.RETRIEVE.toString())) result = makeRetrieveMessage(srcName, text);
        else if (handle.equals(MessageType.DELETE.toString())) result = makeDeleteMessage(srcName, text);
        else if (handle.equals(MessageType.UPDATE.toString())) result = makeUpdateMessage(srcName, text);
        else if (handle.equals(MessageType.JOIN.toString())) result = makeJoinMessage(srcName, text);
        else if (handle.equals(MessageType.LEAVE.toString())) result = makeLeaveMessage(srcName, text);
        else if (handle.equals(MessageType.ROLE.toString())) result = makeSetRoleMessage(srcName, dstName, text);
        else if (handle.equals(MessageType.WIRETAPU.toString())) result = makeWiretapUserMessage(srcName, dstName, text);
        else if (handle.equals(MessageType.WIRETAPG.toString())) result = makeWiretapGroupMessage(srcName, dstName, text);
        else if (handle.equals(MessageType.APPROVE.toString())) result = makeWiretapApproveMessage(srcName, dstName, text);
        else if (handle.equals(MessageType.REJECT.toString())) result = makeWiretapRejectMessage(srcName, dstName, text);
        else if (handle.equals(MessageType.RECALL.toString())) result = makeRecallMessage(srcName, text);
        else if (handle.equals(MessageType.LOGGER.toString())) result = makeLoggerMessage(srcName);
        else if (handle.equals(MessageType.PCONTROL.toString())) result = makePControlMessage(srcName, dstName);
        else if (handle.equals(MessageType.SIGNIN.toString())) result = makeSigninMessage(srcName, text);
        else if (handle.equals(MessageType.SIGNUP.toString())) result = makeSignupMessage(srcName, text);
        else if (handle.equals(MessageType.HELP.toString())) result = makeHelpMessage(srcName);
        return result;
    }

    /**
     * Create a new message to reject the bad login attempt.
     *
     * @return Instance of Message that rejects the bad login attempt.
     */
    public static Message makeNoAcknowledgeMessage() {
        return new Message(MessageType.NO_ACKNOWLEDGE);
    }

    /**
     * Create a new message to acknowledge that the user successfully logged as the
     * name <code>srcName</code>.
     *
     * @param srcName Name the user was able to use to log in.
     * @return Instance of Message that acknowledges the successful login.
     */
    public static Message makeAcknowledgeMessage(String srcName) {
        return new Message(MessageType.ACKNOWLEDGE, srcName);
    }

    /**
     * Create a new message for the early stages when the user logs in without all
     * the special stuff.
     *
     * @param myName Name of the user who has just logged in.
     * @return Instance of Message specifying a new friend has just logged in.
     */
    public static Message makeLoginMessage(String myName) {
        return new Message(MessageType.HELLO, myName);
    }

    /**
     * Return the type of this message.
     *
     * @return MessageType for this message.
     */
    public MessageType getType() {
        return msgType;
    }

    /**
     * Return the name of the sender of this message.
     *
     * @return String specifying the name of the message originator.
     */
    public String getSender() {
        return msgSender;
    }

    /**
     * Return the name of the receiver of this message.
     *
     * @return String specifying the name of the message originator.
     */
    public String getReceiver() {
        return msgReceiver;
    }

    /**
     * Return the text of this message.
     *
     * @return String equal to the text sent by this message.
     */
    public String getText() {
        return msgText;
    }

    /**
     * sets the parent control to a particular user
     */
    public void controlText() {
        this.msgText = ParentControl.getInstance().filterBadWords(msgText);
    }


    /**
     * Sets the text for wire tap
     * @param text the text appended to the wireTap message
     */
    public void setText(String text) {
        msgText = text;
    }

    /**
     * Determine if this message is an acknowledgement message.
     *
     * @return True if the message is an acknowledgement message; false otherwise.
     */
    public boolean isAcknowledge() {
        return (msgType == MessageType.ACKNOWLEDGE);
    }

    /**
     * Determine if this message is broadcasting text to everyone.
     *
     * @return True if the message is a broadcast message; false otherwise.
     */
    public boolean isBroadcastMessage() {
        return (msgType == MessageType.BROADCAST);
    }

    /**
     * Determine if this message is directing text to specific users.
     *
     * @return True if the message is a broadcast message; false otherwise.
     */
    public boolean isDirectMessage() {
        return (msgType == MessageType.DIRECT);
    }



    /**
     * Determine if this message is a sign in message.
     *
     * @return True if the message is a signin message; false otherwise.
     */
    public boolean isSigninMessage() {
        return (msgType == MessageType.SIGNIN);
    }

    /**
     * Determine if this message is a sign up message.
     *
     * @return True if the message is a sign up message; false otherwise.
     */
    public boolean isSignupMessage() {
        return (msgType == MessageType.SIGNUP);
    }


    /**
     * Determine if this message is directing text to specific users.
     *
     * @return True if the message is a broadcast message; false otherwise.
     */
    public boolean isUpdateMessage() {
        return (msgType == MessageType.UPDATE);
    }


    /**
     * Determine if this message is directing text to specific users.
     *
     * @return True if the message is a broadcast message; false otherwise.
     */
    public boolean isGroupMessage() {
        return (msgType == MessageType.GROUP);
    }


    /**
     * Determine if this message is directing text to specific users.
     *
     * @return True if the message is a broadcast message; false otherwise.
     */
    public boolean isRetrieveMessage() {
        return (msgType == MessageType.RETRIEVE);
    }

    /**
     * Determine if this message is directing text to specific users.
     *
     * @return True if the message is a broadcast message; false otherwise.
     */
    public boolean isJoinMessage() {
        return (msgType == MessageType.JOIN);
    }

    /**
     * Determine if this message is directing text to specific users.
     *
     * @return True if the message is a broadcast message; false otherwise.
     */
    public boolean isLeaveMessage() {
        return (msgType == MessageType.LEAVE);
    }

    /**
     * Determine if this message is directing text to specific users.
     *
     * @return True if the message is a broadcast message; false otherwise.
     */
    public boolean isDeleteMessage() {
        return (msgType == MessageType.DELETE);
    }

    /**
     * Determine if this message is a logger message.
     *
     * @return True if the message is a logger message; false otherwise.
     */
    public boolean isLoggerMessage() {
        return (msgType == MessageType.LOGGER);
    }

    /**
     * Determine if this message is parent control text.
     *
     * @return True if the message is a parent control message; false otherwise.
     */
    public boolean isPControlMessage() {
        return (msgType == MessageType.PCONTROL);
    }

    /**
     * Determine if this message contains text which the recipient should display.
     *
     * @return True if the message is an actual instant message; false if the
     * message contains data
     */
    public boolean isDisplayMessage() {
        return (msgType == MessageType.BROADCAST);
    }

    /**
     * Determine if this message is a recall request by client.
     *
     * @return True if the message is a recall message; false otherwise
     */
    public boolean isRecallMessage() {
        return (msgType == MessageType.RECALL);
    }

    /**
     * Determine if this message is a wireTap message for user.
     *
     * @return True if the message is a wiretap message for user; false otherwise
     */
    public boolean isWiretapUserMessage() {
        return (msgType == MessageType.WIRETAPU);
    }

    /**
     * Determine if this message is a wireTap message for user.
     *
     * @return True if the message is a wiretap message for user; false otherwise
     */
    public boolean isWiretapGroupMessage() {
        return (msgType == MessageType.WIRETAPG);
    }


    /**
     * Determine if this message is an approved wireTap message for user.
     *
     * @return True if the message is a approved wiretap message for user; false otherwise
     */
    public boolean isApproveMessage() {
        return (msgType == MessageType.APPROVE);
    }


    /**
     * Determine if this message is an rejected wireTap message for user.
     *
     * @return True if the message is a rejected wiretap message for user; false otherwise
     */
    public boolean isRejectMessage() {
        return (msgType == MessageType.REJECT);
    }


    /**
     * Determine if this message is an approved wireTap message for user.
     *
     * @return True if the message is a approved wiretap message for user; false otherwise
     */
    public boolean isSetRoleMessage() {
        return (msgType == MessageType.ROLE);
    }


    /**
     * Determine if this message is to set the role of a user.
     *
     * @return True if the message is for modifying the user role; false otherwise
     */
    public boolean isHelpMessage() {
        return (msgType == MessageType.HELP);
    }


    /**
     * Determine if this message is sent by a new client to log-in to the server.
     *
     * @return True if the message is an initialization message; false otherwise
     */
    public boolean isInitialization() {
        return (msgType == MessageType.HELLO);
    }

    /**
     * Determine if this message is a message signing off from the IM server.
     *
     * @return True if the message is sent when signing off; false otherwise
     */
    public boolean terminate() {
        return (msgType == MessageType.QUIT);
    }

    /**
     * Representation of this message as a String. This begins with the message
     * handle and then contains the length (as an integer) and the value of the next
     * two arguments.
     *
     * @return Representation of this message as a String.
     */
    @Override
    public String toString() {
        String result = msgType.toString();
        if (msgSender != null) {
            result += " " + msgSender.length() + " " + msgSender;
        } else {
            result += " " + NULL_OUTPUT.length() + " " + NULL_OUTPUT;
        }
        if (msgReceiver != null) {
            result += " " + msgReceiver.length() + " " + msgReceiver;
        } else {
            result += " " + NULL_OUTPUT.length() + " " + NULL_OUTPUT;
        }
        if (msgText != null) {
            result += " " + msgText.length() + " " + msgText;
        } else {
            result += " " + NULL_OUTPUT.length() + " " + NULL_OUTPUT;
        }
        return result;
    }
}