import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class UserListPane extends JPanel implements UserStatusListener {

    private final ChatClient client;
    private JList<String> userListUI;
    private DefaultListModel<String> userListModel;

    public UserListPane(ChatClient client) {
        this.client = client;
        this.client.addUserStatusListener(this);

        userListModel = new DefaultListModel<>();
        userListUI = new JList<>(userListModel);
        setLayout(new BorderLayout());
        add(new JScrollPane(userListUI), BorderLayout.CENTER);

        userListUI.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1) {
                    String login = userListUI.getSelectedValue();
                    MessagePane messagePane = new MessagePane(client, login);

                    JFrame fr = new JFrame("Message: " + login);
                    fr.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    fr.setSize(500, 500);
                    fr.getContentPane().add(messagePane, BorderLayout.CENTER);
                    fr.setVisible(true);
                }
            }
        });
    }

    public static void main(String[] args) {
        ChatClient client = new ChatClient("localhost", 10001);

        UserListPane userListPane = new UserListPane(client);
        JFrame frame = new JFrame("user List");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        
        frame.getContentPane().add(new JScrollPane(userListPane), BorderLayout.CENTER);
        frame.setVisible(true);

        if (client.connect()) {
            try {
                client.login("lion", "one");
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    @Override
    public void onLine(String login) {
        userListModel.addElement(login);
    }

    @Override
    public void offLine(String login) {
        userListModel.removeElement(login);
    }
}
