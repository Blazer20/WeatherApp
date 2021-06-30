package com.example.weatherapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StateViewModel extends ViewModel {

    public enum MotionState {
        IDLE, START
    }

    private final MutableLiveData<MotionState> motionState = new MutableLiveData<>();

    public StateViewModel() {
        motionState.setValue(MotionState.IDLE);
    }

    public void setMotionState(MotionState state) {
        motionState.setValue(state);
    }

    public LiveData<MotionState> getMotionState() {
        return motionState;
    }

}
