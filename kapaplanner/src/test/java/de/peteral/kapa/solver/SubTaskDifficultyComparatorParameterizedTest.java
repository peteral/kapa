package de.peteral.kapa.solver;

import de.peteral.kapa.domain.Project;
import de.peteral.kapa.domain.SubTask;
import de.peteral.kapa.domain.Task;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

import java.util.Arrays;

@RunWith(Parameterized.class)
public class SubTaskDifficultyComparatorParameterizedTest {

    private final String due1;
    private final int cost1;
    private final String due2;
    private final int cost2;
    private final int expected;

    @Mock
    private SubTask subtask1;
    @Mock
    private SubTask subtask2;
    @Mock
    private Task task1;
    @Mock
    private Task task2;
    @Mock
    private Project project1;
    @Mock
    private Project project2;

    @Parameterized.Parameters(name= "{index}: {0}/{1} vs {2}/{3} = {4}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {null, 0, null, 0, 0},
                {"x", 0, null, 0, Integer.MAX_VALUE},
                {null, 0, "x", 0, Integer.MIN_VALUE},
                {"a", 0, "b", 0, 1},
                {"b", 0, "a", 0, -1},
                {"a", 0, "a", 0, 0},
                {"a", 0, "a", 1, -1},
        });
    }

    public SubTaskDifficultyComparatorParameterizedTest(String due1, int cost1, String due2, int cost2, int expected) {
        this.due1 = due1;
        this.cost1 = cost1;
        this.due2 = due2;
        this.cost2 = cost2;
        this.expected = expected;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(subtask1.getTask()).thenReturn(task1);
        when(subtask2.getTask()).thenReturn(task2);
        when(task1.getProject()).thenReturn(project1);
        when(task2.getProject()).thenReturn(project2);
    }

    @Test
    public void compareTo_Parameterized_ReturnsCorrectResult() {
        when(project1.getDue()).thenReturn(due1);
        when(project2.getDue()).thenReturn(due2);
        when(project1.getCostsOfDelay()).thenReturn(cost1);
        when(project2.getCostsOfDelay()).thenReturn(cost2);

        Assert.assertEquals(expected, new SubTaskDifficultyComparator().compare(subtask1, subtask2));
    }
}
