package de.hszg.datenpannen.virus.model.charts;

import de.hszg.datenpannen.virus.model.ExternalConsequence;
import de.hszg.datenpannen.virus.model.VirusResult;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class ProductivityLossChartModel implements PieChartModel {

    private StringProperty title = new SimpleStringProperty();
    private ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
    @Inject
    private VirusResult result;

    @PostConstruct
    void initialize() {
        title.set("Intern");

        for (ExternalConsequence externalConsequence : ExternalConsequence.values()) {
            PieChart.Data element = new PieChart.Data(externalConsequence.toString(), 0);
            element.pieValueProperty().bind(result.getAvgExternalConsequenceCost(externalConsequence));
            element.nameProperty().bind(Bindings.format("%s%n%,.0f \u20AC",
                    externalConsequence,
                    result.getAvgExternalConsequenceCost(externalConsequence)));
            data.add(element);
        }
    }

    @Override
    public ReadOnlyStringProperty title() {
        return title;
    }

    @Override
    public ObservableList<PieChart.Data> data() {
        return data;
    }
}
