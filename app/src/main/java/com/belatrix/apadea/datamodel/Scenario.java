package com.belatrix.apadea.datamodel;

public class Scenario {

    private String scenarioName;
    private String firstStatement;
    private String secondStatement;
    private String thirdStatement;
    private String fourthStatement;
    private String leftPersonName;
    private String leftPersonPicture;
    private String rightPersonName;
    private String rightPersonPicture;

    public String getRightPersonPicture() {
        return rightPersonPicture;
    }

    public void setRightPersonPicture(String rightPersonPicture) {
        this.rightPersonPicture = rightPersonPicture;
    }

    public String getRightPersonName() {
        return rightPersonName;
    }

    public void setRightPersonName(String rightPersonName) {
        this.rightPersonName = rightPersonName;
    }

    public String getLeftPersonPicture() {
        return leftPersonPicture;
    }

    public void setLeftPersonPicture(String leftPersonPicture) {
        this.leftPersonPicture = leftPersonPicture;
    }

    public String getLeftPersonName() {
        return leftPersonName;
    }

    public void setLeftPersonName(String leftPersonName) {
        this.leftPersonName = leftPersonName;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    public String getFirstStatement() {
        return firstStatement;
    }

    public void setFirstStatement(String firstStatement) {
        this.firstStatement = firstStatement;
    }

    public String getSecondStatement() {
        return secondStatement;
    }

    public void setSecondStatement(String secondStatement) {
        this.secondStatement = secondStatement;
    }

    public String getThirdStatement() {
        return thirdStatement;
    }

    public void setThirdStatement(String thirdStatement) {
        this.thirdStatement = thirdStatement;
    }

    public String getFourthStatement() {
        return fourthStatement;
    }

    public void setFourthStatement(String fourthStatement) {
        this.fourthStatement = fourthStatement;
    }
}
