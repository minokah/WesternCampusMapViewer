/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.json.JSONException;

/**
 * Map Viewer GUI interface.
 * <br><br>
 * The map will load up all the POIs specified by the building code and
 * also load up the user's favourites and personal POIs.
 * @author bluu8
 */
public class MapViewer extends javax.swing.JFrame {

    private User currentUser;
    private Building building;
    private String fileName;
    private ArrayList<Floor> userFloors;
    private ArrayList<Floor> userFavourites;
    private ArrayList<JLabel> mapImagesReference;
    private HashMap<POI, JLabel> poiReference;

    // These are for the Sidebar highlighting and in-map POI knowing which JLayeredPane is currently active
    // It will compare the current floor pane object with the *supposed* floor pane that we are on
    // If they are the same, then don't re-add the info box into the same pane (cause it will crash for some reason!)
    private JLayeredPane currentFloorLayerPanel = null;
    private ArrayList<JLayeredPane> floorLayerPanelReference;
    
    private ArrayList<JScrollPane> floorScrollPanelReference;
    
    // current POI that we are looking at to potentially edit
    POI selectedPOI = null;
    
    // Add or Edit mode
    String editorMode = "none";
    
    JFrame window = this;

    /**
     * Sets up the map viewer. Generates the built-in and user POIs, mark favourites, and generate the floor information.
     * @param b The building. It is generated and passed from the MapSelectionForm.
     * @param user The current user.
     */
    public MapViewer(Building b, User user) {
        initComponents();
        floorLayerPanelReference = new ArrayList();
        poiInfoPanel.setVisible(false);
        modeTopLabel.setVisible(false);
        modeTopButton.setVisible(false);
        modeTopImage.setVisible(false);

        try {
            // set the user and get map data from json
            currentUser = user;
            building = b;
            mapImagesReference = new ArrayList();
            poiReference = new HashMap();
            floorLayerPanelReference = new ArrayList();
            floorScrollPanelReference = new ArrayList();
            
            fileName = building.getCode();
            
            // we need to normalize the user's data of the buildings.
            // if a new building is created, and the user does not have the building entry
            // or is missing floors that were added, fix it
            user.normalizeBuilding(building);
            userFloors = user.getCustomData(building.getCode());
            userFavourites = user.getFavouritesData(building.getCode());

            // for each floor create the GUI object

            ArrayList<String> categoryAdded = new ArrayList();
            
            // no floors! disable the interface
            if (building.getNumFloors() == 0) {
                floorNumText.setText("No Floors");
                accessibilityToggle.setEnabled(false);
                createPOIButton.setEnabled(false);
                modeTopLabel.setVisible(true);
                modeTopLabel.setText("There are no floors found in this building.");
            }
            
            // otherwise, we do have floors, create a new scroll panel for each
            for (int i = 0; i != building.getNumFloors(); i++) {
                Floor fl = building.getFloor(i);
                JScrollPane newScrollPanel = new JScrollPane();
                floorScrollPanelReference.add(newScrollPanel);
                
                JLayeredPane newLayerPanel = new JLayeredPane();
                JLabel newMapImage = new JLabel();
                newMapImage.setIcon(new ImageIcon("data/building/" + building.getCode() + "/" + fl.getID() + ".png"));
                newLayerPanel.add(newMapImage);
                
                // this single line took me 3 hours to debug
                newLayerPanel.setComponentZOrder(newMapImage, 0);

                // strange panel behaviour, we must set the bounds and size. this took me like an hour to figure out
                newMapImage.setBounds(0, 0, 3400, 2200);
                newLayerPanel.setPreferredSize(new Dimension(3400, 2200));
                
                // make a reference of the map image so we can change accessibility layer
                mapImagesReference.add(newMapImage);

                // Create POI gui objects
                HashMap<String, ArrayList> poiData = fl.getPOIs();

                // for each category in the hashmap
                for (String category : poiData.keySet()) {
                    // create POIs on the GUI
                    ArrayList<POI> poiList = poiData.get(category);

                    // for every single  POI in the ArrayList
                    for (int a = 0; a != poiList.size(); a++) {
                        // get POI
                        POI poiEntry = poiList.get(a);
                        
                        createPOILabel(poiEntry, newLayerPanel);
                    }
                }
                
                // Add/Edit "click on map" functionality for adding a POI and editors
                newLayerPanel.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        // Add new POI
                        if (editorMode.equals("add")) {
                            // set the icon's center to the mouse tip instead of the top left corner
                            int x = e.getX() - 14;
                            int y = e.getY() - 14;
                            if (x < 0) x = 0;
                            if (y < 0) y = 0;
                            
                            POI newPOI = new POI(null, null, null, null, x, y);
                            JFrame newEditor = new POIEditor(newPOI);
                            newEditor.setVisible(true);
                            window.setEnabled(false);

                            newEditor.addWindowListener(new java.awt.event.WindowAdapter() {
                                @Override
                                public void windowClosed(java.awt.event.WindowEvent ev) {
                                    window.setEnabled(true);
                                    
                                    // If the POI was edited successfully, add it
                                    if (newPOI.getName() != null) {        
                                        // Add and refresh
                                        selectedPOI = newPOI;
                                        setPOIInfoBox();
                                        
                                        // if developer, add to built in poi list
                                        if (currentUser.isDeveloper()) {
                                            building.getFloor(floorSelector.getSelectedIndex()).addPOI(newPOI);
                                        }
                                        // otherwise, add it to the user's pois
                                        else {
                                            userFloors.get(floorSelector.getSelectedIndex()).addPOI(newPOI);
                                        }
                                        
                                        createPOILabel(selectedPOI, newLayerPanel);
                                        
                                        refreshSidebar();
                                        
                                        if (user.isDeveloper()) building.writeToFile();
                                        else user.writeToFile();
                                    }
                                }
                            });
                        }
                        // Edit Position
                        else if (editorMode.equals("edit")) {
                            if (selectedPOI != null) {
                                // set the icon's center to the mouse tip instead of the top left corner
                                int x = e.getX() - 14;
                                int y = e.getY() - 14;
                                if (x < 0) x = 0;
                                if (y < 0) y = 0;
                                selectedPOI.setX(x);
                                selectedPOI.setY(y);
                                
                                poiReference.get(selectedPOI).setBounds(selectedPOI.getX(), selectedPOI.getY(), 38, 38);
                                setPOIInfoBox();
                                
                                refreshSidebar();
                            }
                        }
                        // Add mouse click event to hide POI info frame when clicking in background
                        else if (editorMode.equals("none")) {
                            poiInfoPanel.setVisible(false);
                            selectedPOI = null;
                        }
                    }
                });
                
                newScrollPanel.setViewportView(newLayerPanel);
                
                
                // scroll to the middle of the map instead of starting in the corner
                
                // for some ungodly reason, you must set horizontal scroll position,
                // and then the vertical position, and THEN the horizontal position once again
                // to make it scroll to the position you want, otherwise the vertical scroll will
                // get "stuck" at some very low value
                newScrollPanel.getHorizontalScrollBar().setValue(1275);
                newScrollPanel.getVerticalScrollBar().setValue(525);
                newScrollPanel.getHorizontalScrollBar().setValue(1275);
                
                floorLayerPanelReference.add(newLayerPanel);

                floorSelector.add(newScrollPanel);
                floorSelector.setTitleAt(i, fl.getName());
            }
            
            // mark the favourites
            for (int i = 0; i != userFavourites.size(); i++) {
                HashMap<String, ArrayList> poiData = userFavourites.get(i).getPOIs();
                for (String category : poiData.keySet()) {
                    // create POIs on the GUI
                    ArrayList<POI> poiList = poiData.get(category);

                    // for every single  POI in the ArrayList
                    for (int a = 0; a != poiList.size(); a++) {
                        // get POI
                        POI poiEntry = poiList.get(a);
                        
                        // mark each poi in the map as "favourited"
                        for (POI bi : poiReference.keySet()) {
                            if (bi.isEquivalent(poiEntry)) bi.setFavourited(true);
                        }
                    }
                }
            }
            
            // now, add in the User created POIs for each floor
            for (int i = 0; i != userFloors.size(); i++) {
                HashMap<String, ArrayList> poiData = userFloors.get(i).getPOIs();
                for (String category : poiData.keySet()) {
                    // create POIs on the GUI
                    ArrayList<POI> poiList = poiData.get(category);

                    // for every single  POI in the ArrayList
                    for (int a = 0; a != poiList.size(); a++) {
                        // get POI
                        POI poiEntry = poiList.get(a);
                        
                        // Good thing we made a reference
                        createPOILabel(poiEntry, floorLayerPanelReference.get(i));
                    }
                }
            }
            
            // lazy refresh the sidebar
            if (building.getNumFloors() > 0) refreshSidebar();

            buildingNameText.setText(building.getName());
            this.setTitle("Western Campus Map Viewer > " + building.getName());
        } catch (JSONException e) {
            errorText.setText("Failed to read " + building.getName() + ".json : " + e);
        }
    }
    
    /**
     * Creates a JLabel to visually display the position of the POI.
     * @param poiEntry The POI object containing the POI information to use.
     * @param newLayerPanel The LayerPane to add the POI to.
     */
    public void createPOILabel(POI poiEntry, JLayeredPane newLayerPanel) {
        // create POI JLabel on map
        JLabel newPOI = new JLabel(new ImageIcon("data/icon/" + poiEntry.getIcon() + ".png"));
        newPOI.setBounds(poiEntry.getX(), poiEntry.getY(), 38, 38);
        newLayerPanel.add(newPOI);
        poiReference.put(poiEntry, newPOI);
        newLayerPanel.setComponentZOrder(newPOI, 0);
        //newLayerPanel.setComponentZOrder(mapImagesReference.get(floorSelector.getSelectedIndex()), 2);

        // add mouse click event to display info
        newPOI.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (editorMode.equals("none")) {
                    selectedPOI = poiEntry;
                    setPOIInfoBox();
                    poiReference.get(selectedPOI).setBounds(selectedPOI.getX(), selectedPOI.getY(), 38, 38);
                }
            }
        });
    }
    
    /**
     * Set the POI info box position to the currently selected POI and update the information within it.
     */
    public void setPOIInfoBox() {
        // if user isn't a dev and the poi is built in: favouriting POI
        if (!currentUser.isDeveloper() && selectedPOI.isBuiltIn()) {
            poiInfoEditButton.setVisible(false);
            poiPositionEditButton.setVisible(false);
            favouritePOIButton.setVisible(true);
            
            // set favourite state
            if (selectedPOI.isFavourited()) {
                favouritePOIButton.setText("★ Remove from Favourites");
            }
            else {
                favouritePOIButton.setText("☆ Add to Favourites");
            }
        }
        else {
            // otherwise they are a developer OR user editing their own POI
            favouritePOIButton.setVisible(false);
            poiInfoEditButton.setVisible(true);
            poiPositionEditButton.setVisible(true);
        }
        
        // workaround for conncting the poi sidebar gui to the actual map
        if (currentFloorLayerPanel != floorLayerPanelReference.get(floorSelector.getSelectedIndex())) {
            currentFloorLayerPanel = floorLayerPanelReference.get(floorSelector.getSelectedIndex());
            floorLayerPanelReference.get(floorSelector.getSelectedIndex()).add(poiInfoPanel);
            floorLayerPanelReference.get(floorSelector.getSelectedIndex()).setComponentZOrder(poiInfoPanel, 0);
        }

        // Move the POI info box
        poiInfoPanel.setVisible(true);
        
        // prevent tiny panel from going out of bounds
        // if the box goes below y = 0
        if (selectedPOI.getY() - 168 < 0) poiInfoPanel.setBounds(selectedPOI.getX(), selectedPOI.getY() + 55, 350, 162);
        else poiInfoPanel.setBounds(selectedPOI.getX(), selectedPOI.getY() - 168, 350, 162);
        
        // if the box goes beyond x = 3400
        if (selectedPOI.getX() + 355 > 3400) poiInfoPanel.setBounds(selectedPOI.getX() - 360, selectedPOI.getY() - 168, 350, 162);

        poiInfoNameLabel.setText(selectedPOI.getName());
        poiInfoIconLabel.setIcon(new ImageIcon("data/icon/" + selectedPOI.getIcon() + ".png"));
        poiInfoCategoryLabel.setText("<html>" + selectedPOI.getCategory() + "<br>(" + selectedPOI.getX() + ", " + selectedPOI.getY() + ")</html>");
        poiInfoDescLabel.setText("<html>" + selectedPOI.getDescription() + "</html>");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        poiInfoPanel = new javax.swing.JPanel();
        poiInfoIconLabel = new javax.swing.JLabel();
        poiInfoNameLabel = new javax.swing.JLabel();
        poiInfoDescLabel = new javax.swing.JLabel();
        poiInfoEditButton = new javax.swing.JButton();
        poiInfoCategoryLabel = new javax.swing.JLabel();
        favouritePOIButton = new javax.swing.JButton();
        poiPositionEditButton = new javax.swing.JButton();
        buildingNameText = new javax.swing.JLabel();
        floorSelector = new javax.swing.JTabbedPane();
        errorText = new javax.swing.JLabel();
        exitButton = new javax.swing.JButton();
        poiPanel = new javax.swing.JTabbedPane();
        biPOICategory = new javax.swing.JPanel();
        biPOIList = new javax.swing.JScrollPane();
        biPOILayer = new javax.swing.JLayeredPane();
        favPOICategory = new javax.swing.JPanel();
        favPOIList = new javax.swing.JScrollPane();
        favPOILayer = new javax.swing.JLayeredPane();
        myPOICategory = new javax.swing.JPanel();
        myPOIList = new javax.swing.JScrollPane();
        myPOILayer = new javax.swing.JLayeredPane();
        filterPanel = new javax.swing.JTabbedPane();
        categoryCategory = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        categoryPanel = new javax.swing.JLayeredPane();
        accessibilityToggle = new javax.swing.JCheckBox();
        commandsCategory = new javax.swing.JPanel();
        createPOIButton = new javax.swing.JButton();
        floorNumText = new javax.swing.JLabel();
        modeTopLabel = new javax.swing.JLabel();
        modeTopButton = new javax.swing.JButton();
        modeTopImage = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        poiInfoIconLabel.setText("image");

        poiInfoNameLabel.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        poiInfoNameLabel.setText("POI Name");

        poiInfoDescLabel.setText("<html>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce dapibus diam in tortor tristique tempor. Ut eget maximus sem, ac egestas erat. Suspendisse imperdiet nunc ut elit ornare, quis auctor neque lobortis. Integer tempus tempor tellus. Nulla facilisi. Quisque pulvinar condimentum nibh vitae malesuada. Nullam ornare congue ligula eu semper. Maecenas ornare faucibus ante ac consequat. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.</html>");
        poiInfoDescLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        poiInfoDescLabel.setMaximumSize(new java.awt.Dimension(79, 17));

        poiInfoEditButton.setText("Edit Info");
        poiInfoEditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                poiInfoEditButtonActionPerformed(evt);
            }
        });

        poiInfoCategoryLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        poiInfoCategoryLabel.setText("<html>Category<br>(x,y)</html>");

        favouritePOIButton.setText("☆ Add to Favourites");
        favouritePOIButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                favouritePOIButtonActionPerformed(evt);
            }
        });

        poiPositionEditButton.setText("Edit Position");
        poiPositionEditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                poiPositionEditButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout poiInfoPanelLayout = new javax.swing.GroupLayout(poiInfoPanel);
        poiInfoPanel.setLayout(poiInfoPanelLayout);
        poiInfoPanelLayout.setHorizontalGroup(
            poiInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, poiInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(poiInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(poiInfoDescLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, poiInfoPanelLayout.createSequentialGroup()
                        .addComponent(poiInfoIconLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(poiInfoNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(poiInfoCategoryLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, poiInfoPanelLayout.createSequentialGroup()
                        .addComponent(favouritePOIButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(poiInfoEditButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(poiPositionEditButton)))
                .addContainerGap())
        );
        poiInfoPanelLayout.setVerticalGroup(
            poiInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(poiInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(poiInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(poiInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(poiInfoIconLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(poiInfoNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(poiInfoCategoryLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(poiInfoDescLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(poiInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(poiInfoEditButton)
                    .addComponent(favouritePOIButton)
                    .addComponent(poiPositionEditButton))
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Western Campus Map Viewer > Building Name");
        setMinimumSize(new java.awt.Dimension(1280, 768));

        buildingNameText.setFont(new java.awt.Font("Segoe UI Semibold", 0, 36)); // NOI18N
        buildingNameText.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        buildingNameText.setText("Building Name");
        buildingNameText.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        floorSelector.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                floorSelectorStateChanged(evt);
            }
        });

        errorText.setFont(errorText.getFont().deriveFont(errorText.getFont().getStyle() | java.awt.Font.BOLD, 15));
        errorText.setForeground(new java.awt.Color(255, 0, 0));
        errorText.setText(" ");

        exitButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        exitButton.setText("Back to Building Selection");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        biPOIList.setHorizontalScrollBar(null);
        biPOIList.setRowHeaderView(null);
        biPOIList.setViewportView(null);

        javax.swing.GroupLayout biPOILayerLayout = new javax.swing.GroupLayout(biPOILayer);
        biPOILayer.setLayout(biPOILayerLayout);
        biPOILayerLayout.setHorizontalGroup(
            biPOILayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 336, Short.MAX_VALUE)
        );
        biPOILayerLayout.setVerticalGroup(
            biPOILayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 598, Short.MAX_VALUE)
        );

        biPOIList.setViewportView(biPOILayer);

        javax.swing.GroupLayout biPOICategoryLayout = new javax.swing.GroupLayout(biPOICategory);
        biPOICategory.setLayout(biPOICategoryLayout);
        biPOICategoryLayout.setHorizontalGroup(
            biPOICategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(biPOIList, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
        );
        biPOICategoryLayout.setVerticalGroup(
            biPOICategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(biPOICategoryLayout.createSequentialGroup()
                .addComponent(biPOIList, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );

        poiPanel.addTab("Built-In POIs", biPOICategory);

        favPOIList.setHorizontalScrollBar(null);

        favPOILayer.setPreferredSize(new java.awt.Dimension(336, 327));

        javax.swing.GroupLayout favPOILayerLayout = new javax.swing.GroupLayout(favPOILayer);
        favPOILayer.setLayout(favPOILayerLayout);
        favPOILayerLayout.setHorizontalGroup(
            favPOILayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 336, Short.MAX_VALUE)
        );
        favPOILayerLayout.setVerticalGroup(
            favPOILayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 327, Short.MAX_VALUE)
        );

        favPOIList.setViewportView(favPOILayer);

        javax.swing.GroupLayout favPOICategoryLayout = new javax.swing.GroupLayout(favPOICategory);
        favPOICategory.setLayout(favPOICategoryLayout);
        favPOICategoryLayout.setHorizontalGroup(
            favPOICategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(favPOIList, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
        );
        favPOICategoryLayout.setVerticalGroup(
            favPOICategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(favPOICategoryLayout.createSequentialGroup()
                .addComponent(favPOIList, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );

        poiPanel.addTab("Favourite POIs", favPOICategory);

        myPOIList.setHorizontalScrollBar(null);

        myPOILayer.setPreferredSize(new java.awt.Dimension(336, 327));

        javax.swing.GroupLayout myPOILayerLayout = new javax.swing.GroupLayout(myPOILayer);
        myPOILayer.setLayout(myPOILayerLayout);
        myPOILayerLayout.setHorizontalGroup(
            myPOILayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 336, Short.MAX_VALUE)
        );
        myPOILayerLayout.setVerticalGroup(
            myPOILayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 327, Short.MAX_VALUE)
        );

        myPOIList.setViewportView(myPOILayer);

        javax.swing.GroupLayout myPOICategoryLayout = new javax.swing.GroupLayout(myPOICategory);
        myPOICategory.setLayout(myPOICategoryLayout);
        myPOICategoryLayout.setHorizontalGroup(
            myPOICategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(myPOIList, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
        );
        myPOICategoryLayout.setVerticalGroup(
            myPOICategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(myPOICategoryLayout.createSequentialGroup()
                .addComponent(myPOIList, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );

        poiPanel.addTab("My POIs", myPOICategory);

        jLabel2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        jLabel2.setText("Click to toggle categories");

        javax.swing.GroupLayout categoryPanelLayout = new javax.swing.GroupLayout(categoryPanel);
        categoryPanel.setLayout(categoryPanelLayout);
        categoryPanelLayout.setHorizontalGroup(
            categoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        categoryPanelLayout.setVerticalGroup(
            categoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 302, Short.MAX_VALUE)
        );

        accessibilityToggle.setText("Toggle Accessibility");
        accessibilityToggle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accessibilityToggleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout categoryCategoryLayout = new javax.swing.GroupLayout(categoryCategory);
        categoryCategory.setLayout(categoryCategoryLayout);
        categoryCategoryLayout.setHorizontalGroup(
            categoryCategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(categoryCategoryLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(categoryCategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(categoryPanel)
                    .addGroup(categoryCategoryLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                        .addComponent(accessibilityToggle)
                        .addContainerGap())))
        );
        categoryCategoryLayout.setVerticalGroup(
            categoryCategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(categoryCategoryLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(categoryCategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(accessibilityToggle))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(categoryPanel))
        );

        filterPanel.addTab("Categories", categoryCategory);

        createPOIButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        createPOIButton.setText("Create a POI");
        createPOIButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createPOIButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout commandsCategoryLayout = new javax.swing.GroupLayout(commandsCategory);
        commandsCategory.setLayout(commandsCategoryLayout);
        commandsCategoryLayout.setHorizontalGroup(
            commandsCategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(commandsCategoryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(createPOIButton, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE))
        );
        commandsCategoryLayout.setVerticalGroup(
            commandsCategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(commandsCategoryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(createPOIButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(290, Short.MAX_VALUE))
        );

        filterPanel.addTab("Commands", commandsCategory);

        floorNumText.setFont(floorNumText.getFont().deriveFont(floorNumText.getFont().getStyle() & ~java.awt.Font.BOLD, 18));
        floorNumText.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        floorNumText.setText("Floor #");
        floorNumText.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        modeTopLabel.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        modeTopLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        modeTopLabel.setText("[Editing Position] Click on a new position for that POI.");
        modeTopLabel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 0, 0), 1, true));

        modeTopButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        modeTopButton.setText("Done");
        modeTopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modeTopButtonActionPerformed(evt);
            }
        });

        modeTopImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/pencil.png"))); // NOI18N

        jButton1.setText("Help");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(floorSelector)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(filterPanel)
                            .addComponent(poiPanel)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(exitButton)
                                .addGap(18, 18, 18)
                                .addComponent(modeTopImage)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(modeTopLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 530, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(modeTopButton))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addGap(514, 514, 514)
                                .addComponent(errorText)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 367, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(floorNumText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buildingNameText, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(buildingNameText))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(modeTopLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                                .addComponent(modeTopButton))
                            .addComponent(exitButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                            .addComponent(modeTopImage, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(errorText)
                        .addComponent(jButton1))
                    .addComponent(floorNumText))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(poiPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(filterPanel))
                    .addComponent(floorSelector, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Closes the POI viewer and re-opens the map selector.
     * @param evt Default Java event.
     */
    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        new MapSelectionForm(currentUser).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_exitButtonActionPerformed

    /**
     * Toggles the accessibility layer on every panel (which should exist).
     * @param evt Default Java event.
     */
    private void accessibilityToggleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accessibilityToggleActionPerformed
        // change accessibility layer
        for (int i = 0; i != mapImagesReference.size(); i++) {
            Floor fl = building.getFloor(i);
            if (accessibilityToggle.isSelected()) {
                // on
                mapImagesReference.get(i).setIcon(new javax.swing.ImageIcon("data/building/" + fileName + "/" + fl.getID() + "a.png"));
            } else {
                // off
                mapImagesReference.get(i).setIcon(new javax.swing.ImageIcon("data/building/" + fileName + "/" + fl.getID() + ".png"));
            }
        }

        //if (accessibilityToggle.isSelected()) accessibilityImage.setVisible(true);
        //else accessibilityImage.setVisible(false);
    }//GEN-LAST:event_accessibilityToggleActionPerformed
    
    /**
     * Refreshes the sidebar containing categories, POI data for in-built, favourited, and custom POIs.
     */
    private void refreshSidebar() {
        // I didn't even know you had to repaint() them because swing leaves artifacting... what a strange bug
        biPOILayer.removeAll();
        biPOILayer.repaint();
        favPOILayer.removeAll();
        favPOILayer.repaint();
        myPOILayer.removeAll();
        myPOILayer.repaint();
        categoryPanel.removeAll();
        categoryPanel.repaint();
        
        // get current floor and it's categories
        Floor currentFloor = building.getFloor(floorSelector.getSelectedIndex());
        HashMap<String, ArrayList> currentFloorCats = currentFloor.getPOIs();
        
        // get the user's data for this building, get the floor
        Floor userCurrentFloor = userFloors.get(floorSelector.getSelectedIndex());
        HashMap<String, ArrayList> currentUserCats = userCurrentFloor.getPOIs();
        
        ArrayList<String> categoryAdded = new ArrayList();

        // create sidebar labels for built in pois
        for (String cat : currentFloorCats.keySet()) {
            // create categories in the categoryPanel, if not already exists
            
            // i did not account for there being *too many* categories, so hopefully the dev or user doesn't create
            // a billion categories! (it will go off screen)
            if (!categoryAdded.contains(cat)) {
                categoryAdded.add(cat);
                categoryPanel.add(createCategoryToggle(cat, categoryPanel.getComponentCount() * 25));
            }
            
            // get category's POIs and POI labels for the Sidebar
            ArrayList<POI> currentFloorPOIs = currentFloorCats.get(cat);

            for (int i = 0; i != currentFloorPOIs.size(); i++) {
                POI poiEntry = currentFloorPOIs.get(i);
                
                // make visible if hidden early by category changes
                poiReference.get(poiEntry).setVisible(true);
                
                biPOILayer.add(createSidebarLabel(poiEntry, biPOILayer.getComponentCount() * 38));
                
                // add as well to favourites if favourited
                if (poiEntry.isFavourited()) {
                    favPOILayer.add(createSidebarLabel(poiEntry, favPOILayer.getComponentCount() * 38));
                }
            }
        }
        
        // fix scrollbar size for built in and favourited poi list
        biPOILayer.setPreferredSize(new Dimension(348, biPOILayer.getComponentCount() * 38));
        favPOILayer.setPreferredSize(new Dimension(348, favPOILayer.getComponentCount() * 38));
        
        // generate sidebar labels for user pois
        for (String cat : currentUserCats.keySet()) {
            // create categories in the categoryPanel, if not already exists
            if (!categoryAdded.contains(cat)) {
                categoryAdded.add(cat);
                categoryPanel.add(createCategoryToggle(cat, categoryPanel.getComponentCount() * 25));
            }
            
            // get user category's POIs and POI labels for the Sidebar
            ArrayList<POI> currentUserPOIs = currentUserCats.get(cat);

            for (int i = 0; i != currentUserPOIs.size(); i++) {
                POI poiEntry = currentUserPOIs.get(i);
                
                // make visible if hidden early by category changes
                if (poiReference.containsKey(poiEntry)) {
                    poiReference.get(poiEntry).setVisible(true);
                    myPOILayer.add(createSidebarLabel(poiEntry, myPOILayer.getComponentCount() * 38));
                }
            }
        }
        
        // fix scrollbar size for custom poi list
        myPOILayer.setPreferredSize(new Dimension(348, myPOILayer.getComponentCount() * 38));
    }
    
    /**
     * Creates a JCheckBox category toggle and returns it.
     * @param cat The category string.
     * @param catposY The Y coordinate for where the category should go.
     * @return Returns a JCheckBox object with the category toggle.
     */
    private javax.swing.JCheckBox createCategoryToggle(String cat, int catposY) {
        javax.swing.JCheckBox newCategoryToggle = new javax.swing.JCheckBox(cat);
        newCategoryToggle.setBounds(0, catposY, 319, 21);
        newCategoryToggle.setSelected(true); // for category filtering

        // on category toggle
        newCategoryToggle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // for every single POI
                for (POI i : poiReference.keySet()) {
                    // toggle the GUI element if toggled and category matches
                    if (i.getCategory().equals(cat)) {
                        if (newCategoryToggle.isSelected()) {
                            poiReference.get(i).setVisible(true);
                        } else {
                            poiReference.get(i).setVisible(false);
                        }
                    }
                }
            }
        });

        return newCategoryToggle;
    }
    
    /**
     * Creates a JPanel label for the POI data and returns it.
     * @param poi The POI data.
     * @param y The Y coordinate for where the POI should go.
     * @return Returns a JPanel object visually containing the POI data.
     */
    private JPanel createSidebarLabel(POI poi, int y) {
        // create Built-In POI entry for each POI
        JPanel poiEntry = new javax.swing.JPanel();
        poiEntry.setLayout(null);
        
        poiEntry.setBounds(0, y, 350, 38);

        JLabel poiImageLabel = new javax.swing.JLabel(new javax.swing.ImageIcon("data/icon/" + poi.getIcon() + ".png"));
        poiImageLabel.setBounds(5, 0, 35, 38);
        poiEntry.add(poiImageLabel);

        JLabel poiNameLabel = new javax.swing.JLabel(poi.getName());
        poiNameLabel.setBounds(45, 0, 200, 38);
        poiEntry.add(poiNameLabel);

        JLabel poiInfoLabel = new javax.swing.JLabel(poi.getCategory() + " (" + poi.getX() + ", " + poi.getY() + ")");
        poiInfoLabel.setBounds(135, 0, 180, 38);
        poiInfoLabel.setHorizontalAlignment(javax.swing.JLabel.RIGHT);
        poiEntry.add(poiInfoLabel);
        
        // add mouse click event, maybe mouse hover? for JLabel
        poiEntry.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // Don't allow pop up if in any position selector mode
                if (editorMode.equals("none")) {
                    selectedPOI = poi;
                    setPOIInfoBox();
                    
                    // scroll to poi if you click on the sidebar labels
                    javax.swing.JScrollPane scrollPage = floorScrollPanelReference.get(floorSelector.getSelectedIndex());
                    scrollPage.getVerticalScrollBar().setValue(poi.getY() - scrollPage.getSize().height/2);
                    scrollPage.getHorizontalScrollBar().setValue(poi.getX() - scrollPage.getSize().width/2);
                }
            }
        });
        
        return poiEntry;
    }
    
    /**
     * Changes the sidebar information and floor text when the floor currently selected has changed.
     * @param evt Default Java event.
     */
    private void floorSelectorStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_floorSelectorStateChanged
        floorNumText.setText(building.getFloor(floorSelector.getSelectedIndex()).getName());
        refreshSidebar();
    }//GEN-LAST:event_floorSelectorStateChanged

    /**
     * Set the program's mode to Add POI when the Create New POI button is clicked.
     * @param evt Default Java event. 
     */
    private void createPOIButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createPOIButtonActionPerformed
        // set the mode text and show it
        selectedPOI = null;
        poiInfoPanel.setVisible(false);
        modeTopLabel.setText("[Creating POI] Click on a position to create a POI.");
        modeTopLabel.setVisible(true);
        modeTopButton.setText("Done");
        modeTopButton.setVisible(true);
        modeTopImage.setVisible(true);
        editorMode = "add";
    }//GEN-LAST:event_createPOIButtonActionPerformed

    /**
     * Set the program's mode to Edit POI when the Edit Info button is clicked.
     * @param evt Default Java event. 
     */
    private void poiInfoEditButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_poiInfoEditButtonActionPerformed
        // Don't open the Add/Edit POI dialog if it's already open
        javax.swing.JFrame newEditor = new POIEditor(selectedPOI);
        newEditor.setVisible(true);
        
        // prevent interaction until dialog box is closed
        window.setEnabled(false);

        // create event for when the window was closed
        newEditor.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent ev) {
                window.setEnabled(true);
                
                if (selectedPOI != null) {
                    // POI was not deleted
                    if (selectedPOI.getName() != null) {
                        // Move the POI info box and update it's info
                        setPOIInfoBox();
                        poiReference.get(selectedPOI).setIcon(new ImageIcon("data/icon/" + selectedPOI.getIcon() + ".png"));
                    }
                    // POI was deleted, remove label, references, andd POI from floor listing
                    else {
                        building.getFloor(floorSelector.getSelectedIndex()).removePOI(selectedPOI);
                        currentFloorLayerPanel.remove(poiReference.get(selectedPOI));
                        currentFloorLayerPanel.repaint();
                        poiReference.remove(selectedPOI);
                        poiInfoPanel.setVisible(false);
                        selectedPOI = null;
                    }
                    
                    // force update the hashmap and write it to file
                    if (currentUser.isDeveloper()) {
                        building.getFloor(floorSelector.getSelectedIndex()).updateMap();
                        building.writeToFile();
                    }
                    else {
                        userFloors.get(floorSelector.getSelectedIndex()).updateMap();
                        currentUser.writeToFile();
                    }
                    
                    refreshSidebar();
                }
            }
        });
    }//GEN-LAST:event_poiInfoEditButtonActionPerformed

    /**
     * Favourite or unfavourite the POI, refresh the sidebar and save to the user's data file.
     * @param evt Default Java event. 
     */
    private void favouritePOIButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_favouritePOIButtonActionPerformed
        // if it's favourited, unfavourite
        if (selectedPOI.isFavourited()) {
            favouritePOIButton.setText("☆ Add to Favourites");
            userFavourites.get(floorSelector.getSelectedIndex()).removePOI(selectedPOI);
            selectedPOI.setFavourited(false);
        }
        // otherwise favourite it
        else {
            favouritePOIButton.setText("★ Remove from Favourites");
            userFavourites.get(floorSelector.getSelectedIndex()).addPOI(selectedPOI);
            selectedPOI.setFavourited(true);
        }
        
        // refresh
        refreshSidebar();
        currentUser.writeToFile();
    }//GEN-LAST:event_favouritePOIButtonActionPerformed

    /**
     * Set the program's mode to Edit POI Position when the Edit Position button is clicked.
     * @param evt Default Java event. 
     */
    private void poiPositionEditButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_poiPositionEditButtonActionPerformed
        // set the mode header to edit and show it
        modeTopLabel.setText("[Editing POI] Click on a new position for " + selectedPOI.getName() + ".");
        modeTopLabel.setVisible(true);
        modeTopButton.setText("Done");
        modeTopButton.setVisible(true);
        modeTopImage.setVisible(true);
        editorMode = "edit";
    }//GEN-LAST:event_poiPositionEditButtonActionPerformed

    /**
     * Set the program's mode to the default mode and write any changes to the user or building data file.
     * @param evt Default Java event. 
     */
    private void modeTopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modeTopButtonActionPerformed
       // close the top mode header
        modeTopLabel.setVisible(false);
        modeTopButton.setVisible(false);
        modeTopImage.setVisible(false);
        editorMode = "none";
        
        if (currentUser.isDeveloper()) building.writeToFile();
        else currentUser.writeToFile();
    }//GEN-LAST:event_modeTopButtonActionPerformed

    /**
     * Open up the help guide.
     * @param evt Default Java event. 
     */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        new HelpPage().setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(MapViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MapViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MapViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MapViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new MapViewer("uc", null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox accessibilityToggle;
    private javax.swing.JPanel biPOICategory;
    private javax.swing.JLayeredPane biPOILayer;
    private javax.swing.JScrollPane biPOIList;
    private javax.swing.JLabel buildingNameText;
    private javax.swing.JPanel categoryCategory;
    private javax.swing.JLayeredPane categoryPanel;
    private javax.swing.JPanel commandsCategory;
    private javax.swing.JButton createPOIButton;
    private javax.swing.JLabel errorText;
    private javax.swing.JButton exitButton;
    private javax.swing.JPanel favPOICategory;
    private javax.swing.JLayeredPane favPOILayer;
    private javax.swing.JScrollPane favPOIList;
    private javax.swing.JButton favouritePOIButton;
    private javax.swing.JTabbedPane filterPanel;
    private javax.swing.JLabel floorNumText;
    private javax.swing.JTabbedPane floorSelector;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton modeTopButton;
    private javax.swing.JLabel modeTopImage;
    private javax.swing.JLabel modeTopLabel;
    private javax.swing.JPanel myPOICategory;
    private javax.swing.JLayeredPane myPOILayer;
    private javax.swing.JScrollPane myPOIList;
    private javax.swing.JLabel poiInfoCategoryLabel;
    private javax.swing.JLabel poiInfoDescLabel;
    private javax.swing.JButton poiInfoEditButton;
    private javax.swing.JLabel poiInfoIconLabel;
    private javax.swing.JLabel poiInfoNameLabel;
    private javax.swing.JPanel poiInfoPanel;
    private javax.swing.JTabbedPane poiPanel;
    private javax.swing.JButton poiPositionEditButton;
    // End of variables declaration//GEN-END:variables
}