package de.hszg.datenpannen.dataloss.model;

import de.hszg.datenpannen.dataloss.data.StaticDatalossDataProvider;
import org.junit.Before;
import org.junit.Test;

import static de.hszg.datenpannen.dataloss.model.CostDistribution.*;
import static de.hszg.datenpannen.dataloss.model.InfluencingFactor.*;
import static eu.lestard.assertj.javafx.api.Assertions.*;
import static org.assertj.core.data.Offset.*;

/**
 * Dieser Test überprüft die Gesamtberechnung.
 */
public class DatalossCalculationScenarioTest {

    private DatalossResult result;

    private BaseDataModel baseDataModel;
    private UserinputModel userinputModel;

    @Before
    public void setup() {
        StaticDatalossDataProvider baseDataProvider = new StaticDatalossDataProvider();

        baseDataModel = new BaseDataModel(baseDataProvider);
        userinputModel = new UserinputModel();

        result = new DatalossResult(baseDataModel, userinputModel);
    }
    
    @Test
    public void testScenarioKleinesEnergieunternehmen() {

        userinputModel.numberOfDatasets().set(512);
        userinputModel.sector().set(Sector.ENERGY);
        userinputModel.influencingFactors().add(THIRD_PARTY_ERROR);
        userinputModel.influencingFactors().add(QUICK_NOTIFICATION);
        userinputModel.influencingFactors().add(CISO_APPOINTMENT);
        userinputModel.influencingFactors().add(STRONG_SECURITY_POSTURE);


        assertThat(result.avgCostPerDataset()).hasValue(203);
        assertThat(result.avgCostPerDataset()).hasValue(203);
        assertThat(result.minCostPerDataset()).hasValue(108.490066, offset(0.001));
        assertThat(result.maxCostPerDataset()).hasValue(334.781457, offset(0.001));

        assertThat(result.avgCostTotal()).hasValue(103936.00, offset(0.01));
        assertThat(result.minCostTotal()).hasValue(55546.91, offset(0.01));
        assertThat(result.maxCostTotal()).hasValue(171408.11, offset(0.01));

        assertDistribution(result, 36.89, 69.02, 113.83, INVESTIGATIONS_AND_FORENSICS);
        assertDistribution(result, 31.46, 58.87, 97.09, LOST_CUSTOMER_BUSINESS);
        assertDistribution(result, 9.76, 18.27, 30.13, AUDIT_AND_CONSULTING_SERVICES);
        assertDistribution(result, 7.59, 14.21, 23.43, OUTBOUND_CONTACT_COSTS);
        assertDistribution(result, 5.42, 10.15, 16.74, LEGAL_SERVICES_COMPLIANCE);
        assertDistribution(result, 5.42, 10.15, 16.74, CUSTOMER_ACQUISITION_COST);
        assertDistribution(result, 4.34, 8.12, 13.39, INBOUND_CONTACT_COSTS);
        assertDistribution(result, 4.34, 8.12, 13.39, LEGAL_SERVICES_DEFENSE);
        assertDistribution(result, 2.17, 4.06, 6.70, FREE_OR_DISCOUNTED_SERVICES);
        assertDistribution(result, 1.08, 2.03, 3.35, IDENTITY_PROTECTION_SERVICES);
        assertDistribution(result, 0.0, 0.0, 0.0, PUBLIC_RELATIONS_COMMUNICATIONS);
    }

    @Test
    public void testScenarioMittleresTechnologieUnternehmen(){
    	userinputModel.numberOfDatasets().set(5000000);
    	userinputModel.sector().set(Sector.TECHNOLOGY);
    	userinputModel.influencingFactors().add(LOST_OR_STOLEN_DEVICES);
    	userinputModel.influencingFactors().add(CONSULTANTS_ENGAGED);
    	userinputModel.influencingFactors().add(INCIDENT_MANAGEMENT_PLAN);
    	
    	assertThat(result.avgCostPerDataset()).hasValue(115);
    	assertThat(result.minCostPerDataset()).hasValue(58.5761589, offset(0.001));
    	assertThat(result.maxCostPerDataset()).hasValue(193.675497, offset(0.001));
    	
    	assertThat(result.avgCostTotal()).hasValue(575000000.00, offset(0.01));
    	assertThat(result.minCostTotal()).hasValue(292880794.70, offset(0.01));
    	assertThat(result.maxCostTotal()).hasValue(968377483.44, offset(0.01));
    	
    	/*
    	 * Da im Scenario-Test kleines Energieunternehmen bereits alle Distributionen getestet wurden,
    	 * werden in diesem Test nur Stichprobenartig die Werte auf ihre Korrektheit geprüft.
    	 */
    	
    	assertDistribution(result, 19.92, 39.10, 65.85, INVESTIGATIONS_AND_FORENSICS);
    	assertDistribution(result, 4.10, 8.05, 13.56, OUTBOUND_CONTACT_COSTS);
    	assertDistribution(result, 2.93, 5.75,9.68, LEGAL_SERVICES_COMPLIANCE);
    	assertDistribution(result, 1.17, 2.30, 3.87, FREE_OR_DISCOUNTED_SERVICES);
    	assertDistribution(result, 0.59, 1.15, 1.94, IDENTITY_PROTECTION_SERVICES);
    }
    
    @Test
    public void testScenarioGrossesFinanzunternehmen(){
    	userinputModel.numberOfDatasets().set(2000000000);
    	userinputModel.sector().set(Sector.FINANCIAL);
    	userinputModel.influencingFactors().add(THIRD_PARTY_ERROR);
    	userinputModel.influencingFactors().add(LOST_OR_STOLEN_DEVICES);
    	userinputModel.influencingFactors().add(QUICK_NOTIFICATION);
    	
    	assertThat(result.avgCostPerDataset()).hasValue(243);
    	assertThat(result.minCostPerDataset()).hasValue(140.966887, offset(0.001));
    	assertThat(result.maxCostPerDataset()).hasValue(385.271523, offset(0.001));
    	
    	assertThat(result.avgCostTotal()).hasValue(486000000000.00, offset(0.01));
    	assertThat(result.minCostTotal()).hasValue(281933774834.44, offset(0.01));
    	assertThat(result.maxCostTotal()).hasValue(770543046357.62, offset(0.01));
    	
    	/*
    	 * Da im Scenario-Test kleines Energieunternehmen bereits alle Distributionen getestet wurden,
    	 * werden in diesem Test nur Stichprobenartig die Werte auf ihre Korrektheit geprüft.
    	 */
    	
    	assertDistribution(result, 40.88, 70.47, 111.73, LOST_CUSTOMER_BUSINESS);
    	assertDistribution(result, 12.69, 21.87, 34.67, AUDIT_AND_CONSULTING_SERVICES);
    	assertDistribution(result, 5.64, 9.72, 15.41, INBOUND_CONTACT_COSTS);
    	assertDistribution(result, 0.00, 0.00, 0.00, PUBLIC_RELATIONS_COMMUNICATIONS);
    }
    
    @Test
    public void testScenarioMiniPublicSector(){
    	userinputModel.numberOfDatasets().set(50);
    	userinputModel.numberOfLostDatasets().set(25);
    	userinputModel.sector().set(Sector.PUBLIC_SECTOR);
    	userinputModel.influencingFactors().add(CONSULTANTS_ENGAGED);
    	userinputModel.influencingFactors().add(CISO_APPOINTMENT);
    	userinputModel.influencingFactors().add(INCIDENT_MANAGEMENT_PLAN);
    	userinputModel.influencingFactors().add(STRONG_SECURITY_POSTURE);
    	
    	assertThat(result.avgCostPerDataset()).hasValue(64);
    	assertThat(result.minCostPerDataset()).hasValue(20.2715232, offset(0.001));
    	assertThat(result.maxCostPerDataset()).hasValue(124.97351, offset(0.001));
    	
    	assertThat(result.avgCostTotal()).hasValue(3200.00, offset(0.01));
    	assertThat(result.minCostTotal()).hasValue(1013.58, offset(0.01));
    	assertThat(result.maxCostTotal()).hasValue(6248.68, offset(0.01));
    	
    	assertThat(result.avgCostSelected()).hasValue(1600.00, offset(0.01));
    	assertThat(result.minCostSelected()).hasValue(506.79, offset(0.01));
    	assertThat(result.maxCostSelected()).hasValue(3124.34, offset(0.01));
    	
    	/*
    	 * Da im Scenario-Test kleines Energieunternehmen bereits alle Distributionen getestet wurden,
    	 * werden in diesem Test nur Stichprobenartig die Werte auf ihre Korrektheit geprüft.
    	 */
    	
    	assertDistribution(result, 6.89, 21.76, 42.49, INVESTIGATIONS_AND_FORENSICS);
    	assertDistribution(result, 1.82, 5.76, 11.25, AUDIT_AND_CONSULTING_SERVICES);
    	assertDistribution(result, 0.41, 1.28, 2.50, FREE_OR_DISCOUNTED_SERVICES);
    	assertDistribution(result, 0.20, 0.64, 1.25, IDENTITY_PROTECTION_SERVICES);
    	
    	assertThat(result.getSelectedDistributionCost(INVESTIGATIONS_AND_FORENSICS)).hasValue(544.00, offset(0.01));
    	assertThat(result.getSelectedDistributionCost(LOST_CUSTOMER_BUSINESS)).hasValue(464.00, offset(0.01));
    	assertThat(result.getSelectedDistributionCost(AUDIT_AND_CONSULTING_SERVICES)).hasValue(144.00, offset(0.01));
    	assertThat(result.getSelectedDistributionCost(FREE_OR_DISCOUNTED_SERVICES)).hasValue(32.00, offset(0.01));
    	
    }
    
    private void assertDistribution(DatalossResult result, double min, double avg, double max, CostDistribution distribution) {
        assertThat(result.getMinDistributionCost(distribution)).hasValue(min, offset(0.01));
        assertThat(result.getAvgDistributionCost(distribution)).hasValue(avg, offset(0.01));
        assertThat(result.getMaxDistributionCost(distribution)).hasValue(max, offset(0.01));
    }
}
