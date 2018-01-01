package de.peteral.kapa.domain;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class TaskTest {

    @Mock
    private Task other;
    private Task task;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        task = new Task();
    }

    @Test
    public void isBlockedBy_TaskIsDirectBlocker_ReturnsTrue() {
        task.setPreviousTasks(Arrays.asList(other));

        assertTrue(task.isBlockedBy(other));
    }

    @Test
    public void isBlockedBy_EmptyList_ReturnsFalse() {
        task.setPreviousTasks(Collections.emptyList());

        assertFalse(task.isBlockedBy(other));
    }

    @Test
    public void isBlockedBy_PreviousTaskBlockedByOther_ReturnsTrue() {
        Task previous = mock(Task.class);
        when(previous.isBlockedBy(other)).thenReturn(true);

        task.setPreviousTasks(Arrays.asList(previous));

        assertTrue(task.isBlockedBy(other));
    }

    @Test
    public void isBlockedBy_ListIsNull_ReturnsFalse() {
        assertFalse(task.isBlockedBy(other));
    }

    @Test
    public void generateSubtasks_WorkIs25_Generates3TasksWithCorrectSize() {
        task.setWork(25);

        task.generateSubTasks();

        assertEquals(
                Arrays.asList(10L, 10L, 5L),
                task.getSubtasks().stream()
                    .map(subTask -> (Long)subTask.getWork())
                    .collect(Collectors.toList())
        );
    }

    @Test
    public void generateSubtasks_WithMaxVelocity_GeneratesCorrectTasks() {
        task.setWork(25);
        task.setMaxVelocity(5);

        task.generateSubTasks();

        assertEquals(
                Arrays.asList(5L, 5L, 5L, 5L, 5L),
                task.getSubtasks().stream()
                        .map(subTask -> (Long)subTask.getWork())
                        .collect(Collectors.toList())
        );
    }
}
