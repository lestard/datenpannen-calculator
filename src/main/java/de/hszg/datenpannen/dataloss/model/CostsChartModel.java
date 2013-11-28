package de.hszg.datenpannen.dataloss.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import static de.hszg.datenpannen.utils.BindingHelper.*;

/**
 * DataModel für das Kosten-Line-Chart.
 */
public class CostsChartModel {

    private StringProperty title = new SimpleStringProperty();

    private XYChart.Series avgSeries = new XYChart.Series<>();
    private XYChart.Series minSeries = new XYChart.Series<>();
    private XYChart.Series maxSeries = new XYChart.Series<>();

    private XYChart.Series selectionSeries = new XYChart.Series<>();

    @Inject
    private DatalossResult result;

    @Inject
    private UserinputModel userinputModel;

    CostsChartModel(DatalossResult result, UserinputModel userinputModel){
        this.result = result;
        this.userinputModel = userinputModel;
    }

    public CostsChartModel(){
    }

    @PostConstruct
    void initialize(){
        this.title.set("Kosten");

        avgSeries.setName("Durchschnitt");
        minSeries.setName("Min");
        maxSeries.setName("Max");
        selectionSeries.setName("Anzahl verlorener Datensätze");

        avgSeriesData().add(new XYChart.Data<>(0,0.0));
        minSeriesData().add(new XYChart.Data<>(0,0.0));
        maxSeriesData().add(new XYChart.Data<>(0,0.0));



        XYChart.Data<Integer,Double> avgTotal = new XYChart.Data<>();
        avgTotal.XValueProperty().bind(asObjectBinding(userinputModel.numberOfDatasets()));
        avgTotal.YValueProperty().bind(asObjectBinding(result.avgCostTotal()));
        avgSeriesData().add(avgTotal);

        XYChart.Data<Integer,Double> minTotal = new XYChart.Data<>();
        minTotal.XValueProperty().bind(asObjectBinding(userinputModel.numberOfDatasets()));
        minTotal.YValueProperty().bind(asObjectBinding(result.minCostTotal()));
        minSeriesData().add(minTotal);

        XYChart.Data<Integer,Double> maxTotal = new XYChart.Data<>();
        maxTotal.XValueProperty().bind(asObjectBinding(userinputModel.numberOfDatasets()));
        maxTotal.YValueProperty().bind(asObjectBinding(result.maxCostTotal()));
        maxSeriesData().add(maxTotal);


        XYChart.Data<Integer,Double> selectionStart = new XYChart.Data<>();
        selectionStart.XValueProperty().bind(asObjectBinding(userinputModel.numberOfLostDatasets()));
        selectionStart.YValueProperty().set(0d);

        XYChart.Data<Integer,Double> selectionEnd = new XYChart.Data<>();
        selectionEnd.XValueProperty().bind(asObjectBinding(userinputModel.numberOfLostDatasets()));
        selectionEnd.YValueProperty().bind(asObjectBinding(result.maxCostTotal()));

        selectionSeries.getData().add(selectionStart);
        selectionSeries.getData().add(selectionEnd);

    }

    public StringProperty title(){
        return title;
    }

    public ObservableList<XYChart.Data<Integer,Double>> avgSeriesData(){
        return avgSeries.getData();
    }

    public ObservableList<XYChart.Data<Integer,Double>> minSeriesData(){
        return minSeries.getData();
    }

    public ObservableList<XYChart.Data<Integer,Double>> maxSeriesData(){
        return maxSeries.getData();
    }


    public XYChart.Series<Integer,Double> avgSeries(){
        return avgSeries;
    }

    public XYChart.Series<Integer,Double> minSeries(){
        return minSeries;
    }

    public XYChart.Series<Integer,Double> maxSeries(){
        return maxSeries;
    }

    public XYChart.Series<Integer,Double> selectionSeries(){
        return selectionSeries;
    }
}
