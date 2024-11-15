import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RegistrationForm2 extends JFrame {

    private JTextField idField, nameField, addressField, contactField;
    private JRadioButton maleRadio, femaleRadio;
    private JButton registerButton, exitButton;
    private JTable table;
    private DefaultTableModel tableModel;
    private JPanel dataDisplayPanel;

    public RegistrationForm2() {
        setTitle("Registration Form");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Create components
        JLabel idLabel = new JLabel("ID:");
        JLabel nameLabel = new JLabel("Name:");
        JLabel genderLabel = new JLabel("Gender:");
        JLabel addressLabel = new JLabel("Address:");
        JLabel contactLabel = new JLabel("Contact:");

        idField = new JTextField(10);
        nameField = new JTextField(20);
        addressField = new JTextField(20);
        contactField = new JTextField(15);

        maleRadio = new JRadioButton("Male");
        femaleRadio = new JRadioButton("Female");
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);

        registerButton = new JButton("Register");
        exitButton = new JButton("Exit");

        // Create table
        String[] columnNames = {"ID", "Name", "Gender", "Address", "Contact"};
        tableModel = new DefaultTableModel(columnNames, 0); // Initialize with column names only
        table = new JTable(tableModel);

        // Create data display panel
        dataDisplayPanel = new JPanel();
        dataDisplayPanel.setLayout(new BoxLayout(dataDisplayPanel, BoxLayout.Y_AXIS));
        dataDisplayPanel.setBorder(BorderFactory.createTitledBorder("Data Display"));

        // Layout using GroupLayout
        JPanel inputPanel = new JPanel();
        GroupLayout layout = new GroupLayout(inputPanel);
        inputPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(idLabel)
                    .addComponent(nameLabel)
                    .addComponent(genderLabel)
                    .addComponent(addressLabel)
                    .addComponent(contactLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(idField)
                    .addComponent(nameField)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(maleRadio)
                        .addComponent(femaleRadio))
                    .addComponent(addressField)
                    .addComponent(contactField))
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(idLabel)
                    .addComponent(idField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel)
                    .addComponent(nameField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(genderLabel)
                    .addComponent(maleRadio)
                    .addComponent(femaleRadio))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(addressLabel)
                    .addComponent(addressField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(contactLabel)
                    .addComponent(contactField))
        );

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(registerButton);
        buttonPanel.add(exitButton);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Register button action listener
        registerButton.addActionListener(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String gender = maleRadio.isSelected() ? "Male" : femaleRadio.isSelected() ? "Female" : "";
            String address = addressField.getText().trim();
            String contact = contactField.getText().trim();

            // Validate inputs
            if (id.isEmpty() || name.isEmpty() || gender.isEmpty() || address.isEmpty() || contact.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Object[] rowData = {id, name, gender, address, contact};
            tableModel.addRow(rowData);

            // Update data display panel
            dataDisplayPanel.add(new JLabel(String.format("%s | %s | %s | %s | %s", id, name, gender, address, contact)));
            dataDisplayPanel.revalidate();

            // Clear input fields
            idField.setText("");
            nameField.setText("");
            genderGroup.clearSelection();
            addressField.setText("");
            contactField.setText("");
        });

        exitButton.addActionListener(e -> System.exit(0));

        // Main layout
        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.WEST);
        add(dataDisplayPanel, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.SOUTH);
        add(tablePanel, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegistrationForm2::new);
    }
}
