package de.hszg.datenpannen.virus.model;

import de.hszg.datenpannen.utils.MathBindings;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Map;


import static de.hszg.datenpannen.utils.Helper.createEmptyEnumMap;

public class VirusResult {

    private DoubleProperty avgCostPerClient = new SimpleDoubleProperty();
    private DoubleProperty minCostPerClient = new SimpleDoubleProperty();
    private DoubleProperty maxCostPerClient = new SimpleDoubleProperty();
    private DoubleProperty avgCostTotal = new SimpleDoubleProperty();
    private DoubleProperty minCostTotal = new SimpleDoubleProperty();
    private DoubleProperty maxCostTotal = new SimpleDoubleProperty();
    private DoubleProperty avgCostSelected = new SimpleDoubleProperty();
    private DoubleProperty minCostSelected = new SimpleDoubleProperty();
    private DoubleProperty maxCostSelected = new SimpleDoubleProperty();

    private MapProperty<CostComponent,DoubleProperty> avgCostComponentCosts = new SimpleMapProperty<>();
    private MapProperty<CostComponent,DoubleProperty> minCostComponentCosts = new SimpleMapProperty<>();
    private MapProperty<CostComponent,DoubleProperty> maxCostComponentCosts = new SimpleMapProperty<>();


    private MapProperty<InternalActivity,DoubleProperty> avgInternalActivityCosts = new SimpleMapProperty<>();
    private MapProperty<InternalActivity,DoubleProperty> minInternalActivityCosts = new SimpleMapProperty<>();
    private MapProperty<InternalActivity,DoubleProperty> maxInternalActivityCosts = new SimpleMapProperty<>();

    private MapProperty<ExternalConsequence,DoubleProperty> avgExternalConsequenceCosts = new SimpleMapProperty<>();
    private MapProperty<ExternalConsequence,DoubleProperty> minExternalConsequenceCosts = new SimpleMapProperty<>();
    private MapProperty<ExternalConsequence,DoubleProperty> maxExternalConsequenceCosts = new SimpleMapProperty<>();

    private MapProperty<AttackType,DoubleProperty> avgAttackTypeCosts = new SimpleMapProperty<>();
    private MapProperty<AttackType,DoubleProperty> minAttackTypeCosts = new SimpleMapProperty<>();
    private MapProperty<AttackType,DoubleProperty> maxAttackTypeCosts = new SimpleMapProperty<>();

    @Inject
    private BaseDataModel baseDataModel;
    @Inject
    private UserInputModel userInputModel;

    public VirusResult() {
        avgCostComponentCosts.set(FXCollections.observableMap(createEmptyEnumMap(CostComponent.class))) ;
        minCostComponentCosts.set(FXCollections.observableMap(createEmptyEnumMap(CostComponent.class)));
        maxCostComponentCosts.set(FXCollections.observableMap(createEmptyEnumMap(CostComponent.class)));


        avgInternalActivityCosts.set(FXCollections.observableMap(createEmptyEnumMap(InternalActivity.class)));
        minInternalActivityCosts.set(FXCollections.observableMap(createEmptyEnumMap(InternalActivity.class)));
        maxInternalActivityCosts.set(FXCollections.observableMap(createEmptyEnumMap(InternalActivity.class)));

        avgExternalConsequenceCosts.set(FXCollections.observableMap(createEmptyEnumMap(ExternalConsequence.class)));
        minExternalConsequenceCosts.set(FXCollections.observableMap(createEmptyEnumMap(ExternalConsequence.class)));
        maxExternalConsequenceCosts.set(FXCollections.observableMap(createEmptyEnumMap(ExternalConsequence.class)));

        avgAttackTypeCosts.set(FXCollections.observableMap(createEmptyEnumMap(AttackType.class)));
        minAttackTypeCosts.set(FXCollections.observableMap(createEmptyEnumMap(AttackType.class)));
        maxAttackTypeCosts.set(FXCollections.observableMap(createEmptyEnumMap(AttackType.class)));

    }

    public VirusResult(BaseDataModel baseDataModel, UserInputModel userInputModel) {
        this();
        this.baseDataModel = baseDataModel;
        this.userInputModel = userInputModel;

        initialize();
    }

    /**
     * Stellt sämtliche Bindings her.
     */
    @PostConstruct
    private void initialize() {

        IntegerProperty numberOfClients = userInputModel.numberOfClients();

        DoubleBinding activityFactor = Bindings.doubleValueAt(baseDataModel.securityGovernanceActivityDistributions(), userInputModel.securityGovernanceActivity());
        DoubleBinding sectorBase = Bindings.doubleValueAt(baseDataModel.attackCostPerSector(), userInputModel.sector());

        NumberBinding sectorFactor = Bindings.divide(sectorBase,baseDataModel.sectorAverageFactor());

        avgCostPerClient.bind(
                baseDataModel.avgCostBase()
                        .multiply(
                                MathBindings.pow(
                                        numberOfClients,
                                        baseDataModel.avgCostExponent()))
                        .multiply(activityFactor)
                        .multiply(sectorFactor));

        minCostPerClient.bind(
                baseDataModel.minCostBase()
                        .multiply(
                                MathBindings.pow(
                                        numberOfClients,
                                        baseDataModel.minCostExponent()))
                        .multiply(activityFactor)
                        .multiply(sectorFactor));

        maxCostPerClient.bind(
                baseDataModel.maxCostBase()
                        .multiply(
                                MathBindings.pow(
                                        numberOfClients,
                                        baseDataModel.maxCostExponent()))
                        .multiply(activityFactor)
                        .multiply(sectorFactor));


        avgCostTotal.bind(numberOfClients.multiply(avgCostPerClient));
        minCostTotal.bind(numberOfClients.multiply(minCostPerClient));
        maxCostTotal.bind(numberOfClients.multiply(maxCostPerClient));


        IntegerProperty selectedNumberOfClients = userInputModel.selectedNumberOfClientsInChart();
        avgCostSelected.bind(selectedNumberOfClients.multiply(avgCostPerClient));
        minCostSelected.bind(selectedNumberOfClients.multiply(minCostPerClient));
        maxCostSelected.bind(selectedNumberOfClients.multiply(maxCostPerClient));



        initCostComponentCosts(avgCostComponentCosts,avgCostSelected);
        initCostComponentCosts(minCostComponentCosts,minCostSelected);
        initCostComponentCosts(maxCostComponentCosts,maxCostSelected);

        initInternalActivityCosts(avgInternalActivityCosts,avgCostSelected);
        initInternalActivityCosts(minInternalActivityCosts,minCostSelected);
        initInternalActivityCosts(maxInternalActivityCosts, maxCostSelected);

        initExternalConsequencesCosts(avgExternalConsequenceCosts,avgCostSelected);
        initExternalConsequencesCosts(minExternalConsequenceCosts,minCostSelected);
        initExternalConsequencesCosts(maxExternalConsequenceCosts, maxCostSelected);

        initAttackTypeCosts(avgAttackTypeCosts,avgCostSelected);
        initAttackTypeCosts(minAttackTypeCosts,minCostSelected);
        initAttackTypeCosts(maxAttackTypeCosts, maxCostSelected);
    }

    private void initCostComponentCosts(MapProperty<CostComponent, DoubleProperty> map, DoubleProperty perClient) {
        for(Map.Entry<CostComponent,DoubleProperty> entry : map.entrySet()){
            CostComponent key = entry.getKey();
            DoubleProperty value = entry.getValue();

            value.bind(perClient.multiply(baseDataModel.costComponentDistributions().get(key)));
        }
    }

    private void initInternalActivityCosts(MapProperty<InternalActivity, DoubleProperty> map, DoubleProperty perClient) {

        Double directLaborFactor = baseDataModel.costComponentDistributions().get(CostComponent.DIRECT_LABOR);
        for(Map.Entry<InternalActivity,DoubleProperty> entry : map.entrySet()){
            InternalActivity key = entry.getKey();
            DoubleProperty value = entry.getValue();

            value.bind(perClient.multiply(directLaborFactor * baseDataModel.internalActivityDistributions().get(key)));
        }
    }

    private void initExternalConsequencesCosts(MapProperty<ExternalConsequence, DoubleProperty> map, DoubleProperty perClient) {
        Double productivityLossFactor = baseDataModel.costComponentDistributions().get(CostComponent.PRODUCTIVITY_LOSS);

        for(Map.Entry<ExternalConsequence,DoubleProperty> entry : map.entrySet()){
            ExternalConsequence key = entry.getKey();
            DoubleProperty value = entry.getValue();

            value.bind(perClient.multiply(
                            productivityLossFactor *
                            baseDataModel.externalConsequenceDistributions().get(key)));
        }
    }

    private void initAttackTypeCosts(MapProperty<AttackType, DoubleProperty> map, DoubleProperty perClient) {
        for(Map.Entry<AttackType,DoubleProperty> entry : map.entrySet()){
            AttackType key = entry.getKey();
            DoubleProperty value = entry.getValue();

            value.bind(perClient.multiply(baseDataModel.attackTypeDistributions().get(key)));
        }
    }


    public ReadOnlyDoubleProperty avgCostPerClient() {
        return avgCostPerClient;
    }

    public ReadOnlyDoubleProperty minCostPerClient() {
        return minCostPerClient;
    }

    public ReadOnlyDoubleProperty maxCostPerClient() {
        return maxCostPerClient;
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

    public ReadOnlyDoubleProperty avgCostSelected() {
        return avgCostSelected;
    }

    public ReadOnlyDoubleProperty minCostSelected() {
        return minCostSelected;
    }

    public ReadOnlyDoubleProperty maxCostSelected() {
        return maxCostSelected;
    }

    public ReadOnlyDoubleProperty getAvgCostComponentCost(
            CostComponent costCompontent) {
        return avgCostComponentCosts.get(costCompontent);
    }

    public ReadOnlyDoubleProperty getMinCostComponentCost(
            CostComponent costCompontent) {
        return minCostComponentCosts.get(costCompontent);
    }

    public ReadOnlyDoubleProperty getMaxCostComponentCost(
            CostComponent costCompontent) {
        return maxCostComponentCosts.get(costCompontent);
    }

    public ReadOnlyDoubleProperty getAvgInternalActivityCost(
            InternalActivity activity) {
        return avgInternalActivityCosts.get(activity);
    }

    public ReadOnlyDoubleProperty getMinInternalActivityCost(
            InternalActivity activity) {
        return minInternalActivityCosts.get(activity);
    }

    public ReadOnlyDoubleProperty getMaxInternalActivityCost(
            InternalActivity activity) {
        return maxInternalActivityCosts.get(activity);
    }

    public ReadOnlyDoubleProperty getAvgExternalConsequenceCost(
            ExternalConsequence consequence) {
        return avgExternalConsequenceCosts.get(consequence);
    }

    public ReadOnlyDoubleProperty getMinExternalConsequenceCost(
            ExternalConsequence consequence) {
        return minExternalConsequenceCosts.get(consequence);
    }

    public ReadOnlyDoubleProperty getMaxExternalConsequenceCost(
            ExternalConsequence consequence) {
        return maxExternalConsequenceCosts.get(consequence);
    }

    public ReadOnlyDoubleProperty getAvgAttackTypeCost(AttackType attacktype) {
        return avgAttackTypeCosts.get(attacktype);
    }

    public ReadOnlyDoubleProperty getMinAttackTypeCost(AttackType attacktype) {
        return minAttackTypeCosts.get(attacktype);
    }

    public ReadOnlyDoubleProperty getMaxAttackTypeCost(AttackType attacktype) {
        return maxAttackTypeCosts.get(attacktype);
    }
}