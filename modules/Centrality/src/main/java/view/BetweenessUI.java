package view;

import core.Beetweness;

import core.Resources;
import org.gephi.statistics.spi.Statistics;
import org.gephi.statistics.spi.StatisticsUI;
import org.openide.util.lookup.ServiceProvider;

import javax.swing.*;

/**
 * Created by Kasia on 31.05.2018.
 */
@ServiceProvider(service = StatisticsUI.class)
public class BetweenessUI implements StatisticsUI {

    private SettingsPanel settingsPanel;
    private Beetweness betweenness;

    @Override
    public JPanel getSettingsPanel() {
        settingsPanel = new SettingsPanel(Resources.SHORT_DESCRIPTION_BETWEENESS);
        return settingsPanel;
    }

    @Override
    public void setup(Statistics statistics) {
        if (statistics instanceof Beetweness){
            betweenness = (Beetweness) statistics;
        } else {
            throw new RuntimeException("Statistics is not an instance of BetweenessUI.");
        }
    }

    @Override
    public void unsetup() {
    }

    @Override
    public Class<? extends Statistics> getStatisticsClass() {
        return Beetweness.class;
    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return "Betweeness";
    }

    @Override
    public String getShortDescription() {
        return Resources.SHORT_DESCRIPTION_BETWEENESS;
    }

    @Override
    public String getCategory() {
        return StatisticsUI.CATEGORY_NETWORK_OVERVIEW;
    }

    @Override
    public int getPosition() {
        return 1000;
    }
}
