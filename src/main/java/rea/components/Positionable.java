package rea.components;


public abstract class Positionable extends Component {

    Position position;

    public Positionable(Visual visual, String description) {
        super(visual, description);
        position = null;
    }

    public Position getPosition() {
        return position;
    }

    public void moveTo(Position targetPosition) {
        if (position == null) {
            position = new Position(targetPosition.getX(), targetPosition.getY());
        } else {
            position.x = targetPosition.getX();
            position.y = targetPosition.getY();
        }
    }
}
