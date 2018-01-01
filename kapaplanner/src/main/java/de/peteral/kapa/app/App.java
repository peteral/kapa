package de.peteral.kapa.app;

import de.peteral.kapa.domain.Schedule;
import de.peteral.kapa.solver.SolverUtils;
import de.peteral.kapa.view.Renderer;
import de.peteral.kapa.view.Visualization;
import de.peteral.kapa.xstream.Loader;
import de.peteral.kapa.xstream.Simulation;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.XmlType;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    private static final String[] DEFAULT_PARAMETERS = {
            "config/teams-1.xml",
            "config/projects-1.xml",
            "kapaplanner/target/output.svg"
    };

    public static void main(String... params) throws IOException, URISyntaxException {
        if (params.length != DEFAULT_PARAMETERS.length)
            printUsage();

        SolverFactory<Schedule> factory = SolverFactory.createFromXmlResource("de/peteral/kapa/solver/solverConfig.xml");
        Solver<Schedule> solver = factory.buildSolver();

        Schedule unsolvedSchedule = getSchedule(params);

        LOGGER.info("Unsolved schedule: " + unsolvedSchedule);

        Schedule solvedSchedule = solver.solve(unsolvedSchedule);

        Visualization visualization = new Visualization(solvedSchedule);
        LOGGER.info("Solved schedule: " + visualization);

        Renderer renderer = new Renderer(solvedSchedule, solver);
        String svg = renderer.render();
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(getParameter(params, 2)))) {
            writer.write(svg);
        }
    }

    private static Schedule getSchedule(String[] params) {
        Schedule unsolvedSchedule;
        if (params.length >= 1 && "-s".equalsIgnoreCase(params[0])) {
            unsolvedSchedule = Simulation.simulate();
        }
        else {
            unsolvedSchedule = Loader.load(
                    getParameter(params, 0),
                    getParameter(params, 1));
        }

        SolverUtils.injectSprints(
                unsolvedSchedule.getSprints().stream()
                        .map(sprint -> sprint.getName())
                        .distinct()
                        .sorted()
                        .collect(Collectors.toList())
        );
        return unsolvedSchedule;
    }


    private static void printUsage() {
        System.out.println("Usage: <teams.xml> <projects.xml> <output.svg>");
        System.out.print("\tNot enough parameters provided, using defaults for missing (");
        System.out.print(Arrays.stream(DEFAULT_PARAMETERS).collect(Collectors.joining(" ")));
        System.out.println(")");
    }

    private static String getParameter(String[] params, int index) {
        if (params.length - 1 < index)
            return DEFAULT_PARAMETERS[index];

        return params[index];
    }
}
