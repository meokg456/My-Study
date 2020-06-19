package mystudy.Enum;

public enum RequestStatus {
    SENT("Sent"), NO_UPDATED("No updated"), UPDATED("Updated");

    private final String name;

    private RequestStatus(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}