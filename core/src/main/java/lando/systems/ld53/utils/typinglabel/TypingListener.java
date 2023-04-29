package lando.systems.ld53.utils.typinglabel;

public interface TypingListener {

    /**
     * Called each time an {@code EVENT} token is processed.
     *
     * @param event Name of the event specified in the token. e.g. <tt>{EVENT=player_name}</tt> will have
     *              <tt>player_name</tt> as argument.
     */
    public void event(String event);

    /** Called when the char progression reaches the end. */
    public void end();

    /**
     * Called when variable tokens are replaced in text. This is an alternative method to deal with variables, other
     * than directly assigning replacement values to the label. Replacements returned by this method have priority over
     * direct values, unless {@code null} is returned.
     *
     * @param variable The variable name assigned to the <tt>{VAR}</tt> token. For example, in <tt>{VAR=townName}</tt>,
     *                 the variable will be <tt>townName</tt>
     * @return The replacement String, or {@code null} if this method should be ignored and the regular values should be
     * used instead.
     * @see TypingLabel#setVariable(String, String)
     * @see TypingLabel#setVariables(java.util.Map)
     * @see TypingLabel#setVariables(com.badlogic.gdx.utils.ObjectMap)
     */
    public String replaceVariable(String variable);

    /**
     * Called when a new character is displayed. May be called many times per frame depending on the label
     * configurations and text speed. Useful to do a certain action each time a character is displayed, like playing a
     * sound effect.
     */
    public void onChar(Character ch);}
