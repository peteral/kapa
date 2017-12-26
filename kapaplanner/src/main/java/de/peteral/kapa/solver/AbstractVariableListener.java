package de.peteral.kapa.solver;

import org.optaplanner.core.impl.domain.variable.listener.VariableListener;
import org.optaplanner.core.impl.score.director.ScoreDirector;

public abstract class AbstractVariableListener<T> implements VariableListener<T> {

    protected abstract void updateProperty(ScoreDirector scoreDirector, T source);

    @Override
    public void beforeEntityAdded(ScoreDirector scoreDirector, T source) {
        updateProperty(scoreDirector, source);
    }

    @Override
    public void afterEntityAdded(ScoreDirector scoreDirector, T source) {
        updateProperty(scoreDirector, source);
    }

    @Override
    public void beforeVariableChanged(ScoreDirector scoreDirector, T source) {
        updateProperty(scoreDirector, source);
    }

    @Override
    public void afterVariableChanged(ScoreDirector scoreDirector, T source) {
        updateProperty(scoreDirector, source);
    }

    @Override
    public void beforeEntityRemoved(ScoreDirector scoreDirector, T source) {
        updateProperty(scoreDirector, source);
    }

    @Override
    public void afterEntityRemoved(ScoreDirector scoreDirector, T source) {
        updateProperty(scoreDirector, source);
    }
}
