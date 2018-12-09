/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.jfoenix.controls.JFXListView;
import db.koneksi;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import model.Project;
import model.ProjectDao;

/**
 * FXML Controller class
 *
 * @author kuupie
 */
public class PROJECTController implements Initializable {

    @FXML
    private JFXListView<Project> rootViewList;
    
    private String idPro="", civitas="", startMonth="", endMonth="";
    private static final String RED_BAR    = "red-bar";
    private static final String YELLOW_BAR = "yellow-bar";
    private static final String ORANGE_BAR = "orange-bar";
    private static final String GREEN_BAR  = "green-bar";
    
    koneksi kon = new koneksi();
    ProjectDao dao =new ProjectDao();
    PROJECTdetailController detail = new PROJECTdetailController();
    
    private ObservableList<Project> data;
    @FXML
    private BorderPane viewProject;
    
    private void loadListProject(){
        try {
            data=FXCollections.observableArrayList();
            kon.res=kon.stat.executeQuery(dao.queryListProject);
            
            while (kon.res.next()) {                
                data.add(new Project(kon.res.getString(1), kon.res.getString(2), kon.res.getString(3), 
                        kon.res.getString(4), kon.res.getString(5), kon.res.getString(6), 
                        kon.res.getString(7), kon.res.getString(8)));
            }
            rootViewList.setItems(data);
            rootViewList.setCellFactory(projectListView -> new ProjectDao());
        } catch (Exception e) {
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        kon.db();
        loadListProject();
        
        //rootViewList.setExpanded(Boolean.TRUE);
    }    

    @FXML
    private void loadDetailProject(MouseEvent event) throws IOException, SQLException {
        idPro = rootViewList.getSelectionModel().getSelectedItem().getIdActivity();
//        civitas = rootViewList.getSelectionModel().getSelectedItem().getCivitasCol();
//        startMonth = rootViewList.getSelectionModel().getSelectedItem().getStartmonth();
//        endMonth = rootViewList.getSelectionModel().getSelectedItem().getEndmonth();
        openDetail();
    }

    private void openDetail() throws IOException, SQLException {
        viewProject.setCenter(null);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PROJECTdetail.fxml"));
        BorderPane newScene = loader.load();
        PROJECTdetailController mct = loader.getController();
        mct.setData(idPro);
        viewProject.setCenter(newScene);
    }
    
    
}
