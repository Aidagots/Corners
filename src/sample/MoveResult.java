package sample;


public class MoveResult {
    private MoveType type;

    public MoveType getType() {
        return type;
    }


    MoveResult(MoveType type) {
        this(type, null);
    }

    private MoveResult(MoveType type, Saber saber) {
        this.type = type;
    }
}