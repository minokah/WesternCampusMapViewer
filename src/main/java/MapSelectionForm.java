
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import org.json.JSONArray;
import org.json.JSONObject;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 * Map selection form. User will log in and select a map using a drop down.
 * Developers may also edit the building data.
 *
 * @author bluu8
 */
public class MapSelectionForm extends javax.swing.JFrame {

    /**
     * Creates new form MapSelectionForm
     */
    private User currentUser;
    private Weather weather;

    HashMap<String, Building> buildingReference;

    private Building selectedBuilding;

    javax.swing.JFrame window = this;

    /**
     * Generate the map selection GUI. Set the labels.
     *
     * @param user
     */
    public MapSelectionForm(User user) {
        initComponents();

        // create western crest in the layer panel. the netbeans gui editor is awful and
        // will not layer elements even if you want it to, so i will have to do it manually.
        // the effort i put in make this look visually appealing...
        JLabel crest = new JLabel(new ImageIcon(getClass().getResource("/ui/crest.png")));
        crest.setSize(813, 491);
        menuPane.add(crest);
        menuPane.setComponentZOrder(crest, menuPane.getComponentCount() - 1);

        currentUser = user;
        weather = new Weather();

        welcomeText.setText("Welcome, " + user.getUsername());

        // hide the developer stuff
        dataText.setText("Data: " + user.getData());
        isDeveloperText.setText("Developer: " + user.isDeveloper());

        dataText.setVisible(false);

        if (!user.isDeveloper()) {
            isDeveloperText.setVisible(false);
        } else {
            isDeveloperText.setText("Developer Mode");
        }

        // Yes the weather API will call twice but who cares
        updateWeatherWidget();

        // <Building Name, Building Code> ie. <"Middlesex College", "MC">
        buildingReference = new HashMap();

        // clear default box items
        buildingSelector.removeAllItems();

        // reference for editing or adding building
        selectedBuilding = null;

        // hide if user is not developer
        if (!user.isDeveloper()) {
            editBuildingButton.setVisible(false);
            addBuildingButton.setVisible(false);
        }

        try {
            // read all building data
            JSONArray jsonBuildings = new JSONFileReader("data/building.json").getData().getJSONArray("buildings");

            // iterate and create User entries in map
            for (int i = 0; i != jsonBuildings.length(); i++) {
                JSONObject buildingData = jsonBuildings.getJSONObject(i);
                String name = buildingData.getString("name");
                String code = buildingData.getString("code");

                buildingReference.put(name, new Building(code));
                buildingSelector.addItem(name);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Failed to read building data!");
            errorLabel.setText("Failed to read building data!");
            openMapButton.setEnabled(false);
        }
    }

    /**
     * Update the weather widget. Displays an error if it fails.
     */
    public void updateWeatherWidget() {
        if (weather.updateWeatherData()) {
            weatherTempText.setText(weather.getTemperature() + "°C");
            weatherDateText.setText(weather.getDate());
        } else { // Weather update failed
            weatherTempText.setText("N/A");
            weatherDateText.setText("Couldn't Update Weather");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuPane = new javax.swing.JLayeredPane();
        addBuildingButton = new javax.swing.JButton();
        weatherUpdateButton = new javax.swing.JButton();
        editBuildingButton = new javax.swing.JButton();
        isDeveloperText = new javax.swing.JLabel();
        welcomeText = new javax.swing.JLabel();
        buildingSelector = new javax.swing.JComboBox<>();
        openMapButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        weatherDateText = new javax.swing.JLabel();
        weatherTempText = new javax.swing.JLabel();
        returnToLoginButton = new javax.swing.JButton();
        userIcon = new javax.swing.JLabel();
        buildingImage = new javax.swing.JLabel();
        buildingNameLabel = new javax.swing.JLabel();
        dataText = new javax.swing.JLabel();
        weatherIcon = new javax.swing.JLabel();
        errorLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Western Campus Map Viewer > Home");
        setResizable(false);

        menuPane.setBackground(new java.awt.Color(31, 31, 31));
        menuPane.setOpaque(true);

        addBuildingButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        addBuildingButton.setText("Add Building");
        addBuildingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBuildingButtonActionPerformed(evt);
            }
        });

        weatherUpdateButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        weatherUpdateButton.setText("Update");
        weatherUpdateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                weatherUpdateButtonActionPerformed(evt);
            }
        });

        editBuildingButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        editBuildingButton.setText("Edit Building");
        editBuildingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editBuildingButtonActionPerformed(evt);
            }
        });

        isDeveloperText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        isDeveloperText.setForeground(new java.awt.Color(255, 255, 255));
        isDeveloperText.setText("Developer: false");

        welcomeText.setFont(new java.awt.Font("Segoe UI Semibold", 1, 28)); // NOI18N
        welcomeText.setForeground(new java.awt.Color(255, 255, 255));
        welcomeText.setText("Welcome, username");

        buildingSelector.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        buildingSelector.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        buildingSelector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buildingSelectorActionPerformed(evt);
            }
        });

        openMapButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        openMapButton.setText("Open Map");
        openMapButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMapButtonActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Select Building");

        weatherDateText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        weatherDateText.setForeground(new java.awt.Color(255, 255, 255));
        weatherDateText.setText("Couldn't Update Weather");

        weatherTempText.setFont(new java.awt.Font("Segoe UI Semibold", 0, 22)); // NOI18N
        weatherTempText.setForeground(new java.awt.Color(255, 255, 255));
        weatherTempText.setText("N/A");

        returnToLoginButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        returnToLoginButton.setText("Logout");
        returnToLoginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnToLoginButtonActionPerformed(evt);
            }
        });

        userIcon.setFont(userIcon.getFont().deriveFont((float)14));
        userIcon.setForeground(new java.awt.Color(255, 255, 255));
        userIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/profile.png"))); // NOI18N

        buildingImage.setFont(buildingImage.getFont().deriveFont((float)14));
        buildingImage.setForeground(new java.awt.Color(255, 255, 255));
        buildingImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/nobuilding.png"))); // NOI18N

        buildingNameLabel.setFont(new java.awt.Font("Segoe UI Semibold", 0, 24)); // NOI18N
        buildingNameLabel.setForeground(new java.awt.Color(255, 255, 255));
        buildingNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        buildingNameLabel.setText("Building Name");

        dataText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        dataText.setForeground(new java.awt.Color(255, 255, 255));
        dataText.setText("Data: none");

        weatherIcon.setFont(weatherIcon.getFont().deriveFont((float)14));
        weatherIcon.setForeground(new java.awt.Color(255, 255, 255));
        weatherIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/temperature.png"))); // NOI18N

        errorLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        errorLabel.setForeground(new java.awt.Color(255, 255, 255));
        errorLabel.setText(" ");

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));

        jButton1.setText("Help");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        menuPane.setLayer(addBuildingButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
        menuPane.setLayer(weatherUpdateButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
        menuPane.setLayer(editBuildingButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
        menuPane.setLayer(isDeveloperText, javax.swing.JLayeredPane.DEFAULT_LAYER);
        menuPane.setLayer(welcomeText, javax.swing.JLayeredPane.DEFAULT_LAYER);
        menuPane.setLayer(buildingSelector, javax.swing.JLayeredPane.DEFAULT_LAYER);
        menuPane.setLayer(openMapButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
        menuPane.setLayer(jLabel2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        menuPane.setLayer(weatherDateText, javax.swing.JLayeredPane.DEFAULT_LAYER);
        menuPane.setLayer(weatherTempText, javax.swing.JLayeredPane.DEFAULT_LAYER);
        menuPane.setLayer(returnToLoginButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
        menuPane.setLayer(userIcon, javax.swing.JLayeredPane.DEFAULT_LAYER);
        menuPane.setLayer(buildingImage, javax.swing.JLayeredPane.DEFAULT_LAYER);
        menuPane.setLayer(buildingNameLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        menuPane.setLayer(dataText, javax.swing.JLayeredPane.DEFAULT_LAYER);
        menuPane.setLayer(weatherIcon, javax.swing.JLayeredPane.DEFAULT_LAYER);
        menuPane.setLayer(errorLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        menuPane.setLayer(jLabel1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        menuPane.setLayer(jButton1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout menuPaneLayout = new javax.swing.GroupLayout(menuPane);
        menuPane.setLayout(menuPaneLayout);
        menuPaneLayout.setHorizontalGroup(
            menuPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, menuPaneLayout.createSequentialGroup()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(menuPaneLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(menuPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(menuPaneLayout.createSequentialGroup()
                        .addComponent(returnToLoginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(userIcon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(weatherIcon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(weatherTempText)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(weatherDateText)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(weatherUpdateButton)
                        .addGap(12, 12, 12))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, menuPaneLayout.createSequentialGroup()
                        .addGroup(menuPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(menuPaneLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton1))
                            .addGroup(menuPaneLayout.createSequentialGroup()
                                .addGroup(menuPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(buildingSelector, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(openMapButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(addBuildingButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(menuPaneLayout.createSequentialGroup()
                                        .addGroup(menuPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(welcomeText)
                                            .addComponent(jLabel2)
                                            .addComponent(isDeveloperText)
                                            .addComponent(dataText)
                                            .addComponent(errorLabel))
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(editBuildingButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(menuPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(buildingNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(buildingImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(13, 13, 13))))
        );
        menuPaneLayout.setVerticalGroup(
            menuPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, menuPaneLayout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addGap(36, 36, 36)
                .addGroup(menuPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(menuPaneLayout.createSequentialGroup()
                        .addComponent(buildingImage)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buildingNameLabel)
                        .addGap(18, 18, 18)
                        .addGroup(menuPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(weatherUpdateButton, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, menuPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(weatherDateText)
                                .addComponent(weatherTempText))
                            .addComponent(weatherIcon, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(menuPaneLayout.createSequentialGroup()
                        .addComponent(welcomeText)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addGap(12, 12, 12)
                        .addComponent(buildingSelector, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(openMapButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(addBuildingButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editBuildingButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(menuPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(menuPaneLayout.createSequentialGroup()
                                .addComponent(isDeveloperText)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dataText)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(errorLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(returnToLoginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(menuPaneLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(userIcon)))))
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(menuPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(menuPane)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Opens the building editor window if the user is a developer. Makes
     * changes once the window is closed.
     *
     * @param evt Default Java event.
     */
    private void editBuildingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBuildingButtonActionPerformed
        // temporary building to store the "before" data
        try {
            Building oldBuilding = new Building(selectedBuilding.getCode());

            javax.swing.JFrame newDialog = new BuildingEditor(selectedBuilding);
            newDialog.setVisible(true);
            window.setEnabled(false);

            newDialog.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosed(java.awt.event.WindowEvent e) {
                    window.setEnabled(true);
                    
                    // the building reference's name is changed
                    if (!oldBuilding.getName().equals(selectedBuilding.getName())) {
                        // NOT deleted
                        if (selectedBuilding.getName() != null && selectedBuilding.getCode() != null) {
                            String newName = String.valueOf(selectedBuilding.getName());

                            // remove OLD from selection and add in new
                            buildingReference.remove(oldBuilding.getName());
                            buildingReference.put(newName, selectedBuilding);
                            buildingSelector.addItem(newName);
                            buildingSelector.removeItem(oldBuilding.getName());
                        } // data was deleted
                        else {
                            // remove it from selection
                            buildingReference.remove((String) buildingSelector.getSelectedItem());
                            buildingSelector.removeItem((String) buildingSelector.getSelectedItem());
                        }

                        writeToFile();
                    }
                    // cancelled otherwise
                    
                    // set the image to the building image, otherwise set it to the default
                    File image = new File("data/building/" + buildingReference.get((String) buildingSelector.getSelectedItem()).getCode() + "/splash.png");
                    if (image.exists()) {
                        buildingImage.setIcon(new ImageIcon("data/building/" + buildingReference.get((String) buildingSelector.getSelectedItem()).getCode() + "/splash.png"));
                    } 
                    else {
                        buildingImage.setIcon(new ImageIcon(getClass().getResource("ui/nobuilding.png")));
                    }
                }
            });
        } catch (FileNotFoundException e) {
            // See above
        }
    }//GEN-LAST:event_editBuildingButtonActionPerformed

    /**
     * Update the weather widget when Update is clicked.
     *
     * @param evt Default Java event.
     */
    private void weatherUpdateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_weatherUpdateButtonActionPerformed
        updateWeatherWidget();
    }//GEN-LAST:event_weatherUpdateButtonActionPerformed

    /**
     * Return to the log in form when Logout is clicked.
     *
     * @param evt Default Java event.
     */
    private void returnToLoginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnToLoginButtonActionPerformed
        new LoginForm().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_returnToLoginButtonActionPerformed

    /**
     * Open up a building editor window to edit a blank building object. Writes
     * if the building is actually edited.
     *
     * @param evt Default Java event.
     */
    private void addBuildingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBuildingButtonActionPerformed

        try {
            // Create a building template
            Building newBuilding = new Building(null);

            javax.swing.JFrame newDialog = new BuildingEditor(newBuilding);
            newDialog.setVisible(true);
            javax.swing.JFrame window = this;
            window.setEnabled(false);

            newDialog.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosed(java.awt.event.WindowEvent e) {
                    window.setEnabled(true);
                    // successfully created
                    if (newBuilding.getName() != null && newBuilding.getCode() != null) {
                        buildingReference.put(newBuilding.getName(), newBuilding);
                        buildingSelector.addItem(newBuilding.getName());

                        writeToFile();
                    }
                }
            });
        } catch (Exception e) {
            // This will never happen. For some reason, Java wants me to make newBuilding a final variable
            // if I move it outside the exception
        }
    }//GEN-LAST:event_addBuildingButtonActionPerformed

    /**
     * Opens up the selected map with the building and user data.
     *
     * @param evt Default Java event.
     */
    private void openMapButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMapButtonActionPerformed
        new MapViewer(buildingReference.get((String) buildingSelector.getSelectedItem()), currentUser).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_openMapButtonActionPerformed

    /**
     * Updates the labels when a building is selected. The preview image is
     * updated as well.
     *
     * @param evt Default Java event.
     */
    private void buildingSelectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buildingSelectorActionPerformed
        try {
            // set the image to the building image, otherwise set it to the default
            File image = new File("data/building/" + buildingReference.get((String) buildingSelector.getSelectedItem()).getCode() + "/splash.png");
            if (image.exists()) {
                buildingImage.setIcon(new ImageIcon("data/building/" + buildingReference.get((String) buildingSelector.getSelectedItem()).getCode() + "/splash.png"));
                buildingImage.repaint();
            } else {
                buildingImage.setIcon(new ImageIcon(getClass().getResource("ui/nobuilding.png")));
            }
            buildingNameLabel.setText((String) buildingSelector.getSelectedItem());
            selectedBuilding = buildingReference.get((String) buildingSelector.getSelectedItem());
        } 
        catch (Exception e) {
            // bandaid fix, this will run every time the selection changes unfortunately
        }
    }//GEN-LAST:event_buildingSelectorActionPerformed

    /**
     * Opens up the help form.
     *
     * @param evt Default Java event.
     */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        new HelpPage().setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * Write to the building manifest file.
     */
    private void writeToFile() {
        JSONObject json = new JSONObject();
        JSONArray buildings = new JSONArray();

        for (String n : buildingReference.keySet()) {
            Building b = buildingReference.get(n);
            JSONObject nb = new JSONObject();
            nb.put("name", b.getName());
            nb.put("code", b.getCode());

            buildings.put(nb);
        }

        json.put("buildings", buildings);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("data/building.json"));
            writer.write(json.toString());
            writer.close();
        } catch (IOException e) {
            System.out.println("Failed to write building manifest to building.json!");
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MapSelectionForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MapSelectionForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MapSelectionForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MapSelectionForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                System.out.println("Please run LoginForm.java instead");
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBuildingButton;
    private javax.swing.JLabel buildingImage;
    private javax.swing.JLabel buildingNameLabel;
    private javax.swing.JComboBox<String> buildingSelector;
    private javax.swing.JLabel dataText;
    private javax.swing.JButton editBuildingButton;
    private javax.swing.JLabel errorLabel;
    private javax.swing.JLabel isDeveloperText;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLayeredPane menuPane;
    private javax.swing.JButton openMapButton;
    private javax.swing.JButton returnToLoginButton;
    private javax.swing.JLabel userIcon;
    private javax.swing.JLabel weatherDateText;
    private javax.swing.JLabel weatherIcon;
    private javax.swing.JLabel weatherTempText;
    private javax.swing.JButton weatherUpdateButton;
    private javax.swing.JLabel welcomeText;
    // End of variables declaration//GEN-END:variables
}
