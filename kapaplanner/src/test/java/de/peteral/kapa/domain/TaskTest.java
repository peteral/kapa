package de.peteral.kapa.domain;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;

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
}
