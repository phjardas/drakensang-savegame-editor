package de.jardas.drakensang.gui.wizard;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import java.util.HashMap;
import java.util.Map;


/**
 * The model for the Wizard component, which tracks the text, icons, and enabled state
 * of each of the buttons, as well as the current panel that is displayed. Note that
 * the model, in its current form, is not intended to be subclassed.
 */
public class WizardModel {
    /**
     * Identification string for the current panel.
     */
    public static final String CURRENT_PANEL_DESCRIPTOR_PROPERTY = "currentPanelDescriptorProperty";

    /**
     * Property identification String for the Back button's text
     */
    public static final String BACK_BUTTON_TEXT_PROPERTY = "backButtonTextProperty";

    /**
     * Property identification String for the Back button's icon
     */
    public static final String BACK_BUTTON_ICON_PROPERTY = "backButtonIconProperty";

    /**
     * Property identification String for the Back button's enabled state
     */
    public static final String BACK_BUTTON_ENABLED_PROPERTY = "backButtonEnabledProperty";

    /**
     * Property identification String for the Next button's text
     */
    public static final String NEXT_FINISH_BUTTON_TEXT_PROPERTY = "nextButtonTextProperty";

    /**
     * Property identification String for the Next button's icon
     */
    public static final String NEXT_FINISH_BUTTON_ICON_PROPERTY = "nextButtonIconProperty";

    /**
     * Property identification String for the Next button's enabled state
     */
    public static final String NEXT_FINISH_BUTTON_ENABLED_PROPERTY = "nextButtonEnabledProperty";

    /**
     * Property identification String for the Cancel button's text
     */
    public static final String CANCEL_BUTTON_TEXT_PROPERTY = "cancelButtonTextProperty";

    /**
     * Property identification String for the Cancel button's icon
     */
    public static final String CANCEL_BUTTON_ICON_PROPERTY = "cancelButtonIconProperty";

    /**
     * Property identification String for the Cancel button's enabled state
     */
    public static final String CANCEL_BUTTON_ENABLED_PROPERTY = "cancelButtonEnabledProperty";
    private WizardPanelDescriptor currentPanel;
    private final Map<Object, WizardPanelDescriptor> panelMap = new HashMap<Object, WizardPanelDescriptor>();
    private final Map<String, Object> buttonTextMap = new HashMap<String, Object>();
    private final Map<String, Object> buttonEnabledMap = new HashMap<String, Object>();
    private PropertyChangeSupport propertyChangeSupport;

    /**
     * Default constructor.
     */
    public WizardModel() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    /**
     * Returns the currently displayed WizardPanelDescriptor.
     * @return The currently displayed WizardPanelDescriptor
     */
    WizardPanelDescriptor getCurrentPanelDescriptor() {
        return currentPanel;
    }

    /**
     * Registers the WizardPanelDescriptor in the model using the Object-identifier specified.
     * @param id Object-based identifier
     * @param descriptor WizardPanelDescriptor that describes the panel
     */
    void registerPanel(Object id, WizardPanelDescriptor descriptor) {
        //  Place a reference to it in a hashtable so we can access it later
        //  when it is about to be displayed.
        panelMap.put(id, descriptor);
    }

    /**
     * Sets the current panel to that identified by the Object passed in.
     * @param id Object-based panel identifier
     * @return boolean indicating success or failure
     */
    boolean setCurrentPanel(Object id) {
        //  First, get the hashtable reference to the panel that should
        //  be displayed.
        WizardPanelDescriptor nextPanel = (WizardPanelDescriptor) panelMap
            .get(id);

        //  If we couldn't find the panel that should be displayed, return
        //  false.
        if (nextPanel == null) {
            throw new WizardPanelNotFoundException(id);
        }

        WizardPanelDescriptor oldPanel = currentPanel;
        currentPanel = nextPanel;

        if (oldPanel != currentPanel) {
            firePropertyChange(CURRENT_PANEL_DESCRIPTOR_PROPERTY, oldPanel,
                currentPanel);
        }

        return true;
    }

    Object getBackButtonText() {
        return buttonTextMap.get(BACK_BUTTON_TEXT_PROPERTY);
    }

    void setBackButtonText(Object newText) {
        Object oldText = getBackButtonText();

        if (!newText.equals(oldText)) {
            buttonTextMap.put(BACK_BUTTON_TEXT_PROPERTY, newText);
            firePropertyChange(BACK_BUTTON_TEXT_PROPERTY, oldText, newText);
        }
    }

    Object getNextFinishButtonText() {
        return buttonTextMap.get(NEXT_FINISH_BUTTON_TEXT_PROPERTY);
    }

    void setNextFinishButtonText(Object newText) {
        Object oldText = getNextFinishButtonText();

        if (!newText.equals(oldText)) {
            buttonTextMap.put(NEXT_FINISH_BUTTON_TEXT_PROPERTY, newText);
            firePropertyChange(NEXT_FINISH_BUTTON_TEXT_PROPERTY, oldText,
                newText);
        }
    }

    Object getCancelButtonText() {
        return buttonTextMap.get(CANCEL_BUTTON_TEXT_PROPERTY);
    }

    void setCancelButtonText(Object newText) {
        Object oldText = getCancelButtonText();

        if (!newText.equals(oldText)) {
            buttonTextMap.put(CANCEL_BUTTON_TEXT_PROPERTY, newText);
            firePropertyChange(CANCEL_BUTTON_TEXT_PROPERTY, oldText, newText);
        }
    }

    Boolean getBackButtonEnabled() {
        return (Boolean) buttonEnabledMap.get(BACK_BUTTON_ENABLED_PROPERTY);
    }

    void setBackButtonEnabled(Boolean newValue) {
        Boolean oldValue = getBackButtonEnabled();

        if (newValue != oldValue) {
            buttonEnabledMap.put(BACK_BUTTON_ENABLED_PROPERTY, newValue);
            firePropertyChange(BACK_BUTTON_ENABLED_PROPERTY, oldValue, newValue);
        }
    }

    Boolean getNextFinishButtonEnabled() {
        return (Boolean) buttonEnabledMap.get(NEXT_FINISH_BUTTON_ENABLED_PROPERTY);
    }

    void setNextFinishButtonEnabled(Boolean newValue) {
        Boolean oldValue = getNextFinishButtonEnabled();

        if (newValue != oldValue) {
            buttonEnabledMap.put(NEXT_FINISH_BUTTON_ENABLED_PROPERTY, newValue);
            firePropertyChange(NEXT_FINISH_BUTTON_ENABLED_PROPERTY, oldValue,
                newValue);
        }
    }

    Boolean getCancelButtonEnabled() {
        return (Boolean) buttonEnabledMap.get(CANCEL_BUTTON_ENABLED_PROPERTY);
    }

    void setCancelButtonEnabled(Boolean newValue) {
        Boolean oldValue = getCancelButtonEnabled();

        if (newValue != oldValue) {
            buttonEnabledMap.put(CANCEL_BUTTON_ENABLED_PROPERTY, newValue);
            firePropertyChange(CANCEL_BUTTON_ENABLED_PROPERTY, oldValue,
                newValue);
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener p) {
        propertyChangeSupport.addPropertyChangeListener(p);
    }

    public void removePropertyChangeListener(PropertyChangeListener p) {
        propertyChangeSupport.removePropertyChangeListener(p);
    }

    protected void firePropertyChange(String propertyName, Object oldValue,
        Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue,
            newValue);
    }
}
