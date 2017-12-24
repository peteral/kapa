package de.peteral.kapa.solver;

import de.peteral.kapa.domain.Team;
import org.apache.commons.lang3.builder.CompareToBuilder;

import java.util.Comparator;

public class TeamStrengthComparator implements Comparator<Team> {
    public int compare(Team a, Team b) {
        return new CompareToBuilder()
                .append(a.getSkills().size(), b.getSkills().size())
                .toComparison();
    }
}
