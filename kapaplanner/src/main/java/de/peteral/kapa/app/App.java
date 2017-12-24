package de.peteral.kapa.app;

import de.peteral.kapa.domain.Schedule;
import de.peteral.kapa.xstream.XStreamFactory;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String... params) {
        SolverFactory<Schedule> factory = SolverFactory.createFromXmlResource("de/peteral/kapa/solver/solverConfig.xml");
        Solver<Schedule> solver = factory.buildSolver();

        Schedule unsolvedSchedule = (Schedule) XStreamFactory.create().fromXML(App.class.getResource("../solver/dataset-1.xml"));
        // hack - empty collections not initialized by XStream, but needed by OptaPlanner
        unsolvedSchedule.getTeams().stream().forEach(team -> team.setTasks(Collections.emptyList()));

        LOGGER.info("Unsolved schedule: " + unsolvedSchedule);

        Schedule solvedSchedule = solver.solve(unsolvedSchedule);

        LOGGER.info("Solved schedule: " + solvedSchedule);
    }
}
