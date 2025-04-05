package graphics.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * LobbyPanel represents a user interface panel that displays a list of available game rooms.
 * It allows the user to see a list of game rooms, select one, and choose to either join an existing room
 * or create a new game room. This class is designed using the Swing framework.
 */
public class LobbyPanel extends JPanel {

    // The model holding the list of game room names. It automatically notifies the JList when the data changes.
    // https://docs.oracle.com/javase/8/docs/api/javax/swing/DefaultListModel.html
    private DefaultListModel<String> listModel;

    // The JList component that displays the game room names stored in the listModel.
    private JList<String> gameRoomsList;

    // A button that, when pressed, will trigger the action of joining the selected game room.
    private JButton joinGameButton;

    // A button that, when pressed, will trigger the action of creating a new game room.
    private JButton createGameButton;

    /**
     * Constructor for LobbyPanel.
     * It initializes the user interface components by calling the initUI() method.
     */
    public LobbyPanel() {
        initUI(); // Set up the entire layout and UI components.
    }

    /**
     * Initializes and sets up the user interface components and layout of the panel.
     * This method configures:
     *   The layout manager (BorderLayout) for the panel with horizontal and vertical gaps.
     *   A title label at the top of the panel.
     *   The list model and JList for displaying game room names.
     *   A scroll pane that wraps the JList to enable scrolling when needed.
     *   A button panel at the bottom containing the "Join Game" and "Create Game" buttons.
     */
    private void initUI() {
        // Set the layout of the panel to BorderLayout with 10 pixels horizontal and vertical gaps.
        setLayout(new BorderLayout(10, 10));

        // Create a label to serve as the title of the panel.
        JLabel titleLabel = new JLabel("Available Game Rooms:");
        // Center the title horizontally within its area.
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        // Place the title label in the NORTH (top) region of the panel.
        add(titleLabel, BorderLayout.NORTH);

        // Initialize the DefaultListModel which will hold the names of the game rooms.
        listModel = new DefaultListModel<>();
        // Optional: Uncomment the following lines to add sample data for testing.
        // listModel.addElement("Room 1");
        // listModel.addElement("Room 2");
        // listModel.addElement("Room 3");

        // Create the JList component using the list model.
        // The JList automatically updates when the listModel is modified.
        gameRoomsList = new JList<>(listModel);
        // Set the selection mode so that only one game room can be selected at a time.
        gameRoomsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Create a scroll pane for the JList to ensure the list is scrollable when it contains many entries.
        JScrollPane listScrollPane = new JScrollPane(gameRoomsList);
        // Add the scroll pane (and hence the JList) to the CENTER region of the panel.
        add(listScrollPane, BorderLayout.CENTER);

        // Create a panel for the buttons with a FlowLayout.
        // FlowLayout arranges components in a row and centers them by default.
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // Initialize the button that allows the user to join a game.
        joinGameButton = new JButton("Join Game");
        // Initialize the button that allows the user to create a new game.
        createGameButton = new JButton("Create Game");

        // Add both buttons to the button panel.
        buttonPanel.add(joinGameButton);
        buttonPanel.add(createGameButton);

        // Add the button panel to the SOUTH (bottom) region of the main panel.
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Sets the action listener for the "Join Game" button.
     * This allows the controller to define what should happen when the button is clicked.
     *
     * @param actionListener The ActionListener that will handle the join game event.
     */
    public void setJoinGameAction(ActionListener actionListener) {
        joinGameButton.addActionListener(actionListener);
    }

    /**
     * Sets the action listener for the "Create Game" button.
     * This allows the controller to define what should happen when the button is clicked.
     *
     * @param actionListener The ActionListener that will handle the create game event.
     */
    public void setCreateGameAction(ActionListener actionListener) {
        createGameButton.addActionListener(actionListener);
    }

    /**
     * Retrieves the currently selected game room from the JList.
     *
     * @return The name of the selected game room, or null if no room is selected.
     */
    public String getSelectedGameRoom() {
        return gameRoomsList.getSelectedValue();
    }

    /**
     * Updates the list of game rooms displayed in the lobby.
     * <p>
     * This method clears the existing entries in the list model and then iterates over the provided list of room names,
     * adding each one to the list model. The JList is automatically updated as the list model changes.
     * </p>
     *
     * @param rooms A List of Strings where each String represents the name of a game room.
     */
    public void updateGameRooms(List<String> rooms) {
        // Clear all current entries from the list model to prepare for new data.
        listModel.clear();
        // Loop through each room name in the provided list.
        for (String room : rooms) {
            // Add each room name to the list model.
            listModel.addElement(room);
        }
    }
}
