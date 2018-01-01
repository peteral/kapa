package de.peteral.kapa.solver;

import de.peteral.kapa.domain.SubTask;
import de.peteral.kapa.domain.Task;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@Ignore
public class SubTaskDifficultyComparatorTest {

    @Mock
    private SubTask subtask1;
    @Mock
    private SubTask subtask2;
    @Mock
    private Task task1;
    @Mock
    private Task task2;
    private SubTaskDifficultyComparator comparator;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        comparator = new SubTaskDifficultyComparator();

        when(subtask1.getTask()).thenReturn(task1);
        when(subtask2.getTask()).thenReturn(task2);
    }

    @Test
    public void compareTo_ComparedTaskBlocksThisTask_ReturnsVeryEasy() {
        when(task1.isBlockedBy(task2)).thenReturn(true);

        assertEquals(Integer.MIN_VALUE, comparator.compare(subtask1, subtask2));
    }

    @Test
    public void compareTo_ThisTaskBlocksComparedTask_ReturnsVeryHard() {
        when(task2.isBlockedBy(task1)).thenReturn(true);

        assertEquals(Integer.MAX_VALUE, comparator.compare(subtask1, subtask2));
    }
}
