package view;

import core.Closeness;
import core.Resources;
import org.gephi.statistics.spi.Statistics;
import org.gephi.statistics.spi.StatisticsUI;
import org.openide.util.lookup.ServiceProvider;

import javax.swing.*;

@ServiceProvider(service = StatisticsUI.class)
public class ClosenessUI implements StatisticsUI {

    private SettingsPanel settingsPanel;
    private Closeness betweenness;

    @Override
    public JPanel getSettingsPanel() {
        settingsPanel = new SettingsPanel(Resources.SHORT_DESCRIPTION_CLOSENESS);
        return settingsPanel;
    }

    @Override
    public void setup(Statistics statistics) {
        if (statistics instanceof Closeness){
            betweenness = (Closeness) statistics;
        } else {
            throw new RuntimeException("Statistics is not an instance of ClosenessUI.");
        }
    }

    @Override
    public void unsetup() {
    }

    @Override
    public Class<? extends Statistics> getStatisticsClass() {
        return Closeness.class;
    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return Resources.NAME_CLOSENESS;
    }

    @Override
    public String getShortDescription() {
        return Resources.SHORT_DESCRIPTION_CLOSENESS;
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
