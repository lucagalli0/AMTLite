package com.edqueeneland.amtlite;

class Stop {
    private int id;
    private String name;
    private int bNumber;
    private int stopId;

    Stop(int id, String name, int bNumber, int stopId) {
        this.id = id;

        this.name = name;
        this.bNumber = bNumber;
        this.stopId = stopId;
    }

    public Stop(String name, int bNumber) {
        this.name = name;
        this.bNumber = bNumber;
    }

    public Stop(int id, String name, int bNumber) {
        this.id=id;
        this.name=name;
        this.bNumber=bNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    int getbNumber() {
        return bNumber;
    }

    public void setbNumber(int bNumber) {
        this.bNumber = bNumber;
    }

    int getStopId() {
        return stopId;
    }

    public void setStopId(int stopId) {
        this.stopId = stopId;
    }
}
