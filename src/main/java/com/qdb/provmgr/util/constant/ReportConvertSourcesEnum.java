package com.qdb.provmgr.util.constant;

/**
 * Created by wenzhong on 2017/2/21.
 */
public enum ReportConvertSourcesEnum {

    // 1_6   1_6_2   1_9   1_9_2   1_11   1_12  需要转置

    //账户表
    TAB_1_1("1_1", 1, 6, 1, 14, 22, 45),
    TAB_1_2_1("1_2_1", 1, 6, 1, 12, 9, 43),
    TAB_1_6("1_6 ", 1, 6, 1, 10, 32, 36),
    TAB_1_9("1_9", 1, 6, 1, 12, 32, 13),
    TAB_1_10("1_10", 1, 6, 1, 12, 24, 43),

    //汇总表
    TAB_1_1_2("1_1_2", -1, -1, 1, 9, 22, 40),
    TAB_1_2("1_2", -1, -1, 1, 7, 9, 38),
    TAB_1_3("1_3", -1, -1, 0, 4, 34, 41),
    TAB_1_4("1_4", -1, -1, 1, 4, 4, 35),
    TAB_1_5("1_5", -1, -1, 1, 6, 6, 35),
    TAB_1_6_2("1_6_2", -1, -1, 1, 5, 32, 31),
    TAB_1_7("1_7", -1, -1, -1, -1, -1, -1),
    TAB_1_8("1_8", -1, -1, -1, -1, -1, -1),
    TAB_1_9_2("1_9_2", -1, -1, 1, 5, 32, 8),
    TAB_1_10_2("1_10_2", -1, -1, 1, 7, 24, 38),
    TAB_1_11("1_11", -1, -1, 1, 4, 32, 34),
    TAB_1_12("1_12", -1, -1, 1, 4, 32, 23),
    TAB_1_13("1_13", -1, -1, 1, 5, 6, 36);

    private String tabNo;
    private int actCol;
    private int actRow;
    private int startCol;
    private int startRow;
    private int endCol;
    private int endRow;

    ReportConvertSourcesEnum(String tabNo, int actCol, int actRow, int startCol, int startRow, int endCol, int endRow) {
        this.tabNo = tabNo;
        this.actCol = actCol;
        this.actRow = actRow;
        this.startCol = startCol;
        this.endCol = endCol;
        this.startRow = startRow;
        this.endRow = endRow;
    }

    public String getTabNo() {
        return tabNo;
    }

    public void setTabNo(String tabNo) {
        this.tabNo = tabNo;
    }

    public int getActCol() {
        return actCol;
    }

    public void setActCol(int actCol) {
        this.actCol = actCol;
    }

    public int getActRow() {
        return actRow;
    }

    public void setActRow(int actRow) {
        this.actRow = actRow;
    }

    public int getStartCol() {
        return startCol;
    }

    public void setStartCol(int startCol) {
        this.startCol = startCol;
    }

    public int getEndCol() {
        return endCol;
    }

    public void setEndCol(int endCol) {
        this.endCol = endCol;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }
}
