/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.jfoenix.controls.JFXListView;
import db.koneksi;
import java.awt.Color;
import java.awt.Dimension;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import model.ProjectDetail;
import model.ProjectDetailDao;
import model.Scope;
import model.ScopeDao;
import model.TaskForm;
import model.TaskFormDao;
import model.Team;
import model.TeamDao;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;



/**
 * FXML Controller class
 *
 * @author kuupie
 */
public class PROJECTdetailController implements Initializable {
    
    
    private String idPro, textCivitas, textActivity, textStratMonth, textEndMonth, datestartlbl, dateendLbl;
    public String idPorject, taskPlann;
    int dateStart, dateEnd, yearStart, monthStart, monthEnd, yearEnd;
    int dateActStart, dateActEnd, monthActStart, monthActEnd,yearActStart,yearActEnd;
    
    
    @FXML
    private Label lblStartMonth;
    @FXML
    private Label lblEndMonth;
    @FXML
    private JFXListView<Scope> listScope;
    @FXML
    private JFXListView<Team> listTeam;
    @FXML
    private Label txtStartMonth;
    @FXML
    private Label txtEndMonth;
    @FXML
    private Label txtTglStart;
    @FXML
    private Label txtTglEnd;
    @FXML
    private Label txtCount;
    @FXML
    private ProgressBar barOu;
    @FXML
    private ProgressBar barField;
    @FXML
    private ProgressBar barIara;
    @FXML
    private ProgressBar barFinalization;
    @FXML
    private Label txtPcrPreliminary;
    @FXML
    private Label txtPcrOu;
    @FXML
    private Label txtPcrIARA;
    @FXML
    private Label txtPcrField;
    @FXML
    private Label txtPcrFinalization;
    @FXML
    private ProgressBar barPreliminary;
    @FXML
    private Label lblProjectCivitas111;
    @FXML
    private Label lblActivity;
    @FXML
    private Label lblProjectCivitas;
    @FXML
    private ScrollPane paneView;
    
    //Koneki
    koneksi kon = new koneksi();
    ProjectDetailDao model = new ProjectDetailDao();
    ScopeDao modelScope = new ScopeDao();
    TeamDao modelTeam = new TeamDao();
    TaskFormDao modelTask = new TaskFormDao();
    
   
    private ObservableList<ProjectDetail> data;
    private ObservableList<Scope>dataScope;
    private ObservableList<Team>dataTeam;
    private ObservableList<TaskForm>dataTaskForm;
    

    
    public void setData (String idProject) throws SQLException{
        kon.db();
        idPorject = idProject;
        
        //System.out.println(idPorject);
        
        setScope(idProject);
        setTeamMember(idProject);
        //getCategoryDataset(idProject);
        loadDataTask();
        
        
        model.loadDetail(idProject);
        
        data=FXCollections.observableArrayList();
        kon.res=kon.stat.executeQuery(model.queryload);
        
        while (kon.res.next()) {                
                data.add(new ProjectDetail(kon.res.getString(1),kon.res.getString(2),kon.res.getString(3),
                kon.res.getString(4),kon.res.getString(5),kon.res.getString(6),kon.res.getString(7)));
                textCivitas = kon.res.getString(2);
                textActivity = kon.res.getString(3);
                textStratMonth = kon.res.getString(4);
                datestartlbl = kon.res.getString(5);
                textEndMonth = kon.res.getString(6);
                dateendLbl = kon.res.getString(7);
                
            }
                
                //label atas
                lblProjectCivitas.setText(textCivitas);
                lblActivity.setText(textActivity);
                lblStartMonth.setText(textStratMonth);
                lblEndMonth.setText(textEndMonth);
                
                //label bawah
                txtStartMonth.setText(textStratMonth);
                txtTglStart.setText(datestartlbl);
                txtEndMonth.setText(textEndMonth);
                txtTglEnd.setText(dateendLbl);
                
    }
    
    public void setScope(String idProject) throws SQLException{
        modelScope.loadScope(idProject);
        dataScope =FXCollections.observableArrayList();
        kon.res=kon.stat.executeQuery(modelScope.scopequery);
        while (kon.res.next()) {                
                dataScope.add(new Scope(kon.res.getString(1),kon.res.getString(2),kon.res.getString(3)));
            }
            listScope.setItems(dataScope);
            listScope.setCellFactory(scopeListView -> new ScopeDao());
    }
    
    public void setTeamMember(String idProject) throws SQLException{
        modelTeam.teamLoad(idProject);
        dataTeam =FXCollections.observableArrayList();
        kon.res=kon.stat.executeQuery(modelTeam.queryteam);
        while (kon.res.next()) {                
                dataTeam.add(new Team(kon.res.getString(1),kon.res.getString(2)));
            }
            listTeam.setItems(dataTeam);
            listTeam.setCellFactory(teamListView -> new TeamDao());
    }
    
    private static Date date(final int day, final int month, final int year) {

        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        final Date result = calendar.getTime();
        return result;

    }    
    
     private void loadDataTask() throws SQLException{
      kon.db();
      
      
      //get data chart from above
      IntervalCategoryDataset dataset = getCategoryDataset(idPorject);
        JFreeChart chart = ChartFactory.createGanttChart("", "", "", dataset);
        
        //Style plot
        chart.getPlot().setBackgroundPaint(new Color(246, 246, 246));
        chart.getPlot().setBackgroundAlpha(0.9f);
        chart.getPlot().setOutlineVisible(false);
        chart.getPlot().setForegroundAlpha(0.6f);
        chart.getPlot().setNoDataMessage("No Data");
        
        

        //style chart
        chart.setBackgroundPaint(new Color (241,241,241));
        //chart.getXYPlot().setWeight(yearEnd);
        
        
        
        
        //load chart
        SwingNode chartSwingNode = new SwingNode();
        ChartPanel jchaChartPanel = new ChartPanel(chart);
        jchaChartPanel.setFillZoomRectangle(true);
        //jchaChartPanel.setMouseWheelEnabled(true);
        jchaChartPanel.setAutoscrolls(true);
        jchaChartPanel.getAutoscrolls();
        jchaChartPanel.setPreferredSize(new Dimension(745,700));
        chartSwingNode.setContent(jchaChartPanel); 
        paneView.setContent(chartSwingNode);
    }
    
    private IntervalCategoryDataset getCategoryDataset(String idProject) throws SQLException{
        modelTask.loadData(idProject);
        dataTaskForm=FXCollections.observableArrayList();
        kon.res=kon.stat.executeQuery(modelTask.queryload);
        
        final TaskSeries s1 = new TaskSeries("Scheduled");
        final TaskSeries s2 = new TaskSeries("Actual");
        while (kon.res.next()) {   
            try {
                dataTaskForm.add(new TaskForm(kon.res.getString(1), kon.res.getString(2), 
                kon.res.getInt(3),kon.res.getInt(4),kon.res.getInt(5),kon.res.getInt(6),kon.res.getInt(7),
                kon.res.getInt(8),kon.res.getInt(9),kon.res.getInt(10),kon.res.getInt(11),kon.res.getInt(12)
                ,kon.res.getInt(13),kon.res.getInt(14)));
                
                taskPlann=kon.res.getString(2);
                dateStart=kon.res.getInt(3);
                monthStart=kon.res.getInt(4);
                yearStart=kon.res.getInt(5);
                dateEnd=kon.res.getInt(6);
                monthEnd=kon.res.getInt(7);
                yearEnd=kon.res.getInt(8);
                
                s1.add(new Task(taskPlann, Date.from(LocalDate.of(yearStart, monthStart, dateStart).atStartOfDay().toInstant(ZoneOffset.UTC)),
                Date.from(LocalDate.of(yearEnd, monthEnd, dateEnd).atStartOfDay().toInstant(ZoneOffset.UTC))));

                dateActStart=kon.res.getInt(9);
                monthActStart=kon.res.getInt(10);
                yearActStart=kon.res.getInt(11);
                dateActEnd=kon.res.getInt(12);
                monthActEnd=kon.res.getInt(13);
                yearActEnd=kon.res.getInt(14);
                
                if(dateActStart!=0 ){
                    if(dateActEnd==0){
                        
                    } else {
                        s2.add(new Task(taskPlann, Date.from(LocalDate.of(yearActStart, monthActStart, dateActStart).atStartOfDay().toInstant(ZoneOffset.UTC)),
                        Date.from(LocalDate.of(yearActEnd, monthActEnd, dateActEnd).atStartOfDay().toInstant(ZoneOffset.UTC))));
                    }
                    
                }
                
            } catch (Exception e) {
                
            }

        }
        
        final TaskSeriesCollection collection = new TaskSeriesCollection();
        collection.add(s1);
        collection.add(s2);

        return collection;
    }
    
   
    
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        kon.db();
        try {
            loadDataTask();
        } catch (SQLException ex) {
            Logger.getLogger(PROJECTdetailController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    
    
}
