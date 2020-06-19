package mystudy.Enum;

public enum GradeColumn {
    MIDTERM("Mid-term grade"), FINAL_EXAM("Final exam grade"), OTHER("Other grade"), TOTAL("Total grade");

    private final String name;

    private GradeColumn(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}