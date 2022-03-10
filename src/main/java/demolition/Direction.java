package demolition;

/**
 * This enum is used to represent the seven directions relevant to various
 * entities in this game. To understand how the methods in this enum work,
 * envision each direction as an arrow, with the horizontal and vertical
 * directions pointing in two opposite directions and the centre "direction"
 * being a single point without an arrowhead.
 */
public enum Direction {
    UP{
        public Direction rotateClockwise() {return RIGHT;};
        public Direction getAxis() {return VERTICAL;};
        public int getXOffset() {return 0;};
        public int getYOffset() {return -1;}
    },

    LEFT{
        public Direction rotateClockwise() {return UP;};
        public Direction getAxis() {return HORIZONTAL;};
        public int getXOffset() {return -1;};
        public int getYOffset() {return 0;}
    },

    DOWN{
        public Direction rotateClockwise() {return LEFT;};
        public Direction getAxis() {return VERTICAL;};
        public int getXOffset() {return 0;};
        public int getYOffset() {return 1;}
    },

    RIGHT{
        public Direction rotateClockwise() {return DOWN;};
        public Direction getAxis() {return HORIZONTAL;};
        public int getXOffset() {return 1;};
        public int getYOffset() {return 0;}
    },

    CENTRE{
        public Direction rotateClockwise() {return CENTRE;};
        public Direction getAxis() {return CENTRE;};
        public int getXOffset() {return 0;};
        public int getYOffset() {return 0;}
    },

    VERTICAL{
        public Direction rotateClockwise() {return HORIZONTAL;};
        public Direction getAxis() {return VERTICAL;};
        public int getXOffset() {return 0;};
        public int getYOffset() {return 0;}
    },

    HORIZONTAL{
        public Direction rotateClockwise() {return VERTICAL;};
        public Direction getAxis() {return HORIZONTAL;};
        public int getXOffset() {return 0;};
        public int getYOffset() {return 0;}
    };

    /**
     * @return The direction created when this direction is rotated clockwise.
     */
    public abstract Direction rotateClockwise();
    /**
     * @return The axis along which this direction lies.
     */
    public abstract Direction getAxis();
    /**
     * @return The offset along the x-axis from moving in this direction.
     */
    public abstract int getXOffset();
    /**
     * @return The offset along the y-axis from moving in this direction.
     */
    public abstract int getYOffset();
}
