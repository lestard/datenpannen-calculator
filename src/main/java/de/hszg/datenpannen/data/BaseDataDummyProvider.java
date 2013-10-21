package de.hszg.datenpannen.data;

import de.hszg.datenpannen.model.CostDistribution;
import de.hszg.datenpannen.model.InfluencingFactor;
import de.hszg.datenpannen.model.Sector;
import java.util.EnumMap;
import java.util.Map;

/**
 * Dummy Implementation des BaseDataProvider.
 */
public class BaseDataDummyProvider implements BaseDataProvider {

    private Map<Sector, Double> lossCostPerSector = new EnumMap<>(Sector.class);
    private Map<InfluencingFactor, Double> factorValues = new EnumMap<>(InfluencingFactor.class);
    private Map<CostDistribution, Double> costDistributions = new EnumMap<>(CostDistribution.class);

    public BaseDataDummyProvider() {
        initLossCostPerSector();
        initFactorValues();
        initCostDistributions();
    }

    private void initLossCostPerSector() {
        lossCostPerSector.put(Sector.COMMUNICATIONS, 119.0);
        lossCostPerSector.put(Sector.CONSUMER, 161.0);
        lossCostPerSector.put(Sector.ENERGY, 201.0);
        lossCostPerSector.put(Sector.FINANCIAL, 217.0);
        lossCostPerSector.put(Sector.HOSPITALITY, 141.0);
        lossCostPerSector.put(Sector.INDUSTRIAL, 214.0);
        lossCostPerSector.put(Sector.PHARMACEUTICAL, 114.0);
        lossCostPerSector.put(Sector.PUBLIC_SECTOR, 93.0);
        lossCostPerSector.put(Sector.RETAIL, 126.0);
        lossCostPerSector.put(Sector.SERVICES, 134.0);
        lossCostPerSector.put(Sector.TECHNOLOGY, 120.0);
    }

    private void initFactorValues() {
        factorValues.put(InfluencingFactor.CISO_APPOINTMENT, -5.0);
        factorValues.put(InfluencingFactor.CONSULTANTS_ENGAGED, -4.0);
        factorValues.put(InfluencingFactor.INCIDENT_MANAGEMENT_PLAN, -9.0);
        factorValues.put(InfluencingFactor.LOST_OR_STOLEN_DEVICES, 8.0);
        factorValues.put(InfluencingFactor.QUICK_NOTIFICATION, 6.0);
        factorValues.put(InfluencingFactor.STRONG_SECURITY_POSTURE, -11.0);
        factorValues.put(InfluencingFactor.THIRD_PARTY_ERROR, 12.0);
    }

    private void initCostDistributions() {
        costDistributions.put(CostDistribution.INVESTIGATIONS_AND_FORENSICS, .34);
        costDistributions.put(CostDistribution.LOST_CUSTOMER_BUSINESS, .29);
        costDistributions.put(CostDistribution.AUDIT_AND_CONSULTING_SERVICES, .09);
        costDistributions.put(CostDistribution.OUTBOUND_CONTACT_COSTS, .07);
        costDistributions.put(CostDistribution.LEGAL_SERVICES_COMPLIANCE, .05);
        costDistributions.put(CostDistribution.CUSTOMER_ACQUISITION_COST, .05);
        costDistributions.put(CostDistribution.INBOUND_CONTACT_COSTS, .04);
        costDistributions.put(CostDistribution.LEGAL_SERVICES_DEFENSE, .04);
        costDistributions.put(CostDistribution.FREE_OR_DISCOUNTED_SERVICES, .02);
        costDistributions.put(CostDistribution.IDENTITY_PROTECTION_SERVICES, .01);
        costDistributions.put(CostDistribution.PUBLIC_RELATIONS_COMMUNICATIONS, .0);
    }

    @Override
    public double getMinLossCost() {
        return 80.0;
    }

    @Override
    public double getMaxLossCost() {
        return 250.0;
    }

    @Override
    public double getAvgLossCost() {
        return 151.0;
    }

    @Override
    public double getValueOf(InfluencingFactor factor) {
        Double value = factorValues.get(factor);
        return checkValue(value);
    }

    @Override
    public double getLossCostOf(Sector sector) {
        Double value = lossCostPerSector.get(sector);
        return checkValue(value);
    }

    @Override
    public double getPercentageOf(CostDistribution distribution) {
        Double value = costDistributions.get(distribution);
        return checkValue(value);
    }

    private double checkValue(Double value) {
        if (value == null) {
            return Double.NaN;
        } else {
            return value;
        }
    }
}
