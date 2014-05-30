enum DriveState {
  forward,
  backward,
  left,
  right,
  none
};

DriveState getStateFromButtons();
DriveState getStateFromPi();
