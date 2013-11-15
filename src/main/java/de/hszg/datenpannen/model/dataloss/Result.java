package de.hszg.datenpannen.model.dataloss;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.EnumMap;
import java.util.Map;

/**
 * DataModel für die Ergebnisdaten.
 */
public class Result {

    /**
     * Die tatsächlichen (d.h. für den Industriezweig) Durchschnittskosten pro
     * Datensatz.
     */
    private DoubleProperty avgCostPerDataset = new SimpleDoubleProperty();
    /**
     * Die tatsächlichen (d.h. für den Industriezweig) Minimalen Kosten pro
     * Datensatz.
     */
    private DoubleProperty minCostPerDataset = new SimpleDoubleProperty();
    /**
     * Die tatsächlichen (d.h. für den Industriezweig) Maximalen Kosten pro
     * Datensatz.
     */
    private DoubleProperty maxCostPerDataset = new SimpleDoubleProperty();
    /**
     * Die gesamten Durchschnittskosten über alle Datensätze.
     */
    private DoubleProperty avgCostTotal = new SimpleDoubleProperty();
    /**
     * Die gesamten Minimalen Kosten über alle Datensätze.
     */
    private DoubleProperty minCostTotal = new SimpleDoubleProperty();
    /**
     * Die gesamten Maximalen Kosten über alle Datensätze.
     */
    private DoubleProperty maxCostTotal = new SimpleDoubleProperty();
    /**
     * Die Aufteilung der Durchschnittlichen Kosten.
     */
    private MapProperty<CostDistribution, DoubleProperty> avgDistributionCosts = new SimpleMapProperty<>();
    /**
     * Die Aufteilung der Minimalen Kosten.
     */
    private MapProperty<CostDistribution, DoubleProperty> minDistributionCosts = new SimpleMapProperty<>();
    /**
     * Die Aufteilung der Maximalen Kosten.
     */
    private MapProperty<CostDistribution, DoubleProperty> maxDistributionCosts = new SimpleMapProperty<>();
    @Inject
    private BaseDataModel baseDataModel;
    @Inject
    private UserinputModel userinputModel;

    public Result() {
        avgDistributionCosts.set(FXCollections.observableMap(createEmptyEnumMap(CostDistribution.class)));
        minDistributionCosts.set(FXCollections.observableMap(createEmptyEnumMap(CostDistribution.class)));
        maxDistributionCosts.set(FXCollections.observableMap(createEmptyEnumMap(CostDistribution.class)));
    }

    /**
     * Der Parametrisierte Konstruktor ist zum Testen der Klasse gedacht.
     * Darüber können die Abhängigkeiten direkt injeziert werden.
     * <p/>
     * Im Realen Betrieb wird dies durch das Dependency-Injection-Framework
     * übernommen.
     */
    protected Result(BaseDataModel baseDataModel, UserinputModel userinputModel) {
        this();
        this.baseDataModel = baseDataModel;
        this.userinputModel = userinputModel;

        initialize();
    }

    /**
     * Stellt sämtliche Bindings her. Diese Methode wird dank der
     * {@link PostConstruct} Annotation durch das DI-Framework nach
     * Initialisierung der Klasse aufgerufen.
     */
    @PostConstruct
    protected void initialize() {
        IntegerBinding lossCostPerSector = Bindings.integerValueAt(baseDataModel.lossCostPerSector(), userinputModel.sector());

        DoubleBinding sumOfInfluencingFactors = new DoubleBinding() {
            {
                bind(baseDataModel.factorValues(), userinputModel.influencingFactors());
            }

            @Override
            protected double computeValue() {
                double sum = 0;
                for (InfluencingFactor factor : userinputModel.influencingFactors()) {
                    sum += baseDataModel.factorValues().get(factor);
                }
                return sum;
            }
        };

        avgCostPerDataset.bind(lossCostPerSector.add(sumOfInfluencingFactors));

        // Der Faktor, mit dem Min-Werte ausgehend von AVG berechnet werden.
        DoubleBinding minFactor = baseDataModel.minLossCost().divide(baseDataModel.avgLossCost());

        // Faktor, mit dem Max-Werte ausgehend von AVG berechnet werden.
        DoubleBinding maxFactor = baseDataModel.maxLossCost().divide(baseDataModel.avgLossCost());

        minCostPerDataset.bind(minFactor.multiply(lossCostPerSector).add(sumOfInfluencingFactors));
        maxCostPerDataset.bind(maxFactor.multiply(lossCostPerSector).add(sumOfInfluencingFactors));


        avgCostTotal.bind(avgCostPerDataset.multiply(userinputModel.numberOfDatasets()));
        minCostTotal.bind(minCostPerDataset.multiply(userinputModel.numberOfDatasets()));
        maxCostTotal.bind(maxCostPerDataset.multiply(userinputModel.numberOfDatasets()));


        initDistributionCostBinding(avgDistributionCosts, avgCostPerDataset);
        initDistributionCostBinding(minDistributionCosts, minCostPerDataset);
        initDistributionCostBinding(maxDistributionCosts, maxCostPerDataset);

    }

    /**
     * Erzeugt für sämtliche DoubleProperty-Values der Map ein Binding, welches
     * die angegebenen "costPerDataset" (je avg,min, max) mit dem dazugehörigen
     * Faktor (aus {@link BaseDataModel} multipliziert.
     */
    private void initDistributionCostBinding(Map<CostDistribution, DoubleProperty> map, DoubleProperty costPerDataset) {
        for (Map.Entry<CostDistribution, DoubleProperty> entry : map.entrySet()) {
            CostDistribution key = entry.getKey();
            DoubleProperty value = entry.getValue();

            ReadOnlyDoubleProperty property = new SimpleDoubleProperty(
                    baseDataModel.costDistributions().get(key));

            value.bind(costPerDataset.multiply(property));
        }
    }

    /**
     * Erzeugt eine EnumMap für den angegebenen Enum-Typ. Die Map wird mit
     * SimpleDoubleProperty-Instanzen gefüllt.
     */
    private <T extends Enum<T>> Map<T, DoubleProperty> createEmptyEnumMap(Class<T> enumType) {
        final Map<T, DoubleProperty> map = new EnumMap<T, DoubleProperty>(enumType);
        for (T enumConstant : enumType.getEnumConstants()) {
            map.put(enumConstant, new SimpleDoubleProperty(0));
        }
        return map;
    }

    public ReadOnlyDoubleProperty getMinDistributionCost(CostDistribution distribution) {
        return minDistributionCosts.get(distribution);
    }

    public ReadOnlyDoubleProperty getAvgDistributionCost(CostDistribution distribution) {
        return avgDistributionCosts.get(distribution);
    }

    public ReadOnlyDoubleProperty getMaxDistributionCost(CostDistribution distribution) {
        return maxDistributionCosts.get(distribution);
    }

    public ReadOnlyDoubleProperty avgCostPerDataset() {
        return avgCostPerDataset;
    }

    public ReadOnlyDoubleProperty minCostPerDataset() {
        return minCostPerDataset;
    }

    public ReadOnlyDoubleProperty maxCostPerDataset() {
        return maxCostPerDataset;
    }

    public ReadOnlyDoubleProperty avgCostTotal() {
        return avgCostTotal;
    }

    public ReadOnlyDoubleProperty minCostTotal() {
        return minCostTotal;
    }

    public ReadOnlyDoubleProperty maxCostTotal() {
        return maxCostTotal;
    }
}
